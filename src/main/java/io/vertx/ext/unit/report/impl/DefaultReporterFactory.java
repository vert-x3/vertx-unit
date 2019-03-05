package io.vertx.ext.unit.report.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.unit.report.ReportOptions;
import io.vertx.ext.unit.report.Reporter;
import io.vertx.ext.unit.report.ReporterFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

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
      BiFunction<String, String, ReportStream> streamFactory;
      switch (prefix) {
        case "console":
          streamFactory = (name, ext) -> new ReportStream() {
            @Override
            public void info(Buffer msg) {
              System.out.print(msg.toString("UTF-8"));
            }
            @Override
            public void error(Buffer msg, Throwable cause) {
              System.err.print(msg.toString("UTF-8"));
              cause.printStackTrace(System.err);
            }
          };
          break;
        case "log":
          if (location == null) {
            throw new IllegalArgumentException("Invalid log report configuration: " + to + " must follow log: + address");
          }
          Logger log = LoggerFactory.getLogger(location);
          streamFactory = (name, ext) -> new ReportStream() {
            @Override
            public void info(Buffer msg) {
              log.info(msg.toString("UTF-8"));
            }
            @Override
            public void error(Buffer msg, Throwable cause) {
              log.error(msg.toString("UTF-8"), cause);
            }
          };
          break;
        case "file": {
          if (location == null) {
            throw new IllegalArgumentException("Invalid file report configuration: " + to + " must follow file: + address");
          }
          if (vertx == null) {
            throw new IllegalArgumentException("No vertx provided for filesystem reporting");
          }
          streamFactory = (name, ext) -> {
            String fileName = location + File.separator + name + "." + ext;
            AsyncFile file = vertx.fileSystem().openBlocking(fileName, new OpenOptions());
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
            return new ReportStream() {
              @Override
              public void info(Buffer msg) {
                file.write(msg);
              }
              @Override
              public void error(Buffer msg, Throwable cause) {
                writer.println(msg.toString("UTF-8"));
                cause.printStackTrace(writer);
              }
              @Override
              public void end() {
                writer.close();
              }
            };
          };
          break;
        }
        default:
          throw new IllegalArgumentException("Illegal reporter name <" + to + ">");
      }
      String format = options.getFormat();
      switch (format) {
        case "simple":
          return new SimpleFormatter(name -> streamFactory.apply(name, "txt"));
        case "junit":
          return new JunitXmlFormatter(name -> streamFactory.apply(name, "xml"));
        default:
          throw new IllegalArgumentException("Invalid format <" + format + ">");
      }
    }
  }
}
