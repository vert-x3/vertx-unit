package io.vertx.ext.unit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class UnitAsyncTest extends UnitTestBase {

  public UnitAsyncTest() {
    super();
    executor = runner -> new Thread(runner::run).start();
  }
}
