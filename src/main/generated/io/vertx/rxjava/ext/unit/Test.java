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

/**
 * The test interface allows the test code to report test completion or failures.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class Test {

  final io.vertx.ext.unit.Test delegate;

  public Test(io.vertx.ext.unit.Test delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public Vertx vertx() {
    Vertx ret= Vertx.newInstance(this.delegate.vertx());
    return ret;
  }

  public Async async() {
    Async ret= Async.newInstance(this.delegate.async());
    return ret;
  }

  public void assertTrue(boolean b) {
    this.delegate.assertTrue(b);
  }

  public void fail(String s) {
    this.delegate.fail(s);
  }


  public static Test newInstance(io.vertx.ext.unit.Test arg) {
    return new Test(arg);
  }
}
