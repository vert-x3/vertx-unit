## Unit Test for Vert.x

Async polyglot unit testing for Vert.x inspired from Qunit (but not only).

## Status

Proof of concept

## How does it look like ?

### Simple test

~~~
Unit.test("my test", test -> {
  test.assertTrue(true);
}).execute();
~~~

### Async test

#### Java

~~~
Unit.asyncTest("my test", test -> {
  vertx.setTimer(50, id -> test.complete());
}).execute();
~~~

#### JavaScript

~~~
var Unit = require('vertx-unit-js/unit')
Unit.asyncTest("Timer test", function(test) {
    vertx.setTimer(50, function() {
       test.complete();
    });
}).execute();
~~~
