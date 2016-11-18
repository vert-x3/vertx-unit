require 'vertx-unit/async'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.unit.TestContext
module VertxUnit
  #  The test context is used for performing test assertions and manage the completion of the test. This context
  #  is provided by <i>vertx-unit</i> as argument of the test case.
  class TestContext
    # @private
    # @param j_del [::VertxUnit::TestContext] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxUnit::TestContext] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == TestContext
    end
    def @@j_api_type.wrap(obj)
      TestContext.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxExtUnit::TestContext.java_class
    end
    #  Get some data from the context.
    # @param [String] key the key of the data
    # @return [Object] the data
    def get(key=nil)
      if key.class == String && !block_given?
        return ::Vertx::Util::Utils.from_object(@j_del.java_method(:get, [Java::java.lang.String.java_class]).call(key))
      end
      raise ArgumentError, "Invalid arguments when calling get(#{key})"
    end
    #  Put some data in the context.
    #  <p>
    #  This can be used to share data between different tests and before/after phases.
    # @param [String] key the key of the data
    # @param [Object] value the data
    # @return [Object] the previous object when it exists
    def put(key=nil,value=nil)
      if key.class == String && ::Vertx::Util::unknown_type.accept?(value) && !block_given?
        return ::Vertx::Util::Utils.from_object(@j_del.java_method(:put, [Java::java.lang.String.java_class,Java::java.lang.Object.java_class]).call(key,::Vertx::Util::Utils.to_object(value)))
      end
      raise ArgumentError, "Invalid arguments when calling put(#{key},#{value})"
    end
    #  Remove some data from the context.
    # @param [String] key the key to remove
    # @return [Object] the removed object when it exists
    def remove(key=nil)
      if key.class == String && !block_given?
        return ::Vertx::Util::Utils.from_object(@j_del.java_method(:remove, [Java::java.lang.String.java_class]).call(key))
      end
      raise ArgumentError, "Invalid arguments when calling remove(#{key})"
    end
    #  Assert the <code>expected</code> argument is <code>null</code>. If the argument is not, an assertion error is thrown
    #  otherwise the execution continue.
    # @param [Object] expected the argument being asserted to be null
    # @param [String] message the failure message
    # @return [self]
    def assert_null(expected=nil,message=nil)
      if ::Vertx::Util::unknown_type.accept?(expected) && !block_given? && message == nil
        @j_del.java_method(:assertNull, [Java::java.lang.Object.java_class]).call(::Vertx::Util::Utils.to_object(expected))
        return self
      elsif ::Vertx::Util::unknown_type.accept?(expected) && message.class == String && !block_given?
        @j_del.java_method(:assertNull, [Java::java.lang.Object.java_class,Java::java.lang.String.java_class]).call(::Vertx::Util::Utils.to_object(expected),message)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling assert_null(#{expected},#{message})"
    end
    #  Assert the <code>expected</code> argument is not <code>null</code>. If the argument is <code>null</code>, an assertion error is thrown
    #  otherwise the execution continue.
    # @param [Object] expected the argument being asserted to be not null
    # @param [String] message the failure message
    # @return [self]
    def assert_not_null(expected=nil,message=nil)
      if ::Vertx::Util::unknown_type.accept?(expected) && !block_given? && message == nil
        @j_del.java_method(:assertNotNull, [Java::java.lang.Object.java_class]).call(::Vertx::Util::Utils.to_object(expected))
        return self
      elsif ::Vertx::Util::unknown_type.accept?(expected) && message.class == String && !block_given?
        @j_del.java_method(:assertNotNull, [Java::java.lang.Object.java_class,Java::java.lang.String.java_class]).call(::Vertx::Util::Utils.to_object(expected),message)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling assert_not_null(#{expected},#{message})"
    end
    #  Assert the specified <code>condition</code> is <code>true</code>. If the condition is <code>false</code>, an assertion error is thrown
    #  otherwise the execution continue.
    # @param [true,false] condition the condition to assert
    # @param [String] message the failure message
    # @return [self]
    def assert_true(condition=nil,message=nil)
      if (condition.class == TrueClass || condition.class == FalseClass) && !block_given? && message == nil
        @j_del.java_method(:assertTrue, [Java::boolean.java_class]).call(condition)
        return self
      elsif (condition.class == TrueClass || condition.class == FalseClass) && message.class == String && !block_given?
        @j_del.java_method(:assertTrue, [Java::boolean.java_class,Java::java.lang.String.java_class]).call(condition,message)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling assert_true(#{condition},#{message})"
    end
    #  Assert the specified <code>condition</code> is <code>false</code>. If the condition is <code>true</code>, an assertion error is thrown
    #  otherwise the execution continue.
    # @param [true,false] condition the condition to assert
    # @param [String] message the failure message
    # @return [self]
    def assert_false(condition=nil,message=nil)
      if (condition.class == TrueClass || condition.class == FalseClass) && !block_given? && message == nil
        @j_del.java_method(:assertFalse, [Java::boolean.java_class]).call(condition)
        return self
      elsif (condition.class == TrueClass || condition.class == FalseClass) && message.class == String && !block_given?
        @j_del.java_method(:assertFalse, [Java::boolean.java_class,Java::java.lang.String.java_class]).call(condition,message)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling assert_false(#{condition},#{message})"
    end
    #  Assert the <code>expected</code> argument is equals to the <code>actual</code> argument. If the arguments are not equals
    #  an assertion error is thrown otherwise the execution continue.
    # @param [Object] expected the object the actual object is supposedly equals to
    # @param [Object] actual the actual object to test
    # @param [String] message the failure message
    # @return [self]
    def assert_equals(expected=nil,actual=nil,message=nil)
      if ::Vertx::Util::unknown_type.accept?(expected) && ::Vertx::Util::unknown_type.accept?(actual) && !block_given? && message == nil
        @j_del.java_method(:assertEquals, [Java::java.lang.Object.java_class,Java::java.lang.Object.java_class]).call(::Vertx::Util::Utils.to_object(expected),::Vertx::Util::Utils.to_object(actual))
        return self
      elsif ::Vertx::Util::unknown_type.accept?(expected) && ::Vertx::Util::unknown_type.accept?(actual) && message.class == String && !block_given?
        @j_del.java_method(:assertEquals, [Java::java.lang.Object.java_class,Java::java.lang.Object.java_class,Java::java.lang.String.java_class]).call(::Vertx::Util::Utils.to_object(expected),::Vertx::Util::Utils.to_object(actual),message)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling assert_equals(#{expected},#{actual},#{message})"
    end
    #  Asserts that the <code>expected</code> double argument is equals to the <code>actual</code> double argument
    #  within a positive delta. If the arguments do not satisfy this, an assertion error is thrown otherwise
    #  the execution continue.
    # @param [Float] expected the object the actual object is supposedly equals to
    # @param [Float] actual the actual object to test
    # @param [Float] delta the maximum delta
    # @param [String] message the failure message
    # @return [self]
    def assert_in_range(expected=nil,actual=nil,delta=nil,message=nil)
      if expected.class == Float && actual.class == Float && delta.class == Float && !block_given? && message == nil
        @j_del.java_method(:assertInRange, [Java::double.java_class,Java::double.java_class,Java::double.java_class]).call(::Vertx::Util::Utils.to_double(expected),::Vertx::Util::Utils.to_double(actual),::Vertx::Util::Utils.to_double(delta))
        return self
      elsif expected.class == Float && actual.class == Float && delta.class == Float && message.class == String && !block_given?
        @j_del.java_method(:assertInRange, [Java::double.java_class,Java::double.java_class,Java::double.java_class,Java::java.lang.String.java_class]).call(::Vertx::Util::Utils.to_double(expected),::Vertx::Util::Utils.to_double(actual),::Vertx::Util::Utils.to_double(delta),message)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling assert_in_range(#{expected},#{actual},#{delta},#{message})"
    end
    #  Assert the <code>first</code> argument is not equals to the <code>second</code> argument. If the arguments are equals
    #  an assertion error is thrown otherwise the execution continue.
    # @param [Object] first the first object to test
    # @param [Object] second the second object to test
    # @param [String] message the failure message
    # @return [self]
    def assert_not_equals(first=nil,second=nil,message=nil)
      if ::Vertx::Util::unknown_type.accept?(first) && ::Vertx::Util::unknown_type.accept?(second) && !block_given? && message == nil
        @j_del.java_method(:assertNotEquals, [Java::java.lang.Object.java_class,Java::java.lang.Object.java_class]).call(::Vertx::Util::Utils.to_object(first),::Vertx::Util::Utils.to_object(second))
        return self
      elsif ::Vertx::Util::unknown_type.accept?(first) && ::Vertx::Util::unknown_type.accept?(second) && message.class == String && !block_given?
        @j_del.java_method(:assertNotEquals, [Java::java.lang.Object.java_class,Java::java.lang.Object.java_class,Java::java.lang.String.java_class]).call(::Vertx::Util::Utils.to_object(first),::Vertx::Util::Utils.to_object(second),message)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling assert_not_equals(#{first},#{second},#{message})"
    end
    #  Throw a failure with the specified failure <code>cause</code>.
    # @overload fail()
    # @overload fail(message)
    #   @param [String] message the failure message
    # @overload fail(cause)
    #   @param [Exception] cause the failure cause
    # @return [void]
    def fail(param_1=nil)
      if !block_given? && param_1 == nil
        return @j_del.java_method(:fail, []).call()
      elsif param_1.class == String && !block_given?
        return @j_del.java_method(:fail, [Java::java.lang.String.java_class]).call(param_1)
      elsif param_1.is_a?(Exception) && !block_given?
        return @j_del.java_method(:fail, [Java::JavaLang::Throwable.java_class]).call(::Vertx::Util::Utils.to_throwable(param_1))
      end
      raise ArgumentError, "Invalid arguments when calling fail(#{param_1})"
    end
    #  Create and returns a new async object, the returned async controls the completion of the test. This async operation
    #  completes when the {::VertxUnit::Async#complete} is called <code>count</code> times.<p/>
    # 
    #  The test case will complete when all the async objects have their {::VertxUnit::Async#complete}
    #  method called at least once.<p/>
    # 
    #  This method shall be used for creating asynchronous exit points for the executed test.<p/>
    # @param [Fixnum] count 
    # @return [::VertxUnit::Async] the async instance
    def async(count=nil)
      if !block_given? && count == nil
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:async, []).call(),::VertxUnit::Async)
      elsif count.class == Fixnum && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:async, [Java::int.java_class]).call(count),::VertxUnit::Async)
      end
      raise ArgumentError, "Invalid arguments when calling async(#{count})"
    end
    #  Creates and returns a new async handler, the returned handler controls the completion of the test.<p/>
    # 
    #  When the returned handler is called back with a succeeded result it invokes the <code>resultHandler</code> argument
    #  with the async result. The test completes after the result handler is invoked and does not fails.<p/>
    # 
    #  When the returned handler is called back with a failed result it fails the test with the cause of the failure.<p/>
    # 
    #  Note that the result handler can create other async objects during its invocation that would postpone
    #  the completion of the test case until those objects are resolved.
    # @yield the result handler
    # @return [Proc] the async result handler
    def async_assert_success
      if !block_given?
        return ::Vertx::Util::Utils.to_async_result_handler_proc(@j_del.java_method(:asyncAssertSuccess, []).call()) { |val| ::Vertx::Util::Utils.to_object(val) }
      elsif block_given?
        return ::Vertx::Util::Utils.to_async_result_handler_proc(@j_del.java_method(:asyncAssertSuccess, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.from_object(event)) }))) { |val| ::Vertx::Util::Utils.to_object(val) }
      end
      raise ArgumentError, "Invalid arguments when calling async_assert_success()"
    end
    #  Creates and returns a new async handler, the returned handler controls the completion of the test.<p/>
    # 
    #  When the returned handler is called back with a failed result it completes the async operation.<p/>
    # 
    #  When the returned handler is called back with a succeeded result it fails the test.<p/>
    # @yield the cause handler
    # @return [Proc] the async result handler
    def async_assert_failure
      if !block_given?
        return ::Vertx::Util::Utils.to_async_result_handler_proc(@j_del.java_method(:asyncAssertFailure, []).call()) { |val| ::Vertx::Util::Utils.to_object(val) }
      elsif block_given?
        return ::Vertx::Util::Utils.to_async_result_handler_proc(@j_del.java_method(:asyncAssertFailure, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.from_throwable(event)) }))) { |val| ::Vertx::Util::Utils.to_object(val) }
      end
      raise ArgumentError, "Invalid arguments when calling async_assert_failure()"
    end
    # @return [Proc] an exception handler that will fail this context
    def exception_handler
      if !block_given?
        return ::Vertx::Util::Utils.to_handler_proc(@j_del.java_method(:exceptionHandler, []).call()) { |val| ::Vertx::Util::Utils.to_throwable(val) }
      end
      raise ArgumentError, "Invalid arguments when calling exception_handler()"
    end
  end
end
