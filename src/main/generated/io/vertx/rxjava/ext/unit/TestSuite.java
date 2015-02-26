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
import io.vertx.ext.unit.TestOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.core.Handler;

/**
 * A named suite of test cases that are executed altogether. The suite suite is created with
 * the {@link #create(String)} and the returned suite contains initially no tests.<p/>
 *
 * The suite can declare a callback before the suite with {@link #before(io.vertx.core.Handler)} or after
 * the suite with {@link #after(io.vertx.core.Handler)}.<p/>
 *
 * The suite can declare a callback before each test with {@link #beforeEach(io.vertx.core.Handler)} or after
 * each test with {@link #afterEach(io.vertx.core.Handler)}.<p/>
 *
 * Each test case of the suite is declared by calling the {@link #test(String, io.vertx.core.Handler)} method.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class TestSuite {

  final io.vertx.ext.unit.TestSuite delegate;

  public TestSuite(io.vertx.ext.unit.TestSuite delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Create and return a new test suite.
   *
   * @param name the test suite name
   * @return the created test suite
   */
  public static TestSuite create(String name) {
    TestSuite ret= TestSuite.newInstance(io.vertx.ext.unit.TestSuite.create(name));
    return ret;
  }

  /**
   * Set a callback executed before the tests.
   *
   * @param callback the callback
   * @return a reference to this, so the API can be used fluently
   */
  public TestSuite before(Handler<Test> callback) {
    this.delegate.before(new Handler<io.vertx.ext.unit.Test>() {
      public void handle(io.vertx.ext.unit.Test event) {
        callback.handle(new Test(event));
      }
    });
    return this;
  }

  /**
   * Set a callback executed before each test and after the suite {@code before} callback.
   *
   * @param callback the callback
   * @return a reference to this, so the API can be used fluently
   */
  public TestSuite beforeEach(Handler<Test> callback) {
    this.delegate.beforeEach(new Handler<io.vertx.ext.unit.Test>() {
      public void handle(io.vertx.ext.unit.Test event) {
        callback.handle(new Test(event));
      }
    });
    return this;
  }

  /**
   * Set a callback executed after the tests.
   *
   * @param callback the callback
   * @return a reference to this, so the API can be used fluently
   */
  public TestSuite after(Handler<Test> callback) {
    this.delegate.after(new Handler<io.vertx.ext.unit.Test>() {
      public void handle(io.vertx.ext.unit.Test event) {
        callback.handle(new Test(event));
      }
    });
    return this;
  }

  /**
   * Set a callback executed after each test and before the suite {@code after} callback.
   *
   * @param callback the callback
   * @return a reference to this, so the API can be used fluently
   */
  public TestSuite afterEach(Handler<Test> callback) {
    this.delegate.afterEach(new Handler<io.vertx.ext.unit.Test>() {
      public void handle(io.vertx.ext.unit.Test event) {
        callback.handle(new Test(event));
      }
    });
    return this;
  }

  /**
   * Add a new test case to the suite.
   *
   * @param name the test case name
   * @param testCase the test case
   * @return a reference to this, so the API can be used fluently
   */
  public TestSuite test(String name, Handler<Test> testCase) {
    this.delegate.test(name, new Handler<io.vertx.ext.unit.Test>() {
      public void handle(io.vertx.ext.unit.Test event) {
        testCase.handle(new Test(event));
      }
    });
    return this;
  }

  /**
   * Run the testsuite with the default options.<p/>
   *
   * When the test suite is executed in a Vertx context (i.e `Vertx.currentContext()` returns a context) this
   * context's event loop is used for running the test suite. Otherwise it is executed in the current thread.<p/>
   *
   * The returned {@link io.vertx.ext.unit.TestCompletion} object can be used to get a completion callback.
   *
   * @return the related test completion
   */
  public TestCompletion run() {
    TestCompletion ret= TestCompletion.newInstance(this.delegate.run());
    return ret;
  }

  /**
   * Run the testsuite with the specified {@code options}.<p/>
   *
   * When the test suite is executed in a Vertx context (i.e `Vertx.currentContext()` returns a context) this
   * context's event loop is used for running the test suite unless the {@link io.vertx.ext.unit.TestOptions#setUseEventLoop(Boolean)}
   * is set to {@code false}. In this case it is executed by the current thread.<p/>
   *
   * Otherwise, the test suite will be executed in the current thread when {@link io.vertx.ext.unit.TestOptions#setUseEventLoop(Boolean)} is
   * set to {@code false} or {@code null}. If the value is {@code true}, this methods throws an {@code IllegalStateException}.<p/>
   *
   * The returned {@link io.vertx.ext.unit.TestCompletion} object can be used to get a completion callback.
   *
   * @param options the test options
   * @return the related test completion
   */
  public TestCompletion run(TestOptions options) {
    TestCompletion ret= TestCompletion.newInstance(this.delegate.run(options));
    return ret;
  }

  /**
   * Run the testsuite with the default options and the specified {@code vertx} instance.<p/>
   *
   * The test suite will be executed on the event loop provided by the {@code vertx} argument. The returned
   * {@link io.vertx.ext.unit.TestCompletion} object can be used to get a completion callback.<p/>
   *
   * @param vertx the vertx instance
   * @return the related test completion
   */
  public TestCompletion run(Vertx vertx) {
    TestCompletion ret= TestCompletion.newInstance(this.delegate.run((io.vertx.core.Vertx) vertx.getDelegate()));
    return ret;
  }

  /**
   * Run the testsuite with the specified {@code options} and the specified {@code vertx} instance.<p/>
   *
   * The test suite will be executed on the event loop provided by the {@code vertx} argument when
   * {@link io.vertx.ext.unit.TestOptions#setUseEventLoop(Boolean)} is not set to {@code false}. The returned
   * {@link io.vertx.ext.unit.TestCompletion} object can be used to get a completion callback.
   *
   * @param vertx the vertx instance
   * @param options the test options
   * @return the related test completion
   */
  public TestCompletion run(Vertx vertx, TestOptions options) {
    TestCompletion ret= TestCompletion.newInstance(this.delegate.run((io.vertx.core.Vertx) vertx.getDelegate(), options));
    return ret;
  }


  public static TestSuite newInstance(io.vertx.ext.unit.TestSuite arg) {
    return new TestSuite(arg);
  }
}
