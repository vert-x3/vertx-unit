package plain;

import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.unit.TestSuite

def vertx = Vertx.vertx()

def suite = TestSuite.create().test "Timer test", { test ->
  def async = test.async()
  vertx.setTimer 50, {
    async.complete()
  }
}

def runner = suite.runner()
runner.endHandler {
  // Signal the junit runner we are done
  done.run();
}
runner.run(vertx)