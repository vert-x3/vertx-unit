package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestSuiteReport;
import io.vertx.ext.unit.TestSuiteRunner;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestSuiteRunnerImpl implements TestSuiteRunner {

  private final String name;
  private final Handler<Test> before;
  private final Handler<Test> after;
  private final Handler<Test> beforeEach;
  private final Handler<Test> afterEach;
  private final TestCaseImpl[] tests;
  private final Vertx vertx;
  private Handler<TestSuiteReport> handler;

  public TestSuiteRunnerImpl(String name, Handler<Test> before, Handler<Test> after, Handler<Test> beforeEach,
                             Handler<Test> afterEach, TestCaseImpl[] tests, Vertx vertx) {
    this.name = name;
    this.before = before;
    this.after = after;
    this.beforeEach = beforeEach;
    this.afterEach = afterEach;
    this.tests = tests;
    this.vertx = vertx;
  }

  @Override
  public TestSuiteRunner handler(Handler<TestSuiteReport> reporter) {
    handler = reporter;
    return this;
  }

  @Override
  public void run() {
    TestSuiteReportImpl runner = new TestSuiteReportImpl(name, before, after, beforeEach, afterEach, tests);
    handler.handle(runner);
    if (vertx != null) {
      runner.run(vertx);
    } else {
      runner.run();
    }
  }
}