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
/**
 * An asynchronous exit point for a test.
*/
@CompileStatic
public class Async {
  private final def io.vertx.ext.unit.Async delegate;
  public Async(Object delegate) {
    this.delegate = (io.vertx.ext.unit.Async) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Signals the asynchronous operation is done, this method should be called only once, if the method is called
   * another time it will throw an <code>IllegalStateException</code> to signal the error.
   */
  public void complete() {
    this.delegate.complete();
  }
}
