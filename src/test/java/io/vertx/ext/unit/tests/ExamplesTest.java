package io.vertx.ext.unit.tests;

import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.report.ReportOptions;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ExamplesTest {

  @org.junit.Test
  public void testExample2() {
    // Run it to get the output and copy in the docs
    System.out.println("------ begin example 2");
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.test("my_test_case", context -> {
      String s = "value";
      context.assertEquals("value", s);
    });
    suite.run(new TestOptions().addReporter(new ReportOptions().setTo("console")));
    System.out.println("------ end example 2");
  }

}
