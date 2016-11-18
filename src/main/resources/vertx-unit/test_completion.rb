require 'vertx-unit/completion'
require 'vertx/future'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.unit.TestCompletion
module VertxUnit
  #  This object provides callback-ability for the end of a test suite, the completion <i>succeeds</i>
  #  when all tests pass otherwise it fails.
  class TestCompletion < ::VertxUnit::Completion
    # @private
    # @param j_del [::VertxUnit::TestCompletion] the java delegate
    def initialize(j_del)
      super(j_del, nil)
      @j_del = j_del
    end
    # @private
    # @return [::VertxUnit::TestCompletion] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == TestCompletion
    end
    def @@j_api_type.wrap(obj)
      TestCompletion.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxExtUnit::TestCompletion.java_class
    end
    #  Completes the future upon completion, otherwise fails it.
    # @param [::Vertx::Future] future the future to resolve
    # @return [void]
    def resolve(future=nil)
      if future.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:resolve, [Java::IoVertxCore::Future.java_class]).call(future.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling resolve(#{future})"
    end
    # @return [true,false] true if this completion is completed
    def completed?
      if !block_given?
        return @j_del.java_method(:isCompleted, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling completed?()"
    end
    # @return [true,false] true if the this completion is completed succeeded
    def succeeded?
      if !block_given?
        return @j_del.java_method(:isSucceeded, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling succeeded?()"
    end
    # @return [true,false] true if the this completion is completed and failed
    def failed?
      if !block_given?
        return @j_del.java_method(:isFailed, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling failed?()"
    end
    #  Completion handler to receive a completion signal when this completions completes.
    # @yield the completion handler
    # @return [void]
    def handler
      if block_given?
        return @j_del.java_method(:handler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling handler()"
    end
    #  Cause the current thread to wait until this completion completes with a configurable timeout.<p/>
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
      raise ArgumentError, "Invalid arguments when calling await(#{timeoutMillis})"
    end
    #  Cause the current thread to wait until this completion completes and succeeds with a configurable timeout.<p/>
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
      raise ArgumentError, "Invalid arguments when calling await_success(#{timeoutMillis})"
    end
  end
end
