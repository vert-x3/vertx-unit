package io.vertx.ext.unit.impl;

import io.vertx.core.Context;
import io.vertx.core.Vertx;

/**
 * The test suite global context.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
interface TestSuiteContext {

  Vertx vertx();

  <T> void run(Task<T> task, T value);

  default void run(Task<?> task) {
    run(task, null);
  }

  static TestSuiteContext create(Vertx vertx, Context context) {
    return new TestSuiteContext() {
      @Override
      public Vertx vertx() {
        return vertx;
      }
      @Override
      public <T> void run(Task<T> task, T value) {
        if (context != null) {
          context.runOnContext(v -> task.execute(value, this));
        } else {
          task.execute(value, this);
        }
      }
    };
  }

}
