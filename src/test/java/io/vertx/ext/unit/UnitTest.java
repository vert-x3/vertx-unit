package io.vertx.ext.unit;

import io.vertx.core.Handler;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public class UnitTest {

  private static final Executor ASYNC = command -> new Thread(command).start();
  private static final Executor SYNC = Runnable::run;

  @Test
  public void testRunTest() {
    runTest(SYNC);
  }

  @Test
  public void testRunTestAsync() {
    runTest(ASYNC);
  }

  private void runTest(Executor executor) {
    AtomicInteger count = new AtomicInteger();
    Module module = Unit.
        test("my_test", test -> {
          count.compareAndSet(0, 1);
        });
    Reporter reporter = new Reporter();
    executor.execute(() -> module.execute(reporter));
    reporter.await();
    assertEquals(1, count.get());
    assertEquals(1, reporter.results.size());
    TestResult result = reporter.results.get(0);
    assertEquals("my_test", result.description());
    assertTrue(result.succeeded());
    assertFalse(result.failed());
    assertNull(result.failure());
  }

  @Test
  public void testRunAsyncTest() {
    runAsyncTest(SYNC);
  }

  @Test
  public void testRunAsyncTestAsync() {
    runAsyncTest(ASYNC);
  }

  private void runAsyncTest(Executor executor) {
    AtomicInteger count = new AtomicInteger();
    Module module = Unit.
        asyncTest("my_test", test -> {
          count.compareAndSet(0, 1);
          test.complete();
        }).setThreadCheckEnabled(false);
    Reporter reporter = new Reporter();
    executor.execute(() -> module.execute(reporter));
    reporter.await();
    assertEquals(1, count.get());
    assertEquals(1, reporter.results.size());
    TestResult result = reporter.results.get(0);
    assertEquals("my_test", result.description());
    assertTrue(result.succeeded());
    assertFalse(result.failed());
    assertNull(result.failure());
  }

  @Test
  public void testFailAssertionError() {
    failTest(SYNC, test -> test.fail("message_failure"));
  }

  @Test
  public void testFailAssertionErrorAsync() {
    failTest(ASYNC, test -> test.fail("message_failure"));
  }

  @Test
  public void testFailRuntimeException() {
    failTest(SYNC, test -> { throw new RuntimeException("message_failure"); });
  }

  @Test
  public void testFailRuntimeExceptionAsync() {
    failTest(ASYNC, test -> { throw new RuntimeException("message_failure"); });
  }

  private void failTest(Executor executor, Handler<io.vertx.ext.unit.Test> thrower) {
    AtomicReference<Throwable> failure = new AtomicReference<>();
    Module module = Unit.
        test("my_test", test -> {
          try {
            thrower.handle(test);
          } catch (Error|RuntimeException e) {
            failure.set(e);
            throw e;
          }
        });
    Reporter reporter = new Reporter();
    executor.execute(() -> module.execute(reporter));
    reporter.await();
    assertEquals(1, reporter.results.size());
    TestResult result = reporter.results.get(0);
    assertEquals("my_test", result.description());
    assertFalse(result.succeeded());
    assertTrue(result.failed());
    assertNotNull(result.failure());
    assertSame(failure.get(), result.failure());
  }

  static class Reporter implements Handler<ModuleExec> {
    private final CountDownLatch latch = new CountDownLatch(1);
    final List<TestResult> results = Collections.synchronizedList(new ArrayList<TestResult>());
    @Override
    public void handle(ModuleExec event) {
      event.handler(exec -> {
        exec.completeHandler(results::add);
      });
      event.endHandler(v -> {
        latch.countDown();
      });
    }
    void await() {
      try {
        latch.await(10, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        throw new AssertionError(e);
      }
    }
  }
}
