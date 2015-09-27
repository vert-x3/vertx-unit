package io.vertx.ext.unit;

import io.vertx.codegen.annotations.VertxGen;

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
   * Waits until the completion of the current {@link Async}. This method does not blocks if the asynchronous code
   * has already completed or failed (it throws a runtime exception). If while waiting the test is marked as failed
   * or reached a timeout, it is unblocks and fails with a runtime exception.
   */
  void awaitBlocking();
}
