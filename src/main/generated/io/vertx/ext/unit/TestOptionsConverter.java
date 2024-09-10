package io.vertx.ext.unit;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link io.vertx.ext.unit.TestOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.unit.TestOptions} original class using Vert.x codegen.
 */
public class TestOptionsConverter {

  private static final Base64.Decoder BASE64_DECODER = Base64.getUrlDecoder();
  private static final Base64.Encoder BASE64_ENCODER = Base64.getUrlEncoder().withoutPadding();

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, TestOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "timeout":
          if (member.getValue() instanceof Number) {
            obj.setTimeout(((Number)member.getValue()).longValue());
          }
          break;
        case "useEventLoop":
          if (member.getValue() instanceof Boolean) {
            obj.setUseEventLoop((Boolean)member.getValue());
          }
          break;
      }
    }
  }

   static void toJson(TestOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(TestOptions obj, java.util.Map<String, Object> json) {
    json.put("timeout", obj.getTimeout());
    if (obj.isUseEventLoop() != null) {
      json.put("useEventLoop", obj.isUseEventLoop());
    }
  }
}
