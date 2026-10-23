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

import com.sun.net.httpserver.HttpServer;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.testing.exporter.InMemoryMetricReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.lucene.util.SuppressForbidden;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.metrics.otel.MetricExporterFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressForbidden(reason = "Uses the JDK HTTP server to receive OTLP exports, and reflection to build the exporter for the test")
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
  public void testStandardMetricsConfigurationReachesExporterFactory() {
    assertTrue(MetricExporterFactory.OTLP_EXPORTER_ENABLED);
    assertEquals("http/protobuf", MetricExporterFactory.OTLP_EXPORTER_PROTOCOL);
    assertEquals(1234, MetricExporterFactory.OTLP_EXPORTER_INTERVAL);
    assertEquals(
        "http://collector.example:4318/base/v1/metrics",
        MetricExporterFactory.OTLP_EXPORTER_HTTP_ENDPOINT);
    assertEquals(
        "http://collector.example:4318/base", MetricExporterFactory.OTLP_EXPORTER_GRPC_ENDPOINT);

    var exporter = new OtlpExporterFactory().getExporter();
    assertNotNull(exporter);
    try {
      assertEquals(
          "io.opentelemetry.exporter.otlp.http.metrics.OtlpHttpMetricExporter",
          exporter.getClass().getName());
    } finally {
      exporter.close();
    }
  }

  @Test
  public void testMetricResourceReadsSettingsAfterFactoryInitialization() throws Exception {
    System.setProperty("otel.resource.attributes", "deployment.environment=search");
    System.setProperty("otel.service.name", "solr-metrics");

    Resource resource =
        (Resource) MetricExporterFactory.class.getMethod("currentResource").invoke(null);

    assertEquals("search", resource.getAttribute(AttributeKey.stringKey("deployment.environment")));
    assertEquals("solr-metrics", resource.getAttribute(AttributeKey.stringKey("service.name")));
  }

  @Test
  public void testConfiguredHeadersAndCompressionReachTheHttpCollector() throws Exception {
    AtomicReference<String> authorizationHeader = new AtomicReference<>();
    AtomicReference<String> compressionHeader = new AtomicReference<>();
    CountDownLatch requestReceived = new CountDownLatch(1);
    HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    server.createContext(
        "/v1/metrics",
        exchange -> {
          exchange.getRequestBody().transferTo(OutputStream.nullOutputStream());
          authorizationHeader.set(exchange.getRequestHeaders().getFirst("Authorization"));
          compressionHeader.set(exchange.getRequestHeaders().getFirst("Content-Encoding"));
          exchange.sendResponseHeaders(200, -1);
          exchange.close();
          requestReceived.countDown();
        });
    server.start();

    InMemoryMetricReader reader = InMemoryMetricReader.create();
    SdkMeterProvider meterProvider =
        SdkMeterProvider.builder().registerMetricReader(reader).build();
    MetricExporter exporter = null;
    try {
      meterProvider.get("solr.test").counterBuilder("test.requests").build().add(1);
      Collection<MetricData> metrics = reader.collectAllMetrics();
      assertFalse(metrics.isEmpty());

      String endpoint = "http://127.0.0.1:" + server.getAddress().getPort() + "/v1/metrics";
      exporter = newExporterWithHeader(endpoint, "Bearer test-exporter-secret", 10000);
      assertNotNull(exporter);
      assertTrue(exporter.export(metrics).join(10, TimeUnit.SECONDS).isSuccess());
      assertTrue(requestReceived.await(10, TimeUnit.SECONDS));
      assertEquals("Bearer test-exporter-secret", authorizationHeader.get());
      assertEquals("gzip", compressionHeader.get());
    } finally {
      if (exporter != null) {
        exporter.close();
      }
      meterProvider.close();
      server.stop(0);
    }
  }

  @Test
  public void testConfiguredTimeoutFailsAStalledHttpExport() throws Exception {
    CountDownLatch requestStarted = new CountDownLatch(1);
    CountDownLatch releaseResponse = new CountDownLatch(1);
    HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    server.createContext(
        "/v1/metrics",
        exchange -> {
          exchange.getRequestBody().transferTo(OutputStream.nullOutputStream());
          requestStarted.countDown();
          try {
            releaseResponse.await(5, TimeUnit.SECONDS);
            exchange.sendResponseHeaders(200, -1);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          } finally {
            exchange.close();
          }
        });
    server.start();

    InMemoryMetricReader reader = InMemoryMetricReader.create();
    SdkMeterProvider meterProvider =
        SdkMeterProvider.builder().registerMetricReader(reader).build();
    MetricExporter exporter = null;
    try {
      meterProvider.get("solr.test").counterBuilder("test.requests").build().add(1);
      Collection<MetricData> metrics = reader.collectAllMetrics();
      String endpoint = "http://127.0.0.1:" + server.getAddress().getPort() + "/v1/metrics";
      exporter = newExporterWithHeader(endpoint, "Bearer test-exporter-secret", 100);

      var result = exporter.export(metrics);
      assertTrue(requestStarted.await(5, TimeUnit.SECONDS));
      assertFalse(result.join(5, TimeUnit.SECONDS).isSuccess());
      assertTrue(result.isDone());
    } finally {
      releaseResponse.countDown();
      if (exporter != null) {
        exporter.close();
      }
      meterProvider.close();
      server.stop(0);
    }
  }

  private MetricExporter newExporterWithHeader(
      String endpoint, String headerValue, long timeoutMillis) {
    try {
      Class<?> settingsClass =
          Class.forName("org.apache.solr.opentelemetry.OtlpExporterFactory$ExporterSettings");
      Object settings =
          settingsClass
              .getDeclaredConstructor(
                  boolean.class,
                  String.class,
                  String.class,
                  String.class,
                  long.class,
                  String.class,
                  Map.class,
                  String.class,
                  String.class,
                  String.class)
              .newInstance(
                  true,
                  "http/protobuf",
                  endpoint,
                  endpoint,
                  timeoutMillis,
                  "gzip",
                  Map.of("Authorization", headerValue),
                  null,
                  null,
                  null);
      Object factory =
          OtlpExporterFactory.class.getDeclaredConstructor(settingsClass).newInstance(settings);
      return (MetricExporter) factory.getClass().getMethod("getExporter").invoke(factory);
    } catch (ReflectiveOperationException e) {
      throw new LinkageError("Could not create an OTLP exporter with configured headers", e);
    }
  }

  @Test
  public void testInactiveExporterSkipsMalformedHeaderParsing()
      throws ReflectiveOperationException {
    String previous = System.getProperty("otel.exporter.otlp.headers");
    System.setProperty("otel.exporter.otlp.headers", "malformed-header-config");
    try {
      Method configuredHeaders =
          OtlpExporterFactory.class.getDeclaredMethod(
              "configuredHeaders", boolean.class, String.class);
      configuredHeaders.setAccessible(true);

      assertEquals(Map.of(), configuredHeaders.invoke(null, false, "http/protobuf"));
      assertEquals(Map.of(), configuredHeaders.invoke(null, true, "unsupported"));
    } finally {
      if (previous == null) {
        System.clearProperty("otel.exporter.otlp.headers");
      } else {
        System.setProperty("otel.exporter.otlp.headers", previous);
      }
    }
  }

  @Test
  public void testSdkDisablesMetricsLocallyWithoutChangingGlobalSelection() {
    System.setProperty("otel.metrics.exporter", "unsupported");
    OtelTracerConfigurator configurator = new OtelTracerConfigurator(Map.of());
    configurator.prepareConfiguration(new NamedList<>());

    OpenTelemetry openTelemetry = configurator.createOpenTelemetry();
    try {
      assertEquals("unsupported", System.getProperty("otel.metrics.exporter"));
    } finally {
      ((OpenTelemetrySdk) openTelemetry).close();
    }
  }

  @Test
  public void testSdkConstructionFailureDoesNotChangeGlobalMetricsSelection() {
    System.setProperty("otel.traces.exporter", "unsupported");
    OtelTracerConfigurator configurator = new OtelTracerConfigurator(Map.of());
    configurator.prepareConfiguration(new NamedList<>());

    expectThrows(RuntimeException.class, configurator::createOpenTelemetry);
    assertEquals("otlp", System.getProperty("otel.metrics.exporter"));
  }

  @Test
  public void testTracerConfigurationLogRedactsCredentials() {
    String envSecret = "Authorization=Bearer otel-env-secret";
    String propertySecret = "Authorization=Bearer otel-system-secret";
    String apiKeySecret = "otel-api-key-secret";
    System.setProperty("otel.exporter.otlp.metrics.headers", propertySecret);
    System.clearProperty("otel.exporter.otlp.endpoint");
    String endpointWithCredentials =
        "https://otlp-user:otlp-password@collector.example:4318/v1/metrics?"
            + "token=endpoint-query-secret&tenant=solr&signature=endpoint-signature-secret"
            + "#fragment-secret";
    OtelTracerConfigurator configurator =
        new OtelTracerConfigurator(
            Map.of(
                "OTEL_EXPORTER_OTLP_HEADERS",
                envSecret,
                "OTEL_EXPORTER_OTLP_API_KEY",
                apiKeySecret,
                "OTEL_EXPORTER_OTLP_CLIENT_KEY",
                "client-private-key-path-secret",
                "OTEL_EXPORTER_OTLP_CLIENT_CERTIFICATE",
                "client-certificate-path-secret",
                "OTEL_EXPORTER_OTLP_CERTIFICATE",
                "ca-certificate-path-secret",
                "OTEL_EXPORTER_OTLP_ENDPOINT",
                endpointWithCredentials,
                "OTEL_RESOURCE_ATTRIBUTES",
                "auth.token=resource-attribute-secret",
                "OTEL_SERVICE_NAME",
                "solr"));

    String loggedConfiguration = configurator.getCurrentOtelConfigAsString();

    assertTrue(loggedConfiguration.contains("OTEL_EXPORTER_OTLP_HEADERS=<redacted>"));
    assertTrue(loggedConfiguration.contains("OTEL_EXPORTER_OTLP_METRICS_HEADERS=<redacted>"));
    assertTrue(loggedConfiguration.contains("OTEL_EXPORTER_OTLP_API_KEY=<redacted>"));
    assertTrue(loggedConfiguration.contains("OTEL_EXPORTER_OTLP_CLIENT_KEY=<redacted>"));
    assertTrue(loggedConfiguration.contains("OTEL_EXPORTER_OTLP_CLIENT_CERTIFICATE=<redacted>"));
    assertTrue(loggedConfiguration.contains("OTEL_EXPORTER_OTLP_CERTIFICATE=<redacted>"));
    assertFalse(loggedConfiguration.contains(envSecret));
    assertFalse(loggedConfiguration.contains(propertySecret));
    assertFalse(loggedConfiguration.contains(apiKeySecret));
    assertFalse(loggedConfiguration.contains("client-private-key-path-secret"));
    assertFalse(loggedConfiguration.contains("client-certificate-path-secret"));
    assertFalse(loggedConfiguration.contains("ca-certificate-path-secret"));
    assertFalse(loggedConfiguration.contains("otlp-user"));
    assertFalse(loggedConfiguration.contains("otlp-password"));
    assertFalse(loggedConfiguration.contains("endpoint-query-secret"));
    assertFalse(loggedConfiguration.contains("resource-attribute-secret"));
    assertTrue(
        loggedConfiguration.contains(
            "OTEL_EXPORTER_OTLP_ENDPOINT=https://<redacted>@collector.example:4318/v1/metrics?"
                + "<redacted>#<redacted>"));
    assertTrue(loggedConfiguration.contains("OTEL_RESOURCE_ATTRIBUTES=<redacted>"));
    assertTrue(loggedConfiguration.contains("OTEL_SERVICE_NAME=solr"));
  }

  @Test
  public void testTracingAutoconfigurationLeavesMetricsSelectionAvailable() {
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
