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
import io.vertx.rxjava.core.streams.ReadStream;
import io.vertx.core.Handler;

/**
 * The test suite reports is basically a stream of events reporting the test suite execution.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class TestSuiteReport implements ReadStream<TestCaseReport> {

  final io.vertx.ext.unit.TestSuiteReport delegate;

  public TestSuiteReport(io.vertx.ext.unit.TestSuiteReport delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  private rx.Observable<TestCaseReport> observable;

  public synchronized rx.Observable<TestCaseReport> toObservable() {
    if (observable == null) {
      java.util.function.Function<io.vertx.ext.unit.TestCaseReport, TestCaseReport> conv = TestCaseReport::newInstance;
      io.vertx.lang.rxjava.ReadStreamAdapter<io.vertx.ext.unit.TestCaseReport, TestCaseReport> adapter = new io.vertx.lang.rxjava.ReadStreamAdapter<>(this, conv);
      observable = rx.Observable.create(adapter);
    }
    return observable;
  }

  public ReadStream<TestCaseReport> exceptionHandler(Handler<Throwable> arg0) {
    this.delegate.exceptionHandler(arg0);
    return this;
  }

  public ReadStream<TestCaseReport> handler(Handler<TestCaseReport> arg0) {
    this.delegate.handler(new Handler<io.vertx.ext.unit.TestCaseReport>() {
      public void handle(io.vertx.ext.unit.TestCaseReport event) {
        arg0.handle(new TestCaseReport(event));
      }
    });
    return this;
  }

  public ReadStream<TestCaseReport> pause() {
    this.delegate.pause();
    return this;
  }

  public ReadStream<TestCaseReport> resume() {
    this.delegate.resume();
    return this;
  }

  public ReadStream<TestCaseReport> endHandler(Handler<Void> arg0) {
    this.delegate.endHandler(arg0);
    return this;
  }

  /**
   * @return the test suite name
   */
  public String name() {
    if (cached_0 != null) {
      return cached_0;
    }
    String ret = this.delegate.name();
    cached_0 = ret;
    return ret;
  }

  private java.lang.String cached_0;

  public static TestSuiteReport newInstance(io.vertx.ext.unit.TestSuiteReport arg) {
    return new TestSuiteReport(arg);
  }
}
