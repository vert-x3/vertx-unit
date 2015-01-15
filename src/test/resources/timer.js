var Unit = require('vertx-unit-js/unit')

var module = Unit.test("Timer test", function(test) {
    var async = test.async();
    vertx.setTimer(50, function() {
       async.complete();
    });
});

var exec = module.exec();
exec.endHandler(function() {
    vertx.eventBus().send("test", "done");
});
exec.run();
