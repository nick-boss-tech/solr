# Flaky-fix post-PR review round 6, slice 1: SOLR-18530 (PR #5098), final confirmation

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-6.md`, slice 1. Read-only. No PR body edit, comment, review, close, submit-branch edit, Jira write, build, Gradle run, test, Selenium run, gate run, or test-queue run. The only git fetch was the read-only fork fetch of `refs/heads/solr-18530-submit`. This file is the only one written, and it is not committed.

Head check: `git ls-remote origin refs/heads/solr-18530-submit` returns `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`. It equals PR #5098 `headRefOid` and the fetched fork ref.

## Verdict

**STILL OPEN, two items.** Both are one-clause fixes. Apply each to the live body and to `pr-drafts/flaky-fixes/SOLR-18530.md` together.

1. Limits line 43 says the test checks "only" the adds and the activation. The soft commit request also fails the test on error.
2. Line 17 states SolrJ retry behaviour more widely than the code allows.

Everything else checked is SATISFIED. The body and the draft are identical after CR stripping.

## Item table

| Item | Live wording | Source check | Result |
|---|---|---|---|
| 1. Round 5 Limits item | Line 43: "Besides the adds succeeding, the test checks only that replicas become active within 30 seconds." | Head test. The add loop (lines 111 to 116) sits in a try block (108 to 117) and calls `leaderClient.add` with no catch, so a failed add fails `test()`. The activation check is line 130, `ensureAllReplicasAreActive(DEFAULT_COLLECTION, "shard1", 1, 2, 30)`. The 30 is `maxWaitSecs` (helper line 2729), and `maxWaitMs = maxWaitSecs * 1000L` is at 2743. Line 93, `cloudClient.request(request);` (the soft commit, `ACTION.COMMIT`), also has no catch. A failed commit also fails the test, so "only" is not accurate. Line 80, `waitForRecoveriesToFinish(DEFAULT_COLLECTION, true)`, is one more check before the cut. | STILL OPEN |
| 2a. Title | "SOLR-18530: Route adds after the proxy cut in RecoveryAfterSoftCommitTest to the shard leaders first" | Base `3f5d4c5bf8ac` to head changes one file, the test (15 insertions, 5 deletions). The adds after the cut use a shard-leaders-first client. The title matches. | SATISFIED |
| 2b. Failover endpoint with the flag off | Line 15: "the cut replica can remain on the list as a failover target". Line 44: "the cut replica can stay on the failover list; the leader is still tried first." | `AbstractFullDistribZkTestBase.createNewCloudSolrClient` (2431 to 2449) calls `sendUpdatesOnlyToShardLeaders()` (2440). That sets the leaders-updates field, not `directUpdatesToLeadersOnly`. `SolrTestCaseJ4.RandomizingCloudSolrClientBuilder.randomizeCloudSolrClient()` (2517 to 2521) sets `directUpdatesToLeadersOnly` at random (2518). `CloudSolrClient.buildUrlMap` (470 to 481): with the flag off, non-leader replicas are added after the leader, and the leader is put first (481). With the flag on, only the leader is listed. The cut replica is the non-leader (test line 95), and the proxy is closed on it (line 99), so the leader is not cut. | SATISFIED |
| 2c. "of this shard" qualifier | Line 15: "a failed send to a replica of this shard is logged and does not fail the add" | `DistributedZkUpdateProcessor.java` 1229 to 1231 is the comment and the `log.warn` for a failed forward. `shardId` is set from the failing node's shard (1266 and 1270). Lines 1345 to 1348 add the error for the client only when that shard differs from the leader's shard. | SATISFIED |
| 2d. File citations are head-SHA links with claim lines | Links on lines 15, 17, 33 and 47 | Test 102 to 117 (comment, leaders client, adds): accurate. DUZKP 1229 to 1231, 1254 to 1262 and 1345 to 1348: accurate. `LBSolrClient.java` 675 to 680 (non-retryable connect or RequestNotSent branch): accurate for the LB layer. `CheckRetryUnrollTest.java` 97 to 103 (the method asserts `ClosedChannelException` is not retried): accurate. That test covers `SolrCmdDistributor.StdNode`, not SolrJ, and the body does not say otherwise. Changelog 123 to 131: item 3. | SATISFIED for the links. The line 17 wording is STILL OPEN (item 2e). |
| 2e. Retry claim | Line 17: "SolrJ moves an update to another endpoint only when it could not connect or the request was never sent (...). A write that reached the network and then failed is not retried." | The LB half holds (`LBSolrClient.java` 576, 659 to 680). The second sentence is wider than the code. `CloudSolrClient.requestWithRetryOnStaleState` (619) handles an update sent through `request()` (605). When `wasCommError` is true (725 to 727), it re-sends the request (771). `wasCommError` (213 to 217) is true for a `SocketException`, `UnknownHostException` or `RequestNotSentException` in the cause chain. So a socket error after the request left the client is re-sent. A `ClosedChannelException` is not in that list, so the test's case holds. | STILL OPEN |
| 2f. Proof numbers in the receipt | "6 of 6 runs passed"; "5 of 5 runs passed"; "Checked 2026-10-10" | Receipt line 8: the fixed test ran 6 times with 0 failures, the base test ran 5 times with 0 failures, and the gate finished 2026-10-10. Counts and date match. | SATISFIED |
| 2g. Gate head and test identity | "Checked 2026-10-10 at 14868bc..." | The receipt gated head is `98e5368d996518b9f4a94f85d7c2fcd933d3f485`. The tip is `14868bc`, after a changelog-only deletion. `RecoveryAfterSoftCommitTest.java` has blob `cf399bd1244cf7887150c286e66b3adc5def4a6c` at both SHAs. `git diff --stat 98e5368 14868bc` touches only `changelog/unreleased/SOLR-18530.yml` (7 deletions). The head statement holds for the test under test. | SATISFIED |
| 3. Changelog citation | Line 47: "Changelog: none." and the quoted section 4 sentence, linked to `blob/14868bc.../dev-docs/changelog.adoc#L123-L131` | At head, line 123 is the section 4 heading and lines 130 to 131 hold the exemption sentence (the quote breaks at the wrap). Base `3f5d4c5bf8ac` has the same lines and the same blob (`ac6f0504ff55`). No changelog fragment exists at head. | SATISFIED |
| 4. Body versus draft | n/a | See the body-versus-draft section. | SATISFIED |
| 5. Internal vocabulary and section openers | Scan for gate, receipt, ledger, log names, run IDs, seeds, claim, takeover | No hits for those terms. "runs" is used only for test run counts. "pooled connection" and "leaders" are Solr and HTTP terms. "CI configuration" appears only inside the quoted changelog sentence. The five sections (What happens today, What this change does, Proof, A choice to check, Limits) each open with a bold one-line summary. The "### AI assistance" footer does not. It is the fixed template footer, so it is treated as exempt. | SATISFIED |
| 6. Reviewers, automated comments, CI | n/a | See the CI and review state section. | No automated finding to verify |

