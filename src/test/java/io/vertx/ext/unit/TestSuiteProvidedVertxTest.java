package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.impl.TestSuiteImpl;
import org.junit.After;
import org.junit.Before;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestSuiteProvidedVertxTest extends TestSuiteTestBase {

  private Vertx vertx;

  public TestSuiteProvidedVertxTest() {
    super();
    getRunner = TestSuiteImpl::runner;
    run = (runner) -> vertx.runOnContext(v -> runner.run());
    completeAsync = async -> vertx.runOnContext(v -> async.complete());
  }

  @Override
  protected boolean checkTest(Test test) {
    return Vertx.currentContext() != null && test.vertx() == null;
  }

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
  }

  @After
  public void tearDown() {
    if (vertx != null) {
      vertx.close();
      vertx = null;
    }
  }
}
