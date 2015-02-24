package io.vertx.ext.unit.impl;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.report.Failure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class FailureImpl implements Failure {

  private final boolean error;
  private final String message;
  private final String stackTrace;
  private final Throwable cause;

  public FailureImpl(JsonObject json) {
    byte[] tmp = json.getBinary("cause");
    Throwable t = null;
    if (tmp != null) {
      try {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(tmp));
        t = (Throwable) ois.readObject();
      } catch (Exception ignore) {
      }
    }
    error = json.getBoolean("error");
    message = json.getString("message");
    stackTrace = json.getString("stackTrace");
    cause = t;
  }

  public FailureImpl(boolean error, String message, String stackTrace, Throwable cause) {
    this.error = error;
    this.message = message;
    this.stackTrace = stackTrace;
    this.cause = cause;
  }

  public FailureImpl(Throwable t) {
    StringWriter buffer = new StringWriter();
    PrintWriter writer = new PrintWriter(buffer);
    t.printStackTrace(writer);
    writer.close();
    error = t instanceof AssertionError ? false : true;
    stackTrace = buffer.toString();
    cause = t;
    message = t.getMessage();
  }

  @Override
  public boolean isError() {
    return error;
  }

  @Override
  public String message() {
    return message;
  }

  @Override
  public String stackTrace() {
    return stackTrace;
  }

  @Override
  public Throwable cause() {
    return cause;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject().
        put("error", error).
        put("message", message).
        put("stackTrace", stackTrace);
    if (cause != null) {
      // Attempt to marshall the cause since it is Serializable
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      try(ObjectOutputStream oos = new ObjectOutputStream(buffer)) {
        oos.flush();
        oos.writeObject(cause);
        json.put("cause", buffer.toByteArray());
      } catch (Exception ignore) {
      }
    }
    return json;
  }
}
