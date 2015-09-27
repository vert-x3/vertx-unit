package io.vertx.ext.unit;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.impl.TestSuiteImpl;

/**
 * A named suite of test cases that are executed altogether. The suite suite is created with
 * the {@link #create(String)} and the returned suite contains initially no tests.<p/>
 *
 * The suite can declare a callback before the suite with {@link #before(io.vertx.core.Handler)} or after
 * the suite with {@link #after(io.vertx.core.Handler)}.<p/>
 *
 * The suite can declare a callback before each test with {@link #beforeEach(io.vertx.core.Handler)} or after
 * each test with {@link #afterEach(io.vertx.core.Handler)}.<p/>
 *
 * Each test case of the suite is declared by calling the {@link #test(String, io.vertx.core.Handler)} method.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface TestSuite {

  /**
   * Create and return a new test suite.
   *
   * @param name the test suite name
   * @return the created test suite
   */
  static TestSuite create(String name) {
    return new TestSuiteImpl(name);
  }

  /**
   * Create and return a new test suite configured after the {@code testSuiteObject} argument.
   *
   * The {@code testSuiteObject} argument methods are inspected and the public, non static methods
   * with {@link TestContext} parameter are retained and mapped to a Vertx Unit test suite
   * via the method name:
   *
   * <ul>
   *   <li>{@code before} : {@link #before} callback</li>
   *   <li>{@code after} : {@link #after} callback</li>
   *   <li>{@code beforeEach} : {@link #beforeEach} callback</li>
   *   <li>{@code afterEach} : {@link #afterEach} callback</li>
   *   <li>when the name starts with {@code test} :{@link #test} callback named after the method name</li>
   * </ul>
   *
   * @param testSuiteObject the test suite object
   * @return the configured test suite
   */
  @GenIgnore
  static TestSuite create(Object testSuiteObject) {
    return new TestSuiteImpl(testSuiteObject);
  }

  /**
   * Set a callback executed before the tests.
   *
   * @param callback the callback
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestSuite before(Handler<TestContext> callback);

  /**
   * Set a callback executed before each test and after the suite {@code before} callback.
   *
   * @param callback the callback
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestSuite beforeEach(Handler<TestContext> callback);

  /**
   * Set a callback executed after the tests.
   *
   * @param callback the callback
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestSuite after(Handler<TestContext> callback);

  /**
   * Set a callback executed after each test and before the suite {@code after} callback.
   *
   * @param callback the callback
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestSuite afterEach(Handler<TestContext> callback);

  /**
   * Add a new test case to the suite.
   *
   * @param name the test case name
   * @param testCase the test case
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestSuite test(String name, Handler<TestContext> testCase);

  /**
   * Run the testsuite with the default options.<p/>
   *
   * When the test suite is executed in a Vertx context (i.e `Vertx.currentContext()` returns a context) this
   * context's event loop is used for running the test suite. Otherwise it is executed in the current thread.<p/>
   *
   * The returned {@link Completion} object can be used to get a completion callback.
   *
   * @return the related test completion
   */
  TestCompletion run();

  /**
   * Run the testsuite with the specified {@code options}.<p/>
   *
   * When the test suite is executed in a Vertx context (i.e `Vertx.currentContext()` returns a context) this
   * context's event loop is used for running the test suite unless the {@link io.vertx.ext.unit.TestOptions#setUseEventLoop(Boolean)}
   * is set to {@code false}. In this case it is executed by the current thread.<p/>
   *
   * Otherwise, the test suite will be executed in the current thread when {@link io.vertx.ext.unit.TestOptions#setUseEventLoop(Boolean)} is
   * set to {@code false} or {@code null}. If the value is {@code true}, this methods throws an {@code IllegalStateException}.<p/>
   *
   * The returned {@link Completion} object can be used to get a completion callback.
   *
   * @param options the test options
   * @return the related test completion
   */
  TestCompletion run(TestOptions options);

  /**
   * Run the testsuite with the default options and the specified {@code vertx} instance.<p/>
   *
   * The test suite will be executed on the event loop provided by the {@code vertx} argument. The returned
   * {@link Completion} object can be used to get a completion callback.<p/>
   *
   * @param vertx the vertx instance
   * @return the related test completion
   */
  TestCompletion run(Vertx vertx);

  /**
   * Run the testsuite with the specified {@code options} and the specified {@code vertx} instance.<p/>
   *
   * The test suite will be executed on the event loop provided by the {@code vertx} argument when
   * {@link io.vertx.ext.unit.TestOptions#setUseEventLoop(Boolean)} is not set to {@code false}. The returned
   * {@link Completion} object can be used to get a completion callback.
   *
   * @param vertx the vertx instance
   * @param options the test options
   * @return the related test completion
   */
  TestCompletion run(Vertx vertx, TestOptions options);

}
