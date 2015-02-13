package io.vertx.ext.unit;

import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.AllTests;

/**
 * Junit integration example.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(AllTests.class)
public class JUnitTestSuite {

  public static TestSuite suite() {
    return io.vertx.ext.unit.TestSuite.create().
        test("test 1", test -> {
          test.assertTrue(true);
        }).
        test("test 2", test -> {
          // Test 2
        }).toJUnitSuite();
  }
}
