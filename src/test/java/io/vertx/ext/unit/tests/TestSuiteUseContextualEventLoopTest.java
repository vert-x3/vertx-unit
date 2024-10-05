package io.vertx.ext.unit.tests;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.impl.TestSuiteImpl;
import org.junit.After;
import org.junit.Before;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
    operateOnAsync = (async, action) -> {
      CountDownLatch latch = new CountDownLatch(1);
      assertNull(Vertx.currentContext());
      vertx.runOnContext(v -> {
        action.accept(async);
        latch.countDown();
      });
      try {
        latch.await(10, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
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
