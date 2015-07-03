require 'vertx-unit/test_suite_report'
require 'vertx/vertx'
require 'vertx/message_consumer'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.unit.collect.EventBusCollector
module VertxUnit
  #  The event bus collector listen to events on the Vert.x event bus and translate them
  #  into reports.
  class EventBusCollector
    # @private
    # @param j_del [::VertxUnit::EventBusCollector] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxUnit::EventBusCollector] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [::Vertx::Vertx] vertx 
    # @yield 
    # @return [::VertxUnit::EventBusCollector]
    def self.create(vertx=nil,reporter=nil)
      if vertx.class.method_defined?(:j_del) && reporter.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtUnitCollect::EventBusCollector.java_method(:create, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxExtUnitReport::ReportingOptions.java_class]).call(vertx.j_del,Java::IoVertxExtUnitReport::ReportingOptions.new(::Vertx::Util::Utils.to_json_object(reporter))),::VertxUnit::EventBusCollector)
      elsif vertx.class.method_defined?(:j_del) && block_given? && reporter == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtUnitCollect::EventBusCollector.java_method(:create, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxCore::Handler.java_class]).call(vertx.j_del,(Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxUnit::TestSuiteReport)) })),::VertxUnit::EventBusCollector)
      end
      raise ArgumentError, "Invalid arguments when calling create(vertx,reporter)"
    end
    #  Register the collector as a consumer of the event bus with the specified address.
    # @param [String] address the registration address
    # @return [::Vertx::MessageConsumer] the subscribed message consumer
    def register(address=nil)
      if address.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:register, [Java::java.lang.String.java_class]).call(address),::Vertx::MessageConsumer)
      end
      raise ArgumentError, "Invalid arguments when calling register(address)"
    end
  end
end
