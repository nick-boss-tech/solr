# Flaky-fix post-PR review round 2

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-2.md`. Claim: `claims/pool-flaky-fix-post-pr-review-round-2.md`. Lead: the windows review agent. Part reports: `reports/flaky-fix-post-pr-review-round-2-s1.md` (SOLR-18530, PR #5098) and `reports/flaky-fix-post-pr-review-round-2-s3.md` (SOLR-18532, PR #5100). Slice 2 (SOLR-18531) is inactive: the main side has not recorded a new head in the assignment file, and the fork branch is still at `5ba914ca745933251958412a4ccb226ecbaf0ad8`.

Read-only throughout. Nothing was posted, edited on a PR, or pushed to a submit branch. No build, Gradle, test or gate run.

## Summary

| Slice | PR | Head | Verdict |
|---|---|---|---|
| 1, SOLR-18530 | #5098 (draft) | `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`, equals the fork tip | REMAINING: 3 items, plus 2 records |
| 2, SOLR-18531 | #5101 (draft) | not checked; no new head recorded | INACTIVE |
| 3, SOLR-18532 | #5100 (draft) | `f1e5031fc3a9624b03daf353f26c977891d7d876`, equals the fork tip | REMAINING: 4 items, plus 1 internal record |

Neither PR is satisfied yet. The main side applies the remaining items. Once they clear, the main side marks the draft PRs ready for review.

## Slice 1: SOLR-18530 (PR #5098)

Verdict: REMAINING. The body's round 1 links, the receipt head, the title, the routing wording in the "What this change does" section and the Limits, and the Proof numbers are cleared.

Remaining items:

- **B1. Choice wording.** The bold line of "A choice to check" says "a leaders-only client". Change it to "a leaders-first client", since the cut replica stays a failover endpoint when the random routing flag is off (draft line 31).
- **B2. Changelog line.** The last line still has a "Changelog:" label and a paraphrase. Replace it with the section 4 exemption sentence from `dev-docs/changelog.adoc`, as round 1 D1 asked (draft line 45).
- **B3. "Logged" sentence (round 1 F3, partly done).** "A failed send to a replica is logged and does not fail the add" leaves out the `LeaderChanged` case. At the head, that case is added to the errors returned to the client (`DistributedZkUpdateProcessor.java` lines 1254 to 1262). The sentence needs that case named.
- **Records (not body).** Round 1 D2 (more evidence before opening) and D4 (keep or drop the choice section) are not recorded as decided. The opening itself is settled by Nick's direction in the round 1 assignment.

Lead check: the draft still reads "a leaders-only client" at line 31 and "Changelog: none" at line 45. The `LeaderChanged` branch returns its error to the client, as the subagent says.

## Slice 3: SOLR-18532 (PR #5100)

Verdict: REMAINING. Cleared: the causal sentence, the temp-file Limit, the non-atomic fallback, the failure-wait Limit, the choice question, the Limits summary line, and the SOLR-9865 wording.

Remaining items:

- **R1. Bare file names.** Three file citations are still plain code spans, not links at the head SHA (live body lines 15, 43 and 49 as the subagent counted them; the changelog reference on draft line 49 is one of them). Each file citation needs the form `https://github.com/nick-boss-tech/solr/blob/f1e5031fc3a9624b03daf353f26c977891d7d876/<path>#L<a>-L<b>`.
- **R2. The count.** "TestRestoreCore: 4 of 4" does not match the three `@Test` methods in the source. No record names the four test cases. The receipt's count stands as the only source. Confirm it from the JUnit XML, which is not on disk, or reword to what the record shows.
- **R3. Pass wording.** "4 of 4 pass" and "1 of 1 passes" should use the receipt's wording, "0 failures".
- **R4 (optional). "Instead of deleting it".** The live title and changelog line 1 still read as if the change never deletes the file. It deletes when the core had no `index.properties` before the restore.
- **R5 (CI, not body).** Solr Tests via Crave, Gradle Precommit and Validate Changelog are `action_required` at the head and have not run. Only the labeler check has run. The changelog YAML has not been validated by CI.
- **Internal record (not body).** `gates/SOLR-18532.md` does not record the failed first gate at `07a7ead`, which WORKFLOW.md requires.

## Slice 2: SOLR-18531 (PR #5101)

Inactive. The assignment starts this slice when the main side records the new head after the variant-test commit and its re-gate. No new head is recorded; the fork branch is at `5ba914ca745933251958412a4ccb226ecbaf0ad8`, the head the round 1 slice 2 review checked. The prior post-PR round's slice 2 section (`reports/flaky-fix-post-pr-review-round-1.md`) still stands for that head.

## Not done

No PR body edit, comment, review, close, submit-branch edit or Jira write. No build, Gradle, test or gate run. The only git action with a side effect was a read-only fetch of the two fork branches. That moved their local remote-tracking refs to the heads above.
