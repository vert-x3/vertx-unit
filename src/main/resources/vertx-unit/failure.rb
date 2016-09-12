require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.unit.report.Failure
module VertxUnit
  #  A failure provides the details of a failure that happened during the execution of a test case.<p/>
  # 
  #  The failure can be:
  #  <ul>
  #    <li>an assertion failure: an assertion failed</li>
  #    <li>an error failure: an expected error occured</li>
  #  </ul>
  class Failure
    # @private
    # @param j_del [::VertxUnit::Failure] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxUnit::Failure] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [true,false] true if the failure is an error failure otherwise it is an assertion failure
    def error?
      if !block_given?
        if @cached_is_error != nil
          return @cached_is_error
        end
        return @cached_is_error = @j_del.java_method(:isError, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling error?()"
    end
    # @return [String] the error message
    def message
      if !block_given?
        if @cached_message != nil
          return @cached_message
        end
        return @cached_message = @j_del.java_method(:message, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling message()"
    end
    # @return [String] the stack trace
    def stack_trace
      if !block_given?
        if @cached_stack_trace != nil
          return @cached_stack_trace
        end
        return @cached_stack_trace = @j_del.java_method(:stackTrace, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling stack_trace()"
    end
  end
end
