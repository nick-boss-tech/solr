# Flaky-fix post-PR review round 4, slice 2: SOLR-18531 (PR #5101)

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-4.md`, slice 2. Head checked: `351914f52c99180f0582d45c5bea1bd800194d29`.

Scope: read-only. Git reads on the worktree and `origin/pr-prepare` (tip `835cba367157fec5536b92cabab7111cc3ab17ad`). One read-only fetch of the fork branch (`refs/remotes/origin/solr-18531-submit` moved from `5ba914ca` to `351914f5`). Live PR read through `research/gh.ps1` (`pr view`, and GET calls for reviews, issue comments, review comments, check suites, check runs, workflow runs, and one check-run annotation list). No build, Gradle, test, Selenium, gate, or test-queue run. No PR body edit, comment, review, close, submit-branch edit, or Jira write.

Line numbers for the body ("body line N") count the CR-stripped live body from the top, with the AI header as line 1.

## Verdict

**STILL OPEN.** The head, the round 1 D and R items, the proof numbers, the citations, the changelog file, and the Limits reservation rules all check out at `351914f`. Three items remain:

1. Body line 23 carries a CI seed (`CI seed 3E3D9FF553211ED6`). Check 8 lists seeds as internal vocabulary. Fix: delete the seed and keep "Restart regression runs at the same head:".
2. No section opens with a bold one-line summary. The body has zero bold markers. The five sections (lines 5, 9, 17, 31, 35) open with plain text. The presentation rule in `pr-formula.md` requires the bold line.
3. Minor, Limits accuracy: body lines 38 and 40 say a runner keeps its port "until the JVM exits". `start()` releases a reservation by port number for any runner that starts on that port (`JettySolrRunner.java` 515-517). Fix: "until a runner starts on that port or the JVM exits" in both bullets.

CI: the labeler check succeeded. Four upstream workflows are `action_required` and have not run at this head. No reviews, no issue comments, no review comments.

## Item table

| Item | State at head | State in live body | Result |
|---|---|---|---|
| 1. Head | Fork tip and gated head are `351914f52c99180f0582d45c5bea1bd800194d29` (`ls-remote`, and the fork fetch). Receipt line 4 names this head. | Live `headRefOid` is `351914f52c99180f0582d45c5bea1bd800194d29`. `isDraft` true, state OPEN. | SATISFIED |
| 2. D1, Limits bullet on a fresh-port restart | `close()` releases only the current `jettyPort` (`JettySolrRunner.java` 737-738, 960-970). `start(false)` releases `config.port` (512, 517), and `jettyPort` moves to the new port (335), so the old port stays held. | Body line 39: "A restart on a fresh port leaves the old port reserved: closing the runner releases only the port it currently holds, so the old port stays reserved until a later start on that port or the JVM exits." | SATISFIED |
| 3. D2, behavior sentence | `close()` is a third release route (960-970). Shutdown releases only the snapshot taken at 627, which excludes runners removed by `stopJettySolrRunner` (`MiniSolrCloudCluster.java` 514-517, 528-531, 627, 644-646). | Body line 15: "Behavior changes: a stopped runner keeps its port bound, so nothing else can bind that port until the node starts on it again, the runner is closed (a runner's `close()` releases its port), or, for runners still in the cluster's list, the cluster shuts down." | SATISFIED (edge in row 19) |
| 4. D3, standalone bullet | `close()` releases the port (960-970). A runner never closed or restarted keeps it, except that a later start on that port releases it (517). | Body line 40: "Standalone runners that are stopped and never restarted or closed also keep their port until the JVM exits." | SATISFIED (edge in row 19) |
| 5. D4, Linux variant sentence | The Linux variant is not a separate test. The committed served-traffic test (`TestJettySolrRunner.java` 177-232) replaces it. Receipt line 7 (test), line 8 (reverted-framework outcome), line 10 (correction). | Body line 19 describes the committed test. The Linux outcome sentence is gone. The only outcome numbers are the reverted-framework counts in body line 21. | SATISFIED |
| 6. R1, receipt TestPullReplica wording | Receipt line 9: "TestPullReplica 39 tests with 1 skipped (an @Ignore method) and 0 failures under tests.nightly=true". The "39 tests skipped" text on receipt line 13 is labelled as the first gate's history. | Body line 27 matches receipt line 9. | SATISFIED |
| 7. R2, traceability of the Linux detail | No Linux variant claim is left in the body. The committed test's outcome is in receipt lines 8 and 10. The gate log is named on receipt line 5 (`g18531-regate2.log`). | Body lines 19-21 cite only the committed test and the receipt counts. | SATISFIED |
| 8. O1, TIME_WAIT on Linux (F2) | Reservation bind uses address reuse (`JettySolrRunner.java` 713). No retry is needed for the cited gap. The committed test covers the served-traffic case (see the Windows note in the notes section). | Body line 13 gives address reuse as the reason for the bind. The round 1 F2 residual Limits item is gone. | SATISFIED (Linux; see note N1) |
| 9. O2, proxy port | No reservation for the proxy port. The reserve uses `jettyPort` only (`JettySolrRunner.java` 713-714). | Body line 37 discloses it as a limit. | SATISFIED (disclosed) |
| 10. O3, TestPullReplica skipped | Class is `@Nightly` (`TestPullReplica.java` line 73). One `@Ignore` at line 461. | Body line 27: 39 tests, 1 skipped, 0 failures, nightly enabled, reason given. | SATISFIED |
| 11. O4, standalone runners | `close()` releases (960-970). | Body line 40 (narrowed wording, see row 19). | SATISFIED (see row 19) |
| 12. O5, route choice | Not code. | Body lines 31-33: two routes, the one implemented, and a closing question. | SATISFIED as content. The bold summary is missing (row 17). |
| 13. F1, removed runners not released at shutdown (round 1) | Still true at head. Documented in the cluster comment (`MiniSolrCloudCluster.java` 641-643) and the `releasePortReservation()` Javadoc (`JettySolrRunner.java` 732-735). | Body line 38 discloses it. | SATISFIED (disclosed; see row 19) |
| 14. F4, fresh-port old port (round 1) | Still true at head. Documented in the Javadoc (`JettySolrRunner.java` 733-735). | Body line 39 gives the right rule. | SATISFIED |
| 15. Title | Live title: "SOLR-18531: Reserve a stopped test runner's port until its next start". `close()` and shutdown also release earlier, so the title is a partial summary, not an overclaim. | Live title matches the head. | SATISFIED (optional: name the other release routes) |
| 16. Changelog file | `changelog/unreleased/SOLR-18531-jetty-runner-port-reservation.yml` exists at head. Its title no longer claims the BindException fix. | Body line 42 names the file. It is a plain code span, as in the `pr-formula.md` template. | SATISFIED |
| 17. Bold one-line summary per section (check 8) | Not code. | Zero bold markers. Sections at lines 5, 9, 17, 31, 35 open with plain text. | STILL OPEN (fix 2) |
| 18. Internal vocabulary (check 8) | Not code. | Line 23: "CI seed 3E3D9FF553211ED6" is a seed. No gate, receipt, ledger, log name, run identifier, claim, or takeover found. "Verified 2026-10-10 at <head>" (line 21) is the allowed verification date and head. | STILL OPEN (fix 1) |
| 19. Limits accuracy, "until the JVM exits" (minor) | `start(boolean)` releases the reservation for its port, whichever runner held it (`JettySolrRunner.java` 515-517; the comment says "or another runner was stopped on the same port"). | Body lines 38 and 40 say "until the JVM exits" with no exception for a later start on that port. Line 15 has the same gap. | STILL OPEN (minor, fix 3) |

## Limits check

| Statement (body line) | Code at head | Result |
|---|---|---|
| L1, line 37: runners with a proxy reserve only the Jetty port; the proxy closes on stop and rebinds on start with no reservation; a failed rebind is logged at debug and not raised. | Reserve uses `jettyPort` only (`JettySolrRunner.java` 701-714). Proxy closed in `stop()` (638-639) and reopened in `start()` (545-547). `SocketProxy.reopen()` catches the failure and logs at debug (`solr/test-framework/src/java/org/apache/solr/util/SocketProxy.java` 204-225). The proxy runner constructor is used in `AbstractFullDistribZkTestBase.java` 849. | TRUE |
| L2, line 38: a runner stopped by `stopJettySolrRunner` and never restarted is removed from the list, so shutdown does not release it; closing releases it; otherwise it keeps its port until the JVM exits. | Removal at `MiniSolrCloudCluster.java` 517 and 530. Snapshot at 627 excludes it. Release loop 644-646 covers the snapshot only. `close()` releases (`JettySolrRunner.java` 960-970). A later start on the port releases it (517). | TRUE, except the later-start edge (row 19) |
| L3, line 39: fresh-port restart leaves the old port reserved until a later start on that port or the JVM exits. | `start(false)` uses `config.port` (512). Release at 517 names that port. `jettyPort` moves to the new port (335). Close and shutdown release only `jettyPort` (737-738, 960-970; MSCC 644-646). | TRUE |
| L4, line 40: standalone runners stopped and never restarted or closed keep their port until the JVM exits. | `close()` releases (960-970). No other release except a start on the same port (517). | TRUE, except the later-start edge (row 19) |
| Line 15: `close()` releases the runner's port; cluster shutdown releases runners still in its list. | `close()` 960-970. Shutdown release 644-646 for the snapshot at 627. | TRUE |
| Line 11: shutdown closes the sockets for runners still in its list after the stops finish. | Stops at 635, release at 644-646 (after `checkForExceptions` at 637-638, before the rethrow at 647-649). | TRUE |
| Body has no TIME_WAIT Limits item. | No TIME_WAIT limit in the Limits section (lines 35-40). | TRUE (round 1 F2 residual gone) |

## Proof check

| Claim (body line) | Source at head or on the tip | Result |
|---|---|---|
| Test `testStoppedRunnerKeepsItsPortUntilRestart` (line 19) | Declared at `TestJettySolrRunner.java` 122. Receipt line 8 names it. | TRUE |
| Test `testStoppedRunnerThatServedTrafficKeepsItsPortUntilRestart` (line 19) | Declared at line 177. Receipt line 7 names it. | TRUE |
| "4 tests run, 2 failures" on the reverted framework (line 21) | The class has 4 `@Test` methods (lines 38, 76, 121, 176). Receipt line 8: "runs 4 tests with exactly 2 failures". Not re-run. | TRUE (receipt-sourced) |
| "TestJettySolrRunner passes 4 of 4" at this head (line 21) | Receipt line 8: "At the head the same class passes 4 of 4." | TRUE (receipt-sourced) |
| "Verified 2026-10-10 at 351914f..." (line 21) | Receipt line 3 (re-gate 2 finished 2026-10-10), line 4 (gated head), line 5 (runner done 2026-10-10T20:48:19Z). | TRUE |
| LeaderElectionIntegrationTest 1 test, 0 failures (line 25) | Receipt line 9. | TRUE |
| TestCoordinatorRole 9 tests, 0 failures (line 26) | Receipt line 9. | TRUE |
| TestPullReplica 39 tests, 1 skipped, 0 failures, nightly on (line 27) | Receipt line 9. `@Nightly` at `TestPullReplica.java` 73; `@Ignore` at 461; `@Repeat` at 134. | TRUE |
| Seed (line 23) | Receipt line 9 has the same seed. The value is correct; the item is internal vocabulary (row 18). | Value TRUE; STILL OPEN for check 8 |

The body has no numbers that trace to an earlier head. The first-gate and first re-gate numbers in the receipt's history (line 13) are not used.

## Citation check

| Link (body line) | Target at head | Result |
|---|---|---|
| `MiniSolrCloudCluster.java#L639-L646` (line 11) | Lines 639-643 are the release comment. Lines 644-646 are the loop calling `releasePortReservation()`. | TRUE |
| `JettySolrRunner.java#L709-L714` (line 13) | Lines 709-712 are the comment. 713 is `setReuseAddress(true)`. 714 is the bind on `127.0.0.1`, `jettyPort`. | TRUE |
| `TestJettySolrRunner.java#L122` (line 19) | Declaration of `testStoppedRunnerKeepsItsPortUntilRestart`. | TRUE |
| `TestJettySolrRunner.java#L177` (line 19) | Declaration of `testStoppedRunnerThatServedTrafficKeepsItsPortUntilRestart`. | TRUE |

