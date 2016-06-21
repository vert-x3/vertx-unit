package io.vertx.ext.unit.junit;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class RepeatRule implements TestRule {

  private static class RepeatStatement extends Statement {

    private final int times;
    private final Statement statement;
    private final Description description;
    private final boolean silent;

    private RepeatStatement(int times, Statement statement, Description description, boolean silent) {
      this.times = times;
      this.statement = statement;
      this.description = description;
      this.silent = silent;
    }

    @Override
    public void evaluate() throws Throwable {
      for( int i = 0; i < times; i++ ) {
          if(!silent) {
            System.out.println("*** Iteration " + (i + 1) + "/" + times + " of test " + description.getDisplayName());
          }
        statement.evaluate();
      }
    }
  }

  @Override
  public Statement apply( Statement statement, Description description ) {
    Statement result = statement;
    Repeat repeat = description.getAnnotation(Repeat.class);
    if( repeat != null ) {
      final int times = repeat.value();
      final boolean silent = repeat.silent();
      result = new RepeatStatement(times, statement, description, silent);
    }
    return result;
  }
}
