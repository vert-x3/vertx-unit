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

/** @module vertx-unit-js/event_bus_adapter */
var utils = require('vertx-js/util/utils');
var TestSuiteReport = require('vertx-unit-js/test_suite_report');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JEventBusAdapter = io.vertx.ext.unit.EventBusAdapter;

/**
 A adapter that listen to reports to the event bus and report them to an handler.

 @class
*/
var EventBusAdapter = function(j_val) {

  var j_eventBusAdapter = j_val;
  var that = this;

  /**

   @public
   @param reporter {function} 
   @return {EventBusAdapter}
   */
  this.handler = function(reporter) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_eventBusAdapter.handler(function(jVal) {
      reporter(new TestSuiteReport(jVal));
    });
      return that;
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_eventBusAdapter;
};

/**

 @memberof module:vertx-unit-js/event_bus_adapter

 @return {EventBusAdapter}
 */
EventBusAdapter.create = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return new EventBusAdapter(JEventBusAdapter.create());
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = EventBusAdapter;