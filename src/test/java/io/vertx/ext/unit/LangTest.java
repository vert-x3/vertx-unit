package io.vertx.ext.unit;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.collect.EventBusCollector;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LangTest extends VertxTestBase {

  @org.junit.Test
  public void testAssertionsJs() throws Exception {
    testAssertions("js:verticle/assertions");
  }

  @org.junit.Test
  public void testAssertionsRuby() throws Exception {
    testAssertions("rb:verticle/assertions.rb");
  }

  private void testAssertions(String verticle) throws Exception {
    vertx.eventBus().<JsonObject>consumer("assert_tests").bodyStream().handler(msg -> {
      String type = msg.getString("type");
      switch (type) {
        case EventBusCollector.EVENT_TEST_CASE_END:
          String name = msg.getString("name");
          if (name.startsWith("fail_")) {
            assertNotNull(msg.getValue("failure"));
          } else {
            assertEquals(null, msg.getValue("failure"));
          }
          break;
        case EventBusCollector.EVENT_TEST_SUITE_END:
          testComplete();
          break;
      }
    });
    vertx.deployVerticle(verticle, ar -> {
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
      assertEquals("Error: the_failure", ar.cause().getMessage());
      testComplete();
    });
    await();
  }

  @org.junit.Test
  public void testRubyFailure() {
    vertx.deployVerticle("verticle/failing.rb", ar -> {
      assertTrue(ar.failed());
      assertEquals("(Exception) the_failure", ar.cause().getMessage());
      testComplete();
    });
    await();
  }
}
