package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.TestSuiteReport;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ConsoleReporter implements Handler<TestSuiteReport> {

  @Override
  public void handle(TestSuiteReport module) {
    AtomicInteger run = new AtomicInteger();
    AtomicInteger failures = new AtomicInteger();
    module.handler(test -> {
      System.out.println("Starting test " + test.name());
      run.incrementAndGet();
      test.endHandler(result -> {
        if (result.succeeded()) {
          System.out.println("Passed " + test.name());
        } else {
          failures.incrementAndGet();
          System.out.println("Failed " + test.name());
          System.out.println(result.failure().stackTrace());
        }
      });
    });
    module.endHandler(v -> {
      System.out.println("Tests run: " + run.get() + ", Failures: " + failures.get());
    });
  }
}
