package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import io.vertx.test.core.AsyncTestBase;
import org.junit.Test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class GroovyTest extends AsyncTestBase {

  @Test
  public void testTimer() {
    Vertx vertx = Vertx.vertx();
    vertx.eventBus().consumer("test").handler(msg -> {
      testComplete();
    });
    vertx.deployVerticle("timer.groovy", ar -> {
      assertTrue(ar.succeeded());
    });
    await();
  }
}
