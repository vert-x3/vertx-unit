package examples.junit;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.Repeat;
import io.vertx.ext.unit.junit.RepeatRule;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
@Repeat(10)
public class RepeatingTest {

  @Rule
  public RepeatRule rule = new RepeatRule();

  @Repeat(value = 1000, silent = true)
  @Test
  public void testSomethingWithSpecificRepeat(final TestContext context) {
    // This will be executed 1000 times in silent mode
  }
  
  public void testSomethingWithDefaultConfig(final TestContext context) {
      // This will be executed 10 times with a message per iteration
  }
}