All four links use `blob/351914f52c99180f0582d45c5bea1bd800194d29`. The changelog reference (line 42) is a code span, not a link, as the `pr-formula.md` template has it. The other code spans are symbols (`JettySolrRunner`, `close()`, `stopJettySolrRunner`), not file citations.

## Body vs draft

`pr-drafts/flaky-fixes/SOLR-18531.md` on `origin/pr-prepare` (tip `835cba36`) has no CR bytes. After CR stripping, the live body and the draft match with no content difference. My first capture added one trailing newline to the live file. That was my capture artifact, not a body difference. The draft has no `[HOLD]` tags.

## Title and changelog

The title is accurate for the main change. It names the next start. The body also shows `close()` and cluster shutdown as release routes, so the title is incomplete but not an overclaim. The changelog file exists at the head.

## CI and review state

| Check | Trigger | State at head |
|---|---|---|
| Pull Request Labeler (job `labeler`, check run 114307788368, workflow run 38084417194) | pull_request_target | completed, SUCCESS. One notice annotation: the `ubuntu-latest` label migrates to Ubuntu 26 on 2026-10-19. This concerns the runner, not the change. |
| Gradle Precommit (run 38084418601) | pull_request | action_required, no jobs run |
| Admin UI Tests (run 38084418678) | pull_request | action_required, no jobs run |
| Validate Changelog (run 38084418565) | pull_request | action_required, no jobs run |
| Solr Tests via Crave (run 38084418736) | pull_request | action_required, no jobs run |

