var Unit = require('vertx-unit-js/unit')

var suite = Unit.test("Timer test", function(test) {
    var async = test.async();
    vertx.setTimer(50, function() {
       async.complete();
    });
});

var runner = suite.runner();
runner.endHandler(function() {
    vertx.eventBus().send("test", "done");
});
runner.run(vertx);
