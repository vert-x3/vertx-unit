package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.unit.TestSuiteRunner;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestCaseRunner;

import java.util.List;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class TestSuiteRunnerImpl implements TestSuiteRunner {

  private final Handler<Test> before;
  private final Handler<Test> after;
  private List<TestCaseImpl> tests;
  private Handler<Void> endHandler;
  private Handler<TestCaseRunner> handler;

  TestSuiteRunnerImpl(Handler<Test> before, List<TestCaseImpl> tests, Handler<Test> after) {
    this.before = before;
    this.after = after;
    this.tests = tests;
  }

  @Override
  public ReadStream<TestCaseRunner> exceptionHandler(Handler<Throwable> handler) {
    return this;
  }

  @Override
  public ReadStream<TestCaseRunner> handler(Handler<TestCaseRunner> handler) {
    this.handler = handler;
    return this;
  }

  @Override
  public ReadStream<TestCaseRunner> pause() {
    return this;
  }

  @Override
  public ReadStream<TestCaseRunner> resume() {
    return this;
  }

  @Override
  public ReadStream<TestCaseRunner> endHandler(Handler<Void> handler) {
    endHandler = handler;
    return this;
  }

  private Task<Void> build(TestCaseImpl[] tests, int index) {
    if (tests.length > index) {
      Task<?> next = build(tests, index + 1);
      TestCaseImpl test = tests[index];
      TestCaseRunnerImpl runner = new TestCaseRunnerImpl(test.desc, before, test.handler, after, next);
      return (v, context) -> {
        if (handler != null) {
          handler.handle(runner);
        }
        runner.run(null, context);
      };
    } else {
      return (v, context) -> {
        if (endHandler != null) {
          endHandler.handle(null);
        }
      };
    }
  }

  private Task<?> build() {
    return build(tests.toArray(new TestCaseImpl[tests.size()]), 0);
  }

  // For unit testing
  public void run(Context context) {
    context.run(build());
  }

  public void run() {
    Context.create().run(build());
  }

  @Override
  public void run(Vertx vertx) {
    Context.create(vertx).run(build());
  }
}