Carried from rounds 4 and 5: the Proof has no seed or CI seed value (none found in the body). The Limits section opens with a bold one-line summary. Both SATISFIED.

## Whole-body findings

**W1 (blocking). Line 43, Limits.** Live text: "Besides the adds succeeding, the test checks only that replicas become active within 30 seconds."
Fact: the soft commit at head line 93 (`cloudClient.request(request);`) throws on failure, and the start-of-test recovery wait at line 80 is a check too. The "only" claim is false as written.
Fix, in the live body and the draft: replace the sentence with "Besides the adds and the soft commit succeeding, the test checks only that replicas become active. The wait after the cut allows 30 seconds."

**W2 (blocking). Line 17, "What this change does".** Live text: "SolrJ moves an update to another endpoint only when it could not connect or the request was never sent (...). A write that reached the network and then failed is not retried."
Fact: the LB claim is correct. The second sentence is wrong in general. `CloudSolrClient.java` 213 to 217 and 725 to 771 re-send an update after a `SocketException` in the cause chain. The ClosedChannelException conclusion for the test is correct, because `ClosedChannelException` is not in that list.
Fix, in the live body and the draft:
- Change "SolrJ moves an update to another endpoint only when" to "For an update, SolrJ's endpoint failover runs only when".
- Change "A write that reached the network and then failed is not retried." to "A ClosedChannelException is not one of the causes that makes the cloud client resend an update ([CloudSolrClient](https://github.com/nick-boss-tech/solr/blob/14868bc7a7f5ec5c4fa1ade5f894cbda9533307d/solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java#L213-L217))."

Non-blocking notes (the main side decides):

