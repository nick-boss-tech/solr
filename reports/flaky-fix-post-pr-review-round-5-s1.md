# Flaky-fix post-PR review round 5, slice 1: SOLR-18530 (live PR 5098)

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-5.md`, slice 1. Read-only. No PR body edit, comment, review, close, submit-branch edit, Jira write, build, Gradle run, test run, Selenium run, or gate or test-queue run. The only git fetch was the read-only fork fetch of `refs/heads/solr-18530-submit`. The only file written is this report, not committed.

Head check: `git ls-remote origin refs/heads/solr-18530-submit` returns `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`. It equals the PR head (`headRefOid`) and the fork tip after the fetch.

## Verdict

**STILL OPEN, one item.** Round 4 items 1 to 3 are SATISFIED, and so is the B3 "of this shard" check. The final whole-body read finds one sentence that says more than the test does (Limits, line 43). The fix is one clause, applied to the live body and to `pr-drafts/flaky-fixes/SOLR-18530.md` together.

Remaining item (exact):

- **Limits, line 43.** Live text: "The test checks only that replicas become active within 30 seconds. It does not check which recovery path ran. The document count is what rules out peer sync."
  Fact: the adds after the cut go through `leaderClient.add(...)` with no catch (`RecoveryAfterSoftCommitTest.java` lines 111 to 116 at head). An exception there ends `test()` with an error, so the test also checks that those adds succeed. "Only" is therefore wrong.
  Fix: replace "The test checks only that replicas become active within 30 seconds." with "Besides the adds succeeding, the test checks only that replicas become active within 30 seconds." Make the same edit in the draft.

## Item table

| Item | Live wording | Source check | Result |
|---|---|---|---|
| Round 4 item 1: no seed value or "at the CI seed" in Proof | Line 25: "- `RecoveryAfterSoftCommitTest` with this change: 6 of 6 runs passed. Checked 2026-10-10 at 14868bc7a7f5ec5c4fa1ade5f894cbda9533307d." Line 26: "- The original test: 5 of 5 runs passed. It did not fail, so there is no before-and-after result." | A case-insensitive search of the live body finds no "seed" and no "CI". Counts match `receipts/SOLR-18530.md` line 8: the fixed test ran 6 times with 0 failures, the base test ran 5 times with 0 failures. The gate finished 2026-10-10. | SATISFIED |
| Round 4 item 2: Limits opens with a bold one-line summary | Line 37 is "## Limits". Line 39: "**The fix is not proven by these runs, and some neighboring cases are not covered.**" | The first line under the heading is a bold one-line summary, as in the other sections. | SATISFIED |
| Round 4 item 3: changelog citation is a link at the head SHA | Line 47: "Changelog: none. "Pull requests that only touch documentation, tests, build files or CI configuration are exempt and pass without an entry." ([dev-docs/changelog.adoc, section 4](https://github.com/nick-boss-tech/solr/blob/14868bc7a7f5ec5c4fa1ade5f894cbda9533307d/dev-docs/changelog.adoc#L123-L131))" | `dev-docs/changelog.adoc` is byte-identical at base `3f5d4c5bf8ac` and at head. At head, line 123 is the section 4 heading and lines 130 to 131 hold the exemption sentence, inside the cited range. The quote matches. No `changelog/unreleased/` or `solr/changelog/unreleased/` file for SOLR-18530 exists at head. | SATISFIED |
| Round 4 B3: "of this shard" qualifier (carried, re-read) | Line 15: "a failed send to a replica **of this shard** is logged and does not fail the add" | Head `DistributedZkUpdateProcessor.java` L1229-L1231 logs any replica send error and does not add it to the client errors. L1254-L1262 is the `LeaderChanged` return. L1345-L1348 is the other-shard return. All three anchors hold. | SATISFIED |
| File citations are links at head, with claim lines | Lines 15, 17, 33 and 47 each link to `blob/14868bc7.../<path>#L...` | Test L102-L117: the comment (102 to 106), the leaders-only client (108 to 110), the adds (111 to 116). `LBSolrClient.java` L675-L680: the non-retryable branch (`isNonRetryable` is true for UPDATE requests, line 576), which fails over only on a connect exception or a request never sent. `CheckRetryUnrollTest.java` L97-L103 asserts that `ClosedChannelException` is not retried. | SATISFIED |
| Title | "SOLR-18530: Route adds after the proxy cut in RecoveryAfterSoftCommitTest to the shard leaders first" | Base-to-head diff touches one file, `RecoveryAfterSoftCommitTest.java` (15 insertions, 5 deletions). The adds after the cut go through a shard-leaders-first client. The title matches the change. | SATISFIED |
| Routing wording: bold line 13, title, line 15, line 44 | Line 13: "After the cut, this test sends its adds to the shard leaders first." Line 44: the flag is randomized and "the cut replica can stay on the failover list; the leader is still tried first." | `AbstractFullDistribZkTestBase.createNewCloudSolrClient` (2431 to 2449) calls `sendUpdatesOnlyToShardLeaders()`, so `shardLeadersOnly` is true. `SolrTestCaseJ4.RandomizingCloudSolrClientBuilder` (2486) calls `randomizeCloudSolrClient()` (2517 to 2521), which randomizes `directUpdatesToLeadersOnly`. `CloudSolrClient.buildUrlMap` (470 to 482): with the flag off, the non-leader replicas are appended after the leader, so the cut replica is a failover target. With the flag on, only the leader is listed. The cut replica is a non-leader (`ensureAllReplicasAreActive` returns non-leaders, line 95), so the leader is not cut. | SATISFIED |
| Limits line 43: "checks only" | "The test checks only that replicas become active within 30 seconds." | The add calls throw on failure, so the adds succeeding is also checked. The 30 is `maxWaitSecs` in `ensureAllReplicasAreActive` (line 2729). The recovery-path point is accurate. | STILL OPEN (see above) |

