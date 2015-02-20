package io.vertx.ext.unit;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.report.ReportOptions;
import io.vertx.test.core.TestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class OptionsTest {

  @org.junit.Test
  public void testTestOptions() {
    TestOptions options = new TestOptions();
    assertEquals(TestOptions.DEFAULT_TIMEOUT, options.getTimeout());
    assertEquals(Collections.<ReportOptions>emptyList(), options.getReporters());
    long timeout = TestUtils.randomLong();
    assertSame(options, options.setTimeout(timeout));
    assertEquals(timeout, options.getTimeout());
    List<ReportOptions> reporters = new ArrayList<>();
    ReportOptions reporter1 = new ReportOptions();
    reporters.add(reporter1);
    assertSame(options, options.setReporters(reporters));
    assertEquals(reporters, options.getReporters());
    ReportOptions reporter2 = new ReportOptions();
    assertSame(options, options.addReporter(reporter2));
    assertEquals(reporters, options.getReporters());
    assertEquals(2, reporters.size());
    assertEquals(Arrays.asList(reporter1, reporter2), reporters);
  }

  @org.junit.Test
  public void testReportOptions() {
    ReportOptions options = new ReportOptions();
    assertEquals(ReportOptions.DEFAULT_TO, options.getTo());
    assertEquals(ReportOptions.DEFAULT_FORMAT, options.getFormat());
    assertEquals(null, options.getAt());
    String to = TestUtils.randomAlphaString(10);
    assertSame(options, options.setTo(to));
    assertEquals(to, options.getTo());
    String at = TestUtils.randomAlphaString(10);
    assertSame(options, options.setAt(at));
    assertEquals(at, options.getAt());
    String format = TestUtils.randomAlphaString(10);
    assertSame(options, options.setFormat(format));
    assertEquals(format, options.getFormat());
  }

  @org.junit.Test
  public void testCopyOptions() {
    TestOptions options = new TestOptions();
    long timeout = TestUtils.randomLong();
    String to = TestUtils.randomAlphaString(10);
    String at = TestUtils.randomAlphaString(10);
    String format = TestUtils.randomAlphaString(10);
    ReportOptions reporter = new ReportOptions().setTo(to).setAt(at).setFormat(format);
    options.setTimeout(timeout).addReporter(reporter);
    TestOptions copy = new TestOptions(options);
    options.setTimeout(TestUtils.randomLong());
    reporter.setTo(TestUtils.randomAlphaString(10));
    reporter.setAt(TestUtils.randomAlphaString(10));
    reporter.setFormat(TestUtils.randomAlphaString(10));
    options.getReporters().clear();
    assertEquals(timeout, copy.getTimeout());
    assertEquals(1, copy.getReporters().size());
    assertEquals(at, copy.getReporters().get(0).getAt());
    assertEquals(to, copy.getReporters().get(0).getTo());
    assertEquals(format, copy.getReporters().get(0).getFormat());
  }

  @org.junit.Test
  public void testDefaultJsonTestOptions() {
    TestOptions def = new TestOptions();
    TestOptions json = new TestOptions(new JsonObject());
    assertEquals(json.getTimeout(), def.getTimeout());
    assertEquals(json.getReporters(), def.getReporters());
  }

  @org.junit.Test
  public void testDefaultJsonReportOptions() {
    ReportOptions def = new ReportOptions();
    ReportOptions json = new ReportOptions(new JsonObject());
    assertEquals(json.getTo(), def.getTo());
    assertEquals(json.getAt(), def.getAt());
    assertEquals(json.getFormat(), def.getFormat());
  }

  @org.junit.Test
  public void testJsonOptions() {
    JsonObject json = new JsonObject();
    long timeout = TestUtils.randomLong();
    String to = TestUtils.randomAlphaString(10);
    String at = TestUtils.randomAlphaString(10);
    String format = TestUtils.randomAlphaString(10);
    json.put("timeout", timeout);
    json.put("reporters", new JsonArray().
        add(new JsonObject().
            put("to", to).
            put("at", at).
            put("format", format)));
    TestOptions options = new TestOptions(json);
    assertEquals(timeout, options.getTimeout());
    assertEquals(1, options.getReporters().size());
    assertEquals(at, options.getReporters().get(0).getAt());
    assertEquals(to, options.getReporters().get(0).getTo());
    assertEquals(format, options.getReporters().get(0).getFormat());
  }
}
