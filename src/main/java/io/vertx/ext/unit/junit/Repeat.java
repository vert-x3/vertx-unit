package io.vertx.ext.unit.junit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a test method to repeat this test several times. This can be useful when a test fails randomly and
 * not often.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface Repeat {

  int value();
  
  /**
   * @return true if Vertx Unit repeat test in silent mode
   */
  boolean silent() default false;

}

