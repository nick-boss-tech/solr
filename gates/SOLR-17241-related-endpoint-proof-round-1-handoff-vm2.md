# Handoff: SOLR-17241 related backup and ADDREPLICA proof, round 1 (from vm2)

Status: PARTIAL. No receipt or report written. vm2 claim `claims/SOLR-17241-related-endpoint-proof-round-1-vm2.md` is released by this record. The assignment `gates/SOLR-17241-related-endpoint-proof-round-1.md` is open again for a fresh gate host.

## Identities checked 2026-11-01 UTC (before this record)

- Pinned production head: `86d86e3d3e54` (full `86d86e3d3e54230b5f3e7b6aa758a189b3b2ed6b`).
- Fork `nick-boss-tech/solr:solr-17241-submit`: `86d86e3d3e54`.
- PR apache/solr#5266: open, head `86d86e3d3e54`, 0 comments.
- Parent control: `9859d8bb2e8d`.

## Done on vm2

1. Candidate probe at the pinned head: PASS 21/0/0/0, `rc=0`, Gradle `BUILD SUCCESSFUL in 2m 42s`. Fresh XML: `xml/candidate-probe/TEST-org.apache.solr.handler.RelatedEndpointProofTest.xml`, log `probe-cand.log`.
   - Coverage: 16 backup/ADDREPLICA matrix cases (hosting and non-hosting, javabin and XML, sync and async), 4 JSON baselines, and 1 non-hosting XML `omitHeader=true` reload.
   - All 20 backup and ADDREPLICA cases report `operationCompleted=true`. Each backup restore returns 5 documents.
   - The XML reload returns 200 with `application/xml`.
2. Control run (parent blobs for `MediaTypeOverridingFilter.java` and `MessageBodyWriters.java`, hashes in `control-hashes.txt`, diff in `control-diff.patch`): NOT COMPLETE. The Gradle process ended after `Task :solr:core:test` started, with no `rc=` line and no XML. The log `probe-ctrl.log` contains only the URLPermission and CLDR policy warnings. The outcome is unknown, so no fail-before evidence exists yet.
3. Earlier run 1 (`probe-cand-run1-fixture-failures.log`) failed 17/21 for harness reasons only: JDK HttpClient raw requests denied by the test security policy, and the backup location missing from `solr.security.allow.paths`. The probe now uses the Jetty client and sets the allow path in `@BeforeClass`. Archived, not discarded.
4. An interrupted run after a VM restart (`probe-cand-interrupted-restart.log`) was also not counted.

## Not done

- Control run with a valid outcome and classification. The non-hosting async javabin backup and ADDREPLICA must fail on the parent-blob control with a MIME mismatch or writer NPE after the operation completed. Setup failure does not count as fail-before proof.
- Existing `MediaTypeOverridingFilterTest` (8 methods) and `V2ApiIntegrationTest` (14 tests) on the candidate, with fresh XML and seed `17241C0FFEE17241`.
- Report, receipt, and completion record.

## Next gate host

1. Claim again with a new claim file. Use the idempotency key `SOLR-17241-related-endpoint-proof-round-1@86d86e3d3e54230b5f3e7b6aa758a189b3b2ed6b`.
2. Re-verify the fork and PR heads. Stop and report a mismatch if either differs.
3. Use the probe and control diff under `gates/artifacts/SOLR-17241-related-endpoint-proof-round-1/` on the board. The probe is stored as `RelatedEndpointProofTest.java.txt`, byte-exact to the executed file, so the board's Java format check does not reformat it. Copy it back to `.java` to run it. Check the SHA-256 `e1fb605b8e5aca85f4a6acaf50d18412ca5c168894c65c21c7f79125c9c9b9cd`.
4. Run the candidate probe again only if its XML is not preserved. Run the control with the same probe and topology. Copy each XML before any later run.
5. Run the two existing suites on the candidate. Then write the report and receipt as the assignment requires.

Harness lessons to keep:

- Run each Gradle command under `flock -n /workspace/tools/.gradle-vm2.lock` on vm2 or the equivalent lock on another host, with `env -u NODE_ENV`.
- Set `solr.security.allow.paths` before cluster start.
- Use the Jetty client for raw XML and JSON requests.
- Expect the VM can restart mid-run. Check for `rc=` and the XML before treating a run as finished.
