# Review: SOLR-18471, branch solr-18471-submit (unit 40)

Head reviewed: f621473095 (before the repairs). Gated after repairs: 8938ef3539bc833fbe64a6cea0a4d4e521e7affb. Base: c8ab71430f. Ticket: SOLR-18471, "MetricExporterFactory should use the standard OTEL env vars".

## Pass 1

Review (2026-10-23, vm4):

- Ticket fit. The OTLP exporter should read the standard OpenTelemetry environment variables. The branch moves the exporter settings into `OtlpExporterFactory` (`ExporterSettings`, `configuredHeaders`) and adds tests for each setting. The direction fits the ticket.
- Finding, major: the exporter tests fail on the branch. `TestOtelMetricExporterConfiguration` has 4 of 9 failing at head. Two fail on the endpoint-path check (also on base). Two fail on behaviour the branch is meant to provide (timeout and headers/compression reaching the HTTP collector). The branch does not show that the standard env vars are honoured end to end.
- Finding, major: the focused gate class `TestOtelMetricsConfig` passes on base as well, so it does not prove the change.
- Finding, minor: the branch adds two deployment-guide pages (`metrics-reporting.adoc`, `opentelemetry.adoc`) that are not covered by any gate.
- Repairs: four named-fix commits (see the gate file). No behaviour change.
- Hand-back: `gates/SOLR-18471-review-pass-1.md`, HANDED BACK at pass 1, step 4b. Passes 2 and 3 do not run.
