var Unit = require('vertx-unit-js/unit');

var suite = Unit.suite().
    before(function(test) {
        var async = test.async();
        vertx.deployVerticle("coordinated/server.js", function(id, err) {
            test.assertTrue(err === null);
            async.complete();
        });
    }).test("server GET", function(test) {
        var async = test.async();
        var client = vertx.createHttpClient({});
        client.request("GET", 8080, "localhost", "/path", function(resp) {
            test.assertTrue(resp.statusCode() == 200);
            async.complete();
        }).end();
    });

var runner = suite.runner();
runner.endHandler(function() {
    vertx.eventBus().send("test", "done");
});
runner.run();
