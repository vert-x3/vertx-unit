package io.vertx.ext.unit.impl;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class Result {

  final long time;
  final Throwable failure;

  public Result(long time, Throwable failure) {
    this.time = time;
    this.failure = failure;
  }
}
