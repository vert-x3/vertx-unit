/**
 * = Vertx unit
 *
 * Asynchronous polyglot unit testing.
 *
 * == Introduction
 *
 * Vertx Unit aims to make Vertx applications testable. It provides a polyglot API for writing asynchronous
 * tests easily. Vertx Unit Api borrows from existing test frameworks like http://junit.org[JUnit] or http://qunitjs.com[QUnit]
 * and follows the Vert.x practices.
 *
 * Vertx Unit can be used in different ways and run anywhere your code runs, it is just a matter of reporting
 * the results the right way, this example shows the bare minimum test suite:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#test_01}
 * ----
 *
 * The {@code run} method will execute the suite and go through all the
 * tests of the suite. The suite can fail or pass, this does not matter if the outter world is not aware
 * of the test result.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#test_02}
 * ----
 *
 * When executed, the test suite now reports to the console the steps of the test suite:
 *
 * ----
 * Begin test suite the_test_suite
 * Begin test my_test
 * Passed my_test
 * End test suite the_test_suite , run: 1, Failures: 0, Errors: 0
 * ----
 *
 * The {@code reporters} option configures the reporters used by the suite runner for reporting the execution
 * of the tests, see the <<reporting>> section for more info.
 *
 * == Writing a test suite
 *
 * A test suite is a named collection of test case, a test case is a straight callback to execute. The suite can
 * have lifecycle callbacks to execute _before_ and/or _after_ the test cases or the test suite that are used for
 * initializing or disposing services used by the test suite.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#writing_test_suite_01}
 * ----
 *
 * The API is fluent and therefore the test can be chained:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#writing_test_suite_02}
 * ----
 *
 * Vertx Unit provides _before_ and _after_ callbacks for doing global setup or cleanup:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#writing_test_suite_03}
 * ----
 *
 * The declaration order of the method does not matter, the example declares the _before_ callback before
 * the test cases and _after_ callback after the test cases but it could be anywhere, as long as it is done before
 * running the test suite.
 *
 * The _before_ callback is executed before any tests, when it fails, the test suite execution will stop and the
 * failure is reported. The _after_ callback is the last callback executed by the testsuite, unless
 * the _before_ callback reporter a failure.
 *
 * Likewise, Vertx Unit provides the _beforeEach_ and _afterEach_ callback that do the same but are executed
 * for each test case:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#writing_test_suite_04}
 * ----
 *
 * The _beforeEach_ callback is executed before each test case, when it fails, the test case is not executed and the
 * failure is reported. The _afterEach_ callback is the executed just after the test case callback, unless
 * the _beforeEach_ callback reported a failure.
 *
 * == Asserting
 *
 * Vertx Unit provides the _test_ object for doing assertions in test cases. The _test_ object provides the usual
 * methods when dealing with assertions.
 *
 * === assertEquals
 *
 * Assert two objects are equals, works for _basic_ types or _json_ types.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#asserting_01}
 * ----
 *
 * There is also an overloaded version for providing a message:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#asserting_02}
 * ----
 *
 * Usually each assertion provides an overloaded version.
 *
 * === assertNotEquals
 *
 * The counter part of _assertEquals_.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#asserting_03}
 * ----
 *
 * === assertTrue and assertFalse
 *
 * Asserts the value of a boolean expression.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#asserting_04}
 * ----
 *
 * === Failing
 *
 * Last but not least, _test_ provides a _fail_ method that will throw an assertion error:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#asserting_05}
 * ----
 *
 * == Asynchronous testing
 *
 * todo
 *
 * [[reporting]]
 * == Reporting
 *
 * todo
 *
 * == Junit integration
 *
 * todo
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@GenModule(name = "vertx-unit")
@Document(fileName = "index.adoc")
package io.vertx.ext.unit;

import io.vertx.codegen.annotations.GenModule;
import io.vertx.docgen.Document;