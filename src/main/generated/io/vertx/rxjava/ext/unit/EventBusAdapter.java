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
import io.vertx.rxjava.ext.unit.report.TestSuiteReport;
import io.vertx.rxjava.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.Handler;

/**
 * A adapter that listen to reports to the event bus and report them to an handler.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class EventBusAdapter {

  final io.vertx.ext.unit.EventBusAdapter delegate;

  public EventBusAdapter(io.vertx.ext.unit.EventBusAdapter delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static EventBusAdapter create() {
    EventBusAdapter ret= EventBusAdapter.newInstance(io.vertx.ext.unit.EventBusAdapter.create());
    return ret;
  }

  public EventBusAdapter handler(Handler<TestSuiteReport> reporter) {
    this.delegate.handler(new Handler<io.vertx.ext.unit.report.TestSuiteReport>() {
      public void handle(io.vertx.ext.unit.report.TestSuiteReport event) {
        reporter.handle(new TestSuiteReport(event));
      }
    });
    return this;
  }


  public static EventBusAdapter newInstance(io.vertx.ext.unit.EventBusAdapter arg) {
    return new EventBusAdapter(arg);
  }
}
