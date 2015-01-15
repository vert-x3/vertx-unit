package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestExec;
import io.vertx.ext.unit.TestResult;
import org.junit.Assert;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class TestExecImpl implements TestExec, Runnable {

  enum Status {
    IN_PROGRESS, COMPLETED, ENDED
  }

  private final Runnable next;
  private final TestDesc testDesc;
  private final Handler<TestExec> execHandler;
  private volatile Handler<TestResult> completeHandler;
  private volatile TestResult result;
  private volatile Status status = Status.IN_PROGRESS;
  private volatile boolean running;
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
    if (status == Status.COMPLETED && !running) {
      status = Status.ENDED;
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

  private boolean complete() {
    if (status == Status.IN_PROGRESS) {
      status = Status.COMPLETED;
      tryEnd();
      return true;
    } else {
      return false;
    }
  }

  private void failed(Throwable t) {
    switch (status) {
      case IN_PROGRESS:
        failed = t;
        status = Status.COMPLETED;
        tryEnd();
        break;
      case COMPLETED:
        failed = t;
        break;
    }
  }

  public void run() {

    if (execHandler != null) {
      execHandler.handle(this);
    }

    Test test = new Test() {

      public void complete() {
        if (!TestExecImpl.this.complete()) {
          fail("Already completed");
        }
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
      status = Status.COMPLETED;
      failed = e;
    }
    if (status != Status.COMPLETED) {
      running = true;
      try {
        testDesc.handler.handle(test);
      } catch (Throwable t) {
        status = Status.COMPLETED;
        if (failed == null) {
          failed = t;
        }
      } finally {
        running = false;
      }
    }
    if (!testDesc.async) {
      complete();
    }
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
