package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestResult;
import org.junit.Assert;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class InvokeTask implements Task<Result> {

  private final Task<Result> next;
  private final Handler<Test> test;

  public InvokeTask(Handler<Test> test, Task<Result> next) {
    this.test = test;
    this.next = next;
  }

  @Override
  public void execute(Result failure, Context executor) {

    class TestImpl implements Test {

      private volatile boolean completed;
      private volatile boolean running;
      private volatile int asyncCount;
      private volatile Throwable failed;
      private volatile long time;


      private void tryEnd() {
        if (asyncCount == 0 && !completed) {
          completed = true;
          time += System.currentTimeMillis();
          executor.run(next, new Result(time, failed));
        }
      }

      private void failed(Throwable t) {
        if (asyncCount > 0) {
          failed = t;
          asyncCount = 0;
          if (!running) {
            tryEnd();
          }
        }
      }

      @Override
      public Vertx vertx() {
        return executor.vertx();
      }

      @Override
      public Async async() {
        asyncCount++;
        return new Async() {
          boolean called = false;
          @Override
          public void complete() {
            if (!called) {
              called = true;
              if (--asyncCount == 0 && !running) {
                tryEnd();
              }
            } else {
              throw new IllegalStateException("Already completed");
            }
          }
        };
      }

      public void assertTrue(boolean condition) {
        try {
          Assert.assertTrue(condition);
        } catch (AssertionError err) {
          failed(err);
          throw err;
        }
      }

      public void fail(String message) {
        try {
          Assert.fail(message);
        } catch (AssertionError err) {
          failed(err);
          throw err;
        }
      }
    };

    //
    TestImpl test = new TestImpl();

    test.failed = failure != null ? failure.failure : null;
    test.time = failure != null ? failure.time : 0;
    test.running = true;
    test.time -= System.currentTimeMillis();
    try {
      InvokeTask.this.test.handle(test);
    } catch (Throwable t) {
      if (test.failed == null) {
        test.failed = t;
      }
    } finally {
      test.running = false;
    }

    //
    test.tryEnd();
  }

  static InvokeTask runTestTask(
      String desc,
      Handler<Test> before,
      Handler<Test> test,
      Handler<Test> after,
      Task<TestResult> next) {

    Task<Result> completeTask = (result, executor) -> {
      executor.run(next, new TestResultImpl(desc, result.time, result.failure));
    };
    InvokeTask afterTask = after == null ? null : new InvokeTask(after, completeTask);
    InvokeTask runnerTask = new InvokeTask(test, (result, executor) -> {
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
