package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestResult;
import io.vertx.ext.unit.TestRunner;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class TestRunnerImpl implements TestRunner {

  private final String desc;
  final TestTask task;
  volatile Handler<TestResult> completionHandler;

  public TestRunnerImpl(
      String desc,
      Handler<Test> before,
      Handler<Test> test,
      Handler<Test> after,
      Task<?> next) {

    final Task<Throwable> completeTask = (failure,executor) -> {
      if (completionHandler != null) {
        completionHandler.handle(new TestResultImpl(desc, 0, failure));
      }
      executor.execute(next, null);
    };
    final TestTask afterTask = after == null ? null : new TestTask(after, completeTask);
    final TestTask runnerTask = new TestTask(test, (failure,executor) -> {
      if (afterTask != null) {
        executor.execute(afterTask, failure);
      } else {
        executor.execute(completeTask, failure);
      }
    });
    task = before == null ? runnerTask : new TestTask(before, (failure,executor) -> {
      if (failure != null) {
        executor.execute(completeTask, failure);
      } else {
        executor.execute(runnerTask, null);
      }
    });

    this.desc = desc;
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
