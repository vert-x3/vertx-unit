package io.vertx.ext.unit;

import io.vertx.codegen.annotations.VertxGen;

/**
 * An asynchronous exit point for a test.<p/>
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Async extends Completion<Void> {

  /**
   * @return the current count
   */
  int count();

  /**
   * Count down the async.
   *
   * @throws IllegalStateException in strict mode if invoked more than the initial count
   */
  void countDown();

  /**
   * Signals the asynchronous operation is done, this method must be called with a count greater than {@code 0},
   * otherwise it throws an {@code IllegalStateException} to signal the error.
   */
  void complete();

}
