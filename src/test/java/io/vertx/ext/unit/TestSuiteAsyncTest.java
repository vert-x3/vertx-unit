package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.impl.TestSuiteImpl;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestSuiteAsyncTest extends TestSuiteTestBase {

  public TestSuiteAsyncTest() {
    super();
    getRunner = testsuite -> ((TestSuiteImpl) testsuite).runner();
    run = (runner) -> new Thread() {
      @Override
      public void run() {
        runner.run();
      }
    }.start();
    completeAsync = Async::complete;
  }

  @Override
  protected boolean checkTest(Test test) {
    return Vertx.currentContext() == null && test.vertx() == null;
  }
}
