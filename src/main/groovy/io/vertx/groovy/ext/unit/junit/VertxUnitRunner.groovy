package io.vertx.groovy.ext.unit.junit

import groovy.transform.CompileStatic
import io.vertx.core.Handler
import io.vertx.groovy.ext.unit.TestContext
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError

import java.lang.reflect.Method;

/**
 * A JUnit runner for Groovy JUnit tests.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@CompileStatic
public class VertxUnitRunner extends io.vertx.ext.unit.junit.VertxUnitRunner {

  public VertxUnitRunner(Class<?> klass) throws InitializationError {
    super(klass);
  }

  @Override
  protected void validateTestMethod(FrameworkMethod fMethod) throws Exception {
    doValidateTestMethod(fMethod);
  }

  @Override
  protected Handler<io.vertx.ext.unit.TestContext> invokingHandler(FrameworkMethod fMethod, Object test) {
    return groovyInvokingHandler(fMethod, test);
  }

  static void doValidateTestMethod(FrameworkMethod fMethod) throws Exception {
    Class<?>[] paramTypes = fMethod.getMethod().getParameterTypes();
    if (!(paramTypes.length == 0 || (paramTypes.length == 1 && paramTypes[0].equals(io.vertx.groovy.ext.unit.TestContext.class)))) {
      throw new Exception("Method " + fMethod.getName() + " should have no parameters or " +
          "the " + TestContext.class.getName() + " parameter");
    }
  }

  static Handler<io.vertx.ext.unit.TestContext> groovyInvokingHandler(FrameworkMethod fMethod, Object test) {
    return new Handler<io.vertx.ext.unit.TestContext>() {
      @Override
      void handle(io.vertx.ext.unit.TestContext testContext) {
        Method method = fMethod.getMethod();
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length == 0) {
          method.invoke(test);
        } else {
          method.invoke(test, new TestContext(testContext));
        }
      }
    }
  }
}
