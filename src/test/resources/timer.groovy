import io.vertx.groovy.ext.unit.Unit

def suite = Unit.test "Timer test", { test ->
  def async = test.async()
  vertx.setTimer 50, {
    async.complete()
  }
}

def runner = suite.runner()
runner.endHandler {
  vertx.eventBus().send("test", "done")
}
runner.run(vertx)