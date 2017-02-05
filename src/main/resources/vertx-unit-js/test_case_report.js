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

/** @module vertx-unit-js/test_case_report */
var utils = require('vertx-js/util/utils');
var TestResult = require('vertx-unit-js/test_result');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JTestCaseReport = Java.type('io.vertx.ext.unit.report.TestCaseReport');

/**
 Report the execution of a test case.

 @class
*/
var TestCaseReport = function(j_val) {

  var j_testCaseReport = j_val;
  var that = this;

  /**

   @public

   @return {string} the test case name
   */
  this.name = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedname == null) {
        that.cachedname = j_testCaseReport["name()"]();
      }
      return that.cachedname;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a callback for completion, the specified <code>handler</code> is invoked when the test exec has completed.

   @public
   @param handler {function} the completion handler 
   @return {TestCaseReport} a reference to this, so the API can be used fluently
   */
  this.endHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_testCaseReport["endHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(TestResult, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_testCaseReport;
};

TestCaseReport._jclass = utils.getJavaClass("io.vertx.ext.unit.report.TestCaseReport");
TestCaseReport._jtype = {
  accept: function(obj) {
    return TestCaseReport._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(TestCaseReport.prototype, {});
    TestCaseReport.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
TestCaseReport._create = function(jdel) {
  var obj = Object.create(TestCaseReport.prototype, {});
  TestCaseReport.apply(obj, arguments);
  return obj;
}
module.exports = TestCaseReport;