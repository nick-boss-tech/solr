# Flaky-fix post-PR review round 8, slice 1: SOLR-18530 (PR #5098), final confirmation

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-8.md`, slice 1. Read-only throughout. No PR body edit, comment, review, close, submit-branch edit, Jira write, build, Gradle run, test, Selenium run, gate run, or test-queue run. No claim was made, as the lead asked. No fetch was run: the tip was confirmed with `git ls-remote`, and the head commit is already present locally. Temporary copies of the live body and the draft were written to the scratchpad, not to the repo. This file is the only one written, and it is not committed. Round 7 slice 1 item 10 is corrected below.

## Verdict

**STILL OPEN: 2 items, both on the ClosedChannelException retry wording.** The round 7 failover item is SATISFIED.

Head check: `git ls-remote origin refs/heads/solr-18530-submit` returns `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`, which equals PR #5098 `headRefOid`.

Remaining items. Change the live body and `pr-drafts/flaky-fixes/SOLR-18530.md` together:

1. **Line 45, Limits.** Live: "SolrJ still does not retry a `ClosedChannelException` on an update. That is a separate product decision." Fact: true only after the request has reached the server. If the request was never sent, SolrJ does retry it (see the chain below). Fix: "SolrJ still does not retry a `ClosedChannelException` on an update once the update has reached the server. That is a separate product decision."
2. **Line 17, second sentence of the "Our reading" paragraph.** Live: "A `ClosedChannelException` is not one of the causes that makes the cloud client resend an update" (link to CloudSolrClient L213-L217). Fact: literally true of the three listed causes. But it reads as a general rule, and a `ClosedChannelException` raised before the send is wrapped as `RequestNotSentException`, which the cloud client does resend. Fix: "Once the update has reached the server, a `ClosedChannelException` is not one of the causes that makes the cloud client resend an update" (keep the existing link).

Why items 1 and 2 are open (source chain at head 14868bc):

- `solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java` L479-L489 and L511-L516: an IOException before the request commits becomes `SolrServerException("Connection failed before the request was sent ...", new RequestNotSentException(...))`. After commit (L485, L513) it becomes "IOException occurred when talking to server", with no `RequestNotSentException`.
- `solr/solrj/src/java/org/apache/solr/client/solrj/impl/LBSolrClient.java` L675-L680: for an update, a SolrServerException fails over to another endpoint when `SolrException.hasCause(e, RequestNotSentException.class)`. So a pre-send ClosedChannelException is retried on another endpoint.
- `solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java` L213-L216: `wasCommError` includes `RequestNotSentException`, so the cloud client re-sends (L725-L727, L771). The chain survives both direct-update branches: the sequential branch wraps with `new SolrServerException(e)` (L374), and the parallel branch's `RouteException` keeps the first throwable as its cause (L1715).
- After commit: LB rethrows (L682) and `wasCommError` is false, so the post-commit case is not retried. The Limits sentence is right for that case only.

Round 7 correction: round 7 slice 1 item 10 marked the Limits "SolrJ still does not retry" sentence SATISFIED on the post-commit path only. The pre-send path was missed.

## Item table

| Item | Live wording | Source check | Result |
|---|---|---|---|
| 1. Round 7 failover item (line 17, first sentence) | "Endpoint failover in `LBSolrClient` moves an update to another endpoint only when it could not connect or the request was never sent ([LBSolrClient](...#L675-L680))." | LBSolrClient.java at head: for updates, HTTP-code failures are not retried (L644-L657); a SocketException is rethrown unless it is a ConnectException (L658-L663); a timeout is rethrown (L665-L669); a SolrServerException fails over only when the root cause is a connect exception or the chain holds RequestNotSentException (L675-L683). The sentence matches that rule and is scoped to LBSolrClient. | SATISFIED |
| 2. Other failover or retry sentences (lines 15, 33, 44) | "the cut replica can remain on the list as a failover target" (15); "CheckRetryUnrollTest already asserts that ClosedChannelException is not retried" (33); "the cut replica can stay on the failover list; the leader is still tried first" (44) | Lines 15 and 44 describe this test's client list. buildUrlMap (CloudSolrClient.java L470-L476, L482) keeps non-leaders as failovers only when directUpdatesToLeadersOnly is false. Line 33: the test's `retries` helper calls `SolrCmdDistributor.StdNode.checkRetry` (CheckRetryUnrollTest.java L56-L59), the leader-to-replica forward rule, not SolrJ. Accurate as worded; see N-c. | SATISFIED |
| 3. Limits, line 45 ("SolrJ still does not retry ...") | "SolrJ still does not retry a `ClosedChannelException` on an update. That is a separate product decision." | Pre-send path is retried (chain above). The sentence is SolrJ-wide and overstates the code. | STILL OPEN (fix 1) |
| 4. Line 17, second sentence ("A `ClosedChannelException` is not one of the causes ...") | "A `ClosedChannelException` is not one of the causes that makes the cloud client resend an update" | Literally true of the list at CloudSolrClient L213-L216. A pre-send ClosedChannelException is wrapped as RequestNotSentException, which is in that list, so the cloud client does resend it. | STILL OPEN (fix 2) |
| 5. Title | "SOLR-18530: Route adds after the proxy cut in RecoveryAfterSoftCommitTest to the shard leaders first" | The base 3f5d4c5bf8ac to head diff changes one file (RecoveryAfterSoftCommitTest.java, 15 insertions, 5 deletions). The post-cut client lists the leader first in both flag settings (buildUrlMap L463-L482). | SATISFIED |
| 6. Bold routing line (line 13) | "After the cut, this test sends its adds to the shard leaders first." | Test L108-L116: post-cut adds use the leaders-only client. The adds before the cut use `cloudClient`. | SATISFIED |
| 7. Failover endpoint with the random flag off (lines 15, 44) | "the cut replica can remain on the list as a failover target"; "the leader is still tried first" | AbstractFullDistribZkTestBase.java L2439-L2440 calls `sendUpdatesOnlyToShardLeaders()`, which sets `shardLeadersOnly` (CloudSolrClient.java L1416-L1417). SolrTestCaseJ4.java L2517-L2518 sets `directUpdatesToLeadersOnly` at random, a separate field (CloudSolrClient.java L1297, L1444). So the random flag is not overridden. With the flag off, buildUrlMap lists non-leaders after the leader (L470-L476, L482). The cut replica is a non-leader (`ensureAllReplicasAreActive` returns non-leaders, AbstractFullDistribZkTestBase.java L2733; test L95), and the proxy is closed on it, not on the leader (L97-L99). | SATISFIED |
| 8. "of this shard" qualifier (line 15) | "a failed send to a replica of this shard is logged and does not fail the add (DistributedZkUpdateProcessor L1229-L1231)"; "the error is returned to the client ... when the replica is on another shard (L1345-L1348)" | L1229-L1231 are a comment and `log.warn` only. A same-shard replica still in the replica list takes the L1310-L1335 branch, which logs only. A replica not in the list gets a client error only when its shard differs (L1345-L1348). The LeaderChanged case (L1254-L1262) is the other client error. The "two cases" summary is fair for leader-to-replica sends. | SATISFIED |
| 9. Soft commit and 30-second wait (line 43) | "Besides the adds and the soft commit succeeding, the test checks only that replicas become active. The wait after the cut allows 30 seconds." | Soft commit at test L93 (`cloudClient.request(request)`) has no catch, so a failure fails the test. The post-cut wait is L130, `ensureAllReplicasAreActive(..., 30)`; the helper converts 30 to 30000 ms (AbstractFullDistribZkTestBase.java L2743). The lead's cited line 96 is a comment. The other 30-second wait is at L95 (pre-cut). The helper also asserts replica count, a leader and canBecomeLeader (L2757-L2776); see N-a. | SATISFIED |
| 10. Changelog citation (line 47) | "Changelog: none." with the section 4 exemption sentence, linked to `dev-docs/changelog.adoc` at 14868bc L123-L131 | Heading at L123. The exemption sentence at L130-L131 matches the quote word for word. The file blob is ac6f0504ff55 at both base and head, so the file did not change. | SATISFIED |
| 11. File citations (eight links) | All links are `github.com/nick-boss-tech/solr/blob/14868bc7a7f5ec5c4fa1ade5f894cbda9533307d/<path>#L<a>-L<b>` | Each range holds its claim: test L102-L117; DistributedZkUpdateProcessor L1229-L1231, L1254-L1262, L1345-L1348; LBSolrClient L675-L680; CloudSolrClient L213-L217; CheckRetryUnrollTest L97-L103; changelog L123-L131. | SATISFIED |
| 12. Proof numbers (Proof bullets 1 and 2) | "6 of 6 runs passed. Checked 2026-10-10"; "5 of 5 runs passed" | Receipt: fixed test 6 runs, 0 failures; unmodified base test 5 runs, 0 failures; gate finished 2026-10-10. Counts and date match. | SATISFIED |
| 13. Head named in the Proof bullet | "Checked 2026-10-10 at 14868bc7a7f5ec5c4fa1ade5f894cbda9533307d" | The receipt's gated head is 98e5368. The test blob (cf399bd1244c...) is identical at 98e5368 and 14868bc. The receipt says the only later change is the changelog YAML. Accurate in substance; see N-b. | SATISFIED |
| 14. Scope statement (line 19 area) | "The change touches only this test. No product code changes." | Base-to-head diff: one file, the test. | SATISFIED |

