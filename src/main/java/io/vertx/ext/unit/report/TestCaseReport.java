package io.vertx.ext.unit.report;

import io.vertx.codegen.annotations.CacheReturn;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.unit.TestResult;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface TestCaseReport {

  /**
   * @return the test case name
   */
  @CacheReturn
  String name();

  /**
   * Set a callback for completion, the specified {@code handler} is invoked when the test exec has completed.
   *
   * @param handler the completion handler
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  TestCaseReport endHandler(Handler<TestResult> handler);

}
