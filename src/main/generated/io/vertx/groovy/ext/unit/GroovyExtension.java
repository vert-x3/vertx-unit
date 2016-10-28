package io.vertx.groovy.ext.unit;
public class GroovyExtension {
  public static void handler(io.vertx.ext.unit.Completion j_receiver, io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Object>> completionHandler) {
    j_receiver.handler(completionHandler != null ? new io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Object>>() {
      public void handle(io.vertx.core.AsyncResult<java.lang.Object> ar) {
        completionHandler.handle(ar.map(event -> io.vertx.lang.groovy.RetroCompatExtension.wrap(event)));
      }
    } : null);
  }
  public static <T>java.lang.Object get(io.vertx.ext.unit.TestContext j_receiver, java.lang.String key) {
    return io.vertx.lang.groovy.RetroCompatExtension.wrap(j_receiver.get(key));
  }
  public static <T>java.lang.Object put(io.vertx.ext.unit.TestContext j_receiver, java.lang.String key, java.lang.Object value) {
    return io.vertx.lang.groovy.RetroCompatExtension.wrap(j_receiver.put(key,
      io.vertx.lang.groovy.RetroCompatExtension.unwrap(value)));
  }
  public static <T>java.lang.Object remove(io.vertx.ext.unit.TestContext j_receiver, java.lang.String key) {
    return io.vertx.lang.groovy.RetroCompatExtension.wrap(j_receiver.remove(key));
  }
  public static io.vertx.ext.unit.TestContext assertNull(io.vertx.ext.unit.TestContext j_receiver, java.lang.Object expected) {
    io.vertx.lang.groovy.RetroCompatExtension.wrap(j_receiver.assertNull(io.vertx.lang.groovy.RetroCompatExtension.unwrap(expected)));
    return j_receiver;
  }
  public static io.vertx.ext.unit.TestContext assertNull(io.vertx.ext.unit.TestContext j_receiver, java.lang.Object expected, java.lang.String message) {
    io.vertx.lang.groovy.RetroCompatExtension.wrap(j_receiver.assertNull(io.vertx.lang.groovy.RetroCompatExtension.unwrap(expected),
      message));
    return j_receiver;
  }
  public static io.vertx.ext.unit.TestContext assertNotNull(io.vertx.ext.unit.TestContext j_receiver, java.lang.Object expected) {
    io.vertx.lang.groovy.RetroCompatExtension.wrap(j_receiver.assertNotNull(io.vertx.lang.groovy.RetroCompatExtension.unwrap(expected)));
    return j_receiver;
  }
  public static io.vertx.ext.unit.TestContext assertNotNull(io.vertx.ext.unit.TestContext j_receiver, java.lang.Object expected, java.lang.String message) {
    io.vertx.lang.groovy.RetroCompatExtension.wrap(j_receiver.assertNotNull(io.vertx.lang.groovy.RetroCompatExtension.unwrap(expected),
      message));
    return j_receiver;
  }
  public static io.vertx.ext.unit.TestContext assertEquals(io.vertx.ext.unit.TestContext j_receiver, java.lang.Object expected, java.lang.Object actual) {
    io.vertx.lang.groovy.RetroCompatExtension.wrap(j_receiver.assertEquals(io.vertx.lang.groovy.RetroCompatExtension.unwrap(expected),
      io.vertx.lang.groovy.RetroCompatExtension.unwrap(actual)));
    return j_receiver;
  }
  public static io.vertx.ext.unit.TestContext assertEquals(io.vertx.ext.unit.TestContext j_receiver, java.lang.Object expected, java.lang.Object actual, java.lang.String message) {
    io.vertx.lang.groovy.RetroCompatExtension.wrap(j_receiver.assertEquals(io.vertx.lang.groovy.RetroCompatExtension.unwrap(expected),
      io.vertx.lang.groovy.RetroCompatExtension.unwrap(actual),
      message));
    return j_receiver;
  }
  public static io.vertx.ext.unit.TestContext assertNotEquals(io.vertx.ext.unit.TestContext j_receiver, java.lang.Object first, java.lang.Object second) {
    io.vertx.lang.groovy.RetroCompatExtension.wrap(j_receiver.assertNotEquals(io.vertx.lang.groovy.RetroCompatExtension.unwrap(first),
      io.vertx.lang.groovy.RetroCompatExtension.unwrap(second)));
    return j_receiver;
  }
  public static io.vertx.ext.unit.TestContext assertNotEquals(io.vertx.ext.unit.TestContext j_receiver, java.lang.Object first, java.lang.Object second, java.lang.String message) {
    io.vertx.lang.groovy.RetroCompatExtension.wrap(j_receiver.assertNotEquals(io.vertx.lang.groovy.RetroCompatExtension.unwrap(first),
      io.vertx.lang.groovy.RetroCompatExtension.unwrap(second),
      message));
    return j_receiver;
  }
  public static <T>io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Object>> asyncAssertSuccess(io.vertx.ext.unit.TestContext j_receiver) {
    return io.vertx.lang.groovy.RetroCompatExtension.wrap(j_receiver.asyncAssertSuccess());
  }
  public static <T>io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Object>> asyncAssertSuccess(io.vertx.ext.unit.TestContext j_receiver, io.vertx.core.Handler<java.lang.Object> resultHandler) {
    return io.vertx.lang.groovy.RetroCompatExtension.wrap(j_receiver.asyncAssertSuccess(resultHandler != null ? event -> resultHandler.handle(io.vertx.lang.groovy.RetroCompatExtension.wrap(event)) : null));
  }
  public static <T>io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Object>> asyncAssertFailure(io.vertx.ext.unit.TestContext j_receiver) {
    return io.vertx.lang.groovy.RetroCompatExtension.wrap(j_receiver.asyncAssertFailure());
  }
  public static <T>io.vertx.core.Handler<io.vertx.core.AsyncResult<java.lang.Object>> asyncAssertFailure(io.vertx.ext.unit.TestContext j_receiver, io.vertx.core.Handler<java.lang.Throwable> causeHandler) {
    return io.vertx.lang.groovy.RetroCompatExtension.wrap(j_receiver.asyncAssertFailure(causeHandler != null ? event -> causeHandler.handle(io.vertx.lang.groovy.RetroCompatExtension.wrap(event)) : null));
  }
  public static io.vertx.ext.unit.TestCompletion run(io.vertx.ext.unit.TestSuite j_receiver, java.util.Map<String, Object> options) {
    return io.vertx.lang.groovy.RetroCompatExtension.wrap(j_receiver.run(options != null ? new io.vertx.ext.unit.TestOptions(io.vertx.lang.groovy.RetroCompatExtension.toJsonObject(options)) : null));
  }
  public static io.vertx.ext.unit.TestCompletion run(io.vertx.ext.unit.TestSuite j_receiver, io.vertx.core.Vertx vertx, java.util.Map<String, Object> options) {
    return io.vertx.lang.groovy.RetroCompatExtension.wrap(j_receiver.run(vertx,
      options != null ? new io.vertx.ext.unit.TestOptions(io.vertx.lang.groovy.RetroCompatExtension.toJsonObject(options)) : null));
  }
}
