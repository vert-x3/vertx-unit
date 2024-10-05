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

/** @module vertx-js/server_web_socket */
var utils = require('vertx-js/util/utils');
var WebSocketBase = require('vertx-js/web_socket_base');
var Buffer = require('vertx-js/buffer');
var MultiMap = require('vertx-js/multi_map');
var WebSocketFrame = require('vertx-js/web_socket_frame');
var SocketAddress = require('vertx-js/socket_address');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JServerWebSocket = Java.type('io.vertx.core.http.ServerWebSocket');

/**
 Represents a server side WebSocket.
 <p>
 @class
*/
var ServerWebSocket = function(j_val) {

  var j_serverWebSocket = j_val;
  var that = this;
  WebSocketBase.call(this, j_val);

  /**
   Same as {@link WebSocketBase#end} but writes some data to the stream before ending.

   @public
   @param t {Buffer} 
   */
  this.end = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_serverWebSocket["end()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_serverWebSocket["end(io.vertx.core.buffer.Buffer)"](__args[0]._jdel);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   This will return <code>true</code> if there are more bytes in the write queue than the value set using {@link ServerWebSocket#setWriteQueueMaxSize}

   @public

   @return {boolean} true if write queue is full
   */
  this.writeQueueFull = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_serverWebSocket["writeQueueFull()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   When a <code>Websocket</code> is created it automatically registers an event handler with the event bus - the ID of that
   handler is given by this method.
   <p>
   Given this ID, a different event loop can send a binary frame to that event handler using the event bus and
   that buffer will be received by this instance in its own event loop and written to the underlying connection. This
   allows you to write data to other WebSockets which are owned by different event loops.

   @public

   @return {string} the binary handler id
   */
  this.binaryHandlerID = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_serverWebSocket["binaryHandlerID()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   When a <code>Websocket</code> is created it automatically registers an event handler with the eventbus, the ID of that
   handler is given by <code>textHandlerID</code>.
   <p>
   Given this ID, a different event loop can send a text frame to that event handler using the event bus and
   that buffer will be received by this instance in its own event loop and written to the underlying connection. This
   allows you to write data to other WebSockets which are owned by different event loops.

   @public

   @return {string}
   */
  this.textHandlerID = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_serverWebSocket["textHandlerID()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns the websocket sub protocol selected by the websocket handshake.
   <p/>
   On the server, the value will be <code>null</code> when the handler receives the websocket callback as the
   handshake will not be completed yet.

   @public

   @return {string}
   */
  this.subProtocol = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_serverWebSocket["subProtocol()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Writes a (potentially large) piece of text data to the connection. This data might be written as multiple frames
   if it exceeds the maximum WebSocket frame size.

   @public
   @param text {string} the data to write 
   @return {WebSocketBase} a reference to this, so the API can be used fluently
   */
  this.writeTextMessage = function(text) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_serverWebSocket["writeTextMessage(java.lang.String)"](text);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Writes a ping to the connection. This will be written in a single frame. Ping frames may be at most 125 bytes (octets).
   <p>
   This method should not be used to write application data and should only be used for implementing a keep alive or
   to ensure the client is still responsive, see RFC 6455 Section 5.5.2.
   <p>
   There is no pingHandler because RFC 6455 section 5.5.2 clearly states that the only response to a ping is a pong
   with identical contents.

   @public
   @param data {Buffer} the data to write, may be at most 125 bytes 
   @return {WebSocketBase} a reference to this, so the API can be used fluently
   */
  this.writePing = function(data) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_serverWebSocket["writePing(io.vertx.core.buffer.Buffer)"](data._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Writes a pong to the connection. This will be written in a single frame. Pong frames may be at most 125 bytes (octets).
   <p>
   This method should not be used to write application data and should only be used for implementing a keep alive or
   to ensure the client is still responsive, see RFC 6455 Section 5.5.2.
   <p>
   There is no need to manually write a Pong, as the server and client both handle responding to a ping with a pong
   automatically and this is exposed to users.RFC 6455 Section 5.5.3 states that pongs may be sent unsolicited in order
   to implement a one way heartbeat.

   @public
   @param data {Buffer} the data to write, may be at most 125 bytes 
   @return {WebSocketBase} a reference to this, so the API can be used fluently
   */
  this.writePong = function(data) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_serverWebSocket["writePong(io.vertx.core.buffer.Buffer)"](data._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a text message handler on the connection. This handler will be called similar to the
   , but the buffer will be converted to a String first

   @public
   @param handler {function} the handler 
   @return {WebSocketBase} a reference to this, so the API can be used fluently
   */
  this.textMessageHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_serverWebSocket["textMessageHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(jVal);
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a binary message handler on the connection. This handler serves a similar purpose to {@link ServerWebSocket#handler}
   except that if a message comes into the socket in multiple frames, the data from the frames will be aggregated
   into a single buffer before calling the handler (using {@link WebSocketFrame#isFinal} to find the boundaries).

   @public
   @param handler {function} the handler 
   @return {WebSocketBase} a reference to this, so the API can be used fluently
   */
  this.binaryMessageHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_serverWebSocket["binaryMessageHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnVertxGen(Buffer, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a pong message handler on the connection.  This handler will be invoked every time a pong message is received
   on the server, and can be used by both clients and servers since the RFC 6455 Sections 5.5.2 and 5.5.3 do not
   specify whether the client or server sends a ping.
   <p>
   Pong frames may be at most 125 bytes (octets).
   <p>
   There is no ping handler since pings should immediately be responded to with a pong with identical content
   <p>
   Pong frames may be received unsolicited.

   @public
   @param handler {function} the handler 
   @return {WebSocketBase} a reference to this, so the API can be used fluently
   */
  this.pongHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_serverWebSocket["pongHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnVertxGen(Buffer, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param statusCode {number} 
   @param reason {string} 
   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_serverWebSocket["close()"]();
    }  else if (__args.length === 1 && typeof __args[0] ==='number') {
      j_serverWebSocket["close(short)"](__args[0]);
    }  else if (__args.length === 2 && typeof __args[0] ==='number' && (typeof __args[1] === 'string' || __args[1] == null)) {
      j_serverWebSocket["close(short,java.lang.String)"](__args[0], __args[1]);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {SocketAddress} the remote address for this socket
   */
  this.remoteAddress = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedremoteAddress == null) {
        that.cachedremoteAddress = utils.convReturnVertxGen(SocketAddress, j_serverWebSocket["remoteAddress()"]());
      }
      return that.cachedremoteAddress;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {SocketAddress} the local address for this socket
   */
  this.localAddress = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedlocalAddress == null) {
        that.cachedlocalAddress = utils.convReturnVertxGen(SocketAddress, j_serverWebSocket["localAddress()"]());
      }
      return that.cachedlocalAddress;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {boolean} true if this {@link HttpConnection} is encrypted via SSL/TLS.
   */
  this.isSsl = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_serverWebSocket["isSsl()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {ServerWebSocket}
   */
  this.exceptionHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_serverWebSocket["exceptionHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnThrowable(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {ServerWebSocket}
   */
  this.handler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_serverWebSocket["handler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnVertxGen(Buffer, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {ServerWebSocket}
   */
  this.pause = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_serverWebSocket["pause()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {ServerWebSocket}
   */
  this.resume = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_serverWebSocket["resume()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param endHandler {function} 
   @return {ServerWebSocket}
   */
  this.endHandler = function(endHandler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_serverWebSocket["endHandler(io.vertx.core.Handler)"](endHandler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param data {Buffer} 
   @return {ServerWebSocket}
   */
  this.write = function(data) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_serverWebSocket["write(io.vertx.core.buffer.Buffer)"](data._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param maxSize {number} 
   @return {ServerWebSocket}
   */
  this.setWriteQueueMaxSize = function(maxSize) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_serverWebSocket["setWriteQueueMaxSize(int)"](maxSize);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {ServerWebSocket}
   */
  this.drainHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_serverWebSocket["drainHandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param frame {WebSocketFrame} 
   @return {ServerWebSocket}
   */
  this.writeFrame = function(frame) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_serverWebSocket["writeFrame(io.vertx.core.http.WebSocketFrame)"](frame._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param text {string} 
   @return {ServerWebSocket}
   */
  this.writeFinalTextFrame = function(text) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_serverWebSocket["writeFinalTextFrame(java.lang.String)"](text);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param data {Buffer} 
   @return {ServerWebSocket}
   */
  this.writeFinalBinaryFrame = function(data) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_serverWebSocket["writeFinalBinaryFrame(io.vertx.core.buffer.Buffer)"](data._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param data {Buffer} 
   @return {ServerWebSocket}
   */
  this.writeBinaryMessage = function(data) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_serverWebSocket["writeBinaryMessage(io.vertx.core.buffer.Buffer)"](data._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {ServerWebSocket}
   */
  this.closeHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_serverWebSocket["closeHandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {ServerWebSocket}
   */
  this.frameHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_serverWebSocket["frameHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnVertxGen(WebSocketFrame, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {string}
   */
  this.uri = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_serverWebSocket["uri()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {string} the WebSocket handshake path.
   */
  this.path = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_serverWebSocket["path()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {string} the WebSocket handshake query string.
   */
  this.query = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_serverWebSocket["query()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {MultiMap} the headers in the WebSocket handshake
   */
  this.headers = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedheaders == null) {
        that.cachedheaders = utils.convReturnVertxGen(MultiMap, j_serverWebSocket["headers()"]());
      }
      return that.cachedheaders;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Accept the WebSocket and terminate the WebSocket handshake.
   <p/>
   This method should be called from the websocket handler to explicitely accept the websocker and
   terminate the WebSocket handshake.

   @public

   */
  this.accept = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_serverWebSocket["accept()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Like {@link ServerWebSocket#reject} but with a <code>status</code>.

   @public
   @param status {number} 
   */
  this.reject = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_serverWebSocket["reject()"]();
    }  else if (__args.length === 1 && typeof __args[0] ==='number') {
      j_serverWebSocket["reject(int)"](__args[0]);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_serverWebSocket;
};

ServerWebSocket._jclass = utils.getJavaClass("io.vertx.core.http.ServerWebSocket");
ServerWebSocket._jtype = {
  accept: function(obj) {
    return ServerWebSocket._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(ServerWebSocket.prototype, {});
    ServerWebSocket.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
ServerWebSocket._create = function(jdel) {
  var obj = Object.create(ServerWebSocket.prototype, {});
  ServerWebSocket.apply(obj, arguments);
  return obj;
}
module.exports = ServerWebSocket;