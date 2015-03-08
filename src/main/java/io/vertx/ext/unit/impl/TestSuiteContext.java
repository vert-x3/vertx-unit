package io.vertx.ext.unit.impl;

import io.vertx.core.Context;

/**
 * The test suite global context.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestSuiteContext {

  private final Context context;

  public TestSuiteContext(Context context) {
    this.context = context;
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
