package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.collect.EventBusCollector;
import io.vertx.test.core.AsyncTestBase;
import io.vertx.test.core.VertxTestBase;
import org.junit.*;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

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

  @org.junit.Test
  public void testJavaScriptTimer() {
    vertx.deployVerticle("js:verticle/timer", ar -> {
      assertTrue(ar.succeeded());
      testComplete();
    });
    await();
  }

  @org.junit.Test
  public void testJavaScriptFailure() {
    vertx.deployVerticle("js:verticle/failing", ar -> {
      assertTrue(ar.failed());
      ar.cause().printStackTrace();
      assertEquals("the_failure", ar.cause().getMessage());
      testComplete();
    });
    await();
  }

  @Test
  public void testGroovyTimer() {
    vertx.deployVerticle("verticle/timer.groovy", ar -> {
      assertTrue(ar.succeeded());
      testComplete();
    });
    await();
  }

  @Test
  public void testGroovyFailure() {
    vertx.deployVerticle("verticle/failing.groovy", ar -> {
      assertTrue(ar.failed());
      assertEquals("the_failure", ar.cause().getMessage());
      testComplete();
    });
    await();
  }
}
