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
import io.vertx.core.Handler;

/**
 * A test case object can be used to create a single test.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class TestCase {

  final io.vertx.ext.unit.TestCase delegate;

  public TestCase(io.vertx.ext.unit.TestCase delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Create a test case.
   *
   * @param name the test case name
   * @param testCase the test case
   * @return the created test case
   */
  public static TestCase create(String name, Handler<TestContext> testCase) {
    TestCase ret= TestCase.newInstance(io.vertx.ext.unit.TestCase.create(name, new Handler<io.vertx.ext.unit.TestContext>() {
      public void handle(io.vertx.ext.unit.TestContext event) {
        testCase.handle(new TestContext(event));
      }
    }));
    return ret;
  }


  public static TestCase newInstance(io.vertx.ext.unit.TestCase arg) {
    return new TestCase(arg);
  }
}
