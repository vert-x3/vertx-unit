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

package io.vertx.groovy.ext.unit.report;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.groovy.core.streams.ReadStream
import io.vertx.core.Handler
/**
 * The test suite reports is basically a stream of events reporting the test suite execution.
*/
@CompileStatic
public class TestSuiteReport implements ReadStream<TestCaseReport> {
  private final def io.vertx.ext.unit.report.TestSuiteReport delegate;
  public TestSuiteReport(Object delegate) {
    this.delegate = (io.vertx.ext.unit.report.TestSuiteReport) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * @return the test suite name
   */
  public String name() {
    if (cached_0 != null) {
      return cached_0;
    }
    def ret = delegate.name();
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
    ((io.vertx.ext.unit.report.TestSuiteReport) delegate).handler(handler != null ? new Handler<io.vertx.ext.unit.report.TestCaseReport>(){
      public void handle(io.vertx.ext.unit.report.TestCaseReport event) {
        handler.handle(InternalHelper.safeCreate(event, io.vertx.groovy.ext.unit.report.TestCaseReport.class));
      }
    } : null);
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
    ((io.vertx.ext.unit.report.TestSuiteReport) delegate).endHandler(endHandler);
    return this;
  }
  private String cached_0;
}
