package io.vertx.ext.unit;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Module {

  @Fluent
  Module before(Handler<Void> callback);

  @Fluent
  Module after(Handler<Void> callback);

  @Fluent
  Module test(String desc, Handler<Test> handler);

  ModuleExec exec();

}
