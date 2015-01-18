package io.vertx.ext.unit.impl;

import io.vertx.core.Context;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@FunctionalInterface
interface Executor {
  
  <T> void execute(Task<T> task, T value);

  default void execute(Task<?> task) {
    execute(task, null);
  }

  static Executor create() {
    return new Executor() {
      @Override
      public <T> void execute(Task<T> task, T value) {
        task.run(value, this);
      }
    };
  }

  static Executor create(Context context) {
    return new Executor() {
      @Override
      public <T> void execute(Task<T> task, T value) {
        context.runOnContext(v -> {
          task.run(value, this);
        });
      }
    };
  }

}
