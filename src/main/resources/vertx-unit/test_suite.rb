require 'vertx-unit/test_context'
require 'vertx/vertx'
require 'vertx-unit/test_completion'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.unit.TestSuite
module VertxUnit
  #  A named suite of test cases that are executed altogether. The suite suite is created with
  #  the {::VertxUnit::TestSuite#create} and the returned suite contains initially no tests.<p/>
  # 
  #  The suite can declare a callback before the suite with {::VertxUnit::TestSuite#before} or after
  #  the suite with {::VertxUnit::TestSuite#after}.<p/>
  # 
  #  The suite can declare a callback before each test with {::VertxUnit::TestSuite#before_each} or after
  #  each test with {::VertxUnit::TestSuite#after_each}.<p/>
  # 
  #  Each test case of the suite is declared by calling the {::VertxUnit::TestSuite#test} method.
  class TestSuite
    # @private
    # @param j_del [::VertxUnit::TestSuite] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxUnit::TestSuite] the underlying java delegate
    def j_del
      @j_del
    end
    #  Create and return a new test suite.
    # @param [String] name the test suite name
    # @return [::VertxUnit::TestSuite] the created test suite
    def self.create(name=nil)
      if name.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtUnit::TestSuite.java_method(:create, [Java::java.lang.String.java_class]).call(name),::VertxUnit::TestSuite)
      end
      raise ArgumentError, "Invalid arguments when calling create(name)"
    end
    #  Set a callback executed before the tests.
    # @yield the callback
    # @return [self]
    def before
      if block_given?
        @j_del.java_method(:before, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxUnit::TestContext)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling before()"
    end
    #  Set a callback executed before each test and after the suite <code>before</code> callback.
    # @yield the callback
    # @return [self]
    def before_each
      if block_given?
        @j_del.java_method(:beforeEach, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxUnit::TestContext)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling before_each()"
    end
    #  Set a callback executed after the tests.
    # @yield the callback
    # @return [self]
    def after
      if block_given?
        @j_del.java_method(:after, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxUnit::TestContext)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling after()"
    end
    #  Set a callback executed after each test and before the suite <code>after</code> callback.
    # @yield the callback
    # @return [self]
    def after_each
      if block_given?
        @j_del.java_method(:afterEach, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxUnit::TestContext)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling after_each()"
    end
    #  Add a new test case to the suite.
    # @param [String] name the test case name
    # @param [Fixnum] repeat the number of times the test should be repeated
    # @yield the test case
    # @return [self]
    def test(name=nil,repeat=nil)
      if name.class == String && block_given? && repeat == nil
        @j_del.java_method(:test, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(name,(Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxUnit::TestContext)) }))
        return self
      elsif name.class == String && repeat.class == Fixnum && block_given?
        @j_del.java_method(:test, [Java::java.lang.String.java_class,Java::int.java_class,Java::IoVertxCore::Handler.java_class]).call(name,repeat,(Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxUnit::TestContext)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling test(name,repeat)"
    end
    #  Run the testsuite with the specified <code>options</code> and the specified <code>vertx</code> instance.<p/>
    # 
    #  The test suite will be executed on the event loop provided by the <code>vertx</code> argument when
    #  {Hash#set_use_event_loop} is not set to <code>false</code>. The returned
    #  {::VertxUnit::Completion} object can be used to get a completion callback.
    # @overload run()
    # @overload run(options)
    #   @param [Hash] options the test options
    # @overload run(vertx)
    #   @param [::Vertx::Vertx] vertx the vertx instance
    # @overload run(vertx,options)
    #   @param [::Vertx::Vertx] vertx the vertx instance
    #   @param [Hash] options the test options
    # @return [::VertxUnit::TestCompletion] the related test completion
    def run(param_1=nil,param_2=nil)
      if !block_given? && param_1 == nil && param_2 == nil
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:run, []).call(),::VertxUnit::TestCompletion)
      elsif param_1.class == Hash && !block_given? && param_2 == nil
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:run, [Java::IoVertxExtUnit::TestOptions.java_class]).call(Java::IoVertxExtUnit::TestOptions.new(::Vertx::Util::Utils.to_json_object(param_1))),::VertxUnit::TestCompletion)
      elsif param_1.class.method_defined?(:j_del) && !block_given? && param_2 == nil
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:run, [Java::IoVertxCore::Vertx.java_class]).call(param_1.j_del),::VertxUnit::TestCompletion)
      elsif param_1.class.method_defined?(:j_del) && param_2.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:run, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxExtUnit::TestOptions.java_class]).call(param_1.j_del,Java::IoVertxExtUnit::TestOptions.new(::Vertx::Util::Utils.to_json_object(param_2))),::VertxUnit::TestCompletion)
      end
      raise ArgumentError, "Invalid arguments when calling run(param_1,param_2)"
    end
  end
end
