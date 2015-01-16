package io.vertx.ext.unit;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.unit.impl.SuiteDesc;

/**
 * The factory for creating module or individual tests.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Unit {

  static Suite suite() {
    return new SuiteDesc();
  }

  static Suite suite(String desc) {
    return new SuiteDesc(desc);
  }

  static Suite test(String desc, Handler<Test> handler) {
    return suite(null).test(desc, handler);
  }
}
