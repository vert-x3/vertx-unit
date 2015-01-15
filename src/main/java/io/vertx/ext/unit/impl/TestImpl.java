package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestExec;
import io.vertx.ext.unit.TestResult;
import org.junit.Assert;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestImpl {

  final ModuleImpl module;
  final String desc;
  final Handler<Test> handler;
  final boolean async;

  public TestImpl(ModuleImpl module, String desc, boolean async, Handler<Test> handler) {
    this.module = module;
    this.async = async;
    this.desc = desc;
    this.handler = handler;
  }

  class ExecTask extends Task implements TestExec {

    final Handler<TestExec> execHandler;
    volatile Handler<TestResult> completeHandler;
    volatile TestResult result;
    final AtomicBoolean completed = new AtomicBoolean();
    volatile boolean invoking;
    volatile Throwable failed;

    public ExecTask(Handler<TestExec> execHandler) {
      this.execHandler = execHandler;
    }

    @Override
    public String description() {
      return desc;
    }

    void run(Runnable next) {

      if (execHandler != null) {
        execHandler.handle(this);
      }

      Test test = new Test() {

        public void complete() {
          if (completed.compareAndSet(false, true)) {
            if (!invoking) {
              try {
                for (Handler<Void> after : module.afterCallbacks) {
                  after.handle(null);
                }
              } catch (Throwable t) {
                if (failed == null) {
                  failed = t;
                }
              }
            }
            result = new TestResultImpl(desc, 0, failed);
            if (completeHandler != null) {
              completeHandler.handle(result);
            }
            if (next != null) {
              next.run();
            }
          } else {
            fail("Already completed");
          }
        }

        public void assertTrue(boolean condition) {
          try {
            Assert.assertTrue(condition);
          } catch (AssertionError err) {
            if (failed == null) {
              failed = err;
            }
            throw err;
          }
        }

        public void fail(String message) {
          try {
            Assert.fail(message);
          } catch (AssertionError err) {
            if (failed == null) {
              failed = err;
            }
            throw err;
          }
        }
      };

      try {
        for (Handler<Void> before : module.beforeCallbacks) {
          before.handle(null);
        }
      } catch (Throwable e) {
        completed.set(true);
        failed = e;
      }
      if (!completed.get()) {
        invoking = true;
        try {
          handler.handle(test);
        } catch (Throwable t) {
          if (failed == null) {
            failed = t;
          }
        } finally {
          invoking = false;
        }
      }
      if (!async && !completed.get()) {
        test.complete();
      }
    }

    @Override
    public void completeHandler(Handler<TestResult> handler) {
      if (completeHandler == null) {
        completeHandler = handler;
        if (result != null) {
          completeHandler.handle(result);
        }
      }
    }
  }

  public Task exec(Handler<TestExec> handler) {
    return new ExecTask(handler);
  }
}
