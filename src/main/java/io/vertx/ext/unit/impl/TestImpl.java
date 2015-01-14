package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestExec;
import io.vertx.ext.unit.TestResult;
import org.junit.Assert;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

  class Exec implements TestExec {

    volatile Handler<TestResult> completeHandler;
    volatile TestResult result;
    volatile boolean completed;
    volatile boolean invoking;
    volatile Throwable failed;

    @Override
    public String description() {
      return desc;
    }
    void run(Runnable next) {

      Test test = new Test() {

        public void complete() {
          if (!completed) {
            completed = true;
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
            if (failed == null) {
              result = new TestResult() {
                @Override
                public String description() {
                  return desc;
                }
                @Override
                public long time() {
                  return 0;
                }
                @Override
                public Throwable failure() {
                  return null;
                }
                @Override
                public boolean succeeded() {
                  return true;
                }
                @Override
                public boolean failed() {
                  return false;
                }
              };
            } else {
              result = new TestResult() {
                @Override
                public String description() {
                  return desc;
                }
                @Override
                public long time() {
                  return 0;
                }
                @Override
                public Throwable failure() {
                  return failed;
                }
                @Override
                public boolean succeeded() {
                  return false;
                }
                @Override
                public boolean failed() {
                  return true;
                }
              };
            }
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

      //
      try {
        for (Handler<Void> before : module.beforeCallbacks) {
          before.handle(null);
        }
      } catch (Throwable e) {
        completed = true;
        failed = e;
      }
      if (!completed) {
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

      //
      if (!async && !completed) {
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

  public TestImpl.Exec exec() {
    return new Exec();
  }

}
