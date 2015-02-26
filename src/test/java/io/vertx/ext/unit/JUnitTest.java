package io.vertx.ext.unit;

import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.model.InitializationError;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JUnitTest {

  public static class TestSuiteWithBadConstructor {
    public TestSuiteWithBadConstructor(String s) {
    }
  }

  public static class TestSuiteWithFailingConstructor {
    public TestSuiteWithFailingConstructor() {
      throw new RuntimeException();
    }
  }

  @org.junit.Test
  public void testSuiteWithBadConstructor() {
    try {
      new JUnitCore().run(new VertxUnitRunner(TestSuiteWithBadConstructor.class));
      fail();
    } catch (InitializationError ignore) {
      //
    }
    try {
      new JUnitCore().run(new VertxUnitRunner(TestSuiteWithFailingConstructor.class));
      fail();
    } catch (InitializationError ignore) {
      //
    }
  }

  public static class SimpleTestSuite {
    static final AtomicReference<TestContext> current = new AtomicReference<>();
    static final AtomicInteger count = new AtomicInteger();
    @Test
    public void testSimple1(TestContext context) {
      count.incrementAndGet();
      current.set(context);
    }

    @Test
    public void testSimple2() {
      count.incrementAndGet();
    }
  }

  @org.junit.Test
  public void testSuiteRun() {
    Result result = run(SimpleTestSuite.class);
    assertEquals(2, SimpleTestSuite.count.get());
    assertEquals(2, result.getRunCount());
    assertEquals(0, result.getFailureCount());
    assertNotNull(SimpleTestSuite.current.get());
    assertNotNull(SimpleTestSuite.current.get().vertx());
  }

  public static class TestSuiteFail {
    @Test
    public void testFail(TestContext context) {
      context.fail("the_failure");
    }
  }

  @org.junit.Test
  public void testSuiteFail() {
    Result result = run(TestSuiteFail.class);
    assertEquals(0, result.getRunCount());
    assertEquals(1, result.getFailureCount());
    Failure failure = result.getFailures().get(0);
    assertEquals("the_failure", failure.getMessage());
    assertTrue(failure.getException() instanceof AssertionError);
  }

  public static class TestSuiteRuntimeException {
    static RuntimeException cause = new RuntimeException("the_runtime_exception");
    @Test
    public void testRuntimeException(TestContext context) {
      throw cause;
    }
  }

  @org.junit.Test
  public void testSuiteRuntimeException() {
    Result result = run(TestSuiteRuntimeException.class);
    assertEquals(0, result.getRunCount());
    assertEquals(1, result.getFailureCount());
    Failure failure = result.getFailures().get(0);
    assertEquals("the_runtime_exception", failure.getMessage());
    assertSame(TestSuiteRuntimeException.cause, failure.getException());
  }

  public static class TestSuiteTimingOut {
    @Test
    public void testTimingOut(TestContext context) {
      context.async();
    }
  }

  @org.junit.Test
  public void testSuiteTimeout() {
    Result result = run(TestSuiteTimingOut.class, 100L);
    assertEquals(0, result.getRunCount());
    assertEquals(1, result.getFailureCount());
    Failure failure = result.getFailures().get(0);
    assertTrue(failure.getException() instanceof TimeoutException);
  }

  @org.junit.Test
  public void testSuiteInterrupted() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    AtomicReference<Result> resultRef = new AtomicReference<>();
    AtomicBoolean interrupted = new AtomicBoolean();
    Thread t = new Thread() {
      @Override
      public void run() {
        try {
          Result result = JUnitTest.this.run(TestSuiteTimingOut.class);
          resultRef.set(result);
          interrupted.set(Thread.currentThread().isInterrupted());
        } finally {
          latch.countDown();
        }
      }
    };
    t.start();
    long now = System.currentTimeMillis();
    while (t.getState() != Thread.State.WAITING) {
      Thread.sleep(1);
      if ((System.currentTimeMillis() - now) > 2000) {
        throw new AssertionError("Could not get WAITING state: " + t.getState());
      }
    }
    t.interrupt();
    latch.await();
    Result result = resultRef.get();
    assertEquals(0, result.getRunCount());
    assertEquals(0, result.getFailureCount());
    assertTrue(interrupted.get());
  }


  private Result run(Class<?> testClass) {
    return run(testClass, null);
  }

  private Result run(Class<?> testClass, Long timeout) {
    try {
      return new JUnitCore().run(new VertxUnitRunner(testClass, timeout));
    } catch (InitializationError initializationError) {
      throw new AssertionError(initializationError);
    }
  }

  @org.junit.Test
  public void testAssert() {
    TestCase.create("my_test", context -> {
    }).awaitSuccess();
  }

  @org.junit.Test
  public void testAssertFailure() {
    try {
      TestCase.create("my_test", context -> context.fail("the_failure")).awaitSuccess();
      fail();
    } catch (AssertionError err) {
      assertEquals("the_failure", err.getMessage());
    }
  }

  @org.junit.Test
  public void testAssertRuntimeException() {
    RuntimeException failure = new RuntimeException();
    try {
      TestCase.create("my_test", context -> {
        throw failure;
      }).awaitSuccess();
      fail();
    } catch (RuntimeException err) {
      assertSame(failure, err);
    }
  }

  @org.junit.Test
  public void testAssertTimeout() {
    try {
      TestCase.create("my_test", TestContext::async).awaitSuccess(300, TimeUnit.MILLISECONDS);
      fail();
    } catch (IllegalStateException ignore) {
    }
  }

  @org.junit.Test
  public void testAssertInterrupted() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    AtomicBoolean ise = new AtomicBoolean();
    Thread t = new Thread() {
      @Override
      public void run() {
        try {
          TestCase.create("my_test", TestContext::async).awaitSuccess();
        } catch (IllegalStateException e) {
          ise.set(true);
        } finally {
          latch.countDown();
        }
      }
    };
    t.start();
    long now = System.currentTimeMillis();
    while (t.getState() != Thread.State.TIMED_WAITING) {
      Thread.sleep(1);
      if ((System.currentTimeMillis() - now) > 2000) {
        throw new AssertionError();
      }
    }
    t.interrupt();
    latch.await();
    assertTrue(ise.get());
  }
}
