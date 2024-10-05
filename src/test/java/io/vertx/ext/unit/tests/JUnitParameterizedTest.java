package io.vertx.ext.unit.tests;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunnerWithParametersFactory;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JUnitParameterizedTest {

  @RunWith(Parameterized.class)
  @Parameterized.UseParametersRunnerFactory(VertxUnitRunnerWithParametersFactory.class)
  public static class SimpleParameterizedTestSuite {

    @Parameterized.Parameters
    public static Iterable<Boolean> data() {
      return Arrays.asList(false, true);
    }

    static final ConcurrentLinkedDeque params = new ConcurrentLinkedDeque();
    private boolean pass;

    public SimpleParameterizedTestSuite(boolean pass) {
      params.add(pass);
      this.pass = pass;
    }

    @Test
    public void theTest(TestContext context) {
      Async async = context.async();
      new Thread() {
        @Override
        public void run() {
          try {
            Thread.sleep(200);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          } finally {
            if (pass) {
              async.complete();
            } else {
              context.fail();
            }
          }
        }
      }.start();
    }
  }

  @org.junit.Test
  public void testSuiteRun() {
    Result result = run(SimpleParameterizedTestSuite.class);
    assertEquals(Arrays.asList(false, true), new ArrayList<>(SimpleParameterizedTestSuite.params));
    assertEquals(2, result.getRunCount());
    assertEquals(1, result.getFailureCount());
  }

  static Result run(Class<?> testClass) {
    try {
      return new JUnitCore().run(new Parameterized(testClass));
    } catch (Throwable initializationError) {
      throw new AssertionError(initializationError);
    }
  }
}
