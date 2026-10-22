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

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import io.opentelemetry.exporter.otlp.http.metrics.OtlpHttpMetricExporter;
import io.opentelemetry.exporter.otlp.http.metrics.OtlpHttpMetricExporterBuilder;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporterBuilder;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.apache.solr.metrics.otel.MetricExporterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class for creating OpenTelemetry OTLP metric exporters and its configuration properties.
 *
 * @see io.opentelemetry.exporter.otlp.http.metrics.OtlpHttpMetricExporter
 * @see io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter
 */
public class OtlpExporterFactory implements MetricExporterFactory {

  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final ExporterSettings settings;

  public OtlpExporterFactory() {
    this(
        new ExporterSettings(
            OTLP_EXPORTER_ENABLED,
            OTLP_EXPORTER_PROTOCOL,
            OTLP_EXPORTER_HTTP_ENDPOINT,
            OTLP_EXPORTER_GRPC_ENDPOINT,
            OTLP_EXPORTER_TIMEOUT,
            OTLP_EXPORTER_COMPRESSION,
            configuredHeaders(OTLP_EXPORTER_ENABLED, OTLP_EXPORTER_PROTOCOL),
            OTLP_EXPORTER_CERTIFICATE,
            OTLP_EXPORTER_CLIENT_CERTIFICATE,
            OTLP_EXPORTER_CLIENT_KEY));
  }

  OtlpExporterFactory(ExporterSettings settings) {
    this.settings = settings;
  }

  @Override
  public MetricExporter getExporter() {
    if (!settings.enabled()) {
      log.info("OTLP metric exporter is disabled.");
      return null;
    }

    return switch (settings.protocol()) {
      case "grpc" -> createGrpcExporter();
      case "http/protobuf", "http" -> createHttpExporter();
      case "none" -> null;
      default -> {
        log.warn("Unknown OTLP exporter protocol. Disabling metric exporter.");
        yield null;
      }
    };
  }

  private MetricExporter createHttpExporter() {
    var builder =
        OtlpHttpMetricExporter.getDefault().toBuilder()
            .setEndpoint(settings.httpEndpoint())
            .setTimeout(settings.timeoutMillis(), MILLISECONDS);
    configure(builder);
    return builder.build();
  }

  private MetricExporter createGrpcExporter() {
    var builder =
        OtlpGrpcMetricExporter.getDefault().toBuilder()
            .setEndpoint(settings.grpcEndpoint())
            .setTimeout(settings.timeoutMillis(), MILLISECONDS);
    configure(builder);
    return builder.build();
  }

  private void configure(OtlpHttpMetricExporterBuilder builder) {
    settings.headers().forEach(builder::addHeader);
    if (settings.compression() != null) {
      builder.setCompression(settings.compression());
    }
    byte[] certificate = readFile(settings.certificatePath(), "OTEL_EXPORTER_OTLP_CERTIFICATE");
    if (certificate != null) {
      builder.setTrustedCertificates(certificate);
    }
    if ((settings.clientCertificatePath() == null) != (settings.clientKeyPath() == null)) {
      throw new IllegalArgumentException(
          "Both OTEL_EXPORTER_OTLP_CLIENT_CERTIFICATE and "
              + "OTEL_EXPORTER_OTLP_CLIENT_KEY must be set");
    }
    byte[] clientCertificate =
        readFile(settings.clientCertificatePath(), "OTEL_EXPORTER_OTLP_CLIENT_CERTIFICATE");
    byte[] clientKey = readFile(settings.clientKeyPath(), "OTEL_EXPORTER_OTLP_CLIENT_KEY");
    if (clientCertificate != null) {
      builder.setClientTls(clientKey, clientCertificate);
    }
  }

  private void configure(OtlpGrpcMetricExporterBuilder builder) {
    settings.headers().forEach(builder::addHeader);
    if (settings.compression() != null) {
      builder.setCompression(settings.compression());
    }
    byte[] certificate = readFile(settings.certificatePath(), "OTEL_EXPORTER_OTLP_CERTIFICATE");
    if (certificate != null) {
      builder.setTrustedCertificates(certificate);
    }
    if ((settings.clientCertificatePath() == null) != (settings.clientKeyPath() == null)) {
      throw new IllegalArgumentException(
          "Both OTEL_EXPORTER_OTLP_CLIENT_CERTIFICATE and "
              + "OTEL_EXPORTER_OTLP_CLIENT_KEY must be set");
    }
    byte[] clientCertificate =
        readFile(settings.clientCertificatePath(), "OTEL_EXPORTER_OTLP_CLIENT_CERTIFICATE");
    byte[] clientKey = readFile(settings.clientKeyPath(), "OTEL_EXPORTER_OTLP_CLIENT_KEY");
    if (clientCertificate != null) {
      builder.setClientTls(clientKey, clientCertificate);
    }
  }

  private static Map<String, String> configuredHeaders(boolean enabled, String protocol) {
    if (!enabled
        || !("grpc".equals(protocol)
            || "http".equals(protocol)
            || "http/protobuf".equals(protocol))) {
      return Map.of();
    }
    return MetricExporterFactory.currentHeaders();
  }

  private static byte[] readFile(String path, String settingName) {
    if (path == null) {
      return null;
    }
    try {
      return Files.readAllBytes(Path.of(path));
    } catch (IOException | RuntimeException e) {
      throw new IllegalArgumentException("Unable to read configured " + settingName + " file");
    }
  }

  record ExporterSettings(
      boolean enabled,
      String protocol,
      String httpEndpoint,
      String grpcEndpoint,
      long timeoutMillis,
      String compression,
      Map<String, String> headers,
      String certificatePath,
      String clientCertificatePath,
      String clientKeyPath) {
    ExporterSettings {
      headers = Map.copyOf(headers);
    }
  }
}
