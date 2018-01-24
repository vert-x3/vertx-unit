package io.vertx.ext.unit;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

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
   * Execute the provided handler, which may contain assertions, possibly from any third-party assertion framework.
   * Any {@link AssertionError} thrown will be caught (and propagated) in order to fulfill potential expected async
   * completeness.
   *
   * @param block block of code to be executed
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestContext verify(Handler<Void> block);

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
  void fail(Throwable cause);

  /**
   * Create and returns a new async object, the returned async controls the completion of the test. Calling the
   * {@link Async#complete()} completes the async operation.<p/>
   *
   * The test case will complete when all the async objects have their {@link io.vertx.ext.unit.Async#complete()}
   * method called at least once.<p/>
   *
   * This method shall be used for creating asynchronous exit points for the executed test.
   *
   * @return the async instance
   */
  Async async();

  /**
   * Create and returns a new async object, the returned async controls the completion of the test. This async operation
   * completes when the {@link Async#countDown()} is called {@code count} times.<p/>
   *
   * The test case will complete when all the async objects have their {@link io.vertx.ext.unit.Async#complete()}
   * method called at least once.<p/>
   *
   * This method shall be used for creating asynchronous exit points for the executed test.<p/>
   *
   * @return the async instance
   */
  Async async(int count);

  /**
   * Create and returns a new async object, the returned async controls the completion of the test.
   * This async operation completes when the {@link Async#countDown()} is called {@code count} times.
   * If {@link Async#countDown()} is called more than {@code count} times and {@code strict} is true, an {@link IllegalStateException} is thrown.<p/>
   *
   * The test case will complete when all the async objects have their {@link io.vertx.ext.unit.Async#complete()}
   * method called at least once.<p/>
   *
   * This method shall be used for creating asynchronous exit points for the executed test.<p/>
   *
   * @return the async instance
   */
  Async async(int count, boolean strict);

  /**
   * Creates and returns a new async handler, the returned handler controls the completion of the test.<p/>
   *
   * When the returned handler is called back with a succeeded result it completes the async operation.<p/>
   *
   * When the returned handler is called back with a failed result it fails the test with the cause of the failure.<p/>
   *
   * @return the async result handler
   */
  <T> Handler<AsyncResult<T>> asyncAssertSuccess();

  /**
   * Creates and returns a new async handler, the returned handler controls the completion of the test.<p/>
   *
   * When the returned handler is called back with a succeeded result it invokes the {@code resultHandler} argument
   * with the async result. The test completes after the result handler is invoked and does not fails.<p/>
   *
   * When the returned handler is called back with a failed result it fails the test with the cause of the failure.<p/>
   *
   * Note that the result handler can create other async objects during its invocation that would postpone
   * the completion of the test case until those objects are resolved.
   *
   * @param resultHandler the result handler
   * @return the async result handler
   */
  <T> Handler<AsyncResult<T>> asyncAssertSuccess(Handler<T> resultHandler);

  /**
   * Creates and returns a new async handler, the returned handler controls the completion of the test.<p/>
   *
   * When the returned handler is called back with a failed result it completes the async operation.<p/>
   *
   * When the returned handler is called back with a succeeded result it fails the test.<p/>
   *
   * @return the async result handler
   */
  <T> Handler<AsyncResult<T>> asyncAssertFailure();

  /**
   * Creates and returns a new async handler, the returned handler controls the completion of the test.<p/>
   *
   * When the returned handler is called back with a failed result it completes the async operation.<p/>
   *
   * When the returned handler is called back with a succeeded result it fails the test.<p/>
   *
   * @param causeHandler the cause handler
   * @return the async result handler
   */
  <T> Handler<AsyncResult<T>> asyncAssertFailure(Handler<Throwable> causeHandler);

  /**
   * @return an exception handler that will fail this context
   */
  Handler<Throwable> exceptionHandler();
}
