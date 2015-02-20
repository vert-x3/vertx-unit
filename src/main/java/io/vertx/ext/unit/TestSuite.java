package io.vertx.ext.unit;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.impl.TestSuiteImpl;

import java.util.concurrent.TimeUnit;

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
   * Set a callback executed before the tests.
   *
   * @param callback the callback
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestSuite before(Handler<Test> callback);

  /**
   * Set a callback executed before each test and after the suite {@code before} callback.
   *
   * @param callback the callback
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestSuite beforeEach(Handler<Test> callback);

  /**
   * Set a callback executed after the tests.
   *
   * @param callback the callback
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestSuite after(Handler<Test> callback);

  /**
   * Set a callback executed after each test and before the suite {@code after} callback.
   *
   * @param callback the callback
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestSuite afterEach(Handler<Test> callback);

  /**
   * Add a new test case to the suite.
   *
   * @param name the test case name
   * @param testCase the test case
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestSuite test(String name, Handler<Test> testCase);

  /**
   * Run the testsuite with the default options.<p/>
   *
   * The test suite will be executed on the event loop when one is available otherwise the suite will be executed
   * in the current thread. The returned {@link io.vertx.ext.unit.TestCompletion} object can be used to get
   * a completion callback.
   *
   * @return the related test completion
   */
  TestCompletion run();

  /**
   * Run the testsuite with the specified {@code options}.<p/>
   *
   * The test suite will be executed on the event loop when one is available otherwise the suite will be executed
   * in the current thread. The returned {@link io.vertx.ext.unit.TestCompletion} object can be used to get
   * a completion callback.
   *
   * @param options the test options
   * @return the related test completion
   */
  TestCompletion run(TestOptions options);

  /**
   * Run the testsuite with the default options and the specified {@code vertx} instance.<p/>
   *
   * The test suite will be executed on the provided Vert.x event loop. The returned
   * {@link io.vertx.ext.unit.TestCompletion} object can be used to get a completion callback.
   *
   * @param vertx the vertx instance
   * @return the related test completion
   */
  TestCompletion run(Vertx vertx);

  /**
   * Run the testsuite with the specified {@code options} and the specified {@code vertx} instance.<p/>
   *
   * The test suite will be executed on the provided Vert.x event loop. The returned
   * {@link io.vertx.ext.unit.TestCompletion} object can be used to get a completion callback.
   *
   * @param vertx the vertx instance
   * @param options the test options
   * @return the related test completion
   */
  TestCompletion run(Vertx vertx, TestOptions options);

  /**
   * Create a junit test suite running this testsuite.
   *
   * @return the junit test suite
   */
  @GenIgnore
  junit.framework.TestSuite toJUnitSuite();

  /**
   * Create a junit test suite running this testsuite.
   *
   * @param timeout the suite timeout expressed in the {@code unit} argument
   * @param unit the suite {@code timeout} unit
   * @return the junit test suite
   */
  @GenIgnore
  junit.framework.TestSuite toJUnitSuite(long timeout, TimeUnit unit);

  TestSuiteRunner runner();

}
