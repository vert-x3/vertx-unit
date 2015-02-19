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

  public static TestSuite create(String name) {
    TestSuite ret= TestSuite.newInstance(io.vertx.ext.unit.TestSuite.create(name));
    return ret;
  }

  public TestSuite before(Handler<Test> handler) {
    this.delegate.before(new Handler<io.vertx.ext.unit.Test>() {
      public void handle(io.vertx.ext.unit.Test event) {
        handler.handle(new Test(event));
      }
    });
    return this;
  }

  public TestSuite beforeEach(Handler<Test> handler) {
    this.delegate.beforeEach(new Handler<io.vertx.ext.unit.Test>() {
      public void handle(io.vertx.ext.unit.Test event) {
        handler.handle(new Test(event));
      }
    });
    return this;
  }

  public TestSuite after(Handler<Test> callback) {
    this.delegate.after(new Handler<io.vertx.ext.unit.Test>() {
      public void handle(io.vertx.ext.unit.Test event) {
        callback.handle(new Test(event));
      }
    });
    return this;
  }

  public TestSuite afterEach(Handler<Test> callback) {
    this.delegate.afterEach(new Handler<io.vertx.ext.unit.Test>() {
      public void handle(io.vertx.ext.unit.Test event) {
        callback.handle(new Test(event));
      }
    });
    return this;
  }

  public TestSuite test(String name, Handler<Test> handler) {
    this.delegate.test(name, new Handler<io.vertx.ext.unit.Test>() {
      public void handle(io.vertx.ext.unit.Test event) {
        handler.handle(new Test(event));
      }
    });
    return this;
  }

  public TestCompletion run() {
    TestCompletion ret= TestCompletion.newInstance(this.delegate.run());
    return ret;
  }

  public TestCompletion run(Vertx vertx) {
    TestCompletion ret= TestCompletion.newInstance(this.delegate.run((io.vertx.core.Vertx) vertx.getDelegate()));
    return ret;
  }

  public TestCompletion run(TestOptions options) {
    TestCompletion ret= TestCompletion.newInstance(this.delegate.run(options));
    return ret;
  }

  public TestCompletion run(Vertx vertx, TestOptions options) {
    TestCompletion ret= TestCompletion.newInstance(this.delegate.run((io.vertx.core.Vertx) vertx.getDelegate(), options));
    return ret;
  }

  public TestSuiteRunner runner() {
    TestSuiteRunner ret= TestSuiteRunner.newInstance(this.delegate.runner());
    return ret;
  }

  public TestSuiteRunner runner(Vertx vertx) {
    TestSuiteRunner ret= TestSuiteRunner.newInstance(this.delegate.runner((io.vertx.core.Vertx) vertx.getDelegate()));
    return ret;
  }


  public static TestSuite newInstance(io.vertx.ext.unit.TestSuite arg) {
    return new TestSuite(arg);
  }
}
