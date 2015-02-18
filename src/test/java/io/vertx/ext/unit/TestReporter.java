package io.vertx.ext.unit;

import io.vertx.ext.unit.report.Reporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class TestReporter implements Reporter<Void> {

  private final CountDownLatch latch = new CountDownLatch(1);
  final List<Throwable> exceptions = Collections.synchronizedList(new ArrayList<>());
  final List<TestResult> results = Collections.synchronizedList(new ArrayList<>());

  @Override
  public Void createReport() {
    return null;
  }

  @Override
  public void reportBeginTestSuite(Void report, String name) {

  }

  @Override
  public void reportBeginTestCase(Void report, String name) {

  }

  @Override
  public void reportEndTestCase(Void report, TestResult result) {
    results.add(result);
  }

  @Override
  public void reportEndTestSuite(Void report, String name, Throwable err) {
    if (err != null) {
      exceptions.add(err);
    }
    latch.countDown();
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
