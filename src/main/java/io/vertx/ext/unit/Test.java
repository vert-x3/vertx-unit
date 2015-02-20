package io.vertx.ext.unit;

import io.vertx.codegen.annotations.CacheReturn;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;

/**
 * The test interface allows the test code to report test completion or failures.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Test {

  @CacheReturn
  Vertx vertx();

  Async async();

  void assertTrue(boolean condition);

  void assertTrue(boolean condition, String message);

  void assertFalse(boolean condition);

  void assertFalse(boolean condition, String message);

  void fail();

  void fail(String s);

  void assertEquals(Object expected, Object actual);

  void assertEquals(Object expected, Object actual, String message);

  void assertNotEquals(Object first, Object second);

  void assertNotEquals(Object first, Object second, String message);

}
