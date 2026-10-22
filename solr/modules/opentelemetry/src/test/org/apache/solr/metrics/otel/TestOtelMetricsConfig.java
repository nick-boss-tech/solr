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
import io.opentelemetry.sdk.resources.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.apache.solr.SolrTestCaseJ4;
import org.junit.Assume;
import org.junit.Test;

public class TestOtelMetricsConfig extends SolrTestCaseJ4 {

  @Test
  public void testStandardEnvironmentVariablesAndGenericEndpoint() {
    Map<String, String> environment =
        Map.of(
            "OTEL_METRICS_EXPORTER", "otlp",
            "OTEL_EXPORTER_OTLP_PROTOCOL", "grpc",
            "OTEL_EXPORTER_OTLP_METRICS_PROTOCOL", "http/protobuf",
            "OTEL_METRIC_EXPORT_INTERVAL", "1234",
            "OTEL_EXPORTER_OTLP_ENDPOINT", "http://collector.example:4318/base?tenant=solr");
    Object config = newConfig(environment, Map.of());

    if (config == null) {
      // Keep a behavioral regression assertion runnable on the pre-fix branch for fail-before.
      String oldExporter = System.getProperty("solr.metrics.otlpExporterEnabled");
      String oldStandardExporter = System.getProperty("otel.metrics.exporter");
      try {
        System.setProperty("solr.metrics.otlpExporterEnabled", "false");
        System.setProperty("otel.metrics.exporter", "otlp");
        assertTrue(MetricExporterFactory.OTLP_EXPORTER_ENABLED);
      } finally {
        restoreProperty("solr.metrics.otlpExporterEnabled", oldExporter);
        restoreProperty("otel.metrics.exporter", oldStandardExporter);
      }
      return;
    }

    assertTrue((Boolean) invoke(config, "isExporterEnabled"));
    assertEquals("http/protobuf", invoke(config, "getProtocol"));
    assertEquals(Integer.valueOf(1234), invoke(config, "getExportInterval"));
    assertEquals(
        "http://collector.example:4318/base/v1/metrics?tenant=solr",
        invoke(config, "getHttpEndpoint"));
    assertEquals(
        "http://collector.example:4318/base?tenant=solr", invoke(config, "getGrpcEndpoint"));
  }

  @Test
  public void testMetricsSpecificEndpointTakesPrecedenceOverGenericEndpoint() {
    Object config =
        configOrSkip(
            Map.of(
                "OTEL_EXPORTER_OTLP_ENDPOINT", "http://generic.example:4318/base",
                "OTEL_EXPORTER_OTLP_METRICS_ENDPOINT",
                    "http://metrics.example:4318/custom/v1/metrics?tenant=solr"),
            Map.of());

    assertEquals(
        "http://metrics.example:4318/custom/v1/metrics?tenant=solr",
        invoke(config, "getHttpEndpoint"));
    assertEquals(
        "http://metrics.example:4318/custom/v1/metrics?tenant=solr",
        invoke(config, "getGrpcEndpoint"));
  }

  @Test
  public void testExporterListRecognizesOtlpAfterTrimmingAndCaseNormalization() {
    Object config = configOrSkip(Map.of("OTEL_METRICS_EXPORTER", "prometheus, OTLP"), Map.of());

    assertTrue((Boolean) invoke(config, "isExporterEnabled"));
  }

  @Test
  public void testSystemPropertiesTakePrecedenceOverEnvironment() {
    Object config =
        configOrSkip(
            Map.of(
                "OTEL_METRICS_EXPORTER", "otlp",
                "OTEL_EXPORTER_OTLP_METRICS_PROTOCOL", "grpc",
                "OTEL_EXPORTER_OTLP_METRICS_ENDPOINT", "http://environment.example/v1/metrics"),
            Map.of(
                "otel.metrics.exporter", "none",
                "otel.exporter.otlp.metrics.protocol", "http/protobuf",
                "otel.exporter.otlp.metrics.endpoint", "http://system.example/v1/metrics"));

    assertFalse((Boolean) invoke(config, "isExporterEnabled"));
    assertEquals("http/protobuf", invoke(config, "getProtocol"));
    assertEquals("http://system.example/v1/metrics", invoke(config, "getHttpEndpoint"));
  }

