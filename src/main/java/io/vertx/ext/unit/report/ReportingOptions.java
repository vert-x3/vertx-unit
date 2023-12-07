package io.vertx.ext.unit.report;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.json.annotations.JsonGen;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Reporting options:
 *
 * <ul>
 *   <li>the {@code reporters} is an array of reporter configurations</li>
 * </ul>
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
@JsonGen(publicConverter = false)
public class ReportingOptions {

  private List<ReportOptions> reporters = new ArrayList<>();

  /**
   * Create a new empty options, with the default address out and no reporters.
   */
  public ReportingOptions() {
  }

  /**
   * Copy constructor.
   *
   * @param other the options to copy
   */
  public ReportingOptions(ReportingOptions other) {
    other.reporters.stream().
        map(ReportOptions::new).
        forEach(reporters::add);
  }

  /**
   * Create a new options from the specified json.
   *
   * @param json the json to create from
   */
  public ReportingOptions(JsonObject json) {
    ReportingOptionsConverter.fromJson(json, this);
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
  public ReportingOptions addReporter(ReportOptions reportOptions) {
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
  public ReportingOptions setReporters(List<ReportOptions> reporters) {
    this.reporters = reporters;
    return this;
  }

  /**
   * @return the json modelling the current configuration
   */
  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    ReportingOptionsConverter.toJson(this, json);
    return json;
  }
}
