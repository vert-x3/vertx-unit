/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.ext.unit.report;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.unit.report.ReportingOptions}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.unit.report.ReportingOptions} original class using Vert.x codegen.
 */
public class ReportingOptionsConverter {

  public static void fromJson(JsonObject json, ReportingOptions obj) {
    if (json.getValue("reporters") instanceof JsonArray) {
      json.getJsonArray("reporters").forEach(item -> {
        if (item instanceof JsonObject)
          obj.addReporter(new io.vertx.ext.unit.report.ReportOptions((JsonObject)item));
      });
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