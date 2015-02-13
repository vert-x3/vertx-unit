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

/** @module vertx-unit-js/unit */
var utils = require('vertx-js/util/utils');
var TestSuite = require('vertx-unit-js/test_suite');
var Test = require('vertx-unit-js/test');
var TestCase = require('vertx-unit-js/test_case');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JUnit = io.vertx.ext.unit.Unit;

/**
 The factory for creating module or individual tests.

 @class
*/
var Unit = function(j_val) {

  var j_unit = j_val;
  var that = this;

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_unit;
};

/**

 @memberof module:vertx-unit-js/unit
 @param desc {string} 
 @return {TestSuite}
 */
Unit.suite = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return new TestSuite(JUnit.suite());
  }else if (__args.length === 1 && typeof __args[0] === 'string') {
    return new TestSuite(JUnit.suite(__args[0]));
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-unit-js/unit
 @param desc {string} 
 @param handler {function} 
 @return {TestCase}
 */
Unit.test = function(desc, handler) {
  var __args = arguments;
  if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
    return new TestCase(JUnit.test(desc, function(jVal) {
    handler(new Test(jVal));
  }));
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = Unit;