package io.vertx.ext.unit.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestContextImpl implements TestContext {

  private static final AtomicInteger threadCount = new AtomicInteger(0);
  private static final Throwable SUCCESS = new Throwable();

  private final Map<String, Object> attributes;
  private final Handler<Throwable> unhandledFailureHandler;
  private CountDownLatch completionLatch;
  private Handler<Throwable> completionHandler;
  private List<AsyncImpl> asyncs = new ArrayList<>();
  private Throwable completionResult;
  private boolean running;

  public TestContextImpl(Map<String, Object> attributes, Handler<Throwable> unhandledFailureHandler) {
    this.attributes = attributes;
    this.unhandledFailureHandler = unhandledFailureHandler;
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

  public void run(Throwable failed, long timeout, Handler<TestContext> test, Handler<Throwable> eh) {
    CountDownLatch latch = new CountDownLatch(1);
    synchronized (this) {
      if (running) {
        throw new IllegalStateException("Already running");
      }
      running = true;
      completionLatch = latch;
      completionResult = null;
      completionHandler = err -> {
        if (failed != null) {
          eh.handle(failed);
        } else {
          eh.handle(err);
        }
      };
    }
    if (timeout > 0) {
      Runnable cancel = () -> {
        try {
          if (latch.await(timeout, TimeUnit.MILLISECONDS)) {
            return;
          }
          tryFail(new TimeoutException());
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt(); // Should fail as interrupted ?
        }
      };
      Thread timeoutThread = new Thread(cancel);
      timeoutThread.setName("vert.x-unit-timeout-thread-" + threadCount.incrementAndGet());
      timeoutThread.start();
    }
    Async async = async(1, false);
    try {
      test.handle(TestContextImpl.this);
      async.complete();
    } catch (Throwable t) {
      tryFail(t);
    }
  }

  public void failed(Throwable t) {
    boolean reported;
    synchronized (this) {
      reported = running && tryFail(t);
    }
    if (!reported && unhandledFailureHandler != null) {
      unhandledFailureHandler.handle(t);
    }
  }

  private boolean tryFail(Throwable t) {
    return tryEnd(t);
  }

  private boolean tryEnd(Throwable cause) {
    List<AsyncImpl> copy;
    Handler<Throwable> handler;
    synchronized (TestContextImpl.this) {
      if (completionResult != null || (asyncs.size() > 0 && cause == null)) {
        return false;
      }
      completionResult = cause == null ? SUCCESS : cause;
      // Stack contention to avoid CME.
      copy = new ArrayList<>(asyncs);
      asyncs.clear();
      running = false;
      handler = completionHandler;
      completionLatch.countDown();
    }
    for (AsyncImpl a : copy) {
      a.release(cause);
    }
    handler.handle(cause);
    return true;
  }

  @Override
  public Async async() {
    return async(1);
  }

  @Override
  public Async async(int count) {
    return async(count, false);
  }

  @Override
  public Async strictAsync(int count) {
    return async(count, true);
  }

  private Async async(int count, boolean strict) {
    if (count < 1) {
      throw new IllegalArgumentException("Async completion count must be > 0");
    }
    synchronized (this) {
      if (running) {
        if (completionResult == null) {
          AsyncImpl async = new AsyncImpl(count, strict);
          asyncs.add(async);
          async.completable.whenComplete((v, err) -> {
            synchronized (TestContextImpl.this) {
              asyncs.remove(async);
            }
            tryEnd(null);
          });
          return async;
        } else {
          throw new IllegalStateException("Test already completed");
        }
      } else {
        return new AsyncImpl(count, strict);
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
  public Handler<Throwable> exceptionHandler() {
    return this::failed;
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
  public TestContext verify(Handler<Void> block) {
    try {
      block.handle(null);
    } catch (Throwable t) {
      fail(t);
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
        reportAssertionError("Was expecting a failure instead of of success");
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
