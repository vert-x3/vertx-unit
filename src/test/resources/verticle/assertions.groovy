package verticle

import io.vertx.ext.unit.TestSuite;

def suite = TestSuite.create("the_suite");

suite.test("assert_null", { context ->
    context.assertNull(null);
}).test("fail_assert_null_1", { context ->
    context.assertNull(0);
}).test("fail_assert_null_2", { context ->
    context.assertNull("string");
}).test("fail_assert_null_3", { context ->
    context.assertNull(0.12);
}).test("fail_assert_null_4", { context ->
    context.assertNull(true);
}).test("fail_assert_null_5", { context ->
    context.assertNull(false);
}).test("fail_assert_null_6", { context ->
    context.assertNull([:]);
}).test("fail_assert_null_7", { context ->
    context.assertNull([]);
});

suite.test("assert_null_with_message", { context ->
    context.assertNull(null, "the_message");
}).test("fail_assert_null_with_message_1", { context ->
    context.assertNull(0, "the_message");
}).test("fail_assert_null_with_message_2", { context ->
    context.assertNull("string", "the_message");
}).test("fail_assert_null_with_message_3", { context ->
    context.assertNull(0.12, "the_message");
}).test("fail_assert_null_with_message_4", { context ->
    context.assertNull(true, "the_message");
}).test("fail_assert_null_with_message_5", { context ->
    context.assertNull(false, "the_message");
}).test("fail_assert_null_with_message_6", { context ->
    context.assertNull([:], "the_message");
}).test("fail_assert_null_with_message_7", { context ->
    context.assertNull([], "the_message");
});

suite.test("assert_not_null", { context ->
    context.assertNotNull(0);
    context.assertNotNull("string");
    context.assertNotNull(0.12);
    context.assertNotNull(true);
    context.assertNotNull(false);
    context.assertNotNull([:]);
    context.assertNotNull([]);
}).test("fail_assert_null", { context ->
    context.assertNotNull(null);
});

suite.test("assert_not_null_with_message", { context ->
    context.assertNotNull(0, "the_message");
    context.assertNotNull("string", "the_message");
    context.assertNotNull(0.12, "the_message");
    context.assertNotNull(true, "the_message");
    context.assertNotNull(false, "the_message");
    context.assertNotNull([:], "the_message");
    context.assertNotNull([], "the_message");
}).test("fail_assert_null_with_message", { context ->
    context.assertNotNull(null, "the_message");
});

suite.test("assert_equals", { context ->
    context.assertEquals(0, 0);
    context.assertEquals(10, 10);
    context.assertEquals(0.12, 0.12);
    context.assertEquals(true, true);
    context.assertEquals(false, false);
    context.assertEquals("the_string", "the_" + "string");
    context.assertEquals([foo:1,bar:"juu"], [foo:1,bar:"juu"]);
    context.assertEquals([], []);
    context.assertEquals([123], [123]);
}).test("fail_assert_equals_1", { context ->
    context.assertEquals(0, 1);
}).test("fail_assert_equals_2", { context ->
    context.assertEquals(0.12, 0.13);
}).test("fail_assert_equals_3", { context ->
    context.assertEquals(true, false);
}).test("fail_assert_equals_4", { context ->
    context.assertEquals(false, true);
}).test("fail_assert_equals_5", { context ->
    context.assertEquals("foo", "bar");
}).test("fail_assert_equals_6", { context ->
    context.assertEquals([foo:1,bar:"juu"], [foo:1,bar:"daa"]);
}).test("fail_assert_equals_7", { context ->
    context.assertEquals([123], [321]);
});

suite.test("assert_in_range", { context ->
    context.assertInRange(0.12, 0.13, 0.02);
    context.assertInRange(0.12, 0.11, 0.02);
}).test("fail_assert_in_range_1", { context ->
    context.assertInRange(0.12, 0.15, 0.02);
}).test("fail_assert_in_range_2", { context ->
    context.assertInRange(0.12, 0.09, 0.02);
});

