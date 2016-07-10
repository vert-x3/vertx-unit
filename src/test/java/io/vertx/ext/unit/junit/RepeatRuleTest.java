package io.vertx.ext.unit.junit;

import io.vertx.ext.unit.junit.RepeatRule.RepeatStatement;
import java.io.PrintStream;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.Matchers;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public final class RepeatRuleTest {

  private RepeatRule rule;
  @Mock
  private PrintStream output;
  @Mock
  private Statement statement;
  @Mock
  private Description description;

  @Before
  public void setUp() {
    rule = new RepeatRule(output);
  }

  @Test
  @SuppressWarnings("empty-statement")
  public void executeWithoutRepeatAnnotations() throws Throwable {
    final class ClassToMock {
    };

    when(description.getTestClass()).thenAnswer(invocationOnMock -> ClassToMock.class);
    final Statement result = rule.apply(statement, description);
    assertSame(statement, result);
    result.evaluate();
    verify(output, never()).println(Matchers.anyString());
  }

  @Test
  @SuppressWarnings("empty-statement")
  public void executeWithGlobalRepeatAnnotation() throws Throwable {
    @Repeat(value = 10, silent = true)
    final class ClassToMock {
    };

    when(description.getTestClass()).thenAnswer(invocationOnMock -> ClassToMock.class);
    final Statement result = rule.apply(statement, description);
    assertNotSame(statement, result);
    assertEquals(10, ((RepeatStatement) result).getTimes());
    assertTrue(((RepeatStatement) result).isSilent());
    result.evaluate();
    verify(output, never()).println(Matchers.anyString());
  }

  @Test
  @SuppressWarnings("empty-statement")
  public void executeWithLocalRepeatAnnotation() throws Throwable {
    final class ClassToMock {
    };
    final Repeat local = mock(Repeat.class);
    when(local.silent()).thenReturn(false);
    when(local.value()).thenReturn(5);
    when(description.getTestClass()).thenAnswer(invocationOnMock -> ClassToMock.class);
    when(description.getAnnotation(Repeat.class)).thenReturn(local);
    final Statement result = rule.apply(statement, description);
    assertNotSame(statement, result);
    assertEquals(5, ((RepeatStatement) result).getTimes());
    assertFalse(((RepeatStatement) result).isSilent());
    result.evaluate();
    verify(output, times(5)).println(Matchers.anyString());
  }
  
  @Test
  @SuppressWarnings("empty-statement")
  public void executeWithLocalOverrideRepeatAnnotation() throws Throwable {
    @Repeat(value = 10, silent = true)
    final class ClassToMock {
    };
    final Repeat local = mock(Repeat.class);
    when(local.silent()).thenReturn(false);
    when(local.value()).thenReturn(5);
    when(description.getTestClass()).thenAnswer(invocationOnMock -> ClassToMock.class);
    when(description.getAnnotation(Repeat.class)).thenReturn(local);
    final Statement result = rule.apply(statement, description);
    assertNotSame(statement, result);
    assertEquals(5, ((RepeatStatement) result).getTimes());
    assertFalse(((RepeatStatement) result).isSilent());
    result.evaluate();
    verify(output, times(5)).println(Matchers.anyString());
  }
  
  @Test
  @SuppressWarnings("empty-statement")
  public void executeWithLocalRepeatAnnotationSetOne() throws Throwable {
    @Repeat(value = 10, silent = true)
    final class ClassToMock {
    };
    final Repeat local = mock(Repeat.class);
    when(local.silent()).thenReturn(false);
    when(local.value()).thenReturn(1);
    when(description.getTestClass()).thenAnswer(invocationOnMock -> ClassToMock.class);
    when(description.getAnnotation(Repeat.class)).thenReturn(local);
    final Statement result = rule.apply(statement, description);
    assertSame(statement, result);
    result.evaluate();
    verify(output, never()).println(Matchers.anyString());
  }

}
