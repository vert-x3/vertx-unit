package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestResult;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class RunTestTask {

  final InvokeTask task;

  public RunTestTask(
      String desc,
      Handler<Test> before,
      Handler<Test> test,
      Handler<Test> after,
      Task<TestResult> next) {

    final Task<Throwable> completeTask = (failure,executor) -> {
      executor.execute(next, new TestResultImpl(desc, 0, failure));
    };
    final InvokeTask afterTask = after == null ? null : new InvokeTask(after, completeTask);
    final InvokeTask runnerTask = new InvokeTask(test, (failure,executor) -> {
      if (afterTask != null) {
        executor.execute(afterTask, failure);
      } else {
        executor.execute(completeTask, failure);
      }
    });
    task = before == null ? runnerTask : new InvokeTask(before, (failure,executor) -> {
      if (failure != null) {
        executor.execute(completeTask, failure);
      } else {
        executor.execute(runnerTask, null);
      }
    });
  }
}
