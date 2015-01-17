package io.vertx.ext.unit.impl;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
interface Executor {
  
  <T> void execute(Task<T> task, T value);

}
