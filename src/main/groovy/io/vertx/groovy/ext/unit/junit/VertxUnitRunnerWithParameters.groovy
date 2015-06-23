package io.vertx.groovy.ext.unit.junit

import groovy.transform.CompileStatic;
import io.vertx.ext.unit.TestContext;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.TestWithParameters;

import java.lang.reflect.InvocationTargetException;

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
    VertxUnitRunner.doValidateTestMethod(fMethod);
  }

  @Override
  protected void invokeTestMethod(FrameworkMethod fMethod, Object test, TestContext context) throws InvocationTargetException, IllegalAccessException {
    VertxUnitRunner.doInvokeTestMethod(fMethod, test, context)
  }
}
