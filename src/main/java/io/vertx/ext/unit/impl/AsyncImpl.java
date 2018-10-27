package io.vertx.ext.unit.impl;

import io.vertx.ext.unit.Async;

import java.util.concurrent.atomic.AtomicInteger;

class AsyncImpl extends CompletionImpl<Void> implements Async {

  private final int initialCount;
  private final AtomicInteger count;
  private final boolean strict;

  AsyncImpl(int initialCount, boolean strict) {
    this.initialCount = initialCount;
    this.strict = strict;
    this.count = new AtomicInteger(initialCount);
  }

  @Override
  public int count() {
    return count.get();
  }

  @Override
  public void countDown() {
    int oldValue, newValue;
    do {
      oldValue = count.get();
      if (oldValue == 0) {
        newValue = 0;
        if (strict) {
          String msg;
          if (initialCount == 1) {
            msg = "Countdown invoked more than once";
          } else if (initialCount == 2) {
            msg = "Countdown invoked more than twice";
          } else {
            msg = "Countdown invoked more than " + initialCount + " times";
          }
          throw new IllegalStateException(msg);
        }
      } else {
        newValue = oldValue - 1;
      }
    } while (!count.compareAndSet(oldValue, newValue));
    if (newValue == 0) {
      release(null);
    }
  }

  @Override
  public void complete() {
    int value = count.getAndSet(0);
    if (value > 0) {
      release(null);
    } else {
      throw new IllegalStateException("The Async complete method has been called more than " + initialCount + " times, check your test.");
    }
  }

  void release(Throwable failure) {
    if (failure != null) {
      completable.completeExceptionally(failure);
    } else {
      completable.complete(null);
    }
  }
}
