require 'vertx-unit/test_suite'
suite = VertxUnit::TestSuite.create 'the_suite'


suite.test 'assert_null' do |context| context.assert_null(nil) end
suite.test 'fail_assert_null_1' do |context| context.assert_null(0) end
suite.test 'fail_assert_null_2' do |context| context.assert_null('string') end
suite.test 'fail_assert_null_3' do |context| context.assert_null(0.12) end
suite.test 'fail_assert_null_4' do |context| context.assert_null(true) end
suite.test 'fail_assert_null_5' do |context| context.assert_null(false) end
suite.test 'fail_assert_null_6' do |context| context.assert_null({}) end
suite.test 'fail_assert_null_7' do |context| context.assert_null([]) end

suite.test 'assert_null_with_message' do |context| context.assert_null(nil, 'the_message') end
suite.test 'fail_assert_null_with_message_1' do |context| context.assert_null(0, 'the_message') end
suite.test 'fail_assert_null_with_message_2' do |context| context.assert_null('string', 'the_message') end
suite.test 'fail_assert_null_with_message_3' do |context| context.assert_null(0.12, 'the_message') end
suite.test 'fail_assert_null_with_message_4' do |context| context.assert_null(true, 'the_message') end
suite.test 'fail_assert_null_with_message_5' do |context| context.assert_null(false, 'the_message') end
suite.test 'fail_assert_null_with_message_6' do |context| context.assert_null({}, 'the_message') end
suite.test 'fail_assert_null_with_message_7' do |context| context.assert_null([], 'the_message') end

suite.test 'assert_not_null' do |context|
  context.assert_not_null(0)
  context.assert_not_null('string')
  context.assert_not_null(0.12)
  context.assert_not_null(true)
  context.assert_not_null(false)
  context.assert_not_null({})
  context.assert_not_null([])
end
suite.test 'fail_assert_null' do |context|
  context.assert_not_null(nil)
end

suite.test 'assert_not_null_with_message' do |context|
  context.assert_not_null(0, 'the_message')
  context.assert_not_null('string', 'the_message')
  context.assert_not_null(0.12, 'the_message')
  context.assert_not_null(true, 'the_message')
  context.assert_not_null(false, 'the_message')
  context.assert_not_null({}, 'the_message')
  context.assert_not_null([], 'the_message')
end
suite.test 'fail_assert_null_with_message' do |context|
  context.assert_not_null(nil, 'the_message')
end

suite.test 'assert_equals' do |context|
  context.assert_equals(0, 0)
  context.assert_equals(10, 10)
  context.assert_equals(0.12, 0.12)
  context.assert_equals(true, true)
  context.assert_equals(false, false)
  context.assert_equals('the_string', 'the_' + 'string')
  context.assert_equals({:foo=>1,:bar=>'juu'}, {:foo=>1,:bar=>'juu'})
  context.assert_equals([], [])
  context.assert_equals([123], [123])
end
suite.test 'fail_assert_equals_1' do |context| context.assert_equals(0, 1) end
suite.test 'fail_assert_equals_2' do |context| context.assert_equals(0.12, 0.13) end
suite.test 'fail_assert_equals_3' do |context| context.assert_equals(true, false) end
suite.test 'fail_assert_equals_4' do |context| context.assert_equals(false, true) end
suite.test 'fail_assert_equals_5' do |context| context.assert_equals('foo', 'bar') end
suite.test 'fail_assert_equals_6' do |context| context.assert_equals({:foo=>1,:bar=>'juu'}, {:foo=>1,:bar=>'daa'}) end
suite.test 'fail_assert_equals_7' do |context| context.assert_equals([123], [321]) end

suite.test 'assert_in_range' do |context|
  context.assert_in_range(0.12, 0.13, 0.02)
  context.assert_in_range(0.12, 0.11, 0.02)
end
suite.test 'fail_assert_in_range_1' do |context|
  context.assert_in_range(0.12, 0.15, 0.02)
end
suite.test 'fail_assert_in_range_2' do |context|
  context.assert_in_range(0.12, 0.09, 0.02)
end

suite.test 'assert_equals_with_message' do |context|
  context.assert_equals(0, 0, 'the_message')
  context.assert_equals(10, 10, 'the_message')
  context.assert_equals(0.12, 0.12, 'the_message')
  context.assert_equals(true, true, 'the_message')
  context.assert_equals(false, false, 'the_message')
  context.assert_equals('the_string', 'the_' + 'string', 'the_message')
  context.assert_equals({:foo=>1,:bar=>'juu'}, {:foo=>1,:bar=>'juu'}, 'the_message')
  context.assert_equals([], [], 'the_message')
  context.assert_equals([123], [123], 'the_message')
