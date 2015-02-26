package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.TestCase;
import io.vertx.ext.unit.report.TestCaseReport;
import io.vertx.ext.unit.report.TestResult;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestCaseImpl implements TestCase {

  final String desc;
  final Handler<TestContext> handler;

  public TestCaseImpl(String desc, Handler<TestContext> handler) {
    this.desc = desc;
    this.handler = handler;
  }

  private TestCaseReport runner() {
    return new TestCaseReportImpl(desc, 0, null, handler, null, null, (o, executor) -> {
      // ?
    });
  }

  @Override
  public void awaitSuccess() {
    awaitSuccess(2, TimeUnit.MINUTES);
  }

  @Override
  public void awaitSuccess(long timeout, TimeUnit unit) {
    awaitSuccess(TestSuiteContext.create(null, Vertx.currentContext()), timeout, unit);
  }

  @Override
  public void awaitSuccess(Vertx vertx, long timeout, TimeUnit unit) {
    awaitSuccess(TestSuiteContext.create(vertx, vertx.getOrCreateContext()), timeout, unit);
  }

  @Override
  public void awaitSuccess(Vertx vertx) {
    awaitSuccess(TestSuiteContext.create(vertx, vertx.getOrCreateContext()), 2, TimeUnit.MINUTES);
  }

  private void awaitSuccess(TestSuiteContext context, long timeout, TimeUnit unit) {
    CountDownLatch latch = new CountDownLatch(1);
    TestCaseReportImpl testCase = (TestCaseReportImpl) runner();
    AtomicReference<TestResult> resultRef = new AtomicReference<>();
    testCase.endHandler(result ->{
      resultRef.set(result);
      latch.countDown();
    });
    testCase.execute(null, context);
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
