package io.vertx.ext.unit;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;
import io.vertx.core.streams.ReadStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface TestSuiteRunner extends ReadStream<TestCaseRunner> {

  /**
   * Run the suite in the current thread.
   */
  void run();

  /**
   * Run the suite with the provided vertx.
   */
  void run(Vertx vertx);

}
