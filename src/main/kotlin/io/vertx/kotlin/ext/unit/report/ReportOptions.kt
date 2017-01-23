package io.vertx.kotlin.ext.unit.report

import io.vertx.ext.unit.report.ReportOptions

fun ReportOptions(
    format: String? = null,
  to: String? = null): ReportOptions = io.vertx.ext.unit.report.ReportOptions().apply {

  if (format != null) {
    this.format = format
  }

  if (to != null) {
    this.to = to
  }

}

