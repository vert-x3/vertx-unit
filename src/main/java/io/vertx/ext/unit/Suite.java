package io.vertx.ext.unit;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import junit.framework.TestSuite;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Suite {

  @Fluent
  Suite before(Handler<Test> before);

  @Fluent
  Suite after(Handler<Test> callback);

  @Fluent
  Suite test(String desc, Handler<Test> handler);

  SuiteRunner runner();

  @GenIgnore
  TestSuite toJUnitSuite();

  @GenIgnore
  TestSuite toJUnitSuite(long timeout, TimeUnit unit);
}
