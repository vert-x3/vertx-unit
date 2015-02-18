package plain;

import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.unit.TestSuite

def vertx = Vertx.vertx()

def suite = TestSuite.create("my_suite").test "my_test", { test ->
  def async = test.async()
  vertx.setTimer 50, {
    async.complete()
  }
}

suite.run(vertx) { report ->
  report.endHandler {
    // Signal the junit runner we are done
    done.run();
  }
}
