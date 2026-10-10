# Flaky-fix post-PR review round 1, slice 2a: SOLR-18531 (PR #5101)

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-1.md`, slice 2. Claim: `claims/pool-flaky-fix-post-pr-review-round-1-slice-2.md` (not marked here; the claim split into s2a and s2b, and this report covers both parts as the brief asked).

Scope: read-only. Live reads through `research/gh.ps1` (`pr view`, `pr checks`, and GET calls for reviews, comments, check suites, check runs, workflow runs and annotations). Git read-only. No build, Gradle, test, Selenium or gate run. No PR body edit, comment, review, close, submit-branch edit or Jira write. No fetch was needed: the head commit is already in the local object store, and `origin/solr-18531-submit` resolves to it.

## Verdict

DRIFT. The live body is byte-identical to `pr-drafts/flaky-fixes/SOLR-18531.md` after CR stripping, so body and draft agree. Checked against the head code and the receipt, the shared text has four exact drifts (D1 to D4, section 6), and round 1 item F2 is still open. F4 is disclosed in Limits, but the Limits give the wrong release rule. CI: only the labeler check has run (success). Four upstream workflows are `action_required` and have not run. No reviews or comments.

## 1. Head check

- `git ls-remote origin refs/heads/solr-18531-submit`: `5ba914ca745933251958412a4ccb226ecbaf0ad8`.
- Live `headRefOid` (PR #5101): `5ba914ca745933251958412a4ccb226ecbaf0ad8`.
- Receipt header gated head: `5ba914ca745933251958412a4ccb226ecbaf0ad8`.
- Result: equal. No stop.
- Branch shape, off base `3f5d4c5bf8ac`: two commits. `a0150bf71e8` is the round 1 gated head ("Hold a stopped JettySolrRunner port until the next start on it"). `5ba914ca745` ("Release the port reservation when a runner is closed, and correct the release rules") changes `close()`, the release Javadoc, the cluster shutdown comment, the changelog title and the test. Four files, +153 and -2 in total.
- The worktree is checked out on the pool branch (`a6dc933`), not on the fork branch. Nothing was switched or edited.

## 2. Title

Live title: `SOLR-18531: Reserve a stopped test runner's port until its next start`.

- Round 1 F10 overclaim ("can no longer fail with BindException"): not repeated. The title does not claim that.
- Accuracy: the title names one release route, the next start. At the head the port is also released by `close()` and by `MiniSolrCloudCluster.shutdown()` for runners still in the cluster. Read literally, "until its next start" is not the full rule. This is an incomplete summary, not the F10 kind of overclaim. Optional wording point for the main side.
- The head changelog title (`changelog/unreleased/SOLR-18531-jetty-runner-port-reservation.yml`) is now: "JettySolrRunner in the test framework now holds a stopped runner's port in reserve until the next start on that port". It drops the F10 claim. It has the same single-route wording.

## 3. Body vs draft

Method: live body from `gh pr view --json body`, CR removed, compared with the draft with CR removed. Both are 3806 bytes. `diff` reports no difference. Header, Jira link, all five sections, the Limits bullets, changelog line and footer match.

- The draft file uses CRLF (44 CR bytes). The live body uses LF. Line endings only, no content difference.
- The draft has no `[HOLD]` tags.
- Formula points that apply to both the body and the draft (not drift; for the main side):
  - No file citations are links. The only link is the Jira link. File names are plain code spans, not blob links at the head SHA (pr-formula.md, presentation rule).
  - No section opens with a bold one-line summary (pr-formula.md, presentation rule).
  - The body is about 3,806 characters against the roughly 3,500 guide.

## 4. Citations

- Links in the live body: one, `https://issues.apache.org/jira/browse/SOLR-18531`, which matches the ticket. No GitHub blob links, so there is no link to point at the head.
- Code claims in the body, checked against `git show 5ba914ca:<path>` (line numbers at the head):

