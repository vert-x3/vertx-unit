package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import org.junit.After;
import org.junit.Before;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 *
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestSuiteUseVertxEventLoopTest extends TestSuiteTestBase {

  private Vertx vertx;

  public TestSuiteUseVertxEventLoopTest() {
    super();
    getRunner = (suite) -> suite.runner().setUseEventLoop(true).setVertx(vertx);
    run = runner -> {
      assertNull(Vertx.currentContext());
      runner.run();
    };
    operateOnAsync = (async, action) -> {
      assertNull(Vertx.currentContext());
      action.accept(async);
    };
  }

  @Override
  protected boolean checkTest(TestContext test) {
    return Vertx.currentContext() != null;
  }

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
  }

  @After
  public void tearDown() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    if (vertx != null) {
      vertx.close().onComplete(ar -> {
        latch.countDown();
      });
      vertx = null;
      latch.await();
    }
  }
}
