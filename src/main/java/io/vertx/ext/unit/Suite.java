package io.vertx.ext.unit;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;

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

}
