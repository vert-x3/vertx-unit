package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.impl.TestSuiteImpl;
import io.vertx.ext.unit.report.TestResult;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestSuiteNoEventLoopTest extends TestSuiteTestBase {

  public TestSuiteNoEventLoopTest() {
    super();
    getRunner = TestSuiteImpl::runner;
    run = runner -> {
      assertNull(Vertx.currentContext());
      runner.setUseEventLoop(false).run();
    };
    operateOnAsync = (async,action) -> action.accept(async);
  }

  @Override
  protected boolean checkTest(TestContext test) {
    return Vertx.currentContext() == null;
  }

  /*
  @org.junit.Test
  public void testEndsAfterCallback() throws Exception {
    AtomicBoolean ok = new AtomicBoolean();
    AtomicBoolean after = new AtomicBoolean();
    TestSuite suite = TestSuite.create("my_suite").
        test("my_test", context -> {
          try {
            context.fail();
          } catch (AssertionError e) {
          }
          if (!after.get()) {
            ok.set(true);
          }
        }).after(context -> {
      after.set(true);
    });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    reporter.await();
    assertTrue(ok.get());
    assertTrue(reporter.completed());
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    TestResult result = reporter.results.get(0);
    assertEquals("my_test", result.name());
    assertTrue(result.failed());
  }
  */
}
