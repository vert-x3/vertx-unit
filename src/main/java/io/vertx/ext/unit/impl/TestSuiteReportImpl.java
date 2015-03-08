package io.vertx.ext.unit.impl;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.report.TestSuiteReport;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.report.TestCaseReport;

import java.util.HashMap;
import java.util.Map;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class TestSuiteReportImpl implements TestSuiteReport {

  private final String name;
  private final long timeout;
  private final Handler<TestContext> before;
  private final Handler<TestContext> after;
  private final Handler<TestContext> beforeEach;
  private final Handler<TestContext> afterEach;
  private final TestCaseImpl[] tests;
  private Handler<Void> endHandler;
  private Handler<Throwable> exceptionHandler;
  private Handler<TestCaseReport> handler;

  TestSuiteReportImpl(String name, long timeout, Handler<TestContext> before, Handler<TestContext> after,
                      Handler<TestContext> beforeEach, Handler<TestContext> afterEach,
                      TestCaseImpl[] tests) {
    this.name = name;
    this.timeout = timeout;
    this.before = before;
    this.after = after;
    this.beforeEach = beforeEach;
    this.afterEach = afterEach;
    this.tests = tests;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public TestSuiteReport exceptionHandler(Handler<Throwable> handler) {
    exceptionHandler = handler;
    return this;
  }

  @Override
  public TestSuiteReport handler(Handler<TestCaseReport> handler) {
    this.handler = handler;
    return this;
  }

  @Override
  public TestSuiteReport pause() {
    return this;
  }

  @Override
  public TestSuiteReport resume() {
    return this;
  }

  @Override
  public TestSuiteReport endHandler(Handler<Void> handler) {
    endHandler = handler;
    return this;
  }

  private Task<?> buildTestCasesTasks(Map<String, Object> attributes, TestCaseImpl[] tests, int index, Task<Result> endTask) {
    if (tests.length > index) {
      Task<?> nextTask = buildTestCasesTasks(attributes, tests, index + 1, endTask);
      TestCaseImpl test = tests[index];
      TestCaseReportImpl runner = new TestCaseReportImpl(test.name, timeout, new HashMap<>(attributes), beforeEach, test.handler, afterEach, exceptionHandler);
      return (v, context) -> {
        if (handler != null) {
          handler.handle(runner);
        }
        Task<?> task = runner.buildTask(nextTask);
        task.execute(null, context);
      };
    } else {
      if (after != null) {
        return new TestContextImpl(new HashMap<>(attributes), after, exceptionHandler, endTask, 0);
      } else {
        return endTask;
      }
    }
  }

  private Task<?> buildTask() {
    Task<Result> endTask = (result, context) -> {
      if (result != null && result.failure != null && exceptionHandler != null) {
        exceptionHandler.handle(result.failure);
      }
      if (endHandler != null) {
        endHandler.handle(null);
      }
    };
    if (before != null) {
      return new TestContextImpl(new HashMap<>(), before, exceptionHandler, result -> {
        if (result.failure == null) {
          Task<?> runTask = buildTestCasesTasks(result.attributes, tests, 0, endTask);
          return (result_, context) -> runTask.execute(null, context);
        } else {
          return endTask;
        }
      }, 0);
    } else {
      return buildTestCasesTasks(new HashMap<>(), tests, 0, endTask);
    }
  }

  // For unit testing
  public void run(TestSuiteContext context) {
    context.run(buildTask());
  }

  public void run(Boolean useEventLoop) {
    Context context = null;
    if (useEventLoop == null) {
      context = Vertx.currentContext();
    } else if (useEventLoop) {
      context = Vertx.currentContext();
      if (context == null) {
        throw new IllegalStateException("No event loop, your test should either provide a Vertx instance or " +
            "be executed in a Verticle");
      }
    }
    new TestSuiteContext(context).run(buildTask());
  }

  public void run(Vertx vertx, Boolean useEventLoop) {
    Context context = Boolean.FALSE.equals(useEventLoop) ? null : vertx.getOrCreateContext();
    Task<?> task = buildTask();
    new TestSuiteContext(context).run(task);
  }
}
