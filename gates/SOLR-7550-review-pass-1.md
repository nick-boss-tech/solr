# Gate job: SOLR-7550 review pass 1 (PeerSync 500 from a replica)

Status: DONE (GATE GREEN at fcc8a874ab, vm4, 2026-10-22T04:32:05Z)

- Branch: solr-7550-submit. Head: `fcc8a874abbb8fbd465a835da6903f64de7009b2` (ls-remote 2026-10-22T03:43Z). Head unchanged through the run.
- Prior gated head: `fcc8a874ab` (vm2, 2026-10-15, GATE GREEN). This is a regate on vm4 for the review round.
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused classes: `org.apache.solr.update.PeerSyncHandleResponseTest`, `org.apache.solr.update.PeerSyncTest`. Seed: `7550C0FFEE755075`.
- Diff scope against `adf27bf2ad` (the branch's own commits): 3 files, 48 insertions, 11 deletions (`PeerSync.java`, `PeerSyncHandleResponseTest.java`, `changelog/unreleased/SOLR-7550-peersync-500.yml`).
- Why: pass 1 of the review round for unit 25. First gate on vm4 for this head.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog YAML parse, rc=0 (`/workspace/gates/logs/vm4/7550/pass1-step0.log`)
- [x] step 1 tidy, rc=0, status file empty (`pass1-step1-tidy.log`, `pass1-step1-status.txt`)
- [x] step 2 Error Prone compile `:solr:core:compileTestJava -Pvalidation.errorprone=true`, rc=0 (`pass1-step2-errorprone.log`)
- [x] step 3 proof leg: the core-init branch in `PeerSync.java` (the 14-line `SolrCoreInitializationException` check) removed in a worktree at the head, tests at head. Expected failure by content: `testPeerSyncCountsCoreInitFailureFromVersionRequestAsSuccess` fails with `java.lang.AssertionError` at `Assert.fail`. Result: tests 3, failed 1 (the expected test). rc=1 as expected (`pass1-step3-proofleg.log`, `xml/proof-TEST-org.apache.solr.update.PeerSyncHandleResponseTest.xml`).
- [x] step 4 focused head run, counted from fresh XML: PeerSyncHandleResponseTest tests=3, skipped=0, failures=0, errors=0 (ts 2026-10-22T04:27:04Z); PeerSyncTest tests=1, skipped=0, failures=0, errors=0 (ts 2026-10-22T04:27:04Z). rc=0 (`pass1-step4-focused.log`, `xml/`).
- [x] step 5 module check `:solr:core:check -x test`, rc=0, BUILD SUCCESSFUL in 3m 50s (`pass1-step5-core-check.log`).

Log directory on the host: `/workspace/gates/logs/vm4/7550/`. Runner: `/workspace/gates/logs/vm4/7550/run-pass1.sh`.

Not run: the full `:solr:core:test` suite (only the focused classes and the module check, as the template's step list requires).

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-7550-submit.md`.
- Receipt section in `receipts/SOLR-7550.md`, top section.
