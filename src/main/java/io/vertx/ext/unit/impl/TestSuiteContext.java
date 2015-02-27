package io.vertx.ext.unit.impl;

import io.vertx.core.Context;
import io.vertx.core.Vertx;

/**
 * The test suite global context.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class TestSuiteContext {

  private final Vertx vertx;
  private final Context context;

  public TestSuiteContext(Vertx vertx, Context context) {
    this.vertx = vertx;
    this.context = context;
  }

  Vertx vertx() {
    return vertx;
  }

  <T> void run(Task<T> task, T value) {
    if (context != null) {
      context.runOnContext(v -> task.execute(value, this));
    } else {
      task.execute(value, this);
    }
  }

  void run(Task<?> task) {
    run(task, null);
  }
}
