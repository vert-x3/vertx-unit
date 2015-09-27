package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import org.junit.Test;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CompletionTest {

  @Test
  public void testAwaitSucceeded() {
    testSucceeded(completion -> completion.await());
    testSucceeded(completion -> completion.await(2000));
  }

  @Test
  public void testAwaitSuccessSucceeded() {
    testSucceeded(completion -> completion.awaitSuccess());
    testSucceeded(completion -> completion.awaitSuccess(2000));
  }

  private void testSucceeded(Consumer<Completion> consumer) {
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
    });
    Completion completion = suite.run(Vertx.vertx());
    consumer.accept(completion);
    assertTrue(completion.isCompleted());
    assertTrue(completion.isSucceeded());
    assertFalse(completion.isFailed());
  }

  @Test
  public void testAwaitFailed() {
    testAwaitFailed(completion -> completion.await());
    testAwaitFailed(completion -> completion.await(2000));
  }

  private void testAwaitFailed(Consumer<Completion> consumer) {
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
      context.fail();
    });
    Completion completion = suite.run(Vertx.vertx());
    consumer.accept(completion);
    assertTrue(completion.isCompleted());
    assertFalse(completion.isSucceeded());
    assertTrue(completion.isFailed());
  }

  @Test
  public void testAwaitSuccessFailed() throws Exception {
    testAwaitSuccessFailed(completion -> completion.awaitSuccess());
    testAwaitSuccessFailed(completion -> completion.awaitSuccess(2000));
  }

  private void testAwaitSuccessFailed(Consumer<Completion> consumer) throws Exception {
    AtomicReference<AssertionError> expected = new AtomicReference<>();
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
      try {
        context.fail();
      } catch (AssertionError ae) {
        expected.set(ae);
        throw ae;
      }
    });
    Completion completion = suite.run(Vertx.vertx());
    try {
      consumer.accept(completion);
      fail();
    } catch (AssertionError ae) {
      assertSame(expected.get(), ae);
    }
    assertTrue(completion.isCompleted());
    assertFalse(completion.isSucceeded());
    assertTrue(completion.isFailed());
  }

  @Test
  public void testAwaitTimeout() {
    testTimeout(completion -> completion.await(10));
  }

  @Test
  public void testAwaitSuccessTimeout() {
    testTimeout(completion -> completion.awaitSuccess(10));
  }

  private void testTimeout(Consumer<Completion> consumer) {
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
      context.async();
    });
    Completion completion = suite.run(Vertx.vertx());
    try {
      consumer.accept(completion);
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof TimeoutException);
      assertFalse(completion.isCompleted());
      assertFalse(completion.isSucceeded());
      assertFalse(completion.isFailed());
    }
  }

  @Test
  public void testAwaitInterruption() {
    testAwaitSuccess(completion -> completion.await(), Thread.State.WAITING);
    testAwaitSuccess(completion -> completion.await(2000), Thread.State.TIMED_WAITING);
  }

  @Test
  public void testAwaitSuccessInterruption() {
    testAwaitSuccess(completion -> completion.awaitSuccess(), Thread.State.WAITING);
    testAwaitSuccess(completion -> completion.awaitSuccess(2000), Thread.State.TIMED_WAITING);
  }

  private void testAwaitSuccess(Consumer<Completion> consumer, Thread.State expectedState) {
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
      context.async();
    });
    Thread thread = Thread.currentThread();
    Completion completion = suite.run(Vertx.vertx());
    new Thread(() -> {
      while (thread.getState() != expectedState) {
        try {
          Thread.sleep(1);
        } catch (InterruptedException ignore) {
        }
      }
      thread.interrupt();
    }).start();
    try {
      consumer.accept(completion);
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
