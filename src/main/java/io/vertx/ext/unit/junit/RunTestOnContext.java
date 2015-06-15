package io.vertx.ext.unit.junit;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A JUnit rule that runs tests on a Vert.x context.<p/>
 *
 * When used as a {@link org.junit.Rule} a new context is created for each tested method, the context will be same
 * for the before and after method, but different for all the tested methods.<p/>
 *
 * When used as a {@link org.junit.ClassRule}, a single context is created for all the tested method, the <i>beforeClass</i>
 * and <i>afterClass</i> method will also executed in this context.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class RunTestOnContext implements TestRule {

  private volatile Vertx vertx;
  private final Supplier<Vertx> createVertx;
  private final BiConsumer<Vertx, CountDownLatch> closeVertx;

  /**
   * Create a new rule managing a Vertx instance created with default options. The Vert.x instance
   * is created and closed for each test.
   */
  public RunTestOnContext() {
    this(new VertxOptions());
  }

  /**
   * Create a new rule managing a Vertx instance created with specified options. The Vert.x instance
   * is created and closed for each test.
   *
   * @param options the vertx options
   */
  public RunTestOnContext(VertxOptions options) {
    this(() -> Vertx.vertx(options));
  }

  /**
   * Create a new rule with supplier/consumer for creating/closing a Vert.x instance. The lambda are invoked for each
   * test. The {@code closeVertx} lambda should invoke the consumer with null when the {@code vertx} instance is closed.
   *
   * @param createVertx the create Vert.x supplier
   * @param closeVertx the close Vert.x consumer
   */
  public RunTestOnContext(Supplier<Vertx> createVertx, BiConsumer<Vertx, Consumer<Void>> closeVertx) {
    this.createVertx = createVertx;
    this.closeVertx = (vertx, latch) -> closeVertx.accept(vertx, v -> latch.countDown());
  }

  /**
   * Create a new rule with supplier for creating a Vert.x instance. The lambda are invoked for each
   * test.
   *
   * @param createVertx the create Vert.x supplier
   */
  public RunTestOnContext(Supplier<Vertx> createVertx) {
    this(createVertx, (vertx, latch) -> vertx.close(ar -> latch.accept(null)));
  }

  /**
   * Retrieves the current Vert.x instance, this value varies according to the test life cycle.
   *
   * @return the vertx object
   */
  public Vertx vertx() {
    return vertx;
  }

  @Override
  public Statement apply(Statement base, Description description) {
    return new VertxUnitStatement(() -> vertx != null ? vertx.getOrCreateContext() : null, base) {
      @Override
      public void evaluate() throws Throwable {
        vertx = createVertx.get();
        try {
          super.evaluate();
        } finally {
          CountDownLatch latch = new CountDownLatch(1);
          closeVertx.accept(vertx, latch);
          try {
            if (!latch.await(30 * 1000, TimeUnit.MILLISECONDS)) {
              Logger logger = LoggerFactory.getLogger(description.getTestClass());
              logger.warn("Could not close Vert.x in tme");
            }
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        }
      }
    };
  }
}

