package io.vertx.ext.unit.report.impl;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.collect.EventBusCollector;
import io.vertx.ext.unit.report.Failure;
import io.vertx.ext.unit.report.TestResult;
import io.vertx.ext.unit.impl.FailureImpl;
import io.vertx.ext.unit.report.Reporter;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class EventBusReporter implements Reporter<EventBusReporter.EventBusReport> {

  public static class EventBusReport {
    String name;
  }

  private final Vertx vertx;
  private final String address;

  public EventBusReporter(Vertx vertx, String address) {
    this.vertx = vertx;
    this.address = address;
  }

  @Override
  public EventBusReport createReport() {
    return new EventBusReport();
  }

  @Override
  public void reportBeginTestSuite(EventBusReport report, String name) {
    report.name = name;
    vertx.eventBus().publish(address, new JsonObject().
        put("type", EventBusCollector.EVENT_TEST_SUITE_BEGIN).
        put("name", name));
  }

  @Override
  public void reportBeginTestCase(EventBusReport report, String name) {
    vertx.eventBus().publish(address, new JsonObject().
        put("type", EventBusCollector.EVENT_TEST_CASE_BEGIN).
        put("name", name));
  }

  @Override
  public void reportEndTestCase(EventBusReport report, String name, TestResult result) {
    JsonObject json = new JsonObject().
        put("type", EventBusCollector.EVENT_TEST_CASE_END).
        put("name", result.name()).
        put("time", result.time());
    if (result.failed()) {
      Failure failure = result.failure();
      json.put("failure", ((FailureImpl) failure).toJson());
    }
    vertx.eventBus().publish(address, json);
  }

  @Override
  public void reportError(EventBusReport report, Throwable err) {
    JsonObject msg = new JsonObject().put("type", EventBusCollector.EVENT_TEST_SUITE_ERROR);
    msg.put("failure", new FailureImpl(err).toJson());
    vertx.eventBus().publish(address, msg);
  }

  @Override
  public void reportEndTestSuite(EventBusReport report) {
    JsonObject msg = new JsonObject().put("type", EventBusCollector.EVENT_TEST_SUITE_END).
        put("name", report.name);
    vertx.eventBus().publish(address, msg);
  }
}
