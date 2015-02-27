package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.Assert;

import java.util.LinkedList;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class TestContextImpl implements TestContext, Task<Result> {

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
      synchronized (TestContextImpl.this) {
        complete = asyncs.remove(this);
      }
      if (complete) {
        tryEnd();
      }
    }
  }

  private final Handler<TestContext> callback;
  private final Handler<Throwable> unhandledFailureHandler;
  private final Function<Result, Task<Result>> next;
  private final long timeout;
  private int status;
  private Throwable failed;
  private long beginTime;
  private TestSuiteContext context;
  private final LinkedList<AsyncImpl> asyncs = new LinkedList<>();

  public TestContextImpl(
      Handler<TestContext> callback,
      Handler<Throwable> unhandledFailureHandler,
      Task<Result> next,
      long timeout) {
    this.next = result -> next;
    this.timeout = timeout;
    this.callback = callback;
    this.unhandledFailureHandler = unhandledFailureHandler;
    this.status = STATUS_RUNNING;
  }

  public TestContextImpl(
      Handler<TestContext> callback,
      Handler<Throwable> unhandledFailureHandler,
      Function<Result, Task<Result>> next,
      long timeout) {
    this.next = next;
    this.timeout = timeout;
    this.callback = callback;
    this.unhandledFailureHandler = unhandledFailureHandler;
    this.status = STATUS_RUNNING;
  }

  private void tryEnd() {
    boolean end = false;
    TestSuiteContext ctx;
    synchronized (this) {
      if (asyncs.isEmpty() && status == STATUS_ASYNC) {
        status = STATUS_COMPLETED;
        end = true;
      }
      ctx = context;
    }
    if (end) {
      long endTime = System.currentTimeMillis();
      Result result = new Result(beginTime, endTime, failed);
      ctx.run(next.apply(result), result);
    }
  }

  @Override
  public void execute(Result prev, TestSuiteContext context) {
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
      new Thread(cancel).start();
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
          if (failed == null && unhandledFailureHandler != null) {
            unhandledFailureHandler.handle(t);
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
  public TestContext assertTrue(boolean condition, String message) {
    try {
      Assert.assertTrue(message, condition);
      return this;
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }

  public TestContext assertTrue(boolean condition) {
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
  public TestContext assertFalse(boolean condition) {
    try {
      Assert.assertFalse(condition);
      return this;
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }

  @Override
  public TestContext assertFalse(boolean condition, String message) {
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
  public TestContext assertEquals(Object expected, Object actual) {
    try {
      Assert.assertEquals(expected, actual);
      return this;
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }

  @Override
  public TestContext assertEquals(Object expected, Object actual, String message) {
    try {
      Assert.assertEquals(message, expected, actual);
      return this;
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }

  @Override
  public TestContext assertInRange(double expected, double actual, double delta) {
    try {
      Assert.assertEquals(expected, actual, delta);
      return this;
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }

  @Override
  public TestContext assertInRange(double expected, double actual, double delta, String message) {
    try {
      Assert.assertEquals(message, expected, actual, delta);
      return this;
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }

  @Override
  public TestContext assertNotEquals(Object first, Object second, String message) {
    try {
      Assert.assertNotEquals(message, first, second);
      return this;
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }

  @Override
  public TestContext assertNotEquals(Object first, Object second) {
    try {
      Assert.assertNotEquals(first, second);
      return this;
    } catch (AssertionError err) {
      failed(err);
      throw err;
    }
  }
}