| Body claim | Head location | Result |
|---|---|---|
| A start on the port releases the reservation before the bind | `JettySolrRunner.java` 517 (`releasePortReservation(port)` in `start(boolean)`) | Confirmed |
| Stop holds the port with a socket | `JettySolrRunner.java` 684 (`reserveJettyPort()` after the server join loop); 701 (method) | Confirmed |
| Reservations live in a static map | `JettySolrRunner.java` 125 (`RESERVED_PORTS`, static) | Confirmed |
| `close()` releases its own socket | `JettySolrRunner.java` 955 to 965 (`releasePortReservation()` at 965) | Confirmed |
| Shutdown releases runners still listed | `MiniSolrCloudCluster.java` 627 (snapshot), 644 to 645 (release loop) | Confirmed |
| `stopJettySolrRunner` removes a runner from the list | `MiniSolrCloudCluster.java` 528 to 530 (`jetty.stop()`, `jettys.remove`) | Confirmed; F1 applies |
| Retry tries only the same port for 60 seconds | `JettySolrRunner.java` 578 to 600 (`retryOnPortBindFailure(..., port)`); `JettyConfig.java` 83 (`portRetryTime = 60`) | Confirmed |
| Restart on a fresh port through the cluster | `MiniSolrCloudCluster.java` 489 to 490 (`startJettySolrRunner(jetty)`), 501 to 503 (`jetty.start(reusePort)` with `reusePort=false`) | Confirmed; F4 applies |
| `LeaderElectionIntegrationTest.testSimpleSliceLeaderElection` and its node-name checks | `LeaderElectionIntegrationTest.java` 60 (test), 83 to 147 (node-name comparisons) | Confirmed (choice section) |
| New test `testStoppedRunnerKeepsItsPortUntilRestart` | `TestJettySolrRunner.java` 120; the class has 3 `@Test` methods | Confirmed |
| `TestPullReplica` is `@Nightly`; one `@Ignore`d method | `TestPullReplica.java` 73 (`@Nightly`), 461 (`@Ignore`) | Confirmed |
| `TestPullReplica` count 39 with 1 skipped | `testCreateDelete` 134 has `@Repeat(iterations = 30)`; 30 + 8 non-ignored + 1 ignored = 39 | Confirmed from source; matches the re-gate receipt line |
| Proxy rebind failure logged at debug, not raised | `SocketProxy.java` 218 (bind), 220 (catch), 222 (`log.debug`), no rethrow | Confirmed |
| Shared cloud test base uses proxy runners | `AbstractFullDistribZkTestBase.java` 849 (runner built with the proxy flag) | Confirmed |
| Changelog file exists | `changelog/unreleased/SOLR-18531-jetty-runner-port-reservation.yml` (new in the branch) | Confirmed |
| Hit once in CI on an unrelated PR; no assertion ran | Not verifiable here. The t4 report records the escape and "no assertion" from a quote in the assignment. It does not name the CI run or PR. | Not checked against CI |

## 5. Round 1 items

Owner decisions O1 to O5 are not recorded anywhere in the workspace. The state below comes from the head code and the live body.

| Item | State at head | State in live body | Open or settled |
|---|---|---|---|
| F1 (blocking). Runners removed by `stopJettySolrRunner` are not released at shutdown | Code unchanged. The head comments and Javadoc now state the gap (`MiniSolrCloudCluster.java` 638 to 643; `JettySolrRunner.java` `releasePortReservation()` Javadoc) | Limits bullet 2 states it accurately. The "What this change does" sentence still says the port is held "until the node starts on it again or the cluster shuts down", with no exception (D2) | Settled as a disclosed limit. D2 wording still to fix |
| F2 (blocking until verified). Reservation bind may fail on Linux after traffic (TIME_WAIT) | Code unchanged. Non-reuse bind; on failure it logs a warning and returns with no reservation (`JettySolrRunner.java` 701 to 721). Receipt line 11: the Linux traffic-before-stop variant (two seeds) "did not reproduce the TIME_WAIT gap". The variant is not in the branch test | Proof paragraph reports the variant with sub-claims that the receipt does not contain (D4). No Limits line for the residual case (reservation fails silently and the port is unprotected) | OPEN. The residual gap is neither fixed nor stated in Limits |
| F4 (blocking). Restart on a fresh port leaves the old port reserved | Code unchanged. `start(false)` releases only `config.port`. `releasePortReservation()` names only `jettyPort`, the current port (`JettySolrRunner.java` 732 to 734). The head Javadoc and commit message state the correct rule | Limits bullet 3 states the gap, but gives the wrong release rule: "until the old runner is closed or the JVM exits" (D1) | OPEN. Disclosed, with a wrong release rule |
| F3 (disclosure). Proxy port has no reservation; failed rebind silent | Code unchanged (`SocketProxy.java` 218 to 222) | Limits bullet 1 states it, including the debug-level log | Settled (disclosed) |
| F5 (disclosure). Standalone runners hold their port until JVM exit | Changed at head: `close()` releases (`JettySolrRunner.java` 955 to 965). This is the O4 "release in close()" option | Limits bullet 4 says runners "stopped and never restarted" keep the port until JVM exit. It omits "and never closed" (D3) | Settled in code. The Limits wording is narrower than the rule and needs the fix in D3 |
| F10 (disclosure). Changelog title overclaims a restart cannot fail with BindException | Changed at head; the claim is gone | PR title does not repeat it (section 2) | Settled |
| O3 (disclosure). `TestPullReplica` skipped, never ran | The re-gate ran the class with `tests.nightly=true`: 39 total, 1 skipped, 0 failures (receipt line 11). Source count agrees (section 4) | Body states 39 tests, 1 skipped, 0 failures, with the nightly reason | Settled by the re-gate. Receipt line 8 still has the old "39 tests skipped" wording (R1) |
| O1 (response to F2) | Not taken. No new behavior at head | No Limits line for F2 | OPEN (follows F2) |
| O2 (proxy port) | Not taken as code | Stated as a limit (Limits bullet 1) | Settled as a limit |
| O4 (standalone runners) | Code took "release in close()" | Partly reflected (D3) | Settled in code; wording open |
| O5 (restart route) | Same-port route kept | Choice section asks the question; the fresh-port alternative is named | Settled as a choice to check |
| F6, F7, F8, F11 (minor, no body requirement) | Still present. F6: a failed start drops the reservation. F7: a second stop logs a false "not protected" warning (`JettySolrRunner.java` 701 to 721). F8: release is by port number, not owner. F11: the test does not cover shutdown release, fresh-port restart, a proxy runner, traffic before stop, or a failed start (it now covers `close()`) | Not in the body | Open, low priority; noted for completeness |

