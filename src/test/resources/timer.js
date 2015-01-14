var Unit = require('vertx-unit-js/unit')

Unit.asyncTest("Timer test", function(test) {
    vertx.setTimer(50, function() {
       test.complete();
    });
}).execute();
