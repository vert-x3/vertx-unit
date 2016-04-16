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

package io.vertx.groovy.ext.unit.collect;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.groovy.ext.unit.report.TestSuiteReport
import io.vertx.groovy.core.Vertx
import io.vertx.core.Handler
import io.vertx.groovy.core.eventbus.MessageConsumer
import io.vertx.ext.unit.report.ReportingOptions
/**
 * The event bus collector listen to events on the Vert.x event bus and translate them
 * into reports.
*/
@CompileStatic
public class EventBusCollector {
  private final def io.vertx.ext.unit.collect.EventBusCollector delegate;
  public EventBusCollector(Object delegate) {
    this.delegate = (io.vertx.ext.unit.collect.EventBusCollector) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Create a message handler reporting with the specified options. The returned
   * message handler can be registered to an event bus.
   * @param vertx 
   * @param options the reporting options (see <a href="../../../../../../../../cheatsheet/ReportingOptions.html">ReportingOptions</a>)
   * @return the message handler
   */
  public static EventBusCollector create(Vertx vertx, Map<String, Object> options) {
    def ret = InternalHelper.safeCreate(io.vertx.ext.unit.collect.EventBusCollector.create(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null, options != null ? new io.vertx.ext.unit.report.ReportingOptions(new io.vertx.core.json.JsonObject(options)) : null), io.vertx.groovy.ext.unit.collect.EventBusCollector.class);
    return ret;
  }
  public static EventBusCollector create(Vertx vertx, Handler<TestSuiteReport> reporter) {
    def ret = InternalHelper.safeCreate(io.vertx.ext.unit.collect.EventBusCollector.create(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null, reporter != null ? new Handler<io.vertx.ext.unit.report.TestSuiteReport>(){
      public void handle(io.vertx.ext.unit.report.TestSuiteReport event) {
        reporter.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.unit.report.TestSuiteReport.class));
      }
    } : null), io.vertx.groovy.ext.unit.collect.EventBusCollector.class);
    return ret;
  }
  /**
   * Register the collector as a consumer of the event bus with the specified address.
   * @param address the registration address
   * @return the subscribed message consumer
   */
  public MessageConsumer register(String address) {
    def ret = InternalHelper.safeCreate(delegate.register(address), io.vertx.groovy.core.eventbus.MessageConsumer.class);
    return ret;
  }
}
