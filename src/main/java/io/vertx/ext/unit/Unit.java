package io.vertx.ext.unit;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.unit.impl.SuiteDesc;
import io.vertx.ext.unit.impl.TestDesc;

/**
 * The factory for creating module or individual tests.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Unit {

  static SuiteDef suite() {
    return new SuiteDesc();
  }

  static SuiteDef suite(String desc) {
    return new SuiteDesc(desc);
  }

  static TestDef test(String desc, Handler<Test> handler) {
    return new TestDesc(desc, handler);
  }
}
