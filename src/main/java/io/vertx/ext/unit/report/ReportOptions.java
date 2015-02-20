package io.vertx.ext.unit.report;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;

/**
 * Configures a reporter consisting in a name {@code to}, an address {@code at} and an optional {@code format}.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class ReportOptions {

  /**
   * The {@code console} is the default {@code to} value.
   */
  public static final String DEFAULT_TO = "console";

  /**
   * The {@code simple} format is the default {@code format} value.
   */
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
    to = json.getString("to", DEFAULT_TO);
    at = json.getString("at");
    format = json.getString("format", DEFAULT_FORMAT);
  }

  /**
   * @return the current reporter name
   */
  public String getTo() {
    return to;
  }

  /**
   * Set the current reporter name.
   *
   * @param to the new reporter name
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  public ReportOptions setTo(String to) {
    this.to = to;
    return this;
  }

  /**
   * @return the current reporter address
   */
  public String getAt() {
    return at;
  }

  /**
   * Set the current reporter address
   *
   * @param at the address
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  public ReportOptions setAt(String at) {
    this.at = at;
    return this;
  }

  /**
   * @return the current reporter format
   */
  public String getFormat() {
    return format;
  }

  /**
   * Set the current reporter format.
   *
   * @param format the format
   * @return a reference to this, so the API can be used fluently
a   */
  @Fluent
  public ReportOptions setFormat(String format) {
    this.format = format;
    return this;
  }

  public JsonObject toJson() {
    return new JsonObject().put("to", to).put("at", at).put("format", format);
  }
}
