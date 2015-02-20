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
import io.vertx.rxjava.core.Vertx;
import io.vertx.core.Handler;

/**
 * The test suite runner.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class TestSuiteRunner {

  final io.vertx.ext.unit.TestSuiteRunner delegate;

  public TestSuiteRunner(io.vertx.ext.unit.TestSuiteRunner delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * @return the current runner vertx instance
   */
  public Vertx getVertx() {
    Vertx ret= Vertx.newInstance(this.delegate.getVertx());
    return ret;
  }

  /**
   * Set a vertx instance of the runner.
   *
   * @param vertx the vertx instance
   * @return a reference to this, so the API can be used fluently
   */
  public TestSuiteRunner setVertx(Vertx vertx) {
    this.delegate.setVertx((io.vertx.core.Vertx) vertx.getDelegate());
    return this;
  }

  /**
   * @return the current runner timeout
   */
  public long getTimeout() {
    long ret = this.delegate.getTimeout();
    return ret;
  }

  /**
   * Set a timeout on the runner, zero or a negative value means no timeout.
   *
   * @param timeout the timeout in millis
   * @return a reference to this, so the API can be used fluently
   */
  public TestSuiteRunner setTimeout(long timeout) {
    this.delegate.setTimeout(timeout);
    return this;
  }

  /**
   * Set a reporter for handling the events emitted by the test suite.
   *
   * @param reporter the reporter
   * @return a reference to this, so the API can be used fluently
   */
  public TestSuiteRunner handler(Handler<TestSuiteReport> reporter) {
    this.delegate.handler(new Handler<io.vertx.ext.unit.report.TestSuiteReport>() {
      public void handle(io.vertx.ext.unit.report.TestSuiteReport event) {
        reporter.handle(new TestSuiteReport(event));
      }
    });
    return this;
  }

  /**
   * Run the testsuite with the current {@code timeout}, {@code vertx} and {@code reporter}.
   */
  public void run() {
    this.delegate.run();
  }


  public static TestSuiteRunner newInstance(io.vertx.ext.unit.TestSuiteRunner arg) {
    return new TestSuiteRunner(arg);
  }
}
