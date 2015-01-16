package io.vertx.ext.unit;

import io.vertx.core.Handler;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
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
    Suite suite = Unit.
        test("my_test", test -> {
          count.compareAndSet(0, 1);
        });
    Reporter reporter = new Reporter();
    executor.execute(() -> reporter.run(suite));
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
  public void testRunAsyncTest() throws Exception {
    runAsyncTest(SYNC);
  }

  @Test
  public void testRunAsyncTestAsync() throws Exception {
    runAsyncTest(ASYNC);
  }

  private void runAsyncTest(Executor executor) throws Exception {
    BlockingQueue<Async> queue = new ArrayBlockingQueue<>(1);
    AtomicInteger count = new AtomicInteger();
    Suite suite = Unit.
        test("my_test", test -> {
          count.compareAndSet(0, 1);
          queue.add(test.async());
        });
    Reporter reporter = new Reporter();
    executor.execute(() -> reporter.run(suite));
    Async async = queue.poll(2, TimeUnit.SECONDS);
    assertEquals(1, count.get());
    assertFalse(reporter.completed());
    async.complete();
    assertTrue(reporter.completed());
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
    Suite suite = Unit.
        test("my_test", test -> {
          try {
            thrower.handle(test);
          } catch (Error|RuntimeException e) {
            failure.set(e);
            throw e;
          }
        });
    Reporter reporter = new Reporter();
    executor.execute(() -> reporter.run(suite));
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
  public void asyncTestAsyncFailure() throws Exception {
    asyncTestAsyncFailure(SYNC);
  }

  @Test
  public void asyncTestAsyncFailureAsync() throws Exception {
    asyncTestAsyncFailure(ASYNC);
  }

  private void asyncTestAsyncFailure(Executor executor) throws Exception {
    BlockingQueue<io.vertx.ext.unit.Test> queue = new ArrayBlockingQueue<>(1);
    Suite suite = Unit.test("my_test", test -> {
      test.async();
      queue.add(test);
    });
    Reporter reporter = new Reporter();
    executor.execute(() -> reporter.run(suite));
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
  public void beforeSync() throws Exception {
    before(SYNC);
  }

  @Test
  public void beforeAsync() throws Exception {
    before(ASYNC);
  }

  private void before(Executor executor) throws Exception {
    AtomicInteger count = new AtomicInteger();
    Suite suite = Unit.test("my_test", test -> {
      count.compareAndSet(1, 2);
    }).before(test -> {
      count.compareAndSet(0, 1);
    });
    Reporter reporter = new Reporter();
    executor.execute(() -> reporter.run(suite));
    reporter.await();
    assertEquals(2, count.get());
  }

  @Test
  public void asyncBeforeSync() throws Exception {
    asyncBefore(SYNC);
  }

  @Test
  public void asyncBeforeAsync() throws Exception {
    asyncBefore(ASYNC);
  }

  private void asyncBefore(Executor executor) throws Exception {
    AtomicInteger count = new AtomicInteger();
    BlockingQueue<Async> queue = new ArrayBlockingQueue<>(1);
    Suite suite = Unit.test("my_test", test -> {
      count.compareAndSet(1, 2);
    }).before(test -> {
      count.compareAndSet(0, 1);
      queue.add(test.async());
    });
    Reporter reporter = new Reporter();
    executor.execute(() -> reporter.run(suite));
    Async async = queue.poll(2, TimeUnit.SECONDS);
    async.complete();
    reporter.await();
    assertEquals(2, count.get());
  }

  @Test
  public void beforeFailSync() throws Exception {
    beforeFail(SYNC);
  }

  @Test
  public void beforeFailAsync() throws Exception {
    beforeFail(ASYNC);
  }

  private void beforeFail(Executor executor) throws Exception {
    AtomicInteger count = new AtomicInteger();
    Suite suite = Unit.test("my_test", test -> {
      count.incrementAndGet();
    }).before(test -> {
      throw new RuntimeException();
    });
    Reporter reporter = new Reporter();
    executor.execute(() -> reporter.run(suite));
    reporter.await();
    assertEquals(0, count.get());
  }

  @Test
  public void afterSync() throws Exception {
    after(SYNC);
  }

  @Test
  public void afterAsync() throws Exception {
    after(ASYNC);
  }

  private void after(Executor executor) throws Exception {
    AtomicInteger count = new AtomicInteger();
    BlockingQueue<Async> queue = new ArrayBlockingQueue<>(1);
    Suite suite = Unit.test("my_test", test -> {
      count.compareAndSet(0, 1);
    }).after(test -> {
      count.compareAndSet(1, 2);
      queue.add(test.async());
    });
    Reporter reporter = new Reporter();
    executor.execute(() -> reporter.run(suite));
    Async async = queue.poll(2, TimeUnit.SECONDS);
    assertFalse(reporter.completed());
    assertEquals(2, count.get());
    async.complete();
    reporter.await();
  }

  @Test
  public void asyncAfterSync() throws Exception {
    asyncAfter(SYNC);
  }

  @Test
  public void asyncAfterAsync() throws Exception {
    asyncAfter(ASYNC);
  }

  private void asyncAfter(Executor executor) throws Exception {
    AtomicInteger count = new AtomicInteger();

    Suite suite = Unit.test("my_test", test -> {
      count.compareAndSet(0, 1);
    }).after(test -> {
      count.compareAndSet(1, 2);
    });
    Reporter reporter = new Reporter();
    executor.execute(() -> reporter.run(suite));
    reporter.await();
    assertEquals(2, count.get());
  }

  @Test
  public void failAfterSync() throws Exception {
    failAfter(SYNC);
  }

  @Test
  public void failAfterAsync() throws Exception {
    failAfter(ASYNC);
  }

  private void failAfter(Executor executor) throws Exception {
    AtomicInteger count = new AtomicInteger();
    Suite suite = Unit.test("my_test", test -> {
      count.compareAndSet(0, 1);
      test.fail("the_message");
    }).after(test -> {
      count.compareAndSet(1, 2);
    });
    Reporter reporter = new Reporter();
    executor.execute(() -> reporter.run(suite));
    reporter.await();
    assertEquals(2, count.get());
    assertTrue(reporter.results.get(0).failed());
    assertEquals("the_message", reporter.results.get(0).failure().getMessage());
  }

  static class Reporter{
    private final CountDownLatch latch = new CountDownLatch(1);
    final List<TestResult> results = Collections.synchronizedList(new ArrayList<>());

    void run(Suite suite) {
      SuiteRunner moduleExec = suite.runner();
      moduleExec.handler(testExec -> {
        testExec.completionHandler(results::add);
      });
      moduleExec.endHandler(done -> {
        latch.countDown();
      });
      moduleExec.run();
    }

    void await() {
      try {
        latch.await(10, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        throw new AssertionError(e);
      }
    }
    boolean completed() {
      return latch.getCount() == 0;
    }
  }
}
