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

  public static void asserting_01(io.vertx.ext.unit.TestSuite suite, int callbackCount) {
    suite.test("my_test_1", test -> {
      test.assertEquals(10, callbackCount);
    });
  }

  public static void asserting_02(io.vertx.ext.unit.TestSuite suite, int callbackCount) {
    suite.test("my_test_1", test -> {
      test.assertEquals(10, callbackCount, "Should have been 10 instead of " + callbackCount);
    });
  }

  public static void asserting_03(io.vertx.ext.unit.TestSuite suite, int callbackCount) {
    suite.test("my_test_1", test -> {
      test.assertNotEquals(10, callbackCount);
    });
  }

  public static void asserting_04(io.vertx.ext.unit.TestSuite suite, boolean var, int value) {
    suite.test("my_test_1", test -> {
      test.assertTrue(var);
      test.assertFalse(value > 10);
    });
  }

  public static void asserting_05(io.vertx.ext.unit.TestSuite suite) {
    suite.test("my_test_1", test -> {
      test.fail("That should never happen");
      // Following statements won't be executed
    });
  }
}
