package io.vertx.ext.unit.junit;

import io.vertx.core.Handler;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.impl.Helper;
import io.vertx.ext.unit.impl.Result;
import io.vertx.ext.unit.impl.Task;
import io.vertx.ext.unit.impl.TestContextImpl;
import io.vertx.ext.unit.impl.TestSuiteContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VertxUnitRunner extends BlockJUnit4ClassRunner {

  private final TestClass testClass;
  private final Long timeout;
  private Map<String, Object> classAttributes = new HashMap<>();
  private Map<String, Object> testAttributes;

  public VertxUnitRunner(Class<?> klass) throws InitializationError {
    this(klass, null);
  }

  public VertxUnitRunner(Class<?> klass, Long timeout) throws InitializationError {
    super(klass);
    this.timeout = timeout;
    this.testClass = new TestClass(klass);
  }

  @Override
  protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors) {
    if (annotation == Test.class || annotation == Before.class || annotation == After.class ||
        annotation == BeforeClass.class || annotation == AfterClass.class) {
      List<FrameworkMethod> fMethods = getTestClass().getAnnotatedMethods(annotation);
      for (FrameworkMethod fMethod : fMethods) {
        fMethod.validatePublicVoid(isStatic, errors);
        Class<?>[] paramTypes = fMethod.getMethod().getParameterTypes();
        if (!(paramTypes.length == 0 || (paramTypes.length == 1 && paramTypes[0].equals(TestContext.class)))) {
          errors.add(new Exception("Method " + fMethod.getName() + " should have no parameters or " +
              "the " + TestContext.class.getName() + " parameter"));
        }
      }
    } else {
      super.validatePublicVoidNoArgMethods(annotation, isStatic, errors);
    }
  }

  @Override
  protected Statement methodInvoker(FrameworkMethod method, Object test) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        invokeExplosively(testAttributes, method, test);
      }
    };
  }

  private void invokeExplosively(Map<String, Object> attributes, FrameworkMethod fMethod, Object test) throws Throwable {
    CompletableFuture<Result> future = new CompletableFuture<>();
    Task<Result> task = (result, context) -> future.complete(result);
    Handler<TestContext> callback = context -> {
      Method method = fMethod.getMethod();
      Class<?>[] paramTypes = method.getParameterTypes();
      try {
        if (paramTypes.length == 0) {
          method.invoke(test);
        } else {
          method.invoke(test, context);
        }
      } catch (InvocationTargetException e) {
        Helper.uncheckedThrow(e.getCause());
      } catch (IllegalAccessException e) {
        Helper.uncheckedThrow(e);
      }
    };
    TestContextImpl context = new TestContextImpl(
        attributes,
        callback,
        err -> {},
        task,
        timeout != null ? timeout : 2 * 60 * 1000);
    context.execute(null, new TestSuiteContext(null));
    Result result;
    try {
      result = future.get();
    } catch (InterruptedException e) {
      // Should we do something else ?
      Thread.currentThread().interrupt();
      throw e;
    }
    Throwable failure = result.getFailure();
    if (failure != null) {
      throw failure;
    }
  }

  @Override
  protected Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
    testAttributes = new HashMap<>(classAttributes);
    return withBefores(testAttributes, getTestClass().getAnnotatedMethods(Before.class), target, statement);
  }

  @Override
  protected Statement withAfters(FrameworkMethod method, Object target, Statement statement) {
    List<FrameworkMethod> afters = getTestClass().getAnnotatedMethods(After.class);
    return withAfters(testAttributes, afters, target, statement);
  }

  @Override
  protected Statement withBeforeClasses(Statement statement) {
    List<FrameworkMethod> befores = testClass.getAnnotatedMethods(BeforeClass.class);
    return withBefores(classAttributes, befores, null, statement);
  }

  @Override
  protected Statement withAfterClasses(Statement statement) {
    List<FrameworkMethod> afters = getTestClass().getAnnotatedMethods(AfterClass.class);
    return withAfters(classAttributes, afters, null, statement);
  }

  private Statement withBefores(Map<String, Object> attributes, List<FrameworkMethod> befores, Object target, Statement statement) {
    if (befores.isEmpty()) {
      return statement;
    } else {
      return new Statement() {
        @Override
        public void evaluate() throws Throwable {
          for (FrameworkMethod before : befores) {
            invokeExplosively(attributes, before, target);
          }
          statement.evaluate();
        }
      };
    }
  }

  private Statement withAfters(Map<String, Object> attributes, List<FrameworkMethod> afters, Object target, Statement statement) {
    if (afters.isEmpty()) {
      return statement;
    } else {
      return new Statement() {
        @Override
        public void evaluate() throws Throwable {
          List<Throwable> errors = new ArrayList<Throwable>();
          try {
            statement.evaluate();
          } catch (Throwable e) {
            errors.add(e);
          } finally {
            for (FrameworkMethod after : afters) {
              try {
                invokeExplosively(attributes, after, target);
              } catch (Throwable e) {
                errors.add(e);
              }
            }
          }
          MultipleFailureException.assertEmpty(errors);
        }
      };
    }
  }
}
