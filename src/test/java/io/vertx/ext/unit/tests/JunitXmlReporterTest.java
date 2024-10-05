package io.vertx.ext.unit.tests;

import io.vertx.core.buffer.Buffer;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.impl.TestCompletionImpl;
import io.vertx.ext.unit.impl.TestSuiteImpl;
import io.vertx.ext.unit.report.impl.JunitXmlFormatter;
import io.vertx.ext.unit.report.impl.ReportStream;
import io.vertx.test.core.AsyncTestBase;
import io.vertx.test.core.TestUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JunitXmlReporterTest extends AsyncTestBase {

  private final NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
  private String name;
  private Document doc;
  private CountDownLatch latch;

  private ReportStream reportTo(String name) {
    this.name = name;
    return new ReportStream() {
      private Buffer buf = Buffer.buffer();
      @Override
      public void info(Buffer msg) {
        buf.appendBuffer(msg);
      }
      @Override
      public void end() {
        latch.countDown();
        try {
          DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
          doc = builder.parse(new ByteArrayInputStream(buf.getBytes()));
        } catch (Exception e) {
          fail(e.getMessage());
        }
      }
    };
  }

  private double parseTime(String value) {
    try {
      return format.parse(value).doubleValue();
    } catch (ParseException e) {
      fail(e.getMessage());
      return 0;
    }
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    name = null;
    doc = null;
    latch = new CountDownLatch(1);
  }

  @org.junit.Test
  public void testReportTestCases() throws Exception {
    String testSuiteName = TestUtils.randomAlphaString(10);
    String testCaseName1 = TestUtils.randomAlphaString(10);
    String testCaseName2 = TestUtils.randomAlphaString(10);
    String testCaseName3 = TestUtils.randomAlphaString(10);
    String testCaseName4 = TestUtils.randomAlphaString(10);
    long now = System.currentTimeMillis();
    TestSuiteImpl suite = (TestSuiteImpl) TestSuite.create(testSuiteName).
        test(testCaseName1, context -> {
          try {
            Thread.sleep(10);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }).
        test(testCaseName2, context -> context.fail("the_assertion_failure")).
        test(testCaseName3, context -> { throw new RuntimeException("the_error_failure"); }).
        test(testCaseName4, context -> { throw new RuntimeException(); });

    JunitXmlFormatter reporter = new JunitXmlFormatter(this::reportTo);
    suite.runner().setReporter(new TestCompletionImpl(reporter)).run();
    latch.await(10, TimeUnit.SECONDS);
    Element testsuiteElt = doc.getDocumentElement();
    assertEquals("testsuite", testsuiteElt.getTagName());
    ZonedDateTime result2 = ZonedDateTime.parse(testsuiteElt.getAttribute("timestamp"), DateTimeFormatter.ISO_DATE_TIME);
    Date date = Date.from(result2.toInstant());
    assertTrue(Math.abs(date.getTime() - now) <= 2000);
    assertTrue(parseTime(testsuiteElt.getAttribute("time")) >= 0.010);
    assertEquals("4", testsuiteElt.getAttribute("tests"));
    assertEquals("2", testsuiteElt.getAttribute("errors"));
    assertEquals("1", testsuiteElt.getAttribute("failures"));
    assertEquals("0", testsuiteElt.getAttribute("skipped"));
    assertEquals(testSuiteName, testsuiteElt.getAttribute("name"));
    NodeList testCases = testsuiteElt.getElementsByTagName("testcase");
    assertEquals(4, testCases.getLength());
    Element testCase1Elt = (Element) testCases.item(0);
    assertEquals(testCaseName1, testCase1Elt.getAttribute("name"));
    assertTrue(parseTime(testCase1Elt.getAttribute("time")) >= 0.010);
    assertEquals(0, testCase1Elt.getElementsByTagName("failure").getLength());
    Element testCase2Elt = (Element) testCases.item(1);
    assertEquals(testCaseName2, testCase2Elt.getAttribute("name"));
    assertTrue(parseTime(testCase2Elt.getAttribute("time")) >= 0);
    assertEquals(1, testCase2Elt.getElementsByTagName("failure").getLength());
    Element testCase2FailureElt = (Element) testCase2Elt.getElementsByTagName("failure").item(0);
    assertEquals("AssertionError", testCase2FailureElt.getAttribute("type"));
    assertEquals("the_assertion_failure", testCase2FailureElt.getAttribute("message"));
    Element testCase3Elt = (Element) testCases.item(2);
    assertEquals(testCaseName3, testCase3Elt.getAttribute("name"));
    assertTrue(parseTime(testCase3Elt.getAttribute("time")) >= 0);
    assertEquals(1, testCase3Elt.getElementsByTagName("failure").getLength());
    Element testCase3FailureElt = (Element) testCase3Elt.getElementsByTagName("failure").item(0);
    assertEquals("Error", testCase3FailureElt.getAttribute("type"));
    assertEquals("the_error_failure", testCase3FailureElt.getAttribute("message"));
    Element testCase4Elt = (Element) testCases.item(3);
    assertEquals(testCaseName4, testCase4Elt.getAttribute("name"));
    assertTrue(parseTime(testCase4Elt.getAttribute("time")) >= 0);
    assertEquals(1, testCase4Elt.getElementsByTagName("failure").getLength());
    Element testCase4FailureElt = (Element) testCase4Elt.getElementsByTagName("failure").item(0);
    assertEquals("Error", testCase4FailureElt.getAttribute("type"));
    assertEquals("", testCase4FailureElt.getAttribute("message"));
    testComplete();
  }

  @org.junit.Test
  public void testReportAfterFailure() throws Exception {
    String testSuiteName = TestUtils.randomAlphaString(10);
    String testCaseName1 = TestUtils.randomAlphaString(10);
    TestSuiteImpl suite = (TestSuiteImpl) TestSuite.create(testSuiteName).
        test(testCaseName1, context -> {
        }).
        after(context -> {
          context.fail("the_after_failure");
        });
    JunitXmlFormatter reporter = new JunitXmlFormatter(this::reportTo);
    suite.runner().setReporter(new TestCompletionImpl(reporter)).run();
    latch.await(10, TimeUnit.SECONDS);
    Element testsuiteElt = doc.getDocumentElement();
    assertEquals("testsuite", testsuiteElt.getTagName());
    assertNotNull(testsuiteElt.getAttribute("time"));
    assertEquals("2", testsuiteElt.getAttribute("tests"));
    assertEquals("1", testsuiteElt.getAttribute("errors"));
    assertEquals("0", testsuiteElt.getAttribute("skipped"));
    assertEquals(testSuiteName, testsuiteElt.getAttribute("name"));
    NodeList testCases = testsuiteElt.getElementsByTagName("testcase");
    assertEquals(2, testCases.getLength());
    Element testCase1Elt = (Element) testCases.item(0);
    assertEquals(testCaseName1, testCase1Elt.getAttribute("name"));
    assertNotNull(testCase1Elt.getAttribute("time"));
    assertEquals(0, testCase1Elt.getElementsByTagName("failure").getLength());
    Element testCase2Elt = (Element) testCases.item(1);
    assertEquals(testSuiteName, testCase2Elt.getAttribute("name"));
    assertNotNull(testCase2Elt.getAttribute("time"));
    assertEquals(1, testCase2Elt.getElementsByTagName("failure").getLength());
    Element testCase2FailureElt = (Element) testCase2Elt.getElementsByTagName("failure").item(0);
    assertEquals("AssertionError", testCase2FailureElt.getAttribute("type"));
    assertEquals("the_after_failure", testCase2FailureElt.getAttribute("message"));
    testComplete();
  }

  @org.junit.Test
  public void testReportBeforeFailure() throws Exception {
    String testSuiteName = TestUtils.randomAlphaString(10);
    String testCaseName1 = TestUtils.randomAlphaString(10);

    TestSuiteImpl suite = (TestSuiteImpl) TestSuite.create(testSuiteName).
        test(testCaseName1, context -> {
        }).
        before(context -> {
          context.fail("the_before_failure");
        });

    JunitXmlFormatter reporter = new JunitXmlFormatter(this::reportTo);
    suite.runner().setReporter(new TestCompletionImpl(reporter)).run();
    latch.await(10, TimeUnit.SECONDS);
    Element testsuiteElt = doc.getDocumentElement();
    assertEquals("testsuite", testsuiteElt.getTagName());
    assertNotNull(testsuiteElt.getAttribute("time"));
    assertEquals("1", testsuiteElt.getAttribute("tests"));
    assertEquals("1", testsuiteElt.getAttribute("errors"));
    assertEquals("0", testsuiteElt.getAttribute("skipped"));
    assertEquals(testSuiteName, testsuiteElt.getAttribute("name"));
    NodeList testCases = testsuiteElt.getElementsByTagName("testcase");
    assertEquals(1, testCases.getLength());
    Element testCase1Elt = (Element) testCases.item(0);
    assertEquals(testSuiteName, testCase1Elt.getAttribute("name"));
    assertNotNull(testCase1Elt.getAttribute("time"));
    assertEquals(1, testCase1Elt.getElementsByTagName("failure").getLength());
    Element testCase2FailureElt = (Element) testCase1Elt.getElementsByTagName("failure").item(0);
    assertEquals("AssertionError", testCase2FailureElt.getAttribute("type"));
    assertEquals("the_before_failure", testCase2FailureElt.getAttribute("message"));
    testComplete();
  }
}
