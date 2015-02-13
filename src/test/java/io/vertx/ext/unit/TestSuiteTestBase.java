package io.vertx.ext.unit;

import io.vertx.core.Handler;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public abstract class TestSuiteTestBase {

  protected Consumer<TestSuiteRunner> runSuite;
  protected Consumer<Async> completeAsync;

  public TestSuiteTestBase() {
  }

  protected boolean checkTest(io.vertx.ext.unit.Test test) {
    return true;
  }

  @Test
  public void runTest() {
    AtomicInteger count = new AtomicInteger();
    AtomicBoolean sameContext = new AtomicBoolean();
    TestSuite suite = Unit.suite().
        test("my_test", test -> {
          sameContext.set(checkTest(test));
          count.compareAndSet(0, 1);
        });
    TestReporter reporter = new TestReporter();
    runSuite.accept(reporter.runner(suite));
    reporter.await();
    assertTrue(sameContext.get());
    assertEquals(1, count.get());
    assertEquals(1, reporter.results.size());
    TestResult result = reporter.results.get(0);
    assertEquals("my_test", result.description());
    assertTrue(result.succeeded());
    assertFalse(result.failed());
    assertNull(result.failure());
  }

  @Test
  public void runTestWithAsyncCompletion() throws Exception {
    BlockingQueue<Async> queue = new ArrayBlockingQueue<>(1);
    AtomicInteger count = new AtomicInteger();
    TestSuite suite = Unit.suite().
        test("my_test", test -> {
          count.compareAndSet(0, 1);
          queue.add(test.async());
        });
    TestReporter reporter = new TestReporter();
    runSuite.accept(reporter.runner(suite));
    Async async = queue.poll(2, TimeUnit.SECONDS);
    assertEquals(1, count.get());
    assertFalse(reporter.completed());
    completeAsync.accept(async);
    reporter.await();
    assertTrue(reporter.completed());
    assertEquals(1, reporter.results.size());
    TestResult result = reporter.results.get(0);
    assertEquals("my_test", result.description());
    assertTrue(result.succeeded());
    assertFalse(result.failed());
    assertNull(result.failure());
  }

  @Test
  public void runTestWithAssertionError() {
    failTest(test -> test.fail("message_failure"));
  }

  @Test
  public void runTestWithRuntimeException() {
    failTest(test -> { throw new RuntimeException("message_failure"); });
  }

  private void failTest(Handler<io.vertx.ext.unit.Test> thrower) {
    AtomicReference<Throwable> failure = new AtomicReference<>();
    TestSuite suite = Unit.suite().
        test("my_test", test -> {
          try {
            thrower.handle(test);
          } catch (Error | RuntimeException e) {
            failure.set(e);
            throw e;
          }
        });
    TestReporter reporter = new TestReporter();
    runSuite.accept(reporter.runner(suite));
    reporter.await();
    assertEquals(1, reporter.results.size());
    TestResult result = reporter.results.get(0);
    assertEquals("my_test", result.description());
    assertFalse(result.succeeded());
    assertTrue(result.failed());
    assertNotNull(result.failure());
    assertSame(failure.get(), result.failure());
  }

  @Test
  public void runTestWithAsyncFailure() throws Exception {
    BlockingQueue<io.vertx.ext.unit.Test> queue = new ArrayBlockingQueue<>(1);
    TestSuite suite = Unit.suite().test("my_test", test -> {
      test.async();
      queue.add(test);
    });
    TestReporter reporter = new TestReporter();
    runSuite.accept(reporter.runner(suite));
    assertFalse(reporter.completed());
    io.vertx.ext.unit.Test test = queue.poll(2, TimeUnit.SECONDS);
    try {
      test.fail("the_message");
    } catch (AssertionError ignore) {
    }
    reporter.await();
    assertTrue(reporter.completed());
  }

  @Test
  public void runBefore() throws Exception {
    AtomicInteger count = new AtomicInteger();
    AtomicBoolean sameContext = new AtomicBoolean();
    TestSuite suite = Unit.suite().test("my_test", test -> {
      sameContext.set(checkTest(test));
      count.compareAndSet(1, 2);
    }).before(test -> {
      count.compareAndSet(0, 1);
    });
    TestReporter reporter = new TestReporter();
    runSuite.accept(reporter.runner(suite));
    reporter.await();
    assertEquals(2, count.get());
    assertTrue(sameContext.get());
  }

  @Test
  public void runBeforeWithAsyncCompletion() throws Exception {
    AtomicInteger count = new AtomicInteger();
    AtomicBoolean sameContext = new AtomicBoolean();
    BlockingQueue<Async> queue = new ArrayBlockingQueue<>(1);
    TestSuite suite = Unit.suite().test("my_test", test -> {
      count.compareAndSet(1, 2);
      sameContext.set(checkTest(test));
    }).before(test -> {
      count.compareAndSet(0, 1);
      queue.add(test.async());
    });
    TestReporter reporter = new TestReporter();
    runSuite.accept(reporter.runner(suite));
    Async async = queue.poll(2, TimeUnit.SECONDS);
    completeAsync.accept(async);
    reporter.await();
    assertEquals(2, count.get());
    assertTrue(sameContext.get());
  }

  @Test
  public void runBeforeWithFailure() throws Exception {
    AtomicInteger count = new AtomicInteger();
    TestSuite suite = Unit.suite().test("my_test", test -> {
      count.incrementAndGet();
    }).before(test -> {
      throw new RuntimeException();
    });
    TestReporter reporter = new TestReporter();
    runSuite.accept(reporter.runner(suite));
    reporter.await();
    assertEquals(0, count.get());
  }

  @Test
  public void runAfterWithAsyncCompletion() throws Exception {
    AtomicInteger count = new AtomicInteger();
    BlockingQueue<Async> queue = new ArrayBlockingQueue<>(1);
    TestSuite suite = Unit.suite().test("my_test", test -> {
      count.compareAndSet(0, 1);
    }).after(test -> {
      count.compareAndSet(1, 2);
      queue.add(test.async());
    });
    TestReporter reporter = new TestReporter();
    runSuite.accept(reporter.runner(suite));
    Async async = queue.poll(2, TimeUnit.SECONDS);
    assertFalse(reporter.completed());
    assertEquals(2, count.get());
    completeAsync.accept(async);
    reporter.await();
  }

  @Test
  public void runAfter() throws Exception {
    AtomicInteger count = new AtomicInteger();
    AtomicBoolean sameContext = new AtomicBoolean();
    TestSuite suite = Unit.suite().test("my_test", test -> {
      count.compareAndSet(0, 1);
    }).after(test -> {
      sameContext.set(checkTest(test));
      count.compareAndSet(1, 2);
    });
    TestReporter reporter = new TestReporter();
    runSuite.accept(reporter.runner(suite));
    reporter.await();
    assertEquals(2, count.get());
    assertTrue(sameContext.get());
  }

  @Test
  public void runAfterWithFailure() throws Exception {
    AtomicInteger count = new AtomicInteger();
    TestSuite suite = Unit.suite().test("my_test", test -> {
      count.compareAndSet(0, 1);
      test.fail("the_message");
    }).after(test -> {
      count.compareAndSet(1, 2);
    });
    TestReporter reporter = new TestReporter();
    runSuite.accept(reporter.runner(suite));
    reporter.await();
    assertEquals(2, count.get());
    assertTrue(reporter.results.get(0).failed());
    assertEquals("the_message", reporter.results.get(0).failure().getMessage());
  }
}
