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
 * To use vert.x unit, add the following dependency to the _dependencies_ section of your build descriptor:
 *
 * * Maven (in your `pom.xml`):
 *
 * [source,xml,subs="+attributes"]
 * ----
 * <dependency>
 *   <groupId>${maven.groupId}</groupId>
 *   <artifactId>${maven.artifactId}</artifactId>
 *   <version>${maven.version}</version>
 *   <scope>test</scope>
 * </dependency>
 * ----
 *
 * * Gradle (in your `build.gradle` file):
 *
 * [source,groovy,subs="+attributes"]
 * ----
 * testCompile ${maven.groupId}:${maven.artifactId}:${maven.version}
 * ----
 *
 * Vert.x unit can be used in different ways and run anywhere your code runs, it is just a matter of reporting
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
 * [source,$lang]
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
 * The failure can either be a _string_ as seen previously or an _error_. The _error_ object depends
 * on the target language, for Java or Groovy it can be any class extending _Throwable- , for
 * JavaScript it is an _error_, for Ruby it is an _Exception_.
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
 * It is possible to wait until the completion of a specific {@link io.vertx.ext.unit.Async}, similar
 * to Java's count-down latch:
 *
 * .Wait for completion
 * [source, $lang]
 * ----
 * {@link examples.Examples#async_04(io.vertx.ext.unit.TestContext, io.vertx.core.Vertx, io.vertx.core.Handler)}
 * ----
 *
 * WARNING: this should not be executed from the event loop!
 *
 * Async can also be created with an initial count value, it completes when the count-down reaches
 * zero using {@link io.vertx.ext.unit.Async#countDown()}:
 *
 * .Wait until the complete count-down reaches zero
 * [source, $lang]
 * ----
 * {@link examples.Examples#async_05(io.vertx.ext.unit.TestContext, io.vertx.core.Vertx, io.vertx.core.Handler)}
 * ----
 *
 * Calling `complete()` on an async completes the async as usual, it actually sets the value to `0`.
 *
 * == Asynchronous assertions
 *
 * {@link io.vertx.ext.unit.TestContext} provides useful methods that provides powerful constructs for async testing:
 *
 * The {@link io.vertx.ext.unit.TestContext#asyncAssertSuccess()} method returns an {@literal Handler<AsyncResult<T>>}
 * instance that acts like {@link io.vertx.ext.unit.Async}, resolving the `Async` on success and failing the test
 * on failure with the failure cause.
 *
 * [source,java]
 * ----
 * {@link examples.Examples#asyncAssertSuccess_01}
 * ----
 *
 * The {@link io.vertx.ext.unit.TestContext#asyncAssertSuccess(io.vertx.core.Handler)} method returns an {@literal Handler<AsyncResult<T>>}
 * instance that acts like {@link io.vertx.ext.unit.Async}, invoking the delegating {@literal Handler<T>} on success
 * and failing the test on failure with the failure cause.
 *
 * [source,java]
 * ----
 * {@link examples.Examples#asyncAssertSuccess_02}
 * ----
 *
 * The async is completed when the `Handler` exits, unless new asyncs were created during the invocation, which
 * can be handy to _chain_ asynchronous behaviors:
 *
 * [source,java]
 * ----
 * {@link examples.Examples#asyncAssertSuccess_03}
 * ----
 *
 * The {@link io.vertx.ext.unit.TestContext#asyncAssertFailure()} method returns an {@literal Handler<AsyncResult<T>>}
 * instance that acts like {@link io.vertx.ext.unit.Async}, resolving the `Async` on failure and failing the test
 * on success.
 *
 * [source,java]
 * ----
 * {@link examples.Examples#asyncAssertFailure_01(io.vertx.core.Vertx, io.vertx.ext.unit.TestContext)}
 * ----
 *
 * The {@link io.vertx.ext.unit.TestContext#asyncAssertFailure(io.vertx.core.Handler)} method returns an {@literal Handler<AsyncResult<T>>}
 * instance that acts like {@link io.vertx.ext.unit.Async}, invoking the delegating {@literal Handler<Throwable>} on
 * failure and failing the test on success.
 *
 * [source,java]
 * ----
 * {@link examples.Examples#asyncAssertFailure_02(io.vertx.core.Vertx, io.vertx.ext.unit.TestContext)}
 * ----
 *
 * The async is completed when the `Handler` exits, unless new asyncs were created during the invocation.
 *
 * == Repeating test
 *
 * When a test fails randomly or not often, for instance a race condition, it is convenient to run the same
 * test multiple times to increase the failure likelihood of the test.
 *
 * .Repeating a test
 * [source,$lang]
 * ----
 * {@link examples.Examples#repeating(io.vertx.core.Vertx)}
 * ----
 *
 * When declared, _beforeEach_ and _afterEach_ callbacks will be executed as many times as the test is executed.
 *
 * NOTE: test repetition are executed sequentially
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
 * The test suite can also be run with a specified {@link io.vertx.core.Vertx} instance:
 *
 * .Provides a Vertx instance to run the test suite
 * [source,$lang]
 * ----
 * {@link examples.Examples#running_02}
 * ----
 *
 * When running with a `Vertx` instance, the test suite is executed using the Vertx event loop, see the <<event_loop>>
 * section for more details.
 *
 * A test suite can be run with the Vert.x Command Line Interface with the `vertx test` command:
 *
 * .Running a test suite with the Vert.x CLI
 * [source]
 * ----
 * > vertx test the_test_suite.js
 * Begin test suite the_test_suite
 * Succeeded in deploying verticle
 * Begin test my_test_case
 * Passed my_test_case
 * End test suite my_suite , run: 1, Failures: 0, Errors: 0
 * ----
 *
 * Such test suite just need to be executed via the {@link io.vertx.ext.unit.TestSuite#run()} command, the
 * `vertx test` command takes care of configuring reporting, timeout, etc..., pretty much like in this
 * example:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#test_01}
 * ----
 *
 * The `vertx test` command extends the `vertx run` command. The exit behavior of the JVM is changed
 * the JVM exits when the test suite is executed and a return value is provided indicating the tests
 * success (0) or failure (1).
 *
 * NOTE: several test suites can executed in the same verticle, Vert.x Unit waits until completion of
 * all suite executed.
 *
 * === Test suite completion
 *
 * No assumptions can be made about when the test suite will be completed, and if some code needs to be executed
 * after the test suite, it should either be in the test suite _after_ callback or as callback of the
 * {@link io.vertx.ext.unit.Completion}:
 *
 * .Test suite execution callback
 * [source,$lang]
 * ----
 * {@link examples.Examples#completion_01}
 * ----
 *
 * The {@link io.vertx.ext.unit.Completion} object provides also a {@link io.vertx.ext.unit.Completion#resolve} method that
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
 * The `await` throws an exception when the thread is interrupted or a timeout is fired.
 *
 * The {@link io.vertx.ext.unit.Completion#awaitSuccess()} is a variation that throws an exception when
 * the test suite fails.
 *
 * .Blocking until the test suite succeeds
 * [source,$lang]
 * ----
 * {@link examples.Examples#completion_04}
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
 * [[reporting]]
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
 * [[vertx_integration]]
 * == Vertx integration
 *
 * By default, assertions and failures must be done on the {@link io.vertx.ext.unit.TestContext} and throwing an
 * assertion error works only when called by Vert.x Unit:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#vertxInteg1}
 * ----
 *
 * In a regular Vert.x callback, the failure will be ignored:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#vertxInteg2}
 * ----
 *
 * Since Vert.x 3.3, a global exception handler can be set to report the event loop uncaught exceptions:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#vertxInteg3}
 * ----
 *
 * The exception handler is set during the _before_ phase, the {@link io.vertx.ext.unit.TestContext} is shared
 * between each _before_, _test_ and _after_ phase. So the exception handler obtained during the _before_ phase
 * is correct.
 *
 * == Junit integration
 *
 * Although Vertx Unit is polyglot and not based on JUnit, it is possible to run a Vertx Unit test suite or a test case
 * from JUnit, allowing you to integrate your tests with JUnit and your build system or IDE.
 *
 * .Run a Java class as a JUnit test suite
 * [source,java]
 * ----
 * {@link examples.junit.JUnitTestSuite}
 * ----
 *
 * The {@link io.vertx.ext.unit.junit.VertxUnitRunner} uses the junit annotations for introspecting the class
 * and create a test suite after the class. The methods should declare a {@link io.vertx.ext.unit.TestContext}
 * argument, if they don't it is fine too. However the `TestContext` is the only way to retrieve the associated
 * Vertx instance of perform asynchronous tests.
 *
 * The JUnit integration is also available for the Groovy language with the `io.vertx.groovy.ext.unit.junit.VertxUnitRunner`
 * runner.
 *
 * === Running a test on a Vert.x context
 *
 * By default the thread invoking the test methods is the JUnit thread. The {@link io.vertx.ext.unit.junit.RunTestOnContext}
 * JUnit rule can be used to alter this behavior for running these test methods with a Vert.x event loop thread.
 *
 * Thus there must be some care when state is shared between test methods and Vert.x handlers as they won't be
 * on the same thread, e.g incrementing a counter in a Vert.x handler and asserting the counter in the test method.
 * One way to solve this is to use proper synchronization, another is to execute test methods on a Vert.x context
 * that will be propagated to the created handlers.
 *
 * For this purpose the {@link io.vertx.ext.unit.junit.RunTestOnContext} rule needs a {@link io.vertx.core.Vertx}
 * instance. Such instance can be provided, otherwise the rule will manage an instance under the hood. Such
 * instance can be retrieved when the test is running, making this rule a way to manage a {@link io.vertx.core.Vertx}
 * instance as well.
 *
 * .Run a Java class as a JUnit test suite
 * [source,java]
 * ----
 * {@link examples.junit.RunOnContextJUnitTestSuite}
 * ----
 *
 * The rule can be annotated by {@literal @Rule} or {@literal @ClassRule}, the former manages a Vert.x instance
 * per test, the later a single Vert.x for the test methods of the class.
 *
 * WARNING: keep in mind that you cannot block the event loop when using this rule. Usage of classes like
 * `CountDownLatch` or similar classes must be done with care.
 *
 * === Timeout
 *
 * The Vert.x Unit 2 minutes timeout can be overriden with the `timeout` member of the `@Test` annotation:
 *
 * .Configure the timeout at the test level
 * [source,java]
 * ----
 * {@link examples.junit.JunitTestWithTimeout}
 * ----
 *
 * For a more global configuration, the {@link io.vertx.ext.unit.junit.Timeout} rule can be used:
 *
 * .Configure the timeout at the class level
 * [source,java]
 * ----
 * {@link examples.junit.TimeoutTestSuite}
 * ----
 *
 * NOTE: the `@Test` timeout overrides the the {@link io.vertx.ext.unit.junit.Timeout} rule.
 *
 * === Parameterized tests
 *
 * JUnit provides useful `Parameterized` tests, Vert.x Unit tests can be ran with this particular runner thanks to
 * the {@link io.vertx.ext.unit.junit.VertxUnitRunnerWithParametersFactory}:
 *
 * .Running a Vert.x Unit parameterized test
 * [source,java]
 * ----
 * {@link examples.junit.SimpleParameterizedTest}
 * ----
 *
 * Parameterized tests can also be done in Groovy with the `io.vertx.groovy.ext.unit.junit.VertxUnitRunnerWithParametersFactory`.
 *
 * === Repeating a test
 *
 * When a test fails randomly or not often, for instance a race condition, it is convenient to run the same
 * test multiple times to increase the likelihood failure of the test.
 *
 * With JUnit a test has to be annotated with {@link io.vertx.ext.unit.junit.Repeat} to be repeated. The test must
 * also define the {@link io.vertx.ext.unit.junit.RepeatRule} among its rules.
 *
 * .Repeating a test with JUnit
 * [source,$lang]
 * ----
 * {@link examples.junit.RepeatingTest}
 * ----
 *
 * When declared, _before_ and _after_ life cycle will be executed as many times as the test is executed.
 *
 * NOTE: test repetition are executed sequentially
 *
 * === Using with other assertion libraries
 *
 * Vert.x Unit usability has been greatly improved in Vert.x 3.3. You can now write tests using
 * http://hamcrest.org/[Hamcrest], http://joel-costigliola.github.io/assertj/[AssertJ],
 * https://github.com/rest-assured/rest-assured/[Rest Assured], or any assertion library you want. This is made
 * possible by the global exception handler described in <<vertx_integration>>.
 *
 * You can find Java examples of using Vert.x Unit with Hamcrest and AssertJ in the
 * https://github.com/vert-x3/vertx-examples/tree/master/unit-examples[vertx-examples] project.
 *
 * == Java language integration
 *
 * === Test suite integration
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
 * {@link examples.junit.MyTestSuite}
 * ----
 *
 * This class can be turned into a Vertx test suite easily:
 *
 * .Create a test suite from a Java object
 * [source,java]
 * ----
 * {@link examples.junit.Snippets#testSuite()}
 * ----
 */
@ModuleGen(name = "vertx-unit", groupPackage = "io.vertx")
@Document(fileName = "index.adoc")
package io.vertx.ext.unit;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;
