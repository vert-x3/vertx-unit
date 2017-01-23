package io.vertx.kotlin.ext.unit.report

import io.vertx.ext.unit.report.ReportingOptions

fun ReportingOptions(
    reporters: List<io.vertx.ext.unit.report.ReportOptions>? = null): ReportingOptions = io.vertx.ext.unit.report.ReportingOptions().apply {

  if (reporters != null) {
    this.reporters = reporters
  }

}