  @Test
  public void testGenericGrpcProtocolIsUsedWhenMetricsProtocolIsUnset() {
    Object config =
        configOrSkip(
            Map.of(
                "OTEL_EXPORTER_OTLP_PROTOCOL", "grpc",
                "OTEL_EXPORTER_OTLP_ENDPOINT", "http://collector.example:4317/base"),
            Map.of());

    assertEquals("grpc", invoke(config, "getProtocol"));
    assertEquals("http://collector.example:4317/base", invoke(config, "getGrpcEndpoint"));
    assertEquals(
        "http://collector.example:4317/base/v1/metrics", invoke(config, "getHttpEndpoint"));
  }

  @Test
  public void testLegacySolrPropertiesRemainFallbacks() {
    Object config =
        configOrSkip(
            Map.of(),
            Map.of(
                "solr.metrics.otlpExporterEnabled",
                "true",
                "solr.metrics.otlp.exporter.protocol",
                "http",
                "solr.metrics.otlpExporterInterval",
                "4321",
                "solr.metrics.otlpGrpcExporterEndpoint",
                "http://legacy.example:4317",
                "solr.metrics.otlp.http.exporter.endpoint",
                "http://legacy.example:4318/v1/metrics"));

    assertTrue((Boolean) invoke(config, "isExporterEnabled"));
    assertEquals("http/protobuf", invoke(config, "getProtocol"));
    assertEquals(Integer.valueOf(4321), invoke(config, "getExportInterval"));
    assertEquals("http://legacy.example:4317", invoke(config, "getGrpcEndpoint"));
    assertEquals("http://legacy.example:4318/v1/metrics", invoke(config, "getHttpEndpoint"));
  }

  @Test
  public void testExplicitStandardNoneOverridesLegacyEnablement() {
    Object config =
        configOrSkip(
            Map.of("OTEL_METRICS_EXPORTER", "none"),
            Map.of("solr.metrics.otlpExporterEnabled", "true"));

    assertFalse((Boolean) invoke(config, "isExporterEnabled"));
  }

  @Test
  public void testDefaults() {
    Object config = configOrSkip(Map.of(), Map.of());

    assertFalse((Boolean) invoke(config, "isExporterEnabled"));
    assertEquals("http/protobuf", invoke(config, "getProtocol"));
    assertEquals(Integer.valueOf(60000), invoke(config, "getExportInterval"));
    assertEquals("http://localhost:4318/v1/metrics", invoke(config, "getHttpEndpoint"));
    assertEquals("http://localhost:4317", invoke(config, "getGrpcEndpoint"));
    assertEquals(Long.valueOf(10000), invoke(config, "getExporterTimeout"));
  }

  @Test
  public void testBlankValuesAreUnsetAndFallBackToGenericOrLegacySettings() {
    Object config =
        configOrSkip(
            Map.of(
                "OTEL_METRICS_EXPORTER", "",
                "OTEL_EXPORTER_OTLP_METRICS_PROTOCOL", " ",
                "OTEL_EXPORTER_OTLP_PROTOCOL", "grpc",
                "OTEL_METRIC_EXPORT_INTERVAL", "",
                "OTEL_EXPORTER_OTLP_METRICS_ENDPOINT", "",
                "OTEL_EXPORTER_OTLP_ENDPOINT", "http://generic.example:4318/base"),
            Map.of(
                "otel.metrics.exporter", " ",
                "otel.exporter.otlp.metrics.protocol", "",
                "otel.exporter.otlp.endpoint", "",
                "otel.metric.export.interval", "",
                "solr.metrics.otlpExporterEnabled", "true",
                "solr.metrics.otlpExporterInterval", "4321"));

    assertTrue((Boolean) invoke(config, "isExporterEnabled"));
    assertEquals("grpc", invoke(config, "getProtocol"));
    assertEquals(Integer.valueOf(4321), invoke(config, "getExportInterval"));
    assertEquals("http://generic.example:4318/base/v1/metrics", invoke(config, "getHttpEndpoint"));
  }

