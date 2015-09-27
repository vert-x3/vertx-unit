require 'vertx-unit/completion'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.unit.TestCompletion
module VertxUnit
  #  This object provides callback-ability for the end of a test suite, the completion <i>succeeds</i>
  #  when all tests pass otherwise it fails.
  class TestCompletion < ::VertxUnit::Completion
    # @private
    # @param j_del [::VertxUnit::TestCompletion] the java delegate
    def initialize(j_del)
      super(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxUnit::TestCompletion] the underlying java delegate
    def j_del
      @j_del
    end
  end
end
