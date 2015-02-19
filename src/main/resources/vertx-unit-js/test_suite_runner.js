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

/** @module vertx-unit-js/test_suite_runner */
var utils = require('vertx-js/util/utils');
var TestSuiteReport = require('vertx-unit-js/test_suite_report');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JTestSuiteRunner = io.vertx.ext.unit.TestSuiteRunner;

/**
 The test suite runner.

 @class
*/
var TestSuiteRunner = function(j_val) {

  var j_testSuiteRunner = j_val;
  var that = this;

  /**

   @public

   @return {number}
   */
  this.getTimeout = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_testSuiteRunner.getTimeout();
    } else utils.invalidArgs();
  };

  /**

   @public
   @param timeout {number} 
   @return {TestSuiteRunner}
   */
  this.setTimeout = function(timeout) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_testSuiteRunner.setTimeout(timeout);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Set a reporter for handling the events emitted by the test suite.

   @public
   @param reporter {function} the reporter 
   @return {TestSuiteRunner} a reference to this, so the API can be used fluently
   */
  this.handler = function(reporter) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_testSuiteRunner.handler(function(jVal) {
      reporter(new TestSuiteReport(jVal));
    });
      return that;
    } else utils.invalidArgs();
  };

  /**
   Run the testsuite.

   @public

   */
  this.run = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_testSuiteRunner.run();
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_testSuiteRunner;
};

// We export the Constructor function
module.exports = TestSuiteRunner;