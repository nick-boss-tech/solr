# Flaky-fix post-PR review round 6, slice 2: SOLR-18531 final confirmation

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-6.md`, slice 2. Live PR: #5101 (draft) on apache/solr, fork branch `solr-18531-submit`, head `c8a67c7267d00e33ad8323c683c040b539989b0d`. Claim: `claims/pool-flaky-fix-post-pr-review-round-6.md`.

Scope: read-only. `git ls-remote origin refs/heads/solr-18531-submit` returned `c8a67c7267d00e33ad8323c683c040b539989b0d`, equal to the named head. The head commit was already in the local object store, so no fetch ran. Head source was read with `git show c8a67c7...:<path>` into the session scratchpad. Pr-prepare files were read with `git show origin/pr-prepare:<path>` (worktree at tip `1e949e78954`). The live PR was read with `research/gh.ps1` (`pr view`, and GET calls for reviews, issue comments, review comments, check runs and workflow runs). No PR body edit, comment, review, close, submit-branch edit, Jira write, build, Gradle, test, Selenium, gate, test-queue or reproduction run.

Line numbers in this report count the CR-stripped live body from the top, with the AI header as line 1.

## Verdict

**SATISFIED.** The three round 5 items, the two round 6 Limits lines, the gate status, the Proof numbers and the release-route Limits all match the head and the receipt. Four non-blocking notes are listed under Whole-body findings for the lead's judgment. None is a STILL OPEN item.

## Item table

| Item | Live wording | Source check | Result |
|---|---|---|---|
| R5-a: bind claim qualified to Linux | Line 19: "Behavior changes: a stopped runner keeps its port bound, so nothing else can bind that port on Linux until the node starts on it again, the runner is closed (a runner's `close()` releases its port), or, for runners still in the cluster's list, the cluster shuts down." | JettySolrRunner.java 718 to 720 (code comment) limits the refusal to Linux. Gate host vm1 is the "first Linux VM" (`hosts/vm1.md` on pr-prepare). Line 17 also says "on Linux". | SATISFIED |
| R5-b: summary no longer overstates for removed runners | Line 13: "**A stopped runner holds its port until it starts again, is closed, or its cluster shuts down while the runner is still in the cluster.**" | MiniSolrCloudCluster.java 514 to 519 and 528 to 532 remove a stopped runner from `jettys`. The shutdown snapshot at 627 excludes it, and the release loop at 644 to 646 covers only the snapshot. The wording "while the runner is still in the cluster" is the equivalent qualifier. | SATISFIED |
| R5-c: changelog citation as a head-SHA link | Line 55: "Changelog: [changelog/unreleased/SOLR-18531-jetty-runner-port-reservation.yml](https://github.com/nick-boss-tech/solr/blob/c8a67c7267d00e33ad8323c683c040b539989b0d/changelog/unreleased/SOLR-18531-jetty-runner-port-reservation.yml#L1-L9)" | The file exists at the head with 9 lines. Lines 1 to 3 hold the title and lines 7 to 9 the SOLR-18531 link. Round 5 had this at line 53; it moved to 55 because two Limits lines were added above it. | SATISFIED |
| R6-2a: failed-start Limits line | Line 52: "A failed start leaves the port unreserved. `start()` releases the reservation before it binds the port, and a failure after that release does not put the reservation back." | `start(boolean)` at JettySolrRunner.java 506. The release is at 517, before `init` (520 to 524) and before `server.start()` or the port retry (526 to 531). No later line re-adds the entry. `start()` (495 to 497) calls `start(true)`. | SATISFIED |
| R6-2b: shared-port-map Limits line | Line 53: "The reservations are held in one map keyed by port, not by runner. Closing a runner releases the reservation on its port even when a different runner has taken that port over in the meantime and holds the current reservation." | `RESERVED_PORTS` is keyed by port (125). `releasePortReservation(int)` (749 to 754) removes whatever socket is under that port. `close()` (968 to 979) calls `stop()` first; `stop()` returns early at 705 to 711 when an entry exists, then `close()` releases the port at 978. Scenario: A stopped on P; B starts on P (its start releases A's entry at 517) and stops (B's entry is placed); A.close() removes B's entry. | SATISFIED |

## Limits check, line by line

| Line | Live statement | Code at head | Result |
|---|---|---|---|
| 47 | Runners with a socket proxy in front reserve only their Jetty port. | The entry is keyed by `jettyPort` only (JettySolrRunner.java 705, 722, 731). No other reservation call exists. | TRUE |
| 47 | The shared cloud test base uses such runners. | AbstractFullDistribZkTestBase.java 849 builds `new JettySolrRunner(..., true)`, which sets `enableProxy`. | TRUE |
| 47 | The proxy port closes on stop and rebinds on start, with no reservation. | `proxy.close()` at 638 to 640 in `stop()`; `proxy.reopen()` at 545 to 547 or `proxy.open()` at 549 in `start`. No proxy-port entry. | TRUE |
| 47 | A failed rebind is logged at debug level and not raised. | SocketProxy.java 204 to 225: the bind is inside the try; the catch at 220 logs at debug (221 to 223) and does not rethrow. | TRUE |
| 48 | A runner stopped with `stopJettySolrRunner` and never restarted is removed from the cluster's list, so cluster shutdown does not release its port. | MiniSolrCloudCluster.java 514 to 519 and 528 to 532 remove it; the snapshot at 627 excludes it; the loop at 644 to 646 releases only the snapshot. | TRUE |
| 48 | Closing the runner releases it. | `close()` at JettySolrRunner.java 968 to 979; the release at 978 names the current `jettyPort`. | TRUE |
| 48 | Otherwise the port stays reserved until a later start on that port or the JVM exits. | The only release routes are a start on that port (517), `close()` (978) and the cluster shutdown loop (645, snapshot only). | TRUE |
| 49 | A restart on a fresh port leaves the old port reserved. | `startJettySolrRunner(jetty)` calls `start(false)` (MiniSolrCloudCluster.java 489 to 490, 503). `start(false)` sets `port` to `config.port` (512) and releases only that port (517). `jettyPort` then moves to the new port (335). The old entry is not touched. | TRUE |
| 49 | Closing the runner releases only the port it currently holds. | `releasePortReservation()` (745 to 747) names `jettyPort`; `close()` calls it at 978. | TRUE |
| 49 | Until a later start on that port or the JVM exits. | Only the start at 517 releases by port number. | TRUE |
| 50 | Standalone runners stopped and never restarted or closed keep their port until a later start on it or the JVM exits. | `stop()` reserves for every runner (684); `reserveJettyPort()` has no cluster check. Only 517 and 978 release. Cluster shutdown never sees standalone runners. | TRUE |
| 51 | The refusal of later binds is verified on Linux; not verified on Windows. | Code comment 718 to 720 says the same. Gate host vm1 is Linux (`hosts/vm1.md`). The receipt does not name the OS itself. | TRUE |
| 52 | Failed start leaves the port unreserved (round 1 F6). | See item R6-2a. | TRUE |
| 53 | Shared map: close can release another runner's reservation (round 1 F8). | See item R6-2b. | TRUE |
| 15 | `close()` releases the runner's own socket. | `close()` 978 releases the entry under the runner's port. In the line 53 edge the entry belongs to another runner; see whole-body note N-2. | TRUE in the usual case |
| 15 | Shutdown closes the sockets for runners still in the list, after the stops finish. | `invokeAll` stops (635 to 636); `checkForExceptions` (637 to 638); release loop (644 to 646); rethrow (647 to 649). The release runs before the rethrow. | TRUE |

Release routes, as the lead listed them:
- A later start on the same port releases: 517. TRUE.
- `close()` releases the current port: 978. TRUE.
- Cluster shutdown releases the runners the cluster still holds: 627 and 644 to 646. TRUE.
- A runner removed by `stopJettySolrRunner` is not released at shutdown: 514 to 519, 528 to 532, 627. TRUE.
- A restart on a fresh port leaves the old port reserved: 335, 512, 517, 745 to 747. TRUE.
- The proxy port has no reservation: no entry keyed by proxy port anywhere under `solr/`. TRUE.
- A standalone runner stopped and never closed keeps its port: 684, 517, 978. TRUE.

Only the callers of `releasePortReservation` exist at 517 (start), 645 (shutdown) and 978 (close). Grep of `solr/` at the head confirms there are no others.

## Status and Proof checks

Status (item 3):
- `gates/SOLR-18531.md` on origin/pr-prepare: "- Status: re-gate 3 GREEN at c8a67c7267d00e33ad8323c683c040b539989b0d (runner g18531-regate3.sh, log g18531-regate3.log; GATE RUNNER DONE 2026-10-10T22:30:08Z). Earlier gates: ..."
- `receipts/SOLR-18531.md` on origin/pr-prepare, header: "- Status: GATE GREEN at the branch head (re-gate 3 finished 2026-10-10)." with the gated head c8a67c7.
- Result: SATISFIED. Both say green at c8a67c7.
- Receipt-side note (not a PR item): the receipt's "In flight (2026-10-10)" paragraph still says re-gate 3 is running and "GATE PENDING", which contradicts its own header. The main side should retire that paragraph.

Proof (item 4): every number in the live Proof traces to the receipt at this head.

| Live line | Live claim | Receipt (origin/pr-prepare) | Head check | Result |
|---|---|---|---|---|
| 25 | Two new tests in TestJettySolrRunner, named | Names both tests in the "New test" line | `testStoppedRunnerKeepsItsPortUntilRestart` at TestJettySolrRunner.java 122; `testStoppedRunnerThatServedTrafficKeepsItsPortUntilRestart` at 177 (both `@Test` at 121 and 176) | TRUE |
| 27 | Reverted framework: 4 tests run, 2 failures, each the reservation assertion | "runs 4 tests with exactly 2 failures, both reservation assertions" | The class has 4 `@Test` methods (38, 76, 122, 177) | TRUE |
| 27 | At this head TestJettySolrRunner passes 4 of 4 | "At the head the same class passes 4 of 4" | Not re-run | TRUE (receipt) |
| 27 | Verified 2026-10-10 at c8a67c7... | Header: re-gate 3 finished 2026-10-10; gated head c8a67c7 | Head matches | TRUE |
| 31 | LeaderElectionIntegrationTest 1 test, 0 failures | "LeaderElectionIntegrationTest 1 test, 0 failures" | Not re-run | TRUE (receipt) |
| 32 | TestCoordinatorRole 9 tests, 0 failures | Same | Not re-run | TRUE (receipt) |
| 33 | TestPullReplica 39 tests, 1 skipped, 0 failures, nightly enabled; the skip is an @Ignore | "39 tests with 1 skipped (an @Ignore method) and 0 failures under tests.nightly=true" | Not re-run | TRUE (receipt) |
| 25 | First test checks that a stopped runner's port stays reserved until a restart, and that close() gives it back | Receipt names the same behavior | Test body 122 to 174: foreign bind fails (143 to 149); restart (152); close then foreign bind succeeds (159 to 168) | TRUE |
| 25 | Second test serves a real request before the stop, checks outside binds with reuse off and on, and a restart serves again | Receipt: "serves a real request before the stop, asserts foreign binds fail with reuseAddress false and with reuseAddress true, then restarts" | Test body 177 to 232: request (198 to 201); stop (203); probes with reuse false and true (209 to 217); restart and serve (220 to 226) | TRUE |

Served-traffic test (item 4): `testStoppedRunnerThatServedTrafficKeepsItsPortUntilRestart` exists at TestJettySolrRunner.java 177 at the head.

## Whole-body findings

Checks (item 5):
- Citations: five links, all at `c8a67c7267d00e33ad8323c683c040b539989b0d`, owner `nick-boss-tech`, repo `solr`. Line 15 MiniSolrCloudCluster.java L639-L646 (comment at 639 to 643, loop at 644 to 646). Line 17 JettySolrRunner.java L715-L722 (comment at 715 to 720, reuse at 721, bind at 722). Line 25 TestJettySolrRunner.java L122 and L177. Line 55 changelog L1-L9. Each lines range contains the claim it supports. No bare file citations and no cross-repo PR references.
- Title: "SOLR-18531: Reserve a stopped test runner's port until its next start". Accurate as the headline for the main behavior. It names only the next-start release; close() and cluster shutdown also release, and the body states both. Not an overclaim.
- Bold summaries: lines 7, 13, 23, 39 and 45 open the five claim sections (What happens today, What this change does, Proof, A choice to check, Limits). The "### AI assistance" footer (line 57) has no bold summary, which matches the approved template in pr-formula.md; it carries no claim.
- Internal vocabulary: grep of the body finds no gate, receipt, ledger, seed, claim, takeover, ticket, run identifier, log file name or VM name. "log warning" and "logged at debug level" are ordinary English. The body has no em dashes.
- Release-route Limits: all statements match the code (table above).

Non-blocking notes (no verdict change; for the lead's judgment):
- N-1. Length. The body is 6,312 characters (`wc -m`, CR-stripped). The pr-formula length guide is roughly 3,500 characters unless the ticket is unusually complex. The round 6 Limits lines and the release-route bullets account for much of the growth.
- N-2. Line 15 says a runner's `close()` "releases its own socket". Line 53 says close() can release another runner's reservation. Line 53 discloses the edge, so the claim is not hidden. A tighter wording would be: "a runner's `close()` releases the reservation on its port (see Limits)."
- N-3. Line 19 restates the summary claim on line 13. pr-formula says a claim in the summary is not restated in the body. The on-Linux qualifier on line 19 was a round 5 requirement, so the restatement is expected; no change needed unless the lead wants it trimmed.
- N-4. Proof line 27 does not name the platform. The served-traffic test has no platform guard (no Assume or OS check in the file), so its `reuseAddress=true` refusal probe also runs on Windows. Line 51 discloses that the refusal is verified on Linux only. Optional wording: "At this head, on Linux, TestJettySolrRunner passes 4 of 4." The Linux host is supported by `hosts/vm1.md`; the receipt itself does not name the OS.
- N-5. Line 9 says `LeaderElectionIntegrationTest.testSimpleSliceLeaderElection` "hit this once in CI". The source is the round 1 test 4 record (CI run 37991927523). The CI log is not in the workspace. This is unchanged since round 1 and is not part of this round's items.
- N-6. Code comment only, not a body item: the `RESERVED_PORTS` comment at JettySolrRunner.java 118 to 120 says an entry is released when "the runner holding the entry is closed". Release is by port number (749 to 754), so it has the same imprecision as Limits line 53.

## Body vs draft (item 6)

- Live body: `gh.ps1 pr view 5101 --json body --jq .body`, CR-stripped.
- Draft: `pr-drafts/flaky-fixes/SOLR-18531.md` on origin/pr-prepare, CR-stripped.
- Both are 59 lines. `diff` reports no differences. Both carry the round 5 fixes (line 19 "on Linux", line 13 "while the runner is still in the cluster", changelog as a head-SHA link) and both carry the two round 6 Limits lines (lines 52 and 53).

## CI and review state (item 7)

- `statusCheckRollup` on PR 5101 has one entry: `labeler` (Pull Request Labeler), CheckRun, COMPLETED, SUCCESS (workflow run 38090934759, `pull_request_target`).
- Workflow runs at the head (`actions/runs?head_sha=...`), not in the rollup:
  - Validate Changelog (38090936070, pull_request): action_required, no jobs ran.
  - Solr Tests via Crave (38090936066, pull_request): action_required, no jobs ran.
  - Gradle Precommit (38090936074, pull_request): action_required, no jobs ran.
  - Admin UI Tests (38090936063, pull_request): action_required, no jobs ran.
- `action_required` means maintainer approval is pending. Under AGENTS.md this is incomplete validation, not evidence of a pass.
- `mergeStateStatus`: UNSTABLE. `reviewDecision`: empty. State: OPEN, draft true.
- Reviews: none. Issue comments: none. Review comments: none.

## Verified and rejected automated findings

None. No reviewer or bot comment exists on PR 5101 at this head. The one completed check (labeler) is SUCCESS; its annotations were not read.

## Not done

No PR body edit, comment, review, close, submit-branch edit or Jira write. No build, Gradle, test, Selenium, gate, test-queue or reproduction run. No fetch ran. The report is not committed; the lead commits it.
