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

/** @module vertx-unit-js/test_result */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JTestResult = io.vertx.ext.unit.TestResult;

/**

 @class
*/
var TestResult = function(j_val) {

  var j_testResult = j_val;
  var that = this;

  /**
   The test description, may be null if none was provided.

   @public

   @return {string}
   */
  this.description = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_testResult.description();
    } else utils.invalidArgs();
  };

  /**
   The test execution time.

   @public

   @return {number}
   */
  this.time = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_testResult.time();
    } else utils.invalidArgs();
  };

  /**
   An exception describing failure. This will be null if the test succeeded.

   @public

   @return {todo}
   */
  this.failure = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnTypeUnknown(j_testResult.failure());
    } else utils.invalidArgs();
  };

  /**
   Did it succeed?

   @public

   @return {boolean}
   */
  this.succeeded = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_testResult.succeeded();
    } else utils.invalidArgs();
  };

  /**
   Did it fail?

   @public

   @return {boolean}
   */
  this.failed = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_testResult.failed();
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_testResult;
};

// We export the Constructor function
module.exports = TestResult;