- **N1. Line 15.** "The document count is unchanged" is ambiguous. What is unchanged is the number of documents added after the cut: `MAX_DOCS = 2 + MAX_BUFFERED_DOCS + ULOG_NUM_RECORDS_TO_KEEP` (head line 107; the same expression at base line 101). Optional wording: "The number of documents added after the cut is unchanged, so ...".
- **N2. Line 15.** "when the leader changed during the send (the LeaderChanged case)". The code trigger is that the replica reports it now thinks it is the leader (`DistributedZkUpdateProcessor.java` 1256 to 1260). Optional wording: "when the replica reports that it is now the leader (the LeaderChanged case)". This matches the round 4 and 5 notes.
- **N3. Repeated claim.** "Not shown to fix" appears in the Proof summary (line 23), Proof bullet 3 (line 27), and Limits bullet 1 (line 41, which also restates the Limits summary on line 39). The formula says a claim in a summary is not restated in the body. Optional: drop the Proof bullet 3 sentence "This change is not shown to fix it." and Limits line 41.
- **N4. Sourcing of the symptom text (lines 7 and 9).** The exception text ("IOException occurred when talking to server", cause `ClosedChannelException`) comes from the root-cause record on `origin/pr-prepare` (`reports/flaky-tests-root-cause-round-1-t2.md`, line 25). It is not in the receipt, and no failing CI log is linked. The Jira ticket was not read in this slice, because this agent has no Jira read tool. This is a sourcing note carried from rounds 4 and 5, not a code finding.
- **N5. Footer.** The "### AI assistance" block has no bold summary line. It is fixed template text, so it is treated as exempt.

## Body versus draft

The live body was read with `gh.ps1 pr view 5098 --json body --jq .body`. The draft is `git show origin/pr-prepare:pr-drafts/flaky-fixes/SOLR-18530.md`. Neither contains CR bytes. Both are 5,547 bytes and both end with one newline. `diff` reports no differences.

Differences: none. The fixes for W1 and W2 must go into both.

## CI and review state

- PR #5098: OPEN, draft, `headRefOid` 14868bc7a7f5ec5c4fa1ade5f894cbda9533307d, `mergeStateStatus` UNSTABLE, `reviewDecision` empty.
- `statusCheckRollup` (live): one check, `labeler` (Pull Request Labeler), state SUCCESS.
- Workflow runs at head (read with GET only):
  - Gradle Precommit, run 38079120736: action_required. Not run.
  - Solr Tests via Crave, run 38079120826: action_required. Not run.
  - Validate Changelog, run 38079120887: action_required. Not run. Its exemption regex at `.github/workflows/validate-changelog.yml` line 67 matches the one changed file, `solr/core/src/test/...`, so it should pass once it runs. CI has not confirmed this yet.
  - Pull Request Labeler, run 38079120890: success.
- action_required is an incomplete state (the run is waiting for approval), not a failure.
- Reviews: 0. Issue comments: 0. Inline review comments: 0.

## Automated findings

- Verified: none. No review, inline comment or issue comment exists on PR #5098. The labeler result is a labeling step, not a finding.
- Rejected: none.

## Reads

- `origin/pr-prepare`: `assignments/pool-flaky-fix-post-pr-review-round-6.md`, `pr-formula.md`, `reports/flaky-fix-post-pr-review-round-5.md`, `reports/flaky-fix-post-pr-review-round-5-s1.md`, `receipts/SOLR-18530.md`, `pr-drafts/flaky-fixes/SOLR-18530.md`, `reports/flaky-tests-root-cause-round-1-t2.md`.
- Head `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d` (read with `git show` and `git grep`): `RecoveryAfterSoftCommitTest.java`, `AbstractFullDistribZkTestBase.java`, `SolrTestCaseJ4.java`, `CloudSolrClient.java`, `LBSolrClient.java`, `DistributedZkUpdateProcessor.java`, `CheckRetryUnrollTest.java`, `dev-docs/changelog.adoc`, `.github/workflows/validate-changelog.yml`.
- Base `3f5d4c5bf8ac`: `RecoveryAfterSoftCommitTest.java` (adds at lines 86 and 106) and `dev-docs/changelog.adoc`.
- Live PR 5098: metadata and body through `research/gh.ps1 pr view`; reviews, issue comments, inline comments, workflow runs and check runs through GET endpoints.
