package io.vertx.ext.unit.junit;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.impl.Helper;
import io.vertx.ext.unit.impl.TestCaseImpl;
import io.vertx.ext.unit.impl.TestSuiteImpl;
import io.vertx.ext.unit.impl.TestSuiteRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

/**
 * Runner for running a Vertx Unit suite as a JUnit test class.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VertxUnitRunner extends Runner {

  private final Class<?> testClass;
  private Description suiteDesc;
  private TestSuiteImpl suite;
  private Long timeout;

  public VertxUnitRunner(Class<?> testClass) throws InitializationError {
    this(testClass, null);
  }

  private static final Class<?>[] abc = {TestContext.class};

  private static List<Method> scan(TestClass testClass, Class<? extends Annotation> annotation) {
    List<Method> methods = new ArrayList<>();
    for (FrameworkMethod testCaseFrameworkMethod : testClass.getAnnotatedMethods(annotation)) {
      Method method = testCaseFrameworkMethod.getMethod();
      Class<?>[] paramTypes = method.getParameterTypes();
      if (Modifier.isPublic(method.getModifiers()) && paramTypes.length == 0 || Arrays.equals(abc, paramTypes)) {
        methods.add(method);
      }
    }
    return methods;
  }

  public VertxUnitRunner(Class<?> testClass, Long timeout) throws InitializationError {
    TestClass tc = new TestClass(testClass);

    // Create an instance now, maybe defer to later.
    Object instance;
    try {
      instance = testClass.newInstance();
    } catch (Exception e) {
      throw new InitializationError(e);
    }
    Supplier<?> supplier = () -> instance;

    this.suite = (TestSuiteImpl) TestSuite.create(testClass.getName());

    scan(tc, Test.class).forEach(method -> suite.test(method.getName(), Helper.invoker(method, supplier)));
    scan(tc, Before.class).forEach(method -> suite.beforeEach(Helper.invoker(method, supplier)));
    scan(tc, After.class).forEach(method -> suite.afterEach(Helper.invoker(method, supplier)));
    scan(tc, BeforeClass.class).forEach(method -> suite.before(Helper.invoker(method, supplier)));
    scan(tc, AfterClass.class).forEach(method -> suite.after(Helper.invoker(method, supplier)));

    this.timeout = timeout;
    this.testClass = testClass;
    this.suiteDesc = Description.createTestDescription(testClass.getName(), "foo");
    for (TestCaseImpl testCase : suite.testCases()) {
      suiteDesc.addChild(Description.createTestDescription(testClass.getName(), testCase.name()));
    }
  }

  @Override
  public Description getDescription() {
    Description suiteDesc = Description.createSuiteDescription(testClass.getName());
    suiteDesc.addChild(this.suiteDesc);
    return suiteDesc;
  }

  @Override
  public void run(RunNotifier notifier) {
    CountDownLatch doneLatch = new CountDownLatch(1);
    Vertx vertx = Vertx.vertx();
    TestSuiteRunner runner = suite.runner();
    if (timeout != null) {
      runner.setTimeout(timeout);
    }
    runner.setVertx(vertx);
    runner.handler(testSuiteReport -> {
      testSuiteReport.handler(testCaseReport -> {
        Description testCaseDesc = Description.createTestDescription(testClass.getName(), testCaseReport.name());
        notifier.fireTestStarted(testCaseDesc);
        testCaseReport.endHandler(result -> {
          if (result.succeeded()) {
            notifier.fireTestFinished(testCaseDesc);
          } else {
            notifier.fireTestFailure(new Failure(testCaseDesc, result.failure().cause()));
          }
        });
      });
      testSuiteReport.exceptionHandler(err -> {
        notifier.fireTestFailure(new Failure(suiteDesc, err));
      });
      testSuiteReport.endHandler(v -> {
        doneLatch.countDown();
      });
    });
    runner.run();
    try {
      doneLatch.await();
      CountDownLatch closeLatch = new CountDownLatch(1);
      vertx.close(v -> {
        closeLatch.countDown();
      });
      closeLatch.await();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
