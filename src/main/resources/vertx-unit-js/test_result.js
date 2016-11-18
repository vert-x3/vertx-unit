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
var Failure = require('vertx-unit-js/failure');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JTestResult = io.vertx.ext.unit.report.TestResult;

/**
 The result of a test.

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
  this.name = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedname == null) {
        that.cachedname = j_testResult["name()"]();
      }
      return that.cachedname;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   The time at which the test began in millis.

   @public

   @return {number}
   */
  this.beginTime = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedbeginTime == null) {
        that.cachedbeginTime = j_testResult["beginTime()"]();
      }
      return that.cachedbeginTime;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   How long the test lasted in millis.

   @public

   @return {number}
   */
  this.durationTime = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cacheddurationTime == null) {
        that.cacheddurationTime = j_testResult["durationTime()"]();
      }
      return that.cacheddurationTime;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Did it succeed?

   @public

   @return {boolean}
   */
  this.succeeded = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedsucceeded == null) {
        that.cachedsucceeded = j_testResult["succeeded()"]();
      }
      return that.cachedsucceeded;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Did it fail?

   @public

   @return {boolean}
   */
  this.failed = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedfailed == null) {
        that.cachedfailed = j_testResult["failed()"]();
      }
      return that.cachedfailed;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   An exception describing failure, null if the test succeeded.

   @public

   @return {Failure}
   */
  this.failure = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedfailure == null) {
        that.cachedfailure = utils.convReturnVertxGen(Failure, j_testResult["failure()"]());
      }
      return that.cachedfailure;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_testResult;
};

TestResult._jclass = utils.getJavaClass("io.vertx.ext.unit.report.TestResult");
TestResult._jtype = {
  accept: function(obj) {
    return TestResult._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(TestResult.prototype, {});
    TestResult.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
TestResult._create = function(jdel) {
  var obj = Object.create(TestResult.prototype, {});
  TestResult.apply(obj, arguments);
  return obj;
}
module.exports = TestResult;