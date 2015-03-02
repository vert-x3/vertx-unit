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

package io.vertx.rxjava.ext.unit.collect;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.ext.unit.report.TestSuiteReport;
import io.vertx.rxjava.core.Vertx;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.eventbus.MessageConsumer;
import io.vertx.ext.unit.report.ReportingOptions;

/**
 * The event bus collector listen to events on the Vert.x event bus and translate them
 * into reports.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class EventBusCollector {

  final io.vertx.ext.unit.collect.EventBusCollector delegate;

  public EventBusCollector(io.vertx.ext.unit.collect.EventBusCollector delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Create a message handler reportsing with the specified options. The returned
   * message handler can be registered to an event bus.
   *
   * @param options the reporting options
   * @return the message handler
   */
  public static EventBusCollector create(Vertx vertx, ReportingOptions options) {
    EventBusCollector ret= EventBusCollector.newInstance(io.vertx.ext.unit.collect.EventBusCollector.create((io.vertx.core.Vertx) vertx.getDelegate(), options));
    return ret;
  }

  public static EventBusCollector create(Vertx vertx, Handler<TestSuiteReport> reporter) {
    EventBusCollector ret= EventBusCollector.newInstance(io.vertx.ext.unit.collect.EventBusCollector.create((io.vertx.core.Vertx) vertx.getDelegate(), new Handler<io.vertx.ext.unit.report.TestSuiteReport>() {
      public void handle(io.vertx.ext.unit.report.TestSuiteReport event) {
        reporter.handle(new TestSuiteReport(event));
      }
    }));
    return ret;
  }

  /**
   * Register the collector as a consumer of the event bus with the specified address.
   *
   * @param address the registration address
   * @return the subscribed message consumer
   */
  public MessageConsumer register(String address) {
    MessageConsumer ret= MessageConsumer.newInstance(this.delegate.register(address));
    return ret;
  }


  public static EventBusCollector newInstance(io.vertx.ext.unit.collect.EventBusCollector arg) {
    return new EventBusCollector(arg);
  }
}