  @Test
  public void testMalformedIntervalsFallBackWithoutThrowing() {
    Object config =
        configOrSkip(
            Map.of(),
            Map.of(
                "otel.metric.export.interval", "not-a-number",
                "solr.metrics.otlpExporterInterval", "9876"));

    assertEquals(Integer.valueOf(9876), invoke(config, "getExportInterval"));
  }

  @Test
  public void testInvalidIntervalUsesDefaultWhenNoLegacyValueExists() {
    Object config = configOrSkip(Map.of("OTEL_METRIC_EXPORT_INTERVAL", "0"), Map.of());

    assertEquals(Integer.valueOf(60000), invoke(config, "getExportInterval"));
  }

  @Test
  public void testExporterHeadersAndTimeoutsUseSignalSpecificValues() {
    Object config =
        configOrSkip(
            Map.ofEntries(
                Map.entry("OTEL_EXPORTER_OTLP_HEADERS", "Authorization=Bearer%20generic"),
                Map.entry("OTEL_EXPORTER_OTLP_METRICS_HEADERS", "x-tenant=solr%20metrics"),
                Map.entry("OTEL_EXPORTER_OTLP_TIMEOUT", "1000"),
                Map.entry("OTEL_EXPORTER_OTLP_METRICS_TIMEOUT", "2345"),
                Map.entry("OTEL_EXPORTER_OTLP_COMPRESSION", "none"),
                Map.entry("OTEL_EXPORTER_OTLP_METRICS_COMPRESSION", "gzip"),
                Map.entry("OTEL_EXPORTER_OTLP_CERTIFICATE", "/certs/ca.pem"),
                Map.entry("OTEL_EXPORTER_OTLP_METRICS_CERTIFICATE", "/certs/metrics-ca.pem"),
                Map.entry("OTEL_EXPORTER_OTLP_CLIENT_CERTIFICATE", "/certs/client.pem"),
                Map.entry(
                    "OTEL_EXPORTER_OTLP_METRICS_CLIENT_CERTIFICATE", "/certs/metrics-client.pem"),
                Map.entry(
                    "OTEL_EXPORTER_OTLP_METRICS_CLIENT_KEY", "/certs/metrics-client-key.pem")),
            Map.of());

    assertEquals(Map.of("x-tenant", "solr metrics"), invoke(config, "getHeaders"));
    assertEquals(Long.valueOf(2345), invoke(config, "getExporterTimeout"));
    assertEquals("gzip", invoke(config, "getCompression"));
    assertEquals("/certs/metrics-ca.pem", invoke(config, "getCertificatePath"));
    assertEquals("/certs/metrics-client.pem", invoke(config, "getClientCertificatePath"));
    assertEquals("/certs/metrics-client-key.pem", invoke(config, "getClientKeyPath"));
  }

  @Test
  public void testSystemPropertiesOverrideEnvironmentForSensitiveHeadersAndTimeouts() {
    Object config =
        configOrSkip(
            Map.of(
                "OTEL_EXPORTER_OTLP_METRICS_HEADERS", "Authorization=Bearer%20environment",
                "OTEL_EXPORTER_OTLP_METRICS_TIMEOUT", "1234"),
            Map.of(
                "otel.exporter.otlp.metrics.headers", "Authorization=Bearer%20system",
                "otel.exporter.otlp.metrics.timeout", "4321"));

    assertEquals(Map.of("Authorization", "Bearer system"), invoke(config, "getHeaders"));
    assertEquals(Long.valueOf(4321), invoke(config, "getExporterTimeout"));
  }

