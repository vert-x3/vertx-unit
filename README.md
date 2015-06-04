## Unit Test for Vert.x

[![Build Status](https://vertx.ci.cloudbees.com/buildStatus/icon?job=vert.x3-unit)](https://vertx.ci.cloudbees.com/view/vert.x-3/job/vert.x3-unit/)

Async polyglot unit testing for Vert.x.

## Documentation

See https://github.com/vert-x3/vertx-unit/blob/master/src/main/asciidoc/java/index.adoc

## Todo

- find a way to configure junit runner timeout
- when asserting
    - check the same context is used when running with a context (configurable)
    - ensure that any assert is done before the completion of a test, at the moment we are doing that
      but only in the case of failure
- provide subtest programming model (i.e add a tests in a test)
- consider making TestResult an @DataObject instead
- add closeable resource to register for automatic cleanup or do something like this
- see how to extend state sharing to non json / non basic
- filtering
- handle reporter/formatter failures
- same name test replaces previous ?
- json stream reporter
- reporters
    - xunit
    - markdown
    - tap http://en.wikipedia.org/wiki/Test_Anything_Protocol

