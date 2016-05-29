package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.TestContext;

import java.util.Map;
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestContextTask implements Task<Result> {

  private final TestContextImpl testContext;
  private final Handler<TestContext> callback;
  private Function<Result, Task<Result>> next;
  private final long timeout;

  public TestContextTask(TestContextImpl testContext, Handler<TestContext> callback, Task<Result> next, long timeout) {
    this.testContext = testContext;
    this.callback = callback;
    this.next = result -> next;
    this.timeout = timeout;
  }

  public TestContextTask(TestContextImpl testContext, Handler<TestContext> callback, Function<Result, Task<Result>> next, long timeout) {
    this.testContext = testContext;
    this.callback = callback;
    this.next = next;
    this.timeout = timeout;
  }

  @Override
  public void execute(Result prev, ExecutionContext context) {
    long beginTime;
    if (prev != null) {
      beginTime = prev.beginTime;
    } else {
      beginTime = System.currentTimeMillis();
    }
    testContext.run(prev != null ? prev.failure : null, timeout, callback, failed -> {
      long endTime = System.currentTimeMillis();
      Result result = new Result(beginTime, endTime, failed);
      context.run(next.apply(result), result);
    });
  }
}
