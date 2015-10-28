package io.vertx.ext.unit;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.unit.impl.TestSuiteImpl;
import io.vertx.ext.unit.impl.TestSuiteRunner;
import io.vertx.ext.unit.report.TestResult;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 */
public abstract class TestSuiteTestBase {

  protected Function<TestSuiteImpl, TestSuiteRunner> getRunner;
  protected Consumer<TestSuiteRunner> run;
  protected Consumer<Async> completeAsync;

  public TestSuiteTestBase() {
  }

  void run(TestSuite suite, TestReporter reporter) {
    run.accept(getRunner.apply((TestSuiteImpl) suite).setReporter(reporter));
  }

  void run(TestSuite suite, TestReporter reporter, long timeout) {
    run.accept(getRunner.apply((TestSuiteImpl) suite).setReporter(reporter).setTimeout(timeout));
  }

  protected boolean checkTest(TestContext test) {
    return true;
  }

  @Test
  public void runTest() {
    AtomicInteger count = new AtomicInteger();
    AtomicBoolean sameContext = new AtomicBoolean();
    TestSuite suite = TestSuite.create("my_suite").
        test("my_test", context -> {
          sameContext.set(checkTest(context));
          count.compareAndSet(0, 1);
        });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    reporter.await();
    assertTrue(sameContext.get());
    assertEquals(1, count.get());
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    TestResult result = reporter.results.get(0);
    assertEquals("my_test", result.name());
    assertTrue(result.succeeded());
    assertFalse(result.failed());
    assertNull(result.failure());
  }

  @Test
  public void runTestWithAsyncCompletion() throws Exception {
    BlockingQueue<Async> queue = new ArrayBlockingQueue<>(1);
    AtomicInteger count = new AtomicInteger();
    TestSuite suite = TestSuite.create("my_suite").
        test("my_test", context -> {
          count.compareAndSet(0, 1);
          queue.add(context.async());
        });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    Async async = queue.poll(2, TimeUnit.SECONDS);
    assertEquals(1, count.get());
    assertFalse(reporter.completed());
    completeAsync.accept(async);
    reporter.await();
    assertTrue(reporter.completed());
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    TestResult result = reporter.results.get(0);
    assertEquals("my_test", result.name());
    assertTrue(result.succeeded());
    assertFalse(result.failed());
    assertNull(result.failure());
  }

  @Test
  public void runTestWithFailAfterAsync() throws Exception {
    RuntimeException failure = new RuntimeException();
    BlockingQueue<Async> queue = new ArrayBlockingQueue<>(1);
    TestSuite suite = TestSuite.create("my_suite").
        test("my_test", context -> {
          queue.add(context.async());
          throw failure;
        });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    reporter.await();
    Async async = queue.poll(2, TimeUnit.SECONDS);
    async.complete();
    assertTrue(reporter.completed());
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    TestResult result = reporter.results.get(0);
    assertEquals("my_test", result.name());
    assertFalse(result.succeeded());
    assertTrue(result.failed());
    assertNotNull(result.failure());
    assertSame(failure, result.failure().cause());
  }

  @Test
  public void runTestWithFailBeforeAsync() throws Exception {
    AtomicReference<AssertionError> failure = new AtomicReference<>();
    BlockingQueue<Async> queue = new ArrayBlockingQueue<>(1);
    TestSuite suite = TestSuite.create("my_suite").
        test("my_test", context -> {
          try {
            context.fail();
          } catch (AssertionError e) {
            failure.set(e);
          }
          queue.add(context.async());
        });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    reporter.await();
    Async async = queue.poll(2, TimeUnit.SECONDS);
    async.complete();
    assertTrue(reporter.completed());
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    TestResult result = reporter.results.get(0);
    assertEquals("my_test", result.name());
    assertFalse(result.succeeded());
    assertTrue(result.failed());
    assertNotNull(result.failure());
    assertSame(failure.get(), result.failure().cause());
  }

