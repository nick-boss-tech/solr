# Gate job: SOLR-18507 (Phase 1 recovery policy)

Status: DONE 2026-10-19 (vm1, GATE GREEN at 1ba98f924090; receipts/SOLR-18507.md)

- Branch: `solr-18507-submit`. Head: `1ba98f9240902c26f0bcd411f8787d38555dea8d` (fork tip, confirmed by ls-remote on 2026-10-19). If the tip has moved at claim time, stop and record the new tip here.
- Base: `c8ab71430f6e7547c773da596101d680862310ff`. Prior gated head: none recorded.
- Gate ID: `SOLR-18507-phase-1`. Claim file: `claims/pool-<host>-gate-SOLR-18507-phase-1.md`.
- Intended host: any capable Linux host. WORKFLOW.md lets any capable Linux host claim it if that host is busy.
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.update.PeerSyncFingerprintPolicyTest`. Seed: `18507A001`.
- Diff scope: 5 files: changelog/unreleased/SOLR-18507.yml (8 insertions); solr/core/src/java/org/apache/solr/cloud/RecoveryStrategy.java (2 insertions, 1 deletion); solr/core/src/java/org/apache/solr/update/PeerSync.java (7 insertions); solr/core/src/java/org/apache/solr/update/PeerSyncWithLeader.java (16 insertions, 1 deletion); solr/core/src/test/org/apache/solr/update/PeerSyncFingerprintPolicyTest.java (32 insertions). Total 65 insertions, 2 deletions.
- Why: No receipt covers this exact head. Phase 1 scopes TLOG and PULL fingerprint skipping to recovery while preserving the RealTimeGet syncWithLeader path, and needs its own gate result for a separate release. The policy test uses a helper introduced by this patch, so the base-checkout fail-before run cannot compile and remains inconclusive.

## What to run

Use the host's standard gate runner. Do not run the build tool directly. Check the worktree out at the head first.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-18507.yml`.
- Step 1: tidy. If tidy changes any file, the run is GATE INVALID and stops.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: none requested. Record that in the receipt.
- Step 4, focused head run: `:solr:core:cleanTest` first, then `:solr:core:test --tests org.apache.solr.update.PeerSyncFingerprintPolicyTest -Ptests.seed=18507A001`. Count from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
- Step 5: module check `:solr:core:check -x test`.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure-route row.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Gate failure after tests executed | decided: record and stop | Record the failing test, its failure text, the seed and the XML counts here. Do not relaunch. | Nick Shanin (standing pool rule) |
| Tidy changes any file | decided: GATE INVALID | Record the tidy diff here and stop. Do not commit the tidy change. | Nick Shanin (standing pool rule) |
| Cluster fails to start before any assertion | default: run the same command once more | If the second run also fails before an assertion, record the first exception here and stop. This is harness setup, not a test result. | Nick Shanin (standing pool rule) |
| Proof leg result differs from the stated expectation | decided: record and stop | Record the test name and both failure texts here. Do not edit the tests. | Nick Shanin (standing pool rule) |
| Tip moved at claim time | decided: stop | Record the new tip here and hand back. | Nick Shanin (standing pool rule) |

No row may leave the choice to the claimant.

## On green

1. Write or refresh `receipts/SOLR-18507.md` with a new top section for gate ID `SOLR-18507-phase-1`: head, gate date and host, log path, step results, the proof-leg result (test name and failure text, or none requested), and the focused counts from the fresh XML.
2. No public text edits in this job unless a later job says so.
3. Mark this job DONE and `claims/pool-<host>-gate-SOLR-18507-phase-1.md` DONE in the same push as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog check
- [x] step 1 tidy clean
- [x] step 2 Error Prone compile
- [x] step 3 proof leg, if requested
- [x] step 4 focused head run, counted from fresh XML
- [x] step 5 module check
- [x] receipt written
- [x] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks.
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
