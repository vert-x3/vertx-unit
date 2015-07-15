package examples.junit;

import io.vertx.ext.unit.TestContext;

public class MyTestSuite {

  public void testSomething(TestContext context) {
    context.assertFalse(false);
  }
}