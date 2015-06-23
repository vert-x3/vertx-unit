package io.vertx.groovy.ext.unit.junit

import groovy.transform.CompileStatic;
import io.vertx.ext.unit.TestContext;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.TestWithParameters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@CompileStatic
public class VertxUnitRunnerWithParameters extends io.vertx.ext.unit.junit.VertxUnitRunnerWithParameters {

  public VertxUnitRunnerWithParameters(TestWithParameters test) throws InitializationError {
    super(test);
  }

  @Override
  protected void validateTestMethod(FrameworkMethod fMethod) throws Exception {
    Class<?>[] paramTypes = fMethod.getMethod().getParameterTypes();
    if (!(paramTypes.length == 0 || (paramTypes.length == 1 && paramTypes[0].equals(io.vertx.groovy.ext.unit.TestContext.class)))) {
      throw new Exception("Method " + fMethod.getName() + " should have no parameters or " +
          "the " + io.vertx.groovy.ext.unit.TestContext.class.getName() + " parameter");
    }
  }

  @Override
  protected void invokeTestMethod(FrameworkMethod fMethod, Object test, TestContext context) throws InvocationTargetException, IllegalAccessException {
    Method method = fMethod.getMethod();
    Class<?>[] paramTypes = method.getParameterTypes();
    if (paramTypes.length == 0) {
      method.invoke(test);
    } else {
      method.invoke(test, new io.vertx.groovy.ext.unit.TestContext(context));
    }
  }
}
