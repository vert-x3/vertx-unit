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

/** @module vertx-unit-js/async */
var utils = require('vertx-js/util/utils');
var Completion = require('vertx-unit-js/completion');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JAsync = io.vertx.ext.unit.Async;

/**
 An asynchronous exit point for a test.

 @class
*/
var Async = function(j_val) {

  var j_async = j_val;
  var that = this;
  Completion.call(this, j_val);

  /**
   Signals the asynchronous operation is done, this method should be called only once, if the method is called
   another time it will throw an <code>IllegalStateException</code> to signal the error.

   @public

   */
  this.complete = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_async["complete()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_async;
};

// We export the Constructor function
module.exports = Async;