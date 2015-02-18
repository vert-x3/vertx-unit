package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.streams.WriteStream;
import io.vertx.ext.unit.Reporter;
import io.vertx.ext.unit.TestSuiteReport;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SimpleReporter implements Reporter {

  private final Consumer<String> info;
  private final BiConsumer<String, Throwable> error;
  private final Handler<Void> endHandler;

  public SimpleReporter(Logger log) {
    info = log::info;
    error = log::error;
    endHandler = null;
  }

  public SimpleReporter(PrintStream out, PrintStream err) {
    info = out::println;
    error = (msg, error) -> {
      err.println(msg);
      error.printStackTrace(err);
    };
    endHandler = null;
  }

  public SimpleReporter(Consumer<String> info, BiConsumer<String, Throwable> error, Handler<Void> endHandler) {
    this.info = info;
    this.error = error;
    this.endHandler = endHandler;
  }

  public SimpleReporter(Handler<Buffer> dataHandler, Handler<Void> endHandler) {
    PrintWriter writer = new PrintWriter(new StringWriter() {
      @Override
      public void flush() {
        StringBuffer sb = getBuffer();
        Buffer buf = Buffer.buffer(sb.toString());
        sb.setLength(0);
        dataHandler.handle(buf);
      }
    });
    info = msg -> {
      writer.println(msg);
      writer.flush(); };
    error = (msg, err) -> {
      writer.println(msg);
      err.printStackTrace(writer);
      writer.flush();
    };
    this.endHandler = endHandler;
  }

  @Override
  public void handle(TestSuiteReport suite) {
    info.accept("Begin test suite " + suite.name());
    AtomicInteger run = new AtomicInteger();
    AtomicInteger failures = new AtomicInteger();
    AtomicInteger errors = new AtomicInteger();
    suite.handler(test -> {
      info.accept("Begin test " + test.name());
      run.incrementAndGet();
      test.endHandler(result -> {
        if (result.succeeded()) {
          info.accept("Passed " + test.name());
        } else {
          failures.incrementAndGet();
          error.accept("Failed " + test.name(), result.failure().cause());
        }
      });
    });
    suite.exceptionHandler(err -> {
      error.accept("Test suite " + suite.name() + " failure", err);
    });
    suite.endHandler(v -> {
      info.accept("End test suite " + suite.name() + " , run: " + run.get() + ", Failures: " + failures.get() + ", Errors: " + errors.get());
      if (endHandler != null) {
        endHandler.handle(null);
      }
    });
  }
}
