/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.solr.metrics.otel;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import org.apache.solr.common.util.EnvUtils;

/** Resolves standard OTEL metrics settings, with the former Solr settings as fallbacks. */
final class OtelMetricsConfig {
  private static final String DEFAULT_HTTP_ENDPOINT = "http://localhost:4318/v1/metrics";
  private static final String DEFAULT_GRPC_ENDPOINT = "http://localhost:4317";
  private static final String DEFAULT_PROTOCOL = "http/protobuf";
  private static final String DEFAULT_EXPORT_INTERVAL = "60000";

  private final Map<String, String> environment;
  private final Map<String, String> systemProperties;

  private OtelMetricsConfig(Map<String, String> environment, Map<String, String> systemProperties) {
    this.environment = environment;
    this.systemProperties = systemProperties;
  }

  static OtelMetricsConfig current() {
    return new OtelMetricsConfig(System.getenv(), EnvUtils.getProperties());
  }

  boolean isExporterEnabled() {
    String exporters = get("OTEL_METRICS_EXPORTER");
    if (exporters != null) {
      return Arrays.stream(exporters.split(","))
          .map(String::trim)
          .anyMatch(exporter -> "otlp".equalsIgnoreCase(exporter));
    }

    return Boolean.parseBoolean(getLegacyProperty("solr.metrics.otlpExporterEnabled", "false"));
  }

  String getProtocol() {
    String protocol =
        firstNonNull(
            get("OTEL_EXPORTER_OTLP_METRICS_PROTOCOL"),
            get("OTEL_EXPORTER_OTLP_PROTOCOL"),
            getLegacyProperty("solr.metrics.otlpExporterProtocol", null));
    if (protocol == null) {
      return DEFAULT_PROTOCOL;
    }

    String normalizedProtocol = protocol.trim().toLowerCase(Locale.ROOT);
    return "http".equals(normalizedProtocol) ? DEFAULT_PROTOCOL : normalizedProtocol;
  }

  int getExportInterval() {
    return Integer.parseInt(
        firstNonNull(
            get("OTEL_METRIC_EXPORT_INTERVAL"),
            getLegacyProperty("solr.metrics.otlpExporterInterval", null),
            DEFAULT_EXPORT_INTERVAL));
  }

  String getGrpcEndpoint() {
    return getEndpoint(true);
  }

  String getHttpEndpoint() {
    return getEndpoint(false);
  }

  private String getEndpoint(boolean grpc) {
    String endpoint = get("OTEL_EXPORTER_OTLP_METRICS_ENDPOINT");
    if (endpoint != null) {
      return endpoint;
    }

    endpoint = get("OTEL_EXPORTER_OTLP_ENDPOINT");
    if (endpoint != null) {
      return grpc ? endpoint : appendMetricsPath(endpoint);
    }

    String legacyEndpoint =
        grpc
            ? getLegacyProperty("solr.metrics.otlpGrpcExporterEndpoint", null)
            : getLegacyProperty("solr.metrics.otlpHttpExporterEndpoint", null);
    if (legacyEndpoint != null) {
      return legacyEndpoint;
    }

    return grpc ? DEFAULT_GRPC_ENDPOINT : DEFAULT_HTTP_ENDPOINT;
  }

  private String get(String envName) {
    String systemProperty = systemProperties.get(envNameToSyspropName(envName));
    return systemProperty != null ? systemProperty : environment.get(envName);
  }

  private String getLegacyProperty(String name, String defaultValue) {
    String value = systemProperties.get(name);
    if (value == null) {
      value = systemProperties.get(camelCaseToDotSeparated(name));
    }
    return value != null ? value : defaultValue;
  }

  private static String appendMetricsPath(String endpoint) {
    int suffixStart = endpoint.length();
    int queryStart = endpoint.indexOf('?');
    int fragmentStart = endpoint.indexOf('#');
    if (queryStart >= 0) {
      suffixStart = Math.min(suffixStart, queryStart);
    }
    if (fragmentStart >= 0) {
      suffixStart = Math.min(suffixStart, fragmentStart);
    }

    String base = endpoint.substring(0, suffixStart);
    String suffix = endpoint.substring(suffixStart);
    if (!base.endsWith("/")) {
      base += "/";
    }
    return base + "v1/metrics" + suffix;
  }

  private static String envNameToSyspropName(String envName) {
    return envName.toLowerCase(Locale.ROOT).replace('_', '.');
  }

  private static String camelCaseToDotSeparated(String key) {
    return String.join(".", key.split("(?=[A-Z])")).replace("..", ".").toLowerCase(Locale.ROOT);
  }

  private static String firstNonNull(String... values) {
    return Arrays.stream(values).filter(value -> value != null).findFirst().orElse(null);
  }
}
