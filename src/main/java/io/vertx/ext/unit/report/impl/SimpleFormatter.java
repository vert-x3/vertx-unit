package io.vertx.ext.unit.report.impl;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.unit.report.TestResult;
import io.vertx.ext.unit.report.Reporter;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SimpleFormatter implements Reporter<SimpleFormatter.ReportImpl> {

  private final Consumer<Buffer> info;
  private final BiConsumer<Buffer, Throwable> error;
  private final Handler<Void> endHandler;

  public SimpleFormatter(Consumer<Buffer> info, BiConsumer<Buffer, Throwable> error, Handler<Void> endHandler) {
    this.info = info;
    this.error = error;
    this.endHandler = endHandler;
  }

  public static class ReportImpl {
    private final String name;
    private int run;
    private int failures;
    private int errors;
    public ReportImpl(String name) {
      this.name = name;
    }
  }

  @Override
  public ReportImpl reportBeginTestSuite(String name) {
    ReportImpl report = new ReportImpl(name);
    info.accept(Buffer.buffer("Begin test suite " + name));
    return report;
  }

  @Override
  public void reportBeginTestCase(ReportImpl report, String name) {
    info.accept(Buffer.buffer("Begin test " + name));
    report.run++;
  }

  @Override
  public void reportEndTestCase(ReportImpl report, String name, TestResult result) {
    if (result.succeeded()) {
      info.accept(Buffer.buffer("Passed " + result.name()));
    } else {
      if (result.failure().isError()) {
        report.errors++;
      } else {
        report.failures++;
      }
      error.accept(Buffer.buffer("Failed " + result.name()), result.failure().cause());
    }
  }

  @Override
  public void reportError(ReportImpl report, Throwable err) {
    error.accept(Buffer.buffer("Test suite " + report.name + " failure"), err);
  }

  @Override
  public void reportEndTestSuite(ReportImpl report) {
    String msg = "End test suite " + report.name + " , run: " + report.run + ", Failures: " + report.failures + ", Errors: " + report.errors;
    info.accept(Buffer.buffer(msg));
    if (endHandler != null) {
      endHandler.handle(null);
    }
  }
}
