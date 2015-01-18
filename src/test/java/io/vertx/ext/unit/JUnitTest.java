package io.vertx.ext.unit;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JUnitTest {

  @org.junit.Test
  public void testSuiteRun() {
    AtomicReference<Vertx> current = new AtomicReference<>();
    Result result = run(test -> current.set(test.vertx()));
    assertEquals(1, result.getRunCount());
    assertEquals(0, result.getFailureCount());
    assertNotNull(current.get());
  }

  @org.junit.Test
  public void testSuiteFail() {
    Result result = run(test -> test.fail("the_failure"));
    assertEquals(1, result.getRunCount());
    assertEquals(1, result.getFailureCount());
    Failure failure = result.getFailures().get(0);
    assertEquals("the_failure", failure.getMessage());
    assertTrue(failure.getException() instanceof AssertionError);
  }

  @org.junit.Test
  public void testSuiteRuntimeException() {
    RuntimeException cause = new RuntimeException("the_runtime_exception");
    Result result = run(test -> { throw cause; });
    assertEquals(1, result.getRunCount());
    assertEquals(1, result.getFailureCount());
    Failure failure = result.getFailures().get(0);
    assertEquals("the_runtime_exception", failure.getMessage());
    assertSame(cause, failure.getException());
  }

  @org.junit.Test
  public void testSuiteTimeout() {
    Result result = new JUnitCore().run(Unit.suite().test("test test", Test::async).toJUnitSuite(100, TimeUnit.MILLISECONDS));
    assertEquals(1, result.getRunCount());
    assertEquals(1, result.getFailureCount());
    Failure failure = result.getFailures().get(0);
    assertTrue(failure.getException() instanceof TimeoutException);
  }

  @org.junit.Test
  public void testSuiteInterrupted() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    AtomicReference<Result> resultRef = new AtomicReference<>();
    Thread t = new Thread() {
      @Override
      public void run() {
        try {
          Result result = new JUnitCore().run(Unit.suite().test("test test", Test::async).toJUnitSuite());
          resultRef.set(result);
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
    Result result = resultRef.get();
    assertEquals(1, result.getRunCount());
    assertEquals(1, result.getFailureCount());
    assertTrue(result.getFailures().get(0).getException() instanceof InterruptedException);
  }

  private Result run(Handler<Test> test) {
    return new JUnitCore().run(Unit.suite().test("test test", test).toJUnitSuite());
  }

  @org.junit.Test
  public void testAssert() {
    Unit.test("my_test", test -> {}).runner().assertSuccess();
  }

  @org.junit.Test
  public void testAssertFailure() {
    try {
      Unit.test("my_test", test -> test.fail("the_failure")).runner().assertSuccess();
      fail();
    } catch (AssertionError err) {
      assertEquals("the_failure", err.getMessage());
    }
  }

  @org.junit.Test
  public void testAssertRuntimeException() {
    RuntimeException failure = new RuntimeException();
    try {
      Unit.test("my_test", test -> { throw failure; } ).runner().assertSuccess();
      fail();
    } catch (RuntimeException err) {
      assertSame(failure, err);
    }
  }

  @org.junit.Test
  public void testAssertTimeout() {
    try {
      Unit.test("my_test", Test::async).runner().assertSuccess(300, TimeUnit.MILLISECONDS);
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
          Unit.test("my_test", Test::async).runner().assertSuccess();
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
