package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.impl.FailureImpl;
import io.vertx.test.core.AsyncTestBase;
import io.vertx.test.core.TestUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class EventBusTest extends AsyncTestBase {

  @org.junit.Test
  public void testEventBusReporter() throws Exception {
    String testSuiteName = TestUtils.randomAlphaString(10);
    String testCaseName1 = TestUtils.randomAlphaString(10);
    String testCaseName2 = TestUtils.randomAlphaString(10);
    Vertx vertx = Vertx.vertx();
    AtomicInteger status = new AtomicInteger();
    vertx.eventBus().<JsonObject>consumer("foobar", msg -> {
      JsonObject body = msg.body();
      String type = body.getString("type");
      switch (status.get()) {
        case 0:
          assertEquals("beginTestSuite", type);
          assertEquals(testSuiteName, body.getString("name"));
          break;
        case 1:
          assertEquals("beginTestCase", type);
          assertEquals(testCaseName1, body.getString("name"));
          break;
        case 2:
          assertEquals("endTestCase", type);
          assertEquals(testCaseName1, body.getString("name"));
          assertNotNull(body.getInteger("time"));
          assertNull(testCaseName1, body.getJsonObject("failure"));
          break;
        case 3:
          assertEquals("beginTestCase", type);
          assertEquals(testCaseName2, body.getString("name"));
          break;
        case 4:
          assertEquals("endTestCase", type);
          assertEquals(testCaseName2, body.getString("name"));
          assertNotNull(body.getInteger("time"));
          JsonObject failure = body.getJsonObject("failure");
          assertNotNull(failure);
          assertEquals("the_" + testCaseName2 + "_failure", failure.getString("message"));
          assertNotNull(failure.getString("stackTrace"));
          assertNotNull(failure.getBinary("cause"));
          break;
        case 5:
          assertEquals("endTestSuite", type);
          assertEquals(testSuiteName, body.getString("name"));
          testComplete();
          break;
        default:
          fail("Unexpected status " + status.get());

      }
      status.incrementAndGet();
    });
    TestSuite.create(testSuiteName).test(testCaseName1, test -> {
      // Ok
    }).test(testCaseName2, test -> {
      test.fail("the_" + testCaseName2 + "_failure");
    }).run(vertx, Reporter.eventBusReporter(vertx.eventBus().publisher("foobar")));
    await();
  }

  @org.junit.Test
  public void testEventBusReporterTestSuiteFailure() throws Exception {
    String testSuiteName = TestUtils.randomAlphaString(10);
    String testCaseName = TestUtils.randomAlphaString(10);
    Vertx vertx = Vertx.vertx();
    AtomicInteger status = new AtomicInteger();
    vertx.eventBus().<JsonObject>consumer("foobar", msg -> {
      JsonObject body = msg.body();
      String type = body.getString("type");
      switch (status.get()) {
        case 0:
          assertEquals("beginTestSuite", type);
          assertEquals(testSuiteName, body.getString("name"));
          break;
        case 1:
          assertEquals("beginTestCase", type);
          assertEquals(testCaseName, body.getString("name"));
          break;
        case 2:
          assertEquals("endTestCase", type);
          assertEquals(testCaseName, body.getString("name"));
          assertNotNull(body.getInteger("time"));
          assertNull(testCaseName, body.getJsonObject("failure"));
          break;
        case 3:
          assertEquals("endTestSuite", type);
          assertEquals(testSuiteName, body.getString("name"));
          assertNull(body.getValue("time"));
          JsonObject failure = body.getJsonObject("failure");
          assertNotNull(failure);
          assertEquals("the_after_failure", failure.getString("message"));
          assertNotNull(failure.getString("stackTrace"));
          assertNotNull(failure.getBinary("cause"));
          testComplete();
          break;
        default:
          fail("Unexpected status " + status.get());
      }
      status.incrementAndGet();
    });
    TestSuite.create(testSuiteName).test(testCaseName, test -> {
      // Ok
    }).after(test -> {
      throw new RuntimeException("the_after_failure");
    }).run(vertx, Reporter.eventBusReporter(vertx.eventBus().publisher("foobar")));
    await();
  }

  @org.junit.Test
  public void testEventBusReport() throws Exception {
    String testSuiteName = TestUtils.randomAlphaString(10);
    String testCaseName1 = TestUtils.randomAlphaString(10);
    String testCaseName2 = TestUtils.randomAlphaString(10);
    Vertx vertx = Vertx.vertx();
    EventBusAdapter slurper = EventBusAdapter.create();
    vertx.eventBus().consumer("foobar", slurper);
    slurper.handler(testSuite -> {
      Map<TestCaseReport, TestResult> results = new LinkedHashMap<>();
      testSuite.handler(testCase -> {
        testCase.endHandler(result -> {
          results.put(testCase, result);
        });
      });
      testSuite.endHandler(done -> {
        assertEquals(testSuiteName, testSuite.name());
        assertEquals(2, results.size());
        Iterator<Map.Entry<TestCaseReport, TestResult>> it = results.entrySet().iterator();
        Map.Entry<TestCaseReport, TestResult> entry1 = it.next();
        assertEquals(entry1.getKey().name(), entry1.getValue().name());
        assertEquals(testCaseName1, entry1.getValue().name());
        assertTrue(entry1.getValue().succeeded());
        assertNull(entry1.getValue().failure());
        Map.Entry<TestCaseReport, TestResult> entry2 = it.next();
        assertEquals(entry2.getKey().name(), entry2.getValue().name());
        assertEquals(testCaseName2, entry2.getValue().name());
        assertFalse(entry2.getValue().succeeded());
        assertNotNull(entry2.getValue().failure());
        assertEquals(false, entry2.getValue().failure().isError());
        assertEquals("the_failure_message", entry2.getValue().failure().message());
        assertEquals("the_failure_stackTrace", entry2.getValue().failure().stackTrace());
        assertTrue(entry2.getValue().failure().cause() instanceof IOException);
        testComplete();
      });
    });
    vertx.eventBus().publish("foobar", new JsonObject().put("type", "beginTestSuite").put("name", testSuiteName));
    vertx.eventBus().publish("foobar", new JsonObject().put("type", "beginTestCase").put("name", testCaseName1));
    vertx.eventBus().publish("foobar", new JsonObject().put("type", "endTestCase").put("name", testCaseName1));
    vertx.eventBus().publish("foobar", new JsonObject().put("type", "beginTestCase").put("name", testCaseName2));
    vertx.eventBus().publish("foobar", new JsonObject().put("type", "endTestCase").put("name", testCaseName2).
        put("failure", new FailureImpl(
            false, "the_failure_message", "the_failure_stackTrace", new IOException()).toJson()));
    vertx.eventBus().publish("foobar", new JsonObject().put("type", "endTestSuite"));
    await();
  }

  @org.junit.Test
  public void testEventBusReportAfterFailure() throws Exception {
    String testSuiteName = TestUtils.randomAlphaString(10);
    String testCaseName = TestUtils.randomAlphaString(10);
    Vertx vertx = Vertx.vertx();
    EventBusAdapter slurper = EventBusAdapter.create();
    vertx.eventBus().consumer("foobar", slurper);
    AtomicReference<Throwable> suiteFailure = new AtomicReference<>();
    slurper.handler(testSuite -> {
      Map<TestCaseReport, TestResult> results = new LinkedHashMap<>();
      testSuite.handler(testCase -> {
        testCase.endHandler(result -> {
          assertNull(suiteFailure.get());
          results.put(testCase, result);
        });
      });
      testSuite.exceptionHandler(suiteFailure::set);
      testSuite.endHandler(done -> {
        assertEquals(testSuiteName, testSuite.name());
        assertEquals(1, results.size());
        Iterator<Map.Entry<TestCaseReport, TestResult>> it = results.entrySet().iterator();
        Map.Entry<TestCaseReport, TestResult> entry1 = it.next();
        assertEquals(entry1.getKey().name(), entry1.getValue().name());
        assertEquals(testCaseName, entry1.getValue().name());
        assertTrue(entry1.getValue().succeeded());
        assertNull(entry1.getValue().failure());
        assertNotNull(suiteFailure.get());
        assertTrue(suiteFailure.get() instanceof IOException);
        testComplete();
      });
    });
    vertx.eventBus().publish("foobar", new JsonObject().put("type", "beginTestSuite").put("name", testSuiteName));
    vertx.eventBus().publish("foobar", new JsonObject().put("type", "beginTestCase").put("name", testCaseName));
    vertx.eventBus().publish("foobar", new JsonObject().put("type", "endTestCase").put("name", testCaseName));
    vertx.eventBus().publish("foobar", new JsonObject().put("type", "endTestSuite").
        put("failure", new FailureImpl(
            false, "the_failure_message", "the_failure_stackTrace", new IOException()).toJson()));
    await();
  }

  @org.junit.Test
  public void testEndToEnd() {
    String testSuiteName = TestUtils.randomAlphaString(10);
    String testCaseName1 = TestUtils.randomAlphaString(10);
    String testCaseName2 = TestUtils.randomAlphaString(10);
    Vertx vertx = Vertx.vertx();
    EventBusAdapter slurper = EventBusAdapter.create();
    vertx.eventBus().consumer("the-address", slurper);
    TestReporter testReporter = new TestReporter();
    slurper.handler(testReporter);
    Reporter reporter = Reporter.eventBusReporter(vertx.eventBus().publisher("the-address"));
    TestSuite suite = TestSuite.create(testSuiteName).
        test(testCaseName1, test -> {}).test(testCaseName2, test -> fail());
    suite.run(reporter);
    testReporter.await();
    assertEquals(0, testReporter.exceptions.size());
    assertEquals(2, testReporter.results.size());
    TestResult result1 = testReporter.results.get(0);
    assertEquals(testCaseName1, result1.name());
    assertTrue(result1.succeeded());
    TestResult result2 = testReporter.results.get(1);
    assertEquals(testCaseName2, result2.name());
    assertTrue(result2.failed());
  }

  @org.junit.Test
  public void testEndToEndAfterFailure() {
    String testSuiteName = TestUtils.randomAlphaString(10);
    String testCaseName = TestUtils.randomAlphaString(10);
    Vertx vertx = Vertx.vertx();
    EventBusAdapter slurper = EventBusAdapter.create();
    vertx.eventBus().consumer("the-address", slurper);
    TestReporter testReporter = new TestReporter();
    slurper.handler(testReporter);
    RuntimeException error = new RuntimeException("the_runtime_exception");
    Reporter reporter = Reporter.eventBusReporter(vertx.eventBus().publisher("the-address"));
    TestSuite suite = TestSuite.create(testSuiteName).
        test(testCaseName, test -> {}).after(test -> { throw error; });
    suite.run(reporter);
    testReporter.await();
    assertEquals(1, testReporter.results.size());
    TestResult result1 = testReporter.results.get(0);
    assertEquals(testCaseName, result1.name());
    assertTrue(result1.succeeded());
    assertEquals(1, testReporter.exceptions.size());
    Throwable cause = testReporter.exceptions.get(0);
    assertTrue(cause instanceof RuntimeException);
    assertEquals(error.getMessage(), cause.getMessage());
    assertTrue(Arrays.equals(error.getStackTrace(), cause.getStackTrace()));
  }
}
