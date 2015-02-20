package io.vertx.ext.unit.report;

import io.vertx.core.Vertx;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface ReporterFactory {

  Reporter reporter(Vertx vertx, ReportOptions options);

}
