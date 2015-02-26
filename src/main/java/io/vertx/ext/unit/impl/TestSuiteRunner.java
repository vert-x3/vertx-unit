package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.report.TestSuiteReport;

/**
 * The test suite runner.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestSuiteRunner {

  private final String name;
  private final Handler<Test> before;
  private final Handler<Test> after;
  private final Handler<Test> beforeEach;
  private final Handler<Test> afterEach;
  private final TestCaseImpl[] tests;
  private Vertx vertx;
  private Handler<TestSuiteReport> handler;
  private long timeout;
  private Boolean useEventLoop;

  public TestSuiteRunner(String name, Handler<Test> before, Handler<Test> after, Handler<Test> beforeEach,
                         Handler<Test> afterEach, TestCaseImpl[] tests) {
    this.name = name;
    this.timeout = 0;
    this.before = before;
    this.after = after;
    this.beforeEach = beforeEach;
    this.afterEach = afterEach;
    this.tests = tests;
  }

  public Boolean isUseEventLoop() {
    return useEventLoop;
  }

  public TestSuiteRunner setUseEventLoop(Boolean useEventLoop) {
    this.useEventLoop = useEventLoop;
    return this;
  }

  /**
   * @return the current runner vertx instance
   */
  public Vertx getVertx() {
    return vertx;
  }

  /**
   * Set a vertx instance of the runner.
   *
   * @param vertx the vertx instance
   * @return a reference to this, so the API can be used fluently
   */
  public TestSuiteRunner setVertx(Vertx vertx) {
    this.vertx = vertx;
    return this;
  }

  /**
   * @return the current runner timeout
   */
  public long getTimeout() {
    return timeout;
  }

  /**
   * Set a timeout on the runner, zero or a negative value means no timeout.
   *
   * @param timeout the timeout in millis
   * @return a reference to this, so the API can be used fluently
   */
  public TestSuiteRunner setTimeout(long timeout) {
    this.timeout = timeout;
    return this;
  }

  /**
   * Set a reporter for handling the events emitted by the test suite.
   *
   * @param reporter the reporter
   * @return a reference to this, so the API can be used fluently
   */
  public TestSuiteRunner handler(Handler<TestSuiteReport> reporter) {
    handler = reporter;
    return this;
  }

  /**
   * Run the testsuite with the current {@code timeout}, {@code vertx} and {@code reporter}.
   */
  public void run() {
    TestSuiteReportImpl runner = new TestSuiteReportImpl(name, timeout, before, after, beforeEach, afterEach, tests);
    handler.handle(runner);
    if (vertx != null) {
      runner.run(vertx, useEventLoop);
    } else {
      runner.run(useEventLoop);
    }
  }
}
