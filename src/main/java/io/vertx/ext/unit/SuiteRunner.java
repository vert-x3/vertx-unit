package io.vertx.ext.unit;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.streams.ReadStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface SuiteRunner extends ReadStream<TestRunner> {

  /**
   * Run the module exec.
   */
  void run();

}
