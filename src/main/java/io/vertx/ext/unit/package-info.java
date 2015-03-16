/**
 * = Vertx unit
 *
 * Asynchronous polyglot unit testing.
 *
 * == Introduction
 *
 * Vertx Unit is designed for writing asynchronous unit tests with a polyglot API and running these tests
 * in the JVM. Vertx Unit Api borrows from existing test frameworks like http://junit.org[JUnit] or http://qunitjs.com[QUnit]
 * and follows the Vert.x practices.
 *
 * As a consequence Vertx Unit is the natural choice for testing Vert.x applications.
 *
 * It can be used in different ways and run anywhere your code runs, it is just a matter of reporting
 * the results the right way, this example shows the bare minimum test suite:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#test_01}
 * ----
 *
 * The {@code run} method will execute the suite and go through all the
 * tests of the suite. The suite can fail or pass, this does not matter if the outer world is not aware
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
 * The API is fluent and therefore the test cases can be chained:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#writing_test_suite_02}
 * ----
 *
 * The test cases declaration order is not guaranteed, so test cases should not rely on the execution of
 * another test case to run. Such practice is considered as a bad one.
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
 * Vertx Unit provides the {@link io.vertx.ext.unit.TestContext} object for doing assertions in test cases. The _context_
 * object provides the usual methods when dealing with assertions.
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
 * === assertNull
 *
 * Assert an object is null, works for _basic_ types or _json_ types.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#asserting_04}
 * ----
 *
 * === assertNotNull
 *
 * The counter part of _assertNull_.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#asserting_05}
 * ----
 *
 * === assertInRange
 *
 * The {@link io.vertx.ext.unit.TestContext#assertInRange} targets real numbers.
 *
 * ----
 * {@link examples.Examples#asserting_06}
 * ----
 *
 * === assertTrue and assertFalse
 *
 * Asserts the value of a boolean expression.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#asserting_07}
 * ----
 *
 * === Failing
 *
 * Last but not least, _test_ provides a _fail_ method that will throw an assertion error:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#asserting_08}
 * ----
 *
 * == Asynchronous testing
 *
 * The previous examples supposed that test cases were terminated after their respective callbacks, this is the
 * default behavior of a test case callback. Often it is desirable to terminate the test after the test case
 * callback, for instance:
 *
 * .The Async object asynchronously completes the test case
 * [source,$lang]
 * ----
 * {@link examples.Examples#async_01}
 * ----
 * <1> The callback exits but the test case is not terminated
 * <2> The event callback from the bus terminates the test
 *
 * Creating an {@link io.vertx.ext.unit.Async} object with the {@link io.vertx.ext.unit.TestContext#async()} method marks the
 * executed test case as non terminated. The test case terminates when the {@link io.vertx.ext.unit.Async#complete()}
 * method is invoked.
 *
 * NOTE: When the `complete` callback is not invoked, the test case fails after a certain timeout.
 *
 * Several `Async` objects can be created during the same test case, all of them must be _completed_ to terminate
 * the test.
 *
 * .Several Async objects provide coordination
 * [source,$lang]
 * ----
 * {@link examples.Examples#async_02}
 * ----
 *
 * Async objects can also be used in _before_ or _after_ callbacks, it can be very convenient in a _before_ callback
 * to implement a setup that depends on one or several asynchronous results:
 *
 * .Async starts an http server before test cases
 * [source,$lang]
 * ----
 * {@link examples.Examples#async_03}
 * ----
 *
 * == Sharing objects
 *
 * The {@link io.vertx.ext.unit.TestContext} has `get`/`put`/`remove` operations for sharing state between callbacks.
 *
 * Any object added during the _before_ callback is available in any other callbacks. Each test case will operate on
 * a copy of the shared state, so updates will only be visible for a test case.
 *
 * .Sharing state between callbacks
 * [source,$lang]
 * ----
 * {@link examples.Examples#sharing_01}
 * ----
 *
 * WARNING: sharing any object is only supported in Java, other languages can share only basic or json types.
 * Other objects should be shared using the features of that language.
 *
 * [[reporting]]
 * == Running
 *
 * When a test suite is created, it won't be executed until the {@link io.vertx.ext.unit.TestSuite#run} method
 * is called.
 *
 * .Running a test suite
 * [source,$lang]
 * ----
 * {@link examples.Examples#running_01}
 * ----
 *
 * The test suite can also be ran with a specified {@link io.vertx.core.Vertx} instance:
 *
 * .Provides a Vertx instance to run the test suite
 * [source,$lang]
 * ----
 * {@link examples.Examples#running_02}
 * ----
 *
 * When running with a `Vertx` instance, the test suite is executed using the Vertx event loop, see the <<eventloop>>
 * section for more details.
 *
 * === Test suite completion
 *
 * No assumptions can be made about when the test suite will be completed, and if some code needs to be executed
 * after the test suite, it should either be in the test suite _after_ callback or as callback of the
 * {@link io.vertx.ext.unit.TestCompletion}:
 *
 * .Test suite execution callback
 * [source,$lang]
 * ----
 * {@link examples.Examples#completion_01}
 * ----
 *
 * The {@link io.vertx.ext.unit.TestCompletion} object provides also a {@link io.vertx.ext.unit.TestCompletion#resolve} method that
 * takes a `Future` object, this `Future` will be notified of the test suite execution:
 *
 * .Resolving the start Future with the test suite
 * [source,$lang]
 * ----
 * {@link examples.Examples#completion_02}
 * ----
 *
 * This allow to easily create a _test_ verticle whose deployment is the test suite execution, allowing the
 * code that deploys it to be easily aware of the success or failure.
 *
 * The completion object can also be used like a latch to block until the test suite completes. This should
 * be used when the thread running the test suite is not the same than the current thread:
 *
 * .Blocking until the test suite completes
 * [source,$lang]
 * ----
 * {@link examples.Examples#completion_03}
 * ----
 *
 * === Time out
 *
 * Each test case of a test suite must execute before a certain timeout is reached. The default timeout is
 * of _2 minutes_, it can be changed using _test options_:
 *
 * .Setting the test suite timeout
 * [source,$lang]
 * ----
 * {@link examples.Examples#running_05}
 * ----
 *
 * [[event_loop]]
 * === Event loop
 *
 * Vertx Unit execution is a list of tasks to execute, the execution of each task is driven by the completion
 * of the previous task. These tasks should leverage Vert.x event loop when possible but that depends on the
 * current execution context (i.e the test suite is executed in a `main` or embedded in a `Verticle`) and
 * wether or not a `Vertx` instance is configured.
 *
 * The {@link io.vertx.ext.unit.TestOptions#setUseEventLoop(java.lang.Boolean)} configures the usage of the event
 * loop:
 *
 * .Event loop usage
 * |===
 * | | useEventLoop:null | useEventLoop:true | useEventLoop:false
 *
 * | `Vertx` instance
 * | use vertx event loop
 * | use vertx event loop
 * | force no event loop
 *
 * | in a `Verticle`
 * | use current event loop
 * | use current event loop
 * | force no event loop
 *
 * | in a _main_
 * | use no event loop
 * | raise an error
 * | use no event loop
 *
 * |===
 *
 * The default `useEventLoop` value is `null`, that means that it will uses an event loop when possible and fallback
 * to no event loop when no one is available.
 *
 * == Reporting
 *
 * Reporting is an important piece of a test suite, Vertx Unit can be configured to run with different kind
 * of reporters.
 *
 * By default no reporter is configured, when running a test suite, _test options_ can be provided to
 * configure one or several:
 *
 * .Using the console reporter and as a junit xml file
 * [source,$lang]
 * ----
 * {@link examples.Examples#reporter_01}
 * ----
 *
 * === Console reporting
 *
 * Reports to the JVM `System.out` and `System.err`:
 *
 * to::
 * _console_
 * format::
 * _simple_ or _junit_
 *
 * === File reporting
 *
 * Reports to a file, a `Vertx` instance must be provided:
 *
 * to::
 * _file_ `:` _dir name_
 * format::
 * _simple_ or _junit_
 * example::
 * `file:.`
 *
 * The file reporter will create files in the configured directory, the files will be named after the
 * test suite name executed and the format (i.e _simple_ creates _txt_ files and _junit_ creates _xml_
 * files).
 *
 * === Log reporting
 *
 * Reports to a logger, a `Vertx` instance must be provided:
 *
 * to::
 * _log_ `:` _logger name_
 * example::
 * `log:mylogger`
 *
 * === Event bus reporting
 *
 * Reports events to the event bus, a `Vertx` instance must be provided:
 *
 * to::
 * _bus_ `:` _event bus address_
 * example::
 * `bus:the-address`
 *
 * It allow to decouple the execution of the test suite from the reporting.
 *
 * The messages sent over the event bus can be collected by the {@link io.vertx.ext.unit.collect.EventBusCollector}
 * and achieve custom reporting:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#reporter_02}
 * ----
 *
 * == Junit integration
 *
 * Although Vertx Unit is polyglot and not based on JUnit, it is possible to run a Vertx Unit test suite or a test case
 * from JUnit, allowing you to integrate your tests with JUnit and your build system or IDE.
 *
 * .Run a Java class as a JUnit test suite
 * [source,java]
 * ----
 * &#64;RunWith(io.vertx.ext.unit.junit.VertxUnitRunner.class)
 * public class JUnitTestSuite {
 *
 *   &#64;Test
 *   public void testSomething(Context context) {
 *     context.assertFalse(false);
 *   }
 * }
 * ----
 *
 * The {@link io.vertx.ext.unit.junit.VertxUnitRunner} uses the junit annotations for introspecting the class
 * and create a test suite after the class. The methods should declare a {@link io.vertx.ext.unit.TestContext}
 * argument, if they don't it is fine too. However the `TestContext` is the only way to retrieve the associated
 * Vertx instance of perform asynchronous tests.
 *
 * A single test case can also be executed with a {@link io.vertx.ext.unit.TestCase}:
 *
 * .Run a test case in a JUnit test
 * [source,$lang]
 * ----
 * TestCase.
 *   create("my_test_case", context -> {
 *     context.assertTrue(true);
 *   }).
 *   awaitSuccess(); // <1>
 * ----
 * <1> Block until the test case is executed
 *
 * == Java language integration
 *
 * The Java language provides classes and it is possible to create test suites directly from Java classes with the
 * following mapping rules:
 *
 * The {@code testSuiteObject} argument methods are inspected and the public, non static methods
 * with {@link io.vertx.ext.unit.TestContext} parameter are retained and mapped to a Vertx Unit test suite
 * via the method name:
 *
 * * `before` : before callback
 * * `after` : after callback
 * * `beforeEach` : beforeEach callback
 * * `afterEach` : afterEach callback
 * *  when the name starts with _test_ : test case callback named after the method name
 *
 * .Test suite written using a Java class
 * [source,java]
 * ----
 * public class MyTestSuite {
 *
 *   public void testSomething(TestContext context) {
 *     context.assertFalse(false);
 *   }
 * }
 * ----
 *
 * This class can be turned into a Vertx test suite easily:
 *
 * .Create a test suite from a Java object
 * [source,java]
 * ----
 * TestSuite suite = TestSuite.create(new MyTestSuite());
 * ----
 */
@GenModule(name = "vertx-unit")
@Document(fileName = "index.adoc")
package io.vertx.ext.unit;

import io.vertx.codegen.annotations.GenModule;
import io.vertx.docgen.Document;