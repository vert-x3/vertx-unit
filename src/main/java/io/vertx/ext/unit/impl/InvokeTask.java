package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.report.TestResult;
import org.junit.Assert;

import java.util.LinkedList;
import java.util.concurrent.TimeoutException;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class InvokeTask implements Task<Result> {

  private final Task<Result> next;
  private final Handler<Test> test;
  private final long timeout;

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

    class TestImpl implements Test {

      private boolean completed;
      private Throwable failed;
      private long time;
      private final LinkedList<Async> asyncs = new LinkedList<>();

      public TestImpl(long time, Throwable failed) {
        this.failed = failed;
        this.time = time - System.currentTimeMillis();
      }

      private void tryEnd() {
        boolean run = false;
        synchronized (this) {
          if (asyncs.isEmpty() && !completed) {
            completed = true;
            run = true;
            time += System.currentTimeMillis();
          }
        }
        if (run) {
          context.run(next, new Result(time, failed));
        }
      }

      private void failed(Throwable t) {
        synchronized (this) {
          if (!completed) {
            failed = t;
          }
        }
        while (true) {
          Async async;
          synchronized (this) {
            async = asyncs.peekFirst();
          }
          if (async == null) {
            break;
          } else {
            async.complete();
          }
        }
      }

      @Override
      public Vertx vertx() {
        return context.vertx();
      }

      @Override
      public Async async() {
        synchronized (this) {
          if (!completed) {
            Async async = new Async() {
              @Override
              public boolean complete() {
                boolean completed;
                synchronized (TestImpl.this) {
                  completed = asyncs.remove(this);
                }
                if (completed) {
                  tryEnd();
                  return true;
                } else {
                  return false;
                }
              }
            };
            asyncs.add(async);
            return async;
          } else {
            throw new IllegalStateException("Test already completed");
          }
        }
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
    TestImpl test = new TestImpl(failure != null ? failure.time : 0, failure != null ? failure.failure : null);
    Async async = test.async();

    //
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
      async.complete();
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
