package io.vertx.ext.unit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class UnitSyncTest extends UnitTestBase {

  public UnitSyncTest() {
    super(Runnable::run);
  }
}
