package io.vertx.ext.unit.report;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link io.vertx.ext.unit.report.ReportOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.unit.report.ReportOptions} original class using Vert.x codegen.
 */
public class ReportOptionsConverter {

  private static final Base64.Decoder BASE64_DECODER = Base64.getUrlDecoder();
  private static final Base64.Encoder BASE64_ENCODER = Base64.getUrlEncoder().withoutPadding();

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ReportOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "to":
          if (member.getValue() instanceof String) {
            obj.setTo((String)member.getValue());
          }
          break;
        case "format":
          if (member.getValue() instanceof String) {
            obj.setFormat((String)member.getValue());
          }
          break;
      }
    }
  }

   static void toJson(ReportOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(ReportOptions obj, java.util.Map<String, Object> json) {
    if (obj.getTo() != null) {
      json.put("to", obj.getTo());
    }
    if (obj.getFormat() != null) {
      json.put("format", obj.getFormat());
    }
  }
}
