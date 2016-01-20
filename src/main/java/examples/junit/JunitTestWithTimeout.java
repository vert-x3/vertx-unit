package examples.junit;


import io.vertx.ext.unit.TestContext;
import org.junit.Test;

public class JunitTestWithTimeout {

  @Test(timeout = 1000l)
  public void testSomething(TestContext context) {
    //...
  }

}
