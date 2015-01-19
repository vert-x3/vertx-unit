## Unit Test for Vert.x

Async polyglot unit testing for Vert.x inspired from Qunit (but not only).

## Status

Proof of concept

## Running in a Verticle

### Simple test

~~~
Unit.test("my test", test -> {
  test.assertTrue(true);
}).runner().run();
~~~

### Async test

#### Java

~~~
Unit.asyncTest("my test", test -> {
  Async async = test.async();
  vertx.setTimer(50, id -> async.complete());
}).runner().run();
~~~

#### JavaScript

~~~
var Unit = require('vertx-unit-js/unit')
Unit.asyncTest("Timer test", function(test) {
    var async = test.async();
    vertx.setTimer(50, function() {
       async.complete();
    });
}).runner().run();
~~~

#### Groovy

~~~
def suite = Unit.test "Timer test", { test ->
  def async = test.async()
  vertx.setTimer 50, {
    async.complete()
  }
}
suite.runner().run();
~~~

## Running with JUnit

### JUnit assertion

Run a single test and blocks until it pass or fails, failures are rethrown by the `assertSuccess()` method.

~~~
Unit.test("my_test", test -> { test.assertTrue(true) }).runner().assertSuccess();
~~~


### JUnit test suite

A JUnit testsuite can be created from a Vert.x unit suite.

~~~
@RunWith(AllTests.class)
public class JUnitTestSuite {

  public static TestSuite suite() {
    return Unit.suite().
      test("test 1", test -> {
        test.assertTrue(true);
      }).
      test("test 2", test -> {
        // Test 2
      }).toJUnitSuite();
  }
}
~~~

## Todo

- reporting to console via event bus
- assertion api (http://joel-costigliola.github.io/assertj/)