suite.test("assert_equals_with_message", { context ->
    context.assertEquals(0, 0, "the_message");
    context.assertEquals(10, 10, "the_message");
    context.assertEquals(0.12, 0.12, "the_message");
    context.assertEquals(true, true, "the_message");
    context.assertEquals(false, false, "the_message");
    context.assertEquals("the_string", "the_" + "string", "the_message");
    context.assertEquals([foo:1,bar:"juu"], [foo:1,bar:"juu"], "the_message");
    context.assertEquals([], [], "the_message");
    context.assertEquals([123], [123], "the_message");
}).test("fail_assert_equals_with_message_1", { context ->
    context.assertEquals(0, 1, "the_message");
}).test("fail_assert_equals_with_message_2", { context ->
    context.assertEquals(0.12, 0.13, "the_message");
}).test("fail_assert_equals_with_message_3", { context ->
    context.assertEquals(true, false, "the_message");
}).test("fail_assert_equals_with_message_4", { context ->
    context.assertEquals(false, true, "the_message");
}).test("fail_assert_equals_with_message_5", { context ->
    context.assertEquals("foo", "bar", "the_message");
}).test("fail_assert_equals_with_message_6", { context ->
    context.assertEquals([foo:1,bar:"juu"], [foo:1,bar:"daa"], "the_message");
}).test("fail_assert_equals_with_message_7", { context ->
    context.assertEquals([123], [321], "the_message");
});

suite.test("assert_not_equals", { context ->
    context.assertNotEquals(0, 1);
    context.assertNotEquals(0.12, 0.13);
    context.assertNotEquals(true, false);
    context.assertNotEquals(false, true);
    context.assertNotEquals("foo", "bar");
    context.assertNotEquals([foo:1,bar:"juu"], [foo:1,bar:"daa"]);
    context.assertNotEquals([123], [321]);
}).test("fail_assert_equals_1", { context ->
    context.assertNotEquals(0, 0);
}).test("fail_assert_equals_2", { context ->
    context.assertNotEquals(0.12, 0.12);
}).test("fail_assert_equals_3", { context ->
    context.assertNotEquals(true, true);
}).test("fail_assert_equals_4", { context ->
    context.assertNotEquals(false, false);
}).test("fail_assert_equals_5", { context ->
    context.assertNotEquals("the_string", "the_" + "string");
}).test("fail_assert_equals_6", { context ->
    context.assertNotEquals([foo:1,bar:"juu"], [foo:1,bar:"juu"]);
}).test("fail_assert_equals_7", { context ->
    context.assertNotEquals([], []);
}).test("fail_assert_equals_8", { context ->
    context.assertNotEquals([123], [123]);
});

suite.test("assert_not_equals_with_message", { context ->
    context.assertNotEquals(0, 1, "the_message");
    context.assertNotEquals(0.12, 0.13, "the_message");
    context.assertNotEquals(true, false, "the_message");
    context.assertNotEquals(false, true, "the_message");
    context.assertNotEquals("foo", "bar", "the_message");
    context.assertNotEquals([foo:1,bar:"juu"], [foo:1,bar:"daa"], "the_message");
    context.assertNotEquals([123], [321], "the_message");
}).test("fail_assert_equals_with_message_1", { context ->
    context.assertNotEquals(0, 0, "the_message");
}).test("fail_assert_equals_with_message_2", { context ->
    context.assertNotEquals(0.12, 0.12, "the_message");
}).test("fail_assert_equals_with_message_3", { context ->
    context.assertNotEquals(true, true, "the_message");
}).test("fail_assert_equals_with_message_4", { context ->
    context.assertNotEquals(false, false, "the_message");
}).test("fail_assert_equals_with_message_5", { context ->
    context.assertNotEquals("the_string", "the_" + "string", "the_message");
}).test("fail_assert_equals_with_message_6", { context ->
    context.assertNotEquals([foo:1,bar:"juu"], [foo:1,bar:"juu"], "the_message");
}).test("fail_assert_equals_with_message_7", { context ->
    context.assertNotEquals([], [], "the_message");
}).test("fail_assert_equals_with_message_8", { context ->
    context.assertNotEquals([123], [123], "the_message");
});

suite.test("assert_true", { context ->
    context.assertTrue(true);
}).test("fail_assert_true", { context ->
    context.assertTrue(false);
});

suite.test("assert_true_with_message", { context ->
    context.assertTrue(true, "the_message");
}).test("fail_assert_true_with_message", { context ->
    context.assertTrue(false, "the_message");
});

suite.test("assert_false", { context ->
    context.assertFalse(false);
}).test("fail_assert_false", { context ->
    context.assertFalse(true);
});

suite.test("assert_false_with_message", { context ->
    context.assertFalse(false, "the_message");
}).test("fail_assert_false_with_message", { context ->
    context.assertFalse(true, "the_message");
});

suite.test("fail_", { context ->
    context.fail();
});

suite.test("fail_with_message", { context ->
    context.fail("the_message");
});

suite.run(vertx, [reporters:[[to: "bus:assert_tests"]]]);
