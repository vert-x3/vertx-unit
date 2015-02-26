var TestSuite = require('vertx-unit-js/test_suite');

var suite = TestSuite.create("my_suite").
    before(function(test) {
        var async = test.async();
        vertx.deployVerticle("js:verticle/coordinated/server", function(id, err) {
            test.assertTrue(err === null);
            async.complete();
        });
    }).test("server_get", function(test) {
        var async = test.async();
        var client = vertx.createHttpClient({});
        client.request("GET", 8080, "localhost", "/path", function(resp) {
            test.assertTrue(resp.statusCode() == 200);
            async.complete();
        }).end();
    });

suite.run(vertx, { reporters : [{ to: "bus", at: "test" }] });
