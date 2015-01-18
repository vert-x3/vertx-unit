package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestResult;
import io.vertx.ext.unit.TestRunner;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestRunnerImpl implements TestRunner, Task<Void> {

  private final String desc;
  private final RunTestTask task;
  private volatile Handler<TestResult> completionHandler;
  private volatile TestResult result;

  public TestRunnerImpl(String desc,
                        Handler<Test> before,
                        Handler<Test> test,
                        Handler<Test> after,
                        Task<?> next) {
    this.desc = desc;
    this.task = new RunTestTask(desc, before, test, after, (testResult, executor) -> {
      result = testResult;
      if (completionHandler != null) {
        completionHandler.handle(testResult);
      }
      next.run(null, executor);
    });
  }

  @Override
  public void run(Void v, Context executor) {
    if (result == null) {
      executor.run(task.task, null);
    }
  }

  @Override
  public String description() {
    return desc;
  }

  @Override
  public void completionHandler(Handler<TestResult> handler) {
    completionHandler = handler;
    if (completionHandler != null && result != null) {
      completionHandler.handle(result);
    }
  }

  @Override
  public void run() {
    task.task.run(null, Context.create());
  }

  @Override
  public void run(Vertx vertx) {
    task.task.run(null, Context.create(vertx));
  }
}
