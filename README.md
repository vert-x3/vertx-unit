## Unit Test for Vert.x

Async polyglot unit testing for Vert.x inspired from Qunit (but not only).

## Todo

- handle reporter/formatter failures
- same name test replaces previous ?
- json stream reporter
- reporters
    - xunit
    - markdown
    - tap http://en.wikipedia.org/wiki/Test_Anything_Protocol

## Status

Proof of concept

## Run directly

Vert.x unit can be executed directly from your language and use Vert.x

### Simple test

~~~
Unit.test("my test", test -> {
  test.assertTrue(true);
}).runner().run();
~~~

### Async test

#### Java

~~~
Vertx vertx = Vertx.vertx();
Unit.asyncTest("my test", test -> {
  Async async = test.async();
  vertx.setTimer(50, id -> async.complete());
}).runner().run();
~~~

#### JavaScript

~~~
var Unit = require('vertx-unit-js/unit')
var Vertx = require('vertx-js')
var vertx = Vertx.vertx();
Unit.asyncTest("Timer test", function(test) {
    var async = test.async();
    vertx.setTimer(50, function() {
       async.complete();
    });
}).runner().run(vertx);
~~~

#### Groovy

~~~
def vertx = Vertx.vertx();
def suite = Unit.test "Timer test", { test ->
  def async = test.async()
  vertx.setTimer 50, {
    async.complete()
  }
}
suite.runner().run(vertx);
~~~

## Run from JUnit

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
