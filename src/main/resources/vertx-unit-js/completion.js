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

/** @module vertx-unit-js/completion */
var utils = require('vertx-js/util/utils');
var Future = require('vertx-js/future');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JCompletion = io.vertx.ext.unit.Completion;

/**
 A completion object that emits completion notifications either <i>succeeded</i> or <i>failed</i>.

 @class
*/
var Completion = function(j_val) {

  var j_completion = j_val;
  var that = this;

  /**
   Completes the future upon completion, otherwise fails it.

   @public
   @param future {Future} the future to resolve 
   */
  this.resolve = function(future) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_completion["resolve(io.vertx.core.Future)"](future._jdel);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {boolean} true if this completion is completed
   */
  this.isCompleted = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_completion["isCompleted()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {boolean} true if the this completion is completed succeeded
   */
  this.isSucceeded = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_completion["isSucceeded()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {boolean} true if the this completion is completed and failed
   */
  this.isFailed = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_completion["isFailed()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Completion handler to receive a completion signal when this completions completes.

   @public
   @param completionHandler {function} the completion handler 
   */
  this.handler = function(completionHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_completion["handler(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        completionHandler(utils.convReturnTypeUnknown(ar.result()), null);
      } else {
        completionHandler(null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Cause the current thread to wait until this completion completes with a configurable timeout.<p/>
  
   If completion times out or the current thread is interrupted, an exception will be thrown.

   @public
   @param timeoutMillis {number} the timeout in milliseconds 
   */
  this.await = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_completion["await()"]();
    }  else if (__args.length === 1 && typeof __args[0] ==='number') {
      j_completion["await(long)"](__args[0]);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Cause the current thread to wait until this completion completes and succeeds with a configurable timeout.<p/>
  
   If completion times out or the current thread is interrupted or the suite fails, an exception will be thrown.

   @public
   @param timeoutMillis {number} the timeout in milliseconds 
   */
  this.awaitSuccess = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_completion["awaitSuccess()"]();
    }  else if (__args.length === 1 && typeof __args[0] ==='number') {
      j_completion["awaitSuccess(long)"](__args[0]);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_completion;
};

// We export the Constructor function
module.exports = Completion;