end
suite.test 'fail_assert_equals_with_message_1' do |context| context.assert_equals(0, 1, 'the_message') end
suite.test 'fail_assert_equals_with_message_2' do |context| context.assert_equals(0.12, 0.13, 'the_message') end
suite.test 'fail_assert_equals_with_message_3' do |context| context.assert_equals(true, false, 'the_message') end
suite.test 'fail_assert_equals_with_message_4' do |context| context.assert_equals(false, true, 'the_message') end
suite.test 'fail_assert_equals_with_message_5' do |context| context.assert_equals('foo', 'bar', 'the_message') end
suite.test 'fail_assert_equals_with_message_6' do |context| context.assert_equals({:foo=>1,:bar=>'juu'}, {:foo=>1,:bar=>'daa'}, 'the_message') end
suite.test 'fail_assert_equals_with_message_7' do |context| context.assert_equals([123], [321], 'the_message') end

suite.test 'assert_not_equals' do |context|
  context.assert_not_equals(0, 1)
  context.assert_not_equals(0.12, 0.13)
  context.assert_not_equals(true, false)
  context.assert_not_equals(false, true)
  context.assert_not_equals('foo', 'bar')
  context.assert_not_equals({:foo=>1,:bar=>'juu'}, {:foo=>1,:bar=>'daa'})
  context.assert_not_equals([123], [321])
end
suite.test 'fail_assert_equals_1' do |context| context.assert_not_equals(0, 0)  end
suite.test 'fail_assert_equals_2' do |context| context.assert_not_equals(0.12, 0.12)  end
suite.test 'fail_assert_equals_3' do |context| context.assert_not_equals(true, true)  end
suite.test 'fail_assert_equals_4' do |context| context.assert_not_equals(false, false)  end
suite.test 'fail_assert_equals_5' do |context| context.assert_not_equals('the_string', 'the_' + 'string')  end
suite.test 'fail_assert_equals_6' do |context| context.assert_not_equals({:foo=>1,:bar=>'juu'}, {:foo=>1,:bar=>'juu'})  end
suite.test 'fail_assert_equals_7' do |context| context.assert_not_equals([], [])  end
suite.test 'fail_assert_equals_8' do |context| context.assert_not_equals([123], [123])  end

suite.test 'assert_not_equals_with_message' do |context|
  context.assert_not_equals(0, 1, 'the_message')
  context.assert_not_equals(0.12, 0.13, 'the_message')
  context.assert_not_equals(true, false, 'the_message')
  context.assert_not_equals(false, true, 'the_message')
  context.assert_not_equals('foo', 'bar', 'the_message')
  context.assert_not_equals({:foo=>1,:bar=>'juu'}, {:foo=>1,:bar=>'daa'}, 'the_message')
  context.assert_not_equals([123], [321], 'the_message')
end
suite.test 'fail_assert_equals_with_message_1' do |context| context.assert_not_equals(0, 0, 'the_message')  end
suite.test 'fail_assert_equals_with_message_2' do |context| context.assert_not_equals(0.12, 0.12, 'the_message')  end
suite.test 'fail_assert_equals_with_message_3' do |context| context.assert_not_equals(true, true, 'the_message')  end
suite.test 'fail_assert_equals_with_message_4' do |context| context.assert_not_equals(false, false, 'the_message')  end
suite.test 'fail_assert_equals_with_message_5' do |context| context.assert_not_equals('the_string', 'the_' + 'string', 'the_message')  end
suite.test 'fail_assert_equals_with_message_6' do |context| context.assert_not_equals({:foo=>1,:bar=>'juu'}, {:foo=>1,:bar=>'juu'}, 'the_message')  end
suite.test 'fail_assert_equals_with_message_7' do |context| context.assert_not_equals([], [], 'the_message')  end
suite.test 'fail_assert_equals_with_message_8' do |context| context.assert_not_equals([123], [123], 'the_message')  end

suite.test 'assert_true' do |context| context.assert_true(true)  end
suite.test 'fail_assert_true' do |context| context.assert_true(false)  end

suite.test 'assert_true_with_message' do |context| context.assert_true(true, 'the_message')  end
suite.test 'fail_true_with_message' do |context| context.assert_true(false, 'the_message')  end

suite.test 'assert_false' do |context| context.assert_false(false)  end
suite.test 'fail_assert_false' do |context| context.assert_false(true)  end

suite.test 'assert_false_with_message' do |context| context.assert_false(false, 'the_message')  end
suite.test 'fail_assert_false_with_message' do |context| context.assert_false(true, 'the_message')  end

suite.test 'fail_' do |context|
  context.fail
end

suite.test 'fail_with_message' do |context|
  context.fail 'the_message'
end

suite.run $vertx, { :reporters => [ { :to => 'bus:assert_tests' }] }
