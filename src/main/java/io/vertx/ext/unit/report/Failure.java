package io.vertx.ext.unit.report;

import io.vertx.codegen.annotations.CacheReturn;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;

/**
 * A failure provides the details of a failure that happened during the execution of a test case.<p/>
 *
 * The failure can be:
 * <ul>
 *   <li>an assertion failure: an assertion failed</li>
 *   <li>an error failure: an expected error occured</li>
 * </ul>
 *
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Failure {

  /**
   * @return true if the failure is an error failure otherwise it is an assertion failure
   */
  @CacheReturn
  boolean isError();

  /**
   * @return the error message
   */
  @CacheReturn
  String message();

  /**
   * @return the stack trace
   */
  @CacheReturn
  String stackTrace();

  /**
   * @return the underlying exception causing this failure, it may be null
   */
  @GenIgnore
  Throwable cause();

}
