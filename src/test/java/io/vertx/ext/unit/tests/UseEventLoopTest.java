package io.vertx.ext.unit.tests;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.impl.TestSuiteImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class UseEventLoopTest {

  @org.junit.Test
  public void testWithoutVertx() {
    List<Context> contexts = Collections.synchronizedList(new ArrayList<>());
    TestSuiteImpl suite = (TestSuiteImpl) TestSuite.create("my_suite").test("my_test", context -> {
      contexts.add(Vertx.currentContext());
    });
    try {
      suite.runner().setReporter(new TestReporter()).setUseEventLoop(true).run();
      fail();
    } catch (IllegalStateException expected) {
    }
    assertEquals(Collections.<Context>emptyList(), contexts);
    TestReporter reporter = new TestReporter();
    suite.runner().setReporter(reporter).setUseEventLoop(false).run();
    reporter.await();
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertTrue(reporter.results.get(0).succeeded());
    assertEquals(Collections.<Context>singletonList(null), contexts);
    reporter = new TestReporter();
    suite.runner().setReporter(reporter).setUseEventLoop(null).run();
    reporter.await();
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertTrue(reporter.results.get(0).succeeded());
    assertEquals(Arrays.<Context>asList(null, null), contexts);
  }

  @org.junit.Test
  public void testWitVertx() {
    Vertx vertx = Vertx.vertx();
    List<Context> contexts = Collections.synchronizedList(new ArrayList<>());
    TestSuiteImpl suite = (TestSuiteImpl) TestSuite.create("my_suite").test("my_test", context -> {
      contexts.add(Vertx.currentContext());
    });
    TestReporter reporter = new TestReporter();
    suite.runner().setReporter(reporter).setUseEventLoop(true).setVertx(vertx).run();
    reporter.await();
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertTrue(reporter.results.get(0).succeeded());
    assertEquals(1, contexts.size());
    assertNotNull(contexts.get(0));
    reporter = new TestReporter();
    suite.runner().setReporter(reporter).setUseEventLoop(false).setVertx(vertx).run();
    reporter.await();
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertTrue(reporter.results.get(0).succeeded());
    assertEquals(2, contexts.size());
    assertNull(contexts.get(1));
    reporter = new TestReporter();
    suite.runner().setReporter(reporter).setUseEventLoop(null).setVertx(vertx).run();
    reporter.await();
    assertEquals(0, reporter.exceptions.size());
    assertEquals(1, reporter.results.size());
    assertTrue(reporter.results.get(0).succeeded());
    assertEquals(3, contexts.size());
    assertNotNull(contexts.get(2));
    vertx.close();
  }
}
