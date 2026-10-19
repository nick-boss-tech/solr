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

import io.opentelemetry.sdk.metrics.export.MetricExporter;

public interface MetricExporterFactory {

  Boolean OTLP_EXPORTER_ENABLED = OtelMetricsConfig.current().isExporterEnabled();

  String OTLP_EXPORTER_PROTOCOL = OtelMetricsConfig.current().getProtocol();

  int OTLP_EXPORTER_INTERVAL = OtelMetricsConfig.current().getExportInterval();

  String OTLP_EXPORTER_GRPC_ENDPOINT =
      OtelMetricsConfig.current().getGrpcEndpoint();

  String OTLP_EXPORTER_HTTP_ENDPOINT =
      OtelMetricsConfig.current().getHttpEndpoint();

  MetricExporter getExporter();
}
