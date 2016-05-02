package verticle

import io.vertx.groovy.core.Future
import io.vertx.groovy.ext.unit.TestSuite

def vertxStart(Future future) {
  def suite = TestSuite.create("my_suite").beforeEach({ context ->
    vertx.exceptionHandler(context.exceptionHandler())
  }).test "timer_test", { context ->
    def async = context.async()
    vertx.setTimer 50, {
      throw new AssertionError("the_failure")
    }
  }
  suite.run().resolve(future)
}