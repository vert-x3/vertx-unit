package io.vertx.ext.unit.report;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonCodec;

/**
 * Converter and Codec for {@link io.vertx.ext.unit.report.ReportingOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.unit.report.ReportingOptions} original class using Vert.x codegen.
 */
public class ReportingOptionsConverter implements JsonCodec<ReportingOptions, JsonObject> {

  public static final ReportingOptionsConverter INSTANCE = new ReportingOptionsConverter();

  @Override public JsonObject encode(ReportingOptions value) { return (value != null) ? value.toJson() : null; }

  @Override public ReportingOptions decode(JsonObject value) { return (value != null) ? new ReportingOptions(value) : null; }

  @Override public Class<ReportingOptions> getTargetClass() { return ReportingOptions.class; }

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ReportingOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "reporters":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<io.vertx.ext.unit.report.ReportOptions> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof JsonObject)
                list.add(new io.vertx.ext.unit.report.ReportOptions((JsonObject)item));
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
