package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.Test;
import org.junit.Assert;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class InvokeTask implements Task<Throwable> {

  private final Task<Throwable> next;
  private final Handler<Test> test;

  public InvokeTask(Handler<Test> test, Task<Throwable> next) {
    this.test = test;
    this.next = next;
  }

  @Override
  public void run(Throwable failure, Executor executor) {

    class TestImpl implements Test {

      private volatile boolean completed;
      private volatile boolean running;
      private volatile int asyncCount;
      private volatile Throwable failed;

      private void tryEnd() {
        if (asyncCount == 0 && !completed) {
          completed = true;
          executor.execute(next, failed);
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

    test.failed = failure;
    test.running = true;
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
}
