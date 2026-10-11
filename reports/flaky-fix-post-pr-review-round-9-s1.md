# Flaky-fix post-PR review round 9, slice 1: SOLR-18530 (PR #5098), final confirmation

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-9.md`, slice 1. Read-only throughout. No PR body edit, comment, review, close, submit-branch edit, Jira write, build, Gradle run, test, Selenium run, gate run, or test-queue run. No claim was made, as the lead asked. The only fetch was the read-only fork fetch of `refs/heads/solr-18530-submit`. Scratch copies of the live body and the main-side draft were written to the scratchpad, not to the repo. This file is the only one written, and it is not committed.

## Verdict

**SATISFIED.** Both round 8 items are applied in the live body and in `pr-drafts/flaky-fixes/SOLR-18530.md`, and each wording matches the head code. The whole-body read finds no blocking item. Seven optional wording notes (W1 to W7) are listed below. The author may take them or leave them; none changes the verdict.

Head check: `git ls-remote origin refs/heads/solr-18530-submit` returns `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`, which equals PR #5098 `headRefOid`. The read-only fetch ran, and the local ref resolves to the same SHA.

## Round 8 items, live wording and head code

Item A (line 45, Limits), live: "SolrJ still does not retry a `ClosedChannelException` on an update once the update has reached the server. That is a separate product decision."

Item B (line 17, "Our reading" paragraph), live: "Once the update has reached the server, a `ClosedChannelException` is not one of the causes that makes the cloud client resend an update."

Head code, the split between pre-send and post-send failures:

- `solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java`: before the commit callback fires, an IOException is wrapped as a SolrServerException whose cause is `RequestNotSentException` (L486-L488, L514-L516, and L525 for IllegalStateException). After the commit, the wrapper is "IOException occurred when talking to server" with no `RequestNotSentException` (L484-L485, L512-L513). The body's symptom text matches the post-commit message.
- `solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java`: updates are non-retryable (L576). Failover for an update happens only on a connect exception or a `RequestNotSentException` in the chain (L675-L680). Otherwise the exception is rethrown (L682).
- `solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java` `wasCommError` (L213-L217) lists SocketException, UnknownHostException and RequestNotSentException. ClosedChannelException is an IOException, not a SocketException, and it is not listed.

So a pre-send ClosedChannelException is retried through the wrapper, and a post-send one is not. Both sentences are true as written.

## Item table

| Item | Live wording | Source check | Result |
|---|---|---|---|
| A. Limits, line 45 | "SolrJ still does not retry a `ClosedChannelException` on an update once the update has reached the server. That is a separate product decision." | Post-commit path is not retried: HttpJettySolrClient.java L484-L485 and L511-L513 wrap without RequestNotSentException; LBSolrClient.java L672-L682 rethrows (not a connect exception, no wrapper); CloudSolrClient.java L213-L217 does not list it. The sentence is true and scoped. | SATISFIED |
| B. Line 17 | "Once the update has reached the server, a `ClosedChannelException` is not one of the causes that makes the cloud client resend an update" | The qualifier is needed because the pre-send path is retried. Pre-send IOException is wrapped as RequestNotSentException at HttpJettySolrClient.java L486-L488, L514-L516 and L525; LBSolrClient.java L677 and CloudSolrClient.java L216 retry that wrapper. The post-send path is not wrapped. The statement is true. | SATISFIED |
| Line 17, LBSolrClient sentence | "Endpoint failover in `LBSolrClient` moves an update to another endpoint only when it could not connect or the request was never sent" | For updates (LBSolrClient.java L576), failover only on a connect exception or RequestNotSentException (L675-L680). Scoped to LBSolrClient. | SATISFIED |
| Title | "SOLR-18530: Route adds after the proxy cut in RecoveryAfterSoftCommitTest to the shard leaders first" | Base test used `cloudClient` for the post-cut adds (base test L102-L106). Head routes them through a leaders-first client (test L102-L117). "First" holds with the random flag on or off, because CloudSolrClient.java L482 puts the leader first. | SATISFIED |
| Line 15, failover endpoint and random flag | "Whether the leader is the only endpoint is set by the test helper's randomized `directUpdatesToLeadersOnly` setting, so the cut replica can remain on the list as a failover target." Also Limits line 44: "the leader is still tried first." | SolrTestCaseJ4.java L2517-L2518 randomizes `directUpdatesToLeadersOnly`, and the randomizing builder constructors call that method (L2481-L2503). `sendUpdatesOnlyToShardLeaders()` sets only `shardLeadersOnly` (CloudSolrClient.java L1416-L1417), so the random flag is not overridden. AbstractFullDistribZkTestBase.java L2439-L2440 applies it for the leaders-only client. buildUrlMap adds non-leaders only when the flag is false (CloudSolrClient.java L470-L476) and puts the leader first (L482). With the flag off the cut replica stays on the list after the leader. | SATISFIED |
| Line 15, the leader is not cut | "The leader is not cut, so the adds reach it in either setting." | Test L95-L99 cuts the non-leader returned by the helper (AbstractFullDistribZkTestBase.java L2733, L2783). The leader is not cut. | SATISFIED |
| Line 15, "of this shard" first sentence | "a failed send to a replica of this shard is logged and does not fail the add" | DistributedZkUpdateProcessor.java L1229-L1231 is a comment ("we assume if it was added locally, we succeeded") and a `log.warn`. A same-shard replica reaches `errorsForClient` only in the LeaderChanged block (L1254-L1262). The ForwardNode branch (L1219-L1224) is a different case. | SATISFIED |
| Line 15, the two client-error cases | "when the leader changed during the send (the `LeaderChanged` case) ... and when the replica is on another shard" | L1254-L1262 is the LeaderChanged block. L1345-L1348 adds the error when the replica is not in this shard's replica list and its shard differs. A replica on another shard cannot be in `myReplicas`, so the phrase holds. | SATISFIED (wording note W4) |
| Line 15, "The document count is unchanged" | "The document count is unchanged, so the replica still misses more updates than its update log keeps." | MAX_DOCS (test L107) has the same expression as base L101. True, but ambiguous. | SATISFIED (wording note W3) |
| Line 43, soft commit and 30-second wait | "Besides the adds and the soft commit succeeding, the test checks only that replicas become active. The wait after the cut allows 30 seconds." | Soft commit at test L93 has no catch. The post-cut wait is L130 with 30 seconds, converted to 30000 ms at AbstractFullDistribZkTestBase.java L2743. The helper also asserts the replica count, a leader and canBecomeLeader (L2757-L2776). The lead's cited line 96 is a comment; the 30-second waits are at L95 (pre-cut) and L130 (post-cut). | SATISFIED (wording note W1) |
| Line 43, recovery path | "It does not check which recovery path ran. The document count is what rules out peer sync." | The test comment at L100 says the document count is chosen so peer sync cannot be used. Accurate. | SATISFIED |
| Line 33, choice section | "The existing test `CheckRetryUnrollTest` already asserts that `ClosedChannelException` is not retried" | CheckRetryUnrollTest.java L97-L103 asserts it against `SolrCmdDistributor.StdNode.checkRetry` (helper at L56-L60). The layer is the leader-to-replica forward retry, not SolrJ. The test comment says widening is a separate decision. True as worded. | SATISFIED (wording note W2) |
| Line 19, scope | "The change touches only this test. No product code changes." | Base 3f5d4c5bf8ac to head 14868bc: one file, RecoveryAfterSoftCommitTest.java, 15 insertions and 5 deletions. | SATISFIED |
| Proof bullets 1 and 2, counts | "6 of 6 runs passed" and "5 of 5 runs passed" | receipts/SOLR-18530.md on origin/pr-prepare: fixed test 6 runs, 0 failures; unmodified base test 5 runs, 0 failures; gate finished 2026-10-10. Counts and date match. Every Proof number is in the receipt. | SATISFIED |
| Proof bullet 1, head | "Checked 2026-10-10 at 14868bc7a7f5ec5c4fa1ade5f894cbda9533307d." | The receipt's gated head is 98e5368. The test file blob is cf399bd1244c at both SHAs, and the receipt says the later change is the changelog YAML only. Accurate in substance. | SATISFIED (wording note W5) |
| Line 47, changelog | "Changelog: none." with the quoted section 4 sentence and a link | dev-docs/changelog.adoc at head 14868bc: section 4 heading L123, exemption sentence L130-L131. The quote matches word for word. The file blob is ac6f0504ff55 at both 3f5d4c5bf8ac and head, so the file did not change. | SATISFIED |
| File citations (8 links) | All are `github.com/nick-boss-tech/solr/blob/14868bc7a7f5ec5c4fa1ade5f894cbda9533307d/<path>#L<a>-L<b>` | Each range holds its claim: test L102-L117 (line 15); DistributedZkUpdateProcessor L1229-L1231, L1254-L1262, L1345-L1348 (line 15); LBSolrClient L675-L680 (line 17); CloudSolrClient L213-L217 (line 17); CheckRetryUnrollTest L97-L103 (line 33); changelog L123-L131 (line 47). Every path exists at head. | SATISFIED |
| Presentation rule, openers | Five sections | What happens today, What this change does, Proof, A choice to check, Limits each open with a bold one-line summary. The AI header and the Jira link line are present. The "AI assistance" footer has no summary line; the approved template in pr-formula.md gives the footer none, so it is exempt. | SATISFIED |
| Internal vocabulary | Gate, receipt, ledger, takeover, claim, seed, run identifier, internal log name | No hit. "runs" appears only as test-run counts. "logged" and "update log" are product terms. No em dash or en dash in the body. | SATISFIED |

