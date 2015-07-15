package examples.junit;

import io.vertx.ext.unit.TestSuite;

public class Snippets {


  public void testSuite() {
    TestSuite suite = TestSuite.create(new MyTestSuite());
  }

}
