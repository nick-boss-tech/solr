# Gate job: SOLR-18471 review pass 1 (OTEL metrics exporter, standard env vars)

Status: HANDED BACK (pass 1, step 4b: `TestOtelMetricExporterConfiguration` fails on the head, 4 of 9 tests; the unit stops here)

- Branch: solr-18471-submit. Review head as reviewed: `f621473095`. Gated head after the review repairs: `8938ef3539bc833fbe64a6cea0a4d4e521e7affb` (repair commits `1bb541e8308`, `526ab3adac0`, `d35409b06f4`, `8938ef3539b`).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:modules:opentelemetry` (compiles `:solr:core` as a dependency). Focused class in the gate script: `org.apache.solr.metrics.otel.TestOtelMetricsConfig`. Seed: `18471C0FFEE18471`.
- Diff scope against `c8ab71430f`: 12 files, changelog, and two deployment-guide pages (`.adoc`, not in the gate).

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog YAML parse, rc=0
- [x] step 1 tidy, rc=0
- [x] step 2 Error Prone compile `:solr:modules:opentelemetry:compileTestJava -Pvalidation.errorprone=true`: rc=0 at `8938ef3539`. Failed before the repairs (see repairs below).
- [ ] step 3 proof leg: NOT DISCRIMINATING. The focused class `TestOtelMetricsConfig` (17 tests) passes on base with `OtlpExporterFactory.java` reverted (the proof run in `/workspace/gates/logs/vm4/18471/pass1-step3-proofleg.log` shows 17 of 17 passing on base). It does not exercise the factory's HTTP export path.
- [x] step 4 focused run of the gate class at head: TestOtelMetricsConfig tests=17, skipped=0, failures=0, errors=0 (ts 2026-10-23T00:21:29Z). rc=0.
- [x] step 5 module check `:solr:modules:opentelemetry:check -x test`: rc=0 at `8938ef3539` (00:22:51).
- [ ] step 4b (extra focused run of the class that does exercise the factory, `org.apache.solr.opentelemetry.TestOtelMetricExporterConfiguration`, seed `18471C0FFEE18471`): FAILS at head. Log `/workspace/gates/logs/vm4/18471/exporter-head.log`, XML `/workspace/gates/logs/vm4/18471/xml/exporter-head.xml`. 9 tests, 4 failed:
  - `testSdkDisablesMetricsLocallyWithoutChangingGlobalSelection`: `ConfigurationException: OTLP endpoint must not have a path: /base`. Also fails on base.
  - `testTracingAutoconfigurationLeavesMetricsSelectionAvailable`: the same `must not have a path` message. Also fails on base.
  - `testConfiguredTimeoutFailsAStalledHttpExport`: `AssertionError` at head (on base: `LinkageError` from the factory). Branch behaviour not passing.
  - `testConfiguredHeadersAndCompressionReachTheHttpCollector`: `AssertionError` at head (on base: `LinkageError`). Branch behaviour not passing.
  - Base run `/workspace/gates/logs/vm4/18471/exporter-proof.log` (base `OtlpExporterFactory.java`, the same test file): 6 failed, including `NoSuchMethodException` for `configuredHeaders`, which the branch adds.

## Repairs (review pass 1, Gate repairs scope)

Each is one commit on `solr-18471-submit`, fast-forward from `f621473095`:
1. `1bb541e8308`: Error Prone (`RethrowReflectiveOperationExceptionAsLinkageError`, `-Werror`) in `TestOtelMetricExporterConfiguration.java:229`: `LinkageError` in place of `AssertionError`.
2. `526ab3adac0`: the same check in `TestOtelMetricsConfig.java:337,350`.
3. `d35409b06f4`: ECJ lint: nine unused static imports in `OtlpExporterFactory.java`. The constants are inherited from `MetricExporterFactory`, so the imports are redundant; removed.
4. `8938ef3539b`: forbidden-apis test check: 29 uses of `com.sun.net.httpserver` and reflection in `TestOtelMetricExporterConfiguration.java`; class-level `@SuppressForbidden(reason=...)` (the repo's convention, `org.apache.lucene.util.SuppressForbidden`).

## Hand-back

- Pass 1 is HANDED BACK at step 4b. Two of the four failing tests (`must not have a path: /base`) fail the same way on base, so they predate the branch; the other two (`AssertionError` at head) are the branch's own HTTP export behaviour, which the branch does not pass. Not diagnosed further here, and the run is not relaunched (a run that executed tests and failed is recorded, not repeated).
- The review (passes 2 and 3) does not run.

Deliverable: `reports/newdev-critblock-review-round-1/solr-18471-submit.md` (pass 1 section), `receipts/SOLR-18471.md` (pass 1 section, HANDED BACK).
