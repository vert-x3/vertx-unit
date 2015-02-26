var TestSuite = require('vertx-unit-js/test_suite');
var suite = TestSuite.create("the_suite");

suite.test("assert_equals", function(context) {
    context.assertEquals(0, 0);
    context.assertEquals(10, 10);
    context.assertEquals(0.12, 0.12);
    context.assertEquals(true, true);
    context.assertEquals(false, false);
    context.assertEquals("the_string", "the_" + "string");
    context.assertEquals({foo:1,bar:"juu"}, {foo:1,bar:"juu"});
    context.assertEquals([], []);
    context.assertEquals([123], [123]);
}).test("fail_assert_equals_1", function(context) {
    context.assertEquals(0, 1);
}).test("fail_assert_equals_2", function(context) {
    context.assertEquals(0.12, 0.13);
}).test("fail_assert_equals_3", function(context) {
    context.assertEquals(true, false);
}).test("fail_assert_equals_4", function(context) {
    context.assertEquals(false, true);
}).test("fail_assert_equals_5", function(context) {
    context.assertEquals("foo", "bar");
}).test("fail_assert_equals_6", function(context) {
    context.assertEquals({foo:1,bar:"juu"}, {foo:1,bar:"daa"});
}).test("fail_assert_equals_7", function(context) {
    context.assertEquals([123], [321]);
});

suite.test("assert_in_range", function(context) {
    context.assertInRange(0.12, 0.13, 0.02);
    context.assertInRange(0.12, 0.11, 0.02);
}).test("fail_assert_in_range_1", function(context) {
    context.assertInRange(0.12, 0.15, 0.02);
}).test("fail_assert_in_range_2", function(context) {
    context.assertInRange(0.12, 0.09, 0.02);
});

suite.test("assert_equals_with_message", function(context) {
    context.assertEquals(0, 0, "the_message");
    context.assertEquals(10, 10, "the_message");
    context.assertEquals(0.12, 0.12, "the_message");
    context.assertEquals(true, true, "the_message");
    context.assertEquals(false, false, "the_message");
    context.assertEquals("the_string", "the_" + "string", "the_message");
    context.assertEquals({foo:1,bar:"juu"}, {foo:1,bar:"juu"}, "the_message");
    context.assertEquals([], [], "the_message");
    context.assertEquals([123], [123], "the_message");
}).test("fail_assert_equals_with_message_1", function(context) {
    context.assertEquals(0, 1, "the_message");
}).test("fail_assert_equals_with_message_2", function(context) {
    context.assertEquals(0.12, 0.13, "the_message");
}).test("fail_assert_equals_with_message_3", function(context) {
    context.assertEquals(true, false, "the_message");
}).test("fail_assert_equals_with_message_4", function(context) {
    context.assertEquals(false, true, "the_message");
}).test("fail_assert_equals_with_message_5", function(context) {
    context.assertEquals("foo", "bar", "the_message");
}).test("fail_assert_equals_with_message_6", function(context) {
    context.assertEquals({foo:1,bar:"juu"}, {foo:1,bar:"daa"}, "the_message");
}).test("fail_assert_equals_with_message_7", function(context) {
    context.assertEquals([123], [321], "the_message");
});

suite.test("assert_not_equals", function(context) {
    context.assertNotEquals(0, 1);
    context.assertNotEquals(0.12, 0.13);
    context.assertNotEquals(true, false);
    context.assertNotEquals(false, true);
    context.assertNotEquals("foo", "bar");
    context.assertNotEquals({foo:1,bar:"juu"}, {foo:1,bar:"daa"});
    context.assertNotEquals([123], [321]);
}).test("fail_assert_equals_1", function(context) {
    context.assertNotEquals(0, 0);
}).test("fail_assert_equals_2", function(context) {
    context.assertNotEquals(0.12, 0.12);
}).test("fail_assert_equals_3", function(context) {
    context.assertNotEquals(true, true);
}).test("fail_assert_equals_4", function(context) {
    context.assertNotEquals(false, false);
}).test("fail_assert_equals_5", function(context) {
    context.assertNotEquals("the_string", "the_" + "string");
}).test("fail_assert_equals_6", function(context) {
    context.assertNotEquals({foo:1,bar:"juu"}, {foo:1,bar:"juu"});
}).test("fail_assert_equals_7", function(context) {
    context.assertNotEquals([], []);
}).test("fail_assert_equals_8", function(context) {
    context.assertNotEquals([123], [123]);
});

suite.test("assert_not_equals_with_message", function(context) {
    context.assertNotEquals(0, 1, "the_message");
    context.assertNotEquals(0.12, 0.13, "the_message");
    context.assertNotEquals(true, false, "the_message");
    context.assertNotEquals(false, true, "the_message");
    context.assertNotEquals("foo", "bar", "the_message");
    context.assertNotEquals({foo:1,bar:"juu"}, {foo:1,bar:"daa"}, "the_message");
    context.assertNotEquals([123], [321], "the_message");
}).test("fail_assert_equals_with_message_1", function(context) {
    context.assertNotEquals(0, 0, "the_message");
}).test("fail_assert_equals_with_message_2", function(context) {
    context.assertNotEquals(0.12, 0.12, "the_message");
}).test("fail_assert_equals_with_message_3", function(context) {
    context.assertNotEquals(true, true, "the_message");
}).test("fail_assert_equals_with_message_4", function(context) {
    context.assertNotEquals(false, false, "the_message");
}).test("fail_assert_equals_with_message_5", function(context) {
    context.assertNotEquals("the_string", "the_" + "string", "the_message");
}).test("fail_assert_equals_with_message_6", function(context) {
    context.assertNotEquals({foo:1,bar:"juu"}, {foo:1,bar:"juu"}, "the_message");
}).test("fail_assert_equals_with_message_7", function(context) {
    context.assertNotEquals([], [], "the_message");
}).test("fail_assert_equals_with_message_8", function(context) {
    context.assertNotEquals([123], [123], "the_message");
});

suite.test("assert_true", function(context) {
    context.assertTrue(true);
}).test("fail_assert_true", function(context) {
    context.assertTrue(false);
});

suite.test("assert_true_with_message", function(context) {
    context.assertTrue(true, "the_message");
}).test("fail_assert_true_with_message", function(context) {
    context.assertTrue(false, "the_message");
});

suite.test("assert_false", function(context) {
    context.assertFalse(false);
}).test("fail_assert_false", function(context) {
    context.assertFalse(true);
});

suite.test("assert_false_with_message", function(context) {
    context.assertFalse(false, "the_message");
}).test("fail_assert_false_with_message", function(context) {
    context.assertFalse(true, "the_message");
});

suite.test("fail_", function(context) {
    context.fail();
});

suite.test("fail_with_message", function(context) {
    context.fail("the_message");
});

suite.run(vertx, {reporters:[{to: "bus", at: "assert_tests"}]});
