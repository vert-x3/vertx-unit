package io.vertx.ext.unit;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.impl.LoggerFactory;
import io.vertx.ext.unit.impl.SimpleReporter;
import io.vertx.ext.unit.impl.EventBusReporter;
import io.vertx.ext.unit.impl.JunitXmlReporter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Reporter extends Handler<TestSuiteReport> {

  static Reporter create(ReportOptions options) {
    return create(null, options);
  }

  static Reporter create(Vertx vertx, ReportOptions options) {
    String to = options.getTo().toLowerCase();
    String at = options.getAt();
    if (to.equals("bus")) {
      if (at == null) {
        throw new IllegalArgumentException("Missing 'dest' field in report options specifying the event bus address");
      }
      if (vertx == null) {
        throw new IllegalArgumentException("No vertx provided for event bus reporting");
      }
      return eventBusReporter(vertx.eventBus().publisher(at));
    } else {
      final Consumer<String> infoHandler;
      BiConsumer<String, Throwable> errorHandler;
      Handler<Void> endHandler;
      switch (to) {
        case "console":
          infoHandler = System.out::println;
          errorHandler = (msg, error) -> {
            System.err.println(msg);
            error.printStackTrace(System.err);
          };
          endHandler = null;
          break;
        case "log":
          if (at == null) {
            throw new IllegalArgumentException("Missing 'dest' field in report options specifying the logger name");
          }
          Logger log = LoggerFactory.getLogger(at);
          infoHandler = log::info;
          errorHandler = log::error;
          endHandler = null;
          break;
        case "file": {
          String path = options.getAt();
          if (path == null) {
            throw new IllegalArgumentException("Missing 'dest' field in report options specifying the file name");
          }
          if (vertx == null) {
            throw new IllegalArgumentException("No vertx provided for filesystem reporting");
          }
          AsyncFile file = vertx.fileSystem().openBlocking(path, new OpenOptions());
          infoHandler = msg -> file.write(Buffer.buffer(msg).appendString(System.lineSeparator()));
          PrintWriter writer = new PrintWriter(new Writer() {
            public void write(char[] cbuf, int off, int len) throws IOException {
              file.write(Buffer.buffer(new String(cbuf, off, len)));
            }
            public void flush() throws IOException {}
            public void close() throws IOException {}
          });
          errorHandler = (msg,err) -> {
            writer.println(err);
            err.printStackTrace(writer);
          };
          endHandler = v -> file.close();
          break;
        }
        default:
          throw new IllegalArgumentException("Illegal reporter name <" + to + ">");
      }
      String format = options.getFormat();
      switch (format.toLowerCase()) {
        case "simple":
          return new SimpleReporter(infoHandler, errorHandler, endHandler);
        case "junit":
          return new JunitXmlReporter((String s) -> {
            infoHandler.accept(s);
            if (endHandler != null) {
              endHandler.handle(null);
            }
          });
        default:
          throw new IllegalArgumentException("Invalid format <" + format + ">");
      }
    }
  }

  static Reporter logReporter(String loggerName) {
    return new SimpleReporter(LoggerFactory.getLogger(loggerName));
  }

  static Reporter consoleReporter() {
    return create(null, new ReportOptions());
  }

  static Reporter streamReporter(Handler<Buffer> stream) {
    return new SimpleReporter(stream, null);
  }

  static Reporter eventBusReporter(MessageProducer<?> producer) {
    return new EventBusReporter((MessageProducer) producer);
  }

  static Reporter junitXmlReporter(Handler<Buffer> output) {
    return new JunitXmlReporter(output);
  }
}
