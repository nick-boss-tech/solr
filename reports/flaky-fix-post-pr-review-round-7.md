# Flaky-fix post-PR review round 7: final confirmation for the two remaining slices

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-7.md`. Claim: `claims/pool-flaky-fix-post-pr-review-round-7.md`. Lead: the windows review agent. Part reports: `reports/flaky-fix-post-pr-review-round-7-s1.md` (SOLR-18530, PR #5098) and `-s2.md` (SOLR-18532, PR #5100). SOLR-18531 keeps its round 6 satisfaction; its head has not moved and it is not re-checked here.

Both heads equal their fork tips. Read-only throughout. Nothing was posted, edited on a PR, or pushed to a submit branch. No build, Gradle, test or gate run.

## Verdicts

| Slice | PR | Head | Verdict |
|---|---|---|---|
| 1, SOLR-18530 | #5098 (draft) | `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d` | STILL OPEN: 1 item |
| 2, SOLR-18532 | #5100 (draft) | `7dfd3d98d0a2f016c520139cbca96ea4a49f6689` | STILL OPEN: 1 item |

The main side marks all three draft PRs ready only when every slice is SATISFIED. SOLR-18531 is satisfied from round 6. Neither of the two slices above is.

## Slice 1: SOLR-18530 (PR #5098)

Satisfied: the round 6 Limits sentence about the soft commit and the 30-second wait, and the round 6 sentence that names `ClosedChannelException`. The rest of the final read is satisfied too. The body matches the draft after CR stripping.

Still open:

1. **Body line 17, first sentence (and the draft).** "SolrJ moves an update to another endpoint only when it could not connect or the request was never sent" is accurate for `LBSolrClient`, but it is not true of SolrJ as a whole. `CloudSolrClient` re-sends an update after a `SocketException` (`CloudSolrClient.java` line 214, and the retry path near lines 725 to 771). The lead confirmed the `SocketException` entry at line 214. Suggested fix, in the live body and the draft together: "Endpoint failover in `LBSolrClient` moves an update to another endpoint only when it could not connect or the request was never sent."

## Slice 2: SOLR-18532 (PR #5100)

Satisfied: the round 6 citation item. Line 15 and the draft both link `RestoreCore.java#L213-L314`, which covers the read, write-back and delete code. The Proof numbers, the four test names, the changelog, and the other Limits lines all hold at the head.

Still open:

1. **Body line 36 (summary) and line 38 (bullet), and the draft.** The word "in-memory" is not established by the head code. `MockDirectoryFactory` opens the directory from `LuceneTestCase.newDirectory()` (`solr/test-framework/src/java/org/apache/solr/core/MockDirectoryFactory.java` lines 29 to 50), and the build sets `tests.directory` to `random` (`gradle/testing/randomization.gradle` line 100). The lead checked both. Suggested fix, in the live body and the draft together: name the test directory factory, "test directory factory (`MockDirectoryFactory`), which opens Lucene's randomized test directory", and drop "in-memory".

## Not done

No PR body edit, comment, review, close, branch edit or Jira write. No build, Gradle, test or gate run. The subagents did not commit their part reports, and the lead commits them with this roll-up.

The CI state on both PRs is the same: only the labeler check has run (success). The other checks are `action_required` and have not run. That is a state, not a code finding.
