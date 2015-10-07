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

/** @module vertx-unit-js/test_suite */
var utils = require('vertx-js/util/utils');
var TestContext = require('vertx-unit-js/test_context');
var Vertx = require('vertx-js/vertx');
var TestCompletion = require('vertx-unit-js/test_completion');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JTestSuite = io.vertx.ext.unit.TestSuite;
var TestOptions = io.vertx.ext.unit.TestOptions;

/**
 A named suite of test cases that are executed altogether. The suite suite is created with
 @class
*/
var TestSuite = function(j_val) {

  var j_testSuite = j_val;
  var that = this;

  /**
   Set a callback executed before the tests.

   @public
   @param callback {function} the callback 
   @return {TestSuite} a reference to this, so the API can be used fluently
   */
  this.before = function(callback) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_testSuite["before(io.vertx.core.Handler)"](function(jVal) {
      callback(utils.convReturnVertxGen(jVal, TestContext));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a callback executed before each test and after the suite <code>before</code> callback.

   @public
   @param callback {function} the callback 
   @return {TestSuite} a reference to this, so the API can be used fluently
   */
  this.beforeEach = function(callback) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_testSuite["beforeEach(io.vertx.core.Handler)"](function(jVal) {
      callback(utils.convReturnVertxGen(jVal, TestContext));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a callback executed after the tests.

   @public
   @param callback {function} the callback 
   @return {TestSuite} a reference to this, so the API can be used fluently
   */
  this.after = function(callback) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_testSuite["after(io.vertx.core.Handler)"](function(jVal) {
      callback(utils.convReturnVertxGen(jVal, TestContext));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a callback executed after each test and before the suite <code>after</code> callback.

   @public
   @param callback {function} the callback 
   @return {TestSuite} a reference to this, so the API can be used fluently
   */
  this.afterEach = function(callback) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_testSuite["afterEach(io.vertx.core.Handler)"](function(jVal) {
      callback(utils.convReturnVertxGen(jVal, TestContext));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Add a new test case to the suite.

   @public
   @param name {string} the test case name 
   @param testCase {function} the test case 
   @return {TestSuite} a reference to this, so the API can be used fluently
   */
  this.test = function(name, testCase) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_testSuite["test(java.lang.String,io.vertx.core.Handler)"](name, function(jVal) {
      testCase(utils.convReturnVertxGen(jVal, TestContext));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Run the testsuite with the specified <code>options</code> and the specified <code>vertx</code> instance.<p/>
  
   The test suite will be executed on the event loop provided by the <code>vertx</code> argument when
   <a href="../../dataobjects.html#TestOptions">TestOptions</a> is not set to <code>false</code>. The returned
   {@link Completion} object can be used to get a completion callback.

   @public
   @param vertx {Vertx} the vertx instance 
   @param options {Object} the test options 
   @return {TestCompletion} the related test completion
   */
  this.run = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_testSuite["run()"](), TestCompletion);
    }  else if (__args.length === 1 && typeof __args[0] === 'object') {
      return utils.convReturnVertxGen(j_testSuite["run(io.vertx.ext.unit.TestOptions)"](__args[0] != null ? new TestOptions(new JsonObject(JSON.stringify(__args[0]))) : null), TestCompletion);
    }  else if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      return utils.convReturnVertxGen(j_testSuite["run(io.vertx.core.Vertx)"](__args[0]._jdel), TestCompletion);
    }  else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'object') {
      return utils.convReturnVertxGen(j_testSuite["run(io.vertx.core.Vertx,io.vertx.ext.unit.TestOptions)"](__args[0]._jdel, __args[1] != null ? new TestOptions(new JsonObject(JSON.stringify(__args[1]))) : null), TestCompletion);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_testSuite;
};

/**
 Create and return a new test suite.

 @memberof module:vertx-unit-js/test_suite
 @param name {string} the test suite name 
 @return {TestSuite} the created test suite
 */
TestSuite.create = function(name) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'string') {
    return utils.convReturnVertxGen(JTestSuite["create(java.lang.String)"](name), TestSuite);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = TestSuite;