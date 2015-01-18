package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestDef;
import io.vertx.ext.unit.TestRunner;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestDesc implements TestDef {

  final String desc;
  final Handler<Test> handler;

  public TestDesc(String desc, Handler<Test> handler) {
    this.desc = desc;
    this.handler = handler;
  }

  @Override
  public TestRunner runner() {
    return new TestRunnerImpl(desc, null, handler, null, (o, executor) -> {
      // ?
    });
  }
}
