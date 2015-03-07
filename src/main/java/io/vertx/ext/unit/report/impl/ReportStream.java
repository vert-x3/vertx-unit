package io.vertx.ext.unit.report.impl;

import io.vertx.core.buffer.Buffer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface ReportStream {
  
  default void info(Buffer msg) {}

  default void error(Buffer msg, Throwable cause) {}

  default void end() {}
  
}
