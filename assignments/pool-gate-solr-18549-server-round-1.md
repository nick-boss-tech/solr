# Gate job: SOLR-18549 server-side admin proxy fix (round 1)

Status: DONE (vm5, claims/pool-vm5-gate-solr-18549-round-1.md, 2026-10-18T19:01:22Z)

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

GATE GREEN at head `cf4b86934697`, 2026-10-18T19:39:43ZZ. Log dir `/workspace/gates/logs/vm5/g18549/`, steps in `steps.txt`.

- Step 0 changelog parse: rc 0.
- Step 1 tidy: rc 0, tree clean afterwards.
- Step 2 Error Prone compile of `:solr:core:compileTestJava`: rc 0.
- Step 3 proof leg: rc 1, expected. The head tests call `standaloneAllMeansLocal()`, which does not exist on apache `main`, so a plain revert would not compile. The base was apache `main` plus only the hook declaration (`protected boolean standaloneAllMeansLocal() { return false; }` on `RemoteRequestProxy`), with no behavior change. Result: 14 of 36 focused tests fail (LoggingHandlerTest 2, MetricsHandlerTest 2, GetNodeSystemInfoTest 3, GenericV1RequestProxyTest 4, V2SolrRequestBasedProxyTest 3). Each failure is the NullPointerException from the ticket (`Cannot read field "zkStateReader"`), or the 500/200 in place of the 400/local answer. Log `proof.log`, XML `proof.xml/`.
- Restore: head production files restored, tree clean (`proof-restore` rc 0).
- Step 4 focused head run after `cleanTest`: 36 tests, 0 failures, 0 errors, 0 skipped, from fresh XML (`head-clean.xml/`). rc 0.
- Step 5 module check `:solr:core:check -x test`: rc 0, BUILD SUCCESSFUL.
