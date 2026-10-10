# Flaky-fix post-PR review round 3: confirmation pass

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-3.md`. Claim: `claims/pool-flaky-fix-post-pr-review-round-3.md`. Lead: the windows review agent. Part reports: `reports/flaky-fix-post-pr-review-round-3-s1.md` (SOLR-18530, PR #5098) and `reports/flaky-fix-post-pr-review-round-3-s3.md` (SOLR-18532, PR #5100). Slice 2 (SOLR-18531) is inactive.

Read-only throughout. Nothing was posted, edited on a PR, or pushed to a submit branch. No build, Gradle, test or gate run.

## Verdicts

| Slice | PR | Head | Verdict |
|---|---|---|---|
| 1, SOLR-18530 | #5098 (draft) | `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`, equals the fork tip | STILL OPEN: 1 item |
| 2, SOLR-18531 | #5101 (draft) | not checked | INACTIVE: no green gate recorded at the new head |
| 3, SOLR-18532 | #5100 (draft) | `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`, equals the fork tip | STILL OPEN: 2 items |

Nick marks the three draft PRs ready once all three slices are SATISFIED. That is not yet the case.

## Slice 1: SOLR-18530 (PR #5098)

Verdict: STILL OPEN. Round 2 items B1, B2, D2 and D4 are satisfied, and the live body equals the draft after CR stripping.

Remaining item:

- **B3. "Logged" sentence.** The body says a failed send to a replica is logged and does not fail the add, with one exception for `LeaderChanged`. At the head, the error loop also returns a failed send to the client when the replica is on another shard (`DistributedZkUpdateProcessor.java` lines 1345 to 1348: `if (!shardId.equals(cloudDesc.getShardId())) { errorsForClient.add(error); }`). The sentence needs "of this shard" after "a failed send to a replica". The `LeaderChanged` naming and its link (lines 1254 to 1262) are correct.

Lead check: the lead read lines 1336 to 1352 at the head and confirmed the other-shard branch returns the error to the client.

## Slice 2: SOLR-18531 (PR #5101)

Inactive. The assignment starts this slice when `receipts/SOLR-18531.md` records GATE GREEN at `351914f52c99180f0582d45c5bea1bd800194d29` and the PR body is updated for that head. The fork branch and PR #5101 are at `351914f5`, but the receipt still records green at `5ba914ca745933251958412a4ccb226ecbaf0ad8`. The re-gate has not been recorded.

## Slice 3: SOLR-18532 (PR #5100)

Verdict: STILL OPEN. Round 2 items R2, R3 and R4 are satisfied, along with the changelog commit's tree, the gate file's failed first-gate record, and the body-equals-draft check.

Remaining items (body-only edits, applied to the live body and to the draft together):

- **R1a. A plain code span.** `StandardDirectoryFactory` in the Limits section is a plain code span on draft line 43. It needs a link to `https://github.com/nick-boss-tech/solr/blob/7dfd3d98d0a2f016c520139cbca96ea4a49f6689/solr/core/src/java/org/apache/solr/core/StandardDirectoryFactory.java#L130-L148`.
- **R1b. Links at the old head.** Draft line 15 has two links that still point at `f1e5031fc3a9624b03daf353f26c977891d7d876`: `RestoreCore.java` and `TestRestoreCore.java`. They need the PR head `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`. The content is identical, since the change between the two heads is the changelog only.

Lead check: the lead confirmed both items in the draft at the tip. Line 15 has two `f1e5031` blob links, and line 43 has the plain `StandardDirectoryFactory` span.

## Not done

No PR body edit, comment, review, close, submit-branch edit or Jira write. No build, Gradle, test or gate run. The subagents did not commit their part reports, and the lead commits them with this roll-up.

The CI state is the same on both PRs: only the labeler check has run (success). Gradle Precommit, Solr Tests via Crave and Validate Changelog are `action_required` and have not run. That is a state, not a code finding.
