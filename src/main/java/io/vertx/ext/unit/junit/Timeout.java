package io.vertx.ext.unit.junit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.TimeUnit;

/**
 * A rule for configuring the tests timeout.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Timeout implements TestRule {

  /**
   * @param millis the timeout value in milli seconds
   * @return a new timeout rule
   */
  public static Timeout millis(long millis) {
    return new Timeout(millis, TimeUnit.MILLISECONDS);
  }

  /**
   * @param seconds the timeout value in seconds
   * @return a new timeout rule
   */
  public static Timeout seconds(long seconds) {
    return new Timeout(seconds, TimeUnit.SECONDS);
  }

  private final long value;

  public Timeout(long value, TimeUnit unit) {
    this.value = unit.toMillis(value);
  }

  @Override
  public Statement apply(Statement base, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        VertxUnitRunner.pushTimeout(value);
        try {
          base.evaluate();
        } finally {
          VertxUnitRunner.popTimeout();
        }
      }
    };
  }
}
