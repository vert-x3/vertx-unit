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
  final Task task;
  volatile Handler<TestResult> completionHandler;

  public TestRunnerImpl(
      String desc,
      Handler<Test> before,
      Handler<Test> test,
      Handler<Test> after,
      Runnable next) {

    final Handler<Throwable> completeTask = failure -> {
      if (completionHandler != null) {
        completionHandler.handle(new TestResultImpl(desc, 0, failure));
      }
      next.run();
    };
    final Task afterTask = after == null ? null : new Task(after, completeTask);
    final Task runnerTask = new Task(test, failure -> {
      if (afterTask != null) {
        afterTask.handle(failure);
      } else {
        completeTask.handle(failure);
      }
    });
    task = before == null ? runnerTask : new Task(before, failure -> {
      if (failure != null) {
        completeTask.handle(failure);
      } else {
        runnerTask.run();
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
