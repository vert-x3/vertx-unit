package io.vertx.ext.unit;

import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.report.ReportOptions;
import io.vertx.ext.unit.report.impl.JunitXmlFormatter;
import io.vertx.ext.unit.report.Reporter;
import io.vertx.ext.unit.report.impl.SimpleFormatter;
import io.vertx.test.core.VertxTestBase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ReportOptionsTest extends VertxTestBase {

  private String testSystemOut(Runnable runnable) {
    PrintStream prevOut = System.out;
    Thread t = Thread.currentThread();
    ByteArrayOutputStream out = new ByteArrayOutputStream() {
      @Override
      public synchronized void write(int b) {
        if (Thread.currentThread() == t) {
          super.write(b);
        }
      }
      @Override
      public synchronized void write(byte[] b, int off, int len) {
        if (Thread.currentThread() == t) {
          super.write(b, off, len);
        }
      }
    };
    System.setOut(new PrintStream(out));
    try {
      runnable.run();
      System.out.flush();
      return out.toString();
    } finally {
      System.setOut(prevOut);
    }
  }

  private String testLog(String name, Runnable runnable) {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    StreamHandler handler = new StreamHandler(buffer, new java.util.logging.SimpleFormatter());
    Logger logger = Logger.getLogger(name);
    logger.addHandler(handler);
    try {
      runnable.run();
      handler.flush();
      return buffer.toString();
    } finally {
      logger.removeHandler(handler);
    }
  }

  private static final TestSuite suite = TestSuite.create("my_suite").test("my_test", test -> {});

  @org.junit.Test
  public void testDefaultOptions() {
    String s = testSystemOut(() -> {
      suite.run(new TestOptions().addReporter(new ReportOptions()));
    });
    assertTrue(s.length() > 0);
  }

  @org.junit.Test
  public void testToConsole() {
    String s = testSystemOut(() -> {
      Reporter<?> reporter = Reporter.reporter(vertx, new ReportOptions().setTo("console"));
      assertTrue(reporter instanceof SimpleFormatter);
      suite.run(new TestOptions().addReporter(new ReportOptions().setTo("console")));
    });
    assertTrue(s.length() > 0);
  }

  @org.junit.Test
  public void testToLog() {
    String s = testLog("mylogger", () -> {
      Reporter<?> reporter = Reporter.reporter(vertx, new ReportOptions().setTo("log").setAt("mylogger"));
      assertTrue(reporter instanceof SimpleFormatter);
      suite.run(new TestOptions().addReporter(new ReportOptions().setTo("log").setAt("mylogger")));
    });
    assertTrue(s.length() > 0);
  }

  @org.junit.Test
  public void testToFile() {
    FileSystem fs = vertx.fileSystem();
    String file = "target/report.txt";
    assertFalse(fs.existsBlocking(file));
    suite.run(vertx, new TestOptions().addReporter(new ReportOptions().setTo("file").setAt(file)));
    assertTrue(fs.existsBlocking(file));
    FileProps props = fs.propsBlocking(file);
    assertTrue(props.size() > 0);
  }

  @org.junit.Test
  public void testToEventBus() {
    MessageConsumer<JsonObject> consumer = vertx.eventBus().<JsonObject>consumer("the_address");
    consumer.handler(msg -> {
      if (msg.body().getString("type").equals("endTestSuite")) {
        consumer.unregister();
        testComplete();
      }
    });
    consumer.completionHandler(ar -> {
      assertTrue(ar.succeeded());
      suite.run(vertx, new TestOptions().addReporter(new ReportOptions().setTo("bus").setAt("the_address")));
    });
    await();
  }

  @org.junit.Test
  public void testSimpleFormat() {
    Reporter<?> reporter = Reporter.reporter(vertx, new ReportOptions().setFormat("simple"));
    assertTrue(reporter instanceof SimpleFormatter);
  }

  @org.junit.Test
  public void testJunitFormat() {
    Reporter<?> reporter = Reporter.reporter(vertx, new ReportOptions().setFormat("junit"));
    assertTrue(reporter instanceof JunitXmlFormatter);
  }
}
