package io.vertx.ext.unit;

import io.vertx.ext.unit.impl.TestSuiteImpl;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestSuiteObjectTest {

  public class MySuite1 {
    AtomicInteger count = new AtomicInteger();
    public void testSomething(TestContext test) {
      count.incrementAndGet();
    }
  }

  @org.junit.Test
  public void runTest() {
    MySuite1 obj = new MySuite1();
    TestSuiteImpl suite = (TestSuiteImpl) TestSuite.create(obj);
    TestReporter reporter = new TestReporter();
    suite.runner().setReporter(reporter).run();
    reporter.await();
    assertEquals(1, obj.count.get());
    assertEquals(MySuite1.class.getName(), reporter.name.get());
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertEquals("testSomething", reporter.results.get(0).name());
    assertTrue(reporter.results.get(0).succeeded());
  }

  public class MySuite2 {
    public void testSomething(TestContext test) {
      test.fail("the_message");
    }
  }

  @org.junit.Test
  public void runTestFailure() {
    MySuite2 obj = new MySuite2();
    TestSuiteImpl suite = (TestSuiteImpl) TestSuite.create(obj);
    TestReporter reporter = new TestReporter();
    suite.runner().setReporter(reporter).run();
    reporter.await();
    assertEquals(MySuite2.class.getName(), reporter.name.get());
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertEquals("testSomething", reporter.results.get(0).name());
    assertTrue(reporter.results.get(0).failed());
    assertEquals("the_message", reporter.results.get(0).failure().message());
    assertTrue(reporter.results.get(0).failure().cause() instanceof AssertionError);
  }

  public class MySuite3 {
    final Throwable error;
    public MySuite3(Throwable error) {
      this.error = error;
    }
    public void testSomething(TestContext test) throws Throwable {
      throw error;
    }
  }

  @org.junit.Test
  public void runTestError() {
    Throwable[] errors = {new IOException(),new RuntimeException(),new AssertionError()};
    for (Throwable error :  errors) {
      TestSuiteImpl suite = (TestSuiteImpl) TestSuite.create(new MySuite3(error));
      TestReporter reporter = new TestReporter();
      suite.runner().setReporter(reporter).run();
      reporter.await();
      assertEquals(MySuite3.class.getName(), reporter.name.get());
      assertEquals(0, reporter.exceptions.size());
      assertEquals(1, reporter.results.size());
      assertEquals("testSomething", reporter.results.get(0).name());
      assertTrue(reporter.results.get(0).failed());
      assertSame(error, reporter.results.get(0).failure().cause());
    }
  }

  public class MySuite4 {
    AtomicInteger count = new AtomicInteger();
    public void before(TestContext test) {
      count.compareAndSet(0, 1);
    }
    public void testSomething(TestContext test) {
      count.compareAndSet(1, 2);
    }
  }

  @org.junit.Test
  public void runBefore() {
    MySuite4 obj = new MySuite4();
    TestSuiteImpl suite = (TestSuiteImpl) TestSuite.create(obj);
    TestReporter reporter = new TestReporter();
    suite.runner().setReporter(reporter).run();
    reporter.await();
    assertEquals(2, obj.count.get());
  }

  public class MySuite5 {
    AtomicInteger count = new AtomicInteger();
    public void after(TestContext test) {
      count.compareAndSet(1, 2);
    }
    public void testSomething(TestContext test) {
      count.compareAndSet(0, 1);
    }
  }

  @org.junit.Test
  public void runAfter() {
    MySuite5 obj = new MySuite5();
    TestSuiteImpl suite = (TestSuiteImpl) TestSuite.create(obj);
    TestReporter reporter = new TestReporter();
    suite.runner().setReporter(reporter).run();
    reporter.await();
    assertEquals(2, obj.count.get());
  }

  public class MySuite6 {
    AtomicInteger count = new AtomicInteger();
    public void beforeEach(TestContext test) {
      count.compareAndSet(0, 1);
    }
    public void testSomething(TestContext test) {
      count.compareAndSet(1, 2);
    }
  }

  @org.junit.Test
  public void runBeforeEach() {
    MySuite6 obj = new MySuite6();
    TestSuiteImpl suite = (TestSuiteImpl) TestSuite.create(obj);
    TestReporter reporter = new TestReporter();
    suite.runner().setReporter(reporter).run();
    reporter.await();
    assertEquals(2, obj.count.get());
  }

  public class MySuite7 {
    AtomicInteger count = new AtomicInteger();
    public void afterEach(TestContext test) {
      count.compareAndSet(1, 2);
    }
    public void testSomething(TestContext test) {
      count.compareAndSet(0, 1);
    }
  }

  @org.junit.Test
  public void runAfterEach() {
    MySuite7 obj = new MySuite7();
    TestSuiteImpl suite = (TestSuiteImpl) TestSuite.create(obj);
    TestReporter reporter = new TestReporter();
    suite.runner().setReporter(reporter).run();
    reporter.await();
    assertEquals(2, obj.count.get());
  }
}
