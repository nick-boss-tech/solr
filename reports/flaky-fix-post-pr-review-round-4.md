# Flaky-fix post-PR review round 4: final confirmation

Assignment: `assignments/pool-flaky-fix-post-pr-review-round-4.md`. Claim: `claims/pool-flaky-fix-post-pr-review-round-4.md`. Lead: the windows review agent. Part reports: `reports/flaky-fix-post-pr-review-round-4-s1.md` (SOLR-18530, PR #5098), `-s2.md` (SOLR-18531, PR #5101) and `-s3.md` (SOLR-18532, PR #5100).

All three slices are active, and all three heads equal their fork tips. Read-only throughout. Nothing was posted, edited on a PR, or pushed to a submit branch. No build, Gradle, test or gate run.

## Verdicts

| Slice | PR | Head | Verdict |
|---|---|---|---|
| 1, SOLR-18530 | #5098 (draft) | `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d` | STILL OPEN: 3 items |
| 2, SOLR-18531 | #5101 (draft) | `351914f52c99180f0582d45c5bea1bd800194d29` | STILL OPEN: 3 items |
| 3, SOLR-18532 | #5100 (draft) | `7dfd3d98d0a2f016c520139cbca96ea4a49f6689` | STILL OPEN: 1 item |

Nick marks the draft PRs ready only when all three are SATISFIED. None is yet.

## Slice 1: SOLR-18530 (PR #5098)

Satisfied: the round 3 B3 sentence. It now reads "a failed send to a replica of this shard is logged", and both client-return anchors hold the claimed code (`DistributedZkUpdateProcessor.java` lines 1254 to 1262 and 1345 to 1348).

Still open:

1. **Proof, lines 25 and 26.** The CI seed value and the word "seeds" appear. Delete the seed sentence and the "at the CI seed" phrase. The counts stay.
2. **Limits, line 37.** The section has no bold one-line summary.
3. **Changelog, line 45.** `dev-docs/changelog.adoc` is a plain-text citation. It should be a link at the head SHA.

Body and draft are identical after CR stripping, so the same three edits go to the draft.

## Slice 2: SOLR-18531 (PR #5101)

Satisfied: round 1 items D1 to D4 and R1 to R2, the Proof numbers against the receipt at this head, the Limits statements against the code, the citations, and the changelog file. The body matches the main-side draft.

Still open:

1. **Body, line 23.** The text "CI seed 3E3D9FF553211ED6" is a seed value and must be removed. Seeds are internal vocabulary.
2. **Sections, lines 5, 9, 17, 31 and 35.** No section opens with a bold one-line summary.
3. **Limits, lines 38 and 40.** These say "until the JVM exits". A later start on that port also releases the reservation (`JettySolrRunner.java` line 517), so the wording must allow for that.

Not verified: the subagent's note N1, a Windows-specific risk that it did not check. It is the lead's call whether to pursue it.

## Slice 3: SOLR-18532 (PR #5100)

Satisfied: round 3 items R1a and R1b (the `StandardDirectoryFactory` link at lines 130 to 148 and the two `RestoreCore.java` and `TestRestoreCore.java` links now at `7dfd3d98`), the Proof counts and the four test case names against the receipt, the changelog file at the head, and the body-equals-draft check.

Still open:

1. **Limits, last bullet (live line 47; draft line 47).** It reads "The rollback path does not run in those tests, so this change does not address the teardown failure." After the new-test bullets, "those tests" reads as `TestRestoreCore`'s new test. The Proof says the old rollback runs there and deletes the file, so the bullet contradicts the Proof. Suggested fix, applied to the live body and the draft together: "This change does not address the teardown failure seen in the install tests, because the rollback path does not run there."

## Not done

No PR body edit, comment, review, close, branch edit or Jira write. No build, Gradle, test or gate run. The subagents did not commit their part reports, and the lead commits them with this roll-up.

The CI state is the same on all three PRs: only the labeler check has run (success). The other checks are `action_required` and have not run. That is a state, not a code finding.
