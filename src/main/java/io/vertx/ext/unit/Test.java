package io.vertx.ext.unit;

import io.vertx.codegen.annotations.CacheReturn;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;

/**
 * The test object is used for performing test assertions and manage the completion of the test. This object
 * is provided by <i>vertx-unit</i> as argument of the test case.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Test {

  /**
   * Assert the specified {@code condition} is {@code true}. If the condition is {@code false}, an assertion error is thrown
   * otherwise the execution continue.
   *
   * @param condition the condition to assert
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  Test assertTrue(boolean condition);

  /**
   * Assert the specified {@code condition} is {@code true}. If the condition is {@code false}, an assertion error is thrown
   * otherwise the execution continue.
   *
   * @param condition the condition to assert
   * @param message the failure message
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  Test assertTrue(boolean condition, String message);

  /**
   * Assert the specified {@code condition} is {@code false}. If the condition is {@code true}, an assertion error is thrown
   * otherwise the execution continue.
   *
   * @param condition the condition to assert
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  Test assertFalse(boolean condition);

  /**
   * Assert the specified {@code condition} is {@code false}. If the condition is {@code true}, an assertion error is thrown
   * otherwise the execution continue.
   *
   * @param condition the condition to assert
   * @param message the failure message
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  Test assertFalse(boolean condition, String message);

  /**
   * Assert the {@code expected} argument is equals to the {@code actual} argument. If the arguments are not equals
   * an assertion error is thrown otherwise the execution continue.
   *
   * @param expected the object the actual object is supposedly equals to
   * @param actual the actual object to test
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  Test assertEquals(Object expected, Object actual);

  /**
   * Assert the {@code expected} argument is equals to the {@code actual} argument. If the arguments are not equals
   * an assertion error is thrown otherwise the execution continue.
   *
   * @param expected the object the actual object is supposedly equals to
   * @param actual the actual object to test
   * @param message the failure message
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  Test assertEquals(Object expected, Object actual, String message);

  /**
   * Asserts that the {@code expected} double argument is equals to the {@code actual} double argument
   * within a positive delta. If the arguments do not satisfy this, an assertion error is thrown otherwise
   * the execution continue.
   *
   * @param expected the object the actual object is supposedly equals to
   * @param actual the actual object to test
   * @param delta the maximum delta
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  Test assertInRange(double expected, double actual, double delta);

  /**
   * Asserts that the {@code expected} double argument is equals to the {@code actual} double argument
   * within a positive delta. If the arguments do not satisfy this, an assertion error is thrown otherwise
   * the execution continue.
   *
   * @param expected the object the actual object is supposedly equals to
   * @param actual the actual object to test
   * @param delta the maximum delta
   * @param message the failure message
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  Test assertInRange(double expected, double actual, double delta, String message);

  /**
   * Assert the {@code first} argument is not equals to the {@code second} argument. If the arguments are equals
   * an assertion error is thrown otherwise the execution continue.
   *
   * @param first the first object to test
   * @param second the second object to test
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  Test assertNotEquals(Object first, Object second);

  /**
   * Assert the {@code first} argument is not equals to the {@code second} argument. If the arguments are equals
   * an assertion error is thrown otherwise the execution continue.
   *
   * @param first the first object to test
   * @param second the second object to test
   * @param message the failure message
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  Test assertNotEquals(Object first, Object second, String message);

  /**
   * Throw a failure.
   */
  void fail();

  /**
   * Throw a failure with the specified failure {@code message}.
   *
   * @param message the failure message
   */
  void fail(String message);

  /**
   * Returns the vertx instance associated with this test. The value may be null if no vertx instance was
   * specified when running the test suite.
   *
   * @return the vertx instance
   */
  @CacheReturn
  Vertx vertx();

  /**
   * Create and returns a new async object, the returned async controls the completion of the test. The test case
   * will complete when all the async objects have their {@link io.vertx.ext.unit.Async#complete()} method called
   * at least once.<p/>
   *
   * This method shall be used for creating asynchronous exit points for the executed test.
   *
   * @return the async instance
   */
  Async async();
}
