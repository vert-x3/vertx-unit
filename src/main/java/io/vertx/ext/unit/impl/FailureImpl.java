package io.vertx.ext.unit.impl;

import io.vertx.ext.unit.Failure;

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

  public FailureImpl(boolean error, String message, String stackTrace) {
    this.error = error;
    this.message = message;
    this.stackTrace = stackTrace;
    this.cause = null;
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
}
