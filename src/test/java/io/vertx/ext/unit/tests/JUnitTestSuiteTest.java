package io.vertx.ext.unit.tests;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Junit integration example.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class JUnitTestSuiteTest {

  @BeforeClass
  public static void before(TestContext context) {
    System.out.println("before");
  }

  @Before
  public void beforeEach(TestContext context) {
    System.out.println("beforeEach");
  }

  @Test
  public void testSomething(TestContext context) {
    System.out.println("testSomething");
  }

  @Test
  public void testSomethingElse(TestContext context) {
    System.out.println("testSomethingElse");
  }

  @After
  public void afterEach(TestContext context) {
    System.out.println("afterEach");
  }

  @AfterClass
  public static void after(TestContext context) {
    System.out.println("after");
  }
}
