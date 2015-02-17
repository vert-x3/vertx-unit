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
 * A failure provides the details of a failure that happened during the execution of a test case.<p/>
 *
 * The failure can be:
 * <ul>
 *   <li>an assertion failure: an assertion failed</li>
 *   <li>an error failure: an expected error occured</li>
 * </ul>
 *
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * NOTE: This class has been automatically generated from the original non RX-ified interface using Vert.x codegen.
 */

public class Failure {

  final io.vertx.ext.unit.Failure delegate;

  public Failure(io.vertx.ext.unit.Failure delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * @return true if the failure is an error failure otherwise it is an assertion failure
   */
  public boolean isError() {
    if (cached_0 != null) {
      return cached_0;
    }
    boolean ret = this.delegate.isError();
    cached_0 = ret;
    return ret;
  }

  /**
   * @return the error message
   */
  public String message() {
    if (cached_1 != null) {
      return cached_1;
    }
    String ret = this.delegate.message();
    cached_1 = ret;
    return ret;
  }

  /**
   * @return the stack trace
   */
  public String stackTrace() {
    if (cached_2 != null) {
      return cached_2;
    }
    String ret = this.delegate.stackTrace();
    cached_2 = ret;
    return ret;
  }

  private java.lang.Boolean cached_0;
  private java.lang.String cached_1;
  private java.lang.String cached_2;

  public static Failure newInstance(io.vertx.ext.unit.Failure arg) {
    return new Failure(arg);
  }
}
