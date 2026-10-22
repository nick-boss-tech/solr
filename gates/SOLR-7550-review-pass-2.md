# Gate job: SOLR-7550 review pass 2 (PeerSync 500 from a replica)

Status: DONE (GATE GREEN at fcc8a874ab, vm4, 2026-10-22T05:27:16Z)

- Branch: solr-7550-submit. Head: `fcc8a874abbb8fbd465a835da6903f64de7009b2` (ls-remote 2026-10-22T03:43Z; unchanged since pass 1).
- Prior gated head: `fcc8a874ab` (pass 1, vm4, GATE GREEN).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused classes: `org.apache.solr.update.PeerSyncHandleResponseTest`, `org.apache.solr.update.PeerSyncTest`. Seed: `7550C0FFEE755075`.
- Why: pass 2 of the review round, regate on the head pass 1 reviewed.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope unchanged from pass 1
- [x] step 0 changelog YAML parse, rc=0 (`/workspace/gates/logs/vm4/7550/pass2.log`)
- [x] step 1 tidy, rc=0 (`pass2-step1-tidy.log`)
- [x] step 2 Error Prone compile `:solr:core:compileTestJava -Pvalidation.errorprone=true`, rc=0 (`pass2-step2-errorprone.log`)
- [x] step 3 proof leg (core-init branch removed in worktree `proof-7550`, tests at head): rc=1. Expected failure: `testPeerSyncCountsCoreInitFailureFromVersionRequestAsSuccess`, tests 3, failed 1 (`pass2-step3-proofleg.log`).
- [x] step 4 focused head run, counted from fresh XML: PeerSyncHandleResponseTest tests=3, skipped=0, failures=0, errors=0 (ts 2026-10-22T04:49:59Z); PeerSyncTest tests=1, skipped=0, failures=0, errors=0 (ts 2026-10-22T04:49:59Z). rc=0 (`pass2-step4-core.log`, XML copies in `xml2/`).
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (`pass2-step5-core-check.log`). The first step 5 attempt was lost when the session ended before it logged; it was rerun alone under the Gradle lock.

Log directory on the host: `/workspace/gates/logs/vm4/7550/`.

## Deliverable

- Pass 2 result in `reports/newdev-critblock-review-round-1/solr-7550-submit.md`.
- Receipt section in `receipts/SOLR-7550.md`, top section.
