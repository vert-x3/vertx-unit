/*
 * Copyright (c) 2014 Red Hat, Inc. and others
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
 * Converter for {@link io.vertx.ext.unit.report.ReportOptions}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.unit.report.ReportOptions} original class using Vert.x codegen.
 */
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