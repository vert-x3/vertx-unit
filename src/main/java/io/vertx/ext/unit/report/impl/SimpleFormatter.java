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
    private int run;
    private int failures;
    private int errors;
  }

  @Override
  public ReportImpl createReport() {
    return new ReportImpl();
  }

  @Override
  public void reportBeginTestSuite(ReportImpl report, String name) {
    info.accept(Buffer.buffer("Begin test suite " + name));
  }

  @Override
  public void reportBeginTestCase(ReportImpl report, String name) {
    info.accept(Buffer.buffer("Begin test " + name));
    report.run++;
  }

  @Override
  public void reportEndTestCase(ReportImpl report, TestResult result) {
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
  public void reportEndTestSuite(ReportImpl report, String name, Throwable err) {
    if (err != null) {
      error.accept(Buffer.buffer("Test suite " + name + " failure"), err);
    }
    String msg = "End test suite " + name + " , run: " + report.run + ", Failures: " + report.failures + ", Errors: " + report.errors;
    info.accept(Buffer.buffer(msg));
    if (endHandler != null) {
      endHandler.handle(null);
    }
  }
}
