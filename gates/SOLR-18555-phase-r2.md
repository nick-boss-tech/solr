# Gate job: SOLR-18555 (text supplied highlighting, phase r2)

Status: CLAIMED (claims/pool-vm2-gate-SOLR-18555-phase-r2.md)

- Branch: `solr-18555-submit`. Head: `59a44dbaf10c627d340f23f5481fdb2ba716f089` (fork tip, confirmed on 2026-10-19). If the tip has moved at claim time, stop and record the new tip. Do not retarget this job.
- Base: `c8ab71430f6e7547c773da596101d680862310ff`. Prior gated head: `b99259cba3572cc08586b2456c78780c814a2bf7`.
- Gate ID: `SOLR-18555-phase-r2`. Claim file: `claims/pool-<host>-gate-SOLR-18555-phase-r2.md`.
- Intended host: any capable Linux host with the gate capability.
- Capability tags: gate.
- Modules: `:solr:core` and `:solr:solr-ref-guide`.
- Focused classes: `org.apache.solr.handler.HighlightRequestHandlerTest` and `org.apache.solr.handler.HighlightRequestHandlerHttpTest`.
- Seed: `18555C0FFEE18555`.
- Changelog: `changelog/unreleased/SOLR-18555.yml`.
- Diff scope against base: 6 files, 895 insertions and 2 deletions. `changelog/unreleased/SOLR-18555.yml` (+8); `solr/core/src/java/org/apache/solr/handler/HighlightRequestHandler.java` (+310); `solr/core/src/test/org/apache/solr/handler/HighlightRequestHandlerHttpTest.java` (+128); `solr/core/src/test/org/apache/solr/handler/HighlightRequestHandlerTest.java` (+405); `solr/server/solr/configsets/_default/conf/solrconfig.xml` (+4, -1); `solr/solr-ref-guide/modules/query-guide/pages/highlighting.adoc` (+40, -1). `SolrRequest.java` has no net change at this head.
- Why: Phase r1 passed at `b99259cba3`, but this follow-up removes `METHOD.QUERY` from the common SolrJ method enum and removes `QUERY` from the handler V2 method list. The former enabled `QUERY` on legacy handlers, including update handlers, and was not supported by `HttpJdkSolrClient`. The new HTTP tests require a `QUERY` update request and a `QUERY` request to the V2 highlight route to fail, and exercise the documented V2 collection route. The guide was shortened. This job proves the changes at the new head.

## What to run

Use the host's standard gate runner. Check out the exact head first. Do not run the build tool directly.

- Step 0: parse `changelog/unreleased/SOLR-18555.yml`.
- Step 1: run tidy. If tidy changes any file, mark this gate GATE INVALID, record the diff, and stop. Do not edit or push the ticket branch from the gate host.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: use production code at prior head `b99259cba3572cc08586b2456c78780c814a2bf7`, overlay only `solr/core/src/test/org/apache/solr/handler/HighlightRequestHandlerHttpTest.java` from this head, and run `org.apache.solr.handler.HighlightRequestHandlerHttpTest.testQueryMethodCannotReachUpdateHandler` with seed `18555C0FFEE18555`. Expected: the test fails at its non-200 assertion because the prior production code accepts the `QUERY` update request. Any compile failure or different failure shape is inconclusive; record it and stop.
- Step 4, focused head run: run `:solr:core:cleanTest` first, then `:solr:core:test --tests org.apache.solr.handler.HighlightRequestHandlerTest --tests org.apache.solr.handler.HighlightRequestHandlerHttpTest -Ptests.seed=18555C0FFEE18555`. Count both classes from fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read. Expected: both classes pass with 0 failures and 0 errors; record skipped and total counts for each class.
- Step 5: run `:solr:core:check -x test`.
- Step 6: run `:solr:solr-ref-guide:check -x test` in a full clone at this exact head, not a linked worktree. The previous attempt at `b99259cba3` failed before `checkSiteLinks` because Antora rejected the linked worktree as a content source; it failed identically at base. A full clone is required so the guide check can run. If the full-clone check fails, record the task and log. Do not call the guide verified unless `checkSiteLinks` ran.

A gate that executes a focused test and fails is not relaunched by the claimant. Record the failure and follow the decisions below.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Branch tip moved at claim time | decided: stop | Record the new tip and stop. Do not retarget this job. | main agent |
| Tidy changes any file | decided: GATE INVALID | Record the tidy diff and stop. Do not commit tidy changes or continue the gate. | main agent |
| Proof leg differs from expected failure | decided: record and stop | Record the test name and failure text. Do not continue to the head run. | main agent |
| Focused head test fails after execution | decided: record and stop | Record failing test, failure text, seed, and fresh XML counts. Do not relaunch. | gate coordinator |
| Cluster fails before any assertion | default: run the same command once more | If the second run also fails before an assertion, record the first exception and stop. Treat it as setup noise, not a test result. | Nick Shanin (standing pool rule) |
| Error Prone or module check fails | decided: record and stop | Record the failing task and log path. Do not relaunch an unchanged tree. | gate coordinator |
| Ref guide check fails before `checkSiteLinks` | decided: record and stop | Record the Antora error and checkout type. Do not claim the guide check passed. | gate coordinator |
| Public text edits | decided: none | Leave draft PR #5183 as-is. No PR state change, description edit, comment, or JIRA action is part of this gate. | Nick Shanin |

No row may leave the choice to the claimant.

## On green

1. Write or refresh `receipts/SOLR-18555.md` with a new top section for gate ID `SOLR-18555-phase-r2`: head, gate date and host, log paths, each step result, proof-leg test and exact failure, and focused counts from fresh XML for both classes.
2. Do not push the ticket branch. Its head is already pushed. Do not change the draft PR or post to JIRA.
3. Mark this gate DONE and the claim DONE in the same push as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed
- [ ] step 0 changelog parse
- [ ] step 1 tidy clean
- [ ] step 2 Error Prone compile
- [ ] step 3 proof leg, expected failure by content
- [ ] step 4 focused head tests, counted from fresh XML
- [ ] step 5 core module check
- [ ] step 6 ref guide check in full clone, including checkSiteLinks
- [ ] receipt written
- [ ] gate DONE, claim DONE

## Deliverable and limits

- One fast-forward push to `coordination` with the receipt and DONE marks.
- The ticket branch head remains unchanged by the gate.
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
