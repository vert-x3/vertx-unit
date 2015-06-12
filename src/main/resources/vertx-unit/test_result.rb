require 'vertx-unit/failure'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.unit.report.TestResult
module VertxUnit
  #  The result of a test.
  class TestResult
    # @private
    # @param j_del [::VertxUnit::TestResult] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxUnit::TestResult] the underlying java delegate
    def j_del
      @j_del
    end
    #  The test description, may be null if none was provided.
    # @return [String]
    def name
      if !block_given?
        if @cached_name != nil
          return @cached_name
        end
        return @cached_name = @j_del.java_method(:name, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling name()"
    end
    #  The time at which the test began in millis.
    # @return [Fixnum]
    def begin_time
      if !block_given?
        if @cached_begin_time != nil
          return @cached_begin_time
        end
        return @cached_begin_time = @j_del.java_method(:beginTime, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling begin_time()"
    end
    #  How long the test lasted in millis.
    # @return [Fixnum]
    def duration_time
      if !block_given?
        if @cached_duration_time != nil
          return @cached_duration_time
        end
        return @cached_duration_time = @j_del.java_method(:durationTime, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling duration_time()"
    end
    #  Did it succeed?
    # @return [true,false]
    def succeeded?
      if !block_given?
        if @cached_succeeded != nil
          return @cached_succeeded
        end
        return @cached_succeeded = @j_del.java_method(:succeeded, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling succeeded?()"
    end
    #  Did it fail?
    # @return [true,false]
    def failed?
      if !block_given?
        if @cached_failed != nil
          return @cached_failed
        end
        return @cached_failed = @j_del.java_method(:failed, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling failed?()"
    end
    #  An exception describing failure, null if the test succeeded.
    # @return [::VertxUnit::Failure]
    def failure
      if !block_given?
        if @cached_failure != nil
          return @cached_failure
        end
        return @cached_failure = ::Vertx::Util::Utils.safe_create(@j_del.java_method(:failure, []).call(),::VertxUnit::Failure)
      end
      raise ArgumentError, "Invalid arguments when calling failure()"
    end
  end
end
