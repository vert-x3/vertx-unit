package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.ModuleExec;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ConsoleReporter implements Handler<ModuleExec> {

  @Override
  public void handle(ModuleExec module) {
    AtomicInteger run = new AtomicInteger();
    AtomicInteger failures = new AtomicInteger();
    module.handler(test -> {
      System.out.println("Starting test " + test.description());
      run.incrementAndGet();
      test.completionHandler(result -> {
        if (result.succeeded()) {
          System.out.println("Passed " + test.description());
        } else {
          failures.incrementAndGet();
          System.out.println("Failed " + test.description());
          result.failure().printStackTrace();
        }
      });
    });
    module.endHandler(v -> {
      System.out.println("Tests run: " + run.get() + ", Failures: " + failures.get());
    });
  }
}
