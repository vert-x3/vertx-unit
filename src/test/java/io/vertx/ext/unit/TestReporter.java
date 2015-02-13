package io.vertx.ext.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class TestReporter {
  private final CountDownLatch latch = new CountDownLatch(1);
  final List<TestResult> results = Collections.synchronizedList(new ArrayList<>());

  TestSuiteRunner runner(TestSuite suite) {
    TestSuiteRunner runner = suite.runner();
    runner.handler(testExec -> {
      testExec.endHandler(results::add);
    });
    runner.endHandler(done -> {
      latch.countDown();
    });
    return runner;
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
