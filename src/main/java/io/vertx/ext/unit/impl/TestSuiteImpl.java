package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.TestSuiteRunner;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestResult;

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

  private final String desc;
  private volatile Handler<Test> before;
  private volatile Handler<Test> after;
  private final List<TestCaseImpl> tests = new ArrayList<>();

  public TestSuiteImpl() {
    this(null);
  }

  public TestSuiteImpl(String desc) {
    this.desc = desc;
  }

  @Override
  public TestSuite before(Handler<Test> before) {
    this.before = before;
    return this;
  }

  @Override
  public TestSuite after(Handler<Test> after) {
    this.after = after;
    return this;
  }

  @Override
  public TestSuite test(String desc, Handler<Test> handler) {
    tests.add(new TestCaseImpl(desc, handler));
    return this;
  }

  @Override
  public TestSuiteRunner runner() {


    return new TestSuiteRunnerImpl(before, tests, after);
  }

  @Override
  public junit.framework.TestSuite toJUnitSuite() {
    return toJUnitSuite(2, TimeUnit.MINUTES);
  }

  @Override
  public junit.framework.TestSuite toJUnitSuite(long timeout, TimeUnit unit) {
    junit.framework.TestSuite suite = new junit.framework.TestSuite();
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
          RunTestTask runner = new RunTestTask(test.desc, before, test.handler, after, new Task<TestResult>() {
            @Override
            public void run(TestResult testResult, Context executor) {
              latch.add(testResult);
            }
          });
          testResult.startTest(this);
          Context.create(vertx).run(runner.task);
          try {
            TestResult result = latch.poll(timeout, unit);
            if (result != null) {
              Throwable failure = result.failure();
              if (failure != null) {
                testResult.addError(this, failure);
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
