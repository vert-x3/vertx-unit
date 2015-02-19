package verticle

import io.vertx.groovy.core.Future
import io.vertx.groovy.ext.unit.TestSuite

def vertxStart(Future future) {
  def suite = TestSuite.create("my_suite").test "timer_test", { test ->
    def async = test.async()
    vertx.setTimer 50, {
      test.fail("the_failure")
    }
  }
  suite.run([:]).resolve(future)
}