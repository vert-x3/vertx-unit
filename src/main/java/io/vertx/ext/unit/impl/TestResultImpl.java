package io.vertx.ext.unit.impl;

import io.vertx.ext.unit.Failure;
import io.vertx.ext.unit.report.TestResult;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestResultImpl implements TestResult {

  private final String name;
  private final long time;
  private final Failure failure;

  public TestResultImpl(String name, long time, Failure failure) {
    this.name = name;
    this.time = time;
    this.failure = failure;
  }

  public TestResultImpl(String name, long time, Throwable failure) {
    this(name, time, failure != null ? new FailureImpl(failure) : null);
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public long time() {
    return time;
  }

  @Override
  public Failure failure() {
    return failure;
  }

  @Override
  public boolean succeeded() {
    return failure == null;
  }

  @Override
  public boolean failed() {
    return failure != null;
  }
}
