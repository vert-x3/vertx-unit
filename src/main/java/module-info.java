module io.vertx.testing.unit {
  requires static io.vertx.docgen;
  requires static io.vertx.codegen.api;
  requires static io.vertx.codegen.json;
  requires io.vertx.core;
  requires io.vertx.core.logging;
  requires java.xml;
  requires junit;
  exports io.vertx.ext.unit;
  exports io.vertx.ext.unit.report;
  exports io.vertx.ext.unit.collect;
  exports io.vertx.ext.unit.junit;
  exports io.vertx.ext.unit.impl to io.vertx.testing.unit.tests;
  exports io.vertx.ext.unit.report.impl to io.vertx.testing.unit.tests;
  exports io.vertx.ext.unit.collect.impl to io.vertx.testing.unit.tests;
}
