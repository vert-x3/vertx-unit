require 'vertx/future'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.unit.TestCompletion
module VertxUnit
  #  This object provides callback-ability for the end of a test suite.
  class TestCompletion
    # @private
    # @param j_del [::VertxUnit::TestCompletion] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxUnit::TestCompletion] the underlying java delegate
    def j_del
      @j_del
    end
    #  Completes the future when all test cases of the test suite passes, otherwise fails it.
    # @param [::Vertx::Future] future the future to resolve
    # @return [void]
    def resolve(future=nil)
      if future.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:resolve, [Java::IoVertxCore::Future.java_class]).call(future.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling resolve(future)"
    end
    #  @return true if the test suite completed
    # @return [true,false]
    def completed?
      if !block_given?
        return @j_del.java_method(:isCompleted, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling completed?()"
    end
    #  @return true if the test suite completed and succeeded
    # @return [true,false]
    def succeeded?
      if !block_given?
        return @j_del.java_method(:isSucceeded, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling succeeded?()"
    end
    #  @return true if the test suite completed and failed
    # @return [true,false]
    def failed?
      if !block_given?
        return @j_del.java_method(:isFailed, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling failed?()"
    end
    #  Completion handler for the end of the test, the result is successful when all test cases pass otherwise
    #  it is failed.
    # @yield the completion handler
    # @return [void]
    def handler
      if block_given?
        return @j_del.java_method(:handler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling handler()"
    end
    #  Cause the current thread to wait until the test suite completes with a configurable timeout.<p/>
    # 
    #  If completion times out or the current thread is interrupted, an exception will be thrown.
    # @param [Fixnum] timeoutMillis the timeout in milliseconds
    # @return [void]
    def await(timeoutMillis=nil)
      if !block_given? && timeoutMillis == nil
        return @j_del.java_method(:await, []).call()
      elsif timeoutMillis.class == Fixnum && !block_given?
        return @j_del.java_method(:await, [Java::long.java_class]).call(timeoutMillis)
      end
      raise ArgumentError, "Invalid arguments when calling await(timeoutMillis)"
    end
    #  Cause the current thread to wait until the test suite completes and succeeds with a configurable timeout.<p/>
    # 
    #  If completion times out or the current thread is interrupted or the suite fails, an exception will be thrown.
    # @param [Fixnum] timeoutMillis the timeout in milliseconds
    # @return [void]
    def await_success(timeoutMillis=nil)
      if !block_given? && timeoutMillis == nil
        return @j_del.java_method(:awaitSuccess, []).call()
      elsif timeoutMillis.class == Fixnum && !block_given?
        return @j_del.java_method(:awaitSuccess, [Java::long.java_class]).call(timeoutMillis)
      end
      raise ArgumentError, "Invalid arguments when calling await_success(timeoutMillis)"
    end
  end
end
