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
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.Future;

/**
 * This object provides callback-ability for the end of a test suite.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.unit.TestCompletion original} non RX-ified interface using Vert.x codegen.
 */

public class TestCompletion {

  final io.vertx.ext.unit.TestCompletion delegate;

  public TestCompletion(io.vertx.ext.unit.TestCompletion delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Completes the future when all test cases of the test suite passes, otherwise fails it.
   * @param future the future to resolve
   */
  public void resolve(Future future) { 
    this.delegate.resolve((io.vertx.core.Future) future.getDelegate());
  }

  /**
   * @return true if the test suite completed
   * @return 
   */
  public boolean isCompleted() { 
    boolean ret = this.delegate.isCompleted();
    return ret;
  }

  /**
   * @return true if the test suite completed and succeeded
   * @return 
   */
  public boolean isSucceeded() { 
    boolean ret = this.delegate.isSucceeded();
    return ret;
  }

  /**
   * @return true if the test suite completed and failed
   * @return 
   */
  public boolean isFailed() { 
    boolean ret = this.delegate.isFailed();
    return ret;
  }

  /**
   * Completion handler for the end of the test, the result is successful when all test cases pass otherwise
   * it is failed.
   * @param completionHandler the completion handler
   */
  public void handler(Handler<AsyncResult<Void>> completionHandler) { 
    this.delegate.handler(completionHandler);
  }

  /**
   * Completion handler for the end of the test, the result is successful when all test cases pass otherwise
   * it is failed.
   * @return 
   */
  public Observable<Void> handlerObservable() { 
    io.vertx.rx.java.ObservableFuture<Void> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    handler(completionHandler.toHandler());
    return completionHandler;
  }

  /**
   * Cause the current thread to wait until the test suite completes.<p/>
   *
   * If the current thread is interrupted, an exception will be thrown.
   */
  public void await() { 
    this.delegate.await();
  }

  /**
   * Cause the current thread to wait until the test suite completes with a configurable timeout.<p/>
   *
   * If completion times out or the current thread is interrupted, an exception will be thrown.
   * @param timeoutMillis the timeout in milliseconds
   */
  public void await(long timeoutMillis) { 
    this.delegate.await(timeoutMillis);
  }

  /**
   * Cause the current thread to wait until the test suite completes and succeeds.<p/>
   *
   * If the current thread is interrupted or the suite fails, an exception will be thrown.
   */
  public void awaitSuccess() { 
    this.delegate.awaitSuccess();
  }

  /**
   * Cause the current thread to wait until the test suite completes and succeeds with a configurable timeout.<p/>
   *
   * If completion times out or the current thread is interrupted or the suite fails, an exception will be thrown.
   * @param timeoutMillis the timeout in milliseconds
   */
  public void awaitSuccess(long timeoutMillis) { 
    this.delegate.awaitSuccess(timeoutMillis);
  }


  public static TestCompletion newInstance(io.vertx.ext.unit.TestCompletion arg) {
    return new TestCompletion(arg);
  }
}
