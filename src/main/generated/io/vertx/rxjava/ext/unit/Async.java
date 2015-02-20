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

/**
 * An asynchronous exit point for a test.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class Async {

  final io.vertx.ext.unit.Async delegate;

  public Async(io.vertx.ext.unit.Async delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Signals the asynchronous operation is done, this method should be called only once, calling it several
   * times is tolerated.
   *
   * @return true when called the first time, false otherwise.
   */
  public boolean complete() {
    boolean ret = this.delegate.complete();
    return ret;
  }


  public static Async newInstance(io.vertx.ext.unit.Async arg) {
    return new Async(arg);
  }
}
