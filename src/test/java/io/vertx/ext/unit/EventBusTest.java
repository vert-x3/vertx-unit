package io.vertx.ext.unit;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.collect.EventBusCollector;
import io.vertx.ext.unit.impl.FailureImpl;
import io.vertx.ext.unit.report.ReportOptions;
import io.vertx.ext.unit.report.TestCaseReport;
import io.vertx.ext.unit.report.TestResult;
import io.vertx.test.core.TestUtils;
import io.vertx.test.core.VertxTestBase;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class EventBusTest extends VertxTestBase {

  @org.junit.Test
  public void testEventBusReporter() throws Exception {
    long now = System.currentTimeMillis();
    String address = TestUtils.randomAlphaString(10);
    String testSuiteName = TestUtils.randomAlphaString(10);
    String testCaseName1 = TestUtils.randomAlphaString(10);
    String testCaseName2 = TestUtils.randomAlphaString(10);
    String testCaseName3 = TestUtils.randomAlphaString(10);
    AtomicInteger status = new AtomicInteger();
    MessageConsumer<JsonObject> consumer = vertx.eventBus().localConsumer(address);
    consumer.handler(msg -> {
      JsonObject body = msg.body();
      String type = body.getString("type");
      switch (status.get()) {
        case 0:
          assertEquals(EventBusCollector.EVENT_TEST_SUITE_BEGIN, type);
          assertEquals(testSuiteName, body.getString("name"));
          break;
        case 1:
          assertEquals(EventBusCollector.EVENT_TEST_CASE_BEGIN, type);
          assertEquals(testCaseName1, body.getString("name"));
          break;
        case 2:
          assertEquals(EventBusCollector.EVENT_TEST_CASE_END, type);
          assertEquals(testCaseName1, body.getString("name"));
          assertTrue(body.getInteger("durationTime") >= 10);
          assertNull(testCaseName1, body.getJsonObject("failure"));
          break;
        case 3:
          assertEquals(EventBusCollector.EVENT_TEST_CASE_BEGIN, type);
          assertEquals(testCaseName2, body.getString("name"));
          break;
        case 4:
          assertEquals(EventBusCollector.EVENT_TEST_CASE_END, type);
          assertEquals(testCaseName2, body.getString("name"));
          assertTrue(body.getLong("beginTime") >= now);
          assertTrue(body.getLong("durationTime") >= 0);
          JsonObject failure = body.getJsonObject("failure");
          assertNotNull(failure);
          assertEquals("the_" + testCaseName2 + "_failure", failure.getString("message"));
          assertNotNull(failure.getString("stackTrace"));
          assertNotNull(failure.getBinary("cause"));
          break;
        case 5:
          assertEquals(EventBusCollector.EVENT_TEST_CASE_BEGIN, type);
          assertEquals(testCaseName3, body.getString("name"));
          break;
        case 6:
          assertEquals(EventBusCollector.EVENT_TEST_CASE_END, type);
          assertEquals(testCaseName3, body.getString("name"));
          assertTrue(body.getLong("beginTime") >= now);
          assertTrue(body.getLong("durationTime") >= 0);
          JsonObject error = body.getJsonObject("failure");
          assertNotNull(error);
          assertNull(error.getString("message"));
          assertNotNull(error.getString("stackTrace"));
          assertNotNull(error.getBinary("cause"));
          break;
        case 7:
          assertEquals(EventBusCollector.EVENT_TEST_SUITE_END, type);
          assertEquals(testSuiteName, body.getString("name"));
          consumer.unregister();
          testComplete();
          break;
        default:
          fail("Unexpected status " + status.get());

      }
      status.incrementAndGet();
    });
    consumer.completionHandler(ar -> {
      assertTrue(ar.succeeded());
      TestSuite.create(testSuiteName).test(testCaseName1, context -> {
        try {
          Thread.sleep(10);
        } catch (InterruptedException ignore) {
        }
      }).test(testCaseName2, context -> {
        context.fail("the_" + testCaseName2 + "_failure");
      }).test(testCaseName3, context -> {
        throw new RuntimeException();
      }).run(vertx, new TestOptions().addReporter(new ReportOptions().setTo("bus:" + address)));
    });
    await();
  }

  @org.junit.Test
  public void testEventBusReporterTestSuiteFailure() throws Exception {
    String address = TestUtils.randomAlphaString(10);
    String testSuiteName = TestUtils.randomAlphaString(10);
    String testCaseName = TestUtils.randomAlphaString(10);
    AtomicInteger status = new AtomicInteger();
    MessageConsumer<JsonObject> consumer = vertx.eventBus().localConsumer(address);
    consumer.handler(msg -> {
      JsonObject body = msg.body();
      String type = body.getString("type");
      switch (status.get()) {
        case 0:
          assertEquals(EventBusCollector.EVENT_TEST_SUITE_BEGIN, type);
          assertEquals(testSuiteName, body.getString("name"));
          break;
        case 1:
          assertEquals(EventBusCollector.EVENT_TEST_CASE_BEGIN, type);
          assertEquals(testCaseName, body.getString("name"));
          break;
        case 2:
          assertEquals(EventBusCollector.EVENT_TEST_CASE_END, type);
          assertEquals(testCaseName, body.getString("name"));
          assertNotNull(body.getLong("beginTime"));
          assertNotNull(body.getLong("durationTime"));
          assertNull(testCaseName, body.getJsonObject("failure"));
          break;
        case 3:
          assertEquals(EventBusCollector.EVENT_TEST_SUITE_ERROR, type);
          JsonObject failure = body.getJsonObject("failure");
          assertNotNull(failure);
          assertEquals("the_after_failure", failure.getString("message"));
          assertNotNull(failure.getString("stackTrace"));
          assertNotNull(failure.getBinary("cause"));
          break;
        case 4:
          assertEquals(EventBusCollector.EVENT_TEST_SUITE_END, type);
          assertEquals(testSuiteName, body.getString("name"));
          assertNull(body.getLong("beginTime"));
          assertNull(body.getLong("durationTime"));
          consumer.unregister();
          testComplete();
          break;
        default:
          fail("Unexpected status " + status.get());
      }
      status.incrementAndGet();
    });
    consumer.completionHandler(ar -> {
      assertTrue(ar.succeeded());
      TestSuite.create(testSuiteName).test(testCaseName, context -> {
        // Ok
      }).after(context -> {
        throw new RuntimeException("the_after_failure");
      }).run(vertx, new TestOptions().addReporter(new ReportOptions().setTo("bus:" + address)));
    });
    await();
  }

  @org.junit.Test
  public void testEventBusReport() throws Exception {
    long now = System.currentTimeMillis();
    String address = TestUtils.randomAlphaString(10);
    String testSuiteName = TestUtils.randomAlphaString(10);
    String testCaseName1 = TestUtils.randomAlphaString(10);
    String testCaseName2 = TestUtils.randomAlphaString(10);
    String testCaseName3 = TestUtils.randomAlphaString(10);
    MessageConsumer<JsonObject> consumer = vertx.eventBus().localConsumer(address);
    Handler<Message<JsonObject>> messageHandler = EventBusCollector.create(vertx, testSuite -> {
      Map<TestCaseReport, TestResult> results = new LinkedHashMap<>();
      testSuite.handler(testCase -> {
        testCase.endHandler(result -> {
          results.put(testCase, result);
        });
      });
      testSuite.endHandler(done -> {
        assertEquals(testSuiteName, testSuite.name());
        assertEquals(3, results.size());
        Iterator<Map.Entry<TestCaseReport, TestResult>> it = results.entrySet().iterator();
        Map.Entry<TestCaseReport, TestResult> entry1 = it.next();
        assertEquals(entry1.getKey().name(), entry1.getValue().name());
        assertEquals(testCaseName1, entry1.getValue().name());
        assertTrue(entry1.getValue().succeeded());
        assertTrue(entry1.getValue().beginTime() >= now);
        assertEquals(10, entry1.getValue().durationTime());
        assertNull(entry1.getValue().failure());
        Map.Entry<TestCaseReport, TestResult> entry2 = it.next();
        assertEquals(entry2.getKey().name(), entry2.getValue().name());
        assertEquals(testCaseName2, entry2.getValue().name());
        assertFalse(entry2.getValue().succeeded());
        assertTrue(entry2.getValue().beginTime() >= now);
        assertEquals(5, entry2.getValue().durationTime());
        assertNotNull(entry2.getValue().failure());
        assertEquals(false, entry2.getValue().failure().isError());
        assertEquals("the_failure_message", entry2.getValue().failure().message());
        assertEquals("the_failure_stackTrace", entry2.getValue().failure().stackTrace());
        assertTrue(entry2.getValue().failure().cause() instanceof IOException);
        Map.Entry<TestCaseReport, TestResult> entry3 = it.next();
        assertEquals(entry3.getKey().name(), entry3.getValue().name());
        assertEquals(testCaseName3, entry3.getValue().name());
        assertFalse(entry3.getValue().succeeded());
        assertTrue(entry3.getValue().beginTime() >= now);
        assertEquals(7, entry3.getValue().durationTime());
        assertNotNull(entry3.getValue().failure());
        assertEquals(false, entry3.getValue().failure().isError());
        assertEquals(null, entry3.getValue().failure().message());
        assertEquals("the_failure_stackTrace", entry3.getValue().failure().stackTrace());
        assertTrue(entry3.getValue().failure().cause() instanceof IOException);
        consumer.unregister();
        testComplete();
      });
    }).asMessageHandler();
    consumer.completionHandler(ar -> {
      assertTrue(ar.succeeded());
      vertx.eventBus().publish(address, new JsonObject().put("type", EventBusCollector.EVENT_TEST_SUITE_BEGIN).put("name", testSuiteName));
      vertx.eventBus().publish(address, new JsonObject().put("type", EventBusCollector.EVENT_TEST_CASE_BEGIN).put("name", testCaseName1));
      vertx.eventBus().publish(address, new JsonObject().put("type", EventBusCollector.EVENT_TEST_CASE_END).put("name", testCaseName1).put("beginTime", System.currentTimeMillis()).put("durationTime", 10L));
      vertx.eventBus().publish(address, new JsonObject().put("type", EventBusCollector.EVENT_TEST_CASE_BEGIN).put("name", testCaseName2));
      vertx.eventBus().publish(address, new JsonObject().put("type", EventBusCollector.EVENT_TEST_CASE_END).put("name", testCaseName2).put("beginTime", System.currentTimeMillis()).put("durationTime", 5L).
          put("failure", new FailureImpl(
              false, "the_failure_message", "the_failure_stackTrace", new IOException()).toJson()));
      vertx.eventBus().publish(address, new JsonObject().put("type", EventBusCollector.EVENT_TEST_CASE_BEGIN).put("name", testCaseName3));
      vertx.eventBus().publish(address, new JsonObject().put("type", EventBusCollector.EVENT_TEST_CASE_END).put("name", testCaseName3).put("beginTime", System.currentTimeMillis()).put("durationTime", 7L).
          put("failure", new FailureImpl(
              false, null, "the_failure_stackTrace", new IOException()).toJson()));
      vertx.eventBus().publish(address, new JsonObject().put("type", EventBusCollector.EVENT_TEST_SUITE_END));
    });
    consumer.handler(messageHandler);
    await();
  }

  @org.junit.Test
  public void testEventBusReportAfterFailure() throws Exception {
    String address = TestUtils.randomAlphaString(10);
    String testSuiteName = TestUtils.randomAlphaString(10);
    String testCaseName = TestUtils.randomAlphaString(10);
    MessageConsumer<JsonObject> consumer = vertx.eventBus().localConsumer(address);
    LinkedList<Throwable> suiteFailures = new LinkedList<>();
    Handler<Message<JsonObject>> messageHandler = EventBusCollector.create(vertx, testSuite -> {
      Map<TestCaseReport, TestResult> results = new LinkedHashMap<>();
      testSuite.handler(testCase -> {
        testCase.endHandler(result -> {
          assertEquals(Collections.emptyList(), suiteFailures);
          results.put(testCase, result);
        });
      });
      testSuite.exceptionHandler(suiteFailures::add);
      testSuite.endHandler(done -> {
        assertEquals(testSuiteName, testSuite.name());
        assertEquals(1, results.size());
        Iterator<Map.Entry<TestCaseReport, TestResult>> it = results.entrySet().iterator();
        Map.Entry<TestCaseReport, TestResult> entry1 = it.next();
        assertEquals(entry1.getKey().name(), entry1.getValue().name());
        assertEquals(testCaseName, entry1.getValue().name());
        assertTrue(entry1.getValue().succeeded());
        assertNull(entry1.getValue().failure());
        assertEquals(1, suiteFailures.size());
        assertTrue(suiteFailures.get(0) instanceof IOException);
        consumer.unregister();
        testComplete();
      });
    }).asMessageHandler();
    consumer.completionHandler(ar -> {
      assertTrue(ar.succeeded());
      vertx.eventBus().publish(address, new JsonObject().put("type", EventBusCollector.EVENT_TEST_SUITE_BEGIN).put("name", testSuiteName));
      vertx.eventBus().publish(address, new JsonObject().put("type", EventBusCollector.EVENT_TEST_CASE_BEGIN).put("name", testCaseName));
      vertx.eventBus().publish(address, new JsonObject().put("type", EventBusCollector.EVENT_TEST_CASE_END).put("name", testCaseName));
      vertx.eventBus().publish(address, new JsonObject().put("type", EventBusCollector.EVENT_TEST_SUITE_ERROR).
          put("failure", new FailureImpl(
              false, "the_failure_message", "the_failure_stackTrace", new IOException()).toJson()));
      vertx.eventBus().publish(address, new JsonObject().put("type", EventBusCollector.EVENT_TEST_SUITE_END));
    });
    consumer.handler(messageHandler);
    await();
  }

  @org.junit.Test
  public void testEndToEnd() {
    String testSuiteName = TestUtils.randomAlphaString(10);
    String testCaseName1 = TestUtils.randomAlphaString(10);
    String testCaseName2 = TestUtils.randomAlphaString(10);
    TestReporter testReporter = new TestReporter();
    EventBusCollector collector = EventBusCollector.create(vertx, testReporter);
    MessageConsumer<JsonObject> consumer = vertx.eventBus().consumer("the-address", collector.asMessageHandler());
    consumer.completionHandler(ar -> {
      assertTrue(ar.succeeded());
      TestSuite suite = TestSuite.create(testSuiteName).
          test(testCaseName1, context -> {
          }).test(testCaseName2, context -> context.fail("the_failure"));
      suite.run(vertx, new TestOptions().addReporter(new ReportOptions().setTo("bus:the-address")));
    });
    testReporter.await();
    assertEquals(0, testReporter.exceptions.size());
    assertEquals(2, testReporter.results.size());
    TestResult result1 = testReporter.results.get(0);
    assertEquals(testCaseName1, result1.name());
    assertTrue(result1.succeeded());
    TestResult result2 = testReporter.results.get(1);
    assertEquals(testCaseName2, result2.name());
    assertTrue(result2.failed());
    assertEquals("the_failure", result2.failure().message());
    consumer.unregister();
  }

  @org.junit.Test
  public void testEndToEndAfterFailure() {
    long now = System.currentTimeMillis();
    String address = TestUtils.randomAlphaString(10);
    String testSuiteName = TestUtils.randomAlphaString(10);
    String testCaseName = TestUtils.randomAlphaString(10);
    TestReporter testReporter = new TestReporter();
    MessageConsumer<JsonObject> consumer = vertx.eventBus().consumer(address, EventBusCollector.create(vertx, testReporter).asMessageHandler());
    RuntimeException error = new RuntimeException("the_runtime_exception");
    consumer.completionHandler(ar -> {
      assertTrue(ar.succeeded());
      TestSuite suite = TestSuite.create(testSuiteName).
          test(testCaseName, context -> {
            try {
              Thread.sleep(10);
            } catch (InterruptedException ignore) {
            }
          }).after(context -> {
        throw error;
      });
      suite.run(vertx, new TestOptions().addReporter(new ReportOptions().setTo("bus:" + address)));
    });
    testReporter.await();
    assertEquals(1, testReporter.results.size());
    TestResult result1 = testReporter.results.get(0);
    assertEquals(testCaseName, result1.name());
    assertTrue(result1.succeeded());
    assertTrue(result1.beginTime() >= now);
    assertTrue(result1.durationTime() >= 10);
    assertEquals(1, testReporter.exceptions.size());
    Throwable cause = testReporter.exceptions.get(0);
    assertTrue(cause instanceof RuntimeException);
    assertEquals(error.getMessage(), cause.getMessage());
    assertTrue(Arrays.equals(error.getStackTrace(), cause.getStackTrace()));
    consumer.unregister();
  }
}
