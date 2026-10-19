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
package org.apache.solr.opentelemetry;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.solr.common.util.EnvUtils;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.OpenTelemetryConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An {@link OpenTelemetryConfigurator} implementation which exports spans to OpenTelemetry in OTLP
 * format.
 */
public class OtelTracerConfigurator extends OpenTelemetryConfigurator {
  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final Map<String, String> currentEnv;

  public OtelTracerConfigurator() {
    this(System.getenv());
  }

  OtelTracerConfigurator(Map<String, String> currentEnv) {
    this.currentEnv = currentEnv;
  }

  @Override
  public void init(NamedList<?> args) {
    prepareConfiguration(args);
  }

  @Override
  public OpenTelemetry createOpenTelemetry() {
    // deliberately build() and not initialize(); the latter sets GlobalOpenTelemetry, which is
    // OpenTelemetryConfigurator's job
    return AutoConfiguredOpenTelemetrySdk.builder()
        .addPropertiesCustomizer(properties -> Map.of("otel.metrics.exporter", "none"))
        .build()
        .getOpenTelemetrySdk();
  }

  void prepareConfiguration(NamedList<?> args) {
    injectPluginSettingsIfNotConfigured(args);
    setDefaultIfNotConfigured("OTEL_SERVICE_NAME", "solr");
    setDefaultIfNotConfigured("OTEL_TRACES_EXPORTER", "otlp");
    setDefaultIfNotConfigured("OTEL_EXPORTER_OTLP_PROTOCOL", "http/protobuf");
    setDefaultIfNotConfigured("OTEL_TRACES_SAMPLER", "parentbased_always_on");
    setDefaultIfNotConfigured("OTEL_PROPAGATORS", "tracecontext,baggage");
    if (EnvUtils.getProperty("solr.host.advertise") != null) {
      addOtelResourceAttributes(Map.of("host.name", EnvUtils.getProperty("solr.host.advertise")));
    }

    final String currentConfig = getCurrentOtelConfigAsString();
    log.info("OpenTelemetry tracer enabled with configuration: {}", currentConfig);

    // Metrics are disabled in the tracing SDK's local config; logs remain unsupported.
    String metricsExporter = getEnvOrSysprop("OTEL_METRICS_EXPORTER");
    String logsExporter = getEnvOrSysprop("OTEL_LOGS_EXPORTER");
    if ((metricsExporter != null && hasUnsupportedMetricsExporter(metricsExporter))
        || (logsExporter != null && !"none".equalsIgnoreCase(logsExporter))) {
      log.warn("The opentelemetry module supports OTLP metrics only and does not support logs");
    }
    System.setProperty("otel.logs.exporter", "none");
  }

  private static boolean hasUnsupportedMetricsExporter(String exporters) {
    return Arrays.stream(exporters.split(","))
        .map(String::trim)
        .filter(exporter -> !exporter.isEmpty())
        .anyMatch(
            exporter ->
                !"otlp".equalsIgnoreCase(exporter) && !"none".equalsIgnoreCase(exporter));
  }

  /**
   * Will inject plugin configuration values into system properties if not already setup (existing
   * system properties take precedence)
   */
  private void injectPluginSettingsIfNotConfigured(NamedList<?> args) {
    args.forEach(
        (k, v) -> {
          var asSysName = envNameToSyspropName(k);
          if (asSysName.startsWith("otel.")) {
            setDefaultIfNotConfigured(asSysName, v.toString());
          }
        });
  }

  /**
   * Add explicit tags statically to all traces, independent of request. Attributes with same name
   * supplied in ENV or SysProp will take precedence over attributes added in code.
   */
  void addOtelResourceAttributes(Map<String, String> attrsToAdd) {
    String commaSepAttrs = getEnvOrSysprop("OTEL_RESOURCE_ATTRIBUTES");
    Map<String, String> attrs = new HashMap<>(attrsToAdd);
    if (commaSepAttrs != null) {
      attrs.putAll(
          Arrays.stream(commaSepAttrs.split(","))
              .filter(e -> e.contains("="))
              .map(e -> e.strip().split("="))
              .collect(Collectors.toMap(kv -> kv[0], kv -> kv[1])));
    }
    System.setProperty(
        envNameToSyspropName("OTEL_RESOURCE_ATTRIBUTES"),
        attrs.entrySet().stream()
            .map(e -> e.getKey() + "=" + e.getValue())
            .collect(Collectors.joining(",")));
  }