`statusCheckRollup` lists only the labeler. `mergeStateStatus` UNSTABLE. `reviewDecision` empty. Reviews, issue comments, and review comments are all empty.

`action_required` means maintainer approval is pending and the workflows have not run. Under `AGENTS.md` it is incomplete validation, so the upstream checks are not yet evidence.

## Verified and rejected automated findings

None. No reviewer or bot comment exists on PR 5101 at this head. The one automated item, the labeler notice, was read in full and is rejected: it is a GitHub runner-image notice, not a finding about this change.

## Notes outside the verdict

N1. Windows, not verified. The head comment (`JettySolrRunner.java` 711-712), body line 13, and the reuse=true probe (`TestJettySolrRunner.java` 209-217; failure message at 213) all say the held socket refuses every later bind, with or without reuse. As I understand Windows socket semantics (not checked here), `SO_REUSEADDR` on Windows can let a second socket bind a port already held by a reuse-enabled socket. If so, the reuse=true probe would bind on Windows and the served-traffic test would fail there. The gate host OS is not stated in the receipt, and I had no Windows run. Suggested check when a verify run is authorized: run `TestJettySolrRunner` on Windows at this head. If it fails at line 213, the fix is a platform qualifier in the body and comment, or a guard in the test. This is the lead's call; it is not counted in the verdict.

