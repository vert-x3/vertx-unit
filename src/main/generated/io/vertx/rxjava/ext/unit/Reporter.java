/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.rxjava.ext.unit;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.eventbus.MessageProducer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class Reporter {

  final io.vertx.ext.unit.Reporter delegate;

  public Reporter(io.vertx.ext.unit.Reporter delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static Reporter eventBusReporter(MessageProducer<?> producer) {
    Reporter ret= Reporter.newInstance(io.vertx.ext.unit.Reporter.eventBusReporter((io.vertx.core.eventbus.MessageProducer<?>) producer.getDelegate()));
    return ret;
  }

  public static Reporter junitXmlReporter(Handler<Buffer> output) {
    Reporter ret= Reporter.newInstance(io.vertx.ext.unit.Reporter.junitXmlReporter(new Handler<io.vertx.core.buffer.Buffer>() {
      public void handle(io.vertx.core.buffer.Buffer event) {
        output.handle(new Buffer(event));
      }
    }));
    return ret;
  }


  public static Reporter newInstance(io.vertx.ext.unit.Reporter arg) {
    return new Reporter(arg);
  }
}
