package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import org.junit.After;
import org.junit.Before;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VertxUnitTest extends UnitTestBase {

  private Vertx vertx;

  public VertxUnitTest() {
    super();
    runSuite = runner -> runner.run(vertx);
    completeAsync = Async::complete;
  }

  @Override
  protected boolean checkTest(Test test) {
    return Vertx.currentContext() != null && test.vertx() != null;
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
