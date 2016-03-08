package io.vertx.ext.unit.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.cli.annotations.Description;
import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Option;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.core.impl.launcher.commands.RunCommand;
import io.vertx.ext.unit.report.ReportOptions;
import io.vertx.ext.unit.report.ReporterFactory;
import io.vertx.ext.unit.report.impl.DefaultReporterFactory;

import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("test")
@Summary("Runs a Vert.x Unit test called <test-verticle> in its own instance of vert.x.")
@Description("" +
    "Execute Vert.x Unit tests in its own instance of Vert.x. " +
    "The Verticle must run a Vert.x Unit test suite, for instance:\n" +
    "\n" +
    "  var TestSuite = require('vertx-unit-js/test_suite');\n" +
    "  var suite = TestSuite.create('my-suite');\n" +
    "  ...\n" +
    "  suite.run()\n" +
    "\n" +
    "> vertx test suite.js\n" +
    "\n" +
    "The test command blocks until the suite is fully executed and then exit the JVM with " +
    "the appropriate code: 0 for success, 1 for failure.\n" +
    "\n" +
    "The test suite execution is also logged directly on the console.\n")
public class TestCommand extends RunCommand {

  private final CompletableFuture<String> deployedFuture = new CompletableFuture<>();
  private String test;
  private boolean report;
  private long timeout;

  @Option(longName = "test", argName = "test")
  @Description("Select one or several tests to run")
  public void setTest(String test) {
    this.test = test;
  }

  @Option(longName = "report", argName = "report")
  @Description("Report the execution to a file in JUnit XML format")
  public void setReport(boolean report) {
    // Todo report dir ????
    this.report = report;
  }

  @Option(longName = "timeout", argName = "timeout")
  @Description("Set the test suite timeout in millis")
  public void setTimeout(long timeout) {
    this.timeout = timeout;
  }

  @Override
  protected void deploy() {

    //
    ConcurrentLinkedDeque<TestCompletionImpl> completions = new ConcurrentLinkedDeque<>();
    TestSuiteImpl.setDefaultRunner(runner -> {

      // Reconfigure
      if (test != null) {
        StringBuilder sb = new StringBuilder();
        int prev = 0;
        while (true) {
          int pos = test.indexOf('*', prev);
          if (pos == -1) {
            break;
          }
          sb.append(Pattern.quote(test.substring(prev, pos)));
          sb.append(".*");
          prev = pos + 1;
        }
        sb.append(Pattern.quote(test.substring(prev)));
        Pattern p = Pattern.compile(sb.toString());
        for (Iterator<TestCaseImpl> i = runner.getTests().iterator();i.hasNext();) {
          TestCaseImpl test = i.next();
          if (!p.matcher(test.name).matches()) {
            i.remove();
          }
        }
      }
      TestCompletionImpl reporter = (TestCompletionImpl) runner.getReporter();
      ReporterFactory f = new DefaultReporterFactory();
      reporter.addReporter(f.reporter(vertx, new ReportOptions().setTo("console")));
      if (report) {
        reporter.addReporter(f.reporter(vertx, new ReportOptions().setTo("file:.").setFormat("junit")));
      }
      if (timeout > 0) {
        runner.setTimeout(timeout);
      }
      completions.add(reporter);

      // Finally run the test suite
      runner.run();
    });

    // Deploy the verticle
    super.deploy();

    //
    try {
      deployedFuture.get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.exit(1);
      return;
    } catch (ExecutionException e) {
      e.getCause().printStackTrace();
      System.exit(1);
      return;
    }

    // Now wait each completion
    TestCompletionImpl completion;
    int remainingCompletions = completions.size();
    while ((completion = completions.poll()) != null) {
      completion.await();
      if(completion.isSucceeded()) {
        remainingCompletions--;
      }
    }
    System.exit(remainingCompletions);
  }

  @Override
  public synchronized void deploy(String verticle, Vertx vertx, DeploymentOptions options, Handler<AsyncResult<String>> completionHandler) {
    super.deploy(verticle, vertx, options, ar -> {
      if (ar.succeeded()) {
        deployedFuture.complete(ar.result());
      } else {
        deployedFuture.completeExceptionally(ar.cause());
      }
      if (completionHandler != null) {
        completionHandler.handle(ar);
      }
    });
  }
}
