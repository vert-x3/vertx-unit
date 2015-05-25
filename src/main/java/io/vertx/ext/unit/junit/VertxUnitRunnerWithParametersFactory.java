package io.vertx.ext.unit.junit;

import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

/**
 * A {@link org.junit.runners.parameterized.ParametersRunnerFactory} for a {@link io.vertx.ext.unit.junit.VertxUnitRunner}
 * for enabling Vert.x Unit parameterized tests.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VertxUnitRunnerWithParametersFactory implements ParametersRunnerFactory {

  @Override
  public Runner createRunnerForTestWithParameters(TestWithParameters test) throws InitializationError {
    return new VertxUnitRunnerWithParameters(test);
  }
}
