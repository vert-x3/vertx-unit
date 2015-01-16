package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.Test;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class TestDesc {

  final SuiteDesc module;
  final String desc;
  final Handler<Test> handler;

  TestDesc(SuiteDesc module, String desc, Handler<Test> handler) {
    this.module = module;
    this.desc = desc;
    this.handler = handler;
  }
}
