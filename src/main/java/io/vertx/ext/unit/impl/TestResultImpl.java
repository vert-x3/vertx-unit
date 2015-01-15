package io.vertx.ext.unit.impl;

import io.vertx.ext.unit.TestResult;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class TestResultImpl implements TestResult {

  final String description;
  final long time;
  final Throwable failure;

  TestResultImpl(String description, long time, Throwable failure) {
    this.description = description;
    this.time = time;
    this.failure = failure;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public long time() {
    return time;
  }

  @Override
  public Throwable failure() {
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
