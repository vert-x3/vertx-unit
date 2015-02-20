package io.vertx.ext.unit;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.report.ReportOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Test execution options:
 *
 * <ul>
 *   <li>the {@code timeout} in milliseconds, the default value is 2 minutes </li>
 *   <li>the {@code reporters} is an array of reporter configurations</li>
 * </ul>
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class TestOptions {

  public static final long DEFAULT_TIMEOUT = 2 * 60 * 1000;

  private long timeout = DEFAULT_TIMEOUT;
  private List<ReportOptions> reporters = new ArrayList<>();

  /**
   * Create a new empty options, with the default time out and no reporters.
   */
  public TestOptions() {
  }

  /**
   * Copy constructor.
   *
   * @param other the options to copy
   */
  public TestOptions(TestOptions other) {
    setTimeout(other.timeout);
    other.reporters.stream().
        map(ReportOptions::new).
        forEach(reporters::add);
  }

  /**
   * Create a new options from the specified json.
   *
   * @param json the json to create from
   */
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

  /**
   * @return the current timeout in milliseconds.
   */
  public long getTimeout() {
    return timeout;
  }

  /**
   * Set the test timeout.
   *
   * @param timeout the timeout value in milliseconds.
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  public TestOptions setTimeout(long timeout) {
    this.timeout = timeout;
    return this;
  }

  /**
   * @return the current reporters options
   */
  public List<ReportOptions> getReporters() {
    return reporters;
  }

  /**
   * Add a reporter to the current list.
   *
   * @param reportOptions the options of the reporter to use
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  public TestOptions addReporter(ReportOptions reportOptions) {
    reporters.add(reportOptions);
    return this;
  }

  /**
   * Replace the current list of reporters with a new one.
   *
   * @param reporters the new reporters
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  public TestOptions setReporters(List<ReportOptions> reporters) {
    this.reporters = reporters;
    return this;
  }

  /**
   * @return the json modelling the current configuration
   */
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
