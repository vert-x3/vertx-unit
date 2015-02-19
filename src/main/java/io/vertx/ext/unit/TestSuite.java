package io.vertx.ext.unit;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.impl.TestSuiteImpl;
import io.vertx.ext.unit.report.ReportOptions;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface TestSuite {

  static TestSuite create(String name) {
    return new TestSuiteImpl(name);
  }

  @Fluent
  TestSuite before(Handler<Test> handler);

  @Fluent
  TestSuite beforeEach(Handler<Test> handler);

  @Fluent
  TestSuite after(Handler<Test> callback);

  @Fluent
  TestSuite afterEach(Handler<Test> callback);

  @Fluent
  TestSuite test(String name, Handler<Test> handler);

  void run();

  void run(Vertx vertx);

  void run(TestOptions options);

  void run(TestOptions options, Handler<AsyncResult<Void>> completionHandler);

  void run(TestOptions options, Future future);

  void run(Vertx vertx, TestOptions options);

  void run(Vertx vertx, TestOptions options, Handler<AsyncResult<Void>> completionHandler);

  void run(Vertx vertx, TestOptions options, Future future);

  TestSuiteRunner runner();

  TestSuiteRunner runner(Vertx vertx);

  @GenIgnore
  junit.framework.TestSuite toJUnitSuite();

  @GenIgnore
  junit.framework.TestSuite toJUnitSuite(long timeout, TimeUnit unit);
}
