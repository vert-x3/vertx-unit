package io.vertx.ext.unit.report;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.report.impl.DefaultReporterFactory;

/**
 * The reporter defines a set of callback for the life cycle events.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 * @param <R> the report generic type
 */
public interface Reporter<R> {

  static final ReporterFactory factory = new DefaultReporterFactory();

  static Reporter<?> reporter(Vertx vertx, ReportOptions options) {
    return factory.reporter(vertx, options);
  }

  /**
   * Signals the test suite began.
   *
   * @param name the test suite name
   * @return the report object
   */
  R reportBeginTestSuite(String name);

  /**
   * Signals a test case began.
   *
   * @param report the report
   * @param name the test case name
   */
  void reportBeginTestCase(R report, String name);

  /**
   * Signals a test case ended.
   *
   *  @param report the report
   * @param name the test case name
   * @param result the test case result
   */
  void reportEndTestCase(R report, String name, TestResult result);

  /**
   * Report a global test suite error, it can be called mulitple times between the {@link #reportBeginTestSuite}
   * and the {@link #reportEndTestSuite}.
   *
   * @param report the report
   * @param err the test suite error
   */
  void reportError(R report, Throwable err);

  /**
   * Signals a test suite ended.
   *
   * @param report the report
   */
  void reportEndTestSuite(R report);

}
