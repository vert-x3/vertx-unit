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
 * The result of a test.
*/
@CompileStatic
public class TestResult {
  final def io.vertx.ext.unit.report.TestResult delegate;
  public TestResult(io.vertx.ext.unit.report.TestResult delegate) {
    this.delegate = delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * The test description, may be null if none was provided.
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
   * The time at which the test began in millis.
   * @return 
   */
  public long beginTime() {
    if (cached_1 != null) {
      return cached_1;
    }
    def ret = this.delegate.beginTime();
    cached_1 = ret;
    return ret;
  }
  /**
   * How long the test lasted in millis.
   * @return 
   */
  public long durationTime() {
    if (cached_2 != null) {
      return cached_2;
    }
    def ret = this.delegate.durationTime();
    cached_2 = ret;
    return ret;
  }
  /**
   * Did it succeed?
   * @return 
   */
  public boolean succeeded() {
    if (cached_3 != null) {
      return cached_3;
    }
    def ret = this.delegate.succeeded();
    cached_3 = ret;
    return ret;
  }
  /**
   * Did it fail?
   * @return 
   */
  public boolean failed() {
    if (cached_4 != null) {
      return cached_4;
    }
    def ret = this.delegate.failed();
    cached_4 = ret;
    return ret;
  }
  /**
   * An exception describing failure, null if the test succeeded.
   * @return 
   */
  public Failure failure() {
    if (cached_5 != null) {
      return cached_5;
    }
    def ret= InternalHelper.safeCreate(this.delegate.failure(), io.vertx.ext.unit.report.Failure.class, io.vertx.groovy.ext.unit.report.Failure.class);
    cached_5 = ret;
    return ret;
  }
  private java.lang.String cached_0;
  private long cached_1;
  private long cached_2;
  private boolean cached_3;
  private boolean cached_4;
  private Failure cached_5;
}