## 6. Body drift (exact text against head code and receipt)

- **D1. Limits bullet 3.** Body: "A restart on a fresh port leaves the old port reserved the same way, until the old runner is closed or the JVM exits." Head: `close()` calls `releasePortReservation()`, which names only the current `jettyPort` (`JettySolrRunner.java` 955 to 965, 732 to 734). After a restart on a new port, closing the runner releases the new port and leaves the old one held until the JVM exits or a later start on that exact port. The head Javadoc states this rule ("The same applies to the old port of a runner restarted on a different port").
- **D2. "What this change does", behavior sentence.** Body: a stopped runner keeps its port "until the node starts on it again or the cluster shuts down". Head: `close()` is a third release route, which the sentence omits. Also, cluster shutdown does not release runners removed by `stopJettySolrRunner` (`MiniSolrCloudCluster.java` 627, 644 to 645). The body's own Limits bullet 2 says so, so the two sentences disagree.
- **D3. Limits bullet 4.** Body: "Standalone runners that are stopped and never restarted also keep their port until the JVM exits." Head: `close()` releases. The sentence holds only for standalone runners that are also never closed.
- **D4. Proof, Linux sentence.** Body: "outside binds were refused even with address reuse allowed, and the restart on the same port worked both times." Receipt line 11 records only: "Linux traffic-before-stop variant (two seeds) did not reproduce the TIME_WAIT gap." The three sub-claims are not in the receipt, no gate log is named, and the variant is not in the branch test.

Receipt items for the main side (not body drift):

- **R1.** `receipts/SOLR-18531.md` line 8 ("Regression runs at the head") still says "TestPullReplica 39 tests skipped, 0 failures". Line 11 (the re-gate) says 39 tests with 1 skipped under `tests.nightly=true`. Line 8 is the first gate's wording and conflicts with line 11.
- **R2.** The Linux variant has an outcome line but no gate log name, so the body's extra detail cannot be traced.
- **R3.** `reports/flaky-fix-post-pr-review-round-1.md` still says slice 2 is inactive because no PR was open. PR #5101 is open now. The main side should update it.

## 7. CI and review state

Check suites at head (5 suites, checked with the GET check-suites endpoint):

| Workflow | Trigger | State at head |
|---|---|---|
| Pull Request Labeler | `pull_request_target` | success (completed 2026-10-10T19:43:56Z) |
| Solr Tests via Crave | `pull_request` | action_required, no run |
| Gradle Precommit | `pull_request` | action_required, no run |
| Validate Changelog | `pull_request` | action_required, no run |
| Admin UI Tests | `pull_request` | action_required, no run |

- `statusCheckRollup` in the live PR lists only the labeler. The four `action_required` suites do not appear there.
- `mergeStateStatus`: UNSTABLE. `reviewDecision`: empty.
- `action_required` is incomplete validation under AGENTS.md. The local gate in the receipt does not replace these upstream checks. None of them has run at this head, so none of their results is actionable yet.
- Reviews: none (REST `pulls/5101/reviews` returns []). Issue comments: none. Review comments: none.
- Labeler annotation: one notice from GitHub that the `ubuntu-latest` image migrates on 2026-10-19. It is about the runner, not the change.

Automated findings: none from a reviewer or bot. Nothing to verify as real. The one automated notice (the labeler annotation) is not a finding about this change.

## 8. Verified and rejected automated findings

None to verify. No reviewer or bot comment exists on PR #5101 at this head.

## 9. Not done and limits of this check

- No build, Gradle, test, Selenium or gate run. The `TestPullReplica` count (39) was checked against source (`@Repeat(iterations = 30)`, one `@Ignore`), not against a JUnit XML. The receipt's pass counts are taken as written.
- No CI logs were available. The CI event in the body is not checked.
- The Linux variant is not in the branch and was not run here.
- The claim file was not marked, and no owner decisions are recorded in the workspace.
