package io.vertx.ext.unit.tests;

import io.vertx.core.Handler;
import io.vertx.ext.unit.report.TestResult;
import io.vertx.ext.unit.report.TestSuiteReport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class TestReporter implements Handler<TestSuiteReport> {

  private final CountDownLatch latch = new CountDownLatch(1);
  final AtomicReference<String> name = new AtomicReference<>();
  final List<Throwable> exceptions = Collections.synchronizedList(new ArrayList<>());
  final List<TestResult> results = Collections.synchronizedList(new ArrayList<>());

  @Override
  public void handle(TestSuiteReport report) {
    name.set(report.name());
    report.handler(testExec -> {
      testExec.endHandler(results::add);
    });
    report.exceptionHandler(err -> {
      exceptions.add(err);
    });
    report.endHandler(done -> {
      latch.countDown();
    });
  }

  void await() {
    try {
      latch.await(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new AssertionError(e);
    }
  }
  boolean completed() {
    return latch.getCount() == 0;
  }
}
