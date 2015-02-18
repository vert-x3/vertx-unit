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

/** @module vertx-unit-js/reporter */
var utils = require('vertx-js/util/utils');
var Buffer = require('vertx-js/buffer');
var MessageProducer = require('vertx-js/message_producer');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JReporter = io.vertx.ext.unit.Reporter;
var ReportOptions = io.vertx.ext.unit.ReportOptions;
var ReportOptions = io.vertx.ext.unit.ReportOptions;

/**

 @class
*/
var Reporter = function(j_val) {

  var j_reporter = j_val;
  var that = this;

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_reporter;
};

/**

 @memberof module:vertx-unit-js/reporter
 @param vertx {Vertx} 
 @param options {Object} 
 @return {Reporter}
 */
Reporter.create = function() {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object') {
    return new Reporter(JReporter.create(__args[0] != null ? new ReportOptions(new JsonObject(JSON.stringify(__args[0]))) : null));
  }else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'object') {
    return new Reporter(JReporter.create(__args[0]._jdel, __args[1] != null ? new ReportOptions(new JsonObject(JSON.stringify(__args[1]))) : null));
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-unit-js/reporter
 @param loggerName {string} 
 @return {Reporter}
 */
Reporter.logReporter = function(loggerName) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'string') {
    return new Reporter(JReporter.logReporter(loggerName));
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-unit-js/reporter

 @return {Reporter}
 */
Reporter.consoleReporter = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return new Reporter(JReporter.consoleReporter());
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-unit-js/reporter
 @param stream {function} 
 @return {Reporter}
 */
Reporter.streamReporter = function(stream) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'function') {
    return new Reporter(JReporter.streamReporter(function(jVal) {
    stream(new Buffer(jVal));
  }));
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-unit-js/reporter
 @param producer {MessageProducer} 
 @return {Reporter}
 */
Reporter.eventBusReporter = function(producer) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return new Reporter(JReporter.eventBusReporter(producer._jdel));
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-unit-js/reporter
 @param output {function} 
 @return {Reporter}
 */
Reporter.junitXmlReporter = function(output) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'function') {
    return new Reporter(JReporter.junitXmlReporter(function(jVal) {
    output(new Buffer(jVal));
  }));
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = Reporter;