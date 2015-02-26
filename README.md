## Unit Test for Vert.x

Async polyglot unit testing for Vert.x.

## Documentation

See https://github.com/vert-x3/vertx-unit/blob/initial-work/src/main/asciidoc/java/index.adoc

## Todo

- when asserting
    - check the same context is used when running with a context (configurable)
    - ensure that any assert is done before the completion of a test, at the moment we are doing that
      but only in the case of failure
- in reporting : use a syntax like to:"file:..." , to: "bus:the-address" instead of to+at
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