## Whole-body findings

**Blocking (both on the same ClosedChannelException point):** items 3 and 4 above.

**Non-blocking (author's call):**

- **N-a. Line 43.** "the test checks only that replicas become active" leaves out the helper's replica-count, leader and canBecomeLeader asserts (AbstractFullDistribZkTestBase.java L2757-L2776). Optional: "the test checks only that the replicas become active, with the expected count and a leader".
- **N-b. Proof bullet 1.** "Checked 2026-10-10 at 14868bc..." The runs were made at the receipt's gated head 98e5368. The test file is identical at both SHAs and the only later change is the changelog YAML, so the statement is accurate in substance. Optional: name 98e5368 and note that the test file is unchanged at 14868bc.
- **N-c. Line 33.** CheckRetryUnrollTest's `retries` helper exercises `SolrCmdDistributor.StdNode.checkRetry`, the leader-to-replica forward rule, not the SolrJ rule. The sentence is accurate. Optional: name the layer, so no reader takes it for SolrJ (carried from round 7 N5).
- **N-d. Line 15.** "The document count is unchanged" is ambiguous. What is unchanged is the number of documents added after the cut (`MAX_DOCS`, test L107). Optional rewording (carried from rounds 6 and 7).
- **N-e. Line 15.** "when the leader changed during the send (the `LeaderChanged` case)". The code (L1254-L1257) fires when the replica reports that it now thinks it is the leader. Optional rewording (carried from rounds 4 to 7).
- **N-f. Repetition.** "not proven" or "not shown to fix" appears four times: Proof summary (line 23), Proof bullet 3 (line 27), Limits summary (line 39), Limits bullet 1 (line 41). The presentation rule says a claim is stated once. Optional.
- **N-g. Length.** About 5,850 characters, against the roughly 3,500 guide in `pr-formula.md`. The ticket does not look unusually complex. Optional.
- **N-h. Footer.** "### AI assistance" has no bold summary line. The pr-formula template gives the footer none, so it is exempt.

## Body versus draft

After CR and BOM removal, the live body and `pr-drafts/flaky-fixes/SOLR-18530.md` on `origin/pr-prepare` are identical (52 lines each). The only other difference is one trailing newline added by the read path. There are no content differences. Both fixes in items 3 and 4 must go into both files.

## Vocabulary and section openers

- No hit for gate, receipt, ledger, takeover, claim, seed, run identifier, or internal log name. "runs" appears only in test-run counts. "log" appears only as product logging or as "update log" (the replica's update log, a product term).
- No em dash and no en dash in the body.
- The five sections (What happens today, What this change does, Proof, A choice to check, Limits) each open with a bold one-line summary. The AI header and the Jira link line are present.

## CI and review state (read-only GET calls)

- PR #5098: OPEN, draft, headRefName `solr-18530-submit`, headRefOid `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`, mergeStateStatus UNSTABLE, reviewDecision empty.
- `statusCheckRollup` (live): one check, `labeler` (Pull Request Labeler), COMPLETED, SUCCESS (run 38079120890).
- Workflow runs at head (`actions/runs?head_sha=`):
  - Gradle Precommit, run 38079120736: COMPLETED, `action_required`.
  - Solr Tests via Crave, run 38079120826: COMPLETED, `action_required`.
  - Validate Changelog, run 38079120887: COMPLETED, `action_required`.
  - Pull Request Labeler, run 38079120890: COMPLETED, SUCCESS.
- `action_required` is an incomplete state (the runs wait for approval), not a failure. No code result exists yet for those three runs.
- Reviews: 0. Issue comments: 0. Inline review comments: 0.

## Verified and rejected automated findings

- Verified: none. No review, inline comment or issue comment exists, so there is no automated finding to check. The labeler check only labels.
- Rejected: none.
- My own reading, not an automated finding: `.github/workflows/validate-changelog.yml` L67 exempts paths matching `^solr/.*/test`, which covers the only changed file, so the changelog check should pass once it runs. It has not run.

## Not checked

- Jira SOLR-18530 was not read. No CI log was read. No run was executed. The receipt's seeds and the gate log on vm1 were not read.
