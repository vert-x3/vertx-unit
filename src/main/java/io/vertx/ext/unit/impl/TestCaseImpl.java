package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestCase;
import io.vertx.ext.unit.TestCaseReport;
import io.vertx.ext.unit.TestResult;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestCaseImpl implements TestCase {

  final String desc;
  final Handler<Test> handler;

  public TestCaseImpl(String desc, Handler<Test> handler) {
    this.desc = desc;
    this.handler = handler;
  }

  TestCaseReport runner() {
    return new TestCaseReportImpl(desc, 0, null, handler, null, (o, executor) -> {
      // ?
    });
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
