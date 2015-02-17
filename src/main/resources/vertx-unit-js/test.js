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
 The test interface allows the test code to report test completion or failures.

 @class
*/
var Test = function(j_val) {

  var j_test = j_val;
  var that = this;

  /**

   @public

   @return {Vertx}
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

   @public

   @return {Async}
   */
  this.async = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return new Async(j_test.async());
    } else utils.invalidArgs();
  };

  /**

   @public
   @param b {boolean} 
   */
  this.assertTrue = function(b) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='boolean') {
      j_test.assertTrue(b);
    } else utils.invalidArgs();
  };

  /**

   @public
   @param s {string} 
   */
  this.fail = function(s) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_test.fail(s);
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_test;
};

// We export the Constructor function
module.exports = Test;