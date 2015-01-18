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

  @GenIgnore
  void assertSuccess();

  @GenIgnore
  void assertSuccess(long timeout, TimeUnit unit);

  @GenIgnore
  void assertSuccess(Vertx vertx);

  /**
   * Run the test and assert it is a success.
   *
   * @param vertx the provided vertx
   * @param timeout the timeout value
   * @param unit the timeout unit
   */
  @GenIgnore
  void assertSuccess(Vertx vertx, long timeout, TimeUnit unit);
}
