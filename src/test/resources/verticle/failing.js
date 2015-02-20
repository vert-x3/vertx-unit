exports.vertxStartAsync = function(future) {
    var TestSuite = require('vertx-unit-js/test_suite');
    var suite = TestSuite.create("time_suite").test("timer_test", function(test) {
        var async = test.async();
        vertx.setTimer(50, function() {
            test.fail("the_failure");
        });
    });
    suite.run().resolve(future);
};