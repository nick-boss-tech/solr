# Flaky-fix review round 1: verdicts

Assignment: `assignments/pool-flaky-fix-review-round-1.md`. Claims: `claims/pool-flaky-fix-review-round-1.md` (slices 1 and 2) and `claims/pool-flaky-fix-review-round-1-slice-3.md` (slice 3), both this host. Part reports, one subagent per slice: `reports/flaky-fix-review-round-1-s1.md` (SOLR-18530) and `reports/flaky-fix-review-round-1-s2.md` (SOLR-18531). The lead checked each head against the assignment, read the draft files, and re-read the SOLR-18531 blocking finding F1 against the branch code.

## Verdicts

| Ticket | Head reviewed | Gate (receipt) | Verdict | Draft |
|---|---|---|---|---|
| SOLR-18530 | `98e5368d996518b9f4a94f85d7c2fcd933d3f485` | GATE GREEN | Ready for draft. Hold the opening for owner decisions D1 to D4. | `pr-drafts/flaky-fixes/SOLR-18530.md` |
| SOLR-18531 | `a0150bf71e8e4d630fe55ae04482951a5ca3e179` | GATE GREEN | HOLD. Three items must clear before opening (F1, F2, F4). | `pr-drafts/flaky-fixes/SOLR-18531.md` (hold draft, not for posting) |
| SOLR-18532 | `348dd63d85a563c77e0a20b5742b2a0c845dc191` | GATE GREEN at the corrected head | Ready for draft with conditions. Draft held for two owner decisions (overlap with SOLR-9865; framing against the flake). | `pr-drafts/flaky-fixes/SOLR-18532.md` (hold draft, not for posting) |

## SOLR-18530

The change is test-only. In `RecoveryAfterSoftCommitTest`, adds after the proxy cut go through a leaders-only client. It matches the primary fix in the t2 report. The mechanism is not reproduced: the fixed test passed 6 of 6 runs, and the unmodified test passed 5 of 5 at the CI seed. The draft claims no before-and-after result.

The code references in the part report (the leaders-only path, the pooled connection, and the LBSolrClient failover rule) are the subagent's. This host did not re-read them.

Owner decisions, not taken:

- **D1. Changelog.** The title describes test internals, and `dev-docs/changelog.adoc` section 4 exempts test-only changes. Retitle it to a user-facing line, or drop the entry.
- **D2. More evidence before opening.** t2 step 0 (compare the CI failing port with the cut proxy port; needs the CI log, which is not in the workspace) and t2 step 1 (20 base runs). Both need an approved verify run.
- **D3. Helper routing.** The test helper randomizes `directUpdatesToLeadersOnly`. Keep it (tested 6 of 6), or set it explicitly. The explicit option needs a new gate run.
- **D4. Choice section.** Keep or drop "A choice to check" (leaders-only client versus retry in the test).
- **D5. Opening.** The PR opening and Nick's approval stay with the main agent.

## SOLR-18531

The change is in the test framework. A stopped runner keeps a socket reservation on its port until the next start on that port, or until cluster shutdown. The core behavior matches t4 Fix A. The deterministic proof is green: the new test fails on the reverted framework, and `TestJettySolrRunner` passes 3 of 3 at the head.

Blocking before opening:

- **F1 (verified by this host).** `stopJettySolrRunner` removes a runner from the cluster's list. `shutdown()` releases reservations only for runners still in that list (`MiniSolrCloudCluster.java` lines 514-532, 627, 642-644). A runner stopped and left down keeps its port bound until the JVM exits. `JettySolrRunner.close()` calls `stop()`, which reserves the port again, so it does not release either. The commit message and the Javadoc overstate the release.
- **F2 (not verified).** On Linux, a non-reuse reservation bind may fail after a node served traffic (TIME_WAIT). No Linux host was used, and no test run is allowed here. The new test stops a runner that served no requests, so it cannot show this. Verification: a variant that serves a request before stop, or a CI log check for "Could not reserve port".
- **F4 (verified from code).** `startJettySolrRunner(jetty)` calls `start(false)`, which binds `config.port` and releases only that port. A restart on a fresh port leaves the old port reserved until the JVM exits.

