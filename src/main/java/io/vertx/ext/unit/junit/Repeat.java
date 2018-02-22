package io.vertx.ext.unit.junit;

import java.lang.annotation.*;

/**
 * Annotates a test method or a class test to repeat this test or these tests
 * several times. This can be useful when a test fails randomly and not often.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Repeat {

  /**
   * @return iteration counter
   */
  int value();
  
  /**
   * @return true if Vertx Unit repeat test in silent mode
   */
  boolean silent() default false;

}

