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
   * Signals the asynchronous operation is done, this method should be called only once, if the method is called
   * another time it will throw an {@code IllegalStateException} to signal the error.
   */
  void complete();

}
