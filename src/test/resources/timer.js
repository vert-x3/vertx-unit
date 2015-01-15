var Unit = require('vertx-unit-js/unit')

var module = Unit.asyncTest("Timer test", function(test) {
    vertx.setTimer(50, function() {
       test.complete();
    });
});

var exec = module.exec();
exec.endHandler(function() {
    vertx.eventBus().send("test", "done");
});
exec.run();
