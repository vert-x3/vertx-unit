package io.vertx.ext.unit.impl;

import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Result {

  final Map<String, Object> attributes;
  final long beginTime;
  final long endTime;
  final Throwable failure;

  Result(Map<String, Object> attributes, long beginTime, long endTime, Throwable failure) {
    this.attributes = attributes;
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
