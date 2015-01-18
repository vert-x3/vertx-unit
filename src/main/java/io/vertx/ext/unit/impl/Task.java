package io.vertx.ext.unit.impl;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
interface Task<T> {

  void run(T t, Context executor);

}
