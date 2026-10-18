# Gate job: SOLR-18549 server-side admin proxy fix (round 1)

Status: CLAIMED (vm5, claims/pool-vm5-gate-solr-18549-round-1.md, 2026-10-18T19:01:22Z)

- Branch: `solr-18549-submit` on the fork. Head: `cf4b86934697` (one commit on apache/solr main `c8ab71430f`).
- Intended host: any pool Linux host. Capability tags: gate.
- Module: `:solr:core`. Focused classes: `org.apache.solr.handler.admin.LoggingHandlerTest`, `MetricsHandlerTest`, `api.GetNodeSystemInfoTest`, `proxy.GenericV1RequestProxyTest`, `proxy.V2SolrRequestBasedProxyTest`.
- Diff scope: 13 files under `solr/core` plus `changelog/unreleased/SOLR-18549.yml`.

## Steps

0. Changelog YAML parse of `changelog/unreleased/SOLR-18549.yml`.
1. Tidy. If tidy changes any file, the run is GATE INVALID.
2. Error Prone compile: `:solr:core:compileTestJava -Pvalidation.errorprone=true`.
3. Proof leg: revert the production files under `solr/core/src/java` to apache/solr main `c8ab71430f`, keep the head test files. The new assertions should fail on base. Record the failing tests and messages.
4. Focused head run: clean, then the five classes above on the head. Counts from the fresh JUnit XML.
5. Module check: `:solr:core:check -x test`.

## Result

(pending)
