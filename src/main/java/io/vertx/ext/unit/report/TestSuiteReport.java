package io.vertx.ext.unit.report;

import io.vertx.codegen.annotations.CacheReturn;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;

/**
 * The test suite reports is basically a stream of events reporting the test suite execution.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface TestSuiteReport extends ReadStream<TestCaseReport> {

  /**
   * @return the test suite name
   */
  @CacheReturn
  String name();

  /**
   * Set an exception handler, the exception handler reports the test suite errors, it can be called mulitple
   * times before the test ends.
   *
   * @param handler the exception handler
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  TestSuiteReport exceptionHandler(Handler<Throwable> handler);

  @Override
  TestSuiteReport handler(Handler<TestCaseReport> handler);

  @Override
  TestSuiteReport pause();

  @Override
  TestSuiteReport resume();

  @Override
  TestSuiteReport endHandler(Handler<Void> endHandler);

}
