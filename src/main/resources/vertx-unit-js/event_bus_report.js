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

/** @module vertx-unit-js/event_bus_report */
var utils = require('vertx-js/util/utils');
var TestSuiteReport = require('vertx-unit-js/test_suite_report');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JEventBusReport = io.vertx.ext.unit.EventBusReport;

/**

 @class
*/
var EventBusReport = function(j_val) {

  var j_eventBusReport = j_val;
  var that = this;

  /**

   @public
   @param reporter {function} 
   @return {EventBusReport}
   */
  this.handler = function(reporter) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_eventBusReport.handler(function(jVal) {
      reporter(new TestSuiteReport(jVal));
    });
      return that;
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_eventBusReport;
};

// We export the Constructor function
module.exports = EventBusReport;