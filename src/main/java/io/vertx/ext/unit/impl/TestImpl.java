package io.vertx.ext.unit.impl;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.Test;
import org.junit.Assert;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class TestImpl implements Test {

  private static final int STATUS_RUNNING = 0, STATUS_ASYNC = 1, STATUS_COMPLETED = 2;

  class AsyncImpl implements Async {

    private final AtomicBoolean completeCalled = new AtomicBoolean();

    @Override
    public void complete() {
      if (!completeCalled.compareAndSet(false, true)) {
        throw new IllegalStateException("The Async complete method cannot be called more than one time, check your test.");
      }
      internalComplete();
    }

    void internalComplete() {
      boolean complete;
      synchronized (TestImpl.this) {
        complete = asyncs.remove(this);
      }
      if (complete) {
        tryEnd();
      }
    }
  }

  private final InvokeTask invokeTask;
  private final Context context;
  private int status;
  private Throwable failed;
  private long beginTime;
  private final LinkedList<AsyncImpl> asyncs = new LinkedList<>();

  public TestImpl(InvokeTask invokeTask, Context context, Throwable failed) {
    this.invokeTask = invokeTask;
    this.context = context;
    this.failed = failed;
    this.status = STATUS_RUNNING;
  }

  private void tryEnd() {
    boolean end = false;
    synchronized (this) {
      if (asyncs.isEmpty() && status == STATUS_ASYNC) {
        status = STATUS_COMPLETED;
        end = true;
      }
    }
    if (end) {
      long endTime = System.currentTimeMillis();
      context.run(invokeTask.next, new Result(beginTime, endTime, failed));
    }
  }

  void run() {
    beginTime = System.currentTimeMillis();
    try {
      invokeTask.handler.handle(this);
    } catch (Throwable t) {
      failed(t);
    } finally {
      synchronized (this) {
        status = TestImpl.STATUS_ASYNC;
      }
      tryEnd();
    }
  }

  void failed(Throwable t) {
    synchronized (this) {
      switch (status) {
        case STATUS_COMPLETED:
          if (failed == null && invokeTask.unhandledFailureHandler != null) {
            invokeTask.unhandledFailureHandler.handle(t);
          }
          return;
        case STATUS_RUNNING:
          if (failed == null) {
            failed = t;
          }
          return;
        case STATUS_ASYNC:
          if (failed != null) {
            return;
          }
          asyncs.clear();
          failed = t;
          break;
      }
    }
    tryEnd();
  }

  @Override
  public Vertx vertx() {
    return context.vertx();
  }

  @Override
  public AsyncImpl async() {
    synchronized (this) {
      if (status != STATUS_COMPLETED) {
        AsyncImpl async = new AsyncImpl();
        asyncs.add(async);
        return async;
      } else {
        throw new IllegalStateException("Test already completed");
      }
    }
  }

  @Override
  public Test assertTrue(boolean condition, String message) {
    try {
      Assert.assertTrue(message, condition);
      return this;
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }

  public Test assertTrue(boolean condition) {
    try {
      Assert.assertTrue(condition);
      return this;
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }

  public void fail(String message) {
    try {
      Assert.fail(message);
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }

  @Override
  public Test assertFalse(boolean condition) {
    try {
      Assert.assertFalse(condition);
      return this;
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }

  @Override
  public Test assertFalse(boolean condition, String message) {
    try {
      Assert.assertFalse(message, condition);
      return this;
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }

  @Override
  public void fail() {
    try {
      Assert.fail();
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }

  @Override
  public Test assertEquals(Object expected, Object actual) {
    try {
      Assert.assertEquals(expected, actual);
      return this;
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }

  @Override
  public Test assertEquals(Object expected, Object actual, String message) {
    try {
      Assert.assertEquals(message, expected, actual);
      return this;
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }

  @Override
  public Test assertInRange(double expected, double actual, double delta) {
    try {
      Assert.assertEquals(expected, actual, delta);
      return this;
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }

  @Override
  public Test assertInRange(double expected, double actual, double delta, String message) {
    try {
      Assert.assertEquals(message, expected, actual, delta);
      return this;
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }

  @Override
  public Test assertNotEquals(Object first, Object second, String message) {
    try {
      Assert.assertNotEquals(message, first, second);
      return this;
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }

  @Override
  public Test assertNotEquals(Object first, Object second) {
    try {
      Assert.assertNotEquals(first, second);
      return this;
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }
}
