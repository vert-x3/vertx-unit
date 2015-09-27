package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.TestCompletion;
import io.vertx.ext.unit.report.TestSuiteReport;
import io.vertx.ext.unit.report.Reporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestCompletionImpl extends CompletionImpl<Void> implements TestCompletion, Handler<TestSuiteReport> {

  private final AtomicReference<TestSuiteReport> report = new AtomicReference<>();
  private final List<Reporter> reporters = Collections.synchronizedList(new ArrayList<>());
  private final AtomicReference<Throwable> failure = new AtomicReference<>();

  public TestCompletionImpl(Reporter... reporters) {
    Collections.addAll(this.reporters, reporters);
  }

  public void addReporter(Reporter reporter) {
    reporters.add(reporter);
  }

  @Override
  public void handle(TestSuiteReport report) {
    Reporter[] reporters = this.reporters.toArray(new Reporter[this.reporters.size()]);
    Object[] reports = new Object[reporters.length];
    for (int i = 0;i < reporters.length;i++) {
      reports[i] = reporters[i].reportBeginTestSuite(report.name());
    }
    report.handler(testcase -> {
      for (int i = 0; i < reporters.length; i++) {
        reporters[i].reportBeginTestCase(reports[i], testcase.name());
      }
      testcase.endHandler(result -> {
        if (result.failed()) {
          failure.compareAndSet(null, result.failure().cause());
        }
        for (int i = 0; i < reporters.length; i++) {
          reporters[i].reportEndTestCase(reports[i], testcase.name(), result);
        }
      });
    });
    AtomicReference<Throwable> err = new AtomicReference<>();
    report.exceptionHandler(t -> {
      failure.compareAndSet(null, t);
      err.set(t);
      for (int i = 0; i < reporters.length; i++) {
        reporters[i].reportError(reports[i], t);
      }
    });
    report.endHandler(v -> {
      this.report.set(report);
      for (int i = 0; i < reporters.length; i++) {
        reporters[i].reportEndTestSuite(reports[i]);
      }
      if (failure.get() != null) {
        completable.completeExceptionally(failure.get());
      } else {
        completable.complete(null);
      }
    });
  }
}
