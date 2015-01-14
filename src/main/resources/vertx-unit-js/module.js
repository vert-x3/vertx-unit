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

/** @module vertx-unit-js/module */
var utils = require('vertx-js/util/utils');
var Test = require('vertx-unit-js/test');
var ModuleExec = require('vertx-unit-js/module_exec');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JModule = io.vertx.ext.unit.Module;

/**

 @class
*/
var Module = function(j_val) {

  var j_module = j_val;
  var that = this;

  /**

   @public

   @return {boolean}
   */
  this.isThreadCheckEnabled = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_module.isThreadCheckEnabled();
    } else utils.invalidArgs();
  };

  /**

   @public
   @param threadCheckEnabled {boolean} 
   @return {Module}
   */
  this.setThreadCheckEnabled = function(threadCheckEnabled) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='boolean') {
      j_module.setThreadCheckEnabled(threadCheckEnabled);
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public
   @param callback {function} 
   @return {Module}
   */
  this.before = function(callback) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_module.before(callback);
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public
   @param callback {function} 
   @return {Module}
   */
  this.after = function(callback) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_module.after(callback);
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public
   @param desc {string} 
   @param handler {function} 
   @return {Module}
   */
  this.test = function(desc, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_module.test(desc, function(jVal) {
      handler(new Test(jVal));
    });
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public
   @param desc {string} 
   @param handler {function} 
   @return {Module}
   */
  this.asyncTest = function(desc, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_module.asyncTest(desc, function(jVal) {
      handler(new Test(jVal));
    });
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public

   */
  this.execute = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_module.execute();
    }  else if (__args.length === 1 && typeof __args[0] === 'function') {
      j_module.execute(function(jVal) {
      __args[0](new ModuleExec(jVal));
    });
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_module;
};

// We export the Constructor function
module.exports = Module;