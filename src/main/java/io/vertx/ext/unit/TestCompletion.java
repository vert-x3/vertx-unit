package io.vertx.ext.unit;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

/**
 * This object provides callback-ability for the end of a test suite.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface TestCompletion {

  /**
   * Completes the future when all test cases of the test suite passes, otherwise fails it.
   *
   * @param future the future to resolve
   */
  void resolve(Future future);

  /**
   * Completion handler for the end of the test, the result is successful when all test cases pass otherwise
   * it is failed.
   *
   * @param completionHandler the completion handler
   */
  void handler(Handler<AsyncResult<Void>> completionHandler);

}
