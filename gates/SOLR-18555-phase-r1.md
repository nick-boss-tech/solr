# Gate job: SOLR-18555 (text highlighting request handler, round 1 gate)

Status: DONE (head b99259cba3; receipt receipts/SOLR-18555.md)

- Branch: `solr-18555-submit`. Head: `b99259cba3572cc08586b2456c78780c814a2bf7` (fork tip, confirmed by ls-remote on 2026-10-19). If the tip has moved at claim time, stop and record the new tip here.
- Base: `c8ab71430f6e7547c773da596101d680862310ff`. Prior gated head: none recorded.
- Gate ID: `SOLR-18555-phase-r1`. Claim file: `claims/pool-<host>-gate-SOLR-18555-phase-r1.md`.
- Intended host: any capable Linux host. WORKFLOW.md lets any capable Linux host claim it if that host is busy.
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.handler.HighlightRequestHandlerTest`. Seed: `18555C0FFEE18555`.
- Diff scope: 7 files since base c8ab71430f: changelog/unreleased/SOLR-18555.yml, HighlightRequestHandler.java, HighlightRequestHandlerHttpTest.java, HighlightRequestHandlerTest.java, solrconfig.xml, highlighting.adoc, SolrRequest.java. Head adds 79909b2bd1 (implementor: phrase matches, import) and one tidy repair commit on top of it.
- Why: Round 1 of the three-test decision: the owner rows on the branch (79ddff7d43) settle the phrase, encoder and subquery expectations, and the implementor head implements them. Tidy changed HighlightRequestHandlerTest.java at 79909b2bd1; the repair commit is the head. The focused class is HighlightRequestHandlerTest (expect 10 of 10); HighlightRequestHandlerHttpTest (2 tests) is in the diff scope and runs under the module check if the gate host includes it.

## What to run

Use the host's standard gate runner. Do not run the build tool directly. Check the worktree out at the head first.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-18555.yml`.
- Step 1: tidy. If tidy changes any file, apply the tidy output, record its patch and the tidied tree sha in this job, and continue from step 2. WORKFLOW.md, Gate repairs.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: LinkageError with message "The text highlight request handler must be available", caused by ClassNotFoundException for org.apache.solr.handler.HighlightRequestHandler, when production code is at base and only HighlightRequestHandlerTest.java is overlaid from the head (newHandler now throws LinkageError, not AssertionError) Any other failure shape stops the job and follows the failure-route row.
- Step 4, focused head run: `:solr:core:cleanTest` first, then `:solr:core:test --tests org.apache.solr.handler.HighlightRequestHandlerTest -Ptests.seed=18555C0FFEE18555`. Count from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
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

1. Write or refresh `receipts/SOLR-18555.md` with a new top section for gate ID `SOLR-18555-phase-r1`: head, gate date and host, log path, step results, the proof-leg result (test name and failure text, or none requested), and the focused counts from the fresh XML. Record every attempt, its repair commits and files, and the gated head.
2. No public text edits in this job unless a later job says so.
3. Mark this job DONE and `claims/pool-<host>-gate-SOLR-18555-phase-r1.md` DONE in the same push as the receipt.

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

## Run record (vm3, 2026-10-19)

- Head `b99259cba3`. Proof leg rc=1 as expected (LinkageError, cause ClassNotFoundException). Steps 0 to 5 green. Focused: HighlightRequestHandlerTest 10 of 10, HighlightRequestHandlerHttpTest 2 of 2. Logs under `gates/logs/vm3/18555/gate-r1/`.
