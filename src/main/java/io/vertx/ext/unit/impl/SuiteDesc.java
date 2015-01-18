package io.vertx.ext.unit.impl;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.unit.Suite;
import io.vertx.ext.unit.SuiteRunner;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestResult;
import io.vertx.ext.unit.TestRunner;
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
public class SuiteDesc implements Suite {

  final String desc;
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
  public Suite before(Handler<Test> before) {
    this.before = before;
    return this;
  }

  @Override
  public Suite after(Handler<Test> after) {
    this.after = after;
    return this;
  }

  @Override
  public Suite test(String desc, Handler<Test> handler) {
    tests.add(new TestDesc(this, desc, handler));
    return this;
  }

  @Override
  public SuiteRunner runner() {

    class SuiteRunnerImpl implements SuiteRunner {

      private Handler<Void> endHandler;
      private Handler<TestRunner> testHandler;

      @Override
      public ReadStream<TestRunner> exceptionHandler(Handler<Throwable> handler) {
        return this;
      }

      @Override
      public ReadStream<TestRunner> handler(Handler<TestRunner> handler) {
        this.testHandler = handler;
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

      private Task<?> build(TestDesc[] tests, int index) {
        if (tests.length > index) {
          Task<?> next = build(tests, index + 1);
          TestDesc test = tests[index];
          return new TestRunnerImpl(testHandler, test.desc, before, test.handler, after, next);
        } else {
          return (o, executor) -> {
            if (endHandler != null) {
              endHandler.handle(null);
            }
          };
        }
      }

      private Task<?> build() {
        return build(tests.toArray(new TestDesc[tests.size()]), 0);
      }

      public void run() {
        Executor.create().execute(build());
      }

      @Override
      public void runOnContext() {
        runOnContext(Vertx.currentContext());
      }

      @Override
      public void runOnContext(Context context) {
        Executor.create(context).execute(build());
      }
    }

    return new SuiteRunnerImpl();
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
            public void run(TestResult testResult, Executor executor) {
              latch.add(testResult);
            }
          });
          testResult.startTest(this);
          Executor.create(vertx.getOrCreateContext()).execute(runner.task);
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
