package io.vertx.ext.unit.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestContextImpl implements TestContext, Task<Result> {

  private static final AtomicInteger threadCount = new AtomicInteger(0);
  private static final int STATUS_RUNNING = 0, STATUS_ASYNC = 1, STATUS_COMPLETED = 2;

  class AsyncImpl extends CompletionImpl<Void> implements Async {

    @Override
    public void complete() {
      if (completable.complete(null)) {
        internalComplete();
      } else if (!completable.isCompletedExceptionally()) {
        throw new IllegalStateException("The Async complete method cannot be called more than one time, check your test.");
      }
    }

    private void release() {
      if (TestContextImpl.this.failed != null) {
        completable.completeExceptionally(TestContextImpl.this.failed);
      } else {
        completable.complete(null);
      }
    }

    void internalComplete() {
      boolean complete;
      synchronized (TestContextImpl.this) {
        complete = asyncs.remove(this);
      }
      if (complete) {
        tryEnd();
      }
      release();
    }
  }

  private final Map<String, Object> attributes;
  private final Handler<TestContext> callback;
  private final Handler<Throwable> unhandledFailureHandler;
  private final Function<Result, Task<Result>> next;
  private final long timeout;
  private volatile Thread timeoutThread;
  private int status;
  private Throwable failed;
  private long beginTime;
  private ExecutionContext context;
  private final LinkedList<AsyncImpl> asyncs = new LinkedList<>();

  public TestContextImpl(
      Map<String, Object> attributes,
      Handler<TestContext> callback,
      Handler<Throwable> unhandledFailureHandler,
      Task<Result> next,
      long timeout) {
    this.attributes = attributes;
    this.next = result -> next;
    this.timeout = timeout;
    this.callback = callback;
    this.unhandledFailureHandler = unhandledFailureHandler;
    this.status = STATUS_RUNNING;
  }

  public TestContextImpl(
      Map<String, Object> attributes,
      Handler<TestContext> callback,
      Handler<Throwable> unhandledFailureHandler,
      Function<Result, Task<Result>> next,
      long timeout) {
    this.attributes = attributes;
    this.next = next;
    this.timeout = timeout;
    this.callback = callback;
    this.unhandledFailureHandler = unhandledFailureHandler;
    this.status = STATUS_RUNNING;
  }

  private void tryEnd() {
    boolean end = false;
    ExecutionContext ctx;
    synchronized (this) {
      if (asyncs.isEmpty() && status == STATUS_ASYNC) {
        status = STATUS_COMPLETED;
        end = true;
      }
      ctx = context;
    }
    if (end) {
      if (timeoutThread != null && timeoutThread.getState() == Thread.State.TIMED_WAITING) {
        timeoutThread.interrupt();
      }
      long endTime = System.currentTimeMillis();
      Result result = new Result(attributes, beginTime, endTime, failed);
      ctx.run(next.apply(result), result);
    }
  }

  @Override
  public synchronized <T> T get(String key) {
    return (T) attributes.get(key);
  }

  @Override
  public synchronized <T> T put(String key, Object value) {
    if (value != null) {
      return (T) attributes.put(key, value);
    } else {
      return (T) attributes.remove(key);
    }
  }

  @Override
  public synchronized <T> T remove(String key) {
    return (T) attributes.remove(key);
  }

  @Override
  public void execute(Result prev, ExecutionContext context) {
    synchronized (this) {
      this.context = context;
      if (prev != null) {
        failed = prev.failure;
        beginTime = prev.beginTime;
      } else {
        beginTime = System.currentTimeMillis();
      }
    }
    run();
  }

  private void run() {
    if (timeout > 0) {
      Runnable cancel = () -> {
        try {
          Thread.sleep(timeout);
          failed(new TimeoutException());
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      };
      timeoutThread = new Thread(cancel);
      timeoutThread.setName("vert.x-unit-timeout-thread-" + threadCount.incrementAndGet());
      timeoutThread.start();
    }
    try {
      callback.handle(this);
    } catch (Throwable t) {
      failed(t);
    } finally {
      synchronized (this) {
        status = TestContextImpl.STATUS_ASYNC;
      }
      tryEnd();
    }
  }

  void failed(Throwable t) {
    synchronized (this) {
      switch (status) {
        case STATUS_COMPLETED:
          releaseAndClearAsyncs();
          if (failed == null && unhandledFailureHandler != null) {
            unhandledFailureHandler.handle(t);
          }
          break;
        case STATUS_RUNNING:
          if (failed == null) {
            failed = t;
            releaseAndClearAsyncs();
          }
          break;
        case STATUS_ASYNC:
          if (failed == null) {
            failed = t;
            releaseAndClearAsyncs();
            tryEnd();
          }
          break;
      }
    }
  }

  private void releaseAndClearAsyncs() {
    List<AsyncImpl> copy;
    synchronized (this) {
      // Stack contention to avoid CME.
      copy = new ArrayList<>(asyncs);
      asyncs.clear();
    }
    for (AsyncImpl a : copy) {
      a.release();
    }

  }

  @Override
  public AsyncImpl async() {
    synchronized (this) {
      if (status != STATUS_COMPLETED) {
        AsyncImpl async = new AsyncImpl();
        if (failed == null) {
          asyncs.add(async);
        }
        return async;
      } else {
        throw new IllegalStateException("Test already completed");
      }
    }
  }

  @Override
  public TestContext assertNull(Object expected) {
    return assertNull(expected, null);
  }

  @Override
  public TestContext assertNull(Object expected, String message) {
    if (expected != null) {
      throw reportAssertionError(formatMessage(message, "Expected null"));
    }
    return this;
  }

  @Override
  public TestContext assertNotNull(Object expected) {
    return assertNotNull(expected, null);
  }

  @Override
  public TestContext assertNotNull(Object expected, String message) {
    if (expected == null) {
      throw reportAssertionError(formatMessage(message, "Expected not null"));
    }
    return this;
  }

  @Override
  public TestContext assertTrue(boolean condition, String message) {
    if (!condition) {
      throw reportAssertionError(formatMessage(message, "Expected true"));
    }
    return this;
  }

  public TestContext assertTrue(boolean condition) {
    return assertTrue(condition, null);
  }

  @Override
  public TestContext assertFalse(boolean condition) {
    return assertFalse(condition, null);
  }

  @Override
  public TestContext assertFalse(boolean condition, String message) {
    if (condition) {
      throw reportAssertionError(formatMessage(message, "Expected false"));
    }
    return this;
  }

  @Override
  public void fail() {
    fail((String) null);
  }

  public void fail(String message) {
    throw reportAssertionError(message != null ? message : "Test failed");
  }

  public void fail(Throwable cause) {
    failed(cause);
    Helper.uncheckedThrow(cause);
  }

  @Override
  public TestContext assertEquals(Object expected, Object actual) {
    return assertEquals(expected, actual, null);
  }

  @Override
  public TestContext assertEquals(Object expected, Object actual, String message) {
    if (actual == null) {
      if (expected != null) {
        throw reportAssertionError(formatMessage(message, "Expected " + expected + " got null"));
      }
    } else {
      if (expected == null) {
        throw reportAssertionError(formatMessage(message, "Expected null instead of " + actual));
      } else if (!expected.equals(actual)) {
        throw reportAssertionError(formatMessage(message, "Not equals : " + expected + " != " + actual));
      }
    }
    return this;
  }

  @Override
  public TestContext assertInRange(double expected, double actual, double delta) {
    return assertInRange(expected, actual, delta, null);
  }

  @Override
  public TestContext assertInRange(double expected, double actual, double delta, String message) {
    if (Double.compare(expected, actual) != 0 && Math.abs((actual - expected)) > delta) {
      throw reportAssertionError(formatMessage(message, "Expected " + actual + " to belong to [" +
          (expected - delta) + "," + (expected + delta) + "]"));
    }
    return this;
  }

  @Override
  public TestContext assertNotEquals(Object first, Object second, String message) {
    if (first == null) {
      if (second == null) {
        throw reportAssertionError(formatMessage(message, "Expected null != null"));
      }
    } else {
      if (first.equals(second)) {
        throw reportAssertionError(formatMessage(message, "Expected different values " + first + " != " + second));
      }
    }
    return this;
  }

  @Override
  public <T> Handler<AsyncResult<T>> asyncAssertSuccess() {
    return asyncAssertSuccess(result -> {
    });
  }

  @Override
  public <T> Handler<AsyncResult<T>> asyncAssertSuccess(Handler<T> resultHandler) {
    Async async = async();
    return ar -> {
      if (ar.succeeded()) {
        T result = ar.result();
        try {
          resultHandler.handle(result);
          async.complete();
        } catch (Throwable e) {
          failed(e);
        }
      } else {
        failed(ar.cause());
      }
    };
  }

  @Override
  public <T> Handler<AsyncResult<T>> asyncAssertFailure() {
    return asyncAssertFailure(cause -> {
    });
  }

  @Override
  public <T> Handler<AsyncResult<T>> asyncAssertFailure(Handler<Throwable> causeHandler) {
    Async async = async();
    return ar -> {
      if (ar.failed()) {
        Throwable result = ar.cause();
        try {
          causeHandler.handle(result);
          async.complete();
        } catch (Throwable e) {
          failed(e);
        }
      } else {
        failed(reportAssertionError("Was expecting a failure instead of of success"));
      }
    };
  }

  @Override
  public TestContext assertNotEquals(Object first, Object second) {
    return assertNotEquals(first, second, null);
  }

  /**
   * Create and report an assertion error, the returned throwable can be thrown to change
   * the control flow.
   *
   * @return an assertion error to eventually throw
   */
  private AssertionError reportAssertionError(String message) {
    AssertionError err = new AssertionError(message);
    failed(err);
    return err;
  }

  private static String formatMessage(String providedMessage, String defaultMessage) {
    return providedMessage == null ? defaultMessage : (providedMessage + ". " + defaultMessage);
  }
}
