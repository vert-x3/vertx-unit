package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.impl.TestSuiteImpl;
import io.vertx.ext.unit.impl.TestSuiteRunner;
import io.vertx.ext.unit.report.TestResult;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestSuiteSyncTest extends TestSuiteTestBase {

  public TestSuiteSyncTest() {
    super();
    getRunner = TestSuiteImpl::runner;
    run = TestSuiteRunner::run;
    completeAsync = Async::complete;
  }

  @Override
  protected boolean checkTest(Test test) {
    return Vertx.currentContext() == null && test.vertx() == null;
  }

  @org.junit.Test
  public void testEndsAfterCallback() throws Exception {
    AtomicBoolean ok = new AtomicBoolean();
    AtomicBoolean after = new AtomicBoolean();
    TestSuite suite = TestSuite.create("my_suite").
        test("my_test", test -> {
          try {
            test.fail();
          } catch (AssertionError e) {
          }
          if (!after.get()) {
            ok.set(true);
          }
        }).after(test -> {
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

}
