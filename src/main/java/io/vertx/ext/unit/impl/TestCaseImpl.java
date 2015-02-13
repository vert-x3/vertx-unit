package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestCase;
import io.vertx.ext.unit.TestCaseRunner;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestCaseImpl implements TestCase {

  final String desc;
  final Handler<Test> handler;

  public TestCaseImpl(String desc, Handler<Test> handler) {
    this.desc = desc;
    this.handler = handler;
  }

  @Override
  public TestCaseRunner runner() {
    return new TestCaseRunnerImpl(desc, null, handler, null, (o, executor) -> {
      // ?
    });
  }
}
