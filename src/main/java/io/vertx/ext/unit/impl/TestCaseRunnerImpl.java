package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestResult;
import io.vertx.ext.unit.TestCaseRunner;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestCaseRunnerImpl implements TestCaseRunner, Task<Void> {

  private final String desc;
  private final InvokeTask task;
  private volatile Handler<TestResult> completionHandler;
  private volatile TestResult result;

  public TestCaseRunnerImpl(String desc,
                            Handler<Test> before,
                            Handler<Test> test,
                            Handler<Test> after,
                            Task<?> next) {
    this.desc = desc;
    this.task = InvokeTask.runTestTask(desc, before, test, after, (testResult, executor) -> {
      result = testResult;
      if (completionHandler != null) {
        completionHandler.handle(testResult);
      }
      next.execute(null, executor);
    });
  }

  @Override
  public void execute(Void v, Context executor) {
    if (result == null) {
      executor.run(task, null);
    }
  }

  @Override
  public String description() {
    return desc;
  }

  @Override
  public void endHandler(Handler<TestResult> handler) {
    completionHandler = handler;
    if (completionHandler != null && result != null) {
      completionHandler.handle(result);
    }
  }
}