  @Test
  public void runTestWithAsyncCompletionCompletedInTest() throws Exception {
    AtomicBoolean ok = new AtomicBoolean();
    TestSuite suite = TestSuite.create("my_suite").
        test("my_test", context -> {
          Async async = context.async();
          async.complete();
          try {
            async.complete();
          } catch (IllegalStateException ignore) {
          }
          ok.set(true);
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
    assertTrue(result.succeeded());
    assertFalse(result.failed());
    assertNull(result.failure());
  }

  @Test
  public void runTestWithAsyncCompletionAfterFailureInTest() throws Exception {
    AtomicBoolean completed = new AtomicBoolean();
    TestSuite suite = TestSuite.create("my_suite").
        test("my_test", context -> {
          Async async = context.async();
          try {
            context.fail("msg");
          } catch (AssertionError ignore) {
          }
          async.complete();
          completed.set(true);
        });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    reporter.await();
    assertTrue(completed.get());
    assertTrue(reporter.completed());
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    TestResult result = reporter.results.get(0);
    assertEquals("my_test", result.name());
    assertTrue(result.failed());
  }

  @Test
  public void runTestWithAssertionError() {
    failTest(context -> context.fail("message_failure"));
  }

  @Test
  public void runTestWithEmptyRuntimeException() {
    failTest(context -> { throw new RuntimeException(); });
  }

  @Test
  public void runTestWithRuntimeException() {
    failTest(context -> { throw new RuntimeException("message_failure"); });
  }

  private void failTest(Handler<TestContext> thrower) {
    AtomicReference<Throwable> failure = new AtomicReference<>();
    TestSuite suite = TestSuite.create("my_suite").
        test("my_test", context -> {
          try {
            thrower.handle(context);
          } catch (Error | RuntimeException e) {
            failure.set(e);
            throw e;
          }
        });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    reporter.await();
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    TestResult result = reporter.results.get(0);
    assertEquals("my_test", result.name());
    assertFalse(result.succeeded());
    assertTrue(result.failed());
    assertNotNull(result.failure());
    assertSame(failure.get().getMessage(), result.failure().message());
    assertSame(failure.get(), result.failure().cause());
  }

  @Test
  public void runTestWithAsyncFailure() throws Exception {
    BlockingQueue<TestContext> queue = new ArrayBlockingQueue<>(1);
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
      context.async();
      queue.add(context);
    });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    assertFalse(reporter.completed());
    TestContext test = queue.poll(2, TimeUnit.SECONDS);
    try {
      test.fail("the_message");
    } catch (AssertionError ignore) {
    }
    reporter.await();
    assertTrue(reporter.completed());
  }

  @Test
  public void reportFailureAfterTestCompleted() {
    AtomicReference<TestContext> testRef = new AtomicReference<>();
    TestSuite suite = TestSuite.
        create("my_suite").
        test("my_test_1", testRef::set).
        test("my_test_2", context -> {
          try {
            testRef.get().fail();
          } catch (AssertionError e) {
          }
        });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    reporter.await();
    assertEquals(1, reporter.exceptions.size());
    assertEquals(2, reporter.results.size());
    TestResult result = reporter.results.get(0);
    assertEquals("my_test_1", result.name());
    assertTrue(result.succeeded());
    assertNull(result.failure());
    result = reporter.results.get(1);
    assertEquals("my_test_2", result.name());
    assertTrue(result.succeeded());
    assertNull(result.failure());
  }

  @Test
  public void runBefore() throws Exception {
    for (int i = 0;i < 2;i++) {
      AtomicInteger count = new AtomicInteger();
      AtomicBoolean sameContext = new AtomicBoolean();
      int val = i;
      TestSuite suite = TestSuite.create("my_suite").
          test("my_test_1", context -> {
            sameContext.set(checkTest(context));
            count.compareAndSet(1, 2);
          }).test("my_test_2", context -> {
        if (val == 0) {
          count.compareAndSet(3, 4);
        } else {
          count.compareAndSet(2, 3);
        }
      });
      if (i == 0) {
        suite = suite.beforeEach(context -> {
          count.incrementAndGet();
        });
      } else {
        suite = suite.before(context -> {
          count.incrementAndGet();
        });
      }
      TestReporter reporter = new TestReporter();
      run(suite, reporter);
      reporter.await();
      assertEquals(i == 0 ? 4 : 3, count.get());
      assertTrue(sameContext.get());
      assertEquals(0, reporter.exceptions.size());
      assertEquals(2, reporter.results.size());
      assertEquals("my_test_1", reporter.results.get(0).name());
      assertTrue(reporter.results.get(0).succeeded());
      assertEquals("my_test_2", reporter.results.get(1).name());
      assertTrue(reporter.results.get(1).succeeded());
    }
  }

