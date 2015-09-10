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
import io.vertx.core.Handler
/**
 * Report the execution of a test case.
*/
@CompileStatic
public class TestCaseReport {
  private final def io.vertx.ext.unit.report.TestCaseReport delegate;
  public TestCaseReport(Object delegate) {
    this.delegate = (io.vertx.ext.unit.report.TestCaseReport) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * @return the test case name
   * @return 
   */
  public String name() {
    if (cached_0 != null) {
      return cached_0;
    }
    def ret = this.delegate.name();
    cached_0 = ret;
    return ret;
  }
  /**
   * Set a callback for completion, the specified <code>handler</code> is invoked when the test exec has completed.
   * @param handler the completion handler
   * @return a reference to this, so the API can be used fluently
   */
  public TestCaseReport endHandler(Handler<TestResult> handler) {
    this.delegate.endHandler(new Handler<io.vertx.ext.unit.report.TestResult>() {
      public void handle(io.vertx.ext.unit.report.TestResult event) {
        handler.handle(new io.vertx.groovy.ext.unit.report.TestResult(event));
      }
    });
    return this;
  }
  private String cached_0;
}
