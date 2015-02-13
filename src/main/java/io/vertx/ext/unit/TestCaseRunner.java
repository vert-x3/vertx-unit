package io.vertx.ext.unit;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface TestCaseRunner {

  /**
   * @return the test exec description
   */
  String description();

  /**
   * Set a callback for completion, the specified {@code handler} is invoked when the test exec has completed.
   *
   * @param handler the completion handler
   */
  void endHandler(Handler<TestResult> handler);

}
