package io.vertx.ext.unit.report;

import io.vertx.codegen.annotations.CacheReturn;
import io.vertx.codegen.annotations.VertxGen;

/**
 * The result of a test.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface TestResult {

  /**
   * The test description, may be null if none was provided.
   */
  @CacheReturn
  String name();

  /**
   * The time at which the test began in millis.
   */
  @CacheReturn
  long beginTime();

  /**
   * How long the test lasted in millis.
   */
  @CacheReturn
  long durationTime();

  /**
   * Did it succeed?
   */
  @CacheReturn
  boolean succeeded();

  /**
   * Did it fail?
   */
  @CacheReturn
  boolean failed();

  /**
   * An exception describing failure, null if the test succeeded.
   */
  @CacheReturn
  Failure failure();
}
