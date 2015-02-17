package io.vertx.ext.unit.impl;

import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Failure;
import io.vertx.ext.unit.Reporter;
import io.vertx.ext.unit.TestSuiteReport;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class EventBusReporter implements Reporter {

  private final MessageProducer producer;

  public EventBusReporter(MessageProducer producer) {
    this.producer = producer;
  }

  @Override
  public void handle(TestSuiteReport suiteRunner) {
    AtomicBoolean started = new AtomicBoolean();
    Runnable starter = () -> {
      if (started.compareAndSet(false, true)) {
        producer.write(new JsonObject().
                put("type", "beginTestSuite").
                put("name", suiteRunner.name())
        );
      }
    };
    suiteRunner.handler(caseRunner -> {
      starter.run();
      producer.write(new JsonObject().
          put("type", "beginTestCase").
          put("name", caseRunner.name()));
      caseRunner.endHandler(result -> {
        JsonObject json = new JsonObject().
            put("type", "endTestCase").
            put("name", result.name()).
            put("time", result.time());
        if (result.failed()) {
          Failure failure = result.failure();
          json.put("failure", new JsonObject().
              put("error", failure.isError()).
              put("message", failure.message()).
              put("stackTrace", failure.stackTrace())
          );
        }
        producer.write(json);
      });
    });
    suiteRunner.endHandler(done -> {
      starter.run();
      producer.write(new JsonObject().put("type", "endTestSuite").put("name", suiteRunner.name()));
    });
  }
}
