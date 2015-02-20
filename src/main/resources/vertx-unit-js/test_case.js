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

/** @module vertx-unit-js/test_case */
var utils = require('vertx-js/util/utils');
var Test = require('vertx-unit-js/test');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JTestCase = io.vertx.ext.unit.TestCase;

/**
 A test case object can be used to create a single test.

 @class
*/
var TestCase = function(j_val) {

  var j_testCase = j_val;
  var that = this;

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_testCase;
};

/**
 Create a test case.

 @memberof module:vertx-unit-js/test_case
 @param name {string} the test case name 
 @param testCase {function} the test case 
 @return {TestCase} the created test case
 */
TestCase.create = function(name, testCase) {
  var __args = arguments;
  if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
    return new TestCase(JTestCase.create(name, function(jVal) {
    testCase(new Test(jVal));
  }));
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = TestCase;