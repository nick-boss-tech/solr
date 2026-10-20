# Gate job: SOLR-18507 (Phase 3 persisted gross fingerprint metadata)

Status: OPEN

- Branch: `solr-18507-phase-3-submit`. Head: `35f6e044dda8c15115bccce6b7c965371a97ad30` (fork tip, confirmed by ls-remote on 2026-10-19). If the tip has moved at claim time, stop and record the new tip here.
- Base: `137e77a4c8baa2684ea758edb6bbaa6bda7b7de7`. Prior gated head: none recorded.
- Gate ID: `SOLR-18507-phase-3`. Claim file: `claims/pool-<host>-gate-SOLR-18507-phase-3.md`.
- Intended host: any capable Linux host. WORKFLOW.md lets any capable Linux host claim it if that host is busy.
- Capability tags: gate.
- Module: `:solr:core`. Focused classes: `org.apache.solr.core.SolrCoreFingerprintCacheTest`, `org.apache.solr.core.SolrCoreFingerprintMergeTest`, and `org.apache.solr.update.SolrIndexFingerprintTest`. Seed: `18507A003`.
- Diff scope: 9 files: changelog/unreleased/SOLR-18507-3.yml (8 insertions); solr/core/src/java/org/apache/solr/core/SchemaCodecFactory.java (21 insertions, 2 deletions); solr/core/src/java/org/apache/solr/core/SolrCore.java (17 insertions, 3 deletions); solr/core/src/java/org/apache/solr/update/IndexFingerprint.java (143 insertions); solr/core/src/java/org/apache/solr/update/IndexFingerprintDocValuesFormat.java (101 insertions); solr/core/src/test-files/solr/collection1/conf/schema-fingerprint-docvaluesformat.xml (35 insertions); solr/core/src/test/org/apache/solr/core/SolrCoreFingerprintCacheTest.java (266 insertions, 2 deletions); solr/core/src/test/org/apache/solr/core/SolrCoreFingerprintMergeTest.java (145 insertions); solr/core/src/test/org/apache/solr/update/SolrIndexFingerprintTest.java (25 insertions, 2 deletions). Total 761 insertions, 9 deletions.
- Why: No receipt covers this head. Phase 3 persists gross summaries in SegmentInfo, validates schema and DocValues generation, and falls back to scans for invalid metadata. Run the cache, merge, and fingerprint test classes changed by this phase; the module check excludes tests. The restart, mixed-version, crash, and performance evidence in the implementation plan remains separate follow-up before claiming the optimization is ready.

## What to run

Use the host's standard gate runner. Do not run the build tool directly. Check the worktree out at the head first.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-18507-3.yml`.
- Step 1: tidy. If tidy changes any file, apply the tidy output, record its patch and the tidied tree sha in this job, and continue from step 2. WORKFLOW.md, Gate repairs.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: none requested. Record that in the receipt.
- Step 4, focused head run: `:solr:core:cleanTest` first, then `:solr:core:test --tests org.apache.solr.core.SolrCoreFingerprintCacheTest --tests org.apache.solr.core.SolrCoreFingerprintMergeTest --tests org.apache.solr.update.SolrIndexFingerprintTest -Ptests.seed=18507A003`. Count from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
- Step 5: module check `:solr:core:check -x test`.

Failures follow the rows below and WORKFLOW.md, Gate repairs. A failed retry hands the job back.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Tidy changes any file | decided: repair | Apply the tidy output on the gate worktree, record its patch and the tidied tree sha here, and continue from step 2. WORKFLOW.md, Gate repairs. | Nick Shanin (standing pool rule) |
| Failure in repair scope (compile, changelog, lint, or up to two failing tests) | decided: repair, then one retry | Make the repair as one commit and push it per WORKFLOW.md. Record the commit, its files, and the failing test or task here, then rerun from step 0 on the new head. Do not rerun an unchanged tree. | Nick Shanin (standing pool rule) |
| Failure outside repair scope, or any failure after the retry | decided: hand back | Set Status to HANDED BACK with the reason. Record the failing test or task, its failure text, the seed, the fresh XML counts, and both attempts. Do not relaunch. | Nick Shanin (standing pool rule) |
| Cluster fails to start before any assertion | default: run the same command once more | If the second run also fails before an assertion, record the first exception here and stop. This is harness setup, not a test result. | Nick Shanin (standing pool rule) |
| Proof leg result differs from the stated expectation | decided: record and stop | Record the test name and both failure texts here. Do not edit the tests. | Nick Shanin (standing pool rule) |
| Tip moved at claim time | decided: stop | Record the new tip here and hand back. | Nick Shanin (standing pool rule) |

No row may leave the choice to the claimant.

## On green

1. Write or refresh `receipts/SOLR-18507.md` with a new top section for gate ID `SOLR-18507-phase-3`: head, gate date and host, log path, step results, the proof-leg result (test name and failure text, or none requested), and the focused counts from the fresh XML. Record every attempt, its repair commits and files, and the gated head.
2. No public text edits in this job unless a later job says so.
3. Mark this job DONE and `claims/pool-<host>-gate-SOLR-18507-phase-3.md` DONE in the same push as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed
- [ ] step 0 changelog check
- [ ] step 1 tidy clean, or repaired and recorded
- [ ] step 2 Error Prone compile
- [ ] step 3 proof leg, if requested
- [ ] step 4 focused head run, counted from fresh XML
- [ ] step 5 module check
- [ ] receipt written
- [ ] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks, plus any repair commits (WORKFLOW.md, Gate repairs).
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
