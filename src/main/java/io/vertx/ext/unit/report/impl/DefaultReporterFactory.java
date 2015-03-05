package io.vertx.ext.unit.report.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.impl.LoggerFactory;
import io.vertx.ext.unit.report.ReportOptions;
import io.vertx.ext.unit.report.Reporter;
import io.vertx.ext.unit.report.ReporterFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class DefaultReporterFactory implements ReporterFactory {

  @Override
  public Reporter reporter(Vertx vertx, ReportOptions options) {
    String to = options.getTo();
    String prefix;
    String location;
    int pos = to.indexOf(':');
    if (pos != -1) {
      prefix = to.substring(0, pos);
      location = to.substring(pos + 1);
    } else {
      prefix = to;
      location = null;
    }
    if (prefix.equals("bus")) {
      if (location == null) {
        throw new IllegalArgumentException("Invalid bus report configuration: " + to + " must follow bus: + address");
      }
      if (vertx == null) {
        throw new IllegalArgumentException("No vertx provided for event bus reporting");
      }
      return new EventBusReporter(vertx, location);
    } else {
      Consumer<Buffer> infoHandler;
      BiConsumer<Buffer, Throwable> errorHandler;
      Handler<Void> endHandler;
      switch (prefix) {
        case "console":
          infoHandler = System.out::println;
          errorHandler = (msg, error) -> {
            System.err.println(msg);
            error.printStackTrace(System.err);
          };
          endHandler = null;
          break;
        case "log":
          if (location == null) {
            throw new IllegalArgumentException("Invalid log report configuration: " + to + " must follow log: + address");
          }
          Logger log = LoggerFactory.getLogger(location);
          infoHandler = log::info;
          errorHandler = log::error;
          endHandler = null;
          break;
        case "file": {
          if (location == null) {
            throw new IllegalArgumentException("Invalid file report configuration: " + to + " must follow file: + address");
          }
          if (vertx == null) {
            throw new IllegalArgumentException("No vertx provided for filesystem reporting");
          }
          AsyncFile file = vertx.fileSystem().openBlocking(location, new OpenOptions());
          infoHandler = msg -> file.write(msg.appendString(System.lineSeparator()));
          PrintWriter writer = new PrintWriter(new Writer() {
            public void write(char[] cbuf, int off, int len) throws IOException {
              file.write(Buffer.buffer(new String(cbuf, off, len)));
            }
            public void flush() throws IOException {
              file.flush();
            }
            public void close() throws IOException {
              file.close();
            }
          });
          errorHandler = (msg,err) -> {
            writer.println(err);
            err.printStackTrace(writer);
          };
          endHandler = v -> {
            writer.close();
          };
          break;
        }
        default:
          throw new IllegalArgumentException("Illegal reporter name <" + to + ">");
      }
      String format = options.getFormat();
      switch (format) {
        case "simple":
          return new SimpleFormatter(infoHandler, errorHandler, endHandler);
        case "junit":
          return new JunitXmlFormatter((Buffer buffer) -> {
            infoHandler.accept(buffer);
            if (endHandler != null) {
              endHandler.handle(null);
            }
          });
        default:
          if (endHandler != null) {
            endHandler.handle(null);
          }
          throw new IllegalArgumentException("Invalid format <" + format + ">");
      }
    }
  }
}
