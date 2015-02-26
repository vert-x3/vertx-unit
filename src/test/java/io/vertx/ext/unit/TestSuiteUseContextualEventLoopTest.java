package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.impl.TestSuiteImpl;
import org.junit.After;
import org.junit.Before;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestSuiteUseContextualEventLoopTest extends TestSuiteTestBase {

  private Vertx vertx;

  public TestSuiteUseContextualEventLoopTest() {
    super();
    getRunner = TestSuiteImpl::runner;
    run = (runner) -> {
      assertNull(Vertx.currentContext());
      vertx.runOnContext(v -> runner.setUseEventLoop(true).run());
    };
    completeAsync = async -> {
      assertNull(Vertx.currentContext());
      vertx.runOnContext(v -> async.complete());
    };
  }

  @Override
  protected boolean checkTest(TestContext test) {
    return Vertx.currentContext() != null && test.vertx() == null;
  }

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
  }

  @After
  public void tearDown() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    if (vertx != null) {
      vertx.close(ar -> {
        latch.countDown();
      });
      vertx = null;
      latch.await();
    }
  }
}
