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

/** @module vertx-unit-js/suite_runner */
var utils = require('vertx-js/util/utils');
var ReadStream = require('vertx-js/read_stream');
var TestRunner = require('vertx-unit-js/test_runner');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JSuiteRunner = io.vertx.ext.unit.SuiteRunner;

/**

 @class
*/
var SuiteRunner = function(j_val) {

  var j_suiteRunner = j_val;
  var that = this;
  ReadStream.call(this, j_val);

  /**

   @public
   @param arg0 {function} 
   @return {ReadStream}
   */
  this.exceptionHandler = function(arg0) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_suiteRunner.exceptionHandler(function(jVal) {
      arg0(utils.convReturnTypeUnknown(jVal));
    });
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public
   @param arg0 {function} 
   @return {ReadStream}
   */
  this.handler = function(arg0) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_suiteRunner.handler(function(jVal) {
      arg0(new TestRunner(jVal));
    });
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public

   @return {ReadStream}
   */
  this.pause = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_suiteRunner.pause();
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public

   @return {ReadStream}
   */
  this.resume = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_suiteRunner.resume();
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public
   @param arg0 {function} 
   @return {ReadStream}
   */
  this.endHandler = function(arg0) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_suiteRunner.endHandler(arg0);
      return that;
    } else utils.invalidArgs();
  };

  /**
   Run the suite in the current thread.

   @public

   */
  this.run = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_suiteRunner.run();
    }  else if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_suiteRunner.run(__args[0]._jdel);
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_suiteRunner;
};

// We export the Constructor function
module.exports = SuiteRunner;