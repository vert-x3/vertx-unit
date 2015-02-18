package io.vertx.ext.unit.report;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class ReportOptions {

  public static final String DEFAULT_TO = "console";
  public static final String DEFAULT_FORMAT = "simple";

  private String to = DEFAULT_TO;
  private String at;
  private String format = DEFAULT_FORMAT;

  public ReportOptions() {
  }

  public ReportOptions(ReportOptions other) {
    to = other.to;
    at = other.at;
    format = other.format;
  }

  public ReportOptions(JsonObject json) {
    to = json.getString("to");
    at = json.getString("at");
    format = json.getString("format");
  }

  public String getTo() {
    return to;
  }

  @Fluent
  public ReportOptions setTo(String to) {
    this.to = to;
    return this;
  }

  public String getAt() {
    return at;
  }

  @Fluent
  public ReportOptions setAt(String at) {
    this.at = at;
    return this;
  }

  public String getFormat() {
    return format;
  }

  @Fluent
  public ReportOptions setFormat(String format) {
    this.format = format;
    return this;
  }

  public JsonObject toJson() {
    return new JsonObject().put("to", to).put("at", at).put("format", format);
  }
}
