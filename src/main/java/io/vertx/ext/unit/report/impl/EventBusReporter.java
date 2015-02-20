package io.vertx.ext.unit.report.impl;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.report.Failure;
import io.vertx.ext.unit.report.TestResult;
import io.vertx.ext.unit.impl.FailureImpl;
import io.vertx.ext.unit.report.Reporter;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class EventBusReporter implements Reporter<MessageProducer<JsonObject>> {

  private final Vertx vertx;
  private final String address;

  public EventBusReporter(Vertx vertx, String address) {
    this.vertx = vertx;
    this.address = address;
  }

  @Override
  public MessageProducer<JsonObject> createReport() {
    return vertx.eventBus().publisher(address);
  }

  @Override
  public void reportBeginTestSuite(MessageProducer<JsonObject> report, String name) {
    report.write(new JsonObject().
        put("type", "beginTestSuite").
        put("name", name));
  }

  @Override
  public void reportBeginTestCase(MessageProducer<JsonObject> report, String name) {
    report.write(new JsonObject().
        put("type", "beginTestCase").
        put("name", name));
  }

  @Override
  public void reportEndTestCase(MessageProducer<JsonObject> report, String name, TestResult result) {
    JsonObject json = new JsonObject().
        put("type", "endTestCase").
        put("name", result.name()).
        put("time", result.time());
    if (result.failed()) {
      Failure failure = result.failure();
      json.put("failure", ((FailureImpl) failure).toJson());
    }
    report.write(json);
  }

  @Override
  public void reportEndTestSuite(MessageProducer<JsonObject> report, String name, Throwable err) {
    JsonObject msg = new JsonObject().put("type", "endTestSuite").
        put("name", name);
    if (err != null) {
      msg.put("failure", new FailureImpl(err).toJson());
    }
    report.write(msg);
  }
}
