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

import java.util.Map;
import org.apache.solr.SolrTestCaseJ4;
import org.junit.Test;

public class TestOtelMetricsConfig extends SolrTestCaseJ4 {

  @Test
  public void testStandardEnvironmentVariablesAndGenericEndpoint() {
    OtelMetricsConfig config =
        new OtelMetricsConfig(
            Map.of(
                "OTEL_METRICS_EXPORTER", "otlp",
                "OTEL_EXPORTER_OTLP_PROTOCOL", "grpc",
                "OTEL_EXPORTER_OTLP_METRICS_PROTOCOL", "http/protobuf",
                "OTEL_METRIC_EXPORT_INTERVAL", "1234",
                "OTEL_EXPORTER_OTLP_ENDPOINT", "http://collector.example:4318/base?tenant=solr"),
            Map.of());

    assertTrue(config.isExporterEnabled());
    assertEquals("http/protobuf", config.getProtocol());
    assertEquals(1234, config.getExportInterval());
    assertEquals(
        "http://collector.example:4318/base/v1/metrics?tenant=solr", config.getHttpEndpoint());
    assertEquals("http://collector.example:4318/base?tenant=solr", config.getGrpcEndpoint());
  }

  @Test
  public void testMetricsSpecificEndpointTakesPrecedenceOverGenericEndpoint() {
    OtelMetricsConfig config =
        new OtelMetricsConfig(
            Map.of(
                "OTEL_EXPORTER_OTLP_ENDPOINT", "http://generic.example:4318/base",
                "OTEL_EXPORTER_OTLP_METRICS_ENDPOINT",
                    "http://metrics.example:4318/custom/v1/metrics?tenant=solr"),
            Map.of());

    assertEquals(
        "http://metrics.example:4318/custom/v1/metrics?tenant=solr", config.getHttpEndpoint());
    assertEquals(
        "http://metrics.example:4318/custom/v1/metrics?tenant=solr", config.getGrpcEndpoint());
  }

  @Test
  public void testSystemPropertiesTakePrecedenceOverEnvironment() {
    OtelMetricsConfig config =
        new OtelMetricsConfig(
            Map.of(
                "OTEL_METRICS_EXPORTER", "otlp",
                "OTEL_EXPORTER_OTLP_METRICS_PROTOCOL", "grpc",
                "OTEL_EXPORTER_OTLP_METRICS_ENDPOINT", "http://environment.example/v1/metrics"),
            Map.of(
                "otel.metrics.exporter", "none",
                "otel.exporter.otlp.metrics.protocol", "http/protobuf",
                "otel.exporter.otlp.metrics.endpoint", "http://system.example/v1/metrics"));

    assertFalse(config.isExporterEnabled());
    assertEquals("http/protobuf", config.getProtocol());
    assertEquals("http://system.example/v1/metrics", config.getHttpEndpoint());
  }

  @Test
  public void testLegacySolrPropertiesRemainFallbacks() {
    OtelMetricsConfig config =
        new OtelMetricsConfig(
            Map.of(),
            Map.of(
                "solr.metrics.otlpExporterEnabled", "true",
                "solr.metrics.otlp.exporter.protocol", "http",
                "solr.metrics.otlpExporterInterval", "4321",
                "solr.metrics.otlpGrpcExporterEndpoint", "http://legacy.example:4317",
                "solr.metrics.otlp.http.exporter.endpoint",
                "http://legacy.example:4318/v1/metrics"));

    assertTrue(config.isExporterEnabled());
    assertEquals("http/protobuf", config.getProtocol());
    assertEquals(4321, config.getExportInterval());
    assertEquals("http://legacy.example:4317", config.getGrpcEndpoint());
    assertEquals("http://legacy.example:4318/v1/metrics", config.getHttpEndpoint());
  }

  @Test
  public void testExplicitStandardNoneOverridesLegacyEnablement() {
    OtelMetricsConfig config =
        new OtelMetricsConfig(
            Map.of("OTEL_METRICS_EXPORTER", "none"),
            Map.of("solr.metrics.otlpExporterEnabled", "true"));

    assertFalse(config.isExporterEnabled());
  }

  @Test
  public void testDefaults() {
    OtelMetricsConfig config = new OtelMetricsConfig(Map.of(), Map.of());

    assertFalse(config.isExporterEnabled());
    assertEquals("http/protobuf", config.getProtocol());
    assertEquals(60000, config.getExportInterval());
    assertEquals("http://localhost:4318/v1/metrics", config.getHttpEndpoint());
    assertEquals("http://localhost:4317", config.getGrpcEndpoint());
  }
}
