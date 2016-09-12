require 'vertx/read_stream'
require 'vertx-unit/test_case_report'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.unit.report.TestSuiteReport
module VertxUnit
  #  The test suite reports is basically a stream of events reporting the test suite execution.
  class TestSuiteReport
    include ::Vertx::ReadStream
    # @private
    # @param j_del [::VertxUnit::TestSuiteReport] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxUnit::TestSuiteReport] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [String] the test suite name
    def name
      if !block_given?
        if @cached_name != nil
          return @cached_name
        end
        return @cached_name = @j_del.java_method(:name, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling name()"
    end
    #  Set an exception handler, the exception handler reports the test suite errors, it can be called mulitple
    #  times before the test ends.
    # @yield the exception handler
    # @return [self]
    def exception_handler
      if block_given?
        @j_del.java_method(:exceptionHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.from_throwable(event)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling exception_handler()"
    end
    # @yield 
    # @return [self]
    def handler
      if block_given?
        @j_del.java_method(:handler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxUnit::TestCaseReport)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling handler()"
    end
    # @return [self]
    def pause
      if !block_given?
        @j_del.java_method(:pause, []).call()
        return self
      end
      raise ArgumentError, "Invalid arguments when calling pause()"
    end
    # @return [self]
    def resume
      if !block_given?
        @j_del.java_method(:resume, []).call()
        return self
      end
      raise ArgumentError, "Invalid arguments when calling resume()"
    end
    # @yield 
    # @return [self]
    def end_handler
      if block_given?
        @j_del.java_method(:endHandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling end_handler()"
    end
  end
end
