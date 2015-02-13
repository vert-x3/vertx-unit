package io.vertx.ext.unit;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.unit.impl.TestSuiteImpl;
import io.vertx.ext.unit.impl.TestCaseImpl;

/**
 * The factory for creating module or individual tests.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Unit {

  static TestSuite suite() {
    return new TestSuiteImpl();
  }

  static TestSuite suite(String desc) {
    return new TestSuiteImpl(desc);
  }

  static TestCase test(String desc, Handler<Test> handler) {
    return new TestCaseImpl(desc, handler);
  }
}
