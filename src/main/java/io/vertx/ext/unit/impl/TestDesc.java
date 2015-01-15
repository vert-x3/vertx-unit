package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.Test;
import io.vertx.ext.unit.TestExec;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class TestDesc {

  final ModuleDesc module;
  final String desc;
  final Handler<Test> handler;

  TestDesc(ModuleDesc module, String desc, Handler<Test> handler) {
    this.module = module;
    this.desc = desc;
    this.handler = handler;
  }

  Runnable exec(Handler<TestExec> handler, Runnable next) {
    return new TestExecImpl(this, handler, next);
  }
}
