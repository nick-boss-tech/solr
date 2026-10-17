# Flaky-fix post-PR review round 6: final confirmation

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-6.md`. Claim: `claims/pool-flaky-fix-post-pr-review-round-6.md`. Lead: the windows review agent. Part reports: `reports/flaky-fix-post-pr-review-round-6-s1.md` (SOLR-18530, PR #5098), `-s2.md` (SOLR-18531, PR #5101) and `-s3.md` (SOLR-18532, PR #5100).

All three slices are active, and all three heads equal their fork tips and PR heads. Read-only throughout. Nothing was posted, edited on a PR, or pushed to a submit branch. No build, Gradle, test or gate run.

## Verdicts

| Slice | PR | Head | Verdict |
|---|---|---|---|
| 1, SOLR-18530 | #5098 (draft) | `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d` | STILL OPEN: 2 items |
| 2, SOLR-18531 | #5101 (draft) | `c8a67c7267d00e33ad8323c683c040b539989b0d` | SATISFIED |
| 3, SOLR-18532 | #5100 (draft) | `7dfd3d98d0a2f016c520139cbca96ea4a49f6689` | STILL OPEN: 1 item |

Nick marks the draft PRs ready only when all three are SATISFIED. Two are not yet.

## Slice 1: SOLR-18530 (PR #5098)

Still open:

1. **Limits, line 43.** The sentence "the test checks only that replicas become active within 30 seconds" is wrong. The soft commit after the initial adds is a `cloudClient.request` (`RecoveryAfterSoftCommitTest.java` line 93) that fails the test on error. The lead read the line at the head. Suggested fix, in the live body and the draft together: "Besides the adds and the soft commit succeeding, the test checks only that replicas become active. The wait after the cut allows 30 seconds."
2. **Line 17.** "A write that reached the network and then failed is not retried" is too broad. `CloudSolrClient` retries on `SocketException` (`CloudSolrClient.java` line 214, and the retry path near lines 725 to 771). The narrower and accurate claim is about `ClosedChannelException`, which is not in that list. The test's conclusion still holds. Suggested fix: narrow the sentence to `ClosedChannelException`.

Satisfied: the citations, the title, the routing wording with the random flag off, the "of this shard" qualifier, the receipt numbers, the gate head against the test file's blob identity, and the changelog citation.

## Slice 2: SOLR-18531 (PR #5101)

Satisfied. The round 5 items (the Linux qualifier, "while the runner is still in the cluster", and the changelog citation as a head-SHA link) are in place. Both new Limits lines match the head code, and the gate status matches the receipt (GREEN at `c8a67c7`). Every Proof number traces to the receipt. The live body equals the draft after CR stripping.

Non-blocking notes from the subagent, not verdict items: the body runs to about 6,300 characters against the roughly 3,500 guide, one sentence says "its own socket" while another Limits line says something close, and the Proof does not name the platform.

## Slice 3: SOLR-18532 (PR #5100)

Still open:

1. **Live line 15 and draft line 15.** The second link, "The change is in RestoreCore.java", points at `RestoreCore.java#L213-L226`. That range stops before the write-back and delete code, which runs from about line 296 to line 315 at the head. The lead read those lines at `7dfd3d98`. Suggested fix, in the live body and the draft together: change the range to `#L213-L314`.

Satisfied: the duplicated paragraph is gone, the install-tests bullet ends at "seen in the install tests", the Proof numbers and four test names match the receipt, the title and changelog are accurate, and the Limits statements hold at the head.

## Not done

No PR body edit, comment, review, close, branch edit or Jira write. No build, Gradle, test or gate run. The subagents did not commit their part reports, and the lead commits them with this roll-up.

The CI state on all three PRs is the same: only the labeler check has run (success). The other checks are `action_required` and have not run. That is a state, not a code finding.
