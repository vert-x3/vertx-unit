package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestResult;
import io.vertx.ext.unit.TestCaseRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestCaseRunnerImpl implements TestCaseRunner, Task<Void> {

  private final String desc;
  private final RunTestTask task;
  private volatile Handler<TestResult> completionHandler;
  private volatile TestResult result;

  public TestCaseRunnerImpl(String desc,
                            Handler<Test> before,
                            Handler<Test> test,
                            Handler<Test> after,
                            Task<?> next) {
    this.desc = desc;
    this.task = new RunTestTask(desc, before, test, after, (testResult, executor) -> {
      result = testResult;
      if (completionHandler != null) {
        completionHandler.handle(testResult);
      }
      next.run(null, executor);
    });
  }

  @Override
  public void run(Void v, Context executor) {
    if (result == null) {
      executor.run(task.task, null);
    }
  }

  @Override
  public String description() {
    return desc;
  }

  @Override
  public void completionHandler(Handler<TestResult> handler) {
    completionHandler = handler;
    if (completionHandler != null && result != null) {
      completionHandler.handle(result);
    }
  }

  @Override
  public void run() {
    task.task.run(null, Context.create());
  }

  @Override
  public void run(Vertx vertx) {
    task.task.run(null, Context.create(vertx));
  }

  @Override
  public void assertSuccess() {
    assertSuccess(2, TimeUnit.MINUTES);
  }

  @Override
  public void assertSuccess(long timeout, TimeUnit unit) {
    Vertx vertx = Vertx.vertx();
    assertSuccess(vertx, timeout, unit);
    vertx.close();
  }

  @Override
  public void assertSuccess(Vertx vertx, long timeout, TimeUnit unit) {
    assertSuccess(Context.create(vertx), timeout, unit);
  }

  @Override
  public void assertSuccess(Vertx vertx) {
    assertSuccess(Context.create(vertx), 2, TimeUnit.MINUTES);
  }

  public void assertSuccess(Context context, long timeout, TimeUnit unit) {
    CountDownLatch latch = new CountDownLatch(1);
    AtomicReference<TestResult> resultRef = new AtomicReference<>();
    completionHandler = result ->{
      resultRef.set(result);
      latch.countDown();
    };
    task.task.run(null, context);
    try {
      latch.await(timeout, unit);
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
    TestResult result = resultRef.get();
    if (result == null) {
      throw new IllegalStateException("Time out");
    } else if (result.failed()) {
      Throwable failure = result.failure();
      if (failure instanceof Error) {
        throw (Error) failure;
      } else if (failure instanceof RuntimeException) {
        throw (RuntimeException) failure;
      }
    }
  }
}
