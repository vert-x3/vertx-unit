package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.unit.SuiteRunner;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestRunner;

import java.util.ArrayList;
import java.util.List;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class SuiteRunnerImpl implements SuiteRunner {

  private final Handler<Test> before;
  private final Handler<Test> after;
  private List<TestDesc> tests;
  private Handler<Void> endHandler;
  private Handler<TestRunner> handler;

  SuiteRunnerImpl(Handler<Test> before, List<TestDesc> tests, Handler<Test> after) {
    this.before = before;
    this.after = after;
    this.tests = tests;
  }

  @Override
  public ReadStream<TestRunner> exceptionHandler(Handler<Throwable> handler) {
    return this;
  }

  @Override
  public ReadStream<TestRunner> handler(Handler<TestRunner> handler) {
    this.handler = handler;
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

  private Task<Void> build(TestDesc[] tests, int index) {
    if (tests.length > index) {
      Task<?> next = build(tests, index + 1);
      TestDesc test = tests[index];
      TestRunnerImpl runner = new TestRunnerImpl(test.desc, before, test.handler, after, next);
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
    return build(tests.toArray(new TestDesc[tests.size()]), 0);
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
