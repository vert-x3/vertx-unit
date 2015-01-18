package io.vertx.ext.unit;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface TestRunner {

  /**
   * @return the test exec description
   */
  String description();

  /**
   * Set a callback for completion, the specified {@code handler} is invoked when the test exec has completed.
   *
   * @param handler the completion handler
   */
  void completionHandler(Handler<TestResult> handler);

  void run();

  void run(Vertx vertx);

}
