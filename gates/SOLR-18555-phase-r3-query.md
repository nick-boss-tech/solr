# Gate job: SOLR-18555 (V2 QUERY feasibility for supplied-text highlighting)

Status: NEARLY (claims/pool-vm3-gate-SOLR-18555-phase-r3-query.md; steps 0 to 5 green at 7c859a4d0e; step 6 not verified, environment; receipts/SOLR-18555.md)

- Branch: `solr-18555-query-feasibility`. Head: `e78e776c3e7fe0cf63aac7f0bd705edfeb60e19d` (fork tip, confirmed by ls-remote on 2026-10-20). If the tip has moved at claim time, stop and record the new tip here.
- Base: `2c54781a6ea1c8a5d17b802fbc97fed18005aa2b`. Prior gated head: none recorded.
- Gate ID: `SOLR-18555-phase-r3-query`. Claim file: `claims/pool-<host>-gate-SOLR-18555-phase-r3-query.md`.
- Intended host: any capable Linux host. WORKFLOW.md lets any capable Linux host claim it if that host is busy.
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.handler.HighlightRequestHandlerHttpTest`. Seed: `18555C0FFEE18555`.
- Diff scope: 8 files, 27 insertions and 13 deletions against 2c54781a6ea1; ApiBag.java, V2HttpCall.java, HighlightRequestHandler.java, HttpSolrCall.java, HighlightRequestHandlerHttpTest.java, highlighting.adoc, SolrRequest.java, SOLR-18555.yml.
- Why: Feasibility follow-up to PR 5183. Add QUERY to the shared SolrRequest.METHOD so V2 parsing and authorization retain the real verb, opt in only the supplied-text handler V2 spec, and reject QUERY in legacy HttpSolrCall routes. The phase-r2 gate handed back at this base because its HTTP test used the /c/ route in a non-cloud fixture and hit alias lookup. Nick directed the gate host to make a best-effort test or code repair and retry rather than stop immediately. Preserve the intended collection-route coverage and the V1 update rejection test. HttpJdkSolrClient support remains out of scope for this feasibility branch.

## What to run

Use the host's standard gate runner. Do not run the build tool directly. Check the worktree out at the head first.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-18555.yml`.
- Step 1: tidy. If tidy changes any file, apply the tidy output, record its patch and the tidied tree sha in this job, and continue from step 2. WORKFLOW.md, Gate repairs.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: none requested. Record that in the receipt.
- Step 4, focused head run: `:solr:core:cleanTest` first, then `:solr:core:test --tests org.apache.solr.handler.HighlightRequestHandlerHttpTest -Ptests.seed=18555C0FFEE18555`. Count from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
- Step 5: module check `:solr:core:check -x test`.
- Step 6, ref guide: run `:solr:solr-ref-guide:check -x test` in a full clone at this head. Do not call the guide verified unless `checkSiteLinks` ran.

Failures follow the rows below and WORKFLOW.md, Gate repairs. A failed retry hands the job back.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Tidy changes any file | decided: repair | Apply the tidy output on the gate worktree, record its patch and the tidied tree sha here, and continue from step 2. WORKFLOW.md, Gate repairs. | Nick Shanin (standing pool rule) |
| Failure in repair scope (compile, changelog, lint, or up to two failing tests) | decided: repair, then one retry | Make the repair as one commit and push it per WORKFLOW.md. Record the commit, its files, and the failing test or task here, then rerun from step 0 on the new head. Do not rerun an unchanged tree. | Nick Shanin (standing pool rule) |
| First focused failure outside the old repair scope | decided: best-effort repair, then one retry | Nick directs the gate host to repair tests or code as needed to preserve the intended behavior and make this gate succeed. For the known /c failure in a non-cloud fixture, keep collection-route coverage and use a cloud-capable test setup; do not delete or skip the assertion. Record the repair and rerun from step 0 on the new head. | Nick Shanin (owner direction) |
| Failure remains after the best-effort retry | decided: hand back | Set Status to HANDED BACK with the reason. Record the failing test or task, its failure text, the seed, the fresh XML counts, and both attempts. Do not relaunch. | Nick Shanin (owner direction) |
| Cluster fails to start before any assertion | default: run the same command once more | If the second run also fails before an assertion, record the first exception here and stop. This is harness setup, not a test result. | Nick Shanin (standing pool rule) |
| Proof leg result differs from the stated expectation | decided: record and stop | Record the test name and both failure texts here. Do not edit the tests. | Nick Shanin (standing pool rule) |
| Tip moved at claim time | decided: stop | Record the new tip here and hand back. | Nick Shanin (standing pool rule) |

No row may leave the choice to the claimant.

## On green

1. Write or refresh `receipts/SOLR-18555.md` with a new top section for gate ID `SOLR-18555-phase-r3-query`: head, gate date and host, log path, step results, the proof-leg result (test name and failure text, or none requested), and the focused counts from the fresh XML. Record every attempt, its repair commits and files, and the gated head.
2. No public text edits in this job unless a later job says so.
3. Mark this job DONE and `claims/pool-<host>-gate-SOLR-18555-phase-r3-query.md` DONE in the same push as the receipt.

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
- [ ] step 6 ref guide check in full clone, including checkSiteLinks
- [ ] receipt written
- [ ] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks, plus any repair commits (WORKFLOW.md, Gate repairs).
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
