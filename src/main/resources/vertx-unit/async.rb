require 'vertx-unit/completion'
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
    #  @return the current count
    # @return [Fixnum]
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
