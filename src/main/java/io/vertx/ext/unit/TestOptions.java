package io.vertx.ext.unit;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.report.ReportOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Test configuration options.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class TestOptions {

  public static final long DEFAULT_TIMEOUT = 2000;

  private long timeout = DEFAULT_TIMEOUT;
  private List<ReportOptions> reporters = new ArrayList<>();

  public TestOptions() {
  }

  public TestOptions(TestOptions other) {
    other.reporters.stream().map(ReportOptions::new).forEach(reporters::add);
  }

  public TestOptions(JsonObject json) {
    JsonArray reportersJson = json.getJsonArray("reporters");
    if (reportersJson != null) {
      for (int i = 0;i < reportersJson.size();i++) {
        JsonObject reportJson = reportersJson.getJsonObject(i);
        reporters.add(new ReportOptions(reportJson));
      }
    }
    setTimeout(json.getLong("timeout", DEFAULT_TIMEOUT));
  }

  public long getTimeout() {
    return timeout;
  }

  @Fluent
  public TestOptions setTimeout(long timeout) {
    if (timeout < 0) {
      throw new IllegalArgumentException("Timeout cannot be zero or less");
    }
    this.timeout = timeout;
    return this;
  }

  public List<ReportOptions> getReporters() {
    return reporters;
  }

  @Fluent
  public TestOptions addReporter(ReportOptions reportOptions) {
    reporters.add(reportOptions);
    return this;
  }

  @Fluent
  public TestOptions setReporters(List<ReportOptions> reporters) {
    this.reporters = reporters;
    return this;
  }

  public JsonObject toJson() {
    JsonObject config = new JsonObject();
    if (reporters.size() > 0) {
      JsonArray reportersJson = new JsonArray();
      reporters.stream().map(ReportOptions::toJson).forEach(reportersJson::add);
      config.put("reporters", reportersJson);
    }
    config.put("timeout", timeout);
    return config;
  }
}
