package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import org.junit.Test;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CompletionTest {

  @Test
  public void testSucceeded() {
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
    });
    TestCompletion completion = suite.run(Vertx.vertx());
    completion.await();
    assertTrue(completion.isCompleted());
    assertTrue(completion.isSucceeded());
    assertFalse(completion.isFailed());
  }

  @Test
  public void testFailed() {
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
      context.fail();
    });
    TestCompletion completion = suite.run(Vertx.vertx());
    completion.await();
    assertTrue(completion.isCompleted());
    assertFalse(completion.isSucceeded());
    assertTrue(completion.isFailed());
  }

  @Test
  public void testTimeout() {
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
      context.async();
    });
    TestCompletion completion = suite.run(Vertx.vertx());
    try {
      completion.await(10);
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof TimeoutException);
      assertFalse(completion.isCompleted());
      assertFalse(completion.isSucceeded());
      assertFalse(completion.isFailed());
    }
  }

  @Test
  public void testInterruption() {
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
      context.async();
    });
    Thread thread = Thread.currentThread();
    TestCompletion completion = suite.run(Vertx.vertx());
    new Thread(() -> {
      while (thread.getState() != Thread.State.WAITING) {
        try {
          Thread.sleep(1);
        } catch (InterruptedException ignore) {
        }
      }
      thread.interrupt();
    }).start();
    try {
      completion.await();
      fail();
    } catch (Exception e) {
      assertTrue(Thread.interrupted()); // Check and clear status
      assertTrue(e instanceof InterruptedException);
      assertFalse(completion.isCompleted());
      assertFalse(completion.isSucceeded());
      assertFalse(completion.isFailed());
    }
  }
}
