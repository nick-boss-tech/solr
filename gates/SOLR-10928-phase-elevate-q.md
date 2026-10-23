# Gate job: SOLR-10928 (Support elevate.q in QueryElevationComponent)

Status: OPEN

- Branch: `solr-10928-submit`. Head: `034c9e65b2ce755a0b7dc238e656edc91b103f4b` (fork tip, confirmed by ls-remote on 2026-10-23). If the tip has moved at claim time, stop and record the new tip here.
- Base: `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`. Prior gated head: none recorded.
- Gate ID: `SOLR-10928-phase-elevate-q`. Claim file: `claims/pool-<host>-gate-SOLR-10928-phase-elevate-q.md`.
- Intended host: any capable Linux host. WORKFLOW.md lets any capable Linux host claim it if that host is busy.
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.handler.component.QueryElevationComponentParamTest`. Seed: `10928E1E10928E1E`.
- Diff scope: 5 files, 90 insertions and 3 deletions against Base: solr/core/src/java/org/apache/solr/handler/component/QueryElevationComponent.java; solr/core/src/test/org/apache/solr/handler/component/QueryElevationComponentParamTest.java; solr/solr-ref-guide/modules/query-guide/pages/query-elevation-component.adoc; solr/solrj/src/java/org/apache/solr/common/params/QueryElevationParams.java; changelog/unreleased/SOLR-10928-elevate-q.yml
- Why: This first phase adds the elevate.q request parameter, preserves the parser-derived fallback, and adds focused coverage, documentation, and a changelog entry. The exact branch head is pushed, but this Windows host cannot run Gradle. Run the full hardening gate on Linux, including changelog parsing, tidy, Error Prone compile, the proof leg, the focused class, and the :solr:core module check.

## What to run

Use the host's standard gate runner. Do not run the build tool directly. Check the worktree out at the head first.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-10928-elevate-q.yml`.
- Step 1: tidy. If tidy changes any file, apply the tidy output, record its patch and the tidied tree sha in this job, and continue from step 2. WORKFLOW.md, Gate repairs.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: Temporarily make getElevationQueryString return parserQueryString unconditionally and run the focused class; both elevate.q override tests are expected to fail. Restore the change before the full gate. Any other failure shape stops the job and follows the failure-route row.
- Step 4, focused head run: `:solr:core:cleanTest` first, then `:solr:core:test --tests org.apache.solr.handler.component.QueryElevationComponentParamTest -Ptests.seed=10928E1E10928E1E`. Count from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
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

1. Write or refresh `receipts/SOLR-10928.md` with a new top section for gate ID `SOLR-10928-phase-elevate-q`: head, gate date and host, log path, step results, the proof-leg result (test name and failure text, or none requested), and the focused counts from the fresh XML. Record every attempt, its repair commits and files, and the gated head.
2. No public text edits in this job unless a later job says so.
3. Mark this job DONE and `claims/pool-<host>-gate-SOLR-10928-phase-elevate-q.md` DONE in the same push as the receipt.

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
