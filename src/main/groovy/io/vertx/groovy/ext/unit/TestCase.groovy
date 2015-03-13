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

package io.vertx.groovy.ext.unit;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.Handler
/**
 * A test case object can be used to create a single test.
*/
@CompileStatic
public class TestCase {
  final def io.vertx.ext.unit.TestCase delegate;
  public TestCase(io.vertx.ext.unit.TestCase delegate) {
    this.delegate = delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Create a test case.
   * @param name the test case name
   * @param testCase the test case
   * @return the created test case
   */
  public static TestCase create(String name, Handler<TestContext> testCase) {
    def ret= new io.vertx.groovy.ext.unit.TestCase(io.vertx.ext.unit.TestCase.create(name, new Handler<io.vertx.ext.unit.TestContext>() {
      public void handle(io.vertx.ext.unit.TestContext event) {
        testCase.handle(new io.vertx.groovy.ext.unit.TestContext(event));
      }
    }));
    return ret;
  }
}
