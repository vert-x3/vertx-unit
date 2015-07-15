package examples.junit;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class JUnitTestSuite {
  @Test
  public void testSomething(TestContext context) {
    context.assertFalse(false);
  }
}
