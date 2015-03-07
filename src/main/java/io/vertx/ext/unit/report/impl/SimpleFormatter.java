package io.vertx.ext.unit.report.impl;

import io.vertx.core.buffer.Buffer;
import io.vertx.ext.unit.report.TestResult;
import io.vertx.ext.unit.report.Reporter;

import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SimpleFormatter implements Reporter<SimpleFormatter.ReportImpl> {

  private final String sep = System.lineSeparator();
  private final Function<String, ReportStream> streamFactory;

  public SimpleFormatter(Function<String, ReportStream> streamFactory) {
    this.streamFactory = streamFactory;
  }

  public static class ReportImpl {
    private final ReportStream stream;
    private final String name;
    private int run;
    private int failures;
    private int errors;
    public ReportImpl(ReportStream stream, String name) {
      this.stream = stream;
      this.name = name;
    }
  }

  @Override
  public ReportImpl reportBeginTestSuite(String name) {
    ReportImpl report = new ReportImpl(streamFactory.apply(name), name);
    report.stream.info(Buffer.buffer("Begin test suite " + name + sep));
    return report;
  }

  @Override
  public void reportBeginTestCase(ReportImpl report, String name) {
    report.stream.info(Buffer.buffer("Begin test " + name +  sep));
    report.run++;
  }

  @Override
  public void reportEndTestCase(ReportImpl report, String name, TestResult result) {
    if (result.succeeded()) {
      report.stream.info(Buffer.buffer("Passed " + result.name() +  sep));
    } else {
      if (result.failure().isError()) {
        report.errors++;
      } else {
        report.failures++;
      }
      report.stream.error(Buffer.buffer("Failed " + result.name() +  sep), result.failure().cause());
    }
  }

  @Override
  public void reportError(ReportImpl report, Throwable err) {
    report.stream.error(Buffer.buffer("Test suite " + report.name + " failure" + sep), err);
  }

  @Override
  public void reportEndTestSuite(ReportImpl report) {
    String msg = "End test suite " + report.name + " , run: " + report.run + ", Failures: " + report.failures +
        ", Errors: " + report.errors + sep;
    report.stream.info(Buffer.buffer(msg));
    report.stream.end();
  }
}
