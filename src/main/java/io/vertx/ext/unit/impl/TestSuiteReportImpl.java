package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.unit.TestSuiteReport;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestCaseReport;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class TestSuiteReportImpl implements TestSuiteReport {

  private final String name;
  private final Handler<Test> before;
  private final Handler<Test> after;
  private final Handler<Test> beforeEach;
  private final Handler<Test> afterEach;
  private TestCaseImpl[] tests;
  private Handler<Void> endHandler;
  private Handler<Throwable> exceptionHandler;
  private Handler<TestCaseReport> handler;

  TestSuiteReportImpl(String name, Handler<Test> before, Handler<Test> after,
                      Handler<Test> beforeEach, Handler<Test> afterEach,
                      TestCaseImpl[] tests) {
    this.name = name;
    this.before = before;
    this.after = after;
    this.beforeEach = beforeEach;
    this.afterEach = afterEach;
    this.tests = tests;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public ReadStream<TestCaseReport> exceptionHandler(Handler<Throwable> handler) {
    exceptionHandler = handler;
    return this;
  }

  @Override
  public ReadStream<TestCaseReport> handler(Handler<TestCaseReport> handler) {
    this.handler = handler;
    return this;
  }

  @Override
  public ReadStream<TestCaseReport> pause() {
    return this;
  }

  @Override
  public ReadStream<TestCaseReport> resume() {
    return this;
  }

  @Override
  public ReadStream<TestCaseReport> endHandler(Handler<Void> handler) {
    endHandler = handler;
    return this;
  }

  private Task<Result> build(TestCaseImpl[] tests, int index, Task<Result> last) {
    if (tests.length > index) {
      Task<?> next = build(tests, index + 1, last);
      TestCaseImpl test = tests[index];
      TestCaseReportImpl runner = new TestCaseReportImpl(test.desc, beforeEach, test.handler, afterEach, next);
      return (v, context) -> {
        if (handler != null) {
          handler.handle(runner);
        }
        runner.execute(null, context);
      };
    } else {
      return last;
    }
  }

  private Task<?> build() {
    Task<Result> last = (result, context) -> {
      if (result != null && result.failure != null && exceptionHandler != null) {
        exceptionHandler.handle(result.failure);
      }
      if (endHandler != null) {
        endHandler.handle(null);
      }
    };
    if (after != null) {
      Task<Result> next = last;
      last = new InvokeTask(after, next::execute);
    }
    last = build(tests, 0, last);
    if (before != null) {
      Task<?> next = last;
      last = new InvokeTask(before, (result,context) -> {
        if (result.failure == null) {
          context.run(next, null);
        } else {
          if (exceptionHandler != null) {
            exceptionHandler.handle(result.failure);
          }
          if (endHandler != null) {
            endHandler.handle(null);
          }
        }
      });
    }
    return last;
  }

  // For unit testing
  public void run(Context context) {
    context.run(build());
  }

  public void run() {
    Context.create().run(build());
  }

  public void run(Vertx vertx) {
    Context.create(vertx).run(build());
  }
}
