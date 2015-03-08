package io.vertx.ext.unit;

import io.vertx.core.Vertx;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestSuiteNoEventLoopAsyncTest extends TestSuiteTestBase {

  public TestSuiteNoEventLoopAsyncTest() {
    super();
    getRunner = testsuite -> testsuite.runner();
    run = (runner) -> new Thread() {
      @Override
      public void run() {
        runner.setUseEventLoop(false).run();
      }
    }.start();
    completeAsync = Async::complete;
  }

  @Override
  protected boolean checkTest(TestContext test) {
    return Vertx.currentContext() == null;
  }
}
