package io.vertx.ext.unit.impl;

import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.Result;
import org.junit.runner.JUnitCore;

public final class TestContextImplTest {

    public static class DoubleAsyncCompleteTest {

        @Test
        public void test(final TestContext context) {
            final Async async = context.async();
            async.complete();
            async.complete();
        }

    }

    @Test
    public void testDoubleAsyncComplete() throws Throwable {
        final Result result = new JUnitCore().run(new VertxUnitRunner(DoubleAsyncCompleteTest.class));
        assertFalse(result.wasSuccessful());
        assertEquals(1, result.getFailureCount());
        assertTrue(result.getFailures().get(0).getException() instanceof IllegalStateException);
    }

}
