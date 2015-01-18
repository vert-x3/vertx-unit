package io.vertx.ext.unit;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;

/**
 * The test interface allows the test code to report test completion or failures.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Test {

  Vertx vertx();

  Async async();

  void assertTrue(boolean b);

  void fail(String s);

}
