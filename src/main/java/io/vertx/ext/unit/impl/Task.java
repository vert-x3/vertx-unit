package io.vertx.ext.unit.impl;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface Task<T> {

  void execute(T t, ExecutionContext context);

}
