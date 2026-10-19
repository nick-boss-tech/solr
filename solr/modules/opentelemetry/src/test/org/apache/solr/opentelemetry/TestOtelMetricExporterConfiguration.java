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
import io.opentelemetry.sdk.OpenTelemetrySdk;
import java.util.Map;
import java.util.Properties;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.util.NamedList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestOtelMetricExporterConfiguration extends SolrTestCaseJ4 {
  private Properties previousOtelProperties;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    previousOtelProperties = new Properties();
    System.getProperties().stringPropertyNames().stream()
        .filter(key -> key.startsWith("otel."))
        .forEach(key -> previousOtelProperties.setProperty(key, System.getProperty(key)));

    System.setProperty("otel.metrics.exporter", "otlp");
    System.setProperty("otel.exporter.otlp.protocol", "grpc");
    System.setProperty("otel.exporter.otlp.metrics.protocol", "http/protobuf");
    System.setProperty("otel.metric.export.interval", "1234");
    System.setProperty("otel.exporter.otlp.endpoint", "http://collector.example:4318/base");
  }

  @Override
  @After
  public void tearDown() throws Exception {
    System.getProperties().stringPropertyNames().stream()
        .filter(key -> key.startsWith("otel."))
        .forEach(System::clearProperty);
    previousOtelProperties.forEach(
        (key, value) -> System.setProperty(key.toString(), value.toString()));
    super.tearDown();
  }

  @Test
  public void testMetricsSelectionIsTemporarilyOverriddenDuringBuild() {
    String exporterDuringBuild =
        OtelTracerConfigurator.withMetricsExporterDisabled(
            () -> System.getProperty("otel.metrics.exporter"));

    assertEquals("none", exporterDuringBuild);
    assertEquals("otlp", System.getProperty("otel.metrics.exporter"));
  }

  @Test
  public void testMetricsSelectionIsRestoredWhenSdkConstructionFails() {
    expectThrows(
        IllegalStateException.class,
        () ->
            OtelTracerConfigurator.withMetricsExporterDisabled(
                () -> {
                  assertEquals("none", System.getProperty("otel.metrics.exporter"));
                  throw new IllegalStateException("failed while building SDK");
                }));

    assertEquals("otlp", System.getProperty("otel.metrics.exporter"));
  }

  @Test
  public void testMetricsSelectionIsClearedWhenItWasOriginallyUnset() {
    System.clearProperty("otel.metrics.exporter");

    String exporterDuringBuild =
        OtelTracerConfigurator.withMetricsExporterDisabled(
            () -> System.getProperty("otel.metrics.exporter"));

    assertEquals("none", exporterDuringBuild);
    assertNull(System.getProperty("otel.metrics.exporter"));
  }

  @Test
  public void testTracingAutoconfigurationRestoresMetricsSelection() {
    OtelTracerConfigurator configurator = new OtelTracerConfigurator(Map.of());
    configurator.prepareConfiguration(new NamedList<>());

    OpenTelemetry openTelemetry = configurator.createOpenTelemetry();
    try {
      assertEquals("otlp", System.getProperty("otel.metrics.exporter"));
    } finally {
      ((OpenTelemetrySdk) openTelemetry).close();
    }
  }
}
