package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.SuiteDef;
import io.vertx.ext.unit.SuiteRunner;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestResult;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SuiteDesc implements SuiteDef {

  private final String desc;
  private volatile Handler<Test> before;
  private volatile Handler<Test> after;
  private final List<TestDesc> tests = new ArrayList<>();

  public SuiteDesc() {
    this(null);
  }

  public SuiteDesc(String desc) {
    this.desc = desc;
  }

  @Override
  public SuiteDef before(Handler<Test> before) {
    this.before = before;
    return this;
  }

  @Override
  public SuiteDef after(Handler<Test> after) {
    this.after = after;
    return this;
  }

  @Override
  public SuiteDef test(String desc, Handler<Test> handler) {
    tests.add(new TestDesc(desc, handler));
    return this;
  }

  @Override
  public SuiteRunner runner() {


    return new SuiteRunnerImpl(before, tests, after);
  }

  @Override
  public TestSuite toJUnitSuite() {
    return toJUnitSuite(2, TimeUnit.MINUTES);
  }

  @Override
  public TestSuite toJUnitSuite(long timeout, TimeUnit unit) {
    TestSuite suite = new TestSuite();
    for (TestDesc test : tests) {
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
