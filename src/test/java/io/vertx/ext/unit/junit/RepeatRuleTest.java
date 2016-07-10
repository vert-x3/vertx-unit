package io.vertx.ext.unit.junit;

import io.vertx.ext.unit.junit.RepeatRule.RepeatStatement;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public final class RepeatRuleTest {

    private RepeatRule rule;
    private OutputStream buffer;
    @Mock
    private Statement statement;
    @Mock
    private Description description;

    @Before
    public void setUp() {
        buffer = new ByteArrayOutputStream();
        rule = new RepeatRule(new PrintStream(buffer));
    }

    @Test
    @SuppressWarnings("empty-statement")
    public void executeWithoutRepeatAnnotations() {
        final class ClassToMock {
        };

        when(description.getTestClass()).thenAnswer(invocationOnMock -> ClassToMock.class);
        final Statement result = rule.apply(statement, description);
        assertSame(statement, result);
    }

    @Test
    @SuppressWarnings("empty-statement")
    public void executeWithGlobalRepeatAnnotation() {
        @Repeat(value = 10, silent = true)
        final class ClassToMock {
        };

        when(description.getTestClass()).thenAnswer(invocationOnMock -> ClassToMock.class);
        final Statement result = rule.apply(statement, description);
        assertNotSame(statement, result);
        assertEquals(10, ((RepeatStatement) result).getTimes());
        assertTrue(((RepeatStatement) result).isSilent());
    }

    @Test
    @SuppressWarnings("empty-statement")
    public void executeWithLocalRepeatAnnotation() {
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
    }
    
    @Test
    @SuppressWarnings("empty-statement")
    public void executeWithLocalOverrideRepeatAnnotation() {
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
    }
    
    @Test
    @SuppressWarnings("empty-statement")
    public void executeWithLocalRepeatAnnotationSetOne() {
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
    }

}
