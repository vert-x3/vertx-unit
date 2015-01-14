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
public class ModuleImpl implements Module {

  boolean threadCheckEnabled = true;
  final String desc;
  final List<Handler<Void>> beforeCallbacks = new ArrayList<>();
  final List<Handler<Void>> afterCallbacks = new ArrayList<>();
  private final List<TestImpl> tests = new ArrayList<>();

  public ModuleImpl() {
    this(null);
  }

  public ModuleImpl(String desc) {
    this.desc = desc;
  }

  public boolean isThreadCheckEnabled() {
    return threadCheckEnabled;
  }

  public ModuleImpl setThreadCheckEnabled(boolean threadCheckEnabled) {
    this.threadCheckEnabled = threadCheckEnabled;
    return this;
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
    tests.add(new TestImpl(this, desc, false, handler));
    return this;
  }

  @Override
  public Module asyncTest(String desc, Handler<Test> handler) {
    tests.add(new TestImpl(this, desc, true, handler));
    return this;
  }

  @Override
  public void execute() {
    execute(new ConsoleReporter());
  }

  @Override
  public void execute(Handler<ModuleExec> handler) {
    class ModuleExecImpl implements ModuleExec {

      private Handler<Void> endHandler;
      private Handler<TestExec> handler;

      @Override
      public ReadStream<TestExec> exceptionHandler(Handler<Throwable> handler) {
        return this;
      }
      @Override
      public ReadStream<TestExec> handler(Handler<TestExec> handler) {
        this.handler = handler;
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
        run(tests.toArray(new TestImpl[tests.size()]), 0);
      }

      private void run(TestImpl[] tests, int index) {
        if (tests.length > index) {
          TestImpl.Exec exec = tests[index].exec();
          if (handler != null) {
            handler.handle(exec);
          }
          exec.run(() -> run(tests, index + 1));
        } else {
          if (endHandler != null) {
            endHandler.handle(null);
          }
        }
      }
    }

    ModuleExecImpl exec = new ModuleExecImpl();

    if (handler != null) {
      handler.handle(exec);
    }

    exec.run();

  }
}
