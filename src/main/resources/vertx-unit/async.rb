require 'vertx-unit/completion'
require 'vertx/future'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.unit.Async
module VertxUnit
  #  An asynchronous exit point for a test.<p/>
  class Async < ::VertxUnit::Completion
    # @private
    # @param j_del [::VertxUnit::Async] the java delegate
    def initialize(j_del)
      super(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxUnit::Async] the underlying java delegate
    def j_del
      @j_del
    end
    #  Completes the future upon completion, otherwise fails it.
    # @param [::Vertx::Future] future the future to resolve
    # @return [void]
    def resolve(future=nil)
      if future.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:resolve, [Java::IoVertxCore::Future.java_class]).call(future.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling resolve(future)"
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
      raise ArgumentError, "Invalid arguments when calling await(timeoutMillis)"
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
      raise ArgumentError, "Invalid arguments when calling await_success(timeoutMillis)"
    end
    # @return [Fixnum] the current count
    def count
      if !block_given?
        return @j_del.java_method(:count, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling count()"
    end
    #  Count down the async.
    # @return [void]
    def count_down
      if !block_given?
        return @j_del.java_method(:countDown, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling count_down()"
    end
    #  Signals the asynchronous operation is done, this method must be called with a count greater than <code>0</code>,
    #  otherwise it throw an <code>IllegalStateException</code> to signal the error.
    # @return [void]
    def complete
      if !block_given?
        return @j_del.java_method(:complete, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling complete()"
    end
  end
end