Disclose or fix before opening:

- **F3.** The proxy port has no reservation. A failed proxy rebind is logged at debug level and not raised.
- **F5.** Standalone runners hold their port until the JVM exits.
- **F10.** The changelog title says a restart "can no longer fail with BindException". That is not established yet.
- **O3.** The receipt records `TestPullReplica` as 39 tests skipped, 0 failures. None of them ran, so the receipt does not show that the class works. The draft states them as skipped.
- Minor: F6, F7, F8 (see the part report).

Owner decisions, not taken:

- **O1.** The response to F2, after a Linux check: retry the bind for a bounded time, hold a listening socket, or accept the gap and state it.
- **O2.** The proxy port: reserve it too and make a failed rebind an error, or state it as a limit.
- **O3.** Run `TestPullReplica` where its tests execute (needs an approved verify run), or keep the disclosure.
- **O4.** Standalone runners: release in `close()` after stop, or accept holds until the JVM exits.
- **O5.** Restart route: keep the same port with a reservation, or move the one affected test to a fresh port.

## SOLR-18532

Ready for a draft with conditions. The draft is held until two owner decisions are taken. Claim: `claims/pool-flaky-fix-review-round-1-slice-3.md`. Part reports: `reports/flaky-fix-review-round-1-s3a.md` (production change) and `reports/flaky-fix-review-round-1-s3b.md` (test, history and proof wording). The head was checked against the fork's tip before any work.

The change is a product fix in `RestoreCore`, not a fix for the flake. On a failed restore or install, the rollback writes back the core's previous `index.properties` bytes. It deletes the file only when the core had none before. The root-cause report (`reports/flaky-tests-root-cause-round-1.md` line 34) and t1 (line 95) both name this a separate product defect, not the flake's fix, so the draft must not say the flake is fixed. The lead re-read both framing lines and the rollback block at the head. They match.

Owner decisions, not taken:

- **D-a. Overlap with SOLR-9865 (verified).** The SOLR-9865 branch head `4937608bb181` is gate green. Its commit `cd46e4a3521` changes the same rollback lines in `RestoreCore.java`, but it writes the previous directory name back instead of the bytes. The two cannot both land as written. Nick decides which branch carries the change, or whether to sequence them (s3b-D1).
- **D-b. Framing.** Ship as a standalone product fix with no flake claim, or wait (s3a-D1, s3b-D2). The ticket text is not in the workspace, so the framing could not be checked against it.

Hardening and disclosure (owner decisions, not taken; a code or test change needs a new head and a new gate):

- **s3a-F1. Guard the restore.** If writing the bytes back throws, the rest of the rollback is skipped. The writer is not reopened and the restore directory is not removed. The base code has the same exposure around its delete. Fix: s3a-D2.
- **s3a-F2. Temp file leftover.** A failed write leaves `index.properties.<nanoTime>` in the data directory. It is inherited from `SolrCore.writeNewIndexProps`.
- **s3a-F3. Non-atomic fallback.** On a factory that deletes and then renames, a failure between the two can leave no pointer. Only the in-memory factories use that fallback. The draft names it in Limits.
- **s3a-F4 and s3a-D5. Read errors now abort.** A read error other than file-not-found stops the restore before the switch. The base read ignored it. The new behavior is safer, but the changelog does not mention it. Recommendation: keep it and state it. The draft does, behind a HOLD.
- **s3a-F5 and s3a-D3. Failure wait.** The new test's failure wait is a short poll loop copied from `testFailedRestore`. A slow failure makes the test fail rather than pass. Widening it is a test-only change and needs a new gate.
- **s3a-F6. In-memory factory only.** The proof does not run the atomic move path. The draft names it in Limits.
- **s3b-D3 and s3a-D4. Commit history.** Commit `348dd63d85a` narrates the failed first gate and uses internal words. Squash or reword before any PR (Nick decides).
- **s3b-D4. Test count.** The receipt's "4 tests" comes from JUnit XML that is not on disk. The file has three `@Test` annotations and four `test*` methods. Confirm before the draft says "4 of 4". The draft has a HOLD.
- **s3b-D5. Gate record.** `gates/SOLR-18532.md` still names `07a7ead` on line 3, and it has no record of the failed first gate. The main side should correct it. This review did not edit it.
- **s3b-D6. Coverage.** No assertion that the file is absent when the core had no earlier file. Add it, or keep the Limits line. The draft has a HOLD.
- **s3b-D7. Changelog wording.** "instead of deleting it" reads as never deleting. Optional rewording.

