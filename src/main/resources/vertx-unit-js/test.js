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

/** @module vertx-unit-js/test */
var utils = require('vertx-js/util/utils');
var Async = require('vertx-unit-js/async');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JTest = io.vertx.ext.unit.Test;

/**
 The test object is used for performing test assertions and manage the completion of the test. This object
 is provided by <i>vertx-unit</i> as argument of the test case.

 @class
*/
var Test = function(j_val) {

  var j_test = j_val;
  var that = this;

  /**
   Assert the specified <code>condition</code> is <code>true</code>. If the condition is <code>false</code>, an assertion error is thrown
   otherwise the execution continue.

   @public
   @param condition {boolean} the condition to assert 
   @param message {string} the failure message 
   @return {Test} a reference to this, so the API can be used fluently
   */
  this.assertTrue = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='boolean') {
      j_test.assertTrue(__args[0]);
      return that;
    }  else if (__args.length === 2 && typeof __args[0] ==='boolean' && typeof __args[1] === 'string') {
      j_test.assertTrue(__args[0], __args[1]);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Assert the specified <code>condition</code> is <code>false</code>. If the condition is <code>true</code>, an assertion error is thrown
   otherwise the execution continue.

   @public
   @param condition {boolean} the condition to assert 
   @param message {string} the failure message 
   @return {Test} a reference to this, so the API can be used fluently
   */
  this.assertFalse = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='boolean') {
      j_test.assertFalse(__args[0]);
      return that;
    }  else if (__args.length === 2 && typeof __args[0] ==='boolean' && typeof __args[1] === 'string') {
      j_test.assertFalse(__args[0], __args[1]);
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
   @return {Test} a reference to this, so the API can be used fluently
   */
  this.assertEquals = function() {
    var __args = arguments;
    if (__args.length === 2 && true && true) {
      j_test.assertEquals(utils.convParamTypeUnknown(__args[0]), utils.convParamTypeUnknown(__args[1]));
      return that;
    }  else if (__args.length === 3 && true && true && typeof __args[2] === 'string') {
      j_test.assertEquals(utils.convParamTypeUnknown(__args[0]), utils.convParamTypeUnknown(__args[1]), __args[2]);
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
   @return {Test} a reference to this, so the API can be used fluently
   */
  this.assertInRange = function() {
    var __args = arguments;
    if (__args.length === 3 && typeof __args[0] ==='number' && typeof __args[1] ==='number' && typeof __args[2] ==='number') {
      j_test.assertInRange(__args[0], __args[1], __args[2]);
      return that;
    }  else if (__args.length === 4 && typeof __args[0] ==='number' && typeof __args[1] ==='number' && typeof __args[2] ==='number' && typeof __args[3] === 'string') {
      j_test.assertInRange(__args[0], __args[1], __args[2], __args[3]);
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
   @return {Test} a reference to this, so the API can be used fluently
   */
  this.assertNotEquals = function() {
    var __args = arguments;
    if (__args.length === 2 && true && true) {
      j_test.assertNotEquals(utils.convParamTypeUnknown(__args[0]), utils.convParamTypeUnknown(__args[1]));
      return that;
    }  else if (__args.length === 3 && true && true && typeof __args[2] === 'string') {
      j_test.assertNotEquals(utils.convParamTypeUnknown(__args[0]), utils.convParamTypeUnknown(__args[1]), __args[2]);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Throw a failure with the specified failure <code>message</code>.

   @public
   @param message {string} the failure message 
   */
  this.fail = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_test.fail();
    }  else if (__args.length === 1 && typeof __args[0] === 'string') {
      j_test.fail(__args[0]);
    } else utils.invalidArgs();
  };

  /**
   Returns the vertx instance associated with this test. The value may be null if no vertx instance was
   specified when running the test suite.

   @public

   @return {Vertx} the vertx instance
   */
  this.vertx = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedvertx == null) {
        that.cachedvertx = new Vertx(j_test.vertx());
      }
      return that.cachedvertx;
    } else utils.invalidArgs();
  };

  /**
   Create and returns a new async object, the returned async controls the completion of the test. The test case
   will complete when all the async objects have their {@link Async#complete} method called
   at least once.<p/>
  
   This method shall be used for creating asynchronous exit points for the executed test.

   @public

   @return {Async} the async instance
   */
  this.async = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return new Async(j_test.async());
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_test;
};

// We export the Constructor function
module.exports = Test;