package io.vertx.ext.unit;

import io.vertx.core.Vertx;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestSuiteAsyncTest extends TestSuiteTestBase {

  public TestSuiteAsyncTest() {
    super();
    runSuite = (suite,reporter) -> new Thread() {
      @Override
      public void run() {
        suite.run(reporter);
      }
    }.start();
    completeAsync = Async::complete;
  }

  @Override
  protected boolean checkTest(Test test) {
    return Vertx.currentContext() == null && test.vertx() == null;
  }
}
