package io.vertx.ext.unit.junit;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.impl.Helper;
import io.vertx.ext.unit.impl.Result;
import io.vertx.ext.unit.impl.Task;
import io.vertx.ext.unit.impl.TestContextImpl;
import io.vertx.ext.unit.impl.ExecutionContext;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * A JUnit runner for writing asynchronous tests.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VertxUnitRunner extends BlockJUnit4ClassRunner {

  private static final LinkedList<Context> contextStack = new LinkedList<>();
  private static final LinkedList<Long> timeoutStack = new LinkedList<>();
  private final TestClass testClass;
  private Map<String, Object> classAttributes = new HashMap<>();
  private Map<String, Object> testAttributes;

  public VertxUnitRunner(Class<?> klass) throws InitializationError {
    super(klass);
    this.testClass = new TestClass(klass);
  }

  @Override
  protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors) {
    if (annotation == Test.class || annotation == Before.class || annotation == After.class ||
        annotation == BeforeClass.class || annotation == AfterClass.class) {
      List<FrameworkMethod> fMethods = getTestClass().getAnnotatedMethods(annotation);
      for (FrameworkMethod fMethod : fMethods) {
        fMethod.validatePublicVoid(isStatic, errors);
        try {
          validateTestMethod(fMethod);
        } catch (Exception e) {
          errors.add(e);
        }
      }
    } else {
      super.validatePublicVoidNoArgMethods(annotation, isStatic, errors);
    }
  }

  protected void validateTestMethod(FrameworkMethod fMethod) throws Exception {
    Class<?>[] paramTypes = fMethod.getMethod().getParameterTypes();
    if (!(paramTypes.length == 0 || (paramTypes.length == 1 && paramTypes[0].equals(TestContext.class)))) {
      throw new Exception("Method " + fMethod.getName() + " should have no parameters or " +
          "the " + TestContext.class.getName() + " parameter");
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

  protected void invokeTestMethod(FrameworkMethod fMethod, Object test, TestContext context) throws InvocationTargetException, IllegalAccessException {
    Method method = fMethod.getMethod();
    Class<?>[] paramTypes = method.getParameterTypes();
    if (paramTypes.length == 0) {
      method.invoke(test);
    } else {
      method.invoke(test, context);
    }
  }

  private void invokeExplosively(Map<String, Object> attributes, FrameworkMethod fMethod, Object test) throws Throwable {
    CompletableFuture<Result> future = new CompletableFuture<>();
    Task<Result> task = (result, context) -> future.complete(result);
    Handler<TestContext> callback = context -> {
      try {
        invokeTestMethod(fMethod, test, context);
      } catch (InvocationTargetException e) {
        Helper.uncheckedThrow(e.getCause());
      } catch (IllegalAccessException e) {
        Helper.uncheckedThrow(e);
      }
    };
    long timeout = 2 * 60 * 1000L;
    if (timeoutStack.size() > 0) {
      timeout = timeoutStack.peekLast();
    }
    Test annotation = fMethod.getAnnotation(Test.class);
    if (annotation != null && annotation.timeout() > 0) {
      timeout = annotation.timeout();
    }
    TestContextImpl context = new TestContextImpl(
        attributes,
        callback,
        err -> {},
        task,
        timeout);
    new ExecutionContext(contextStack.peekLast()).run(context);
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

  @Override
  protected Statement withPotentialTimeout(FrameworkMethod method, Object test, Statement next) {
    // Need to be a noop since we handle that without a wrapping statement
    return next;
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

  static void pushContext(Context context) {
    contextStack.push(context);
  }

  static void popContext() {
    contextStack.pop();
  }

  static void pushTimeout(long timeout) {
    timeoutStack.push(timeout);
  }

  static void popTimeout() {
    timeoutStack.pop();
  }
}
