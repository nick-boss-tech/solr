# Flaky-fix post-PR review, round 1, slice 2b: SOLR-18531

Scope: fork branch `solr-18531-submit` (draft PR #5101 on apache/solr), named head `5ba914ca745933251958412a4ccb226ecbaf0ad8`, base upstream main `3f5d4c5bf8ac`.

Head check: `git ls-remote origin refs/heads/solr-18531-submit` returned `5ba914ca745933251958412a4ccb226ecbaf0ad8`, equal to the named head. A read-only fork fetch brought it to `refs/remotes/origin/solr-18531-submit`. The pr-prepare worktree (tip `a6dc93351da2871a7ad610cdfc779175c903e910`) was clean at the start, so the receipt, gate file, and draft read from the worktree equal the tip. Head code was read with `git show 5ba914...:<path>`. Line numbers cite the head blobs. No build, Gradle, test, Selenium, gate, or reproduction run was made.

## Summary

- Receipt head: the receipt header now names `5ba914...`, which equals the fork tip. Round 1 named `a0150bf...`. The receipt is internally inconsistent (Part A, rows A3 and A8), and the gate file still names `a0150bf...`.
- F1 open, now disclosed in code comments and draft Limits. F4 open, now disclosed; draft Limits line 37 misstates it. F2 open, code unchanged (a reading). F3 open, disclosed. F5 changed: `close()` now releases a closed runner's port. F10 fixed.

## Part A: receipt check

Round 1 facts come from `reports/flaky-fix-review-round-1.md` and the round 1 receipt at commit `4601bcc0dae`. The refresh is commit `50ef5ea2a57`. The receipt file is unchanged on the worktree tip since that commit.

| # | Claim | Receipt says | Head check | Result |
|---|---|---|---|---|
| A1 | Gated head | Line 4: `5ba914ca745933251958412a4ccb226ecbaf0ad8`. Round 1 receipt (`4601bcc0dae`) named `a0150bf71e8e4d630fe55ae04482951a5ca3e179`. | Fork tip is `5ba914`. It is the newest commit on the branch, and `a0150bf` is its parent. | Matches the named head. Changed since round 1 (header line only). |
| A2 | Tree | Not recorded in any version of the receipt. | Tree of `5ba914`: `02de5167b19f304acf6ffe86e16d24f999fef625`. Tree of `a0150bf` (round 1 head, first-gate head in the gate file): `f9f7a3461992bf807aa5404b41ff84195260bbbd`. Base tree: `7429efa50352de0fc980e614199dee8d460a1878`. | Not checkable from the receipt. The diff `a0150bf` to `5ba914` contains exactly the re-gate list in line 11 (close() release, Javadoc, changelog retitle, close step in the test). So the re-gate most likely ran on tree `02de5167`, but the receipt does not say so. |
| A3 | Head that the first-gate numbers belong to | Lines 5-9 are the first gate: "Gate log g18531-gate.log", "Regression runs at the head ... TestPullReplica 39 tests skipped", "Change: ... MiniSolrCloudCluster.shutdown() releases reservations after the stops complete". | `gates/SOLR-18531.md` line 3 still says head `a0150bf...` and status DONE, and it was not refreshed (last changed at `4601bcc0dae`). At `a0150bf` close() did not release the port (the close release is in the `a0150bf` to `5ba914` diff, JSR 955-966). | Inconsistent. The header says `5ba914`, but lines 5-9 describe `a0150bf` and sit under a `5ba914` header. The gate file names `a0150bf`. Main side should reconcile the receipt and the gate file. |
| A4 | Proof leg: new test fails on the reverted framework | Line 7 (first gate) and line 11 (re-gate): TestJettySolrRunner 3 tests, exactly 1 failure on the reverted framework (the reservation assertion). | Head test class has 3 `@Test` methods (line 75 `testLookForBindException`; line 119 `testStoppedRunnerKeepsItsPortUntilRestart`). The reservation assertion is at test lines 141-147, with the failure text "the stopped runner's port should still be reserved". | Consistent with the code. Not re-run. |
| A5 | TestJettySolrRunner 3 of 3 at head | Line 11. | 3 `@Test` methods at head. | Consistent (count only). |
| A6 | LeaderElectionIntegrationTest: 1 pass | Line 8 (1 test passes); line 11 (1 of 1). Round 1 also 1 pass. | One test method, `testSimpleSliceLeaderElection` (line 60 of the test file). | Unchanged. Consistent. |
| A7 | TestCoordinatorRole: 9 pass | Line 8; line 11 (9 of 9). Round 1 also 9 pass. | 9 public `test*` methods (lines 69, 122, 178, 493, 585, 632, 708, 776, 869), no `@Test` annotation. | Unchanged. Consistent. |
| A8 | TestPullReplica | Round 1: "39 tests skipped, 0 failures"; none ran (round 1 O3). Refresh: line 8 still says "39 tests skipped, 0 failures"; line 11 says "39 tests with 1 skipped and 0 failures under tests.nightly=true". | Class is `@Nightly` (line 73). `testCreateDelete` has `@Repeat(iterations = 30)` (line 134). Eight active plain methods, and one `@Ignore` (line 461, `testPullReplicaStates`). 30 + 8 + 1 = 39, so "39 tests, 1 skipped" matches the code. | Changed. Round 1 said none ran. The re-gate says the class ran under nightly with 38 passing. The receipt contradicts itself (line 8 against line 11). |
| A9 | CI seed | Lines 8 and 11: `3E3D9FF553211ED6`. | n/a | Unchanged. |
| A10 | Linux traffic-before-stop variant | Not in round 1. Line 11: "Linux traffic-before-stop variant (two seeds) did not reproduce the TIME_WAIT gap." | The head test does not serve a request before stop (test lines 131-135). The variant is not on the branch. | New since round 1. Cannot be checked here (no code or log on disk). |
| A11 | Re-gate log and time | Line 11: `g18531-regate.log` on vm1, "GATE RUNNER DONE 2026-10-10T19:34:25Z". | The log is on vm1 and not in this workspace. | Not checkable. |

What the receipt establishes at head: the proof counts the draft uses come from line 11 (the re-gate). They match the head test class and the class structure. The re-gate paragraph names no SHA, so the tie to `5ba914` is an inference from the header and from the content of the re-gate list. The proof was not re-run.

## Part B: round 1 blocking items at the head

| Item | State at head | File and line (head `5ba914`) | Verdict |
|---|---|---|---|
| F1: stopped and removed runner is not released at shutdown | Unchanged in code. `shutdown()` releases only the runners in the `jettys` list when shutdown starts. Both `stopJettySolrRunner` overloads still remove the runner from `jettys`. Nothing releases a removed runner unless it is closed or started on its port. The head adds comments and Javadoc that state this gap. | MSCC 514-519 (remove at 517); 528-532 (remove at 530); snapshot at 627; release loop at 644-646; disclosure comment at 639-643. JSR 115-124 (`RESERVED_PORTS` comment) and 723-730 (`releasePortReservation` Javadoc). The head commit body also names the gap. | OPEN, now disclosed. The round 1 fix (keep a list of runners stopped by the cluster and release it at shutdown) is not applied. t4 asked for release of "a cluster's runners"; the head meets that only for runners still listed. Draft Limits line 36 states it correctly. |
| F4: restart on a fresh port leaves the old port reserved | Unchanged. `start(false)` releases `config.port` (usually 0), not the old `jettyPort`. `close()` and `shutdown()` release only the current `jettyPort`. | JSR 512 (port is `config.port` when `reusePort` is false), 517 (releases that port only), 732-733 (public release names the current port), 965 (close releases the current port only). MSCC 489-491 and 503 (`startJettySolrRunner(jetty)` calls `start(false)`). Disclosure at JSR 729-730. | OPEN, now disclosed. Draft Limits line 37 says the old port is held "until the old runner is closed". That is wrong at head: `close()` does not release the old port. The line needs correcting. |
| F2: non-reuse reservation bind may fail on Linux after traffic (TIME_WAIT) | Bind code unchanged from `a0150bf`. It binds `127.0.0.1` with address reuse off, logs a WARN on failure, does not retry, and leaves no reservation. The head adds Javadoc stating that on failure a restart behaves as it did before. | JSR 707-709 (bind); 710-716 (WARN and return with no reservation); 697-700 (Javadoc). The head test serves no request before stop: TestJettySolrRunner 131-135. | OPEN, code unchanged. Reading only: the receipt's Linux traffic variant (two seeds, not on the branch, not checkable here) did not reproduce the gap. That lowers the risk for the cases run. It does not show that the bind cannot fail after a node served traffic. The draft has no Limits line for this, so round 1 O1 is still undecided. |
| F3: proxy port has no reservation; failed rebind is silent | Unchanged. The reservation covers `jettyPort` only. `stop()` closes the proxy before the server stops, and `start()` reopens it. A failed reopen is caught and logged at debug level. `SocketProxy.java` is unchanged from base. | JSR 638-640 (`proxy.close()`), 545-547 (`proxy.reopen()`), 701-720 (reservation keyed on `jettyPort`). `solr/test-framework/src/java/org/apache/solr/util/SocketProxy.java` 204-225, with the catch at 220-224 that logs at debug only. | OPEN, disclosed in draft Limits line 35. Round 1 O2 (reserve the proxy port, or make a failed rebind an error) is not decided. |
| F5: standalone runners hold their port until the JVM exits | Changed. `close()` now calls `releasePortReservation()`, so a closed runner gives its port back. A runner that is stopped and never closed still holds its port until the JVM exits. The head test checks the close release. | JSR 955-966 (close: `stop()` at 957, release at 965). Test 154-166. | CHANGED. Partly fixed: it depends on callers calling `close()`. Whether the 15 test files that construct `JettySolrRunner` call `close()` was not checked. Draft Limits line 38 omits "or closed". |
| F10: changelog title overstates ("can no longer fail with BindException") | Fixed. Head title: "JettySolrRunner in the test framework now holds a stopped runner's port in reserve until the next start on that port". No BindException claim. | `changelog/unreleased/SOLR-18531-jetty-runner-port-reservation.yml` lines 1-3. | FIXED. The title is narrower than the change (it does not mention `close()` or shutdown), but it does not overclaim. |

Other round 1 items at head (not asked, noted for completeness):

- F6 (release happens before `init` and bind; no restore on failure): unchanged. JSR 512-517 and 521-532.
- F7 (second stop logs a false WARN): code unchanged, but `close()` now reaches it whenever the runner was stopped first. `close()` calls `stop()` (957), `stop()` reserves again (684), the bind fails against the existing reservation (709), and the WARN is logged (711-714). The head test's close step will log this WARN. The test still passes.
- F8 (release by port number, not by owner): unchanged. JSR 736-741. The repo's only callers at head are MSCC 645 and JSR 517, 733, 965.
- F11 (test gap): the head adds a close() release check only (test 154-166). No test covers cluster shutdown release, a fresh-port restart, a proxy runner, a runner that served traffic, or a failed start.
- O3 (TestPullReplica not run): see A8. The re-gate line says the class ran under nightly, and the count matches the code. Line 8 still says "skipped" and needs correcting.
- The `RESERVED_PORTS` comment (JSR 115-124) says an entry is released "in exactly three ways". The public `releasePortReservation()` (732) is callable by any caller, so in principle it is a fourth route. In this repo only MSCC and `close()` call it.
- The head commit body (`5ba914`) opens "Review of the first version found ...". That is internal review wording in a public commit message. Main side may want it reworded or squashed (same concern as SOLR-18532 s3b-D3). Noted only.

## Part C: proof-claim check (draft Proof section)

The draft is `pr-drafts/flaky-fixes/SOLR-18531.md`, Proof section at lines 15-27.

| # | Draft Proof claim | Receipt backing | Head check | Result |
|---|---|---|---|---|
| C1 | New test is `TestJettySolrRunner.testStoppedRunnerKeepsItsPortUntilRestart` (line 17) | Line 7 | Method at test line 120 | Backed. |
| C2 | Fails on the reverted framework on the reservation check: a foreign socket binds the released port (line 17) | Line 11: exactly 1 failure on the reverted framework (reservation assertion); line 7 | Assertion at test lines 141-147 | Backed. Not re-run. |
| C3 | At this head TestJettySolrRunner passes 3 of 3 (line 17) | Line 11 | 3 `@Test` methods | Backed. |
| C4 | "including the release on close()" (line 17) | Line 11 names the close() release as part of the change. It is not counted separately. | Close step at test lines 154-166, in the same method | Backed by code. Acceptable. |
| C5 | "Verified 2026-10-10 at 5ba914..." (line 17) | Header line 4 names `5ba914`; line 11 gives the date and no SHA | Fork tip `5ba914` | Backed by the header. The re-gate SHA is not stated, and the gate file still names `a0150bf`. Caveat for the main side (A2, A3). |
| C6 | Restart regression runs at the same head, CI seed `3E3D9FF553211ED6` (line 19) | Line 11 | n/a | Backed. |
| C7 | LeaderElectionIntegrationTest: 1 test passes (line 21) | Line 11 (1 of 1) | 1 test method (line 60) | Backed. |
| C8 | TestCoordinatorRole: 9 tests pass (line 22) | Line 11 (9 of 9) | 9 test methods | Backed. |
| C9 | TestPullReplica: 39 tests, 1 skipped, 0 failures, nightly enabled (line 23) | Line 11 | Count consistent with the class (A8) | Backed by line 11. Line 8 still says 39 skipped; the draft correctly uses line 11. |
| C10 | The class is marked @Nightly, so a plain run skips it (line 23) | Not in the receipt | Line 73 | Backed by code only. |
| C11 | The one skip is an @Ignore'd method (line 23) | Not in the receipt. The receipt does not name the skipped test. | Line 461 (`testPullReplicaStates`, `@Ignore`) | Backed by code only. |
| C12 | On Linux, a variant of the new test that serves a real request before the stop was also run (two seeds) (line 25) | Line 11: "Linux traffic-before-stop variant (two seeds) did not reproduce the TIME_WAIT gap." | Head test has no request before stop (lines 131-135). The variant is not on the branch. | Partly backed. The run and its negative result are in the receipt. The variant itself is not checkable. |
| C13 | "the port stayed reserved, outside binds were refused even with address reuse allowed, and the restart on the same port worked both times" (line 25) | Not in the receipt | n/a | NOT backed. These outcomes are not recorded in the receipt. Remove them, or record them in the receipt first. |
| C14 | "These runs show that the existing restart tests still pass" (line 27) | Line 11: 0 failures in the three restart suites | n/a | Backed. |
| C15 | "They do not show that the flake is gone" (line 27) | Interpretive, no number | n/a | Not a receipt claim. Acceptable. |

Draft text outside the Proof section that the head contradicts or omits:

- Limits line 37 ("until the old runner is closed"): wrong at head (F4; JSR 732-733, 965).
- Limits line 38 (standalone runners "stopped and never restarted ... until the JVM exits"): omits close(). Should read "stopped and never restarted or closed" (F5; JSR 965).
- What this change does, line 13 ("until the node starts on it again or the cluster shuts down"): broader than the head for runners removed by `stopJettySolrRunner` (F1; MSCC 514-532), and it omits close(). Restating with the three release routes would match the head comment at JSR 115-124.
- No Limits line for the reservation bind failing silently (F2; JSR 710-716). Add one, or decide round 1 O1.
- Limits lines 35 and 36 match the head (F3; F1).

## Not checked

- No build, Gradle, test, Selenium, gate, or reproduction run. The reverted-framework proof and the head counts are taken from the receipt and were not re-run.
- The re-gate log (`g18531-regate.log` on vm1) and the first-gate log (`g18531-gate.log`) are not in this workspace. The re-gate SHA and tree are not recorded.
- The Linux traffic variant (receipt line 11) is not on the branch and has no code or log on disk. F2 stays a reading. The kernel TIME_WAIT and bind rules were not checked on a Linux host.
- Whether the 15 test files that construct `JettySolrRunner` directly call `close()` (F5). Not checked.
- The live PR body, title, checks, and reviewer comments (main side, slice 2). Not fetched.
- The draft's "What happens today" (60-second retry, "hit this once in CI on an unrelated pull request") was not re-checked. The CI run is not in the workspace, and the t4 report gives the holder as unknown.
- No commit, push, comment, PR edit, Jira write, or submit-branch edit. The only remote-state action was the read-only fetch of the fork branch.
