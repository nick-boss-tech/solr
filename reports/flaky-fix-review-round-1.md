# Flaky-fix review round 1: verdicts

Assignment: `assignments/pool-flaky-fix-review-round-1.md`. Claim: `claims/pool-flaky-fix-review-round-1.md` (slices 1 and 2, this host). Part reports, one subagent per slice: `reports/flaky-fix-review-round-1-s1.md` (SOLR-18530) and `reports/flaky-fix-review-round-1-s2.md` (SOLR-18531). The lead checked each head against the assignment, read the draft files, and re-read the SOLR-18531 blocking finding F1 against the branch code.

## Verdicts

| Ticket | Head reviewed | Gate (receipt) | Verdict | Draft |
|---|---|---|---|---|
| SOLR-18530 | `98e5368d996518b9f4a94f85d7c2fcd933d3f485` | GATE GREEN | Ready for draft. Hold the opening for owner decisions D1 to D4. | `pr-drafts/flaky-fixes/SOLR-18530.md` |
| SOLR-18531 | `a0150bf71e8e4d630fe55ae04482951a5ca3e179` | GATE GREEN | HOLD. Three items must clear before opening (F1, F2, F4). | `pr-drafts/flaky-fixes/SOLR-18531.md` (hold draft, not for posting) |
| SOLR-18532 | `07a7ead478337498f2dd8bce08507c683e61517f` | QUEUED on vm1, no receipt | Not reviewed. Held until `receipts/SOLR-18532.md` records GATE GREEN at this head. | none |

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

Not reviewed in this round. Its gate is still queued on vm1 (`gates/SOLR-18532.md`, no receipt). Slice 3 is held and is not claimed. A later round claims it once `receipts/SOLR-18532.md` records GATE GREEN at `07a7ead478337498f2dd8bce08507c683e61517f`, and checks that head again before reviewing.

## Not done

No build, Gradle, test, Selenium or gate run. No PR, comment, Jira write, submit-branch edit or live PR description edit. No ticket text was available in the workspace, so both verdicts rest on the assignment and the root-cause reports.

The claim for slices 1 and 2 is marked DONE in the same push as these deliverables. The verdicts are review results. They are not approval to open either PR.
