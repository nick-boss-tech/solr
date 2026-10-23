# Gate job: SOLR-10928 (Request and distributed elevate.q hardening)

Status: CLAIMED (claims/pool-phone-agent-2-gate-SOLR-10928-phase-request-hardening.md)

- Branch: `solr-10928-submit`. Head: `d5621a29281f28659db7b9e9ca57cc46c2651f53` (fork tip, confirmed by ls-remote on 2026-10-23). If the tip has moved at claim time, stop and record the new tip here.
- Base: `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`. Prior gated head: none recorded.
- Gate ID: `SOLR-10928-phase-request-hardening`. Claim file: `claims/pool-<host>-gate-SOLR-10928-phase-request-hardening.md`.
- Intended host: phone-agent-2 independent gate session. WORKFLOW.md lets any capable Linux host claim it if that host is busy.
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.handler.component.QueryElevationComponentTest`. Seed: `10928E1E10928E1E`.
- Diff scope: 7 files, 269 insertions and 3 deletions against base: QueryElevationComponent.java, QueryElevationParams.java, QueryElevationComponentParamTest.java, QueryElevationComponentTest.java, DistributedQueryElevationComponentTest.java, query-elevation-component.adoc, changelog/unreleased/SOLR-10928-elevate-q.yml. Since previously gated head 1ff57c1b21c22031513f733b641496b77057ea55 only the two existing integration test classes and docs changed (183 insertions, 3 deletions).
- Why: Owner explicitly requested hardening because helper-only tests miss request integration. New coverage adds seven embedded request methods and plain/local-parser requests through the existing three-shard test, plus exact precedence/fallback docs. Previous gate at 1ff57c1b21c22031513f733b641496b77057ea55 passed but did not run these request paths. Run standard gate with helper, full QueryElevationComponentTest, DistributedQueryElevationComponentTest, and org.apache.solr.search.TestReRankQParserPlugin in the focused head set; run the same four-class regression set on second seed 10928E2E10928E2E with separate archived fresh XML. Run core check and root check -x test; classify root-only environment/baseline failures separately if any. No builds may overlap; use host lock and guards. Independent gate session owns validation, implementation session will not touch source during execution. No PR/comment/Jira/public-text actions.

## What to run

Use the host's standard gate runner. Do not run the build tool directly. Check the worktree out at the head first.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-10928-elevate-q.yml`.
- Step 1: tidy. If tidy changes any file, apply the tidy output, record its patch and the tidied tree sha in this job, and continue from step 2. WORKFLOW.md, Gate repairs.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: Temporarily return parserQueryString unconditionally from getElevationQueryString, select QueryElevationComponentTest.testElevateQOverrides* and require exactly those two request-level tests to fail on missing expected elevated IDs or result counts, with zero errors or skips; restore production source before head tests. Also verify parser fallback discrimination by temporarily making parserQueryString equal queryString unconditionally, selecting testElevateQFallbackUsesParsedQuery and requiring its single assertion failure, then restore. Keep separate logs/XML for both proofs. Any other failure shape stops the job and follows the failure-route row.
- Step 4, focused head run: `:solr:core:cleanTest` first, then `:solr:core:test --tests org.apache.solr.handler.component.QueryElevationComponentTest -Ptests.seed=10928E1E10928E1E`. Count from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
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

1. Write or refresh `receipts/SOLR-10928.md` with a new top section for gate ID `SOLR-10928-phase-request-hardening`: head, gate date and host, log path, step results, the proof-leg result (test name and failure text, or none requested), and the focused counts from the fresh XML. Record every attempt, its repair commits and files, and the gated head.
2. No public text edits in this job unless a later job says so.
3. Mark this job DONE and `claims/pool-<host>-gate-SOLR-10928-phase-request-hardening.md` DONE in the same push as the receipt.

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

## Attempt 1 and compiler-directed repair
Attempt 1 ran at d5621a29281f28659db7b9e9ca57cc46c2651f53. YAML parse and clean tidy passed. Error Prone compile stopped at QueryElevationComponentTest.java:121: `.add(params)` returns void and cannot be used in the fluent assignment. No tests ran. Evidence: /workspace/logs/phone-agent-2-SOLR-10928-request-hardening-20261023/gate-attempt-1/.

Permitted compiler-directed test repair: f2001e640a464e8e861ee3c202821b6dcc4bd497, `SOLR-10928: gate repair: separate void parameter merge`. Only QueryElevationComponentTest.java changed, terminating the fluent setup then invoking requestParams.add(params) separately. No expected values or production behavior changed. Guarded precommit tidy passed. Repair fast-forward pushed and read back at exact head.

The owning host resumes the same claim after the independent subagent launched its build and returned control to the main session. Single changed-tree retry starts at step 0 on f2001e640a464e8e861ee3c202821b6dcc4bd497, with evidence /workspace/logs/phone-agent-2-SOLR-10928-request-hardening-20261023/gate-attempt-2/run/. Wrapper retains the standard runner namespace so the host lock remains held through supplementary proof/seed/root-check runs. Standard runner unchanged. Any failed retry follows the job's handback rule.
