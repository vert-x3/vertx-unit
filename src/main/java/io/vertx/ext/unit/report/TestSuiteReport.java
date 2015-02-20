package io.vertx.ext.unit.report;

import io.vertx.codegen.annotations.CacheReturn;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.streams.ReadStream;

/**
 * The test suite reports is basically a stream of events reporting the test suite execution.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface TestSuiteReport extends ReadStream<TestCaseReport> {

  /**
   * @return the test suite name
   */
  @CacheReturn
  String name();

}
