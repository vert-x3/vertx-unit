package io.vertx.ext.unit.impl;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class Result {

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
}
