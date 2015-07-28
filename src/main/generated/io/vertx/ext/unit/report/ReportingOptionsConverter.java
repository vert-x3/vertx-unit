package io.vertx.ext.unit.report;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

public class ReportingOptionsConverter {

  public static void fromJson(JsonObject json, ReportingOptions obj) {
    if (json.getValue("reporters") instanceof JsonArray) {
      java.util.List<io.vertx.ext.unit.report.ReportOptions> list = new java.util.ArrayList<>();
      json.getJsonArray("reporters").forEach( item -> {
        if (item instanceof JsonObject)
          list.add(new io.vertx.ext.unit.report.ReportOptions((JsonObject)item));
      });
      obj.setReporters(list);
    }
  }

  public static void toJson(ReportingOptions obj, JsonObject json) {
    if (obj.getReporters() != null) {
      json.put("reporters", new JsonArray(
          obj.getReporters().
              stream().
              map(item -> item.toJson()).
              collect(java.util.stream.Collectors.toList())));
    }
  }
}