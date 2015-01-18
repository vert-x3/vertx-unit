package io.vertx.ext.unit;

import io.vertx.core.Vertx;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SuiteDefSyncTest extends SuiteDefTestBase {

  public SuiteDefSyncTest() {
    super();
    runSuite = SuiteRunner::run;
    completeAsync = Async::complete;
  }

  @Override
  protected boolean checkTest(Test test) {
    return Vertx.currentContext() == null && test.vertx() == null;
  }
}
