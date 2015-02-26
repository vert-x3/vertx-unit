package io.vertx.ext.unit.impl;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.report.TestSuiteReport;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.report.TestCaseReport;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class TestSuiteReportImpl implements TestSuiteReport {

  private final String name;
  private final long timeout;
  private final Handler<Test> before;
  private final Handler<Test> after;
  private final Handler<Test> beforeEach;
  private final Handler<Test> afterEach;
  private TestCaseImpl[] tests;
  private Handler<Void> endHandler;
  private Handler<Throwable> exceptionHandler;
  private Handler<TestCaseReport> handler;

  TestSuiteReportImpl(String name, long timeout, Handler<Test> before, Handler<Test> after,
                      Handler<Test> beforeEach, Handler<Test> afterEach,
                      TestCaseImpl[] tests) {
    this.name = name;
    this.timeout = timeout;
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
  public TestSuiteReport exceptionHandler(Handler<Throwable> handler) {
    exceptionHandler = handler;
    return this;
  }

  @Override
  public TestSuiteReport handler(Handler<TestCaseReport> handler) {
    this.handler = handler;
    return this;
  }

  @Override
  public TestSuiteReport pause() {
    return this;
  }

  @Override
  public TestSuiteReport resume() {
    return this;
  }

  @Override
  public TestSuiteReport endHandler(Handler<Void> handler) {
    endHandler = handler;
    return this;
  }

  private Task<Throwable> build(TestCaseImpl[] tests, int index, Task<Throwable> last) {
    if (tests.length > index) {
      Task<?> next = build(tests, index + 1, last);
      TestCaseImpl test = tests[index];
      TestCaseReportImpl runner = new TestCaseReportImpl(test.desc, timeout, beforeEach, test.handler, afterEach, exceptionHandler, next);
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
    Task<Throwable> last = (failure, context) -> {
      if (failure != null && exceptionHandler != null) {
        exceptionHandler.handle(failure);
      }
      if (endHandler != null) {
        endHandler.handle(null);
      }
    };
    if (after != null) {
      Task<Throwable> next = last;
      last = new InvokeTask(after, exceptionHandler, (result, context) -> {
        next.execute(result.failure, context);
      });
    }
    last = build(tests, 0, last);
    if (before != null) {
      Task<?> next = last;
      last = new InvokeTask(before, exceptionHandler, (result,context) -> {
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
  public void run(TestContext context) {
    context.run(build());
  }

  public void run(Boolean useEventLoop) {
    Context context = null;
    if (useEventLoop == null) {
      context = Vertx.currentContext();
    } else if (useEventLoop) {
      context = Vertx.currentContext();
      if (context == null) {
        throw new IllegalStateException("No event loop, your test should either provide a Vertx instance or " +
            "be executed in a Verticle");
      }
    }
    TestContext.create(null, context).run(build());
  }

  public void run(Vertx vertx, Boolean useEventLoop) {
    Context context = Boolean.FALSE.equals(useEventLoop) ? null : vertx.getOrCreateContext();
    TestContext.create(vertx, context).run(build());
  }
}
