package io.vertx.ext.unit.report;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

public class ReportOptionsConverter {

  public static void fromJson(JsonObject json, ReportOptions obj) {
    if (json.getValue("format") instanceof String) {
      obj.setFormat((String)json.getValue("format"));
    }
    if (json.getValue("to") instanceof String) {
      obj.setTo((String)json.getValue("to"));
    }
  }

  public static void toJson(ReportOptions obj, JsonObject json) {
    if (obj.getFormat() != null) {
      json.put("format", obj.getFormat());
    }
    if (obj.getTo() != null) {
      json.put("to", obj.getTo());
    }
  }
}