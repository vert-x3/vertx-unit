package io.vertx.test.unit

import io.vertx.groovy.ext.unit.Async
import io.vertx.groovy.ext.unit.TestContext;
import io.vertx.groovy.ext.unit.junit.VertxUnitRunnerWithParametersFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(Parameterized.class)
@Parameterized.UseParametersRunnerFactory(VertxUnitRunnerWithParametersFactory.class)
public class GroovyJUnitParameterizedTest {

  @Parameterized.Parameters
  public static Iterable<Integer> data() {
    return Arrays.asList(50, 100);
  }

  private final Integer time;

  public GroovyJUnitParameterizedTest(Integer time) {
    this.time = time;
  }

  @Test
  public void theTest(TestContext context) {
    Async async = context.async();
    new Thread() {
      @Override
      public void run() {
        try {
          Thread.sleep(time);
        } catch (InterruptedException e) {
          e.printStackTrace();
        } finally {
          async.complete();
        }
      }
    }.run();
  }
}
