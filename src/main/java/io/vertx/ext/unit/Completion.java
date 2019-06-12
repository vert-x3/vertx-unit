package io.vertx.ext.unit;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;

/**
 * A completion object that emits completion notifications either <i>succeeded</i> or <i>failed</i>.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Completion<T> {

  /**
   * Completes the future upon completion, otherwise fails it.
   *
   * @param future the future to resolve
   */
  void resolve(Promise<T> future);

  /**
   * Completes the future upon completion, otherwise fails it.
   *
   * @param future the future to resolve
   * @deprecated use {@link #resolve(Promise)} instead
   */
  @Deprecated
  @GenIgnore
  void resolve(Future<T> future);

  /**
   * @return true if this completion is completed
   */
  boolean isCompleted();

  /**
   * @return true if this completion is completed and succeeded
   */
  boolean isSucceeded();

  /**
   * @return true if the this completion is completed and failed
   */
  boolean isFailed();

  /**
   * Completion handler to receive a completion signal when this completions completes.
   *
   * @param completionHandler the completion handler
   */
  void handler(Handler<AsyncResult<@Nullable T>> completionHandler);

  /**
   * Cause the current thread to wait until this completion completes.<p/>
   *
   * If the current thread is interrupted, an exception will be thrown.
   */
  void await();

  /**
   * Cause the current thread to wait until this completion completes with a configurable timeout.<p/>
   *
   * If completion times out or the current thread is interrupted, an exception will be thrown.
   *
   * @param timeoutMillis the timeout in milliseconds
   */
  void await(long timeoutMillis);

  /**
   * Cause the current thread to wait until this completion completes and succeeds.<p/>
   *
   * If the current thread is interrupted or the suite fails, an exception will be thrown.
   */
  void awaitSuccess();

  /**
   * Cause the current thread to wait until this completion completes and succeeds with a configurable timeout.<p/>
   *
   * If completion times out or the current thread is interrupted or the suite fails, an exception will be thrown.
   *
   * @param timeoutMillis the timeout in milliseconds
   */
  void awaitSuccess(long timeoutMillis);

}
