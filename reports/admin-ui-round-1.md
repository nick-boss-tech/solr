# Admin UI round 1: round roll-up (audit only; no ticket is draftable on the recorded state)

Claim: `claims/admin-ui-round-1.md` (commit `13830da0da6`). Assignment: `assignments/admin-ui-round-1.md` (commit `5c9b8de53ca`). Part reports: `reports/admin-ui-round-1-u1.md` (SOLR-9759), `-u2.md` (SOLR-9818) and `-u3.md` (SOLR-9831). No drafts were written, as the assignment requires for a round where no ticket is draftable on evidence the receipts support.

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Three subagents in parallel, one per ticket, within the cap of six. Nothing has run: no build, no Gradle, no test, no Selenium or Chrome run, and no compile. No branch, live PR, JIRA item or comment was touched. Nothing was posted.

## The premise, stated once for the round

All three tickets were first flagged as "Admin UI angular, obsolete UI". The Angular UI under `solr/webapp/web` is still the shipped UI on current main, so that premise is false. Each ticket is judged against the UI as it ships today.

## Heads

All three live tips match the assignment (`ls-remote`, fetched explicitly): SOLR-9759 `31e702622dae`, SOLR-9818 `63f2d7ce9267`, SOLR-9831 `f269a70e84f0`. Each has four or five files against `cabedd1d968`, as the record says. The local `upstream/main` in this worktree is `3f5d4c5bf8ac`, one range ahead of the assignment's `8e62c2686882`. The premise files are identical between them, so the readings hold on both.

## Verdicts

