package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestResult;
import org.junit.Assert;

import java.util.LinkedList;

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
      private volatile Throwable failed;
      private volatile long time;
      private final LinkedList<Async> asyncs = new LinkedList<>();

      private void tryEnd() {
        if (asyncs.isEmpty() && !completed) {
          completed = true;
          time += System.currentTimeMillis();
          executor.run(next, new Result(time, failed));
        }
      }

      private void failed(Throwable t) {
        if (asyncs.size() > 0) {
          failed = t;
          while (asyncs.size() > 0) {
            asyncs.peekFirst().complete();
          }
        }
      }

      @Override
      public Vertx vertx() {
        return executor.vertx();
      }

      @Override
      public Async async() {
        Async async = new Async() {
          @Override
          public boolean complete() {
            if (asyncs.remove(this)) {
              tryEnd();
              return true;
            } else {
              return false;
            }
          }
        };
        asyncs.add(async);
        return async;
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
    test.time -= System.currentTimeMillis();
    Async async = test.async();
    try {
      InvokeTask.this.test.handle(test);
    } catch (Throwable t) {
      test.failed(t);
    } finally {
      async.complete();
    }
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
