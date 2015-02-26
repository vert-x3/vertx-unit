package io.vertx.ext.unit.impl;

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
}
