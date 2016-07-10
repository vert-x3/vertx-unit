package io.vertx.ext.unit.junit;

import java.io.PrintStream;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class RepeatRule implements TestRule {
    
  private final PrintStream output;

  public RepeatRule(PrintStream output) {
    this.output = output;
  }

   public RepeatRule() {
    this(System.out);
  }
  
  static class RepeatStatement extends Statement {

    private final PrintStream output;
    private final int times;
    private final Statement statement;
    private final Description description;
    private final boolean silent;
    
    private RepeatStatement(final PrintStream output, int times, Statement statement, Description description, boolean silent) {
      this.output = output;
      this.times = times;
      this.statement = statement;
      this.description = description;
      this.silent = silent;
    }

    int getTimes() {
      return times;
    }

    public boolean isSilent() {
      return silent;
    }

    @Override
    public void evaluate() throws Throwable {
      for( int i = 0; i < times; i++ ) {
          if(!silent) {
            output.println("*** Iteration " + (i + 1) + "/" + times + " of test " + description.getDisplayName());
          }
        statement.evaluate();
      }
    }
  }

  @Override
  public Statement apply( Statement statement, Description description ) {
    Statement result = statement;
    final Repeat global = description.getTestClass().getAnnotation(Repeat.class);
    final Repeat local = description.getAnnotation(Repeat.class);
    if( local != null || global != null ) {
      final int times = local != null ? local.value() : global.value();
      final boolean silent = local != null ? local.silent() : global.silent();
      if(times > 1) {
        result = new RepeatStatement(output, times, statement, description, silent);
      }
    }
    return result;
  }
}
