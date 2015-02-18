package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.unit.Reporter;
import io.vertx.ext.unit.TestResult;
import io.vertx.ext.unit.TestSuiteReport;

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
public class JunitXmlReporter implements Reporter {

  private final NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
  private final Consumer<String> dataHandler;

  public JunitXmlReporter(Consumer<String> dataHandler) {
    this.dataHandler = dataHandler;
  }

  public JunitXmlReporter(Handler<Buffer> dataHandler) {
    this.dataHandler = msg -> dataHandler.handle(Buffer.buffer(msg, "UTF-8"));
  }

  @Override
  public void handle(TestSuiteReport suite) {
    List<TestResult> results = new ArrayList<>();
    AtomicInteger errors = new AtomicInteger();
    AtomicInteger failures = new AtomicInteger();
    AtomicLong time = new AtomicLong();
    suite.handler(testCase -> testCase.endHandler(result -> {
      results.add(result);
      if (result.failed()) {
        if (result.failure().isError()) {
          errors.incrementAndGet();
        } else {
          failures.incrementAndGet();
        }
      }
      time.addAndGet(result.time());
    }));
    suite.exceptionHandler(err -> {
      results.add(new TestResultImpl(suite.name(), 0, err));
      errors.incrementAndGet();
    });
    suite.endHandler(done -> {
      // Create xml and send it
      try {
        StringWriter buffer = new StringWriter();
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(buffer);
        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeStartElement("testsuite");
        writer.writeAttribute("name", suite.name());
        writer.writeAttribute("time", "" + formatTimeMillis(time.get()));
        writer.writeAttribute("tests", "" + results.size());
        writer.writeAttribute("errors", "" + errors.get());
        writer.writeAttribute("failures", "" + failures.get());
        writer.writeAttribute("skipped", "0");
        for (TestResult result : results) {
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
        dataHandler.accept(buffer.toString());
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  private String formatTimeMillis(long timeMillis) {
    return format.format((((double)timeMillis) / 1000));
  }
}
