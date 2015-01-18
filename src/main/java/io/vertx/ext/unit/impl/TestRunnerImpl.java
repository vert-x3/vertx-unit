package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestResult;
import io.vertx.ext.unit.TestRunner;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestRunnerImpl implements TestRunner, Task<Void> {

  private final Handler<TestRunner> testHandler;
  private final String desc;
  private final RunTestTask task;
  private volatile Handler<TestResult> completionHandler;

  public TestRunnerImpl(Handler<TestRunner> testHandler,
                        String desc,
                        Handler<Test> before,
                        Handler<Test> test,
                        Handler<Test> after,
                        Task<?> next) {
    this.testHandler = testHandler;
    this.desc = desc;
    this.task = new RunTestTask(desc, before, test, after, (testResult, executor) -> {
      if (completionHandler != null) {
        completionHandler.handle(testResult);
      }
      next.run(null, executor);
    });
  }

  @Override
  public void run(Void aVoid, Executor executor) {
    if (testHandler != null) {
      testHandler.handle(this);
    }
    executor.execute(task.task, null);
  }

  @Override
  public String description() {
    return desc;
  }

  @Override
  public void completionHandler(Handler<TestResult> handler) {
    completionHandler = handler;
  }
}
