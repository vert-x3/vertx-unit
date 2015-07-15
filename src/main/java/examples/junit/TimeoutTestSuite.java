package examples.junit;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.Timeout;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class TimeoutTestSuite {

  @Rule
  public Timeout rule = Timeout.seconds(1);

  @Test
  public void testSomething(TestContext context) {
    //...
  }
}
