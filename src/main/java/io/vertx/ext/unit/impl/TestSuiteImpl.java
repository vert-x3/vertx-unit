package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestCompletion;
import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.report.Reporter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestSuiteImpl implements TestSuite {

  private final String name;
  private volatile Handler<TestContext> before;
  private volatile Handler<TestContext> beforeEach;
  private volatile Handler<TestContext> after;
  private volatile Handler<TestContext> afterEach;
  private final List<TestCaseImpl> testCases = new ArrayList<>();

  public TestSuiteImpl(String name) {
    this.name = name;
  }

  public TestSuiteImpl(Object testSuiteObject) {
    Class<?> suiteClass = testSuiteObject.getClass();
    name = suiteClass.getName();
    for (Method method : suiteClass.getMethods()) {
      int modifiers = method.getModifiers();
      String methodName = method.getName();
      if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers) &&
          Arrays.equals(method.getParameterTypes(), new Class[]{TestContext.class})) {
        Handler<TestContext> handler = context -> {
          try {
            method.invoke(testSuiteObject, context);
          } catch (IllegalAccessException e) {
            Helper.uncheckedThrow(e);
          } catch (InvocationTargetException e) {
            Helper.uncheckedThrow(e.getCause());
          }
        };
        switch (methodName) {
          case "before":
            before(handler);
            break;
          case "after":
            after(handler);
            break;
          case "beforeEach":
            beforeEach(handler);
            break;
          case "afterEach":
            afterEach(handler);
            break;
          default:
            if (methodName.startsWith("test") && methodName.length() > 4) {
              test(methodName, handler);
            }
            break;
        }
      }
    }
  }

  public List<TestCaseImpl> testCases() {
    return testCases;
  }

  @Override
  public TestSuite before(Handler<TestContext> callback) {
    this.before = callback;
    return this;
  }

  @Override
  public TestSuite beforeEach(Handler<TestContext> callback) {
    beforeEach = callback;
    return this;
  }

  @Override
  public TestSuite after(Handler<TestContext> handler) {
    this.after = handler;
    return this;
  }

  @Override
  public TestSuite afterEach(Handler<TestContext> handler) {
    afterEach = handler;
    return this;
  }

  @Override
  public TestSuite test(String name, Handler<TestContext> testCase) {
    testCases.add(new TestCaseImpl(name, testCase));
    return this;
  }

  @Override
  public TestCompletion run() {
    return run(null, new TestOptions());
  }

  @Override
  public TestCompletion run(Vertx vertx) {
    return run(vertx, new TestOptions());
  }

  @Override
  public TestCompletion run(TestOptions options) {
    return run(null, options);
  }

  @Override
  public TestCompletion run(Vertx vertx, TestOptions options) {
    Reporter[] reporters = options  .getReporters().stream().map(reportOptions -> Reporter.reporter(vertx, reportOptions)).toArray(Reporter[]::new);
    ReporterHandler handler = new ReporterHandler(null, reporters);
    runner().
        setVertx(vertx).
        setTimeout(options.getTimeout()).
        setUseEventLoop(options.isUseEventLoop()).
        handler(handler).
        run();
    return handler;
  }

  public TestSuiteRunner runner() {
    return new TestSuiteRunner(name, before, after, beforeEach, afterEach, testCases.toArray(new TestCaseImpl[testCases.size()]));
  }
}
