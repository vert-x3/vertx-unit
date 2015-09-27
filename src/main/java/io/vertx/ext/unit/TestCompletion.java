package io.vertx.ext.unit;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.unit.report.TestSuiteReport;

/**
 * This object provides callback-ability for the end of a test suite, the completion <i>succeeds</i>
 * when all tests pass otherwise it fails.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface TestCompletion extends Completion<Void> {
}
