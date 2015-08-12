package io.vertx.groovy.ext.unit.junit

import groovy.transform.CompileStatic
import io.vertx.core.Handler;
import io.vertx.ext.unit.TestContext;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.TestWithParameters;

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
  protected Handler<TestContext> invokingHandler(FrameworkMethod fMethod, Object test) {
    return VertxUnitRunner.groovyInvokingHandler(fMethod, test);
  }
}
