package io.vertx.ext.unit.collect.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.unit.collect.EventBusCollector;
import io.vertx.ext.unit.impl.FailureImpl;
import io.vertx.ext.unit.impl.TestResultImpl;
import io.vertx.ext.unit.report.Failure;
import io.vertx.ext.unit.report.TestCaseReport;
import io.vertx.ext.unit.report.TestResult;
import io.vertx.ext.unit.report.TestSuiteReport;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class EventBusCollectorImpl implements EventBusCollector, Handler<Message<JsonObject>> {

  private final Vertx vertx;
  private final Handler<TestSuiteReport> reporter;
  private Handler<TestCaseReport> testCaseRunnerHandler;
  private Handler<Throwable> exceptionHandler;
  private Handler<Void> endHandler;
  private TestSuiteReport runner;
  private Handler<TestResult> testCaseHandler;

  public EventBusCollectorImpl(Vertx vertx, Handler<TestSuiteReport> reporter) {
    this.reporter = reporter;
    this.vertx = vertx;
  }

  @Override
  public void handle(Message<JsonObject> event) {
    JsonObject body = event.body();
    String type = body.getString("type", "");
    String name = body.getString("name");
    switch (type) {
      case EVENT_BEGIN_TEST_SUITE: {
        runner = new TestSuiteReport() {
          @Override
          public String name() {
            return name;
          }
          @Override
          public ReadStream<TestCaseReport> exceptionHandler(Handler<Throwable> handler) {
            exceptionHandler = handler;
            return this;
          }
          @Override
          public ReadStream<TestCaseReport> handler(Handler<TestCaseReport> handler) {
            testCaseRunnerHandler = handler;
            return this;
          }
          @Override
          public ReadStream<TestCaseReport> pause() {
            return this;
          }
          @Override
          public ReadStream<TestCaseReport> resume() {
            return this;
          }
          @Override
          public ReadStream<TestCaseReport> endHandler(Handler<Void> handler) {
            endHandler = handler;
            return this;
          }
        };
        reporter.handle(runner);
        break;
      }
      case EVENT_BEGIN_TEST_CASE: {
        if (testCaseRunnerHandler != null) {
          testCaseRunnerHandler.handle(new TestCaseReport() {
            @Override
            public String name() {
              return name;
            }
            @Override
            public TestCaseReport endHandler(Handler<TestResult> handler) {
              testCaseHandler = handler;
              return this;
            }
          });
        }
        break;
      }
      case EVENT_END_TEST_CASE: {
        if (testCaseHandler != null) {
          JsonObject failureJson = body.getJsonObject("failure");
          Failure failure = null;
          if (failureJson != null) {
            failure = new FailureImpl(failureJson);
          }
          TestResult result = new TestResultImpl(name, body.getInteger("time", 0), failure);
          testCaseHandler.handle(result);
          testCaseHandler = null;
        }
        break;
      }
      case EVENT_END_TEST_SUITE: {
        JsonObject failureJson = body.getJsonObject("failure");
        if (failureJson != null && exceptionHandler != null) {
          FailureImpl failure = new FailureImpl(failureJson);
          Throwable cause = failure.cause();
          if (cause == null) {
            // Best effort
            cause = new Exception(failureJson.getString("message"));
          }
          exceptionHandler.handle(cause);
        }
        if (endHandler != null) {
          endHandler.handle(null);
        }
        break;
      }
    }
  }

  @Override
  public MessageConsumer<?> register(String address) {
    return vertx.eventBus().consumer(address, this);
  }

  @Override
  public Handler<Message<JsonObject>> asMessageHandler() {
    return this;
  }
}
