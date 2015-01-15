import io.vertx.ext.unit.Unit

def module = Unit.test "Timer test", { test ->
  def async = test.async()
  vertx.setTimer 50, {
    async.complete()
  }
}

def exec = module.exec()
exec.endHandler {
  vertx.eventBus().send("test", "done")
}
exec.run()