# Gate job: SOLR-10928 (Support elevate.q in QueryElevationComponent)

Status: CLAIMED (claims/pool-phone-agent-2-gate-SOLR-10928-phase-elevate-q.md)

- Branch: `solr-10928-submit`. Head: `034c9e65b2ce755a0b7dc238e656edc91b103f4b` (fork tip, confirmed by ls-remote on 2026-10-23). If the tip has moved at claim time, stop and record the new tip here.
- Base: `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`. Prior gated head: none recorded.
- Gate ID: `SOLR-10928-phase-elevate-q`. Claim file: `claims/pool-<host>-gate-SOLR-10928-phase-elevate-q.md`.
- Intended host: phone-agent-2 (owner direction, 2026-10-22: this gate goes to phone-agent-2). Other hosts: do not claim.
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

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog check
- [x] step 1 tidy clean, or repaired and recorded
- [ ] step 2 Error Prone compile
- [ ] step 3 proof leg, if requested
- [ ] step 4 focused head run, counted from fresh XML
- [ ] step 5 module check
- [ ] receipt written
- [ ] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks, plus any repair commits (WORKFLOW.md, Gate repairs).
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.

## phone-agent-2 execution record

- Started: 2026-10-23T02:51:49Z.
- Assigned head confirmed by public ls-remote: `034c9e65b2ce755a0b7dc238e656edc91b103f4b`. Diff scope matches the stated 5 files, 90 insertions and 3 deletions.
- Worktree: `/workspace/solr`, detached at the assigned head and initially clean.
- Standard runner: `/workspace/tools/phone-agent-2-hardening-gate-v2.py` (unchanged), invoked by `/workspace/tools/phone-agent-2-10928-gate-proof-guard.py`. The wrapper adds a strict check of exactly both requested proof failures before allowing the head test run.
- Configuration: `/workspace/tools/phone-agent-2-10928-gate-attempt-1.json`.
- Attempt 1 evidence: `/workspace/logs/phone-agent-2-SOLR-10928-phase-elevate-q-20261023/attempt-1/`, including `runner.log`, per-step logs, `state.json`, archived XML, and the proof patch. Runner, config, and proof mutation sources are retained there.
- Toolchain: `/usr/lib/jvm/java-21-openjdk-amd64`; host lock and disk/inode guard are enforced by the standard runner. Preflight free space: 35 GB; free inodes: 465,242. No competing Gradle build was active.

### Tidy repair and retry

- Attempt 1: changelog YAML parsed successfully; `tidy` succeeded but changed only the formatting of `parserQueryString` in `solr/core/src/java/org/apache/solr/handler/component/QueryElevationComponent.java`. No tests ran in this attempt.
- Exact tidy patch: `/workspace/logs/phone-agent-2-SOLR-10928-phase-elevate-q-20261023/attempt-1/tidy.patch`; committed patch also retained as `tidy-committed.patch`.
- Tidied tree SHA: `6c54a326b59fc7c98e62851bf518d2d8691b0812`.
- Repair commit: `1ff57c1b21c22031513f733b641496b77057ea55`, authored and committed as Nick Shanin with no trailers. Fast-forward pushed to `solr-10928-submit`, read back by ls-remote.
- Decision applied: the stated tidy repair row and WORKFLOW.md Gate repairs. The unchanged standard runner restarts at step 0 on the repaired head as required by the workflow's one-retry rule, then continues through step 2 and the remaining gate. No unchanged-tree retry.
- Attempt 2 configuration: `/workspace/tools/phone-agent-2-10928-gate-attempt-2.json`; evidence directory: `/workspace/logs/phone-agent-2-SOLR-10928-phase-elevate-q-20261023/attempt-2/`. This is the single repaired-tree retry; any failure hands the job back.