## Whole-body findings

Blocking: none.

Optional notes (author's call; none blocks the verdict):

- **W1, line 43.** "the test checks only that replicas become active" understates the helper. `ensureAllReplicasAreActive` also asserts the replica count (rf), a non-null leader and `canBecomeLeader` (AbstractFullDistribZkTestBase.java L2757-L2776). Option: "the test checks that the replicas become active, with the expected count and a leader".
- **W2, line 33.** `CheckRetryUnrollTest` tests the leader-to-replica forward rule (`SolrCmdDistributor.StdNode.checkRetry`), not the SolrJ rule. The sentence is accurate. Option: name the layer in the sentence.
- **W3, line 15.** "The document count is unchanged" is ambiguous. What is unchanged is the number of documents added after the cut (MAX_DOCS, test L107, same expression as base L101). Option: "The number of documents added after the cut is unchanged".
- **W4, line 15.** "when the leader changed during the send (the LeaderChanged case)". The code at L1254-L1257 fires when the replica reports that it now thinks it is the leader. Option: "when the replica reports that it now thinks it is the leader (the LeaderChanged case)".
- **W5, line 25.** "Checked 2026-10-10 at 14868bc..." The runs were at the receipt's gated head 98e5368. The test file is identical at both SHAs. Option: "Checked 2026-10-10 at 98e5368; the test file is identical at 14868bc7a7f5ec5c4fa1ade5f894cbda9533307d."
- **W6, lines 23, 27, 39, 41.** "not proven" or "not shown to fix" is stated four times, and "test-only" appears in lines 19 and 23. The presentation rule says a claim is stated once in the summary. Option: drop the repeats from the bullets.
- **W7, length.** The body is about 5,924 characters, above the roughly 3,500 guide in pr-formula.md. The ticket does not look unusually complex. Optional.

Not checked: the symptom sentence in "What happens today" (a SolrServerException with a ClosedChannelException cause). Its wording matches the post-commit message at HttpJettySolrClient.java L485 and L512-L513. The receipt does not record the observed error text, and the Jira packet was not read.

## Body versus draft

Identical. After the CR strip (there were no CR bytes in either copy), the live body and `pr-drafts/flaky-fixes/SOLR-18530.md` on origin/pr-prepare are both 52 lines, and `diff` exits 0. No content difference, no trailing-newline difference, no BOM. Both copies carry the line 17 and line 45 fixes.

## CI and review state

- PR #5098: OPEN, draft, headRefName `solr-18530-submit`, headRefOid `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`, mergeStateStatus UNSTABLE, reviewDecision empty.
- `statusCheckRollup` (live): one check, `labeler` (Pull Request Labeler), COMPLETED, SUCCESS (run 38079120890).
- Workflow runs at the head SHA (not in the rollup):
  - Gradle Precommit, run 38079120736: COMPLETED, `action_required`.
  - Solr Tests via Crave, run 38079120826: COMPLETED, `action_required`.
  - Validate Changelog, run 38079120887: COMPLETED, `action_required`.
  - Pull Request Labeler, run 38079120890: COMPLETED, SUCCESS.
- The three `action_required` runs have no jobs. `action_required` is an incomplete state (the runs wait for approval), not a failure. No code result exists for them yet.
- Reviews: 0. Issue comments: 0. Inline review comments: 0.

## Verified and rejected automated findings

- Verified: none. No review, inline comment or issue comment exists, so there is no automated finding to check. The labeler check only labels.
- Rejected: none.
- My own reading, not an automated finding: `.github/workflows/validate-changelog.yml` L67 exempts paths matching `^solr/.*/test`, which matches the one changed file. L73-L75 exit 0 when there are no code changes. The check should pass when it runs. It has not run.

## Not done

No PR body edit, comment, review, close, branch edit or Jira write. No build, Gradle, test, Selenium, gate or test-queue run. The gate log and the seeds on vm1 were not read. The CI logs were not read.
