package io.vertx.ext.unit;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.impl.TestCaseImpl;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface TestCase {

  static TestCase create(String desc, Handler<Test> handler) {
    return new TestCaseImpl(desc, handler);
  }

  TestCaseRunner runner();

  @GenIgnore
  void assertSuccess();

  @GenIgnore
  void assertSuccess(long timeout, TimeUnit unit);

  @GenIgnore
  void assertSuccess(Vertx vertx);

  /**
   * Run the test and assert it is a success.
   *
   * @param vertx the provided vertx
   * @param timeout the timeout value
   * @param unit the timeout unit
   */
  @GenIgnore
  void assertSuccess(Vertx vertx, long timeout, TimeUnit unit);
}
