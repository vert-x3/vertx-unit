package io.vertx.ext.unit;

import io.vertx.codegen.annotations.VertxGen;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Test {

  void assertTrue(boolean b);

  void fail(String s);

  void complete();

}
