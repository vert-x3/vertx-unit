package io.vertx.ext.unit.collect;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.collect.impl.EventBusCollectorImpl;
import io.vertx.ext.unit.impl.ReporterHandler;
import io.vertx.ext.unit.report.Reporter;
import io.vertx.ext.unit.report.ReportingOptions;
import io.vertx.ext.unit.report.TestSuiteReport;

/**
 * The event bus collector listen to events on the Vert.x event bus and translate them
 * into reports.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface EventBusCollector {

  /**
   * Json {@code type} field value that signals a test suite begins, used as part of the test reporting
   * protocol for the event bus.
   */
  String EVENT_TEST_SUITE_BEGIN = "testSuiteBegin";

  /**
   * Json {@code type} field value that signals a test suite ends, used as part of the test reporting
   * protocol for the event bus.
   */
  String EVENT_TEST_SUITE_END = "testSuiteEnd";

  /**
   * Json {@code type} field value that reports a test suite error, used as part of the test reporting
   * protocol for the event bus.
   */
  String EVENT_TEST_SUITE_ERROR = "testSuiteError";

  /**
   * Json {@code type} field value that signals a test case begins, used as part of the test reporting
   * protocol for the event bus.
   */
  String EVENT_TEST_CASE_BEGIN = "testCaseBegin";

  /**
   * Json {@code type} field value that signals a test case ends, used as part of the test reporting
   * protocol for the event bus.
   */
  String EVENT_TEST_CASE_END = "testCaseEnd";

  /**
   * Create a message handler reportsing with the specified options. The returned
   * message handler can be registered to an event bus.
   *
   * @param options the reporting options
   * @return the message handler
   */
  static EventBusCollector create(Vertx vertx, ReportingOptions options) {
    Reporter[] reporters = options.getReporters().stream().map(reportOptions -> Reporter.reporter(vertx, reportOptions)).toArray(Reporter[]::new);
    ReporterHandler reporter = new ReporterHandler(null, reporters);
    return new EventBusCollectorImpl(vertx, reporter);
  }

  static EventBusCollector create(Vertx vertx, Handler<TestSuiteReport> reporter) {
    return new EventBusCollectorImpl(vertx, reporter);
  }

  /**
   * Register the collector as a consumer of the event bus with the specified address.
   *
   * @param address the registration address
   * @return the subscribed message consumer
   */
  MessageConsumer<?> register(String address);

  @GenIgnore
  Handler<Message<JsonObject>> asMessageHandler();
}
