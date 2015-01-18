package io.vertx.ext.unit;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JUnitTest {

  @org.junit.Test
  public void testRun() {
    AtomicReference<Context> current = new AtomicReference<>();
    Result result = run(test -> current.set(Vertx.currentContext()));
    assertEquals(1, result.getRunCount());
    assertEquals(0, result.getFailureCount());
    assertNotNull(current.get());
  }

  @org.junit.Test
  public void testFail() {
    Result result = run(test -> test.fail("the_failure"));
    assertEquals(1, result.getRunCount());
    assertEquals(1, result.getFailureCount());
    Failure failure = result.getFailures().get(0);
    assertEquals("the_failure", failure.getMessage());
    assertTrue(failure.getException() instanceof AssertionError);
  }

  @org.junit.Test
  public void testRuntimeException() {
    RuntimeException cause = new RuntimeException("the_runtime_exception");
    Result result = run(test -> { throw cause; });
    assertEquals(1, result.getRunCount());
    assertEquals(1, result.getFailureCount());
    Failure failure = result.getFailures().get(0);
    assertEquals("the_runtime_exception", failure.getMessage());
    assertSame(cause, failure.getException());
  }

  @org.junit.Test
  public void testTimeout() {
    Result result = new JUnitCore().run(Unit.test("test test", Test::async).toJUnitSuite(100, TimeUnit.MILLISECONDS));
    assertEquals(1, result.getRunCount());
    assertEquals(1, result.getFailureCount());
    Failure failure = result.getFailures().get(0);
    assertTrue(failure.getException() instanceof TimeoutException);
  }

  private Result run(Handler<Test> test) {
    return new JUnitCore().run(Unit.test("test test", test).toJUnitSuite());
  }
}
