package io.vertx.ext.unit;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.report.TestSuiteReport;

/**
 * The test suite runner.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface TestSuiteRunner {

  /**
   * @return the current runner vertx instance
   */
  Vertx getVertx();

  /**
   * Set a vertx instance of the runner.
   *
   * @param vertx the vertx instance
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestSuiteRunner setVertx(Vertx vertx);

  /**
   * @return the current runner timeout
   */
  long getTimeout();

  /**
   * Set a timeout on the runner, zero or a negative value means no timeout.
   *
   * @param timeout the timeout in millis
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestSuiteRunner setTimeout(long timeout);

  /**
   * Set a reporter for handling the events emitted by the test suite.
   *
   * @param reporter the reporter
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestSuiteRunner handler(Handler<TestSuiteReport> reporter);

  /**
   * Run the testsuite with the current {@code timeout}, {@code vertx} and {@code reporter}.
   */
  void run();

}
