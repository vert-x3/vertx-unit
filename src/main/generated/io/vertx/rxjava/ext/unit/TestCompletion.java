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
import rx.Observable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.Future;

/**
 * This object provides callback-ability for the end of a test suite, the completion <i>succeeds</i>
 * when all tests pass otherwise it fails.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.unit.TestCompletion original} non RX-ified interface using Vert.x codegen.
 */

public class TestCompletion extends Completion<Void> {

  final io.vertx.ext.unit.TestCompletion delegate;

  public TestCompletion(io.vertx.ext.unit.TestCompletion delegate) {
    super(delegate);
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Completes the future upon completion, otherwise fails it.
   * @param future the future to resolve
   */
  public void resolve(Future<Void> future) { 
    delegate.resolve((io.vertx.core.Future<java.lang.Void>)future.getDelegate());
  }

  /**
   * Completion handler to receive a completion signal when this completions completes.
   * @param completionHandler the completion handler
   */
  public void handler(Handler<AsyncResult<Void>> completionHandler) { 
    delegate.handler(completionHandler);
  }

  /**
   * Completion handler to receive a completion signal when this completions completes.
   * @return 
   */
  public Observable<Void> handlerObservable() { 
    io.vertx.rx.java.ObservableFuture<Void> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    handler(completionHandler.toHandler());
    return completionHandler;
  }


  public static TestCompletion newInstance(io.vertx.ext.unit.TestCompletion arg) {
    return arg != null ? new TestCompletion(arg) : null;
  }
}
