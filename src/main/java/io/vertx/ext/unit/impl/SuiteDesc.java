package io.vertx.ext.unit.impl;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.unit.Suite;
import io.vertx.ext.unit.SuiteRunner;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SuiteDesc implements Suite {

  final String desc;
  private volatile Handler<Test> before;
  private volatile Handler<Test> after;
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
  public Suite after(Handler<Test> after) {
    this.after = after;
    return this;
  }

  @Override
  public Suite test(String desc, Handler<Test> handler) {
    tests.add(new TestDesc(this, desc, handler));
    return this;
  }

  @Override
  public SuiteRunner runner() {

    class SuiteRunnerImpl implements SuiteRunner {

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

      private Task<?> build(TestDesc[] tests, int index) {
        if (tests.length > index) {
          Task<?> next = build(tests, index + 1);
          TestDesc test = tests[index];
          TestRunnerImpl runner = new TestRunnerImpl(test.desc, before, test.handler, after, next);
          return (o, executor) -> {
            if (testHandler != null) {
              testHandler.handle(runner);
            }
            executor.execute(runner.task, null);
          };
        } else {
          return (o, executor) -> {
            if (endHandler != null) {
              endHandler.handle(null);
            }
          };
        }
      }

      private Task<?> build() {
        return build(tests.toArray(new TestDesc[tests.size()]), 0);
      }

      public void run() {
        build().run(null, new Executor() {
          @Override
          public <T> void execute(Task<T> task, T value) {
            task.run(value, this);
          }
        });
      }

      @Override
      public void runOnContext() {
        runOnContext(Vertx.currentContext());
      }

      @Override
      public void runOnContext(Context context) {
        build().run(null, new Executor() {
          @Override
          public <T> void execute(Task<T> task, T value) {
            context.runOnContext(v -> {
              task.run(value, this);
            });
          }
        });
      }
    }

    return new SuiteRunnerImpl();
  }
}
