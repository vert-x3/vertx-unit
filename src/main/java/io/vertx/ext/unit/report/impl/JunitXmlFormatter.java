package io.vertx.ext.unit.report.impl;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.unit.report.TestResult;
import io.vertx.ext.unit.impl.TestResultImpl;
import io.vertx.ext.unit.report.Reporter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JunitXmlFormatter implements Reporter<JunitXmlFormatter.XmlReport> {

  public static class XmlReport {
    String name;
    List<TestResult> results = new ArrayList<>();
    AtomicInteger errors = new AtomicInteger();
    AtomicInteger failures = new AtomicInteger();
    AtomicLong time = new AtomicLong();
  }

  private final NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
  private final Handler<String> dataHandler;

  public JunitXmlFormatter(Consumer<Buffer> dataHandler) {
    this.dataHandler = msg -> dataHandler.accept(Buffer.buffer(msg, "UTF-8"));
  }

  @Override
  public XmlReport createReport() {
    return new XmlReport();
  }

  @Override
  public void reportBeginTestSuite(XmlReport report, String name) {
    report.name = name;
  }

  @Override
  public void reportBeginTestCase(XmlReport report, String name) {
  }

  @Override
  public void reportEndTestCase(XmlReport report, TestResult result) {
    report.results.add(result);
    if (result.failed()) {
      if (result.failure().isError()) {
        report.errors.incrementAndGet();
      } else {
        report.failures.incrementAndGet();
      }
    }
    report.time.addAndGet(result.time());
  }

  @Override
  public void reportEndTestSuite(XmlReport report, String name, Throwable err) {
    if (err != null) {
      report.results.add(new TestResultImpl(report.name, 0, err));
      report.errors.incrementAndGet();
    }
    // Create xml and send it
    try {
      StringWriter buffer = new StringWriter();
      XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
      XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(buffer);
      writer.writeStartDocument("UTF-8", "1.0");
      writer.writeStartElement("testsuite");
      writer.writeAttribute("name", report.name);
      writer.writeAttribute("time", "" + formatTimeMillis(report.time.get()));
      writer.writeAttribute("tests", "" + report.results.size());
      writer.writeAttribute("errors", "" + report.errors.get());
      writer.writeAttribute("failures", "" + report.failures.get());
      writer.writeAttribute("skipped", "0");
      for (TestResult result : report.results) {
        writer.writeStartElement("testcase");
        writer.writeAttribute("name", result.name());
        writer.writeAttribute("time", "" + formatTimeMillis(result.time()));
        if (result.failed()) {
          writer.writeStartElement("failure");
          writer.writeAttribute("type", result.failure().isError() ? "Error" : "AssertionError");
          String msg = result.failure().message();
          writer.writeAttribute("message", msg != null ? msg : "");
          writer.writeCharacters(result.failure().stackTrace());
          writer.writeEndElement();
        }
        writer.writeEndElement();
      }
      writer.writeEndElement();
      writer.writeEndDocument();
      writer.close();
      TransformerFactory factory = TransformerFactory.newInstance();
      Transformer transformer = factory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
      StreamSource source = new StreamSource(new StringReader(buffer.toString()));
      buffer.getBuffer().setLength(0);
      StreamResult result = new StreamResult(buffer);
      transformer.transform(source, result);
      dataHandler.handle(buffer.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String formatTimeMillis(long timeMillis) {
    return format.format((((double)timeMillis) / 1000));
  }
}
