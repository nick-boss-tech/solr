# Flaky-fix post-PR review round 5: final confirmation

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-5.md`. Claim: `claims/pool-flaky-fix-post-pr-review-round-5.md`. Lead: the windows review agent. Part reports: `reports/flaky-fix-post-pr-review-round-5-s1.md` (SOLR-18530, PR #5098) and `reports/flaky-fix-post-pr-review-round-5-s3.md` (SOLR-18532, PR #5100). Slice 2 (SOLR-18531) is inactive.

Read-only throughout. Nothing was posted, edited on a PR, or pushed to a submit branch. No build, Gradle, test or gate run.

## Verdicts

| Slice | PR | Head | Verdict |
|---|---|---|---|
| 1, SOLR-18530 | #5098 (draft) | `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`, equals the fork tip | STILL OPEN: 1 item |
| 2, SOLR-18531 | #5101 (draft) | `c8a67c7267d00e33ad8323c683c040b539989b0d` | INACTIVE: the receipt does not yet record a green re-gate at this head |
| 3, SOLR-18532 | #5100 (draft) | `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`, equals the fork tip | STILL OPEN: 2 items |

Nick marks the draft PRs ready only when all three slices are SATISFIED. None is yet.

## Slice 1: SOLR-18530 (PR #5098)

Satisfied: round 4 items 1 to 3. The Proof has no CI seed value, the Limits section opens with a bold summary, and the changelog citation is a link at the head. The "of this shard" qualifier also holds.

Still open:

1. **Limits, line 43.** It reads "The test checks only that replicas become active within 30 seconds." The adds after the cut run without a catch (`RecoveryAfterSoftCommitTest.java` lines 108 to 118, `leaderClient.add(document)`), so a failed add also fails the test, and "only" is wrong. Suggested fix, applied to the live body and to the draft together: "Besides the adds succeeding, the test checks only that replicas become active within 30 seconds."

Non-blocking notes from the subagent, not verdict items: the Proof says "at 14868bc" while the gate ran at `98e5368` (the test file is identical at both), a claim repeats in Limits line 41, and the LeaderChanged paraphrase is loose.

## Slice 2: SOLR-18531 (PR #5101)

Inactive. The fork branch and PR #5101 are at `c8a67c7267d00e33ad8323c683c040b539989b0d`. `receipts/SOLR-18531.md` still records GATE GREEN at `351914f52c99180f0582d45c5bea1bd800194d29`. The assignment starts this slice only when the receipt records a green re-gate at the new head.

## Slice 3: SOLR-18532 (PR #5100)

Satisfied: the round 4 Limits bullet naming the install tests does not contradict the Proof. The receipt's counts and four test case names, the links at the head, the title, the changelog file, and the body-equals-draft check all hold.

Still open:

1. **Live line 17 (draft line 17).** The paragraph "The rollback path does not run in those tests" follows the new-test link. After that link, "those tests" reads as the new test, which the Proof says runs the old rollback and deletes the file. Fix: delete that paragraph and its blank line.
2. **Live line 47 (draft line 47).** The causal clause "because the rollback path does not run there" is stronger than the record. The root-cause report (`reports/flaky-tests-root-cause-round-1-t1.md`) calls the install-test route evidence, not proof, and says no way was found for the second route to fire. Fix: end the bullet at "seen in the install tests."

Lead check: the lead read the head test's add loop and the t1 record. Both support the findings above.

## Not done

No PR body edit, comment, review, close, branch edit or Jira write. No build, Gradle, test or gate run. The subagents did not commit their part reports, and the lead commits them with this roll-up.

The CI state is the same on both active PRs: only the labeler check has run (success). The other checks are `action_required` and have not run. That is a state, not a code finding.
