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

/** @module vertx-unit-js/event_bus_collector */
var utils = require('vertx-js/util/utils');
var TestSuiteReport = require('vertx-unit-js/test_suite_report');
var Vertx = require('vertx-js/vertx');
var MessageConsumer = require('vertx-js/message_consumer');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JEventBusCollector = io.vertx.ext.unit.collect.EventBusCollector;
var ReportingOptions = io.vertx.ext.unit.report.ReportingOptions;

/**
 The event bus collector listen to events on the Vert.x event bus and translate them
 into reports.

 @class
*/
var EventBusCollector = function(j_val) {

  var j_eventBusCollector = j_val;
  var that = this;

  /**
   Register the collector as a consumer of the event bus with the specified address.

   @public
   @param address {string} the registration address 
   @return {MessageConsumer} the subscribed message consumer
   */
  this.register = function(address) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(j_eventBusCollector["register(java.lang.String)"](address), MessageConsumer);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_eventBusCollector;
};

/**

 @memberof module:vertx-unit-js/event_bus_collector
 @param vertx {Vertx} 
 @param reporter {function} 
 @return {EventBusCollector}
 */
EventBusCollector.create = function() {
  var __args = arguments;
  if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'object') {
    return utils.convReturnVertxGen(JEventBusCollector["create(io.vertx.core.Vertx,io.vertx.ext.unit.report.ReportingOptions)"](__args[0]._jdel, __args[1] != null ? new ReportingOptions(new JsonObject(JSON.stringify(__args[1]))) : null), EventBusCollector);
  }else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'function') {
    return utils.convReturnVertxGen(JEventBusCollector["create(io.vertx.core.Vertx,io.vertx.core.Handler)"](__args[0]._jdel, function(jVal) {
    __args[1](utils.convReturnVertxGen(jVal, TestSuiteReport));
  }), EventBusCollector);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = EventBusCollector;