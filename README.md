## Unit Test for Vert.x

Async polyglot unit testing for Vert.x inspired from Qunit (but not only).

## Status

Proof of concept

## How does it look like ?

### Simple test

~~~
Unit.test("my test", test -> {
  test.assertTrue(true);
}).exec().run();
~~~

### Async test

#### Java

~~~
Unit.asyncTest("my test", test -> {
  Async async = test.async();
  vertx.setTimer(50, id -> async.complete());
}).exec().run();
~~~

#### JavaScript

~~~
var Unit = require('vertx-unit-js/unit')
Unit.asyncTest("Timer test", function(test) {
    var async = test.async();
    vertx.setTimer(50, function() {
       async.complete();
    });
}).exec().run();
~~~

#### Groovy

~~~
def module = Unit.test "Timer test", { test ->
  def async = test.async()
  vertx.setTimer 50, {
    async.complete()
  }
}
module.exec().run();
~~~
