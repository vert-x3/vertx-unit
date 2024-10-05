package io.vertx.ext.unit.tests;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestSuite;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

public class ConcurrentTest extends VertxTestBase {

  @Test
  public void testAsyncCompletion() {
    for (int i = 0;i < 128;i++) {
      TestSuite suite = TestSuite.create("the-suite").test("the-test", ctx -> {
        // Try to create an async asynchronously
        // this might succeed or not depending on race agains the testsuite runner thread
        // which is fine, but we want to check that when it wins the race there
        // are no deadlocks (which is why we repeat this test several times)
        new Thread(() -> {
          Async async = ctx.async();
          sleepQuietly(10);
          async.complete();
          sleepQuietly(10);
        }).start();
        sleepQuietly(10);
      });
      suite.run().await(1000);
    }
  }

  private static void sleepQuietly(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
