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
/**
 * A failure provides the details of a failure that happened during the execution of a test case.<p/>
 *
 * The failure can be:
 * <ul>
 *   <li>an assertion failure: an assertion failed</li>
 *   <li>an error failure: an expected error occured</li>
 * </ul>
*/
@CompileStatic
public class Failure {
  final def io.vertx.ext.unit.report.Failure delegate;
  public Failure(io.vertx.ext.unit.report.Failure delegate) {
    this.delegate = delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * @return true if the failure is an error failure otherwise it is an assertion failure
   * @return 
   */
  public boolean isError() {
    if (cached_0 != null) {
      return cached_0;
    }
    def ret = this.delegate.isError();
    cached_0 = ret;
    return ret;
  }
  /**
   * @return the error message
   * @return 
   */
  public String message() {
    if (cached_1 != null) {
      return cached_1;
    }
    def ret = this.delegate.message();
    cached_1 = ret;
    return ret;
  }
  /**
   * @return the stack trace
   * @return 
   */
  public String stackTrace() {
    if (cached_2 != null) {
      return cached_2;
    }
    def ret = this.delegate.stackTrace();
    cached_2 = ret;
    return ret;
  }
  private boolean cached_0;
  private java.lang.String cached_1;
  private java.lang.String cached_2;
}
