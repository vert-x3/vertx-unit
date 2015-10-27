package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.report.TestResult;
import io.vertx.ext.unit.report.TestCaseReport;

import java.util.Map;
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestCaseReportImpl implements TestCaseReport {

  private final String name;
  private final long timeout;
  private final int repeat;
  private final Map<String, Object> attributes;
  private final Handler<TestContext> before;
  private final Handler<TestContext> test;
  private final Handler<TestContext> after;
  private final Handler<Throwable> unhandledFailureHandler;
  private volatile Handler<TestResult> completionHandler;

  public TestCaseReportImpl(String name,
                            long timeout,
                            int repeat,
                            Map<String, Object> attributes,
                            Handler<TestContext> before,
                            Handler<TestContext> test,
                            Handler<TestContext> after,
                            Handler<Throwable> unhandledFailureHandler) {

    this.attributes = attributes;
    this.timeout = timeout;
    this.repeat = repeat;
    this.name = name;
    this.before = before;
    this.test = test;
    this.after = after;
    this.unhandledFailureHandler = unhandledFailureHandler;
  }

  Task<?> buildTask(Task<?> nextTask) {
    // Build task assemblies for the test case
    Task<Result> task = (result, context) -> {
      if (completionHandler != null) {
        completionHandler.handle(new TestResultImpl(name, result.beginTime, result.duration(), result.failure));
      }
      nextTask.execute(null, context);
    };
    for (int count = 0;count < repeat;count++) {
      task = runTask(task);
    }
    return task;
  }

  private Task<Result> runTask(Task<Result> next) {
    Task<Result> afterHandler;
    if (after != null) {
      afterHandler = new TestContextImpl(attributes, after, unhandledFailureHandler, next, timeout);
    } else {
      afterHandler = next;
    }
    Task<Result> testHandler = new TestContextImpl(attributes, test, unhandledFailureHandler, afterHandler, timeout);
    if (before != null) {
      Function<Result, Task<Result>> tmp = result -> {
        if (result.failure != null) {
          return next;
        } else {
          return testHandler;
        }
      };
      return new TestContextImpl(attributes, before, unhandledFailureHandler, tmp, timeout);
    } else {
      return testHandler;
    }
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public TestCaseReport endHandler(Handler<TestResult> handler) {
    completionHandler = handler;
    return this;
  }
}
