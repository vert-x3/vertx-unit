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
    String to = options.getTo().toLowerCase();
    String at = options.getAt();
    if (to.equals("bus")) {
      if (at == null) {
        throw new IllegalArgumentException("Missing 'dest' field in report options specifying the event bus address");
      }
      if (vertx == null) {
        throw new IllegalArgumentException("No vertx provided for event bus reporting");
      }
      return new EventBusReporter(vertx, at);
    } else {
      Consumer<Buffer> infoHandler;
      BiConsumer<Buffer, Throwable> errorHandler;
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
      switch (format.toLowerCase()) {
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
