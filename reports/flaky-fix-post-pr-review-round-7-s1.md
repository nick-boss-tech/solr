# Flaky-fix post-PR review round 7, slice 1: SOLR-18530 (PR #5098), final confirmation

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-7.md`, slice 1. Read-only. No PR body edit, comment, review, close, submit-branch edit, Jira write, build, Gradle run, test, Selenium run, gate run, or test-queue run. The only git fetch was the read-only fork fetch of `refs/heads/solr-18530-submit`. No claim was made, as the lead asked. This file is the only one written, and it is not committed.

Head check: `git ls-remote origin refs/heads/solr-18530-submit` returns `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`. It equals PR #5098 `headRefOid` and the fetched fork ref.

## Verdict

**STILL OPEN, one item.** Line 17 of the live body, first sentence of the paragraph that starts "Our reading of the cause":

- Live: "SolrJ moves an update to another endpoint only when it could not connect or the request was never sent ([LBSolrClient](...#L675-L680))."
- Fact: accurate for `LBSolrClient`, but not for SolrJ as a whole. `CloudSolrClient` re-sends an update after a `SocketException`, and a socket error can happen after the request has left the client. The body's own next sentence cites those `CloudSolrClient` lines, so the two sentences read as conflicting.
- Fix, in the live body and in `pr-drafts/flaky-fixes/SOLR-18530.md` together: replace "SolrJ moves an update to another endpoint only when" with "Endpoint failover in LBSolrClient moves an update to another endpoint only when". Keep the `LBSolrClient` link at L675-L680.

The round 6 narrowing (the `ClosedChannelException` sentence) is in place and accurate. The round 6 Limits item is in place and accurate. Everything else checked is SATISFIED. The fix above is the round 6 slice 1 W2 first-bullet request (scope of the SolrJ sentence), which was not applied.

## Item table

| Item | Live wording | Source check | Result |
|---|---|---|---|
| 1. Round 6 item 1: Limits, soft commit and 30-second wait (line 43) | "Besides the adds and the soft commit succeeding, the test checks only that replicas become active. The wait after the cut allows 30 seconds." | Head `RecoveryAfterSoftCommitTest.java`. The soft commit, `cloudClient.request(request);` at line 93, has no catch, so a failure fails `test()`. Pre-cut adds at line 87 and post-cut adds at lines 111 to 116 (`leaderClient.add`, inside the try at 108 to 117) have no catch. Activation checks: line 95 (pre-cut) and line 130 (post-cut), both `ensureAllReplicasAreActive(..., 30)`. Line 80 is `waitForRecoveriesToFinish`. The lead's "activation check at line 96" is off: line 96 is a comment. The 30 is `maxWaitSecs`, giving `maxWaitMs = maxWaitSecs * 1000L` (`AbstractFullDistribZkTestBase.java` 2728 to 2743). The helper also asserts that the replica count equals rf, that a leader is non-null, and that each replica can become leader (2757 to 2776). So "only that replicas become active" is a shorthand that leaves out those shape checks. It does not make the claim false. See N1. | SATISFIED |
| 2. Round 6 item 2: retry claim, sentence A (line 17) | "SolrJ moves an update to another endpoint only when it could not connect or the request was never sent ([LBSolrClient] L675-L680)." | `LBSolrClient.java`: an update is non-retryable (576). A `SocketException` is rethrown unless it is a `ConnectException` (658 to 663). A `SolrServerException` is failed over only when its root cause is a connect exception or `RequestNotSentException` (675 to 683). That is accurate for the LB layer. At SolrJ level it is wider than the code: `LBSolrClient` rethrows a `SocketException` for an update (662), `CloudSolrClient.requestWithRetryOnStaleState` catches it (725 to 727, `wasCommError` true), and re-sends the update (771). So "SolrJ ... only when" conflicts with `CloudSolrClient` 213 to 217 and 725 to 771, which the body cites in the next sentence. See W1. | STILL OPEN |
| 2. Round 6 item 2: sentence B (line 17) | "A `ClosedChannelException` is not one of the causes that makes the cloud client resend an update ([CloudSolrClient] L213-L217)." | `wasCommError` (`CloudSolrClient.java` 213 to 217) is true only when the cause chain holds a `SocketException`, `UnknownHostException` or `RequestNotSentException`. `SolrException.hasCause` uses `isInstance`. `ClosedChannelException` is an `IOException`, not a `SocketException`, so it is not in the set. `wasCommError` gates the re-send at 725 to 771. Nothing in `solr/solrj/src/java` or `solr/solrj-jetty/src/java` names `ClosedChannelException`. The narrowed claim is accurate. It does not say that all failed writes are not retried. | SATISFIED |
| 3. Title | "SOLR-18530: Route adds after the proxy cut in RecoveryAfterSoftCommitTest to the shard leaders first" | Base `3f5d4c5bf8ac` to head changes one file, `solr/core/src/test/org/apache/solr/cloud/RecoveryAfterSoftCommitTest.java` (15 insertions, 5 deletions). The post-cut adds use a client built with `sendUpdatesOnlyToShardLeaders()`. `buildUrlMap` (`CloudSolrClient.java` 470 to 482) puts the leader first in both flag settings. The title is accurate. | SATISFIED |
| 4. Failover endpoint when the random flag is off (line 15 and Limits line 44) | "the cut replica can remain on the list as a failover target" (line 15); "the cut replica can stay on the failover list; the leader is still tried first" (line 44) | `createNewCloudSolrClient` (`AbstractFullDistribZkTestBase.java` 2431 to 2449) calls `sendUpdatesOnlyToShardLeaders()` (2440), which sets only the leaders-only field. `RandomizingCloudSolrClientBuilder.randomizeCloudSolrClient()` (`SolrTestCaseJ4.java` 2517 to 2521) sets `directUpdatesToLeadersOnly = random().nextBoolean()` (2518). `buildUrlMap` (`CloudSolrClient.java` 445 to 482): with the flag off, non-leaders are added after the leader (470 to 476, 482). With the flag on, only the leader is listed (463 to 464). `directUpdate` (245 to 299) builds its routes from `buildUrlMap`, so the failover list applies to the update path. The cut replica is a non-leader (test line 95 `notLeader`), and the proxy is closed on it (line 99), so the leader is not cut. | SATISFIED |
| 5. "of this shard" qualifier (line 15) | "a failed send to a replica of this shard is logged and does not fail the add" ([DUZKP] L1229-L1231) | Lines 1229 to 1231 are the comment and `log.warn` for a failed forward to a replica. For a same-shard replica found in the leader's replica list, no client error is added (1338 to 1350). The only same-shard client error is the `LeaderChanged` case (1254 to 1262). The error goes to the client when the failing node is not in this shard's replica list and its shard differs from the leader's (1298 to 1308, 1338 to 1348). The body's "when the replica is on another shard" is a shorthand for that condition. Accurate. | SATISFIED |
| 6. File citations | Eight links, all `blob/14868bc7a7f5ec5c4fa1ade5f894cbda9533307d/...` | Each range holds its claim. Test L102-L117 (comment 102 to 106, leaders client 108 to 110, adds 111 to 116). DUZKP L1229-L1231, L1254-L1262, L1345-L1348 (same lines as above). `LBSolrClient` L675-L680 (675 to 680). `CloudSolrClient` L213-L217 (213 to 217). `CheckRetryUnrollTest` L97-L103 (method and two asserts). `dev-docs/changelog.adoc` L123-L131 (heading at 123, exemption at 130 to 131). | SATISFIED |
| 7. Proof numbers (lines 25 and 26) | "6 of 6 runs passed. Checked 2026-10-10 at 14868bc..."; "5 of 5 runs passed" | Receipt: fixed test 6 runs, 0 failures; base test 5 runs, 0 failures; gate finished 2026-10-10. Counts and date match. | SATISFIED |
| 8. Gate head and test identity (line 25) | "Checked 2026-10-10 at 14868bc..." | Receipt gated head is `98e5368d996518b9f4a94f85d7c2fcd933d3f485`. `git diff --stat 98e5368 14868bc` touches only `changelog/unreleased/SOLR-18530.yml` (7 deletions). `RecoveryAfterSoftCommitTest.java` has blob `cf399bd1244cf7887150c286e66b3adc5def4a6c` at both SHAs. The head statement is accurate for the test under test. The body does not name 98e5368, and it need not. | SATISFIED |
| 9. Changelog citation (line 47) | "Changelog: none." with the section 4 exemption sentence, linked to `dev-docs/changelog.adoc` at 14868bc L123-L131 | `dev-docs/changelog.adoc` blob `ac6f0504ff55` at head and at base. Heading at 123 (`== 4. Changelog Validation in Pull Requests`). Exemption sentence at 130 to 131 (the quote joins the wrapped lines). No `changelog/unreleased/SOLR-18530.yml` at head. | SATISFIED |
| 10. Limits: no retry (line 45) | "SolrJ still does not retry a `ClosedChannelException` on an update." | No `ClosedChannelException` handling in `solr/solrj/src/java` or `solr/solrj-jetty/src/java`. `HttpJettySolrClient.java` (471 to 476) marks a write as committed once its request is on the wire, and the failure paths at 484 to 522 do not retry it. The LB and cloud rules above exclude it. Accurate. | SATISFIED |
| 11. Symptom and mechanism (lines 5 to 9 and 17) | "SolrServerException: IOException occurred when talking to server at ...", cause `ClosedChannelException` | The wrapping text is in `solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java` at lines 485 and 513 at head. The root-cause record on `origin/pr-prepare` (`reports/flaky-tests-root-cause-round-1-t2.md`, line 25) gives the mechanism at about 80 percent confidence. The body says "Our reading of the cause, which these runs do not prove." Accurate and hedged. | SATISFIED |

## Whole-body findings

**W1 (blocking). Line 17, first sentence.** Fix as in the verdict. This is the only blocking item.

Non-blocking notes (the main side decides):

- **N1. Line 43, Limits.** "the test checks only that replicas become active" leaves out the helper's replica-count, leader and `canBecomeLeader` assertions (`AbstractFullDistribZkTestBase.java` 2757 to 2776). Optional: "the test checks only that the replicas become active, with the expected replica count and a leader".
- **N2. Line 15.** "The document count is unchanged" is ambiguous. What is unchanged is the number of documents added after the cut (`MAX_DOCS`, test line 107). Optional: "The number of documents added after the cut is unchanged". Carried from round 6 N1.
- **N3. Line 15.** "when the leader changed during the send (the `LeaderChanged` case)". The code (`DistributedZkUpdateProcessor.java` 1254 to 1260) tests the cause the replica reports when it now thinks it is the leader. Optional: "when the replica reports that it is now the leader (the `LeaderChanged` case)". Carried from rounds 4 to 6.
- **N4. Lines 27, 41 and 39 (with the summary at 23).** "not shown to fix" or "not proven" is stated four times (Proof summary, Proof bullet 3, Limits summary, Limits bullet 1). The presentation rule says a claim is stated once in the summary. Optional: drop Proof bullet 3's second sentence and Limits bullet 1. Carried from round 6 N3.
- **N5. Line 33, A choice to check.** It cites `CheckRetryUnrollTest`, which exercises `SolrCmdDistributor.StdNode.checkRetry` (the core leader-to-replica forward rule), not SolrJ. The sentence "changing that rule" does not name the layer, so a reader may take it as the SolrJ rule. Optional: name the layer, for example "the forward retry rule in `SolrCmdDistributor`".
- **N6. Length.** About 5,800 characters, against the roughly 3,500 guide in `pr-formula.md`. The ticket does not look unusually complex.
- **N7. Cosmetic.** Two blank lines (48 and 49) before "### AI assistance". The template has one.

Whole-body checks that passed:

- Every file citation is a link at the head SHA, with its lines holding the claim (item 6).
- The title, the flag-off failover wording, the "of this shard" qualifier, the Proof numbers and the changelog citation are accurate (items 3 to 5, 7 to 9).
- The body contains no em dash or en dash.

## Body versus draft

Compared the live body (`gh.ps1 pr view 5098 --json body --jq .body`) with `pr-drafts/flaky-fixes/SOLR-18530.md` on `origin/pr-prepare`, after removing CR. The read path added a UTF-8 BOM and one extra trailing newline. After removing those, the two are identical, 52 lines each.

Differences: none in content. The fix in W1 must go into both.

## Vocabulary and section openers

- No hits for gate, receipt, ledger, takeover, claim, seed, run identifier, or internal log name. "runs" appears only for test-run counts. "log" and "logged" describe product logging. "pooled connection" is a product term.
- All five sections (What happens today, What this change does, Proof, A choice to check, Limits) open with a bold one-line summary.
- "### AI assistance" has no bold summary. It is the fixed template footer and is treated as exempt.
- The AI header and the Jira link line are present, as the template requires.

## CI and review state

- PR #5098: OPEN, draft (`isDraft` true), `headRefOid` `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`, `mergeStateStatus` UNSTABLE, `reviewDecision` empty.
- `statusCheckRollup` (live): one check, `labeler` (Pull Request Labeler), COMPLETED, SUCCESS (run 38079120890).
- Check runs at head: `labeler` only, SUCCESS.
- Workflow runs at head (GET only):
  - Gradle Precommit, run 38079120736: COMPLETED, `action_required`. Not run.
  - Solr Tests via Crave, run 38079120826: COMPLETED, `action_required`. Not run.
  - Validate Changelog, run 38079120887: COMPLETED, `action_required`. Not run. The only file changed from base to head is `solr/core/src/test/org/apache/solr/cloud/RecoveryAfterSoftCommitTest.java`. It matches the exemption pattern `^solr/.*/test` at `.github/workflows/validate-changelog.yml` line 67, so the run should pass when it runs. This is my reading. CI has not reported.
  - Pull Request Labeler, run 38079120890: COMPLETED, SUCCESS.
- `action_required` is an incomplete state (the runs wait for approval), not a failure.
- Reviews: 0. Issue comments: 0. Inline review comments: 0.

## Automated findings

- Verified: none. No review, inline comment or issue comment exists on PR #5098. The labeler result is a labeling step, not a finding.
- Rejected: none.

## Not checked

- Jira SOLR-18530 was not read. This agent has no Jira read tool, and no Jira write was made.
- No CI log was read. No run was executed.
- The receipt's seeds and the gate log on vm1 were not checked. The Proof numbers come from the receipt's counts only.

## Reads

- `origin/pr-prepare`: `assignments/pool-flaky-fix-post-pr-review-round-7.md`, `pr-formula.md`, `reports/flaky-fix-post-pr-review-round-6.md`, `reports/flaky-fix-post-pr-review-round-6-s1.md`, `receipts/SOLR-18530.md`, `pr-drafts/flaky-fixes/SOLR-18530.md`, `reports/flaky-tests-root-cause-round-1-t2.md`.
- Head `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d` (read with `git show`): `RecoveryAfterSoftCommitTest.java`, `CloudSolrClient.java`, `LBSolrClient.java`, `DistributedZkUpdateProcessor.java`, `SolrCmdDistributor.java`, `CheckRetryUnrollTest.java`, `AbstractFullDistribZkTestBase.java`, `SolrTestCaseJ4.java`, `HttpJettySolrClient.java` (solrj-jetty), `SolrException.java`, `dev-docs/changelog.adoc`, `.github/workflows/validate-changelog.yml`.
- Base `3f5d4c5bf8ac`: `dev-docs/changelog.adoc` (blob check) and the one-file diff.
- Gate `98e5368d996518b9f4a94f85d7c2fcd933d3f485`: `RecoveryAfterSoftCommitTest.java` blob identity and the diff to head.
- Live PR 5098: metadata and body through `research/gh.ps1 pr view`; reviews, issue comments, inline comments, check runs and workflow runs through GET endpoints only.
