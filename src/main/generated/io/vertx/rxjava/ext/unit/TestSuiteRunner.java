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
import io.vertx.rxjava.core.Vertx;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class TestSuiteRunner implements ReadStream<TestCaseRunner> {

  final io.vertx.ext.unit.TestSuiteRunner delegate;

  public TestSuiteRunner(io.vertx.ext.unit.TestSuiteRunner delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  private rx.Observable<TestCaseRunner> observable;

  public synchronized rx.Observable<TestCaseRunner> toObservable() {
    if (observable == null) {
      java.util.function.Function<io.vertx.ext.unit.TestCaseRunner, TestCaseRunner> conv = TestCaseRunner::newInstance;
      io.vertx.lang.rxjava.ReadStreamAdapter<io.vertx.ext.unit.TestCaseRunner, TestCaseRunner> adapter = new io.vertx.lang.rxjava.ReadStreamAdapter<>(this, conv);
      observable = rx.Observable.create(adapter);
    }
    return observable;
  }

  public ReadStream<TestCaseRunner> exceptionHandler(Handler<Throwable> arg0) {
    this.delegate.exceptionHandler(arg0);
    return this;
  }

  public ReadStream<TestCaseRunner> handler(Handler<TestCaseRunner> arg0) {
    this.delegate.handler(new Handler<io.vertx.ext.unit.TestCaseRunner>() {
      public void handle(io.vertx.ext.unit.TestCaseRunner event) {
        arg0.handle(new TestCaseRunner(event));
      }
    });
    return this;
  }

  public ReadStream<TestCaseRunner> pause() {
    this.delegate.pause();
    return this;
  }

  public ReadStream<TestCaseRunner> resume() {
    this.delegate.resume();
    return this;
  }

  public ReadStream<TestCaseRunner> endHandler(Handler<Void> arg0) {
    this.delegate.endHandler(arg0);
    return this;
  }

  /**
   * Run the suite in the current thread.
   */
  public void run() {
    this.delegate.run();
  }

  /**
   * Run the suite with the provided vertx.
   */
  public void run(Vertx vertx) {
    this.delegate.run((io.vertx.core.Vertx) vertx.getDelegate());
  }


  public static TestSuiteRunner newInstance(io.vertx.ext.unit.TestSuiteRunner arg) {
    return new TestSuiteRunner(arg);
  }
}
