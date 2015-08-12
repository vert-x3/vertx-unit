package io.vertx.ext.unit.junit;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.impl.ExecutionContext;
import io.vertx.ext.unit.impl.Result;
import io.vertx.ext.unit.impl.Task;
import io.vertx.ext.unit.impl.TestContextImpl;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class AsyncStatement extends Statement {

  final FrameworkMethod fMethod;
  final Handler<TestContext> callback;

  public AsyncStatement(Handler<TestContext> callback, FrameworkMethod fMethod) {
    this.callback = callback;
    this.fMethod = fMethod;
  }

  protected long getDefaultTimeout() {
    return 2 * 60 * 1000L;
  }

  protected Context getContext() {
    return null;
  }


  protected Map<String, Object> getAttributes() {
    return new HashMap<>();
  }

  @Override
  public void evaluate() throws Throwable {
    CompletableFuture<Result> future = new CompletableFuture<>();
    Task<Result> task = (result, context) -> future.complete(result);
    Test annotation = fMethod.getAnnotation(Test.class);
    long timeout = getDefaultTimeout();
    if (annotation != null && annotation.timeout() > 0) {
      timeout = annotation.timeout();
    }
    TestContextImpl testContext = new TestContextImpl(
      getAttributes(),
        callback,
        err -> {},
        task,
        timeout);
    Context context = getContext();
    new ExecutionContext(context).run(testContext);
    Result result;
    try {
      result = future.get();
    } catch (InterruptedException e) {
      // Should we do something else ?
      Thread.currentThread().interrupt();
      throw e;
    }
    Throwable failure = result.getFailure();
    if (failure != null) {
      throw failure;
    }
  }
}
