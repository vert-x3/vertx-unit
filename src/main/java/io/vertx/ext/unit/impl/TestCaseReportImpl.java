package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.report.TestResult;
import io.vertx.ext.unit.report.TestCaseReport;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestCaseReportImpl implements TestCaseReport, Task<Void> {

  private final String name;
  private final Task<?> task;
  private volatile Handler<TestResult> completionHandler;
  private volatile TestResult result;

  public TestCaseReportImpl(String name,
                            long timeout,
                            Handler<TestContext> before,
                            Handler<TestContext> test,
                            Handler<TestContext> after,
                            Handler<Throwable> exceptionHandler,
                            Task<?> next) {
    this.name = name;
    this.task = InvokeTask.runTestTask(name, timeout, before, test, after, exceptionHandler, (testResult, executor) -> {
      result = testResult;
      if (completionHandler != null) {
        completionHandler.handle(testResult);
      }
      next.execute(null, executor);
    });
  }

  @Override
  public void execute(Void v, TestSuiteContext executor) {
    if (result == null) {
      executor.run(task, null);
    }
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public TestCaseReport endHandler(Handler<TestResult> handler) {
    completionHandler = handler;
    if (completionHandler != null && result != null) {
      completionHandler.handle(result);
    }
    return this;
  }
}
