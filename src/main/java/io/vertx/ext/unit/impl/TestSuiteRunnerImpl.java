package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.report.TestSuiteReport;
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
  private Vertx vertx;
  private Handler<TestSuiteReport> handler;
  private long timeout;

  public TestSuiteRunnerImpl(String name, Handler<Test> before, Handler<Test> after, Handler<Test> beforeEach,
                             Handler<Test> afterEach, TestCaseImpl[] tests) {
    this.name = name;
    this.timeout = 0;
    this.before = before;
    this.after = after;
    this.beforeEach = beforeEach;
    this.afterEach = afterEach;
    this.tests = tests;
  }

  @Override
  public Vertx getVertx() {
    return vertx;
  }

  @Override
  public TestSuiteRunner setVertx(Vertx vertx) {
    this.vertx = vertx;
    return this;
  }

  @Override
  public long getTimeout() {
    return timeout;
  }

  @Override
  public TestSuiteRunner setTimeout(long timeout) {
    this.timeout = timeout;
    return this;
  }

  @Override
  public TestSuiteRunner handler(Handler<TestSuiteReport> reporter) {
    handler = reporter;
    return this;
  }

  @Override
  public void run() {
    TestSuiteReportImpl runner = new TestSuiteReportImpl(name, timeout, before, after, beforeEach, afterEach, tests);
    handler.handle(runner);
    if (vertx != null) {
      runner.run(vertx);
    } else {
      runner.run();
    }
  }
}
