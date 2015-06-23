package io.vertx.groovy.ext.unit.junit

import groovy.transform.CompileStatic;
import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.parameterized.ParametersRunnerFactory;
import org.junit.runners.parameterized.TestWithParameters;

/**
 * A {@link org.junit.runners.parameterized.ParametersRunnerFactory} for a {@link io.vertx.groovy.ext.unit.junit.VertxUnitRunner}
 * for enabling Vert.x Unit parameterized tests in Groovy.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@CompileStatic
public class VertxUnitRunnerWithParametersFactory implements ParametersRunnerFactory {

  @Override
  public Runner createRunnerForTestWithParameters(TestWithParameters test) throws InitializationError {
    return new VertxUnitRunnerWithParameters(test);
  }
}