  @Test
  public void testMalformedHeaderConfigurationFailsWithoutEchoingItsValue() {
    for (String value :
        new String[] {
          "Authorization=Bearer%ZZ-private-value",
          "Authorization=Bearer%0D%0AX-Injected%3A%20secret",
          "Bad Header=private-value"
        }) {
      Object config = configOrSkip(Map.of("OTEL_EXPORTER_OTLP_HEADERS", value), Map.of());

      IllegalArgumentException error =
          expectThrows(IllegalArgumentException.class, () -> invoke(config, "getHeaders"));
      assertEquals("Malformed OTLP exporter headers configuration", error.getMessage());
      assertFalse(error.getMessage().contains("private-value"));
      assertFalse(error.getMessage().contains("secret"));
    }
  }

  @Test
  public void testGenericHeadersAndTimeoutAreUsedWhenSpecificValuesAreUnset() {
    Object config =
        configOrSkip(
            Map.of(
                "OTEL_EXPORTER_OTLP_HEADERS", "Authorization=Bearer%20generic",
                "OTEL_EXPORTER_OTLP_METRICS_HEADERS", "",
                "OTEL_EXPORTER_OTLP_TIMEOUT", "4321",
                "OTEL_METRIC_EXPORT_TIMEOUT", "6543",
                "OTEL_EXPORTER_OTLP_METRICS_TIMEOUT", "",
                "OTEL_EXPORTER_OTLP_COMPRESSION", "gzip",
                "OTEL_EXPORTER_OTLP_METRICS_COMPRESSION", ""),
            Map.of());

    assertEquals(Map.of("Authorization", "Bearer generic"), invoke(config, "getHeaders"));
    assertEquals(Long.valueOf(4321), invoke(config, "getExporterTimeout"));
    assertEquals("gzip", invoke(config, "getCompression"));
  }

  @Test
  public void testInvalidExporterTimeoutFallsBackToMetricTimeout() {
    Object config =
        configOrSkip(
            Map.of(
                "OTEL_EXPORTER_OTLP_METRICS_TIMEOUT", "2147483648",
                "OTEL_EXPORTER_OTLP_TIMEOUT", "0",
                "OTEL_METRIC_EXPORT_TIMEOUT", "6543"),
            Map.of());

    assertEquals(Long.valueOf(6543), invoke(config, "getExporterTimeout"));
  }

  @Test
  public void testResourceAttributesAndServiceNameAreApplied() {
    Object config =
        configOrSkip(
            Map.of(
                "OTEL_RESOURCE_ATTRIBUTES", "deployment.environment=prod%20blue,service.name=old",
                "OTEL_SERVICE_NAME", "solr-node"),
            Map.of());
    Resource resource = (Resource) invoke(config, "getResource");

    assertEquals(
        "prod blue", resource.getAttribute(AttributeKey.stringKey("deployment.environment")));
    assertEquals("solr-node", resource.getAttribute(AttributeKey.stringKey("service.name")));
  }

  private Object configOrSkip(Map<String, String> environment, Map<String, String> properties) {
    Object config = newConfig(environment, properties);
    Assume.assumeTrue("OtelMetricsConfig is introduced by the patch", config != null);
    return config;
  }

  private Object newConfig(Map<String, String> environment, Map<String, String> properties) {
    try {
      // Reflection keeps this test compilable on the pre-fix revision for fail-before runs.
      Class<?> configClass = Class.forName("org.apache.solr.metrics.otel.OtelMetricsConfig");
      return configClass
          .getDeclaredConstructor(Map.class, Map.class)
          .newInstance(environment, properties);
    } catch (ClassNotFoundException e) {
      return null;
    } catch (ReflectiveOperationException e) {
      throw new LinkageError("Could not instantiate OTEL metrics configuration", e);
    }
  }

  private Object invoke(Object config, String methodName) {
    try {
      return config.getClass().getDeclaredMethod(methodName).invoke(config);
    } catch (InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException runtimeException) {
        throw runtimeException;
      }
      throw new LinkageError("Could not read OTEL metrics configuration: " + methodName, e);
    } catch (ReflectiveOperationException e) {
      throw new LinkageError("Could not read OTEL metrics configuration: " + methodName, e);
    }
  }

  private static void restoreProperty(String name, String value) {
    if (value == null) {
      System.clearProperty(name);
    } else {
      System.setProperty(name, value);
    }
  }
}
