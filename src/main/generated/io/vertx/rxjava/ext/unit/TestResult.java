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
 * The result of a test.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class TestResult {

  final io.vertx.ext.unit.TestResult delegate;

  public TestResult(io.vertx.ext.unit.TestResult delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * The test description, may be null if none was provided.
   */
  public String description() {
    String ret = this.delegate.description();
    return ret;
  }

  /**
   * The test execution time.
   */
  public long time() {
    long ret = this.delegate.time();
    return ret;
  }

  /**
   * Did it succeed?
   */
  public boolean succeeded() {
    boolean ret = this.delegate.succeeded();
    return ret;
  }

  /**
   * Did it fail?
   */
  public boolean failed() {
    boolean ret = this.delegate.failed();
    return ret;
  }

  /**
   * An exception describing failure, null if the test succeeded.
   */
  public Throwable failure() {
    Throwable ret = this.delegate.failure();
    return ret;
  }


  public static TestResult newInstance(io.vertx.ext.unit.TestResult arg) {
    return new TestResult(arg);
  }
}
