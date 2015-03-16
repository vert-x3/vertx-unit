package io.vertx.ext.unit;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * An asynchronous exit point for a test.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Async {

  /**
   * Signals the asynchronous operation is done, this method should be called only once, if the method is called
   * another time it will throw an {@code IllegalStateException} to signal the error.
   */
  void complete();

  /**
   * Return an async result handler calling {@link #complete()} or {@link TestContext#fail()} on callback.
   *
   * @return the async result handler
   */
  @GenIgnore
  <T> Handler<AsyncResult<T>> handler();

}
