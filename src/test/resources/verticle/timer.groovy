package verticle

import io.vertx.groovy.ext.unit.TestSuite

def suite = TestSuite.create("my_suite").test "timer_test", { test ->
  def async = test.async()
  vertx.setTimer 50, {
    async.complete()
  }
}

suite.run(vertx, [reporters:[[to:"bus",at:"test"]]]);
