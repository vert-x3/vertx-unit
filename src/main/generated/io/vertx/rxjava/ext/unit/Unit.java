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
 * The factory for creating module or individual tests.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class Unit {

  final io.vertx.ext.unit.Unit delegate;

  public Unit(io.vertx.ext.unit.Unit delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static TestSuite suite() {
    TestSuite ret= TestSuite.newInstance(io.vertx.ext.unit.Unit.suite());
    return ret;
  }

  public static TestSuite suite(String desc) {
    TestSuite ret= TestSuite.newInstance(io.vertx.ext.unit.Unit.suite(desc));
    return ret;
  }

  public static TestCase test(String desc, Handler<Test> handler) {
    TestCase ret= TestCase.newInstance(io.vertx.ext.unit.Unit.test(desc, new Handler<io.vertx.ext.unit.Test>() {
      public void handle(io.vertx.ext.unit.Test event) {
        handler.handle(new Test(event));
      }
    }));
    return ret;
  }


  public static Unit newInstance(io.vertx.ext.unit.Unit arg) {
    return new Unit(arg);
  }
}
