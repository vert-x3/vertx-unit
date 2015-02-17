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
  private TestCaseImpl[] tests;
  private Handler<Void> endHandler;
  private Handler<TestCaseReport> handler;

  TestSuiteReportImpl(String name, Handler<Test> before, TestCaseImpl[] tests, Handler<Test> after) {
    this.name = name;
    this.before = before;
    this.after = after;
    this.tests = tests;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public ReadStream<TestCaseReport> exceptionHandler(Handler<Throwable> handler) {
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

  private Task<Void> build(TestCaseImpl[] tests, int index) {
    if (tests.length > index) {
      Task<?> next = build(tests, index + 1);
      TestCaseImpl test = tests[index];
      TestCaseReportImpl runner = new TestCaseReportImpl(test.desc, before, test.handler, after, next);
      return (v, context) -> {
        if (handler != null) {
          handler.handle(runner);
        }
        runner.execute(null, context);
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
    return build(tests, 0);
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
