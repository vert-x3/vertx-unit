package io.vertx.ext.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class Reporter {
  private final CountDownLatch latch = new CountDownLatch(1);
  final List<TestResult> results = Collections.synchronizedList(new ArrayList<>());

  void run(Suite suite) {
    SuiteRunner moduleExec = suite.runner();
    moduleExec.handler(testExec -> {
      testExec.completionHandler(results::add);
    });
    moduleExec.endHandler(done -> {
      latch.countDown();
    });
    moduleExec.run();
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
