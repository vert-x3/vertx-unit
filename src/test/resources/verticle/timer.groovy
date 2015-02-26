package verticle

import io.vertx.groovy.core.Future
import io.vertx.groovy.ext.unit.TestSuite

def vertxStart(Future future) {
  def suite = TestSuite.create("my_suite").test "timer_test", { context ->
    def async = context.async()
    vertx.setTimer 50, {
      async.complete()
    }
  }
  suite.run().resolve(future);
}