  /** Prepares a string with all configuration K/V pairs sorted and semicolon separated */
  String getCurrentOtelConfigAsString() {
    return getCurrentOtelConfig().entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .map(e -> e.getKey() + "=" + redactSensitiveValue(e.getKey(), e.getValue()))
        .collect(Collectors.joining("; "));
  }

  private static String redactSensitiveValue(String key, String value) {
    String normalizedKey = key.toUpperCase(Locale.ROOT);
    if (normalizedKey.contains("HEADER")
        || normalizedKey.contains("PASSWORD")
        || normalizedKey.contains("TOKEN")
        || normalizedKey.contains("SECRET")
        || normalizedKey.contains("CREDENTIAL")
        || normalizedKey.contains("AUTH")
        || normalizedKey.contains("SIGNATURE")
        || normalizedKey.contains("RESOURCE_ATTRIBUTES")
        || normalizedKey.endsWith("KEY")) {
      return "<redacted>";
    }
    return redactUrlCredentialsAndQuery(value);
  }

  private static String redactUrlCredentialsAndQuery(String value) {
    int schemeEnd = value.indexOf("://");
    if (schemeEnd >= 0) {
      int authorityStart = schemeEnd + 3;
      int authorityEnd = value.length();
      for (char delimiter : new char[] {'/', '?', '#'}) {
        int delimiterIndex = value.indexOf(delimiter, authorityStart);
        if (delimiterIndex >= 0) {
          authorityEnd = Math.min(authorityEnd, delimiterIndex);
        }
      }

      int userInfoEnd = value.lastIndexOf('@', authorityEnd - 1);
      if (userInfoEnd >= authorityStart) {
        value =
            value.substring(0, authorityStart)
                + "<redacted>@"
                + value.substring(userInfoEnd + 1);
      }
    }

    int fragmentStart = value.indexOf('#');
    int queryStart = value.indexOf('?');
    if (fragmentStart >= 0 && (queryStart < 0 || fragmentStart < queryStart)) {
      return value.substring(0, fragmentStart) + "#<redacted>";
    }
    if (queryStart < 0) {
      return value;
    }

    return value.substring(0, queryStart + 1)
        + "<redacted>"
        + (fragmentStart < 0 ? "" : "#<redacted>");
  }

  /**
   * Finds all configuration based on environment with prefix <code>OTEL_</code> or System
   * Properties with prefix <code>otel.</code>.
   *
   * @return a unified map of config, using the ENV_VAR as keys, even if the config was pulled from
   *     a system property.
   */
  Map<String, String> getCurrentOtelConfig() {
    HashMap<String, String> currentConfig = new HashMap<>();
    currentEnv.entrySet().stream()
        .filter(e -> e.getKey().startsWith("OTEL_"))
        .forEach(entry -> currentConfig.put(entry.getKey(), entry.getValue()));
    EnvUtils.getProperties().entrySet().stream()
        .filter(e -> e.getKey().startsWith("otel."))
        .forEach(
            entry -> {
              String key = entry.getKey();
              String envKey = key.toUpperCase(Locale.ROOT).replace('.', '_');
              String value = entry.getValue();
              currentConfig.put(envKey, value);
            });
    return currentConfig;
  }

  /**
   * Returns system property if found, else returns environment variable, or null if none found.
   *
   * @param envName the environment variable to look for
   * @return the resolved value
   */
  String getEnvOrSysprop(String envName) {
    return getConfig(envName, currentEnv);
  }

  /**
   * First checks if the property is defined in environment or properties. If not, we set it as a
   * property.
   *
   * @param envName environment variable name, should start with OTEL_
   * @param defaultValue the value to set if not already configured
   */
  void setDefaultIfNotConfigured(String envName, String defaultValue) {
    String incomingValue = getEnvOrSysprop(envName);
    if (incomingValue == null) {
      EnvUtils.setProperty(envNameToSyspropName(envName), defaultValue);
      if (log.isDebugEnabled()) {
        log.debug("Using default setting {}={}", envName, getEnvOrSysprop(envName));
      }
    }
  }
}
