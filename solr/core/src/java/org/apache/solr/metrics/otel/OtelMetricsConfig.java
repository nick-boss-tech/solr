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

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.sdk.resources.Resource;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.solr.common.util.EnvUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Resolves standard OTEL metrics settings, with the former Solr settings as fallbacks. */
final class OtelMetricsConfig {
  private static final Logger log = LoggerFactory.getLogger(OtelMetricsConfig.class);

  private static final String DEFAULT_HTTP_ENDPOINT = "http://localhost:4318/v1/metrics";
  private static final String DEFAULT_GRPC_ENDPOINT = "http://localhost:4317";
  private static final String DEFAULT_PROTOCOL = "http/protobuf";
  private static final int DEFAULT_EXPORT_INTERVAL = 60000;
  private static final long DEFAULT_EXPORTER_TIMEOUT = 10000;

  private final Map<String, String> environment;
  private final Map<String, String> systemProperties;

  OtelMetricsConfig(Map<String, String> environment, Map<String, String> systemProperties) {
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
        firstNonBlank(
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
    return getPositiveInt(
        "OTEL_METRIC_EXPORT_INTERVAL",
        getLegacyProperty("solr.metrics.otlpExporterInterval", null),
        DEFAULT_EXPORT_INTERVAL);
  }

  long getExporterTimeout() {
    Long parsed =
        parsePositiveLong(
            get("OTEL_EXPORTER_OTLP_METRICS_TIMEOUT"), "OTEL_EXPORTER_OTLP_METRICS_TIMEOUT");
    if (parsed == null) {
      parsed = parsePositiveLong(get("OTEL_EXPORTER_OTLP_TIMEOUT"), "OTEL_EXPORTER_OTLP_TIMEOUT");
    }
    if (parsed == null) {
      parsed = parsePositiveLong(get("OTEL_METRIC_EXPORT_TIMEOUT"), "OTEL_METRIC_EXPORT_TIMEOUT");
    }
    return parsed == null ? DEFAULT_EXPORTER_TIMEOUT : parsed;
  }

  String getGrpcEndpoint() {
    return getEndpoint(true);
  }

  String getHttpEndpoint() {
    return getEndpoint(false);
  }

  Map<String, String> getHeaders() {
    String headers =
        firstNonBlank(
            get("OTEL_EXPORTER_OTLP_METRICS_HEADERS"), get("OTEL_EXPORTER_OTLP_HEADERS"));
    return parseHeaders(headers);
  }

  String getCompression() {
    return firstNonBlank(
        get("OTEL_EXPORTER_OTLP_METRICS_COMPRESSION"), get("OTEL_EXPORTER_OTLP_COMPRESSION"));
  }

  String getCertificatePath() {
    return firstNonBlank(
        get("OTEL_EXPORTER_OTLP_METRICS_CERTIFICATE"), get("OTEL_EXPORTER_OTLP_CERTIFICATE"));
  }

  String getClientCertificatePath() {
    return firstNonBlank(
        get("OTEL_EXPORTER_OTLP_METRICS_CLIENT_CERTIFICATE"),
        get("OTEL_EXPORTER_OTLP_CLIENT_CERTIFICATE"));
  }

  String getClientKeyPath() {
    return firstNonBlank(
        get("OTEL_EXPORTER_OTLP_METRICS_CLIENT_KEY"), get("OTEL_EXPORTER_OTLP_CLIENT_KEY"));
  }

  Resource getResource() {
    AttributesBuilder attributes = Attributes.builder();
    String resourceAttributes = get("OTEL_RESOURCE_ATTRIBUTES");
    parseKeyValueList(resourceAttributes, "OTEL_RESOURCE_ATTRIBUTES")
        .forEach((key, value) -> attributes.put(AttributeKey.stringKey(key), value));
    String serviceName = get("OTEL_SERVICE_NAME");
    if (serviceName != null) {
      attributes.put(AttributeKey.stringKey("service.name"), serviceName);
    }
    return Resource.getDefault().merge(Resource.create(attributes.build()));
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

  private int getPositiveInt(String envName, String legacyValue, int defaultValue) {
    Integer parsed = parsePositiveInt(get(envName), envName);
    if (parsed != null) {
      return parsed;
    }
    return parsePositiveInt(legacyValue, "legacy " + envName, defaultValue);
  }

  private Integer parsePositiveInt(String value, String setting) {
    if (value == null) {
      return null;
    }
    try {
      int parsed = Integer.parseInt(value.trim());
      if (parsed > 0) {
        return parsed;
      }
    } catch (NumberFormatException ignored) {
      // Use the documented fallback below.
    }
    log.warn("Ignoring invalid positive integer for {}", setting);
    return null;
  }

  private int parsePositiveInt(String value, String setting, int defaultValue) {
    Integer parsed = parsePositiveInt(value, setting);
    return parsed == null ? defaultValue : parsed;
  }

  private Long parsePositiveLong(String value, String setting) {
    if (value == null) {
      return null;
    }
    try {
      long parsed = Long.parseLong(value.trim());
      if (parsed > 0 && parsed <= Integer.MAX_VALUE) {
        return parsed;
      }
    } catch (NumberFormatException ignored) {
      // Use the documented fallback below.
    }
    log.warn("Ignoring invalid positive timeout for {}", setting);
    return null;
  }

  private String get(String envName) {
    String systemProperty = nonBlank(systemProperties.get(envNameToSyspropName(envName)));
    return systemProperty != null ? systemProperty : nonBlank(environment.get(envName));
  }

  private String getLegacyProperty(String name, String defaultValue) {
    String value = nonBlank(systemProperties.get(name));
    if (value == null) {
      value = nonBlank(systemProperties.get(camelCaseToDotSeparated(name)));
    }
    return value != null ? value : defaultValue;
  }

  private static String nonBlank(String value) {
    return value == null || value.isBlank() ? null : value;
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

  private Map<String, String> parseKeyValueList(String value, String setting) {
    if (value == null) {
      return Map.of();
    }
    Map<String, String> parsed = new LinkedHashMap<>();
    for (String entry : value.split(",", -1)) {
      int separator = entry.indexOf('=');
      if (separator <= 0) {
        log.warn("Ignoring malformed entry in {}", setting);
        continue;
      }
      String key = entry.substring(0, separator).trim();
      if (key.isEmpty()) {
        log.warn("Ignoring malformed entry in {}", setting);
        continue;
      }
      try {
        String decodedValue =
            URLDecoder.decode(entry.substring(separator + 1).trim(), StandardCharsets.UTF_8);
        parsed.put(key, decodedValue);
      } catch (IllegalArgumentException e) {
        log.warn("Ignoring malformed encoded value in {}", setting);
      }
    }
    return Map.copyOf(parsed);
  }

  private Map<String, String> parseHeaders(String value) {
    if (value == null) {
      return Map.of();
    }
    Map<String, String> parsed = new LinkedHashMap<>();
    for (String entry : value.split(",", -1)) {
      int separator = entry.indexOf('=');
      if (separator <= 0) {
        throw new IllegalArgumentException("Malformed OTLP exporter headers configuration");
      }
      String key = entry.substring(0, separator).trim();
      if (!isValidHeaderName(key)) {
        throw new IllegalArgumentException("Malformed OTLP exporter headers configuration");
      }
      try {
        String decodedValue =
            URLDecoder.decode(entry.substring(separator + 1).trim(), StandardCharsets.UTF_8);
        if (containsHeaderControlCharacter(decodedValue)) {
          throw new IllegalArgumentException("Malformed OTLP exporter headers configuration");
        }
        parsed.put(key, decodedValue);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Malformed OTLP exporter headers configuration");
      }
    }
    return Map.copyOf(parsed);
  }

  private static boolean isValidHeaderName(String name) {
    if (name.isEmpty()) {
      return false;
    }
    for (int i = 0; i < name.length(); i++) {
      char c = name.charAt(i);
      if (!(c >= '0' && c <= '9')
          && !(c >= 'A' && c <= 'Z')
          && !(c >= 'a' && c <= 'z')
          && "!#$%&'*+-.^_`|~".indexOf(c) < 0) {
        return false;
      }
    }
    return true;
  }

  private static boolean containsHeaderControlCharacter(String value) {
    for (int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);
      if ((c < 0x20 && c != '\t') || c == 0x7f) {
        return true;
      }
    }
    return false;
  }

  private static String envNameToSyspropName(String envName) {
    return envName.toLowerCase(Locale.ROOT).replace('_', '.');
  }

  private static String camelCaseToDotSeparated(String key) {
    return String.join(".", key.split("(?=[A-Z])")).replace("..", ".").toLowerCase(Locale.ROOT);
  }

  private static String firstNonBlank(String... values) {
    return Arrays.stream(values).filter(value -> nonBlank(value) != null).findFirst().orElse(null);
  }
}
