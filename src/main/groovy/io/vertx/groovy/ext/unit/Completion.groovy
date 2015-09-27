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
 * A completion object that emits completion notifications either <i>succeeded</i> or <i>failed</i>.
*/
@CompileStatic
public class Completion<T> {
  private final def io.vertx.ext.unit.Completion delegate;
  public Completion(Object delegate) {
    this.delegate = (io.vertx.ext.unit.Completion) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Completes the future upon completion, otherwise fails it.
   * @param future the future to resolve
   */
  public void resolve(Future<T> future) {
    ((io.vertx.ext.unit.Completion) this.delegate).resolve((io.vertx.core.Future<T>)future.getDelegate());
  }
  /**
   * @return true if this completion is completed
   * @return 
   */
  public boolean isCompleted() {
    def ret = ((io.vertx.ext.unit.Completion) this.delegate).isCompleted();
    return ret;
  }
  /**
   * @return true if the this completion is completed succeeded
   * @return 
   */
  public boolean isSucceeded() {
    def ret = ((io.vertx.ext.unit.Completion) this.delegate).isSucceeded();
    return ret;
  }
  /**
   * @return true if the this completion is completed and failed
   * @return 
   */
  public boolean isFailed() {
    def ret = ((io.vertx.ext.unit.Completion) this.delegate).isFailed();
    return ret;
  }
  /**
   * Completion handler to receive a completion signal when this completions completes.
   * @param completionHandler the completion handler
   */
  public void handler(Handler<AsyncResult<T>> completionHandler) {
    ((io.vertx.ext.unit.Completion) this.delegate).handler(new Handler<AsyncResult<Object>>() {
      public void handle(AsyncResult<Object> event) {
        AsyncResult<Object> f
        if (event.succeeded()) {
          f = InternalHelper.<Object>result(InternalHelper.wrapObject(event.result()))
        } else {
          f = InternalHelper.<Object>failure(event.cause())
        }
        completionHandler.handle(f)
      }
    });
  }
  /**
   * Cause the current thread to wait until thi completion completes.<p/>
   *
   * If the current thread is interrupted, an exception will be thrown.
   */
  public void await() {
    ((io.vertx.ext.unit.Completion) this.delegate).await();
  }
  /**
   * Cause the current thread to wait until this completion completes with a configurable timeout.<p/>
   *
   * If completion times out or the current thread is interrupted, an exception will be thrown.
   * @param timeoutMillis the timeout in milliseconds
   */
  public void await(long timeoutMillis) {
    ((io.vertx.ext.unit.Completion) this.delegate).await(timeoutMillis);
  }
  /**
   * Cause the current thread to wait until this completion completes and succeeds.<p/>
   *
   * If the current thread is interrupted or the suite fails, an exception will be thrown.
   */
  public void awaitSuccess() {
    ((io.vertx.ext.unit.Completion) this.delegate).awaitSuccess();
  }
  /**
   * Cause the current thread to wait until this completion completes and succeeds with a configurable timeout.<p/>
   *
   * If completion times out or the current thread is interrupted or the suite fails, an exception will be thrown.
   * @param timeoutMillis the timeout in milliseconds
   */
  public void awaitSuccess(long timeoutMillis) {
    ((io.vertx.ext.unit.Completion) this.delegate).awaitSuccess(timeoutMillis);
  }
}
