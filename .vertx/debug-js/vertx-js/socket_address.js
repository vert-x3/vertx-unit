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

/** @module vertx-js/socket_address */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JSocketAddress = Java.type('io.vertx.core.net.SocketAddress');

/**
 The address of a socket, an inet socket address or a domain socket address.
 <p/>
 @class
*/
var SocketAddress = function(j_val) {

  var j_socketAddress = j_val;
  var that = this;

  /**

   @public

   @return {string} the address host or <code>null</code> for a domain socket
   */
  this.host = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_socketAddress["host()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {number} the address port or <code>-1</code> for a domain socket
   */
  this.port = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_socketAddress["port()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {string} the address path or <code>null</code> for a inet socket
   */
  this.path = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_socketAddress["path()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_socketAddress;
};

SocketAddress._jclass = utils.getJavaClass("io.vertx.core.net.SocketAddress");
SocketAddress._jtype = {
  accept: function(obj) {
    return SocketAddress._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(SocketAddress.prototype, {});
    SocketAddress.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
SocketAddress._create = function(jdel) {
  var obj = Object.create(SocketAddress.prototype, {});
  SocketAddress.apply(obj, arguments);
  return obj;
}
/**
 Create a inet socket address, <code>host</code> must be non <code>null</code> and <code>port</code> must be between <code>0</code>
 and <code>65536</code>.

 @memberof module:vertx-js/socket_address
 @param port {number} the address port 
 @param host {string} the address host 
 @return {SocketAddress} the created socket address
 */
SocketAddress.inetSocketAddress = function(port, host) {
  var __args = arguments;
  if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] === 'string') {
    return utils.convReturnVertxGen(SocketAddress, JSocketAddress["inetSocketAddress(int,java.lang.String)"](port, host));
  } else throw new TypeError('function invoked with invalid arguments');
};

/**
 Create a domain socket address.

 @memberof module:vertx-js/socket_address
 @param path {string} the address path 
 @return {SocketAddress} the created socket address
 */
SocketAddress.domainSocketAddress = function(path) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'string') {
    return utils.convReturnVertxGen(SocketAddress, JSocketAddress["domainSocketAddress(java.lang.String)"](path));
  } else throw new TypeError('function invoked with invalid arguments');
};

module.exports = SocketAddress;