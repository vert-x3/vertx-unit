package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.Test;
import org.junit.Assert;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class Task implements Runnable {

  private final Handler<Throwable> next;
  private final Handler<Test> test;
  private volatile boolean completed;
  private volatile boolean running;
  private volatile int asyncCount;
  private volatile Throwable failed;

  public Task(Handler<Test> test, Handler<Throwable> next) {
    this.test = test;
    this.next = next;
  }

  private void tryEnd() {
    if (asyncCount == 0 && !completed) {
      completed = true;
      next.handle(failed);
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

  public void run() {

    Test test = new Test() {

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
          Task.this.failed(err);
          throw err;
        }
      }

      public void fail(String message) {
        try {
          Assert.fail(message);
        } catch (AssertionError err) {
          Task.this.failed(err);
          throw err;
        }
      }
    };

    if (failed == null) {
      running = true;
      try {
        Task.this.test.handle(test);
      } catch (Throwable t) {
        if (failed == null) {
          failed = t;
        }
      } finally {
        running = false;
      }
    }

    //
    tryEnd();
  }
}
