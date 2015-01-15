package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestExec;
import io.vertx.ext.unit.TestResult;
import org.junit.Assert;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class TestExecImpl implements TestExec, Runnable {

  private final Runnable next;
  private final TestDesc testDesc;
  private final Handler<TestExec> execHandler;
  private volatile Handler<TestResult> completeHandler;
  private volatile boolean completed;
  private volatile boolean running;
  private volatile int asyncCount;
  private volatile TestResult result;
  private volatile Throwable failed;

  public TestExecImpl(TestDesc testDesc, Handler<TestExec> execHandler, Runnable next) {
    this.testDesc = testDesc;
    this.execHandler = execHandler;
    this.next = next;
  }

  @Override
  public String description() {
    return testDesc.desc;
  }

  private void tryEnd() {
    if (asyncCount == 0 && !completed) {
      completed = true;
      try {
        for (Handler<Void> after : testDesc.module.afterCallbacks) {
          after.handle(null);
        }
      } catch (Throwable t) {
        if (failed == null) {
          failed = t;
        }
      }
      result = new TestResultImpl(testDesc.desc, 0, failed);
      if (completeHandler != null) {
        completeHandler.handle(result);
      }
      next.run();
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

    if (execHandler != null) {
      execHandler.handle(this);
    }

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
          TestExecImpl.this.failed(err);
          throw err;
        }
      }

      public void fail(String message) {
        try {
          Assert.fail(message);
        } catch (AssertionError err) {
          TestExecImpl.this.failed(err);
          throw err;
        }
      }
    };

    try {
      for (Handler<Void> before : testDesc.module.beforeCallbacks) {
        before.handle(null);
      }
    } catch (Throwable e) {
      failed = e;
    }

    if (failed == null) {
      running = true;
      try {
        testDesc.handler.handle(test);
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

  @Override
  public void completionHandler(Handler<TestResult> handler) {
    if (completeHandler == null) {
      completeHandler = handler;
      if (result != null) {
        completeHandler.handle(result);
      }
    }
  }
}
