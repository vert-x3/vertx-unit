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
/**
 * The test context is used for performing test assertions and manage the completion of the test. This context
 * is provided by <i>vertx-unit</i> as argument of the test case.
*/
@CompileStatic
public class TestContext {
  private final def io.vertx.ext.unit.TestContext delegate;
  public TestContext(Object delegate) {
    this.delegate = (io.vertx.ext.unit.TestContext) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Get some data from the context.
   * @param key the key of the data
   * @return the data
   */
  public <T> T get(String key) {
    def ret = (T) InternalHelper.wrapObject(delegate.get(key));
    return ret;
  }
  /**
   * Put some data in the context.
   * <p>
   * This can be used to share data between different tests and before/after phases.
   * @param key the key of the data
   * @param value the data
   * @return the previous object when it exists
   */
  public <T> T put(String key, Object value) {
    def ret = (T) InternalHelper.wrapObject(delegate.put(key, value != null ? InternalHelper.unwrapObject(value) : null));
    return ret;
  }
  /**
   * Remove some data from the context.
   * @param key the key to remove
   * @return the removed object when it exists
   */
  public <T> T remove(String key) {
    def ret = (T) InternalHelper.wrapObject(delegate.remove(key));
    return ret;
  }
  /**
   * Assert the <code>expected</code> argument is <code>null</code>. If the argument is not, an assertion error is thrown
   * otherwise the execution continue.
   * @param expected the argument being asserted to be null
   * @return a reference to this, so the API can be used fluently
   */
  public TestContext assertNull(Object expected) {
    delegate.assertNull(expected != null ? InternalHelper.unwrapObject(expected) : null);
    return this;
  }
  /**
   * Assert the <code>expected</code> argument is <code>null</code>. If the argument is not, an assertion error is thrown
   * otherwise the execution continue.
   * @param expected the argument being asserted to be null
   * @param message the failure message
   * @return a reference to this, so the API can be used fluently
   */
  public TestContext assertNull(Object expected, String message) {
    delegate.assertNull(expected != null ? InternalHelper.unwrapObject(expected) : null, message);
    return this;
  }
  /**
   * Assert the <code>expected</code> argument is not <code>null</code>. If the argument is <code>null</code>, an assertion error is thrown
   * otherwise the execution continue.
   * @param expected the argument being asserted to be not null
   * @return a reference to this, so the API can be used fluently
   */
  public TestContext assertNotNull(Object expected) {
    delegate.assertNotNull(expected != null ? InternalHelper.unwrapObject(expected) : null);
    return this;
  }
  /**
   * Assert the <code>expected</code> argument is not <code>null</code>. If the argument is <code>null</code>, an assertion error is thrown
   * otherwise the execution continue.
   * @param expected the argument being asserted to be not null
   * @param message the failure message
   * @return a reference to this, so the API can be used fluently
   */
  public TestContext assertNotNull(Object expected, String message) {
    delegate.assertNotNull(expected != null ? InternalHelper.unwrapObject(expected) : null, message);
    return this;
  }
  /**
   * Assert the specified <code>condition</code> is <code>true</code>. If the condition is <code>false</code>, an assertion error is thrown
   * otherwise the execution continue.
   * @param condition the condition to assert
   * @return a reference to this, so the API can be used fluently
   */
  public TestContext assertTrue(boolean condition) {
    delegate.assertTrue(condition);
    return this;
  }
  /**
   * Assert the specified <code>condition</code> is <code>true</code>. If the condition is <code>false</code>, an assertion error is thrown
   * otherwise the execution continue.
   * @param condition the condition to assert
   * @param message the failure message
   * @return a reference to this, so the API can be used fluently
   */
  public TestContext assertTrue(boolean condition, String message) {
    delegate.assertTrue(condition, message);
    return this;
  }
  /**
   * Assert the specified <code>condition</code> is <code>false</code>. If the condition is <code>true</code>, an assertion error is thrown
   * otherwise the execution continue.
   * @param condition the condition to assert
   * @return a reference to this, so the API can be used fluently
   */
  public TestContext assertFalse(boolean condition) {
    delegate.assertFalse(condition);
    return this;
  }
  /**
   * Assert the specified <code>condition</code> is <code>false</code>. If the condition is <code>true</code>, an assertion error is thrown
   * otherwise the execution continue.
   * @param condition the condition to assert
   * @param message the failure message
   * @return a reference to this, so the API can be used fluently
   */
  public TestContext assertFalse(boolean condition, String message) {
    delegate.assertFalse(condition, message);
    return this;
  }
  /**
   * Assert the <code>expected</code> argument is equals to the <code>actual</code> argument. If the arguments are not equals
   * an assertion error is thrown otherwise the execution continue.
   * @param expected the object the actual object is supposedly equals to
   * @param actual the actual object to test
   * @return a reference to this, so the API can be used fluently
   */
  public TestContext assertEquals(Object expected, Object actual) {
    delegate.assertEquals(expected != null ? InternalHelper.unwrapObject(expected) : null, actual != null ? InternalHelper.unwrapObject(actual) : null);
    return this;
  }
  /**
   * Assert the <code>expected</code> argument is equals to the <code>actual</code> argument. If the arguments are not equals
   * an assertion error is thrown otherwise the execution continue.
   * @param expected the object the actual object is supposedly equals to
   * @param actual the actual object to test
   * @param message the failure message
   * @return a reference to this, so the API can be used fluently
   */
  public TestContext assertEquals(Object expected, Object actual, String message) {
    delegate.assertEquals(expected != null ? InternalHelper.unwrapObject(expected) : null, actual != null ? InternalHelper.unwrapObject(actual) : null, message);
    return this;
  }
  /**
   * Asserts that the <code>expected</code> double argument is equals to the <code>actual</code> double argument
   * within a positive delta. If the arguments do not satisfy this, an assertion error is thrown otherwise
   * the execution continue.
   * @param expected the object the actual object is supposedly equals to
   * @param actual the actual object to test
   * @param delta the maximum delta
   * @return a reference to this, so the API can be used fluently
   */
  public TestContext assertInRange(double expected, double actual, double delta) {
    delegate.assertInRange(expected, actual, delta);
    return this;
  }
  /**
   * Asserts that the <code>expected</code> double argument is equals to the <code>actual</code> double argument
   * within a positive delta. If the arguments do not satisfy this, an assertion error is thrown otherwise
   * the execution continue.
   * @param expected the object the actual object is supposedly equals to
   * @param actual the actual object to test
   * @param delta the maximum delta
   * @param message the failure message
   * @return a reference to this, so the API can be used fluently
   */
  public TestContext assertInRange(double expected, double actual, double delta, String message) {
    delegate.assertInRange(expected, actual, delta, message);
    return this;
  }
  /**
   * Assert the <code>first</code> argument is not equals to the <code>second</code> argument. If the arguments are equals
   * an assertion error is thrown otherwise the execution continue.
   * @param first the first object to test
   * @param second the second object to test
   * @return a reference to this, so the API can be used fluently
   */
  public TestContext assertNotEquals(Object first, Object second) {
    delegate.assertNotEquals(first != null ? InternalHelper.unwrapObject(first) : null, second != null ? InternalHelper.unwrapObject(second) : null);
    return this;
  }
  /**
   * Assert the <code>first</code> argument is not equals to the <code>second</code> argument. If the arguments are equals
   * an assertion error is thrown otherwise the execution continue.
   * @param first the first object to test
   * @param second the second object to test
   * @param message the failure message
   * @return a reference to this, so the API can be used fluently
   */
  public TestContext assertNotEquals(Object first, Object second, String message) {
    delegate.assertNotEquals(first != null ? InternalHelper.unwrapObject(first) : null, second != null ? InternalHelper.unwrapObject(second) : null, message);
    return this;
  }
  /**
   * Throw a failure.
   */
  public void fail() {
    delegate.fail();
  }
  /**
   * Throw a failure with the specified failure <code>message</code>.
   * @param message the failure message
   */
  public void fail(String message) {
    delegate.fail(message);
  }
  /**
   * Throw a failure with the specified failure <code>cause</code>.
   * @param cause the failure cause
   */
  public void fail(Throwable cause) {
    delegate.fail(cause);
  }
  /**
   * Create and returns a new async object, the returned async controls the completion of the test. Calling the
   * {@link io.vertx.groovy.ext.unit.Async#complete} completes the async operation.<p/>
   *
   * The test case will complete when all the async objects have their {@link io.vertx.groovy.ext.unit.Async#complete}
   * method called at least once.<p/>
   *
   * This method shall be used for creating asynchronous exit points for the executed test.
   * @return the async instance
   */
  public Async async() {
    def ret = InternalHelper.safeCreate(delegate.async(), io.vertx.groovy.ext.unit.Async.class);
    return ret;
  }
  /**
   * Create and returns a new async object, the returned async controls the completion of the test. This async operation
   * completes when the {@link io.vertx.groovy.ext.unit.Async#complete} is called <code>count</code> times.<p/>
   *
   * The test case will complete when all the async objects have their {@link io.vertx.groovy.ext.unit.Async#complete}
   * method called at least once.<p/>
   *
   * This method shall be used for creating asynchronous exit points for the executed test.<p/>
   * @param count 
   * @return the async instance
   */
  public Async async(int count) {
    def ret = InternalHelper.safeCreate(delegate.async(count), io.vertx.groovy.ext.unit.Async.class);
    return ret;
  }
  /**
   * Creates and returns a new async handler, the returned handler controls the completion of the test.<p/>
   *
   * When the returned handler is called back with a succeeded result it completes the async operation.<p/>
   *
   * When the returned handler is called back with a failed result it fails the test with the cause of the failure.<p/>
   * @return the async result handler
   */
  public <T> Handler<AsyncResult<T>> asyncAssertSuccess() {
    def ret = new Handler<AsyncResult<Object>>() {
      public void handle(AsyncResult<Object> ar_) {
        if (ar_.succeeded()) {
          delegate.asyncAssertSuccess().handle(io.vertx.core.Future.succeededFuture(InternalHelper.unwrapObject(ar_.result())));
        } else  {
          delegate.asyncAssertSuccess().handle(io.vertx.core.Future.failedFuture(ar_.cause()));
        }
      }
    };
    return ret;
  }
  /**
   * Creates and returns a new async handler, the returned handler controls the completion of the test.<p/>
   *
   * When the returned handler is called back with a succeeded result it invokes the <code>resultHandler</code> argument
   * with the async result. The test completes after the result handler is invoked and does not fails.<p/>
   *
   * When the returned handler is called back with a failed result it fails the test with the cause of the failure.<p/>
   *
   * Note that the result handler can create other async objects during its invocation that would postpone
   * the completion of the test case until those objects are resolved.
   * @param resultHandler the result handler
   * @return the async result handler
   */
  public <T> Handler<AsyncResult<T>> asyncAssertSuccess(Handler<T> resultHandler) {
    def ret = new Handler<AsyncResult<Object>>() {
      public void handle(AsyncResult<Object> ar_) {
        if (ar_.succeeded()) {
          delegate.asyncAssertSuccess(resultHandler != null ? new Handler<java.lang.Object>(){
      public void handle(java.lang.Object event) {
        resultHandler.handle((Object) InternalHelper.wrapObject(event));
      }
    } : null).handle(io.vertx.core.Future.succeededFuture(InternalHelper.unwrapObject(ar_.result())));
        } else  {
          delegate.asyncAssertSuccess(resultHandler != null ? new Handler<java.lang.Object>(){
      public void handle(java.lang.Object event) {
        resultHandler.handle((Object) InternalHelper.wrapObject(event));
      }
    } : null).handle(io.vertx.core.Future.failedFuture(ar_.cause()));
        }
      }
    };
    return ret;
  }
  /**
   * Creates and returns a new async handler, the returned handler controls the completion of the test.<p/>
   *
   * When the returned handler is called back with a failed result it completes the async operation.<p/>
   *
   * When the returned handler is called back with a succeeded result it fails the test.<p/>
   * @return the async result handler
   */
  public <T> Handler<AsyncResult<T>> asyncAssertFailure() {
    def ret = new Handler<AsyncResult<Object>>() {
      public void handle(AsyncResult<Object> ar_) {
        if (ar_.succeeded()) {
          delegate.asyncAssertFailure().handle(io.vertx.core.Future.succeededFuture(InternalHelper.unwrapObject(ar_.result())));
        } else  {
          delegate.asyncAssertFailure().handle(io.vertx.core.Future.failedFuture(ar_.cause()));
        }
      }
    };
    return ret;
  }
  /**
   * Creates and returns a new async handler, the returned handler controls the completion of the test.<p/>
   *
   * When the returned handler is called back with a failed result it completes the async operation.<p/>
   *
   * When the returned handler is called back with a succeeded result it fails the test.<p/>
   * @param causeHandler the cause handler
   * @return the async result handler
   */
  public <T> Handler<AsyncResult<T>> asyncAssertFailure(Handler<Throwable> causeHandler) {
    def ret = new Handler<AsyncResult<Object>>() {
      public void handle(AsyncResult<Object> ar_) {
        if (ar_.succeeded()) {
          delegate.asyncAssertFailure(causeHandler).handle(io.vertx.core.Future.succeededFuture(InternalHelper.unwrapObject(ar_.result())));
        } else  {
          delegate.asyncAssertFailure(causeHandler).handle(io.vertx.core.Future.failedFuture(ar_.cause()));
        }
      }
    };
    return ret;
  }
  /**
   * @return an exception handler that will fail this context
   * @return 
   */
  public Handler<Throwable> exceptionHandler() {
    def ret = delegate.exceptionHandler();
    return ret;
  }
}
