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

/** @module vertx-unit-js/test_completion */
var utils = require('vertx-js/util/utils');
var Future = require('vertx-js/future');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JTestCompletion = io.vertx.ext.unit.TestCompletion;

/**
 This object provides callback-ability for the end of a test suite.

 @class
*/
var TestCompletion = function(j_val) {

  var j_testCompletion = j_val;
  var that = this;

  /**
   Completes the future when all test cases of the test suite passes, otherwise fails it.

   @public
   @param future {Future} the future to resolve 
   */
  this.resolve = function(future) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_testCompletion["resolve(io.vertx.core.Future)"](future._jdel);
    } else utils.invalidArgs();
  };

  /**
   @return true if the test suite completed

   @public

   @return {boolean}
   */
  this.isCompleted = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_testCompletion["isCompleted()"]();
    } else utils.invalidArgs();
  };

  /**
   @return true if the test suite completed and succeeded

   @public

   @return {boolean}
   */
  this.isSucceeded = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_testCompletion["isSucceeded()"]();
    } else utils.invalidArgs();
  };

  /**
   @return true if the test suite completed and failed

   @public

   @return {boolean}
   */
  this.isFailed = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_testCompletion["isFailed()"]();
    } else utils.invalidArgs();
  };

  /**
   Completion handler for the end of the test, the result is successful when all test cases pass otherwise
   it is failed.

   @public
   @param completionHandler {function} the completion handler 
   */
  this.handler = function(completionHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_testCompletion["handler(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        completionHandler(null, null);
      } else {
        completionHandler(null, ar.cause());
      }
    });
    } else utils.invalidArgs();
  };

  /**
   Cause the current thread to wait until the test suite completes with a configurable timeout.<p/>
  
   If completion times out or the current thread is interrupted, an exception will be thrown.

   @public
   @param timeoutMillis {number} the timeout in milliseconds 
   */
  this.await = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_testCompletion["await()"]();
    }  else if (__args.length === 1 && typeof __args[0] ==='number') {
      j_testCompletion["await(long)"](__args[0]);
    } else utils.invalidArgs();
  };

  /**
   Cause the current thread to wait until the test suite completes and succeeds with a configurable timeout.<p/>
  
   If completion times out or the current thread is interrupted or the suite fails, an exception will be thrown.

   @public
   @param timeoutMillis {number} the timeout in milliseconds 
   */
  this.awaitSuccess = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_testCompletion["awaitSuccess()"]();
    }  else if (__args.length === 1 && typeof __args[0] ==='number') {
      j_testCompletion["awaitSuccess(long)"](__args[0]);
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_testCompletion;
};

// We export the Constructor function
module.exports = TestCompletion;