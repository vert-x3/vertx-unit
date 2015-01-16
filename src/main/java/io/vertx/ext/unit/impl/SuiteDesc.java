package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.unit.Suite;
import io.vertx.ext.unit.SuiteRunner;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestResult;
import io.vertx.ext.unit.TestRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SuiteDesc implements Suite {

  final String desc;
  private volatile Handler<Test> before;
  final List<Handler<Void>> afterCallbacks = new ArrayList<>();
  private final List<TestDesc> tests = new ArrayList<>();

  public SuiteDesc() {
    this(null);
  }

  public SuiteDesc(String desc) {
    this.desc = desc;
  }

  @Override
  public Suite before(Handler<Test> before) {
    this.before = before;
    return this;
  }

  @Override
  public Suite after(Handler<Void> callback) {
    afterCallbacks.add(callback);
    return this;
  }

  @Override
  public Suite test(String desc, Handler<Test> handler) {
    tests.add(new TestDesc(this, desc, handler));
    return this;
  }

  @Override
  public SuiteRunner runner() {

    class ModuleExecImpl implements SuiteRunner {

      private Handler<Void> endHandler;
      private Handler<TestRunner> testHandler;

      @Override
      public ReadStream<TestRunner> exceptionHandler(Handler<Throwable> handler) {
        return this;
      }

      @Override
      public ReadStream<TestRunner> handler(Handler<TestRunner> handler) {
        this.testHandler = handler;
        return this;
      }

      @Override
      public ReadStream<TestRunner> pause() {
        return this;
      }

      @Override
      public ReadStream<TestRunner> resume() {
        return this;
      }

      @Override
      public ReadStream<TestRunner> endHandler(Handler<Void> handler) {
        endHandler = handler;
        return this;
      }

      private Runnable build(TestDesc[] tests, int index) {
        if (tests.length > index) {
          Runnable next = build(tests, index + 1);
          TestDesc test = tests[index];

          class TestRunnerImpl implements TestRunner {

            volatile Handler<TestResult> completionHandler;

            final Task runnerTask = new Task(test.handler, t -> {
              TestResultImpl tr = new TestResultImpl(test.desc, 0, t);
              if (completionHandler != null) {
                completionHandler.handle(tr);
              }
              next.run();
            });
            final Task beforeTask = before == null ? null : new Task(before, t -> {
              if (t != null) {
                if (completionHandler != null) {
                  completionHandler.handle(new TestResultImpl(test.desc, 0, t));
                  next.run();
                }
              } else {
                runnerTask.run();
              }
            });

            @Override
            public String description() {
              return test.desc;
            }

            @Override
            public void completionHandler(Handler<TestResult> handler) {
              completionHandler = handler;
            }
          }

          TestRunnerImpl runner = new TestRunnerImpl();
          return () -> {
            if (testHandler != null) {
              testHandler.handle(runner);
            }
            if (runner.beforeTask != null) {
              runner.beforeTask.run();
            } else {
              runner.runnerTask.run();
            }
          };
        } else {
          return () -> {
            if (endHandler != null) {
              endHandler.handle(null);
            }
          };
        }
      }

      public void run() {
        build(tests.toArray(new TestDesc[tests.size()]), 0).run();
      }
    }

    return new ModuleExecImpl();
  }
}
