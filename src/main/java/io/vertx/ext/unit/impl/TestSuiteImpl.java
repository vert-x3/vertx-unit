package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestCompletion;
import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.report.TestResult;
import io.vertx.ext.unit.TestSuiteRunner;
import io.vertx.ext.unit.report.Reporter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestSuiteImpl implements TestSuite {

  private final String name;
  private volatile Handler<Test> before;
  private volatile Handler<Test> beforeEach;
  private volatile Handler<Test> after;
  private volatile Handler<Test> afterEach;
  private final List<TestCaseImpl> tests = new ArrayList<>();

  public TestSuiteImpl(String name) {
    this.name = name;
  }

  @Override
  public TestSuite before(Handler<Test> callback) {
    this.before = callback;
    return this;
  }

  @Override
  public TestSuite beforeEach(Handler<Test> callback) {
    beforeEach = callback;
    return this;
  }

  @Override
  public TestSuite after(Handler<Test> handler) {
    this.after = handler;
    return this;
  }

  @Override
  public TestSuite afterEach(Handler<Test> handler) {
    afterEach = handler;
    return this;
  }

  @Override
  public TestSuite test(String name, Handler<Test> testCase) {
    tests.add(new TestCaseImpl(name, testCase));
    return this;
  }

  @Override
  public TestCompletion run() {
    return run(null, new TestOptions());
  }

  @Override
  public TestCompletion run(Vertx vertx) {
    return run(vertx, new TestOptions());
  }

  @Override
  public TestCompletion run(TestOptions options) {
    return run(null, options);
  }

  @Override
  public TestCompletion run(Vertx vertx, TestOptions options) {
    Reporter[] reporters = options.getReporters().stream().map(reportOptions -> Reporter.reporter(vertx, reportOptions)).toArray(Reporter[]::new);
    ReporterHandler abc = new ReporterHandler(null, reporters);
    runner().
        setVertx(vertx).
        setTimeout(options.getTimeout()).
        handler(abc).run();
    return abc;
  }

  @Override
  public TestSuiteRunner runner() {
    return new TestSuiteRunnerImpl(name, before, after, beforeEach, afterEach, tests.toArray(new TestCaseImpl[tests.size()]));
  }

  @Override
  public junit.framework.TestSuite toJUnitSuite() {
    return toJUnitSuite(2, TimeUnit.MINUTES);
  }

  @Override
  public junit.framework.TestSuite toJUnitSuite(long timeout, TimeUnit unit) {
    junit.framework.TestSuite suite = new junit.framework.TestSuite(name);
    for (TestCaseImpl test : tests) {
      suite.addTest(new junit.framework.Test() {
        @Override
        public int countTestCases() {
          return 1;
        }
        @Override
        public void run(junit.framework.TestResult testResult) {
          BlockingQueue<TestResult> latch = new ArrayBlockingQueue<>(1);
          Vertx vertx = Vertx.vertx();
          InvokeTask task = InvokeTask.runTestTask(test.desc, 0L, before, test.handler, after, (testResult1, executor) -> {
            latch.add(testResult1);
          });
          testResult.startTest(this);
          Context.create(vertx).run(task);
          try {
            TestResult result = latch.poll(timeout, unit);
            if (result != null) {
              if (result.failed()) {
                testResult.addError(this, result.failure().cause());
              }
            } else {
              testResult.addError(this, new TimeoutException("Timed out in waiting for test complete"));
            }
          } catch (InterruptedException e) {
            testResult.addError(this, e);
          }
          vertx.close();
          testResult.endTest(this);
        }
      });
    }
    return suite;
  }
}
