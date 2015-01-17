package io.vertx.ext.unit;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Context;
import io.vertx.core.streams.ReadStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface SuiteRunner extends ReadStream<TestRunner> {

  /**
   * Run the suite in the current thread.
   */
  void run();

  /**
   * Run the suite on current vertx context.
   */
  void runOnContext();

  /**
   * Run the suite with the specified context.
   *
   * @param context the execution context
   */
  void runOnContext(Context context);

}
