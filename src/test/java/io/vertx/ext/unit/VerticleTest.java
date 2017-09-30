package io.vertx.ext.unit;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.collect.EventBusCollector;
import io.vertx.test.core.VertxTestBase;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VerticleTest extends VertxTestBase {

  @org.junit.Test
  public void testCoordinated() {
    vertx.eventBus().<JsonObject>consumer("test").handler(msg -> {
      switch (msg.body().getString("type")) {
        case EventBusCollector.EVENT_TEST_SUITE_ERROR:
          // Replace with data object when done
          fail("Unexpected failure " + msg.body().getJsonObject("failure"));
          break;
        case EventBusCollector.EVENT_TEST_SUITE_END:
          testComplete();
          break;
      }
    });
    vertx.deployVerticle("js:verticle/coordinated/test", ar -> {
      assertTrue(ar.succeeded());
    });
    await();
  }
}
