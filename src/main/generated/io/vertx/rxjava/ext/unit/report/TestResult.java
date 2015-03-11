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

package io.vertx.rxjava.ext.unit.report;

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

  final io.vertx.ext.unit.report.TestResult delegate;

  public TestResult(io.vertx.ext.unit.report.TestResult delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * The test description, may be null if none was provided.
   */
  public String name() { 
    if (cached_0 != null) {
      return cached_0;
    }
    String ret = this.delegate.name();
    cached_0 = ret;
    return ret;
  }

  /**
   * The time at which the test began in millis.
   */
  public long beginTime() { 
    if (cached_1 != null) {
      return cached_1;
    }
    long ret = this.delegate.beginTime();
    cached_1 = ret;
    return ret;
  }

  /**
   * How long the test lasted in millis.
   */
  public long durationTime() { 
    if (cached_2 != null) {
      return cached_2;
    }
    long ret = this.delegate.durationTime();
    cached_2 = ret;
    return ret;
  }

  /**
   * Did it succeed?
   */
  public boolean succeeded() { 
    if (cached_3 != null) {
      return cached_3;
    }
    boolean ret = this.delegate.succeeded();
    cached_3 = ret;
    return ret;
  }

  /**
   * Did it fail?
   */
  public boolean failed() { 
    if (cached_4 != null) {
      return cached_4;
    }
    boolean ret = this.delegate.failed();
    cached_4 = ret;
    return ret;
  }

  /**
   * An exception describing failure, null if the test succeeded.
   */
  public Failure failure() { 
    if (cached_5 != null) {
      return cached_5;
    }
    Failure ret= Failure.newInstance(this.delegate.failure());
    cached_5 = ret;
    return ret;
  }

  private java.lang.String cached_0;
  private java.lang.Long cached_1;
  private java.lang.Long cached_2;
  private java.lang.Boolean cached_3;
  private java.lang.Boolean cached_4;
  private Failure cached_5;

  public static TestResult newInstance(io.vertx.ext.unit.report.TestResult arg) {
    return new TestResult(arg);
  }
}
