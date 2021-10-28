package io.vertx.ext.unit.report;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link io.vertx.ext.unit.report.ReportingOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.unit.report.ReportingOptions} original class using Vert.x codegen.
 */
public class ReportingOptionsConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ReportingOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "reporters":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<io.vertx.ext.unit.report.ReportOptions> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new io.vertx.ext.unit.report.ReportOptions((io.vertx.core.json.JsonObject)item));
            });
            obj.setReporters(list);
          }
          break;
      }
    }
  }

  public static void toJson(ReportingOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(ReportingOptions obj, java.util.Map<String, Object> json) {
    if (obj.getReporters() != null) {
      JsonArray array = new JsonArray();
      obj.getReporters().forEach(item -> array.add(item.toJson()));
      json.put("reporters", array);
    }
  }
}
