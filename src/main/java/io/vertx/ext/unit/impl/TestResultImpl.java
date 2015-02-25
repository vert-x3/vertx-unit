package io.vertx.ext.unit.impl;

import io.vertx.ext.unit.report.Failure;
import io.vertx.ext.unit.report.TestResult;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestResultImpl implements TestResult {

  private final String name;
  private final long beginTime;
  private final long durationTime;
  private final Failure failure;

  public TestResultImpl(String name, long beginTime, long durationTime, Failure failure) {
    this.name = name;
    this.beginTime = beginTime;
    this.durationTime = durationTime;
    this.failure = failure;
  }

  public TestResultImpl(String name, long beginTime, long durationTime,  Throwable failure) {
    this(name, beginTime, durationTime, failure != null ? new FailureImpl(failure) : null);
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public long beginTime() {
    return beginTime;
  }

  @Override
  public long durationTime() {
    return durationTime;
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
