package io.vertx.ext.unit.impl;

import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Result {

  final long beginTime;
  final long endTime;
  final Throwable failure;

  Result(long beginTime, long endTime, Throwable failure) {
    this.beginTime = beginTime;
    this.endTime = endTime;
    this.failure = failure;
  }

  long duration() {
    return endTime - beginTime;
  }

  public Throwable getFailure() {
    return failure;
  }
}
