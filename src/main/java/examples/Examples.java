package examples;

import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.report.ReportOptions;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Examples {

  public static void test_01() {
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.test("my_test", test -> {
      String s = "value";
      test.assertEquals("value", s);
    });
    suite.run();
  }

  public static void test_02() {
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.test("my_test", test -> {
      String s = "value";
      test.assertEquals("value", s);
    });
    suite.run(new TestOptions().addReporter(new ReportOptions().setTo("console")));
  }

  public static void writing_test_suite_01() {
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.test("my_test_1", test -> {
      // Test 1
    });
    suite.test("my_test_2", test -> {
      // Test 2
    });
    suite.test("my_test_3", test -> {
      // Test 3
    });
  }

  public static void writing_test_suite_02() {
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.test("my_test_1", test -> {
      // Test 1
    }).test("my_test_2", test -> {
      // Test 2
    }).test("my_test_3", test -> {
      // Test 3
    });
  }

  public static void writing_test_suite_03() {
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.before(test -> {
      // Test suite setup
    }).test("my_test_1", test -> {
      // Test 1
    }).test("my_test_2", test -> {
      // Test 2
    }).test("my_test_3", test -> {
      // Test 3
    }).after(test -> {
      // Test suite cleanup
    });
  }

  public static void writing_test_suite_04() {
    TestSuite suite = TestSuite.create("the_test_suite");
    suite.beforeEach(test -> {
      // Test case setup
    }).test("my_test_1", test -> {
      // Test 1
    }).test("my_test_2", test -> {
      // Test 2
    }).test("my_test_3", test -> {
      // Test 3
    }).afterEach(test -> {
      // Test case cleanup
    });
  }
}
