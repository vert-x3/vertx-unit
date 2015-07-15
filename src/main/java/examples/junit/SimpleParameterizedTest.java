package examples.junit;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunnerWithParametersFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(Parameterized.class)
@Parameterized.UseParametersRunnerFactory(VertxUnitRunnerWithParametersFactory.class)
public class SimpleParameterizedTest {

  @Parameterized.Parameters
  public static Iterable<Integer> data() {
    return Arrays.asList(0, 1, 2);
  }

  public SimpleParameterizedTest(int value) {
    //...
  }

  @Test
  public void testSomething(TestContext context) {
    // Execute test with the current value
  }
}
