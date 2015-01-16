package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.unit.Suite;
import io.vertx.ext.unit.SuiteRunner;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SuiteDesc implements Suite {

  final String desc;
  final List<Handler<Void>> beforeCallbacks = new ArrayList<>();
  final List<Handler<Void>> afterCallbacks = new ArrayList<>();
  private final List<TestDesc> tests = new ArrayList<>();

  public SuiteDesc() {
    this(null);
  }

  public SuiteDesc(String desc) {
    this.desc = desc;
  }

  @Override
  public Suite before(Handler<Void> callback) {
    beforeCallbacks.add(callback);
    return null;
  }

  @Override
  public Suite after(Handler<Void> callback) {
    afterCallbacks.add(callback);
    return this;
  }

  @Override
  public Suite test(String desc, Handler<Test> handler) {
    tests.add(new TestDesc(this, desc, handler));
    return this;
  }

  @Override
  public SuiteRunner exec() {

    class ModuleExecImpl implements SuiteRunner {

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

      public void run() {
        run(tests.toArray(new TestDesc[tests.size()]), 0);
      }

      private void run(TestDesc[] tests, int index) {
        if (tests.length > index) {
          Runnable task = tests[index].exec(testHandler, () -> run(tests, index + 1));
          task.run();
        } else {
          if (endHandler != null) {
            endHandler.handle(null);
          }
        }
      }
    }

    return new ModuleExecImpl();
  }
}
