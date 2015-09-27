package io.vertx.ext.unit.impl;

import io.vertx.core.cli.CLI;
import io.vertx.core.spi.launcher.DefaultCommandFactory;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestCommandFactory extends DefaultCommandFactory<TestCommand> {

  @Override
  public CLI define() {
    CLI cli = super.define();
    cli.getArgument(0).setArgName("test-verticle");
    return cli;
  }

  public TestCommandFactory() {
    super(TestCommand.class);
  }
}
