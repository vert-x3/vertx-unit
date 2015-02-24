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
  final Handler<Test> handler;
  final Handler<Throwable> unhandledFailureHandler;
  final long timeout;

  public InvokeTask(Handler<Test> test, Handler<Throwable> unhandledFailureHandler, Task<Result> next) {
    this.handler = test;
    this.timeout = 0;
    this.next = next;
    this.unhandledFailureHandler = unhandledFailureHandler;
  }

  public InvokeTask(Handler<Test> test, long timeout, Handler<Throwable> unhandledFailureHandler, Task<Result> next) {
    this.handler = test;
    this.timeout = timeout;
    this.next = next;
    this.unhandledFailureHandler = unhandledFailureHandler;
  }

  @Override
  public void execute(Result failure, Context context) {
    TestImpl test = new TestImpl(this, context, failure != null ? failure.time : 0, failure != null ? failure.failure : null);
    if (timeout > 0) {
      Runnable cancel = () -> {
        try {
          Thread.sleep(timeout);
          test.failed(new TimeoutException());
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      };
      if (context.vertx() != null) {
        context.vertx().executeBlocking(future -> cancel.run(), null);
      } else {
        new Thread(cancel).start();
      }
    }
    test.run();
  }

  static InvokeTask runTestTask(
      String name,
      long timeout,
      Handler<Test> before,
      Handler<Test> test,
      Handler<Test> after,
      Handler<Throwable> unhandledFailureHandler,
      Task<TestResult> next) {

    Task<Result> completeTask = (result, executor) -> {
      executor.run(next, new TestResultImpl(name, result.time, result.failure));
    };
    InvokeTask afterTask = after == null ? null : new InvokeTask(after, unhandledFailureHandler, completeTask);
    InvokeTask runnerTask = new InvokeTask(test, timeout, unhandledFailureHandler, (result, executor) -> {
      if (afterTask != null) {
        executor.run(afterTask, result);
      } else {
        executor.run(completeTask, result);
      }
    });
    return before == null ? runnerTask : new InvokeTask(before, unhandledFailureHandler, (result, executor) -> {
      if (result.failure != null) {
        executor.run(completeTask, result);
      } else {
        executor.run(runnerTask, null);
      }
    });
  }
}