| Ticket | Verdict | Draft | Owner decision |
|---|---|---|---|
| SOLR-9759 | **Superseded on main** by `c6f910d3ccf` (#5048, 2026-10-08). The branch duplicates it. The branch's visible-failure claim does not hold. | none | Retire the branch (recommended) |
| SOLR-9818 | Premise **holds in part**. It fails for collection reload and ADDREPLICA, which never reach the interceptor. It holds for other `$http` state changes. | none | Read retries (a count?); Jan Høydahl's request; state-changing GET actions; the schema-designer exemption |
| SOLR-9831 | Premise **holds on a reading**. The fix is cosmetic and correct. Nothing has run. | none | The `TESTING.md` file and the commit body; the `tfoot` colspan; trace-row coverage |

## SOLR-9759: superseded on main

- The Stream screen no longer sends a GET. Main commit `c6f910d3ccf` ("SOLR-9759: Admin UI should post streaming expressions (#5048)", Eric Pugh, 2026-10-08) carries the same fix to the same two files, with its own changelog and test. **The branch duplicates main.** The branch's test is a near copy of main's `testLargeExpressionSucceedsViaUi`.
- **Main's residuals, by reading:**
  - (i) The error callback never runs for HTTP-status failures, because the Query opt-out turns them into the success path (`app.js` lines 425 and 426). So the "HTTP `<status>`" text is never printed, on main or on the branch.
  - (ii) A non-JSON failure body reaches main's success path, which catches the parse error and shows the raw body.
  - (iii) A failure with no body (status 0, or the ten-second default timeout) reaches the success path with an empty body. Main's box stays empty with no message.
- **The branch's own claim does not hold:** the success callback has no guard, so a non-JSON failure body leaves the box blank. The error callback cannot run. The changelog title says the branch "shows request failures instead of staying blank", which is contradicted. Main already ships a changelog for this ticket, so the two fragments would collide in the release notes.
- **The opt-out bypass:** `Query.queryPost` is opted out, and streaming expressions can carry update, delete and commit (`Lang.java` lines 342 to 345). So a Stream-screen POST can write, and a connection drop reaches the success path with no message. The user cannot tell whether the write happened.
- **Premise run (main-side, not run):** main's `AdminUiStreamScreenTest`, all three tests, at `8e62c2686882`. The branch's test is not needed for a merge decision. Whether to spend a Chrome run at all is an owner call.

## SOLR-9818: the premise holds in part

- **Holds:** a status-0 request that reaches the interceptor is replayed at once with `$http(rejection.config)`, with no delay and no method check (`app.js` lines 435 to 441 on main). The note's quote is accurate. The branch's code matches the note, and the change applies cleanly to main (a dry-run apply).
- **Fails for collection reload and ADDREPLICA:** on main, both go through the generated v2 client (`collections.js` line 238, `reloadCollection`; `collections.js` line 303, `ReplicasV2.createReplica`). That client is superagent-based and does not pass through `$httpProvider`, so `httpInterceptor` never sees them. On connection loss they do not replay; they show no message (`services.js` lines 51 to 53) or set `reloadFailure`. The 23-replica report comes from the older `$http`-based UI. **The PR text must not claim the branch fixes either command.**
- **Holds for other state-changing requests that do reach the interceptor:** `Replication.command` (the fetchindex, abortfetch, enable and disable buttons), `Logging.setLevel`, `Security.post`, `ParamSet.submit` and `FileUpload.upload`. `Replication.command` and `Logging.setLevel` send GET by default, so the branch's GET and HEAD rule replays them after a second.
- **The branch's own file:** `$timeout` and `$q` are in scope in the factory (`app.js` line 380). The exceptions mechanism matches the banner (`index.html` lines 152 to 154). The entry is cleared on the next request to the same URL and also on every route change, which the note leaves out. The helper is a window global, because `app.js` is a classic script. Replay is unbounded while the server is away.
- **The test pins the helper only.** `AdminUiRetryPolicyTest` calls `isRetryableRequest` through `executeScript` and sends no request, so no status 0 ever occurs. It does not check the delay, the replay count, the rejection's pass-through, the banner, or the opt-out. An end-to-end check needs a real `$http` request, a status 0 in the browser (an XHR stub, CDP offline emulation, or a severing proxy), a server-side arrival counter, a side-effect-free target for the POST case, and the assertions listed in the part report.
- **Scope points, with recommendations:**
  - (i) Reads are retried with no cap. Ere Maijala's comment (15885278) asks for "at max once a second or so and with a safe request". Recommendation: add a fixed count, then stop and keep the banner up.
  - (ii) Maintainer Jan Høydahl asks (comment 17111893) "Can we please fully get rid of the retry buffer?". Recommendation: keep bounded read replay and answer his question in the PR text with the reason. The `SOLR-9818.patch` attachment was not checked.
  - (iii) The GET and HEAD rule misses state-changing GETs (`Replication.command`, `Logging.setLevel`). Mark those actions non-retryable per action. The owner's call.
  - The schema-designer exemption applies in the generic branch but not in the new message. Recommendation: match it.
  - The changelog title says "replays reads at most once a second" (a count bound that does not exist) and lists POST, PUT, DELETE and PATCH, where the rule is "every method other than GET or HEAD". Suggested: "replays reads once a second until the connection returns" and "any method other than GET or HEAD".
- **Premise run (main-side, not run):** three runs. Run A is base with the branch's test overlaid; it should fail at the first `executeScript` with "isRetryableRequest is not defined". Run B is the head's helper truth table. Run C, which the receipt does not record, is the interceptor premise in a browser: on main, a forced status-0 POST arrives more than once; at the head, one arrival, the not-repeated text, and no replay.

## SOLR-9831: the premise holds on a reading

- **The level cell:** `logging.html` line 36 renders `{{ event.level }} {{event.showTrace}}`, so the cell reads "WARN false" when collapsed and "WARN true" when expanded. The flag is set per row in `logging.js` (line 52, reset on every refresh; line 98, toggled by `toggleRow`). The branch drops the interpolation and the stray closing span. Removing the stray tag has no visible effect, so the test cannot detect it.
- **The trace row:** main line 42 spans four of five columns, and the head spans five. The `tfoot` "No Events available" row (line 47) still spans four at the head. That is one token in the same file, the same defect class, and untested.
- **The Core half:** `Log4j2Watcher.toSolrDocument` copies the MDC and defaults core to an empty string (lines 311 to 318). The branch correctly leaves the Core column alone. The PR body may say so. The title and the changelog must not claim a Core fix.
- **The Assume skip:** the test skips when the shared-JVM log watcher is blind. A skipped run is zero evidence about the cell, and a base run that skips is no fail-before evidence. A passing run shows only the level cell of the probe row, for WARN. It does not show the trace row's colspan, ERROR or other levels, the Core column, or the expanded text.
- **The changelog title** is accurate for the colspan ("all columns"). The first clause is exact only for collapsed rows, because the cell also printed "true" when expanded. Optional precision: "an internal flag value next to each log level".
- **The receipt** says "4 files" but lists three. The fourth, `SOLR-9831-TESTING.md`, is at the repository root, inside the outbound diff. Commit `e4aa23b3e4d` has a body that says "Hypothetical, unrun regression test", which conflicts with the rule that public text carries no internal process vocabulary.
- **Premise run (main-side, not run):** compile the `solr/webapp` test sources first, and run spotless on the changed file. Then run the single method `testEventsViewerShowsWarnings` alone, with Chrome. Base (with the head's test overlaid): expected FAIL at head line 84, with expected "WARN" and actual "WARN false". Head: expected PASS. Record the head SHA, executed or skipped, pass or fail, and the message. A skip is not a result.

## Cross-ticket

- **The opt-out interaction:** the Query resource and the Ping actions are opted out, so their requests never reach the interceptor's status-0 handler. So no request gets both treatments, and no opted-out resource loses reporting from SOLR-9818's change. But opted-out state changes (Ping enable and disable, and `Query.queryPost`) get no not-repeated message on a connection drop, and the Ping toggle shows "lit" after a refused enable until the next refresh. This is an existing defect on main, caused by the opt-out. Owner decision 3 below.
- **SOLR-9759's receipt** does not record that main carries the change. The receipt's "premise unverified" line is settled on a reading: superseded. The lead must reconcile the receipt with main.
- **The shared harness:** all three tests extend `AdminUiTestBase` and need Chrome. The runs can be batched. The order is smallest first: SOLR-9831, then SOLR-9818, then SOLR-9759 (which is a main-side run against main's test file).

## Owner decisions

1. **SOLR-9759:** retire the branch as superseded by `c6f910d3ccf`, and mark its TESTING note superseded (recommended). Whether to chase main's residuals (the error callback that never fires; the empty box on a status 0 or a timeout), as a new ticket.
2. **SOLR-9759:** whether the Stream screen's POST should stay opted out, given that it can carry write operations. At minimum, a status-0 failure for that request should show an unknown-outcome message (recommended).
3. **The opt-out bypass in general:** whether a status-0 message reaches opted-out calls, and whether the Ping toggle should read the server outcome (`core-overview.js` lines 83 and 89).
4. **SOLR-9818, reads:** add a retry count, or keep reads unbounded at one second (recommended: a count).
5. **SOLR-9818, Jan Høydahl's request:** keep bounded read replay and answer in the PR, or drop all replay (recommended: keep and answer).
6. **SOLR-9818, state-changing GET actions:** mark non-retryable per action, now or in a follow-up (recommended: per action).
7. **SOLR-9818, the schema-designer exemption:** match it in the new message (recommended).
8. **SOLR-9818, the PR text:** it must not claim the fix covers collection reload or ADDREPLICA. The v2 connection-loss gap is a separate question.
9. **SOLR-9818, the end-to-end harness:** build it, or accept the helper-only test with its limit stated in the PR.
10. **SOLR-9831, the `TESTING.md` file and the commit body:** exclude the file from the outbound set (recommended), and rewrite commit `e4aa23b3e4d`'s body before any PR.
11. **SOLR-9831, the `tfoot` colspan:** fix it in this branch (one token, recommended), or name it in Limits.
12. **SOLR-9831, the trace-row colspan:** accept a reading-only Limit, or extend the test with a probe that has a throwable and a row click (new test scope).
13. **SOLR-9831, the Assume skip:** accept it and state it in Limits (recommended), or restructure the check.

## Main-side work owed

- The SOLR-9831 premise run, and the SOLR-9818 runs A, B and C. These share one harness run.
- The SOLR-9759 receipt reconciled with main, and a main-side run against main's test file if the owner wants one.
- The SOLR-9818 `SOLR-9818.patch` attachment, which was not in the JSON.
- The round 36 routing record for SOLR-9818, which is not in the repo.
- The generated JavaScript client source, to settle superagent use beyond the UI's own statements.

## Receipt and assignment corrections

- **SOLR-9759 receipt:** the premise line should say the change is on main (`c6f910d3ccf`). Line 8's "base" needs clarifying. The assignment's "the controller passes no error callback" is true only at base.
- **SOLR-9818 receipt:** the diff lists three files, and there are four. Line 8's "base" needs the test overlaid. The assignment's "a collection reload or ADDREPLICA can be issued many times" is false on main.
- **SOLR-9831 receipt:** "4 files" with three listed. The assignment's "changes only `logging.html`" holds only if "changes" means product code.
- **The SOLR-9818 changelog title:** the count bound and the method list (see above).

## Not done

- Nothing has run. No build, Gradle run, test, Selenium or Chrome run, and no compile. No `gh` write call, commit to a submit branch, or live PR edit. No JIRA access.
- The changelog YAML files were read by eye, not parsed by a tool.
- No draft was written, as the assignment requires for this round.
