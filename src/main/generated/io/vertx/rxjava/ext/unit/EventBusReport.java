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

import io.vertx.ext.unit.EventBusAdapter;
import io.vertx.core.Handler;

/**
 * A {@link TestSuiteReport} that takes its input from the event bus.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class EventBusReport {

  final EventBusAdapter delegate;

  public EventBusReport(EventBusAdapter delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public EventBusReport handler(Handler<TestSuiteReport> reporter) {
    this.delegate.handler(new Handler<io.vertx.ext.unit.TestSuiteReport>() {
      public void handle(io.vertx.ext.unit.TestSuiteReport event) {
        reporter.handle(new TestSuiteReport(event));
      }
    });
    return this;
  }


  public static EventBusReport newInstance(EventBusAdapter arg) {
    return new EventBusReport(arg);
  }
}
