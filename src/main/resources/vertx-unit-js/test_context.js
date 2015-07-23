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

/** @module vertx-unit-js/test_context */
var utils = require('vertx-js/util/utils');
var Async = require('vertx-unit-js/async');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JTestContext = io.vertx.ext.unit.TestContext;

/**
 The test context is used for performing test assertions and manage the completion of the test. This context
 is provided by <i>vertx-unit</i> as argument of the test case.

 @class
*/
var TestContext = function(j_val) {

  var j_testContext = j_val;
  var that = this;

  /**
   Get some data from the context.

   @public
   @param key {string} the key of the data 
   @return {Object} the data
   */
  this.get = function(key) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnTypeUnknown(j_testContext["get(java.lang.String)"](key));
    } else utils.invalidArgs();
  };

  /**
   Put some data in the context.
   <p>
   This can be used to share data between different tests and before/after phases.

   @public
   @param key {string} the key of the data 
   @param value {Object} the data 
   @return {Object} the previous object when it exists
   */
  this.put = function(key, value) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && true) {
      return utils.convReturnTypeUnknown(j_testContext["put(java.lang.String,java.lang.Object)"](key, utils.convParamTypeUnknown(value)));
    } else utils.invalidArgs();
  };

  /**
   Remove some data from the context.

   @public
   @param key {string} the key to remove 
   @return {Object} the removed object when it exists
   */
  this.remove = function(key) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnTypeUnknown(j_testContext["remove(java.lang.String)"](key));
    } else utils.invalidArgs();
  };

  /**
   Assert the <code>expected</code> argument is <code>null</code>. If the argument is not, an assertion error is thrown
   otherwise the execution continue.

   @public
   @param expected {Object} the argument being asserted to be null 
   @param message {string} the failure message 
   @return {TestContext} a reference to this, so the API can be used fluently
   */
  this.assertNull = function() {
    var __args = arguments;
    if (__args.length === 1 && true) {
      j_testContext["assertNull(java.lang.Object)"](utils.convParamTypeUnknown(__args[0]));
      return that;
    }  else if (__args.length === 2 && true && typeof __args[1] === 'string') {
      j_testContext["assertNull(java.lang.Object,java.lang.String)"](utils.convParamTypeUnknown(__args[0]), __args[1]);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Assert the <code>expected</code> argument is not <code>null</code>. If the argument is <code>null</code>, an assertion error is thrown
   otherwise the execution continue.

   @public
   @param expected {Object} the argument being asserted to be not null 
   @param message {string} the failure message 
   @return {TestContext} a reference to this, so the API can be used fluently
   */
  this.assertNotNull = function() {
    var __args = arguments;
    if (__args.length === 1 && true) {
      j_testContext["assertNotNull(java.lang.Object)"](utils.convParamTypeUnknown(__args[0]));
      return that;
    }  else if (__args.length === 2 && true && typeof __args[1] === 'string') {
      j_testContext["assertNotNull(java.lang.Object,java.lang.String)"](utils.convParamTypeUnknown(__args[0]), __args[1]);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Assert the specified <code>condition</code> is <code>true</code>. If the condition is <code>false</code>, an assertion error is thrown
   otherwise the execution continue.

   @public
   @param condition {boolean} the condition to assert 
   @param message {string} the failure message 
   @return {TestContext} a reference to this, so the API can be used fluently
   */
  this.assertTrue = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='boolean') {
      j_testContext["assertTrue(boolean)"](__args[0]);
      return that;
    }  else if (__args.length === 2 && typeof __args[0] ==='boolean' && typeof __args[1] === 'string') {
      j_testContext["assertTrue(boolean,java.lang.String)"](__args[0], __args[1]);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Assert the specified <code>condition</code> is <code>false</code>. If the condition is <code>true</code>, an assertion error is thrown
   otherwise the execution continue.

   @public
   @param condition {boolean} the condition to assert 
   @param message {string} the failure message 
   @return {TestContext} a reference to this, so the API can be used fluently
   */
  this.assertFalse = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='boolean') {
      j_testContext["assertFalse(boolean)"](__args[0]);
      return that;
    }  else if (__args.length === 2 && typeof __args[0] ==='boolean' && typeof __args[1] === 'string') {
      j_testContext["assertFalse(boolean,java.lang.String)"](__args[0], __args[1]);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Assert the <code>expected</code> argument is equals to the <code>actual</code> argument. If the arguments are not equals
   an assertion error is thrown otherwise the execution continue.

   @public
   @param expected {Object} the object the actual object is supposedly equals to 
   @param actual {Object} the actual object to test 
   @param message {string} the failure message 
   @return {TestContext} a reference to this, so the API can be used fluently
   */
  this.assertEquals = function() {
    var __args = arguments;
    if (__args.length === 2 && true && true) {
      j_testContext["assertEquals(java.lang.Object,java.lang.Object)"](utils.convParamTypeUnknown(__args[0]), utils.convParamTypeUnknown(__args[1]));
      return that;
    }  else if (__args.length === 3 && true && true && typeof __args[2] === 'string') {
      j_testContext["assertEquals(java.lang.Object,java.lang.Object,java.lang.String)"](utils.convParamTypeUnknown(__args[0]), utils.convParamTypeUnknown(__args[1]), __args[2]);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Asserts that the <code>expected</code> double argument is equals to the <code>actual</code> double argument
   within a positive delta. If the arguments do not satisfy this, an assertion error is thrown otherwise
   the execution continue.

   @public
   @param expected {number} the object the actual object is supposedly equals to 
   @param actual {number} the actual object to test 
   @param delta {number} the maximum delta 
   @param message {string} the failure message 
   @return {TestContext} a reference to this, so the API can be used fluently
   */
  this.assertInRange = function() {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] ==='number' && typeof __args[1] ==='number' && typeof __args[2] ==='number') {
      j_testContext["assertInRange(double,double,double)"](__args[0], __args[1], __args[2]);
      return that;
    }  else if (__args.length === 4 && typeof __args[0] ==='number' && typeof __args[1] ==='number' && typeof __args[2] ==='number' && typeof __args[3] === 'string') {
      j_testContext["assertInRange(double,double,double,java.lang.String)"](__args[0], __args[1], __args[2], __args[3]);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Assert the <code>first</code> argument is not equals to the <code>second</code> argument. If the arguments are equals
   an assertion error is thrown otherwise the execution continue.

   @public
   @param first {Object} the first object to test 
   @param second {Object} the second object to test 
   @param message {string} the failure message 
   @return {TestContext} a reference to this, so the API can be used fluently
   */
  this.assertNotEquals = function() {
    var __args = arguments;
    if (__args.length === 2 && true && true) {
      j_testContext["assertNotEquals(java.lang.Object,java.lang.Object)"](utils.convParamTypeUnknown(__args[0]), utils.convParamTypeUnknown(__args[1]));
      return that;
    }  else if (__args.length === 3 && true && true && typeof __args[2] === 'string') {
      j_testContext["assertNotEquals(java.lang.Object,java.lang.Object,java.lang.String)"](utils.convParamTypeUnknown(__args[0]), utils.convParamTypeUnknown(__args[1]), __args[2]);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Throw a failure with the specified failure <code>cause</code>.

   @public
   @param cause {todo} the failure cause 
   */
  this.fail = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_testContext["fail()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'string') {
      j_testContext["fail(java.lang.String)"](__args[0]);
    }  else if (__args.length === 1 && typeof __args[0] === 'object') {
      j_testContext["fail(java.lang.Throwable)"](utils.convParamThrowable(__args[0]));
    } else utils.invalidArgs();
  };

  /**
   Create and returns a new async object, the returned async controls the completion of the test. Calling the
   {@link Async#complete} completes the async operation.<p/>
  
   The test case will complete when all the async objects have their {@link Async#complete}
   method called at least once.<p/>
  
   This method shall be used for creating asynchronous exit points for the executed test.

   @public

   @return {Async} the async instance
   */
  this.async = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_testContext["async()"](), Async);
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_testContext;
};

// We export the Constructor function
module.exports = TestContext;