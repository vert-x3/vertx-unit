package io.vertx.ext.unit.report;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestResult;
import io.vertx.ext.unit.TestSuiteReport;
import io.vertx.ext.unit.report.impl.DefaultReporterFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface Reporter<R> {

  static final ReporterFactory factory = new DefaultReporterFactory();

  static Reporter<?> reporter(Vertx vertx, ReportOptions options) {
    return factory.reporter(vertx, options);
  }

  R createReport();

  void reportBeginTestSuite(R report, String name);

  void reportBeginTestCase(R report, String name);

  void reportEndTestCase(R report, TestResult result);

  void reportEndTestSuite(R report, String name, Throwable err);

/*
  default Handler<TestSuiteReport> asHandler() {
    return testsuite -> {
      R report = createReport();
      reportBeginTestSuite(report, testsuite.name());
      testsuite.handler(testcase -> {
        reportBeginTestCase(report, testcase.name());
        testcase.endHandler(result -> reportEndTestCase(report, result));
      });
      AtomicReference<Throwable> err = new AtomicReference<>();
      testsuite.exceptionHandler(err::set);
      testsuite.endHandler(v -> {
        reportEndTestSuite(report, testsuite.name(), err.get());
      });
    };
  }
*/
}
