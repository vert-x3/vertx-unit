package io.vertx.ext.unit.tests;

import examples.Examples;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ExamplesTest {

  @org.junit.Test
  public void testExample2() {
    // Run it to get the output and copy in the docs
    System.out.println("------ begin example 2");
    Examples.test_02();
    System.out.println("------ end example 2");
  }

}
