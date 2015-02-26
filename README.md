## Unit Test for Vert.x

Async polyglot unit testing for Vert.x inspired from Qunit (but not only).

## Documentation

See https://github.com/vert-x3/vertx-unit/blob/initial-work/src/main/asciidoc/java/index.adoc

## Todo

- when executing a test with a context, check that the various asserts are executed with the *same* context
- add a blocking mode to wait for the result of a test suite using a latch
- provide subtest programming model (i.e add a tests in a test)
- consider making TestResult an @DataObject instead
- add closeable resource to register for automatic cleanup or do something like this
- object sharing via io.vertx.core.Context#put
- report to directory instead of file (for multiplexing based on test suite name)
- filtering
- handle reporter/formatter failures
- same name test replaces previous ?
- json stream reporter
- reporters
    - xunit
    - markdown
    - tap http://en.wikipedia.org/wiki/Test_Anything_Protocol

