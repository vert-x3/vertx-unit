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
var TestSuiteRunner = require('vertx-unit-js/test_suite_runner');
var Test = require('vertx-unit-js/test');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JTestSuite = io.vertx.ext.unit.TestSuite;
var TestOptions = io.vertx.ext.unit.TestOptions;
var TestOptions = io.vertx.ext.unit.TestOptions;

/**

 @class
*/
var TestSuite = function(j_val) {

  var j_testSuite = j_val;
  var that = this;

  /**

   @public
   @param handler {function} 
   @return {TestSuite}
   */
  this.before = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_testSuite.before(function(jVal) {
      handler(new Test(jVal));
    });
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public
   @param handler {function} 
   @return {TestSuite}
   */
  this.beforeEach = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_testSuite.beforeEach(function(jVal) {
      handler(new Test(jVal));
    });
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public
   @param callback {function} 
   @return {TestSuite}
   */
  this.after = function(callback) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_testSuite.after(function(jVal) {
      callback(new Test(jVal));
    });
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public
   @param callback {function} 
   @return {TestSuite}
   */
  this.afterEach = function(callback) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_testSuite.afterEach(function(jVal) {
      callback(new Test(jVal));
    });
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public
   @param name {string} 
   @param handler {function} 
   @return {TestSuite}
   */
  this.test = function(name, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_testSuite.test(name, function(jVal) {
      handler(new Test(jVal));
    });
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public
   @param vertx {Vertx} 
   @param options {Object} 
   */
  this.run = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_testSuite.run();
    }  else if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_testSuite.run(__args[0]._jdel);
    }  else if (__args.length === 1 && typeof __args[0] === 'object') {
      j_testSuite.run(__args[0] != null ? new TestOptions(new JsonObject(JSON.stringify(__args[0]))) : null);
    }  else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'object') {
      j_testSuite.run(__args[0]._jdel, __args[1] != null ? new TestOptions(new JsonObject(JSON.stringify(__args[1]))) : null);
    } else utils.invalidArgs();
  };

  /**

   @public
   @param vertx {Vertx} 
   @return {TestSuiteRunner}
   */
  this.runner = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return new TestSuiteRunner(j_testSuite.runner());
    }  else if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      return new TestSuiteRunner(j_testSuite.runner(__args[0]._jdel));
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_testSuite;
};

/**

 @memberof module:vertx-unit-js/test_suite
 @param name {string} 
 @return {TestSuite}
 */
TestSuite.create = function(name) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'string') {
    return new TestSuite(JTestSuite.create(name));
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = TestSuite;