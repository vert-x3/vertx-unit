package io.vertx.ext.unit.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.unit.TestCompletion;
import io.vertx.ext.unit.report.TestSuiteReport;
import io.vertx.ext.unit.report.Reporter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReporterHandler implements Handler<TestSuiteReport>, TestCompletion {

  private final CountDownLatch latch = new CountDownLatch(1);
  private final Reporter[] reporters;

  private volatile Future<?> completion;
  private final AtomicReference<Throwable> failure = new AtomicReference<>();
  private volatile boolean completed;

  public ReporterHandler(Reporter... reporters) {
    this(null, reporters);
  }

  public ReporterHandler(Future completion,
                         Reporter... reporters) {
    this.completion = completion;
    this.reporters = reporters;
  }

  @Override
  public void resolve(Future future) {
    completion = future;
    if (completed) {
      if (failure.get() == null) {
        future.complete();
      } else {
        future.fail(failure.get());
      }
    }
  }

  @Override
  public boolean isCompleted() {
    return completed;
  }

  @Override
  public boolean isSucceeded() {
    return completed && failure.get() == null;
  }

  @Override
  public boolean isFailed() {
    return completed && failure.get() != null;
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
      completed = true;
      latch.countDown();
      if (completion != null) {
        if (failure.get() == null) {
          completion.complete();
        } else {
          completion.fail(failure.get());
        }
      }
    });
  }

  @Override
  public void await() {
    try {
      latch.await();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      Helper.uncheckedThrow(e);
    }
  }

  @Override
  public void await(long timeoutMillis) {
    try {
      boolean ok = latch.await(timeoutMillis, TimeUnit.MILLISECONDS);
      if (!ok) {
        Helper.uncheckedThrow(new TimeoutException("Timed out"));
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      Helper.uncheckedThrow(e);
    }
  }
}
