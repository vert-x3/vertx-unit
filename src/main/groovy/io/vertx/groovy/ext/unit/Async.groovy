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
import io.vertx.core.json.JsonObject
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.groovy.core.Future
/**
 * An asynchronous exit point for a test.<p/>
*/
@CompileStatic
public class Async extends Completion<Void> {
  private final def io.vertx.ext.unit.Async delegate;
  public Async(Object delegate) {
    super((io.vertx.ext.unit.Async) delegate);
    this.delegate = (io.vertx.ext.unit.Async) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Completes the future upon completion, otherwise fails it.
   * @param future the future to resolve
   */
  public void resolve(Future<Void> future) {
    ((io.vertx.ext.unit.Completion) delegate).resolve(future != null ? (io.vertx.core.Future<java.lang.Void>)future.getDelegate() : null);
  }
  /**
   * Completion handler to receive a completion signal when this completions completes.
   * @param completionHandler the completion handler
   */
  public void handler(Handler<AsyncResult<Void>> completionHandler) {
    ((io.vertx.ext.unit.Completion) delegate).handler(completionHandler);
  }
  /**
   * @return the current count
   */
  public int count() {
    def ret = delegate.count();
    return ret;
  }
  /**
   * Count down the async.
   */
  public void countDown() {
    delegate.countDown();
  }
  /**
   * Signals the asynchronous operation is done, this method must be called with a count greater than <code>0</code>,
   * otherwise it throw an <code>IllegalStateException</code> to signal the error.
   */
  public void complete() {
    delegate.complete();
  }
}
