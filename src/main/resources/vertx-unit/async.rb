require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.unit.Async
module VertxUnit
  #  An asynchronous exit point for a test.
  class Async
    # @private
    # @param j_del [::VertxUnit::Async] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxUnit::Async] the underlying java delegate
    def j_del
      @j_del
    end
    #  Signals the asynchronous operation is done, this method should be called only once, if the method is called
    #  another time it will throw an <code>IllegalStateException</code> to signal the error.
    # @return [void]
    def complete
      if !block_given?
        return @j_del.java_method(:complete, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling complete()"
    end
  end
end
