package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import io.vertx.test.core.AsyncTestBase;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JavaScriptTest extends AsyncTestBase {

  @Test
  public void testSimple() {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle("js:timer", ar -> {
      assertTrue(ar.succeeded());
    });
    await();
  }

}
