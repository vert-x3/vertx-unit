package examples;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.docgen.Source;
import io.vertx.ext.unit.*;
import io.vertx.ext.unit.collect.EventBusCollector;
import io.vertx.ext.unit.report.ReportOptions;
import io.vertx.ext.unit.report.ReportingOptions;
import org.junit.Assert;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Examples {

  public static void test_01() {
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.test("my_test_case", context -> {
      String s = "value";
      context.assertEquals("value", s);
    });
    suite.run();
  }

  public static void test_02() {
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.test("my_test_case", context -> {
      String s = "value";
      context.assertEquals("value", s);
    });
    suite.run(new TestOptions().addReporter(new ReportOptions().setTo("console")));
  }

  public static void writing_test_suite_01() {
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.test("my_test_case_1", context -> {
      // Test 1
    });
    suite.test("my_test_case_2", context -> {
      // Test 2
    });
    suite.test("my_test_case_3", context -> {
      // Test 3
    });
  }

  public static void writing_test_suite_02() {
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.test("my_test_case_1", context -> {
      // Test 1
    }).test("my_test_case_2", context -> {
      // Test 2
    }).test("my_test_case_3", context -> {
      // Test 3
    });
  }

  public static void writing_test_suite_03() {
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.before(context -> {
      // Test suite setup
    }).test("my_test_case_1", context -> {
      // Test 1
    }).test("my_test_case_2", context -> {
      // Test 2
    }).test("my_test_case_3", context -> {
      // Test 3
    }).after(context -> {
      // Test suite cleanup
    });
  }

  public static void writing_test_suite_04() {
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.beforeEach(context -> {
      // Test case setup
    }).test("my_test_case_1", context -> {
      // Test 1
    }).test("my_test_case_2", context -> {
      // Test 2
    }).test("my_test_case_3", context -> {
      // Test 3
    }).afterEach(context -> {
      // Test case cleanup
    });
  }

  public static void asserting_01(io.vertx.ext.unit.TestSuite suite, int callbackCount) {
    suite.test("my_test_case", context -> {
      context.assertEquals(10, callbackCount);
    });
  }

  public static void asserting_02(io.vertx.ext.unit.TestSuite suite, int callbackCount) {
    suite.test("my_test_case", context -> {
      context.assertEquals(10, callbackCount, "Should have been 10 instead of " + callbackCount);
    });
  }

  public static void asserting_03(io.vertx.ext.unit.TestSuite suite, int callbackCount) {
    suite.test("my_test_case", context -> {
      context.assertNotEquals(10, callbackCount);
    });
  }

  public static void asserting_04(io.vertx.ext.unit.TestSuite suite, int callbackCount) {
    suite.test("my_test_case", context -> {
      context.assertNull(null);
    });
  }

  public static void asserting_05(io.vertx.ext.unit.TestSuite suite, int callbackCount) {
    suite.test("my_test_case", context -> {
      context.assertNotNull("not null!");
    });
  }

  public static void asserting_06(io.vertx.ext.unit.TestSuite suite, int callbackCount) {
    suite.test("my_test_case", context -> {

      // Assert that 0.1 is equals to 0.2 +/- 0.5

      context.assertInRange(0.1, 0.2, 0.5);
    });
  }

  public static void asserting_07(io.vertx.ext.unit.TestSuite suite, boolean var, int value) {
    suite.test("my_test_case", context -> {
      context.assertTrue(var);
      context.assertFalse(value > 10);
    });
  }

  public static void asserting_08(io.vertx.ext.unit.TestSuite suite) {
    suite.test("my_test_case", context -> {
      context.fail("That should never happen");
      // Following statements won't be executed
    });
  }

  public static void asserting_09(io.vertx.ext.unit.TestSuite suite, int callbackCount) {
    suite.test("my_test_case", context -> context.verify(v -> {
      // Using here Assert from junit, could be assertj, hamcrest or any other
      // Even manually throwing an AssertionError.
      Assert.assertNotNull("not null!");
      Assert.assertEquals(10, callbackCount);
    }));
  }

  public static void async_01(io.vertx.ext.unit.TestSuite suite, EventBus eventBus) {
    suite.test("my_test_case", context -> {
      Async async = context.async();
      eventBus.consumer("the-address", msg -> {
        // <2>
        async.complete();
      });
      // <1>
    });
  }

  public static void async_02(io.vertx.ext.unit.TestSuite suite, Vertx vertx) {
    suite.test("my_test_case", context -> {

      HttpClient client = vertx.createHttpClient();
      client.getNow(8080, "localhost", "/", context.asyncAssertSuccess(resp -> {
        context.assertEquals(200, resp.statusCode());
      }));

      Async async = context.async();
      vertx.eventBus().consumer("the-address", msg -> {
        async.complete();
      });
    });
  }

  public static void async_03(io.vertx.ext.unit.TestSuite suite, Vertx vertx, Handler<HttpServerRequest> requestHandler) {
    suite.before(context -> {
      Async async = context.async();
      HttpServer server = vertx.createHttpServer();
      server.requestHandler(requestHandler);
      server.listen(8080, ar -> {
        context.assertTrue(ar.succeeded());
        async.complete();
      });
    });
  }

  public static void async_04(TestContext context, Vertx vertx, Handler<HttpServerRequest> requestHandler) {
    Async async = context.async();
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(requestHandler);
    server.listen(8080, ar -> {
      context.assertTrue(ar.succeeded());
      async.complete();
    });

    // Wait until completion
    async.awaitSuccess();

    // Do something else
  }

  public static void async_05(TestContext context, Vertx vertx, Handler<HttpServerRequest> requestHandler) {
    Async async = context.async(2);
    HttpServer server = vertx.createHttpServer();
    server.requestHandler(requestHandler);
    server.listen(8080, ar -> {
      context.assertTrue(ar.succeeded());
      async.countDown();
    });

    vertx.setTimer(1000, id -> {
      async.complete();
    });

    // Wait until completion of the timer and the http request
    async.awaitSuccess();

    // Do something else
  }

  @Source(translate = false)
  public static void asyncAssertSuccess_01(Vertx vertx, TestContext context) {
    Async async = context.async();
    vertx.deployVerticle("my.verticle", ar -> {
      if (ar.succeeded()) {
        async.complete();
      } else {
        context.fail(ar.cause());
      }
    });

    // Can be replaced by

    vertx.deployVerticle("my.verticle", context.asyncAssertSuccess());
  }

  @Source(translate = false)
  public static void asyncAssertSuccess_02(Vertx vertx, TestContext context) {
    AtomicBoolean started = new AtomicBoolean();
    Async async = context.async();
    vertx.deployVerticle(new AbstractVerticle() {
      public void start() throws Exception {
        started.set(true);
      }
    }, ar -> {
      if (ar.succeeded()) {
        context.assertTrue(started.get());
        async.complete();
      } else {
        context.fail(ar.cause());
      }
    });

    // Can be replaced by

    vertx.deployVerticle("my.verticle", context.asyncAssertSuccess(id -> {
      context.assertTrue(started.get());
    }));
  }

  @Source(translate = false)
  public static void asyncAssertSuccess_03(Vertx vertx, TestContext context) {
    Async async = context.async();
    vertx.deployVerticle("my.verticle", ar1 -> {
      if (ar1.succeeded()) {
        vertx.deployVerticle("my.otherverticle", ar2 -> {
          if (ar2.succeeded()) {
            async.complete();
          } else {
            context.fail(ar2.cause());
          }
        });
      } else {
        context.fail(ar1.cause());
      }
    });

    // Can be replaced by

    vertx.deployVerticle("my.verticle", context.asyncAssertSuccess(id ->
            vertx.deployVerticle("my_otherverticle", context.asyncAssertSuccess())
    ));
  }

  @Source(translate = false)
  public static void asyncAssertFailure_01(Vertx vertx, TestContext context) {
    Async async = context.async();
    vertx.deployVerticle("my.verticle", ar -> {
      if (ar.succeeded()) {
        context.fail();
      } else {
        async.complete();
      }
    });

    // Can be replaced by

    vertx.deployVerticle("my.verticle", context.asyncAssertFailure());
  }

  @Source(translate = false)
  public static void asyncAssertFailure_02(Vertx vertx, TestContext context) {
    Async async = context.async();
    vertx.deployVerticle("my.verticle", ar -> {
      if (ar.succeeded()) {
        context.fail();
      } else {
        context.assertTrue(ar.cause() instanceof IllegalArgumentException);
        async.complete();
      }
    });

    // Can be replaced by

    vertx.deployVerticle("my.verticle", context.asyncAssertFailure(cause -> {
      context.assertTrue(cause instanceof IllegalArgumentException);
    }));
  }

  public static void repeating(Vertx vertx) {
    TestSuite.create("my_suite").test("my_test", 1000, context -> {
      // This will be executed 1000 times
    });
  }

  public static void sharing_01(Vertx vertx, Helper helper) {
    TestSuite.create("my_suite").before(context -> {

      // host is available for all test cases
      context.put("host", "localhost");

    }).beforeEach(context -> {

      // Generate a random port for each test
      int port = helper.randomPort();

      // Get host
      String host = context.get("host");

      // Setup server
      Async async = context.async();
      HttpServer server = vertx.createHttpServer();
      server.requestHandler(req -> {
        req.response().setStatusCode(200).end();
      });
      server.listen(port, host, ar -> {
        context.assertTrue(ar.succeeded());
        context.put("port", port);
        async.complete();
      });

    }).test("my_test", context -> {

      // Get the shared state
      int port = context.get("port");
      String host = context.get("host");

      // Do request
      HttpClient client = vertx.createHttpClient();
      client.getNow(port, host, "/resource", context.asyncAssertSuccess(resp -> {
        context.assertEquals(200, resp.statusCode());
      }));
    });
  }

  public static void running_01(TestSuite suite) {
    suite.run();
  }

  public static void running_02(TestSuite suite, Vertx vertx) {
    suite.run(vertx);
  }

  public static void completion_01(TestSuite suite, Vertx vertx) {
    TestCompletion completion = suite.run(vertx);

    // Simple completion callback
    completion.handler(ar -> {
      if (ar.succeeded()) {
        System.out.println("Test suite passed!");
      } else {
        System.out.println("Test suite failed:");
        ar.cause().printStackTrace();
      }
    });
  }

  public static void completion_02(TestSuite suite, Future<Void> startFuture) {
    TestCompletion completion = suite.run();

    // When the suite completes, the future is resolved
    completion.resolve(startFuture);
  }

  public static void completion_03(TestSuite suite) {
    Completion completion = suite.run();

    // Wait until the test suite completes
    completion.await();
  }

  public static void completion_04(TestSuite suite) {
    Completion completion = suite.run();

    // Wait until the test suite succeeds otherwise throw an exception
    completion.awaitSuccess();
  }

  public static void running_05(TestSuite suite) {
    TestOptions options = new TestOptions().setTimeout(10000);

    // Run with a 10 seconds time out
    suite.run(options);
  }

  public static void reporter_01(TestSuite suite) {

    // Report to console
    ReportOptions consoleReport = new ReportOptions().
        setTo("console");

    // Report junit files to the current directory
    ReportOptions junitReport = new ReportOptions().
        setTo("file:.").
        setFormat("junit");

    suite.run(new TestOptions().
            addReporter(consoleReport).
            addReporter(junitReport)
    );
  }

  public static void reporter_02(Vertx vertx) {
    EventBusCollector collector = EventBusCollector.create(
        vertx,
        new ReportingOptions().addReporter(
            new ReportOptions().setTo("file:report.xml").setFormat("junit")));

    collector.register("the-address");
  }

  public static void vertxInteg1(Vertx vertx, TestSuite suite) throws Exception {
    suite.test("my_test_case", ctx -> {

      // The failure will be reported by Vert.x Unit
      throw new RuntimeException("it failed!");
    });
  }

  public static void vertxInteg2(Vertx vertx, TestSuite suite) throws Exception {
    suite.test("test-server", testContext -> {
      HttpServer server = vertx.createHttpServer().requestHandler(req -> {
        if (req.path().equals("/somepath")) {
          throw new AssertionError("Wrong path!");
        }
        req.response().end();
      });
    });
  }

  public static void vertxInteg3(Vertx vertx, TestSuite suite) throws Exception {

    suite.before(testContext -> {

      // Report uncaught exceptions as Vert.x Unit failures
      vertx.exceptionHandler(testContext.exceptionHandler());
    });

    suite.test("test-server", testContext -> {
      HttpServer server = vertx.createHttpServer().requestHandler(req -> {
        if (req.path().equals("/somepath")) {
          throw new AssertionError("Wrong path!");
        }
        req.response().end();
      });
    });
  }
}
