# Flaky-fix post-PR review round 8: final confirmation for the two remaining slices

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-8.md`. Claim: `claims/pool-flaky-fix-post-pr-review-round-8.md`. Lead: the windows review agent. Part reports: `reports/flaky-fix-post-pr-review-round-8-s1.md` (SOLR-18530, PR #5098) and `-s2.md` (SOLR-18532, PR #5100). SOLR-18531 keeps its round 6 satisfaction; its head has not moved and it is not re-checked here.

Both heads equal their fork tips. Read-only throughout. Nothing was posted, edited on a PR, or pushed to a submit branch. No build, Gradle, test or gate run.

## Verdicts

| Slice | PR | Head | Verdict |
|---|---|---|---|
| 1, SOLR-18530 | #5098 (draft) | `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d` | STILL OPEN: 2 items |
| 2, SOLR-18532 | #5100 (draft) | `7dfd3d98d0a2f016c520139cbca96ea4a49f6689` | STILL OPEN: 1 item |

## Slice 1: SOLR-18530 (PR #5098)

Satisfied: the round 7 failover item. Line 17 now reads that endpoint failover in `LBSolrClient` moves an update only when it could not connect or the request was never sent, which matches `LBSolrClient` at the head.

Still open, both about when a `ClosedChannelException` is retried. The lead checked the wrapping and the retry paths at the head. Connection failures before the send are wrapped as `RequestNotSentException` (`HttpJettySolrClient.java` lines 488, 516 and 525), and both `CloudSolrClient` (line 216) and `LBSolrClient` (line 677) retry that wrapper. So a pre-send `ClosedChannelException` is retried, and the claim must say so.

1. **Line 45 (Limits).** "SolrJ still does not retry a `ClosedChannelException` on an update" needs the qualifier "once the update has reached the server".
2. **Line 17.** "A `ClosedChannelException` is not one of the causes that makes the cloud client resend an update" needs the prefix "Once the update has reached the server,".

Apply both fixes to the live body and to `pr-drafts/flaky-fixes/SOLR-18530.md` together. The body and draft are otherwise identical.

## Slice 2: SOLR-18532 (PR #5100)

Satisfied: the round 7 edits at lines 36 and 38 (the test directory factory is named `MockDirectoryFactory`, and "in-memory" is gone from the summary and the bullet), the citations, the title, the changelog, the Proof counts, the test names, and the other Limits facts.

Still open:

1. **Line 41, in the body and in the draft.** "Only the in-memory factories use that fallback today" still uses "in-memory", which the head code does not establish. The lead confirmed the sentence. Suggested fix, in both copies: replace "the in-memory factories" with "the factories that extend `EphemeralDirectoryFactory`".

## Not done

No PR body edit, comment, review, close, branch edit or Jira write. No build, Gradle, test or gate run. The subagents did not commit their part reports, and the lead commits them with this roll-up.

The CI state on both PRs is the same: only the labeler check has run (success). The other checks are `action_required` and have not run. That is a state, not a code finding.