N2. Minor code note, verified by reading, not in the body. `reserveJettyPort()` has no guard for a port the runner already holds (`JettySolrRunner.java` 701-704). A second `stop()` (for example `close()` after `stop()`, or a direct stop followed by cluster shutdown) tries to bind again, fails against the held listening socket, and logs "Could not reserve port ... a restart on this port is not protected" (716-719). The port is protected, so the warning is false. The gate test `testStoppedRunnerKeepsItsPortUntilRestart` takes this path at its `close()` call (line 161). This is the round 1 F7 item and is still open. It is not in the D or R list.

N3. `gates/SOLR-18531.md` on `origin/pr-prepare` still names `5ba914ca745933251958412a4ccb226ecbaf0ad8` as its head. The receipt names `351914f`. The main side should update the gate note if it is read as the head record. The body does not depend on it.

N4. The body is 5,182 characters. The `pr-formula.md` guide is about 3,500 unless the ticket is complex. This is not an assignment check.

N5. The assignment points D1 to D4 and R1 to R2 at `reports/flaky-fix-review-round-1-s2.md`. Those items are defined in `reports/flaky-fix-post-pr-review-round-1-s2a.md` section 6. `reports/flaky-fix-review-round-1-s2.md` holds the F and O items, which this report also covers (rows 8 to 14).

## Not checked

- No build, Gradle, test, Selenium, gate, or reproduction run. Pass counts are receipt-sourced; only the test names, the `@Test` count, and the source markers were checked against the code.
- CI logs were not read. The statement in body line 7 that `LeaderElectionIntegrationTest.testSimpleSliceLeaderElection` "hit this once in CI" is not in any file I read.
- The claim in the choice section (body line 33) that the node-name comparison is in the affected test was not checked.
