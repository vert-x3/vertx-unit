package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.report.TestResult;

import java.util.concurrent.TimeoutException;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class InvokeTask implements Task<Result> {

  final Task<Result> next;
  final Handler<Test> test;
  final long timeout;

  public InvokeTask(Handler<Test> test, Task<Result> next) {
    this.test = test;
    this.timeout = 0;
    this.next = next;
  }

  public InvokeTask(Handler<Test> test, long timeout, Task<Result> next) {
    this.test = test;
    this.timeout = timeout;
    this.next = next;
  }

  @Override
  public void execute(Result failure, Context context) {
    TestImpl test = new TestImpl(this, context, failure != null ? failure.time : 0, failure != null ? failure.failure : null);
    TestImpl.AsyncImpl async = test.async();
    if (timeout > 0) {
      Runnable cancel = () -> {
        try {
          Thread.sleep(timeout);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
        test.failed(new TimeoutException());
      };
      if (context.vertx() != null) {
        context.vertx().executeBlocking(future -> cancel.run(), null);
      } else {
        new Thread(cancel).start();
      }
    }
    try {
      InvokeTask.this.test.handle(test);
    } catch (Throwable t) {
      test.failed(t);
    } finally {
      async.internalComplete();
    }
  }

  static InvokeTask runTestTask(
      String name,
      Long timeout,
      Handler<Test> before,
      Handler<Test> test,
      Handler<Test> after,
      Task<TestResult> next) {

    Task<Result> completeTask = (result, executor) -> {
      executor.run(next, new TestResultImpl(name, result.time, result.failure));
    };
    InvokeTask afterTask = after == null ? null : new InvokeTask(after, completeTask);
    InvokeTask runnerTask = new InvokeTask(test, timeout, (result, executor) -> {
      if (afterTask != null) {
        executor.run(afterTask, result);
      } else {
        executor.run(completeTask, result);
      }
    });
    return before == null ? runnerTask : new InvokeTask(before, (result, executor) -> {
      if (result.failure != null) {
        executor.run(completeTask, result);
      } else {
        executor.run(runnerTask, null);
      }
    });
  }
}
