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

/** @module vertx-js/web_socket_base */
var utils = require('vertx-js/util/utils');
var Buffer = require('vertx-js/buffer');
var WriteStream = require('vertx-js/write_stream');
var ReadStream = require('vertx-js/read_stream');
var WebSocketFrame = require('vertx-js/web_socket_frame');
var SocketAddress = require('vertx-js/socket_address');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JWebSocketBase = Java.type('io.vertx.core.http.WebSocketBase');

/**
 Base WebSocket implementation.
 <p>
 @class
*/
var WebSocketBase = function(j_val) {

  var j_webSocketBase = j_val;
  var that = this;
  ReadStream.call(this, j_val);
  WriteStream.call(this, j_val);

  /**
   Same as {@link WebSocketBase#end} but writes some data to the stream before ending.

   @public
   @param t {Buffer} 
   */
  this.end = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_webSocketBase["end()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_webSocketBase["end(io.vertx.core.buffer.Buffer)"](__args[0]._jdel);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   This will return <code>true</code> if there are more bytes in the write queue than the value set using {@link WebSocketBase#setWriteQueueMaxSize}

   @public

   @return {boolean} true if write queue is full
   */
  this.writeQueueFull = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_webSocketBase["writeQueueFull()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {WebSocketBase}
   */
  this.exceptionHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_webSocketBase["exceptionHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnThrowable(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {WebSocketBase}
   */
  this.handler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_webSocketBase["handler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnVertxGen(Buffer, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {WebSocketBase}
   */
  this.pause = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_webSocketBase["pause()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {WebSocketBase}
   */
  this.resume = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_webSocketBase["resume()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param endHandler {function} 
   @return {WebSocketBase}
   */
  this.endHandler = function(endHandler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_webSocketBase["endHandler(io.vertx.core.Handler)"](endHandler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param data {Buffer} 
   @return {WebSocketBase}
   */
  this.write = function(data) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_webSocketBase["write(io.vertx.core.buffer.Buffer)"](data._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param maxSize {number} 
   @return {WebSocketBase}
   */
  this.setWriteQueueMaxSize = function(maxSize) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      j_webSocketBase["setWriteQueueMaxSize(int)"](maxSize);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {WebSocketBase}
   */
  this.drainHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_webSocketBase["drainHandler(io.vertx.core.Handler)"](handler);
      return that;
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
      return j_webSocketBase["binaryHandlerID()"]();
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
      return j_webSocketBase["textHandlerID()"]();
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
      return j_webSocketBase["subProtocol()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Write a WebSocket frame to the connection

   @public
   @param frame {WebSocketFrame} the frame to write 
   @return {WebSocketBase} a reference to this, so the API can be used fluently
   */
  this.writeFrame = function(frame) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_webSocketBase["writeFrame(io.vertx.core.http.WebSocketFrame)"](frame._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Write a final WebSocket text frame to the connection

   @public
   @param text {string} The text to write 
   @return {WebSocketBase} a reference to this, so the API can be used fluently
   */
  this.writeFinalTextFrame = function(text) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_webSocketBase["writeFinalTextFrame(java.lang.String)"](text);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Write a final WebSocket binary frame to the connection

   @public
   @param data {Buffer} The data to write 
   @return {WebSocketBase} a reference to this, so the API can be used fluently
   */
  this.writeFinalBinaryFrame = function(data) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_webSocketBase["writeFinalBinaryFrame(io.vertx.core.buffer.Buffer)"](data._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Writes a (potentially large) piece of binary data to the connection. This data might be written as multiple frames
   if it exceeds the maximum WebSocket frame size.

   @public
   @param data {Buffer} the data to write 
   @return {WebSocketBase} a reference to this, so the API can be used fluently
   */
  this.writeBinaryMessage = function(data) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_webSocketBase["writeBinaryMessage(io.vertx.core.buffer.Buffer)"](data._jdel);
      return that;
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
      j_webSocketBase["writeTextMessage(java.lang.String)"](text);
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
      j_webSocketBase["writePing(io.vertx.core.buffer.Buffer)"](data._jdel);
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
      j_webSocketBase["writePong(io.vertx.core.buffer.Buffer)"](data._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a close handler. This will be called when the WebSocket is closed.

   @public
   @param handler {function} the handler 
   @return {WebSocketBase} a reference to this, so the API can be used fluently
   */
  this.closeHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_webSocketBase["closeHandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a frame handler on the connection. This handler will be called when frames are read on the connection.

   @public
   @param handler {function} the handler 
   @return {WebSocketBase} a reference to this, so the API can be used fluently
   */
  this.frameHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_webSocketBase["frameHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(utils.convReturnVertxGen(WebSocketFrame, jVal));
    });
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
      j_webSocketBase["textMessageHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
      handler(jVal);
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a binary message handler on the connection. This handler serves a similar purpose to {@link WebSocketBase#handler}
   except that if a message comes into the socket in multiple frames, the data from the frames will be aggregated
   into a single buffer before calling the handler (using {@link WebSocketFrame#isFinal} to find the boundaries).

   @public
   @param handler {function} the handler 
   @return {WebSocketBase} a reference to this, so the API can be used fluently
   */
  this.binaryMessageHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && (typeof __args[0] === 'function' || __args[0] == null)) {
      j_webSocketBase["binaryMessageHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
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
      j_webSocketBase["pongHandler(io.vertx.core.Handler)"](handler == null ? null : function(jVal) {
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
      j_webSocketBase["close()"]();
    }  else if (__args.length === 1 && typeof __args[0] ==='number') {
      j_webSocketBase["close(short)"](__args[0]);
    }  else if (__args.length === 2 && typeof __args[0] ==='number' && (typeof __args[1] === 'string' || __args[1] == null)) {
      j_webSocketBase["close(short,java.lang.String)"](__args[0], __args[1]);
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
        that.cachedremoteAddress = utils.convReturnVertxGen(SocketAddress, j_webSocketBase["remoteAddress()"]());
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
        that.cachedlocalAddress = utils.convReturnVertxGen(SocketAddress, j_webSocketBase["localAddress()"]());
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
      return j_webSocketBase["isSsl()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_webSocketBase;
};

WebSocketBase._jclass = utils.getJavaClass("io.vertx.core.http.WebSocketBase");
WebSocketBase._jtype = {
  accept: function(obj) {
    return WebSocketBase._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(WebSocketBase.prototype, {});
    WebSocketBase.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
WebSocketBase._create = function(jdel) {
  var obj = Object.create(WebSocketBase.prototype, {});
  WebSocketBase.apply(obj, arguments);
  return obj;
}
module.exports = WebSocketBase;