package io.vertx.ext.unit;

import io.vertx.codegen.annotations.VertxGen;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface TestResult {

  /**
   * The test description, may be null if none was provided.
   */
  String description();

  /**
   * The test execution time.
   */
  long time();

  /**
   * An exception describing failure. This will be null if the test succeeded.
   */
  Throwable failure();

  /**
   * Did it succeed?
   */
  boolean succeeded();

  /**
   * Did it fail?
   */
  boolean failed();
}
