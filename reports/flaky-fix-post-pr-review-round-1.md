# Flaky-fix post-PR review round 1

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-1.md`. Claim: `claims/pool-flaky-fix-post-pr-review-round-1.md`. Lead: the windows review agent. Part reports: `reports/flaky-fix-post-pr-review-round-1-s1.md` (SOLR-18530, PR #5098) and `reports/flaky-fix-post-pr-review-round-1-s3.md` (SOLR-18532, PR #5100). Slice 2 (SOLR-18531) stays inactive: no PR is open for it.

All live reads were read-only. Nothing was posted, edited or pushed to a PR, a submit branch or Jira. No build, Gradle, test or gate run.

## Summary

| Slice | PR | Head checked | Verdict | Main-side items |
|---|---|---|---|---|
| 1, SOLR-18530 | #5098 (draft) | `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`, equals the fork tip | DRIFT | 4 |
| 3, SOLR-18532 | #5100 (draft) | `f1e5031fc3a9624b03daf353f26c977891d7d876`, equals the fork tip | FINDINGS | 11 (2 causal or Limits wording, 1 dropped round 1 item, 1 open count) |

Both live bodies are byte-identical to their drafts on the `pr-prepare` tip. Both heads are the fork tips. Every Proof number on each PR matches its receipt.

CI on both PRs: only the labeler check has run (success). Gradle Precommit, Solr Tests via Crave and Validate Changelog are `action_required` on both heads, so they have not run. Neither PR has reviews or comments. Per AGENTS.md, `action_required` is incomplete validation. The local gate in each receipt does not replace those checks.

## Slice 1: SOLR-18530 (PR #5098)

Verdict: DRIFT. The findings are in `reports/flaky-fix-post-pr-review-round-1-s1.md`. The lead confirmed the link SHA and the routing wording against the draft.

1. **Link SHA.** All four code links in the body and the draft cite `98e5368d996…`. The rule is the PR head, `14868bc7a7f…`. The file content is the same at both SHAs, so this is a re-point.
2. **Routing wording (F2, round 1 D3).** The bold line "this test sends its adds only to the shard leader" and the title "to leaders only" are stronger than the code. The test's random routing flag decides whether the cut replica stays on the failover list. The Limits line discloses the randomization, but not that the cut replica stays on the list. Options: reword the bold line and title, or set `directUpdatesToLeadersOnly` explicitly, which needs a new gate run.
3. **"Forward" wording (F3).** "A failed forward is logged" uses "forward" for a leader-to-replica send. The cited range uses "forward" for a different node type, where failures are returned. The claim is right for the leader-to-replica case. The sentence needs rewording, and the link range re-checked after the edit.
4. **Round 1 D1 exemption (F4).** The changelog entry was dropped at the head. The exemption sentence that round 1 asked for is not in the body. Add it, or record that none is wanted.

Still open from round 1: D2 (more evidence before opening: the CI-log port check and the 20 base runs), D4 (keep or drop "A choice to check"), D5 (the record of Nick's decision on D1 to D4 and the opening). None is recorded in the workspace.

Lead's check: the four links cite `98e5368d996` in the draft on the `pr-prepare` tip, and the bold line reads "sends its adds only to the shard leader". Both match the report.

## Slice 3: SOLR-18532 (PR #5100)

Verdict: FINDINGS. The findings are in `reports/flaky-fix-post-pr-review-round-1-s3.md`.

Head and tree check: the fork tip is `f1e5031`. Its tree, `40a18b12e637`, equals the gated tree at `348dd63`, so the squashed head is the gated tree. The framing is correct: the body says the change "does not address" the install-test teardown failure, and makes no flake-fix claim.

1. **Causal sentence (S3-2).** "The intermittent teardown failure in the install tests comes from another path" states a cause the record does not confirm. Round 1 and the root-cause report mark the install-test causes as unconfirmed. Suggested wording: "The rollback path does not run in those tests, so this change does not address the teardown failure."
2. **Temp-file Limit (S3-3).** The body says "if the write fails". The file is left behind after any failure once it is created: write, close, sync or rename. Suggested: "if any step after the file is created fails".
3. **Non-atomic fallback (S3-4).** The Limit names only the base delete-then-rename default. `StandardDirectoryFactory` also has a non-atomic move when atomic move is unsupported. The main side decides whether to name it.
4. **Dropped round 1 item (S3-5).** The new test's failure wait is 10 polls of 50 ms (`TestRestoreCore.java` 297-306). A slow failure makes the test fail rather than pass. Round 1 s3a-F5 and s3a-D3 are not in the body or in Limits. Add a Limit line, or change the test with a new head and a new gate.
5. **Test count (S3-6, round 1 s3b-D4).** The body says "TestRestoreCore: 4 of 4". The receipt says 4 tests. The source has three `@Test` annotations and one un-annotated `test*` method. Confirm the count from the gate JUnit XML, which is not on disk, or use the receipt's wording.
6. **Optional wording (S3-7, S3-10, S3-9).** "Instead of deleting it" reads as never deleting; the code still deletes when the core had no file. The receipt says "0 failures" where the body says "4 of 4 pass". "A separate open change for SOLR-9865" points at a fork branch, not a public change; name the ticket instead. The SOLR-9865 hunk drop that Nick decided has not happened yet.
7. **Pr-formula gaps (S3-8).** The body has no file links at the head SHA; the file names are plain code spans. "A choice to check" ends with an offer, not the pointed question. Limits has no bold summary line. The body is 3,754 bytes against the roughly 3,500 guide.

Round 1 item status: the overlap with SOLR-9865 and the framing are settled in the body. The read-error behavior change is stated. The "final assertion" wording is resolved; the body and receipt say "pointer check". Commit history is settled by the single squashed commit. Still open: the count (item 5) and the optional "instead of deleting" wording (item 6).

## Main-side actions (none taken here)

1. Re-point the four SOLR-18530 links to `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`, and reword "forward" (SOLR-18530 items 1 and 3).
2. Decide the routing wording or the explicit flag for SOLR-18530 (item 2). An explicit flag needs a new gate run.
3. Record decisions D1 to D5 for SOLR-18530, including the exemption sentence (item 4).
4. Reword the SOLR-18532 causal sentence and the temp-file Limit (items 1 and 2). Decide on the non-atomic fallback Limit (item 3).
5. Add the failure-wait Limit, or change the test with a new gate (item 4).
6. Confirm the "4 of 4" count from the JUnit XML, or reword (item 5).
7. When a maintainer approves the three `action_required` checks on each PR, compare each run's `head_sha` with the head before reading a failure as actionable.
8. The receipt header for SOLR-18530 names `14868bc7a7f` as the gated head, but its runs were at `98e5368d99` (the subagent's F5). The receipt should name the head the runs used.

## Not done

No PR body edit, comment, review, close, submit-branch edit or Jira write. No build, Gradle, test, Selenium or gate run. The only git action with a side effect was a read-only fetch of the two fork branches. That moved the local remote-tracking refs for `solr-18530-submit` and `solr-18532-submit` to the heads above.

## Slice 2: SOLR-18531 (PR #5101, draft)

Head `5ba914ca745933251958412a4ccb226ecbaf0ad8`, which equals the fork tip. The live body equals `pr-drafts/flaky-fixes/SOLR-18531.md` after line-ending normalization. Part reports: `reports/flaky-fix-post-pr-review-round-1-s2a.md` (PR body and checks) and `reports/flaky-fix-post-pr-review-round-1-s2b.md` (receipt and head code).

**Verdict: DRIFT.** Four sentences in the body do not match the head code or the receipt. The lead checked the disputed point directly.

Body drift:

1. **Limits, fresh-port restart (draft line 37).** The body says the old port stays reserved "until the old runner is closed or the JVM exits". The lead read `JettySolrRunner.close()` at the head (line 955): it calls `releasePortReservation()` with no argument, so it releases only the port the runner currently holds. After a restart on a fresh port, the old port stays reserved until a later start on that port or the JVM exits. The sentence is wrong for that case.
2. **Behavior sentence (draft line 11).** It omits that `close()` now releases the port, and it overstates the cluster shutdown release. Round 1 F1 is still open: a runner removed with `stopJettySolrRunner` is not released at shutdown.
3. **Standalone wording (draft line 38).** It says "stopped and never restarted". It should say "stopped and never restarted or closed", because `close()` now releases.
4. **Linux variant (draft line 25).** The body describes what the variant showed: the port stayed reserved, outside binds were refused with address reuse allowed, and the restart worked both times. The receipt records only that the two-seed variant did not reproduce the TIME_WAIT failure. The body's detail is not in the receipt.

Receipt issues (main side, not the body):

- Lines 5 to 9 of `receipts/SOLR-18531.md` still hold the first-gate numbers under the new head header. The re-gate paragraph names no SHA.
- `gates/SOLR-18531.md` still names the earlier head `a0150bf`.
- Line 8 still says "TestPullReplica 39 tests skipped". The re-gate paragraph says the class ran under `tests.nightly=true` with 39 tests, 1 skipped, 0 failures. The round 1 O3 item is settled by the re-gate, so the stale line should be replaced.

Round 1 blocking and disclosure items at the head (state in the live body in brackets):

| Item | State at head | Open or settled |
|---|---|---|
| F1 runners removed by `stopJettySolrRunner` not released at shutdown | Not changed (`MiniSolrCloudCluster.java` 514-532, 644-646) [disclosed] | Open, disclosed |
| F2 Linux bind may fail after traffic (TIME_WAIT) | Code unchanged (`JettySolrRunner.java` 707-716); reading only [not in Limits; the body's Linux detail is not in the receipt] | Open, not named in Limits |
| F3 proxy port not reserved; failed rebind silent | Not changed (`SocketProxy.java` 204-225) [disclosed in Limits] | Open, disclosed |
| F4 restart on a fresh port leaves the old port reserved | Not changed (`JettySolrRunner.java` 512-517); `close()` does not release it [disclosed, wrong release rule, item 1] | Open, disclosed with a wrong rule |
| F5 standalone runners hold their port until the JVM exits | Changed: `close()` releases the current port (line 955) [Limits line 38; wording, item 3] | Settled in code; wording to fix |
| F10 changelog title overstates | Fixed: the changelog title no longer claims a BindException | Settled |
| O3 `TestPullReplica` skipped, none ran | Settled by the re-gate (39 tests, 1 skipped, 0 failures under nightly) | Settled; receipt line 8 stale |

Lead check: the reviewers' reading of `close()` and of the fresh-port release rule matches the code. The re-gate paragraph is the only record for the head's proof numbers. The lead did not run or re-verify any test.

Main-side actions (none taken here): correct draft lines 11, 25, 37 and 38; name F2 in Limits, or record why not; replace the stale `TestPullReplica` line in the receipt; update the receipt header and `gates/SOLR-18531.md` to the head. Body edits go through the main side.
