package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.TestSuiteReport;
import io.vertx.ext.unit.report.Reporter;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReporterHandler implements Handler<TestSuiteReport> {

  private final Reporter[] reporters;

  public ReporterHandler(Reporter... reporters) {
    this.reporters = reporters;
  }

  @Override
  public void handle(TestSuiteReport testsuite) {
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
        for (int i = 0;i < reporters.length;i++) {
          reporters[i].reportEndTestCase(reports[i], result);
        }
      });
    });
    AtomicReference<Throwable> err = new AtomicReference<>();
    testsuite.exceptionHandler(err::set);
    testsuite.endHandler(v -> {
      for (int i = 0;i < reporters.length;i++) {
        reporters[i].reportEndTestSuite(reports[i], testsuite.name(), err.get());
      }
    });
  }
}
