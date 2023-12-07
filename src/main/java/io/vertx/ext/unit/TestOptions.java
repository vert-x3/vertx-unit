package io.vertx.ext.unit;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.json.annotations.JsonGen;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.report.ReportOptions;
import io.vertx.ext.unit.report.ReportingOptions;

import java.util.List;

/**
 * Test execution options:
 *
 * <ul>
 *   <li>the {@code timeout} in milliseconds, the default value is 2 minutes </li>
 *   <li>the {@code useEventLoop}</li> configures the event loop usage
 *     <ul>
 *       <li>{@code true} always runs with an event loop</li>
 *       <li>{@code false} never runs with an event loop</li>
 *       <li>{@code null} uses an event loop if there is one (provided by {@link io.vertx.core.Vertx#currentContext()})
 *       otherwise run without</li>
 *     </ul>
 *   </li>
 *   <li>the {@code reporters} is an array of reporter configurations</li>
 * </ul>
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
@JsonGen(publicConverter = false)
public class TestOptions extends ReportingOptions {

  /**
   * The default time out value in milliseconds: 2 minutes.
   */
  public static final long DEFAULT_TIMEOUT = 2 * 60 * 1000;

  /**
   * The default value for using or not the event loop: {@code null}.
   */
  public static final Boolean DEFAULT_USE_EVENT_LOOP = null;

  private long timeout = DEFAULT_TIMEOUT;
  private Boolean useEventLoop = DEFAULT_USE_EVENT_LOOP;

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
    super(other);
    setTimeout(other.timeout);
    setUseEventLoop(other.useEventLoop);
  }

  /**
   * Create a new options from the specified json.
   *
   * @param json the json to create from
   */
  public TestOptions(JsonObject json) {
    super(json);
    TestOptionsConverter.fromJson(json, this);
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
   * @return true if the execution should use an event loop when there is no one existing
   */
  public Boolean isUseEventLoop() {
    return useEventLoop;
  }

  /**
   * Configure the execution to use an event loop when there is no one existing.
   *
   * @param useEventLoop the even loop usage
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  public TestOptions setUseEventLoop(Boolean useEventLoop) {
    this.useEventLoop = useEventLoop;
    return this;
  }

  @Override
  public TestOptions addReporter(ReportOptions reportOptions) {
    return (TestOptions) super.addReporter(reportOptions);
  }

  @Override
  public TestOptions setReporters(List<ReportOptions> reporters) {
    return (TestOptions) super.setReporters(reporters);
  }

  /**
   * @return the json modelling the current configuration
   */
  public JsonObject toJson() {
    JsonObject json = super.toJson();
    TestOptionsConverter.toJson(this, json);
    return json;
  }
}
