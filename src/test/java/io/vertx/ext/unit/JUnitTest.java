package io.vertx.ext.unit;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.Timeout;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.test.core.Repeat;
import io.vertx.test.core.RepeatRule;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestName;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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

  public static class SimpleTestSuite {
    static final AtomicReference<TestContext> current = new AtomicReference<>();
    static final AtomicInteger count = new AtomicInteger();
    @Test
    public void testMethod1(TestContext context) {
      count.incrementAndGet();
      current.set(context);
    }

    @Test
    public void testMethod2() {
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
//    assertNotNull(SimpleTestSuite.current.get().vertx());
  }

  public static class AsyncTestSuite {
    @Test
    public void testMethod1(TestContext context) {
      Async async = context.async();
      new Thread(() -> {
        try {
          Thread.sleep(250);
        } catch (InterruptedException ignore) {
        } finally {
          async.complete();
        }
      }).start();
    }
  }

  @Test
  public void testSuiteAsync() {
    Result result = run(AsyncTestSuite.class);
    assertEquals(1, result.getRunCount());
    assertEquals(0, result.getFailureCount());
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
    assertEquals(1, result.getRunCount());
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
    assertEquals(1, result.getRunCount());
    assertEquals(1, result.getFailureCount());
    Failure failure = result.getFailures().get(0);
    assertEquals("the_runtime_exception", failure.getMessage());
    assertSame(TestSuiteRuntimeException.cause, failure.getException());
  }

  public static class TestSuiteTimingOut {
    @Test(timeout = 100L)
    public void testTimingOut(TestContext context) {
      context.async();
    }
  }

  @org.junit.Test
  public void testSuiteTimeout() {
    testTimeout(TestSuiteTimingOut.class);
  }

  public static class TestSuiteTimoutRule {
    @Rule
    public final Timeout timeout = new Timeout(100, TimeUnit.MILLISECONDS);
    @Test
    public void testTimingOut(TestContext context) {
      context.async();
    }
  }

  @org.junit.Test
  public void testTimeoutRule() {
    testTimeout(TestSuiteTimoutRule.class);
  }

  public static class TestSuiteTimoutClassRule {
    @ClassRule
    public static final Timeout timeout = new Timeout(100, TimeUnit.MILLISECONDS);
    @Test
    public void testTimingOut(TestContext context) {
      context.async();
    }
  }

  @org.junit.Test
  public void testTimeoutClassRule() {
    testTimeout(TestSuiteTimoutClassRule.class);
  }

  private void testTimeout(Class<?> testClass) {
    Result result = run(testClass);
    assertEquals(1, result.getRunCount());
    assertEquals(1, result.getFailureCount());
    Failure failure = result.getFailures().get(0);
    assertTrue("Was expecting failure " + failure.getException() + " to be instance of " + TimeoutException.class, failure.getException() instanceof TimeoutException);
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
    assertEquals(1, result.getRunCount());
    assertEquals(1, result.getFailureCount());
    assertTrue(interrupted.get());
  }

  public static class BeforeTestSuite {
    static final AtomicReference<TestContext> current = new AtomicReference<>();
    static final List<String> events = Collections.synchronizedList(new ArrayList<>());
    @Before
    public void before(TestContext context) {
      events.add("before");
      current.set(context);
    }
    @Test
    public void testMethod(TestContext context) {
      events.add("test");
    }
  }

  @Test
  public void testBefore() {
    Result result = run(BeforeTestSuite.class);
    assertEquals(1, result.getRunCount());
    assertEquals(0, result.getFailureCount());
    assertEquals(Arrays.asList("before", "test"), BeforeTestSuite.events);
  }

  public static class FailBeforeTestSuite {
    static final AtomicInteger count = new AtomicInteger();
    @Before
    public void before(TestContext context) {
      throw new RuntimeException();
    }
    @Test
    public void testMethod(TestContext context) {
      count.incrementAndGet();
    }
  }

  @Test
  public void testFailBefore() {
    Result result = run(FailBeforeTestSuite.class);
    assertEquals(1, result.getRunCount());
    assertEquals(1, result.getFailureCount());
    assertEquals(0, FailBeforeTestSuite.count.get());
  }

  public static class AsyncBeforeTestSuite {
    static final List<String> events = Collections.synchronizedList(new ArrayList<>());
    @Before
    public void before(TestContext context) {
      events.add("before");
      Async async = context.async();
      new Thread(() -> {
        try {
          Thread.sleep(250);
        } catch (InterruptedException ignore) {
        } finally {
          events.add("complete");
          async.complete();
        }
      }).start();
    }
    @Test
    public void testMethod(TestContext context) {
      events.add("test");
    }
  }

  @Test
  public void testAsyncBefore() {
    Result result = run(AsyncBeforeTestSuite.class);
    assertEquals(1, result.getRunCount());
    assertEquals(0, result.getFailureCount());
    assertEquals(Arrays.asList("before", "complete", "test"), AsyncBeforeTestSuite.events);
  }

  public static class AfterTestSuite {
    static final AtomicReference<TestContext> current = new AtomicReference<>();
    static final List<String> events = Collections.synchronizedList(new ArrayList<>());
    @After
    public void after(TestContext context) {
      events.add("after");
      current.set(context);
    }
    @Test
    public void testMethod(TestContext context) {
      events.add("test");
    }
  }

  @Test
  public void testAfter() {
    Result result = run(AfterTestSuite.class);
    assertEquals(1, result.getRunCount());
    assertEquals(0, result.getFailureCount());
    assertEquals(Arrays.asList("test", "after"), AfterTestSuite.events);
  }

  public static class AfterFailureTestSuite {
    static final List<String> events = Collections.synchronizedList(new ArrayList<>());
    @After
    public void after(TestContext context) {
      events.add("after");
    }
    @Test
    public void testMethod(TestContext context) {
      events.add("test");
      throw new RuntimeException();
    }
  }

  @Test
  public void testAfterFailure() {
    Result result = run(AfterFailureTestSuite.class);
    assertEquals(1, result.getRunCount());
    assertEquals(1, result.getFailureCount());
    assertEquals(Arrays.asList("test", "after"), AfterFailureTestSuite.events);
  }

  public static class FailAfterTestSuite {
    @After
    public void after(TestContext context) {
      throw new RuntimeException();
    }
    @Test
    public void testMethod(TestContext context) {
    }
  }

  @Test
  public void testFailAfter() {
    Result result = run(FailAfterTestSuite.class);
    assertEquals(1, result.getRunCount());
    assertEquals(1, result.getFailureCount());
  }

  public static class BeforeClassTestSuite {
    static final AtomicReference<TestContext> current = new AtomicReference<>();
    static final List<String> events = Collections.synchronizedList(new ArrayList<>());
    @BeforeClass
    public static void before(TestContext context) {
      events.add("beforeClass");
      current.set(context);
    }
    @Test
    public void testMethod(TestContext context) {
      events.add("test");
    }
  }

  @Test
  public void testBeforeClass() {
    Result result = run(BeforeClassTestSuite.class);
    assertEquals(1, result.getRunCount());
    assertEquals(0, result.getFailureCount());
    assertEquals(Arrays.asList("beforeClass", "test"), BeforeClassTestSuite.events);
  }

  public static class AfterClassTestSuite {
    static final AtomicReference<TestContext> current = new AtomicReference<>();
    static final List<String> events = Collections.synchronizedList(new ArrayList<>());
    @AfterClass
    public static void after(TestContext context) {
      events.add("afterClass");
      current.set(context);
    }
    @Test
    public void testMethod(TestContext context) {
      events.add("test");
    }
  }

  @Test
  public void testAfterClass() {
    Result result = run(AfterClassTestSuite.class);
    assertEquals(1, result.getRunCount());
    assertEquals(0, result.getFailureCount());
    assertEquals(Arrays.asList("test", "afterClass"), AfterClassTestSuite.events);
  }

  public static class AttributesTestSuite {
    static Integer beforeClassCount;
    static Integer beforeMethodCount;
    static Integer testMethodCount;
    static Integer afterMethodCount;
    static Integer afterClassCount;
    static int count = 0;
    @BeforeClass
    public static void beforeClass(TestContext context) {
      beforeClassCount = context.get("count");
      context.put("count", count++);
    }
    @Before
    public void before(TestContext context) {
      beforeMethodCount = context.get("count");
      context.put("count", count++);
    }
    @Test
    public void testMethod(TestContext context) {
      testMethodCount = context.get("count");
      context.put("count", count++);
    }
    @After
    public void after(TestContext context) {
      afterMethodCount = context.get("count");
      context.put("count", count++);
    }
    @AfterClass
    public static void afterClass(TestContext context) {
      afterClassCount = context.get("count");
      context.put("count", count++);
    }
  }

  @Test
  public void testAttributes() {
    Result result = run(AttributesTestSuite.class);
    assertEquals(1, result.getRunCount());
    assertEquals(0, result.getFailureCount());
    assertEquals(5, AttributesTestSuite.count);
    assertEquals(null, AttributesTestSuite.beforeClassCount);
    assertEquals((Integer)0, AttributesTestSuite.beforeMethodCount);
    assertEquals((Integer)1, AttributesTestSuite.testMethodCount);
    assertEquals((Integer)2, AttributesTestSuite.afterMethodCount);
    assertEquals((Integer)0, AttributesTestSuite.afterClassCount);
  }

  private Result run(Class<?> testClass) {
    try {
      return new JUnitCore().run(new VertxUnitRunner(testClass));
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

  public static class MyMethodRule implements MethodRule {

    final AtomicInteger evaluateCount = new AtomicInteger();
    final List<FrameworkMethod> methods = Collections.synchronizedList(new ArrayList<>());
    final List<Object> targets = Collections.synchronizedList(new ArrayList<>());
    final List<Throwable> failures = Collections.synchronizedList(new ArrayList<>());

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
      methods.add(method);
      targets.add(target);
      return new Statement() {
        @Override
        public void evaluate() throws Throwable {
          evaluateCount.incrementAndGet();
          try {
            base.evaluate();
          } catch (Throwable throwable) {
            failures.add(throwable);
            throw throwable;
          }
        }
      };
    }
  }

  public static class MethodRuleTestSuite {

    static final MyMethodRule rule = new MyMethodRule();
    static final AtomicInteger count = new AtomicInteger();

    @Rule
    public MyMethodRule instanceRule = rule;

    @Test
    public void testMethod(TestContext context) {
      count.incrementAndGet();
    }
  }

  @Test
  public void testMethodRule() throws Exception {
    Result result = new JUnitCore().run(new VertxUnitRunner(MethodRuleTestSuite.class));
    assertEquals(0, result.getFailures().size());
    assertEquals(1, MethodRuleTestSuite.count.get());
    assertEquals(0, MethodRuleTestSuite.rule.failures.size());
    assertEquals(1, MethodRuleTestSuite.rule.methods.size());
    assertEquals(MethodRuleTestSuite.class.getDeclaredMethod("testMethod", TestContext.class), MethodRuleTestSuite.rule.methods.get(0).getMethod());
    assertEquals(1, MethodRuleTestSuite.rule.targets.size());
    assertTrue(MethodRuleTestSuite.rule.targets.get(0) instanceof MethodRuleTestSuite);
    assertEquals(1, MethodRuleTestSuite.rule.evaluateCount.get());
  }

  public static class FailingMethodRuleTestSuite {

    static final MyMethodRule rule = new MyMethodRule();

    @Rule
    public MyMethodRule instanceRule = rule;

    @Test
    public void testMethod(TestContext context) {
      throw new RuntimeException();
    }
  }

  @Test
  public void testFailingMethodRule() throws Exception {
    Result result = new JUnitCore().run(new VertxUnitRunner(FailingMethodRuleTestSuite.class));
    assertEquals(1, FailingMethodRuleTestSuite.rule.failures.size());
    assertEquals(1, result.getFailures().size());
    Failure failure = result.getFailures().get(0);
    assertSame(failure.getException(), FailingMethodRuleTestSuite.rule.failures.get(0));
    assertEquals(1, FailingMethodRuleTestSuite.rule.methods.size());
    assertEquals(FailingMethodRuleTestSuite.class.getDeclaredMethod("testMethod", TestContext.class), FailingMethodRuleTestSuite.rule.methods.get(0).getMethod());
    assertEquals(1, FailingMethodRuleTestSuite.rule.targets.size());
    assertTrue(FailingMethodRuleTestSuite.rule.targets.get(0) instanceof FailingMethodRuleTestSuite);
    assertEquals(1, FailingMethodRuleTestSuite.rule.evaluateCount.get());
  }

  public static class AsyncMethodRuleTestSuite {

    static final List<String> events = Collections.synchronizedList(new ArrayList<>());
    static final MethodRule rule = (base, method, target) -> new Statement() {
      @Override
      public void evaluate() throws Throwable {
        events.add("before");
        base.evaluate();
        events.add("after");
      }
    };

    @Rule
    public MethodRule instanceRule = rule;

    @Test
    public void testMethod(TestContext context) {
      events.add("test");
      Async async = context.async();
      new Thread((() -> {
        try {
          Thread.sleep(250);
        } catch (InterruptedException ignore) {
        } finally {
          events.add("complete");
          async.complete();
        }
      })).start();
    }
  }

  @Test
  public void testAsyncMethodRule() throws Exception {
    Result result = run(AsyncMethodRuleTestSuite.class);
    assertEquals(1, result.getRunCount());
    assertEquals(0, result.getFailureCount());
    assertEquals(Arrays.asList("before", "test", "complete", "after"), AsyncMethodRuleTestSuite.events);
  }

  public static class RepeatRuleTestSuite {

    static final List<String> events = Collections.synchronizedList(new ArrayList<>());

    @Rule
    public RepeatRule instanceRule = new RepeatRule();
    private int count = 0;

    @Test
    @Repeat(times = 3)
    public void testMethod(TestContext context) {
      events.add("test" + count);
      Async async = context.async();
      new Thread((() -> {
        try {
          Thread.sleep(250);
        } catch (InterruptedException ignore) {
        } finally {
          events.add("complete" + count++);
          async.complete();
        }
      })).start();
    }
  }

  @Test
  public void testRepeatRule() {
    Result result = run(RepeatRuleTestSuite.class);
    assertEquals(1, result.getRunCount());
    assertEquals(0, result.getFailureCount());
    assertEquals(Arrays.asList("test0", "complete0", "test1", "complete1", "test2", "complete2"), RepeatRuleTestSuite.events);
  }

  public static class UseRunOnContextRule {
    static final ConcurrentMap<String, Context> before = new ConcurrentHashMap<>();
    static final ConcurrentMap<String, Context> method = new ConcurrentHashMap<>();
    static final ConcurrentMap<String, Context> after = new ConcurrentHashMap<>();
    @Rule
    public final TestName testName = new TestName();
    @Rule
    public final RunTestOnContext rule = new RunTestOnContext();
    @Before
    public void before() {
      before.put(testName.getMethodName(), Vertx.currentContext());
    }
    @Test
    public void testMethod1() {
      method.put(testName.getMethodName(), Vertx.currentContext());
    }
    @Test
    public void testMethod2() {
      method.put(testName.getMethodName(), Vertx.currentContext());
    }
    @After
    public void after() {
      after.put(testName.getMethodName(), Vertx.currentContext());
    }
  }

  @Test
  public void testRunTestOnContext() throws Exception {
    Result result = run(UseRunOnContextRule.class);
    assertEquals(2, result.getRunCount());
    assertEquals(0, result.getFailureCount());
    for (String name : Arrays.asList("testMethod1","testMethod2")) {
      Context methodCtx = UseRunOnContextRule.method.get(name);
      Context beforeCtx = UseRunOnContextRule.before.get(name);
      Context afterCtx = UseRunOnContextRule.after.get(name);
      assertNotNull(methodCtx);
      assertSame(methodCtx, beforeCtx);
      assertSame(methodCtx, afterCtx);
    }
    assertNotSame(UseRunOnContextRule.method.get("testMethod1"), UseRunOnContextRule.method.get("testMethod2"));
  }

  public static class StaticUseRunOnContextRule {
    static volatile Context beforeClass;
    static final ConcurrentMap<String, Context> before = new ConcurrentHashMap<>();
    static final ConcurrentMap<String, Context> method = new ConcurrentHashMap<>();
    static final ConcurrentMap<String, Context> after = new ConcurrentHashMap<>();
    static volatile Context afterClass;
    @Rule
    public final TestName testName = new TestName();
    @ClassRule
    public static final RunTestOnContext rule = new RunTestOnContext();
    @BeforeClass
    public static void beforeClass() {
      beforeClass = Vertx.currentContext();
    }
    @Before
    public void before() {
      before.put(testName.getMethodName(), Vertx.currentContext());
    }
    @Test
    public void testMethod1() {
      method.put(testName.getMethodName(), Vertx.currentContext());
    }
    @Test
    public void testMethod2() {
      method.put(testName.getMethodName(), Vertx.currentContext());
    }
    @After
    public void after() {
      after.put(testName.getMethodName(), Vertx.currentContext());
    }
    @AfterClass
    public static void afterClass() {
      afterClass = Vertx.currentContext();
    }
  }

  @Test
  public void testStaticRunTestOnContext() throws Exception {
    Result result = run(StaticUseRunOnContextRule.class);
    assertEquals(2, result.getRunCount());
    assertEquals(0, result.getFailureCount());
    for (String name : Arrays.asList("testMethod1","testMethod2")) {
      Context methodCtx = StaticUseRunOnContextRule.method.get(name);
      Context beforeCtx = StaticUseRunOnContextRule.before.get(name);
      Context afterCtx = StaticUseRunOnContextRule.after.get(name);
      assertNotNull(methodCtx);
      assertSame(methodCtx, beforeCtx);
      assertSame(methodCtx, afterCtx);
    }
    assertSame(StaticUseRunOnContextRule.method.get("testMethod1"), StaticUseRunOnContextRule.method.get("testMethod2"));
    assertSame(StaticUseRunOnContextRule.beforeClass, StaticUseRunOnContextRule.method.get("testMethod1"));
    assertSame(StaticUseRunOnContextRule.afterClass, StaticUseRunOnContextRule.method.get("testMethod1"));
  }
}
