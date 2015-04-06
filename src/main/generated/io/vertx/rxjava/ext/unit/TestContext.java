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
 * The test context is used for performing test assertions and manage the completion of the test. This context
 * is provided by <i>vertx-unit</i> as argument of the test case.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.unit.TestContext original} non RX-ified interface using Vert.x codegen.
 */

public class TestContext {

  final io.vertx.ext.unit.TestContext delegate;

  public TestContext(io.vertx.ext.unit.TestContext delegate) {
    this.delegate = delegate;
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
    T ret = (T) this.delegate.get(key);
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
    T ret = (T) this.delegate.put(key, value);
    return ret;
  }

  /**
   * Remove some data from the context.
   * @param key the key to remove
   * @return the removed object when it exists
   */
  public <T> T remove(String key) { 
    T ret = (T) this.delegate.remove(key);
    return ret;
  }

  /**
   * Assert the <code>expected</code> argument is <code>null</code>. If the argument is not, an assertion error is thrown
   * otherwise the execution continue.
   * @param expected the argument being asserted to be null
   * @return a reference to this, so the API can be used fluently
   */
  public TestContext assertNull(Object expected) { 
    this.delegate.assertNull(expected);
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
    this.delegate.assertNull(expected, message);
    return this;
  }

  /**
   * Assert the <code>expected</code> argument is not <code>null</code>. If the argument is <code>null</code>, an assertion error is thrown
   * otherwise the execution continue.
   * @param expected the argument being asserted to be not null
   * @return a reference to this, so the API can be used fluently
   */
  public TestContext assertNotNull(Object expected) { 
    this.delegate.assertNotNull(expected);
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
    this.delegate.assertNotNull(expected, message);
    return this;
  }

  /**
   * Assert the specified <code>condition</code> is <code>true</code>. If the condition is <code>false</code>, an assertion error is thrown
   * otherwise the execution continue.
   * @param condition the condition to assert
   * @return a reference to this, so the API can be used fluently
   */
  public TestContext assertTrue(boolean condition) { 
    this.delegate.assertTrue(condition);
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
    this.delegate.assertTrue(condition, message);
    return this;
  }

  /**
   * Assert the specified <code>condition</code> is <code>false</code>. If the condition is <code>true</code>, an assertion error is thrown
   * otherwise the execution continue.
   * @param condition the condition to assert
   * @return a reference to this, so the API can be used fluently
   */
  public TestContext assertFalse(boolean condition) { 
    this.delegate.assertFalse(condition);
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
    this.delegate.assertFalse(condition, message);
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
    this.delegate.assertEquals(expected, actual);
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
    this.delegate.assertEquals(expected, actual, message);
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
    this.delegate.assertInRange(expected, actual, delta);
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
    this.delegate.assertInRange(expected, actual, delta, message);
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
    this.delegate.assertNotEquals(first, second);
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
    this.delegate.assertNotEquals(first, second, message);
    return this;
  }

  /**
   * Throw a failure.
   */
  public void fail() { 
    this.delegate.fail();
  }

  /**
   * Throw a failure with the specified failure <code>message</code>.
   * @param message the failure message
   */
  public void fail(String message) { 
    this.delegate.fail(message);
  }

  /**
   * Create and returns a new async object, the returned async controls the completion of the test. Calling the
   * {@link  Async#complete()} completes the async operation.<p/>
   *
   * The test case will complete when all the async objects have their {@link  io.vertx.rxjava.ext.unit.Async#complete()}
   * method called at least once.<p/>
   *
   * This method shall be used for creating asynchronous exit points for the executed test.
   * @return the async instance
   */
  public Async async() { 
    Async ret= Async.newInstance(this.delegate.async());
    return ret;
  }


  public static TestContext newInstance(io.vertx.ext.unit.TestContext arg) {
    return new TestContext(arg);
  }
}
