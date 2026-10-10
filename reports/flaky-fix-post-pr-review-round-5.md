# Flaky-fix post-PR review round 5: final confirmation

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-5.md`. Claim: `claims/pool-flaky-fix-post-pr-review-round-5.md`. Lead: the windows review agent. Part reports: `reports/flaky-fix-post-pr-review-round-5-s1.md` (SOLR-18530, PR #5098) and `reports/flaky-fix-post-pr-review-round-5-s3.md` (SOLR-18532, PR #5100). Slice 2 (SOLR-18531) is inactive.

Read-only throughout. Nothing was posted, edited on a PR, or pushed to a submit branch. No build, Gradle, test or gate run.

## Verdicts

| Slice | PR | Head | Verdict |
|---|---|---|---|
| 1, SOLR-18530 | #5098 (draft) | `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`, equals the fork tip | STILL OPEN: 1 item |
| 2, SOLR-18531 | #5101 (draft) | `c8a67c7267d00e33ad8323c683c040b539989b0d`, equals the fork tip | STILL OPEN: 3 items |
| 3, SOLR-18532 | #5100 (draft) | `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`, equals the fork tip | STILL OPEN: 2 items |

Nick marks the draft PRs ready only when all three slices are SATISFIED. None is yet.

## Slice 1: SOLR-18530 (PR #5098)

Satisfied: round 4 items 1 to 3. The Proof has no CI seed value, the Limits section opens with a bold summary, and the changelog citation is a link at the head. The "of this shard" qualifier also holds.

Still open:

1. **Limits, line 43.** It reads "The test checks only that replicas become active within 30 seconds." The adds after the cut run without a catch (`RecoveryAfterSoftCommitTest.java` lines 108 to 118, `leaderClient.add(document)`), so a failed add also fails the test, and "only" is wrong. Suggested fix, applied to the live body and to the draft together: "Besides the adds succeeding, the test checks only that replicas become active within 30 seconds."

Non-blocking notes from the subagent, not verdict items: the Proof says "at 14868bc" while the gate ran at `98e5368` (the test file is identical at both), a claim repeats in Limits line 41, and the LeaderChanged paraphrase is loose.

## Slice 2: SOLR-18531 (PR #5101)

Verdict: STILL OPEN, three items. `receipts/SOLR-18531.md` now records GATE GREEN at `c8a67c7267d00e33ad8323c683c040b539989b0d` (re-gate 3), so the assignment's start condition holds. The fork branch and PR #5101 are at that head.

Satisfied: the round 4 items 1 to 3 (no seed in the Proof, a bold summary on every section, and the release routes in Limits), the N1 platform Limits line, the N2 guard, the served-traffic test, and the Proof numbers against the receipt.

Code check (part 2b): every release-route statement in Limits matches the head code. Runners removed by `stopJettySolrRunner` are not released at shutdown (`MiniSolrCloudCluster.java` lines 514 to 519 and 528 to 532; the shutdown snapshot at 627; the release at 644 to 646). A restart on a fresh port leaves the old port reserved (`JettySolrRunner.java` lines 512 and 517). The proxy port has no reservation. A standalone runner that is stopped and never closed keeps its port. The bind uses address reuse on the head (line 721). That is a code reading. No Linux run was made, so the Linux behaviour is not verified here.

Still open:

1. **Body, line 19.** "so nothing else can bind that port" must say "on Linux", because the claim depends on the Linux result.
2. **Body, line 13 (summary).** "or its cluster shuts down" overstates for a runner removed by `stopJettySolrRunner`, which is not released at shutdown. Qualify it to "while it is still in its cluster".
3. **Body, line 53 (changelog).** The changelog citation is a plain code span. It should be a head-SHA blob link, as slice 1 has it.

Fixes go to the live body and the draft together.

Non-blocking notes: `gates/SOLR-18531.md` still says RUNNING while the receipt says GREEN. Round 1 F6 (a failed start) and F8 (`close()` can release another runner's entry on the same port) are not named in Limits.

## Slice 3: SOLR-18532 (PR #5100)

Satisfied: the round 4 Limits bullet naming the install tests does not contradict the Proof. The receipt's counts and four test case names, the links at the head, the title, the changelog file, and the body-equals-draft check all hold.

Still open:

1. **Live line 17 (draft line 17).** The paragraph "The rollback path does not run in those tests" follows the new-test link. After that link, "those tests" reads as the new test, which the Proof says runs the old rollback and deletes the file. Fix: delete that paragraph and its blank line.
2. **Live line 47 (draft line 47).** The causal clause "because the rollback path does not run there" is stronger than the record. The root-cause report (`reports/flaky-tests-root-cause-round-1-t1.md`) calls the install-test route evidence, not proof, and says no way was found for the second route to fire. Fix: end the bullet at "seen in the install tests."

Lead check: the lead read the head test's add loop and the t1 record. Both support the findings above.

## Not done

No PR body edit, comment, review, close, branch edit or Jira write. No build, Gradle, test or gate run. The subagents did not commit their part reports, and the lead commits them with this roll-up.

The CI state is the same on both active PRs: only the labeler check has run (success). The other checks are `action_required` and have not run. That is a state, not a code finding.
