package io.vertx.ext.unit.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.unit.TestCompletion;
import io.vertx.ext.unit.report.TestSuiteReport;
import io.vertx.ext.unit.report.Reporter;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReporterHandler implements Handler<TestSuiteReport>, TestCompletion {

  private final Reporter[] reporters;
  private CompletableFuture<Void> completable = new CompletableFuture<>();
  private final AtomicReference<Throwable> failure = new AtomicReference<>();

  public ReporterHandler(Reporter... reporters) {
    this.reporters = reporters;
  }

  @Override
  public void resolve(Future future) {
    completable.whenComplete((done, err) -> {
      if (err != null) {
        future.fail(err);
      } else {
        future.complete();
      }
    });
  }

  @Override
  public boolean isCompleted() {
    return completable.isDone();
  }

  @Override
  public boolean isSucceeded() {
    return isCompleted() && !isFailed();
  }

  @Override
  public boolean isFailed() {
    return completable.isCompletedExceptionally();
  }

  @Override
  public void handler(Handler<AsyncResult<Void>> completionHandler) {
    Future<Void> completion = Future.future();
    completion.setHandler(completionHandler);
    resolve(completion);
  }

  @Override
  public void handle(TestSuiteReport testsuite) {
    Object[] reports = new Object[reporters.length];
    for (int i = 0;i < reporters.length;i++) {
      reports[i] = reporters[i].reportBeginTestSuite(testsuite.name());
    }
    testsuite.handler(testcase -> {
      for (int i = 0;i < reporters.length;i++) {
        reporters[i].reportBeginTestCase(reports[i], testcase.name());
      }
      testcase.endHandler(result -> {
        if (result.failed()) {
          failure.compareAndSet(null, result.failure().cause());
        }
        for (int i = 0;i < reporters.length;i++) {
          reporters[i].reportEndTestCase(reports[i], testcase.name(), result);
        }
      });
    });
    AtomicReference<Throwable> err = new AtomicReference<>();
    testsuite.exceptionHandler(t -> {
      failure.compareAndSet(null, t);
      err.set(t);
      for (int i = 0;i < reporters.length;i++) {
        reporters[i].reportError(reports[i], t);
      }
    });
    testsuite.endHandler(v -> {
      for (int i = 0;i < reporters.length;i++) {
        reporters[i].reportEndTestSuite(reports[i]);
      }
      if (failure.get() != null) {
        completable.completeExceptionally(failure.get());
      } else {
        completable.complete(null);
      }
    });
  }

  @Override
  public void await() {
    try {
      completable.get();
    } catch (ExecutionException ignore) {
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      Helper.uncheckedThrow(e);
    }
  }

  @Override
  public void await(long timeoutMillis) {
    try {
      completable.get(timeoutMillis, TimeUnit.MILLISECONDS);
    } catch (ExecutionException ignore) {
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      Helper.uncheckedThrow(e);
    } catch (TimeoutException e) {
      Helper.uncheckedThrow(new TimeoutException("Timed out"));
    }
  }

  @Override
  public void awaitSuccess() {
    try {
      completable.get();
    } catch (ExecutionException result) {
      Helper.uncheckedThrow(result.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      Helper.uncheckedThrow(e);
    }
  }

  @Override
  public void awaitSuccess(long timeoutMillis) {
    try {
      completable.get(timeoutMillis, TimeUnit.MILLISECONDS);
    } catch (ExecutionException result) {
      Helper.uncheckedThrow(result.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      Helper.uncheckedThrow(e);
    } catch (TimeoutException e) {
      Helper.uncheckedThrow(new TimeoutException("Timed out"));
    }
  }
}
