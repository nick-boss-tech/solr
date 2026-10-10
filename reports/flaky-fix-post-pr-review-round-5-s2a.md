# Flaky-fix post-PR review round 5, slice 2a: SOLR-18531 final confirmation

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-5.md`, slice 2. Live PR: #5101 (draft) on apache/solr, fork branch `solr-18531-submit`, head `c8a67c7267d00e33ad8323c683c040b539989b0d`.

Scope: read-only. `git ls-remote origin refs/heads/solr-18531-submit` returned `c8a67c7267d00e33ad8323c683c040b539989b0d`, equal to the PR head. One read-only fork fetch ran (`refs/remotes/origin/solr-18531-submit`). Live PR read through `research/gh.ps1` (`pr view`, and GET calls for reviews, issue comments, review comments, check runs, and workflow runs). No PR body edit, comment, review, close, submit-branch edit, Jira write, build, Gradle, test, Selenium, gate, test-queue, or reproduction run. The line numbers below count the CR-stripped live body from the top, with the AI header as line 1.

## Verdict

**STILL OPEN.** Three items remain. All are wording or citation fixes; the code, proof numbers, and release routes are correct.

1. **Line 19** (Behavior changes): "so nothing else can bind that port" is an unqualified bind claim. Add "on Linux" (fix S2A-1).
2. **Line 13** (summary of What this change does): "or its cluster shuts down" overstates the release for runners removed by `stopJettySolrRunner`, which Limits line 48 says are not released at shutdown (fix S2A-2).
3. **Line 53** (Changelog): the file citation is a code span, not a link at the head SHA. Slice 1 of this round requires the head-SHA link for its changelog (fix S2A-3).

Fixes S2A-1 to S2A-3 must go to the live body and to `pr-drafts/flaky-fixes/SOLR-18531.md` on `origin/pr-prepare` together.

## Item table

| Item | Live wording | Source check | Result |
|---|---|---|---|
| R4-1: Proof has no CI seed value or seed text | Line 23: "**New tests pin the reservation down, and the existing restart suites still pass at this head.**" Line 29: "Restart regression runs at the same head:". No "seed" anywhere in the body. | Receipt line 9 holds the seed; the body omits it. Counts on lines 25, 27, and 31 to 33 match receipt lines 8 and 9. | SATISFIED |
| R4-2: every section opens with a bold one-line summary | Bold lines 7, 13, 23, 39, 45 open the sections at lines 5, 11, 21, 37, 43. The footer "### AI assistance" (line 55, text on line 57) is plain template boilerplate and is not counted as a section. | `pr-formula.md` presentation rule. The footer carries no claim. | SATISFIED (lead to confirm the footer is not counted) |
| R4-3: Limits release routes complete; "until the JVM exits" allows for a later start | Lines 48, 49, and 50 each end "until a later start on that port or the JVM exits". Line 47 covers the proxy. Line 15 covers close and shutdown. | Every statement checked in the Limits table below. `RESERVED_PORTS` javadoc (JettySolrRunner.java 115 to 124) names the same three release routes and the same exceptions. | SATISFIED |
| N1: platform Limits line present | Line 51: "The reservation's refusal of later binds is verified on Linux. On Windows, address-reuse bind semantics differ, and the same exclusion is not verified there." | Present. Gate host vm1 is Linux (`hosts/vm1.md`: "first Linux VM"). The code comment at JettySolrRunner.java 718 to 720 carries the same qualifier. | SATISFIED |
| N1: bind claim qualified to Linux | Line 17 is qualified ("refuses every later bind, with or without reuse, on Linux"). Line 19 is not: "so nothing else can bind that port until the node starts on it again, the runner is closed ..., or, for runners still in the cluster's list, the cluster shuts down." | Line 19 restates the exclusion without the Linux qualifier, which the Limits line and the code comment both carry. | STILL OPEN (line 19, fix S2A-1) |
| N2: guard present in the code | Not a body line. | JettySolrRunner.java 705 to 711: `reserveJettyPort()` returns early when `RESERVED_PORTS` already holds `jettyPort`, with a comment naming close after stop and stop before cluster shutdown. | SATISFIED (code) |
| Summary line 13 (whole-body read) | "**A stopped runner holds its port until it starts again, is closed, or its cluster shuts down.**" | MiniSolrCloudCluster.java 627 snapshot and 644 to 646 release exclude runners removed by `stopJettySolrRunner` (514 to 517, 528 to 531). Line 15 qualifies this ("for runners still in its list"); the summary does not. | STILL OPEN (line 13, fix S2A-2) |
| Citations: file links | Four links on lines 15, 17, 25 at `blob/c8a67c7267d00e33ad8323c683c040b539989b0d`. | See the citation table. | SATISFIED |
| Citations: changelog | Line 53: "Changelog: `changelog/unreleased/SOLR-18531-jetty-runner-port-reservation.yml`" (a code span). | The file exists at the head. Slice 1 of this round requires a head-SHA link for its changelog; the `pr-formula.md` template shows a code span, but the 2026-10-08 presentation rule says each file citation is a link. | STILL OPEN (line 53, fix S2A-3) |
| Title | "SOLR-18531: Reserve a stopped test runner's port until its next start" | Accurate for the main behavior. It omits close() and cluster shutdown as earlier release routes, which the body covers. Not an overclaim. | SATISFIED (partial summary, optional) |
| Changelog file | `changelog/unreleased/SOLR-18531-jetty-runner-port-reservation.yml` | Exists at the head. Its title ("holds a stopped runner's port in reserve until the next start on that port") matches the body. | SATISFIED |
| Body vs draft | See the body-vs-draft section. | Identical content after CR stripping. | SATISFIED |
| Internal vocabulary | No seed, gate, receipt, ledger, claim, takeover, log file name, or run identifier in the body. The only SHA is the head SHA in the links, which is allowed. | Grep of the body. | SATISFIED |
| Line 9: retry and 60 seconds | "The retry only tries the same port, and it stops after 60 seconds." | JettySolrRunner.java 578 to 601 retries only the `port` argument; `JettyConfig.java` line 83 sets `portRetryTime = 60`. | SATISFIED |
| Line 9: CI hit | "`LeaderElectionIntegrationTest.testSimpleSliceLeaderElection` hit this once in CI, on an unrelated pull request. No assertion ran." | The source is the round 1 test 4 record (CI run 37991927523; the record says the branch changed only another class). The CI log is not in the workspace. | SATISFIED (source only; not re-checked against the CI log) |
| Line 41: node names | "the test compares node names" | LeaderElectionIntegrationTest.java 83, 91, 119, 126, 141 compare `getNodeName()`. I did not trace how the ZooKeeper node name derives from the port. | SATISFIED |
| Line 33: TestPullReplica nightly and skip | "`@Nightly` ... the one skip is an `@Ignore`d method" | TestPullReplica.java line 73 `@Nightly`; one `@Ignore`, at line 461. | SATISFIED |

## Limits check, line by line

| Line | Statement | Code at head | Result |
|---|---|---|---|
| 47 | Runners with a socket proxy reserve only their Jetty port. | `reserveJettyPort()` uses `jettyPort` only (JettySolrRunner.java 701 to 722); the entry is keyed by `jettyPort` (731). | TRUE |
| 47 | The proxy port closes on stop and rebinds on start, with no reservation. | Proxy closed in `stop()` (638 to 640); reopened in `start()` (545 to 547) or opened (549). No proxy-port entry. | TRUE |
| 47 | A failed rebind is logged at debug level and not raised. | `SocketProxy.reopen()` (SocketProxy.java 204 to 225) catches `Exception` and logs at debug. | TRUE |
| 47 | The shared cloud test base uses such runners. | AbstractFullDistribZkTestBase.java 849 constructs `new JettySolrRunner(..., true)`. | TRUE |
| 48 | A runner stopped with `stopJettySolrRunner` and never restarted is removed from the list, so shutdown does not release its port. | `stopJettySolrRunner(int)` 514 to 519 and `(JettySolrRunner)` 528 to 532 remove it. Shutdown snapshot at 627 excludes it; the release loop (644 to 646) covers only the snapshot. | TRUE |
| 48 | Closing the runner releases it. | `close()` (JettySolrRunner.java 968 to 979) calls `releasePortReservation()` at 978. | TRUE |
| 48 | Otherwise the port stays reserved until a later start on that port or the JVM exits. | `start()` releases the port it binds (517) before `init` and `server.start()`. No other route. | TRUE |
| 49 | A restart on a fresh port leaves the old port reserved. | `start(false)` binds `config.port` (512); `jettyPort` moves to the new port (335). `releasePortReservation()` (745 to 747), `close()` (978), and shutdown (644 to 646) name only `jettyPort`, the current port. | TRUE |
| 49 | Until a later start on that port or the JVM exits. | Start releases by port number (517). | TRUE |
| 50 | Standalone runners stopped and never restarted or closed keep the port until a later start on it or the JVM exits. | Only routes: start on the port (517) and close (978). Shutdown does not see standalone runners. | TRUE |
| 15 | `close()` releases the runner's port. | JettySolrRunner.java 978. | TRUE |
| 15 | Shutdown closes the sockets for runners still in the list, after the stops finish. | `invokeAll` stops (635 to 636), `checkForExceptions` (637 to 638), release loop (644 to 646), rethrow (647 to 649). Release runs before any rethrow. | TRUE |
| 17 | The held socket refuses every later bind, with or without reuse, on Linux. | Code comment 718 to 720 says the same and limits it to Linux. Verified on Linux per the receipt; Windows not verified. | TRUE (as qualified) |
| 19 | "nothing else can bind that port" | Same exclusion, but no Linux qualifier. | STILL OPEN (fix S2A-1) |
| 51 | Refusal of later binds verified on Linux; not verified on Windows. | Matches code comment 718 to 720; gate host vm1 is Linux. | TRUE |
| all | Every release route is listed. | Only callers of `releasePortReservation`: start (JettySolrRunner.java 517), shutdown (MiniSolrCloudCluster.java 645), close (978). The `RESERVED_PORTS` javadoc (115 to 124) gives the same three routes. No route is missing from the body. | TRUE |

## Proof and citation checks

| Claim | Source at head or in the receipt | Result |
|---|---|---|
| `testStoppedRunnerKeepsItsPortUntilRestart` (line 25) | Declared at TestJettySolrRunner.java 122. Behavior matches 122 to 174. | TRUE |
| `testStoppedRunnerThatServedTrafficKeepsItsPortUntilRestart` (line 25) | Declared at 177. Serves a request, probes foreign binds with reuse off and on (209 to 217), restarts and serves again. Matches the body. | TRUE |
| "4 tests run, 2 failures" on the reverted framework (line 27) | The class has 4 `@Test` methods (38, 76, 122, 177). Receipt line 8: "runs 4 tests with exactly 2 failures". Not re-run. | TRUE (receipt-sourced) |
| "At this head, TestJettySolrRunner passes 4 of 4" (line 27) | Receipt line 8. | TRUE (receipt-sourced) |
| "Verified 2026-10-10 at c8a67c..." (line 27) | Receipt line 1: re-gate 3 finished 2026-10-10. | TRUE |
| LeaderElectionIntegrationTest 1 test, 0 failures (line 31) | Receipt line 9. The class has one `@Test` (line 60). | TRUE |
| TestCoordinatorRole 9 tests, 0 failures (line 32) | Receipt line 9. | TRUE (receipt-sourced) |
| TestPullReplica 39 tests, 1 skipped, 0 failures, nightly on (line 33) | Receipt line 9. `@Nightly` at line 73; one `@Ignore`, at 461. | TRUE |
| Citation L639 to L646 (line 15) | Lines 639 to 643 are the release comment; 644 to 646 are the release loop. | TRUE |
| Citation JettySolrRunner L715 to L722 (line 17) | 715 to 720 comment; 721 `setReuseAddress(true)`; 722 bind on `127.0.0.1`. | TRUE |
| Citation TestJettySolrRunner L122 (line 25) | Declaration of `testStoppedRunnerKeepsItsPortUntilRestart`. | TRUE |
| Citation TestJettySolrRunner L177 (line 25) | Declaration of `testStoppedRunnerThatServedTrafficKeepsItsPortUntilRestart`. | TRUE |
| All links point at owner `nick-boss-tech`, repo `solr`, ref `c8a67c7267d00e33ad8323c683c040b539989b0d` | Read from the body. | TRUE |
| Changelog citation (line 53) | Code span, not a link. | STILL OPEN (fix S2A-3) |

Receipt premise, corrected. The brief says the receipt reports that the traffic-before-stop variant on Linux did not reproduce the TIME_WAIT failure. The receipt at the tip says the opposite: the earlier record was "from an uncommitted scratch variant and was wrong", and the committed test reproduced the gap. The body no longer carries the "did not reproduce" sentence, so no body change follows from this.

## Body vs draft

`pr-drafts/flaky-fixes/SOLR-18531.md` on `origin/pr-prepare` has no CR bytes. The live body (`pr view --json body`) has no CR escapes. After CR stripping, the two match line for line. My capture added one trailing newline to the live file; the JSON body ends with a single newline, as the draft does. Content differences: none. Both files need fixes S2A-1 to S2A-3.

## CI and review state

| Check | Trigger | State at head |
|---|---|---|
| labeler (Pull Request Labeler, run 38090934759) | pull_request_target | COMPLETED, SUCCESS. The only entry in `statusCheckRollup`. |
| Validate Changelog (run 38090936070) | pull_request | action_required; no jobs ran |
| Solr Tests via Crave (run 38090936066) | pull_request | action_required; no jobs ran |
| Gradle Precommit (run 38090936074) | pull_request | action_required; no jobs ran |
| Admin UI Tests (run 38090936063) | pull_request | action_required; no jobs ran |

`action_required` means maintainer approval is pending. Under `AGENTS.md` this is incomplete validation, so these four are not yet evidence. `mergeStateStatus` is UNSTABLE; `reviewDecision` is empty; PR state OPEN, draft true. Reviews, issue comments, and review comments are all empty.

## Verified and rejected automated findings

None. No reviewer or bot comment exists on PR 5101 at this head. The one check that ran (labeler) is SUCCESS. I did not re-read its annotations this round.

## Exact fixes

- **S2A-1, line 19.** Replace "so nothing else can bind that port until the node starts on it again" with "so, on Linux, nothing else can bind that port until the node starts on it again".
- **S2A-2, line 13.** Replace the summary with "**A stopped runner holds its port until it starts again, is closed, or, while it is still in its cluster, the cluster shuts down.**"
- **S2A-3, line 53.** Replace the code span with a link: "Changelog: [`changelog/unreleased/SOLR-18531-jetty-runner-port-reservation.yml`](https://github.com/nick-boss-tech/solr/blob/c8a67c7267d00e33ad8323c683c040b539989b0d/changelog/unreleased/SOLR-18531-jetty-runner-port-reservation.yml)". If the lead keeps the template's code span, that is a decision to record, because slice 1 requires the link.

## Other observations (not verdict items)

- `gates/SOLR-18531.md` on `origin/pr-prepare` still reads "re-gate 3 RUNNING ... GATE PENDING", while the receipt reads GATE GREEN. The receipt is the proof source; the gate file should be updated to match, as N3 was for the head.
- The receipt's claims that the changelog YAML parses and that tidy and Error Prone pass were not re-checked here; no tools were run.
- The gate host is Linux (hosts/vm1.md), which supports the Linux qualifier.
