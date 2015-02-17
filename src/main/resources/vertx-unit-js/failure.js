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

/** @module vertx-unit-js/failure */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JFailure = io.vertx.ext.unit.Failure;

/**
 A failure provides the details of a failure that happened during the execution of a test case.<p/>

 The failure can be:
 <ul>
   <li>an assertion failure: an assertion failed</li>
   <li>an error failure: an expected error occured</li>
 </ul>


 @class
*/
var Failure = function(j_val) {

  var j_failure = j_val;
  var that = this;

  /**
   @return true if the failure is an error failure otherwise it is an assertion failure

   @public

   @return {boolean}
   */
  this.isError = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedisError == null) {
        that.cachedisError = j_failure.isError();
      }
      return that.cachedisError;
    } else utils.invalidArgs();
  };

  /**
   @return the error message

   @public

   @return {string}
   */
  this.message = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedmessage == null) {
        that.cachedmessage = j_failure.message();
      }
      return that.cachedmessage;
    } else utils.invalidArgs();
  };

  /**
   @return the stack trace

   @public

   @return {string}
   */
  this.stackTrace = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedstackTrace == null) {
        that.cachedstackTrace = j_failure.stackTrace();
      }
      return that.cachedstackTrace;
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_failure;
};

// We export the Constructor function
module.exports = Failure;