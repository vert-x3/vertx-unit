package io.vertx.ext.unit;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.impl.EventBusAdapterImpl;
import io.vertx.ext.unit.report.TestSuiteReport;

/**
 * A adapter that listen to reports to the event bus and report them to an handler.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface EventBusAdapter extends Handler<Message<JsonObject>> {

  static EventBusAdapter create() {
    return new EventBusAdapterImpl();
  }

  @Fluent
  EventBusAdapter handler(Handler<TestSuiteReport> reporter);

}
