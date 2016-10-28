package io.vertx.groovy.ext.unit;
public class GroovyStaticExtension {
  public static io.vertx.ext.unit.collect.EventBusCollector create(io.vertx.ext.unit.collect.EventBusCollector j_receiver, io.vertx.core.Vertx vertx, java.util.Map<String, Object> options) {
    return io.vertx.lang.groovy.RetroCompatExtension.wrap(io.vertx.ext.unit.collect.EventBusCollector.create(vertx,
      options != null ? new io.vertx.ext.unit.report.ReportingOptions(io.vertx.lang.groovy.RetroCompatExtension.toJsonObject(options)) : null));
  }
}
