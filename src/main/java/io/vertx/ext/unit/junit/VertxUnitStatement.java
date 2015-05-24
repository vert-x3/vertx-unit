package io.vertx.ext.unit.junit;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import org.junit.runners.model.Statement;

import java.util.function.Supplier;

/**
 * A JUnit statemnt that when executed associates the evaluated statement with a {@link io.vertx.core.Context}.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VertxUnitStatement extends Statement {

  private final Supplier<Context> supplier;
  private final Statement statement;

  public VertxUnitStatement(Supplier<Context> supplier, Statement base) {
    this.supplier = supplier;
    this.statement = base;
  }

  @Override
  public void evaluate() throws Throwable {
    Context context = supplier.get();
    VertxUnitRunner.pushContext(context);
    try {
      statement.evaluate();
    } finally {
      VertxUnitRunner.popContext();
    }
  }
}
