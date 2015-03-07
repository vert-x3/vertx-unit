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
   * @return true if the test suite completed
   */
  boolean isCompleted();

  /**
   * @return true if the test suite completed and succeeded
   */
  boolean isSucceeded();

  /**
   * @return true if the test suite completed and failed
   */
  boolean isFailed();

  /**
   * Completion handler for the end of the test, the result is successful when all test cases pass otherwise
   * it is failed.
   *
   * @param completionHandler the completion handler
   */
  void handler(Handler<AsyncResult<Void>> completionHandler);

  /**
   * Cause the current thread to wait until the test suite completes.<p/>
   *
   * If the current thread is interrupted, an exception will be thrown.
   */
  void await();

  /**
   * Cause the current thread to wait until the test suite completes with a configurable timeout.<p/>
   *
   * If completion times out or the current thread is interrupted, an exception will be thrown.
   *
   * @param timeoutMillis the timeout in milliseconds
   */
  void await(long timeoutMillis);

}
