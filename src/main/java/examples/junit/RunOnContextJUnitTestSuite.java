package examples.junit;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class RunOnContextJUnitTestSuite {

  @Rule
  public RunTestOnContext rule = new RunTestOnContext();

  @Test
  public void testSomething(TestContext context) {
    // Use the underlying vertx instance
    Vertx vertx = rule.vertx();
  }
}
