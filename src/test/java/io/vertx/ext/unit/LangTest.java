package io.vertx.ext.unit;

import groovy.lang.GroovyShell;
import io.vertx.test.core.AsyncTestBase;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LangTest extends AsyncTestBase {

  @org.junit.Test
  public void testGroovy() throws Exception {
    GroovyShell shell = new GroovyShell();
    shell.setVariable("done", (Runnable) () -> {
      System.out.println("DONE");
      testComplete();
    });
    shell.evaluate(LangTest.class.getResource("/plain/timer.groovy").toURI());
    await();


  }

}
