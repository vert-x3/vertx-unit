package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.unit.Module;
import io.vertx.ext.unit.ModuleExec;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestExec;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ModuleDesc implements Module {

  final String desc;
  final List<Handler<Void>> beforeCallbacks = new ArrayList<>();
  final List<Handler<Void>> afterCallbacks = new ArrayList<>();
  private final List<TestDesc> tests = new ArrayList<>();

  public ModuleDesc() {
    this(null);
  }

  public ModuleDesc(String desc) {
    this.desc = desc;
  }

  @Override
  public Module before(Handler<Void> callback) {
    beforeCallbacks.add(callback);
    return null;
  }

  @Override
  public Module after(Handler<Void> callback) {
    afterCallbacks.add(callback);
    return this;
  }

  @Override
  public Module test(String desc, Handler<Test> handler) {
    tests.add(new TestDesc(this, desc, handler));
    return this;
  }

  @Override
  public ModuleExec exec() {

    class ModuleExecImpl implements ModuleExec {

      private Handler<Void> endHandler;
      private Handler<TestExec> testHandler;

      @Override
      public ReadStream<TestExec> exceptionHandler(Handler<Throwable> handler) {
        return this;
      }

      @Override
      public ReadStream<TestExec> handler(Handler<TestExec> handler) {
        this.testHandler = handler;
        return this;
      }

      @Override
      public ReadStream<TestExec> pause() {
        return this;
      }

      @Override
      public ReadStream<TestExec> resume() {
        return this;
      }

      @Override
      public ReadStream<TestExec> endHandler(Handler<Void> handler) {
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
