package io.vertx.ext.unit;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

public class TestOptionsConverter {

  public static void fromJson(JsonObject json, TestOptions obj) {
    if (json.getValue("timeout") instanceof Number) {
      obj.setTimeout(((Number)json.getValue("timeout")).longValue());
    }
    if (json.getValue("useEventLoop") instanceof Boolean) {
      obj.setUseEventLoop((Boolean)json.getValue("useEventLoop"));
    }
  }

  public static void toJson(TestOptions obj, JsonObject json) {
    json.put("timeout", obj.getTimeout());
    if (obj.isUseEventLoop() != null) {
      json.put("useEventLoop", obj.isUseEventLoop());
    }
  }
}