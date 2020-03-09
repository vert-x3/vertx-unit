package io.vertx.ext.unit.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.ext.unit.Completion;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CompletionImpl<T> implements Completion<T> {

  protected final CompletableFuture<T> completable = new CompletableFuture<>();

  @Override
  public void resolve(Promise<T> future) {
    completable.whenComplete((done, err) -> {
      if (err != null) {
        future.fail(err);
      } else {
        future.complete();
      }
    });
  }

  @Override
  public boolean isCompleted() {
    return completable.isDone();
  }

  @Override
  public boolean isSucceeded() {
    return isCompleted() && !isFailed();
  }

  @Override
  public boolean isFailed() {
    return completable.isCompletedExceptionally();
  }

  @Override
  public void handler(Handler<AsyncResult<T>> completionHandler) {
    Promise<T> completion = Promise.promise();
    completion.future().onComplete(completionHandler);
    resolve(completion);
  }

  @Override
  public void await() {
    try {
      completable.get();
    } catch (ExecutionException ignore) {
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      Helper.uncheckedThrow(e);
    }
  }

  @Override
  public void await(long timeoutMillis) {
    try {
      completable.get(timeoutMillis, TimeUnit.MILLISECONDS);
    } catch (ExecutionException ignore) {
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      Helper.uncheckedThrow(e);
    } catch (TimeoutException e) {
      Helper.uncheckedThrow(new TimeoutException("Timed out"));
    }
  }

  @Override
  public void awaitSuccess() {
    try {
      completable.get();
    } catch (ExecutionException result) {
      Helper.uncheckedThrow(result.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      Helper.uncheckedThrow(e);
    }
  }

  @Override
  public void awaitSuccess(long timeoutMillis) {
    try {
      completable.get(timeoutMillis, TimeUnit.MILLISECONDS);
    } catch (ExecutionException result) {
      Helper.uncheckedThrow(result.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      Helper.uncheckedThrow(e);
    } catch (TimeoutException e) {
      Helper.uncheckedThrow(new TimeoutException("Timed out"));
    }
  }
}