The receipt's "final assertion" is inaccurate: the pointer check comes before the document check. The draft says "the pointer check".

## Not done

No build, Gradle, test, Selenium or gate run. No PR, comment, Jira write, submit-branch edit or live PR description edit. No ticket text was available in the workspace, so both verdicts rest on the assignment and the root-cause reports.

The claims for slices 1 and 2 and for slice 3 are marked DONE in the same push as these deliverables. The verdicts are review results. None of them is approval to open a PR. Openings and the owner decisions above stay with Nick and the main agent.

## Slice 4: SOLR-16630 (fork branch solr-16630-submit)

Added to the assignment by the main side after the round 1 verdicts. Head `9bea59741ac30ffd11ed11be5269ea025cf17f68`, which equals the fork tip and the head in `receipts/SOLR-16630.md` (GATE GREEN, run on vm2). The change is test-only: a changelog entry and `TestCoordinatorRole.java` (+6 lines). Part reports: `reports/flaky-fix-review-round-1-s4a.md` (code audit) and `reports/flaky-fix-review-round-1-s4b.md` (receipt check and draft). Draft: `pr-drafts/flaky-fixes/SOLR-16630.md`.

**Verdict: ready for draft. The proof claim is held until owner decision O1 is taken.**

- The change matches the primary fix in the t3 report. The test's fixed-delay stop of the PULL node no longer runs before the add loop's first successful add. The lead checked the wait at the head: `TestCoordinatorRole.java` line 262, `addDone.await(2, TimeUnit.MINUTES)`.
- No hang. If no add succeeds within two minutes, the test logs a warning and stops the node anyway. The failure then comes up to two minutes later than before. The draft states this as a behavior change.
- Evidence gap: no run of this test on the base code is on record. The test has not been shown to fail without the change. The draft says so plainly. The receipt's six focused runs (CI seed `681E2A715B2CE1D3` plus five random seeds, 1 test and 0 failures each) are the only proof numbers.
- Not addressed: the stale pooled connection from the test client to the PULL node (H2 in t3), and the add loop's `SolrException`-only catch.

Owner decisions and main-side items, not taken:

- **O1.** Run `testNRTRestart` on the base code on vm2 with seed `681E2A715B2CE1D3`, which is a verify run this review does not do. Or publish with the "not shown to fail" wording the draft already has (s4a).
- **O2.** Check the six vm2 logs for the order of events and for the absence of the fallback warning (s4a).
- **Changelog title.** The title says the test "no longer stops the PULL node while an add through it is still in flight". Only the first add is waited for, so later adds can still be in flight. Fixing it needs a submit-branch edit, which moves the head. Main side (s4b item 3).
- **"Likely cause" wording.** Keep "the likely cause ... not proven" in What happens today, or move it to Limits (s4b item 2).
- **Choice section.** The draft has none. The catch-widening trade-off is in Limits. Owner may want it as a choice (s4b item 4).
- **Assignment text.** The slice 4 text names a coordinator-endpoint race, which conflicts with the t3 mechanism (a fixed-delay stop of the PULL node). The draft follows t3. Main side should correct the assignment text (s4a O8, s4b item 5).
- **Unverified in the draft.** "Reopened" status against Jira (s4b item 7). The failing CI run `38009158573` is not linked (s4b item 6). The two-minute constant is from the code, not the receipt (s4b item 8).
