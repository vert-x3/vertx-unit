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

  public TestSuite before(Handler<Test> before) {
    this.delegate.before(new Handler<io.vertx.ext.unit.Test>() {
      public void handle(io.vertx.ext.unit.Test event) {
        before.handle(new Test(event));
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

  public TestSuite test(String name, Handler<Test> handler) {
    this.delegate.test(name, new Handler<io.vertx.ext.unit.Test>() {
      public void handle(io.vertx.ext.unit.Test event) {
        handler.handle(new Test(event));
      }
    });
    return this;
  }

  public void run() {
    this.delegate.run();
  }

  public void run(Vertx vertx) {
    this.delegate.run((io.vertx.core.Vertx) vertx.getDelegate());
  }

  public void run(Handler<TestSuiteReport> reporter) {
    this.delegate.run(new Handler<io.vertx.ext.unit.TestSuiteReport>() {
      public void handle(io.vertx.ext.unit.TestSuiteReport event) {
        reporter.handle(new TestSuiteReport(event));
      }
    });
  }

  public void run(Vertx vertx, Handler<TestSuiteReport> reporter) {
    this.delegate.run((io.vertx.core.Vertx) vertx.getDelegate(), new Handler<io.vertx.ext.unit.TestSuiteReport>() {
      public void handle(io.vertx.ext.unit.TestSuiteReport event) {
        reporter.handle(new TestSuiteReport(event));
      }
    });
  }

  public TestSuiteRunner runner(Vertx vertx) {
    TestSuiteRunner ret= TestSuiteRunner.newInstance(this.delegate.runner((io.vertx.core.Vertx) vertx.getDelegate()));
    return ret;
  }

  public TestSuiteRunner runner() {
    TestSuiteRunner ret= TestSuiteRunner.newInstance(this.delegate.runner());
    return ret;
  }


  public static TestSuite newInstance(io.vertx.ext.unit.TestSuite arg) {
    return new TestSuite(arg);
  }
}
