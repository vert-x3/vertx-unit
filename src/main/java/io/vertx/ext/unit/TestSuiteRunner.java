package io.vertx.ext.unit;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;

/**
 * The test suite runner.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface TestSuiteRunner {

  long getTimeout();

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
   * Run the testsuite.
   */
  void run();

}
