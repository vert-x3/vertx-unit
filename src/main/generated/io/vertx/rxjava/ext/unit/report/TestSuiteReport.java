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

package io.vertx.rxjava.ext.unit.report;

import java.util.Map;
import rx.Observable;
import io.vertx.rxjava.core.streams.ReadStream;
import io.vertx.core.Handler;

/**
 * The test suite reports is basically a stream of events reporting the test suite execution.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.unit.report.TestSuiteReport original} non RX-ified interface using Vert.x codegen.
 */

public class TestSuiteReport implements ReadStream<TestCaseReport> {

  final io.vertx.ext.unit.report.TestSuiteReport delegate;

  public TestSuiteReport(io.vertx.ext.unit.report.TestSuiteReport delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  private rx.Observable<TestCaseReport> observable;

  public synchronized rx.Observable<TestCaseReport> toObservable() {
    if (observable == null) {
      java.util.function.Function<io.vertx.ext.unit.report.TestCaseReport, TestCaseReport> conv = TestCaseReport::newInstance;
      io.vertx.lang.rxjava.ReadStreamAdapter<io.vertx.ext.unit.report.TestCaseReport, TestCaseReport> adapter = new io.vertx.lang.rxjava.ReadStreamAdapter<>(this, conv);
      observable = rx.Observable.create(adapter);
    }
    return observable;
  }

  /**
   * @return the test suite name
   * @return 
   */
  public String name() { 
    if (cached_0 != null) {
      return cached_0;
    }
    String ret = delegate.name();
    cached_0 = ret;
    return ret;
  }

  /**
   * Set an exception handler, the exception handler reports the test suite errors, it can be called mulitple
   * times before the test ends.
   * @param handler the exception handler
   * @return a reference to this, so the API can be used fluently
   */
  public TestSuiteReport exceptionHandler(Handler<Throwable> handler) { 
    ((io.vertx.ext.unit.report.TestSuiteReport) delegate).exceptionHandler(handler);
    return this;
  }

  public TestSuiteReport handler(Handler<TestCaseReport> handler) { 
    ((io.vertx.ext.unit.report.TestSuiteReport) delegate).handler(new Handler<io.vertx.ext.unit.report.TestCaseReport>() {
      public void handle(io.vertx.ext.unit.report.TestCaseReport event) {
        handler.handle(TestCaseReport.newInstance(event));
      }
    });
    return this;
  }

  public TestSuiteReport pause() { 
    ((io.vertx.ext.unit.report.TestSuiteReport) delegate).pause();
    return this;
  }

  public TestSuiteReport resume() { 
    ((io.vertx.ext.unit.report.TestSuiteReport) delegate).resume();
    return this;
  }

  public TestSuiteReport endHandler(Handler<Void> endHandler) { 
    ((io.vertx.ext.unit.report.TestSuiteReport) delegate).endHandler(new Handler<java.lang.Void>() {
      public void handle(java.lang.Void event) {
        endHandler.handle(event);
      }
    });
    return this;
  }

  private String cached_0;

  public static TestSuiteReport newInstance(io.vertx.ext.unit.report.TestSuiteReport arg) {
    return arg != null ? new TestSuiteReport(arg) : null;
  }
}
