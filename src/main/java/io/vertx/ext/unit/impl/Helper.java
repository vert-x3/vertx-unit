package io.vertx.ext.unit.impl;

import io.vertx.core.Handler;
import io.vertx.ext.unit.TestContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Helper {

  /**
   * JVM hack to throw a throwable as unchecked.
   *
   * @param throwable the throwable to throw
   */
  public static void uncheckedThrow(Throwable throwable) {
    Helper.<RuntimeException>throwIt(throwable);
  }

  private static <T extends Throwable> void throwIt(Throwable throwable) throws T {
    throw (T)throwable;
  }

  public static Handler<TestContext> invoker(Method method, Supplier<?> instance) {
    Class<?>[] paramTypes = method.getParameterTypes();
    if (!(paramTypes.length == 0 || (paramTypes.length == 1 && paramTypes[0].equals(TestContext.class)))) {
      throw new IllegalArgumentException("Incorrect method handler mapping " + method);
    }
    return context -> {
      try {
        Object o = instance.get();
        if (paramTypes.length == 0) {
          method.invoke(o);
        } else {
          method.invoke(o, context);
        }
      } catch (IllegalAccessException e) {
        Helper.uncheckedThrow(e);
      } catch (InvocationTargetException e) {
        Helper.uncheckedThrow(e.getCause());
      }
    };
  }
}
