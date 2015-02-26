package io.vertx.ext.unit;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.AllTests;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Junit integration example.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(AllTests.class)
public class JUnitTestSuiteTest {

  public static TestSuite suite() {
    JUnitTestSuiteTest suite = new JUnitTestSuiteTest();
    TestSuite testSuite = io.vertx.ext.unit.TestSuite.create(suite).toJUnitSuite();
    testSuite.addTest(new junit.framework.Test() {
      @Override
      public int countTestCases() {
        return 1;
      }
      @Override
      public void run(TestResult result) {
        result.startTest(this);
        if (suite.count != 2) {
          result.addError(this, new RuntimeException());
        }
        result.endTest(this);
      }
    });
    return testSuite;
  }

  int count;

  public void testSomething(Test test) {
    count++;
  }

  public void testSomethingElse(Test test) {
    count++;
  }
}
