package io.vertx.ext.unit.junit;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.impl.Helper;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VertxUnitRule implements MethodRule {

  @Override
  public Statement apply(Statement base, FrameworkMethod method, Object target) {
    return new AsyncStatement(context -> {

      for (Field f : target.getClass().getDeclaredFields()) {
        if (f.getType() == TestContext.class && Modifier.isPublic(f.getModifiers()) && !Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers())) {
          try {
            f.set(target, context);
          } catch (IllegalAccessException ignore) {
          }
        }
      }

      try {
        base.evaluate();
      } catch (Throwable throwable) {
        Helper.uncheckedThrow(throwable);
      }
    }, method);
  }
}