  @Test
  public void runBeforeWithAsyncCompletion() throws Exception {
    for (int i = 0;i < 2;i++) {
      AtomicInteger count = new AtomicInteger();
      AtomicBoolean sameContext = new AtomicBoolean();
      BlockingQueue<Async> queue = new ArrayBlockingQueue<>(1);
      TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
        count.compareAndSet(1, 2);
        sameContext.set(checkTest(context));
      });
      if (i == 0) {
        suite = suite.before(context -> {
          count.compareAndSet(0, 1);
          queue.add(context.async());
        });
      } else {
        suite = suite.beforeEach(context -> {
          count.compareAndSet(0, 1);
          queue.add(context.async());
        });
      }
      TestReporter reporter = new TestReporter();
      run(suite, reporter);
      Async async = queue.poll(2, TimeUnit.SECONDS);
      completeAsync.accept(async);
      reporter.await();
      assertEquals(2, count.get());
      assertTrue(sameContext.get());
      assertEquals(0, reporter.exceptions.size());
      assertEquals(1, reporter.results.size());
      assertEquals("my_test", reporter.results.get(0).name());
      assertTrue(reporter.results.get(0).succeeded());
    }
  }

  @Test
  public void failBefore() throws Exception {
    for (int i = 0;i < 2;i++) {
      AtomicInteger count = new AtomicInteger();
      TestSuite suite = TestSuite.create("my_suite").
          test("my_test_1", context -> count.incrementAndGet()).
          test("my_test_2", context -> count.incrementAndGet());
      if (i == 0) {
        suite.before(context -> {
          throw new RuntimeException();
        });
      } else {
        AtomicBoolean failed = new AtomicBoolean();
        suite.beforeEach(context -> {
          if (failed.compareAndSet(false, true)) {
            throw new RuntimeException();
          }
        });
      }
      TestReporter reporter = new TestReporter();
      run(suite, reporter);
      reporter.await();
      if (i == 0) {
        assertEquals(0, count.get());
        assertEquals(0, reporter.results.size());
        assertEquals(1, reporter.exceptions.size());
      } else {
        assertEquals(1, count.get());
        assertEquals(0, reporter.exceptions.size());
        assertEquals(2, reporter.results.size());
        assertEquals("my_test_1", reporter.results.get(0).name());
        assertTrue(reporter.results.get(0).failed());
        assertEquals("my_test_2", reporter.results.get(1).name());
        assertTrue(reporter.results.get(1).succeeded());
      }
    }
  }

  @Test
  public void runAfterWithAsyncCompletion() throws Exception {
    for (int i = 0;i < 2;i++) {
      AtomicInteger count = new AtomicInteger();
      BlockingQueue<Async> queue = new ArrayBlockingQueue<>(1);
      TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
        count.compareAndSet(0, 1);
      });
      if (i == 0) {
        suite = suite.after(context -> {
          count.compareAndSet(1, 2);
          queue.add(context.async());
        });
      } else {
        suite = suite.afterEach(context -> {
          count.compareAndSet(1, 2);
          queue.add(context.async());
        });
      }
      TestReporter reporter = new TestReporter();
      run(suite, reporter);
      Async async = queue.poll(2, TimeUnit.SECONDS);
      assertFalse(reporter.completed());
      assertEquals(2, count.get());
      completeAsync.accept(async);
      reporter.await();
      assertEquals(0, reporter.exceptions.size());
      assertEquals(1, reporter.results.size());
      assertEquals("my_test", reporter.results.get(0).name());
      assertTrue(reporter.results.get(0).succeeded());
    }
  }

  @Test
  public void runAfter() throws Exception {
    for (int i = 0;i < 2;i++) {
      AtomicInteger count = new AtomicInteger();
      AtomicBoolean sameContext = new AtomicBoolean();
      int val = i;
      TestSuite suite = TestSuite.create("my_suite").
          test("my_test_1", context -> {
            count.compareAndSet(0, 1);
          }).
          test("my_test_2", context -> {
            if (val == 0) {
              count.compareAndSet(1, 2);
            } else {
              count.compareAndSet(2, 3);
            }
          });
      if (i == 0) {
        suite = suite.after(context -> {
          sameContext.set(checkTest(context));
          count.incrementAndGet();
        });
      } else {
        suite = suite.afterEach(context -> {
          sameContext.set(checkTest(context));
          count.incrementAndGet();
        });
      }
      TestReporter reporter = new TestReporter();
      run(suite, reporter);
      reporter.await();
      assertEquals(i == 0 ? 3 : 4, count.get());
      assertTrue(sameContext.get());
      assertEquals(0, reporter.exceptions.size());
      assertEquals(2, reporter.results.size());
      assertEquals("my_test_1", reporter.results.get(0).name());
      assertTrue(reporter.results.get(0).succeeded());
      assertEquals("my_test_2", reporter.results.get(1).name());
      assertTrue(reporter.results.get(1).succeeded());
    }
  }

  @Test
  public void afterIsRunAfterFailure() throws Exception {
    for (int i = 0;i < 2;i++) {
      AtomicInteger count = new AtomicInteger();
      TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
        count.compareAndSet(0, 1);
        context.fail("the_message");
      });
      if (i == 0) {
        suite = suite.after(context -> count.compareAndSet(1, 2));
      } else {
        suite = suite.afterEach(context -> count.compareAndSet(1, 2));
      }
      TestReporter reporter = new TestReporter();
      run(suite, reporter);
      reporter.await();
      assertEquals(2, count.get());
      assertEquals(0, reporter.exceptions.size());
      assertEquals(1, reporter.results.size());
      assertEquals("my_test", reporter.results.get(0).name());
      assertTrue(reporter.results.get(0).failed());
      assertEquals("the_message", reporter.results.get(0).failure().message());
    }
  }

  @Test
  public void failAfter() throws Exception {
    for (int i = 0;i < 2;i++) {
      AtomicInteger count = new AtomicInteger();
      int val = i;
      TestSuite suite = TestSuite.create("my_suite").
          test("my_test_1", context -> count.compareAndSet(0, 1)).
          test("my_test_2", context -> {
            if (val == 0) {
              count.compareAndSet(1, 2);
            } else {
              count.compareAndSet(2, 3);
            }
          });
      if (i == 0) {
        suite = suite.after(context -> {
          count.incrementAndGet();
          context.fail("the_message");
        });
      } else {
        AtomicBoolean failed = new AtomicBoolean();
        suite = suite.afterEach(context -> {
          count.incrementAndGet();
          if (failed.compareAndSet(false, true)) {
            context.fail("the_message");
          }
        });
      }
      TestReporter reporter = new TestReporter();
      run(suite, reporter);
      reporter.await();
      if (i == 0) {
        assertEquals(3, count.get());
        assertEquals(1, reporter.exceptions.size());
        assertEquals(2, reporter.results.size());
        assertEquals("my_test_1", reporter.results.get(0).name());
        assertTrue(reporter.results.get(0).succeeded());
        assertEquals("my_test_2", reporter.results.get(1).name());
        assertTrue(reporter.results.get(1).succeeded());
      } else {
        assertEquals(4, count.get());
        assertEquals(0, reporter.exceptions.size());
        assertEquals(2, reporter.results.size());
        assertEquals("my_test_1", reporter.results.get(0).name());
        assertTrue(reporter.results.get(0).failed());
        assertEquals("my_test_2", reporter.results.get(1).name());
        assertTrue(reporter.results.get(1).succeeded());
      }
    }
  }

  @Test
  public void timeExecution() {
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
      Async async = context.async();
      new Thread() {
        @Override
        public void run() {
          try {
            Thread.sleep(15);
          } catch (InterruptedException ignore) {
          } finally {
            async.complete();
          }
        }
      }.start();
    });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    reporter.await();
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertEquals("my_test", reporter.results.get(0).name());
    assertFalse(reporter.results.get(0).failed());
    assertTrue(reporter.results.get(0).durationTime() >= 15);
  }

  @Test
  public void testRepeatAllPass() throws Exception {
    AtomicInteger count = new AtomicInteger();
    TestSuite suite = TestSuite.create("my_suite").test("my_test", 3, ctx -> {
      count.incrementAndGet();
    });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    reporter.await();
    assertEquals(1, reporter.results.size());
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertEquals(3, count.get());
    assertEquals("my_test", reporter.results.get(0).name());
    assertFalse(reporter.results.get(0).failed());
  }

  @Test
  public void testRepeatOneFailure() throws Exception {
    AtomicInteger count = new AtomicInteger();
    TestSuite suite = TestSuite.create("my_suite").test("my_test", 3, ctx -> {
      if (count.incrementAndGet() == 3) {
        ctx.fail();
      }
    });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    reporter.await();
    assertEquals(1, reporter.results.size());
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertEquals(3, count.get());
    assertEquals("my_test", reporter.results.get(0).name());
    assertTrue(reporter.results.get(0).failed());
  }

  @Test
  public void testRepeatBeforeAfterEach() throws Exception {
    List<String> events = Collections.synchronizedList(new ArrayList<String>());
    TestSuite suite = TestSuite.create("my_suite").beforeEach(ctx -> {
      events.add("before");
    }).test("my_test", 3, ctx -> {
      events.add("test");
    }).afterEach(ctx -> {
      events.add("after");
    });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    reporter.await();
    assertEquals(1, reporter.results.size());
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertEquals(Arrays.asList("before", "test", "after", "before", "test", "after", "before", "test", "after"), events);
    assertEquals("my_test", reporter.results.get(0).name());
    assertFalse(reporter.results.get(0).failed());
  }

  @Test
  public void testTimeout() throws Exception {
    BlockingQueue<Async> queue = new ArrayBlockingQueue<>(2);
    TestSuite suite = TestSuite.create("my_suite").
        test("my_test0", context -> queue.add(context.async())).
        test("my_test1", context -> queue.add(context.async()));
    TestReporter reporter = new TestReporter();
    run(suite, reporter, 300); // 300 ms
    reporter.await(); // Wait until timeout and suite completes
    assertEquals(2, reporter.results.size());
    for (int i = 0;i < 2;i++) {
      Async async = queue.poll(2, TimeUnit.SECONDS);
      assertEquals("my_test" + i, reporter.results.get(i).name());
      assertTrue(reporter.results.get(i).failed());
      assertNotNull(reporter.results.get(i).failure());
      assertTrue(reporter.results.get(i).failure().cause() instanceof TimeoutException);
      async.complete();
    }
  }

  @Test
  public void testTimeoutThreadGarbaged() throws Exception {
    Set<String> threadNames = Thread.getAllStackTraces().
        keySet().
        stream().
        filter(t -> t.getName().startsWith("vert.x-unit-timeout-thread-")).
        map(Thread::getName).
        collect(Collectors.toSet());
    AtomicReference<Thread> timeoutThread = new AtomicReference<>();
    TestSuite suite = TestSuite.create("my_suite").
        test("my_test0", context -> {
          while (timeoutThread.get() == null) {
            for (Thread thread : Thread.getAllStackTraces().keySet()) {
              if (thread.getName().startsWith("vert.x-unit-timeout-thread-")) {
                if (!threadNames.contains(thread.getName())) {
                  timeoutThread.set(thread);
                }
              }
            }
          }
        });
    TestReporter reporter = new TestReporter();
    run(suite, reporter, 20000);
    reporter.await();
    assertNotNull(timeoutThread.get());
    timeoutThread.get().join(20000);
    assertEquals(timeoutThread.get().getState(), Thread.State.TERMINATED);
  }

  @Test
  public void testScopedAttributes() throws Exception {
    List<Integer> before = Collections.synchronizedList(new ArrayList<>());
    List<Integer> beforeEach = Collections.synchronizedList(new ArrayList<>());
    AtomicInteger count = new AtomicInteger();
    AtomicInteger test0 = new AtomicInteger(-1);
    AtomicInteger test1 = new AtomicInteger(-1);
    List<Integer> afterEach = Collections.synchronizedList(new ArrayList<>());
    List<Integer> after = Collections.synchronizedList(new ArrayList<>());
    TestSuite suite = TestSuite.create("my_suite").before(context -> {
      Integer value = context.get("value");
      if (value != null) {
        before.add(value);
      }
      context.put("value", -10);
    }).beforeEach(context -> {
      Integer value = context.get("value");
      beforeEach.add(value);
      context.put("value", count.getAndIncrement());
    }).test("my_test0", context -> {
      int value = context.get("value");
      test0.set(value);
      context.put("value", value * 2);
    }).test("my_test1", context -> {
      int value = context.get("value");
      test1.set(context.get("value"));
      context.put("value", value * 2);
    }).afterEach(context -> {
      int value = context.get("value");
      afterEach.add(value);
    }).after(context -> {
      int value = context.get("value");
      after.add(value);
    });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    reporter.await();
    assertEquals(Arrays.<Integer>asList(), before);
    assertEquals(Arrays.<Integer>asList(-10, -10), beforeEach);
    assertEquals(0, test0.get());
    assertEquals(1, test1.get());
    assertEquals(Arrays.<Integer>asList(0, 2), afterEach);
    assertEquals(Arrays.<Integer>asList(-10), after);
    assertEquals(0, reporter.exceptions.size());
    assertEquals(2, reporter.results.size());
    assertEquals("my_test0", reporter.results.get(0).name());
    assertFalse(reporter.results.get(0).failed());
    assertEquals("my_test1", reporter.results.get(1).name());
    assertFalse(reporter.results.get(1).failed());
  }

  @Test
  public void testAttributesOperations() throws Exception {
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
      context.assertEquals(null, context.get("value"));
      context.assertEquals(null, context.put("value", 4));
      context.assertEquals(4, context.get("value"));
      context.assertEquals(4, context.put("value", 5));
      context.assertEquals(5, context.get("value"));
      context.assertEquals(5, context.remove("value"));
      context.assertEquals(null, context.get("value"));
    });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    reporter.await();
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertEquals("my_test", reporter.results.get(0).name());
    assertFalse(reporter.results.get(0).failed());
  }

  @Test
  public void testAssertAsyncSuccessHandlerSucceeded() throws Exception {
    BlockingQueue<Handler<AsyncResult<String>>> handlerQueue = new ArrayBlockingQueue<>(1);
    BlockingQueue<String> resultQueue = new ArrayBlockingQueue<>(1);
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
      handlerQueue.add(context.<String>asyncAssertSuccess(r -> {
        resultQueue.add(r);
      }));
    });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    Handler<AsyncResult<String>> handler = handlerQueue.poll(2, TimeUnit.SECONDS);
    handler.handle(Future.succeededFuture("foo"));
    String result = resultQueue.poll(2, TimeUnit.SECONDS);
    assertEquals("foo", result);
    reporter.await();
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertEquals("my_test", reporter.results.get(0).name());
    assertFalse(reporter.results.get(0).failed());
  }

  @Test
  public void testAssertAsyncSuccessHandlerThrowsFailure() throws Exception {
    RuntimeException cause = new RuntimeException();
    BlockingQueue<Handler<AsyncResult<String>>> handlerQueue = new ArrayBlockingQueue<>(1);
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
      handlerQueue.add(context.<String>asyncAssertSuccess(r -> {
        throw cause;
      }));
    });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    Handler<AsyncResult<String>> handler = handlerQueue.poll(2, TimeUnit.SECONDS);
    handler.handle(Future.succeededFuture("foo"));
    reporter.await();
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertTrue(reporter.results.get(0).failed());
    assertSame(cause, reporter.results.get(0).failure().cause());
  }

  @Test
  public void testAssertAsyncSuccessHandlerSucceededAsync() throws Exception {
    BlockingQueue<Handler<AsyncResult<String>>> handlerQueue = new ArrayBlockingQueue<>(1);
    BlockingQueue<Async> resultQueue = new ArrayBlockingQueue<>(1);
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
      handlerQueue.add(context.<String>asyncAssertSuccess(r -> {
        resultQueue.add(context.async());
      }));
    });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    Handler<AsyncResult<String>> handler = handlerQueue.poll(2, TimeUnit.SECONDS);
    handler.handle(Future.succeededFuture("foo"));
    Async result = resultQueue.poll(2, TimeUnit.SECONDS);
    assertFalse(reporter.completed());
    result.complete();
    reporter.await();
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertEquals("my_test", reporter.results.get(0).name());
    assertFalse(reporter.results.get(0).failed());
  }

  @Test
  public void testAssertAsyncSuccessFailed() throws Exception {
    BlockingQueue<Handler<AsyncResult<String>>> handlerQueue = new ArrayBlockingQueue<>(1);
    AtomicBoolean called = new AtomicBoolean();
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
      handlerQueue.add(context.<String>asyncAssertSuccess(r -> {
        called.set(true);
      }));
    });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    Handler<AsyncResult<String>> handler = handlerQueue.poll(2, TimeUnit.SECONDS);
    RuntimeException cause = new RuntimeException();
    handler.handle(Future.failedFuture(cause));
    reporter.await();
    assertFalse(called.get());
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertEquals("my_test", reporter.results.get(0).name());
    assertTrue(reporter.results.get(0).failed());
    assertSame(cause, reporter.results.get(0).failure().cause());
  }

  @Test
  public void testAssertAsyncFailureHandlerSucceeded() throws Exception {
    BlockingQueue<Handler<AsyncResult<String>>> handlerQueue = new ArrayBlockingQueue<>(1);
    BlockingQueue<Throwable> resultQueue = new ArrayBlockingQueue<>(1);
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
      handlerQueue.add(context.<String>asyncAssertFailure(resultQueue::add));
    });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    Handler<AsyncResult<String>> handler = handlerQueue.poll(2, TimeUnit.SECONDS);
    Throwable expected = new Throwable();
    handler.handle(Future.failedFuture(expected));
    Throwable cause = resultQueue.poll(2, TimeUnit.SECONDS);
    assertSame(expected, cause);
    reporter.await();
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertEquals("my_test", reporter.results.get(0).name());
    assertFalse(reporter.results.get(0).failed());
  }

  @Test
  public void testAssertAsyncFailureHandlerThrowsFailure() throws Exception {
    RuntimeException cause = new RuntimeException();
    BlockingQueue<Handler<AsyncResult<String>>> handlerQueue = new ArrayBlockingQueue<>(1);
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
      handlerQueue.add(context.<String>asyncAssertFailure(r -> {
        throw cause;
      }));
    });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    Handler<AsyncResult<String>> handler = handlerQueue.poll(2, TimeUnit.SECONDS);
    handler.handle(Future.failedFuture(new Throwable()));
    reporter.await();
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertTrue(reporter.results.get(0).failed());
    assertSame(cause, reporter.results.get(0).failure().cause());
  }

  @Test
  public void testAssertAsyncFailureHandlerSucceededAsync() throws Exception {
    BlockingQueue<Handler<AsyncResult<String>>> handlerQueue = new ArrayBlockingQueue<>(1);
    BlockingQueue<Async> resultQueue = new ArrayBlockingQueue<>(1);
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
      handlerQueue.add(context.<String>asyncAssertFailure(r -> {
        resultQueue.add(context.async());
      }));
    });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    Handler<AsyncResult<String>> handler = handlerQueue.poll(2, TimeUnit.SECONDS);
    handler.handle(Future.failedFuture(new Throwable()));
    Async result = resultQueue.poll(2, TimeUnit.SECONDS);
    assertFalse(reporter.completed());
    result.complete();
    reporter.await();
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertEquals("my_test", reporter.results.get(0).name());
    assertFalse(reporter.results.get(0).failed());
  }

  @Test
  public void testAssertAsyncFailureFailed() throws Exception {
    BlockingQueue<Handler<AsyncResult<String>>> handlerQueue = new ArrayBlockingQueue<>(1);
    AtomicBoolean called = new AtomicBoolean();
    TestSuite suite = TestSuite.create("my_suite").test("my_test", context -> {
      handlerQueue.add(context.<String>asyncAssertFailure(r -> {
        called.set(true);
      }));
    });
    TestReporter reporter = new TestReporter();
    run(suite, reporter);
    Handler<AsyncResult<String>> handler = handlerQueue.poll(2, TimeUnit.SECONDS);
    handler.handle(Future.succeededFuture("foo"));
    reporter.await();
    assertFalse(called.get());
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertEquals("my_test", reporter.results.get(0).name());
    assertTrue(reporter.results.get(0).failed());
  }
}
