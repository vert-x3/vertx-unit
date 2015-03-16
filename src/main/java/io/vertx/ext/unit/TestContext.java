package io.vertx.ext.unit;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;

/**
 * The test context is used for performing test assertions and manage the completion of the test. This context
 * is provided by <i>vertx-unit</i> as argument of the test case.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface TestContext {

  /**
   * Get some data from the context.
   *
   * @param key  the key of the data
   * @param <T>  the type of the data
   * @return the data
   */
  <T> T get(String key);

  /**
   * Put some data in the context.
   * <p>
   * This can be used to share data between different tests and before/after phases.
   *
   * @param key  the key of the data
   * @param value  the data
   * @return the previous object when it exists
   */
  <T> T put(String key, Object value);

  /**
   * Remove some data from the context.
   *
   * @param key  the key to remove
   * @return the removed object when it exists
   */
  <T> T remove(String key);

  /**
   * Assert the {@code expected} argument is {@code null}. If the argument is not, an assertion error is thrown
   * otherwise the execution continue.
   *
   * @param expected the argument being asserted to be null
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestContext assertNull(Object expected);

  /**
   * Assert the {@code expected} argument is {@code null}. If the argument is not, an assertion error is thrown
   * otherwise the execution continue.
   *
   * @param expected the argument being asserted to be null
   * @param message the failure message
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestContext assertNull(Object expected, String message);

  /**
   * Assert the {@code expected} argument is not {@code null}. If the argument is {@code null}, an assertion error is thrown
   * otherwise the execution continue.
   *
   * @param expected the argument being asserted to be not null
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestContext assertNotNull(Object expected);

  /**
   * Assert the {@code expected} argument is not {@code null}. If the argument is {@code null}, an assertion error is thrown
   * otherwise the execution continue.
   *
   * @param expected the argument being asserted to be not null
   * @param message the failure message
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestContext assertNotNull(Object expected, String message);

  /**
   * Assert the specified {@code condition} is {@code true}. If the condition is {@code false}, an assertion error is thrown
   * otherwise the execution continue.
   *
   * @param condition the condition to assert
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestContext assertTrue(boolean condition);

  /**
   * Assert the specified {@code condition} is {@code true}. If the condition is {@code false}, an assertion error is thrown
   * otherwise the execution continue.
   *
   * @param condition the condition to assert
   * @param message the failure message
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestContext assertTrue(boolean condition, String message);

  /**
   * Assert the specified {@code condition} is {@code false}. If the condition is {@code true}, an assertion error is thrown
   * otherwise the execution continue.
   *
   * @param condition the condition to assert
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestContext assertFalse(boolean condition);

  /**
   * Assert the specified {@code condition} is {@code false}. If the condition is {@code true}, an assertion error is thrown
   * otherwise the execution continue.
   *
   * @param condition the condition to assert
   * @param message the failure message
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestContext assertFalse(boolean condition, String message);

  /**
   * Assert the {@code expected} argument is equals to the {@code actual} argument. If the arguments are not equals
   * an assertion error is thrown otherwise the execution continue.
   *
   * @param expected the object the actual object is supposedly equals to
   * @param actual the actual object to test
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestContext assertEquals(Object expected, Object actual);

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
  TestContext assertEquals(Object expected, Object actual, String message);

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
  TestContext assertInRange(double expected, double actual, double delta);

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
  TestContext assertInRange(double expected, double actual, double delta, String message);

  /**
   * Assert the {@code first} argument is not equals to the {@code second} argument. If the arguments are equals
   * an assertion error is thrown otherwise the execution continue.
   *
   * @param first the first object to test
   * @param second the second object to test
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestContext assertNotEquals(Object first, Object second);

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
  TestContext assertNotEquals(Object first, Object second, String message);

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
   * Throw a failure with the specified failure {@code cause}.
   *
   * @param cause the failure cause
   */
  @GenIgnore
  void fail(Throwable cause);

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
