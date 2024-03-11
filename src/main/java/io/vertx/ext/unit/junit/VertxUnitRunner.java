package io.vertx.ext.unit.junit;

import io.vertx.core.*;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.impl.Helper;
import io.vertx.ext.unit.impl.TestContextImpl;
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
 * Note : a runner is needed because when a rule statement is evaluated, it will run the before/test/after
 *       method and then test method is executed even if there are pending Async objects in the before
 *       method. The runner gives this necessary fine grained control.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VertxUnitRunner extends BlockJUnit4ClassRunner {

  private static final ThreadLocal<VertxUnitRunner> currentRunner = new ThreadLocal<>();
  private static final LinkedList<Context> contextStack = new LinkedList<>();
  private static final LinkedList<Long> timeoutStack = new LinkedList<>();
  private final TestClass testClass;
  private Map<String, Object> classAttributes = new HashMap<>();

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
        validateAnnotatedMethod(fMethod, isStatic, errors);
      }
    } else {
      super.validatePublicVoidNoArgMethods(annotation, isStatic, errors);
    }
  }

  protected void validateAnnotatedMethod(FrameworkMethod fMethod, boolean isStatic, List<Throwable> errors) {
    if (fMethod.isStatic() != isStatic) {
      String state = isStatic ? "should" : "should not";
      errors.add(new Exception("Method " + fMethod.getMethod().getName() + "() " + state + " be static"));
    }
    if (!fMethod.isPublic()) {
      errors.add(new Exception("Method " + fMethod.getMethod().getName() + "() should be public"));
    }

    Class<?> returnType = fMethod.getMethod().getReturnType();
    if ((returnType != Void.TYPE) && (returnType != Future.class)) {
      errors.add(new Exception("Method " + fMethod.getName() + "() should have return type void or " +
        Future.class.getName()));
    }

    try {
      validateTestMethod(fMethod);
    } catch (Exception e) {
      errors.add(e);
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
    TestContextImpl ctx = testContext;
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        invokeExplosively(ctx, method, test);
      }
    };
  }

  protected void invokeTestMethod(FrameworkMethod fMethod, Object test, TestContext context) throws InvocationTargetException, IllegalAccessException {
    Method method = fMethod.getMethod();
    Class<?>[] paramTypes = method.getParameterTypes();

    Object result;
    if (paramTypes.length == 0) {
      result = method.invoke(test);
    } else {
      result = method.invoke(test, context);
    }

    if (result instanceof Future) {
      Future<?> future = (Future<?>) result;
      future.setHandler(context.asyncAssertSuccess());
    }
  }

  private long getTimeout(FrameworkMethod fMethod) {
    long timeout = 2 * 60 * 1000L;
    if (timeoutStack.size() > 0) {
      timeout = timeoutStack.peekLast();
    }
    Test annotation = fMethod.getAnnotation(Test.class);
    if (annotation != null && annotation.timeout() > 0) {
      timeout = annotation.timeout();
    }
    return timeout;
  }

  private void invokeExplosively(TestContextImpl testContext, FrameworkMethod fMethod, Object test) throws Throwable {
    Handler<TestContext> callback = context -> {
      try {
        invokeTestMethod(fMethod, test, context);
      } catch (InvocationTargetException e) {
        Helper.uncheckedThrow(e.getCause());
      } catch (IllegalAccessException e) {
        Helper.uncheckedThrow(e);
      }
    };
    long timeout = getTimeout(fMethod);
    currentRunner.set(this);
    Context ctx = contextStack.peekLast();
    CompletableFuture<Throwable> future = new CompletableFuture<>();
    if (ctx != null) {
      ctx.runOnContext(v -> {
        testContext.run(null, timeout, callback, future::complete);
      });
    } else {
      testContext.run(null, timeout, callback, future::complete);
    }
    Throwable failure;
    try {
      failure = future.get();
    } catch (InterruptedException e) {
      // Should we do something else ?
      Thread.currentThread().interrupt();
      throw e;
    } finally {
      currentRunner.set(null);
    }
    if (failure != null) {
      throw failure;
    }
  }

  private TestContextImpl testContext;

  @Override
  protected Statement methodBlock(FrameworkMethod method) {
    testContext = new TestContextImpl(new HashMap<>(classAttributes), null);
    Statement statement = super.methodBlock(method);
    testContext = null;
    return statement;
  }

  @Override
  protected Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
    return withBefores(testContext, getTestClass().getAnnotatedMethods(Before.class), target, statement);
  }

  @Override
  protected Statement withAfters(FrameworkMethod method, Object target, Statement statement) {
    List<FrameworkMethod> afters = getTestClass().getAnnotatedMethods(After.class);
    return withAfters(testContext, afters, target, statement);
  }

  @Override
  protected Statement withBeforeClasses(Statement statement) {
    List<FrameworkMethod> befores = testClass.getAnnotatedMethods(BeforeClass.class);
    return withBefores(new TestContextImpl(classAttributes, null), befores, null, statement);
  }

  @Override
  protected Statement withAfterClasses(Statement statement) {
    List<FrameworkMethod> afters = getTestClass().getAnnotatedMethods(AfterClass.class);
    return withAfters(new TestContextImpl(classAttributes, null), afters, null, statement);
  }

  @Override
  protected Statement withPotentialTimeout(FrameworkMethod method, Object test, Statement next) {
    // Need to be a noop since we handle that without a wrapping statement
    return next;
  }

  private Statement withBefores(TestContextImpl testContext, List<FrameworkMethod> befores, Object target, Statement statement) {
    if (befores.isEmpty()) {
      return statement;
    } else {
      return new Statement() {
        @Override
        public void evaluate() throws Throwable {
          for (FrameworkMethod before : befores) {
            invokeExplosively(testContext, before, target);
          }
          statement.evaluate();
        }
      };
    }
  }

  private Statement withAfters(TestContextImpl testContext, List<FrameworkMethod> afters, Object target, Statement statement) {
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
                invokeExplosively(testContext, after, target);
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