## Whole-body findings

Only the line 43 item above blocks the verdict. Checked and accurate: the citations and their line ranges, the title, the routing wording in both settings, the "of this shard" qualifier, the proof counts against the receipt, the "no product code change" statement, the "document count" reasoning (3 documents after the cut against an update log that keeps 2, from `MAX_DOCS` and `ULOG_NUM_RECORDS_TO_KEEP`, with the test's own comment saying the same), and the statement that SolrJ does not retry `ClosedChannelException` on an update.

Non-blocking observations (not part of the verdict; the main side may decide):

- **Proof head.** Line 25 says "Checked 2026-10-10 at 14868bc...". The receipt says the gate ran at `98e5368d996518b9f4a94f85d7c2fcd933d3f485`. Between that head and 14868bc, the only change is the deletion of `changelog/unreleased/SOLR-18530.yml` (7 lines). The test file is identical at both, so the claim holds in substance. An exact option: "Checked 2026-10-10; the test file is unchanged at 14868bc (gated at 98e5368)."
- **Repeated claim.** Line 27 ("This change is not shown to fix it.") and line 41 ("so the fix is not shown to remove it") restate the bold summaries of Proof (line 23) and Limits (line 39). The formula says a claim in the summary is not restated in the body. Removing line 41 is optional, since the summary and line 27 carry it.
- **Trigger wording, line 15.** "when the leader changed during the send" is a loose paraphrase. The code trigger is the replica reporting `LeaderChanged`, that is, that it now thinks it is the leader (`DistributedZkUpdateProcessor.java` L1256 to L1260). An optional paraphrase: "when the replica reports that it is now the leader". This is the same optional point as round 4 O2.
- **Length.** The live body and the draft are both 5,518 bytes, about 5,500 characters, against the formula's guide of about 3,500 characters. This is the same observation as round 4 O1, and the formula allows more for a complex ticket.
- **Symptom text.** The "What happens today" wording (`SolrServerException: IOException occurred when talking to server at ...`, cause `ClosedChannelException`) was not compared with the Jira ticket. The Jira text was not read in this slice.

## Body versus draft

`pr-drafts/flaky-fixes/SOLR-18530.md` on `origin/pr-prepare` and the live body were compared after CR stripping. Neither contains a CR byte. `diff` reports no differences in the text. The live body (JSON `body` field) ends with one newline, as the draft does. The only byte difference seen in my scratch copies was an extra trailing newline added by my own save step, not by the PR or the draft. So the body and the draft match, and the line 43 fix goes to both.

## CI and review state

- Head `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d` equals the fork tip and the PR `headRefOid`.
- PR 5098: OPEN, draft, `reviewDecision` empty, `mergeStateStatus` UNSTABLE (it was UNKNOWN at the round 4 read).
- `statusCheckRollup`: one check, `labeler` (Pull Request Labeler, `pull_request_target`, run 38079120890): SUCCESS.
- Workflow runs at head (read with GET only):
  - Gradle Precommit, run 38079120736: completed, `action_required`. Not run.
  - Solr Tests via Crave, run 38079120826: completed, `action_required`. Not run.
  - Validate Changelog, run 38079120887: completed, `action_required`. Not run. The workflow's exemption at `.github/workflows/validate-changelog.yml` line 67 matches the one changed file (`solr/core/src/test/...`), so it should pass once it runs.
  - `action_required` is an incomplete state, not a failure. Required upstream checks are still pending.
- Reviews: 0. Inline review comments: 0. Issue comments: 0.

## Automated findings

- Verified: none. No review, inline comment or issue comment exists on PR 5098.
- Rejected: none. The labeler result is a labeling step, not a review finding.

## Reads

- From `origin/pr-prepare` (read with `git show`): `assignments/pool-flaky-fix-post-pr-review-round-5.md`, `pr-formula.md`, `reports/flaky-fix-post-pr-review-round-4.md`, `reports/flaky-fix-post-pr-review-round-4-s1.md`, `receipts/SOLR-18530.md`, `pr-drafts/flaky-fixes/SOLR-18530.md`.
- From head `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d` (read-only `git show`): `RecoveryAfterSoftCommitTest.java`, `DistributedZkUpdateProcessor.java` (L1196 to L1352), `LBSolrClient.java` (L570 to L690), `CheckRetryUnrollTest.java`, `CloudSolrClient.java` (routing and builder), `AbstractFullDistribZkTestBase.java` (client helper, `ensureAllReplicasAreActive`), `SolrTestCaseJ4.java` (randomizing builder), `.github/workflows/validate-changelog.yml`. Also `git diff --stat` for base to head and receipt head to tip.
- From base `3f5d4c5bf8ac`: `dev-docs/changelog.adoc` section 4.
- Live PR 5098: metadata and body through `research/gh.ps1 pr view`; reviews, inline comments and issue comments through `gh.ps1 api` GET endpoints; workflow runs and check runs through GET endpoints.
- Not done: no claim change, no push, no DONE mark, no commit, no build, no test, no gate, no Jira action.
