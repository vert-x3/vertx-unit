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
  private final Handler<AsyncResult<Void>> completionHandler;

  public ReporterHandler(Reporter... reporters) {
    this(null, reporters);
  }

  public ReporterHandler(Handler<AsyncResult<Void>> completionHandler,
                         Reporter... reporters) {
    this.completionHandler = completionHandler;
    this.reporters = reporters;
  }

  @Override
  public void handle(TestSuiteReport testsuite) {
    Future<Void> future = Future.future();
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
        if (result.failed() && !future.isComplete()) {
          future.fail(result.failure().cause());
        }
        for (int i = 0;i < reporters.length;i++) {
          reporters[i].reportEndTestCase(reports[i], result);
        }
      });
    });
    AtomicReference<Throwable> err = new AtomicReference<>();
    testsuite.exceptionHandler(t -> {
      if (!future.isComplete()) {
        future.fail(t);
      }
      err.set(t);
    });
    testsuite.endHandler(v -> {
      for (int i = 0;i < reporters.length;i++) {
        reporters[i].reportEndTestSuite(reports[i], testsuite.name(), err.get());
      }
      if (completionHandler != null) {
        if (!future.isComplete()) {
          future.complete();
        }
        completionHandler.handle(future);
      }
    });
  }
}
