package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.TestCase;
import io.vertx.ext.unit.report.TestCaseReport;
import io.vertx.ext.unit.report.TestResult;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestCaseImpl implements TestCase {

  final String name;
  final int repeat;
  final Handler<TestContext> handler;

  public TestCaseImpl(String name, int repeat, Handler<TestContext> handler) {
    if (name == null) {
      throw new IllegalArgumentException("Test name cannot be null");
    }
    if (repeat < 1) {
      throw new IllegalArgumentException("Repeat count must be >= 1");
    }
    if (handler == null) {
      throw new IllegalArgumentException("Test cannot be null");
    }
    this.name = name;
    this.repeat = repeat;
    this.handler = handler;
  }

  private TestCaseReport runner() {
    return new TestCaseReportImpl(name, 0, 1, new HashMap<>(), null, handler, null, null);
  }

  public String name() {
    return name;
  }

  @Override
  public void awaitSuccess() {
    awaitSuccess(2, TimeUnit.MINUTES);
  }

  @Override
  public void awaitSuccess(long timeout, TimeUnit unit) {
    awaitSuccess(new ExecutionContext(Vertx.currentContext()), timeout, unit);
  }

  @Override
  public void awaitSuccess(Vertx vertx, long timeout, TimeUnit unit) {
    awaitSuccess(new ExecutionContext(vertx.getOrCreateContext()), timeout, unit);
  }

  @Override
  public void awaitSuccess(Vertx vertx) {
    awaitSuccess(new ExecutionContext(vertx.getOrCreateContext()), 2, TimeUnit.MINUTES);
  }

  private void awaitSuccess(ExecutionContext context, long timeout, TimeUnit unit) {
    CountDownLatch latch = new CountDownLatch(1);
    TestCaseReportImpl testCase = (TestCaseReportImpl) runner();
    AtomicReference<TestResult> resultRef = new AtomicReference<>();
    testCase.endHandler(result ->{
      resultRef.set(result);
      latch.countDown();
    });
    Task<?> task = testCase.buildTask((v, executor) -> {});
    context.run(task);
    try {
      latch.await(timeout, unit);
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
    TestResult result = resultRef.get();
    if (result == null) {
      throw new IllegalStateException("Time out");
    } else if (result.failed()) {
      Throwable failure = result.failure().cause();
      if (failure instanceof Error) {
        throw (Error) failure;
      } else if (failure instanceof RuntimeException) {
        throw (RuntimeException) failure;
      }
    }
  }
}
