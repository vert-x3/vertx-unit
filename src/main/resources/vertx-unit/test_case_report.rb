require 'vertx-unit/test_result'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.unit.report.TestCaseReport
module VertxUnit
  #  Report the execution of a test case.
  class TestCaseReport
    # @private
    # @param j_del [::VertxUnit::TestCaseReport] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxUnit::TestCaseReport] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [String] the test case name
    def name
      if !block_given?
        if @cached_name != nil
          return @cached_name
        end
        return @cached_name = @j_del.java_method(:name, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling name()"
    end
    #  Set a callback for completion, the specified <code>handler</code> is invoked when the test exec has completed.
    # @yield the completion handler
    # @return [self]
    def end_handler
      if block_given?
        @j_del.java_method(:endHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxUnit::TestResult)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling end_handler()"
    end
  end
end
