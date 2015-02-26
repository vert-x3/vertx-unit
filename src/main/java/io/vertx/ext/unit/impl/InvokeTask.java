package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.report.TestResult;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class InvokeTask implements Task<Throwable> {

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
  public void execute(Throwable failure, TestContext context) {
    TestImpl test = new TestImpl(this, context, failure);
    if (timeout > 0) {
      Runnable cancel = () -> {
        try {
          Thread.sleep(timeout);
          test.failed(new TimeoutException());
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      };
      new Thread(cancel).start();
    }
    test.run();
  }

  static Task<?> runTestTask(
      String name,
      long timeout,
      Handler<Test> before,
      Handler<Test> test,
      Handler<Test> after,
      Handler<Throwable> unhandledFailureHandler,
      Task<TestResult> next) {

    AtomicLong begin = new AtomicLong();
    AtomicLong duration = new AtomicLong();

    Task<Result> completeTask = (result, executor) -> {
      duration.addAndGet(result.duration());
      executor.run(next, new TestResultImpl(name, begin.get(), duration.get(), result.failure));
    };
    InvokeTask afterTask = after == null ? null : new InvokeTask(after, unhandledFailureHandler, completeTask);
    InvokeTask runnerTask = new InvokeTask(test, timeout, unhandledFailureHandler, (result, executor) -> {
      duration.addAndGet(result.duration());
      if (begin.get() == 0) {
        begin.set(result.beginTime);
      }
      if (afterTask != null) {
        executor.run(afterTask, result.failure);
      } else {
        executor.run(completeTask, result);
      }
    });
    return before == null ? runnerTask : new InvokeTask(before, unhandledFailureHandler, (result, executor) -> {
      begin.set(result.beginTime);
      duration.set(result.duration());
      if (result.failure != null) {
        executor.run(completeTask, result);
      } else {
        executor.run(runnerTask, null);
      }
    });
  }
}
