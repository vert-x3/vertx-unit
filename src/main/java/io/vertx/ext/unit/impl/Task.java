package io.vertx.ext.unit.impl;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
abstract class Task {

  abstract void run(Runnable next);

}
