package io.vertx.kotlin.ext.unit

import io.vertx.ext.unit.TestOptions

fun TestOptions(
    reporters: List<io.vertx.ext.unit.report.ReportOptions>? = null,
  timeout: Long? = null,
  useEventLoop: Boolean? = null): TestOptions = io.vertx.ext.unit.TestOptions().apply {

  if (reporters != null) {
    this.reporters = reporters
  }

  if (timeout != null) {
    this.timeout = timeout
  }

  if (useEventLoop != null) {
    this.isUseEventLoop = useEventLoop
  }

}

