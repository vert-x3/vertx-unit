package plain;

import io.vertx.core.Vertx
import io.vertx.ext.unit.TestSuite

def vertx = Vertx.vertx()

def suite = TestSuite.create("my_suite").test "my_test", { context ->
  def async = context.async()
  vertx.setTimer 50, {
    async.complete()
  }
}

suite.run(vertx).handler({
  // Signal the junit runner we are done
  done.run();
});
