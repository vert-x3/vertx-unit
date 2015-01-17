package io.vertx.ext.unit;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import org.junit.After;
import org.junit.Before;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class UnitEventLoopTest extends UnitTestBase {

  private Vertx vertx;
  private Context context;

  public UnitEventLoopTest() {
    super();
    executor = runner -> runner.runOnContext(context);
  }

  @Override
  protected boolean checkContext() {
    return Vertx.currentContext() == context;
  }

  @Before
  public void setUp() {
    vertx = Vertx.vertx();
    context = vertx.getOrCreateContext();
  }

  @After
  public void tearDown() {
    if (vertx != null) {
      vertx.close();
      vertx = null;
    }
  }


}
