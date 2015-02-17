package io.vertx.ext.unit;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.ext.unit.impl.EventBusReporter;
import io.vertx.ext.unit.impl.JunitXmlReporter;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Reporter extends Handler<TestSuiteReport> {

  static Reporter eventBusReporter(MessageProducer<?> producer) {
    return new EventBusReporter((MessageProducer) producer);
  }

  static Reporter junitXmlReporter(Handler<Buffer> output) {
    return new JunitXmlReporter(output);
  }

}
