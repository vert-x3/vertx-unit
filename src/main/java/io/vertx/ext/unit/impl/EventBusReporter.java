package io.vertx.ext.unit.impl;

import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Failure;
import io.vertx.ext.unit.Reporter;
import io.vertx.ext.unit.TestSuiteReport;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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
          json.put("failure", ((FailureImpl) failure).toJson());
        }
        producer.write(json);
      });
    });
    AtomicReference<Throwable> exception = new AtomicReference<>();
    suiteRunner.exceptionHandler(exception::set);
    suiteRunner.endHandler(done -> {
      starter.run();
      JsonObject msg = new JsonObject().put("type", "endTestSuite").
          put("name", suiteRunner.name());
      Throwable error = exception.get();
      if (error != null) {
        msg.put("failure", new FailureImpl(exception.get()).toJson());
      }
      producer.write(msg);
    });
  }
}
