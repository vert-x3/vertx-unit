package io.vertx.ext.unit.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.unit.TestSuiteReport;
import io.vertx.ext.unit.report.Reporter;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReporterHandler implements Handler<TestSuiteReport> {

  private final Reporter[] reporters;
  private final Future completion;

  public ReporterHandler(Reporter... reporters) {
    this(null, reporters);
  }

  public ReporterHandler(Future completion,
                         Reporter... reporters) {
    this.completion = completion;
    this.reporters = reporters;
  }

  @Override
  public void handle(TestSuiteReport testsuite) {
    AtomicReference<Throwable> global = new AtomicReference<>();
    Object[] reports = new Object[reporters.length];
    for (int i = 0;i < reporters.length;i++) {
      reports[i] = reporters[i].createReport();
      reporters[i].reportBeginTestSuite(reports[i], testsuite.name());
    }
    testsuite.handler(testcase -> {
      for (int i = 0;i < reporters.length;i++) {
        reporters[i].reportBeginTestCase(reports[i], testcase.name());
      }
      testcase.endHandler(result -> {
        if (result.failed()) {
          global.compareAndSet(null, result.failure().cause());
        }
        for (int i = 0;i < reporters.length;i++) {
          reporters[i].reportEndTestCase(reports[i], result);
        }
      });
    });
    AtomicReference<Throwable> err = new AtomicReference<>();
    testsuite.exceptionHandler(t -> {
      global.compareAndSet(null, t);
      err.set(t);
    });
    testsuite.endHandler(v -> {
      for (int i = 0;i < reporters.length;i++) {
        reporters[i].reportEndTestSuite(reports[i], testsuite.name(), err.get());
      }
      if (completion != null) {
        if (global.get() == null) {
          completion.complete();
        } else {
          completion.fail(global.get());
        }
      }
    });
  }
}
