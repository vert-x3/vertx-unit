package io.vertx.ext.unit.impl;

import io.vertx.core.Vertx;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
interface Context {

  Vertx vertx();
  
  <T> void run(Task<T> task, T value);

  default void run(Task<?> task) {
    run(task, null);
  }

  static Context create() {
    return new Context() {
      @Override
      public Vertx vertx() {
        return null;
      }
      @Override
      public <T> void run(Task<T> task, T value) {
        io.vertx.core.Context context = Vertx.currentContext();
        if (context != null) {
          context.runOnContext(v -> task.execute(value, this));
        } else {
          task.execute(value, this);
        }
      }
    };
  }

  static Context create(Vertx vertx) {
    return new Context() {
      @Override
      public Vertx vertx() {
        return vertx;
      }
      @Override
      public <T> void run(Task<T> task, T value) {
        vertx.runOnContext(v -> task.execute(value, this));
      }
    };
  }
}
