package io.vertx.ext.unit;

import groovy.lang.GroovyShell;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.test.core.AsyncTestBase;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LangTest extends AsyncTestBase {

  @org.junit.Test
  public void testGroovy() throws Exception {
    GroovyShell shell = new GroovyShell();
    shell.setVariable("done", (Runnable) () -> {
      testComplete();
    });
    shell.evaluate(LangTest.class.getResource("/plain/timer.groovy").toURI());
    await();
  }

  @org.junit.Test
  public void testAssertionsJs() throws Exception {
    testAssertions("js:verticle/assertions");
  }

  @org.junit.Test
  public void testAssertionsGroovy() throws Exception {
    testAssertions("groovy:verticle/assertions.groovy");
  }

  private void testAssertions(String verticle) throws Exception {
    Vertx vertx = Vertx.vertx();
    vertx.eventBus().<JsonObject>consumer("assert_tests").bodyStream().handler(msg -> {
      String type = msg.getString("type");
      switch (type) {
        case "endTestCase":
          String name = msg.getString("name");
          if (name.startsWith("fail_")) {
            assertNotNull(msg.getValue("failure"));
          } else {
            assertEquals(null, msg.getValue("failure"));
          }
          break;
        case "endTestSuite":
          testComplete();
          break;
      }
    });
    vertx.deployVerticle(verticle, ar -> {
      assertTrue(ar.succeeded());
    });
    await();
  }
}
