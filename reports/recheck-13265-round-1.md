# SOLR-13265 fresh re-check, round 1

Verdict: **NEEDS EDIT, and two edits are applied.** The heads, links, anchors and behaviour claims hold. The receipt's run date is 2026-10-07, and the draft carried a different run date and a seed that the receipt does not record. The bold summary repeated itself in the body. Both are corrected.

Scope: `assignments/pool-recheck-13265-round-1.md`. Claim: `claims/pool-recheck-13265-round-1.md`. Draft `pr-drafts/update-processing/SOLR-13265.md` at `a12c08b553e`, checked against `receipts/SOLR-13265.md` (receipt head `c134b34aa27f`, dated 2026-10-07) and the fork branch. One read-only subagent.

## Checks

- **C1, heads and links: PASS.** The head `c134b34aa27f` exists and matches the fork branch. The base `9b3a84b1c460` is the parent of the branch's first commit and equals the merge-base with `upstream/main`. Every linked path exists at its named commit.
- **C2, anchors: PASS.** Each line range covers the sentence it supports, at the commit the link names.
- **C3, counts, dates and run claims: FAIL, fixed.** The date 2026-10-07, the counts (3 of 3 at head, 1 of 3 failing on base) and the test name match the receipt. The Proof bullet dated the base run 2026-10-09 with a seed value, and said the test failed at its assertion with a counter datapoint present. Neither the date, the seed nor the assertion detail is in the receipt. The bullet now states only the recorded failure and run date.
- **C4, behaviour claims: PASS.** On the base, the bypass path returns 1 with the return code still at -1, so the finally block counts an error. At the head, the flag sets the bypass and skips the counter. The pending counter is not incremented on either side. Deletes with the flag return before the counter. The assertion can fail only when the datapoint exists and is nonzero.
- **C5, format: FAIL, fixed.** The body repeated the summary's claim. The first three sentences of the "What happens today" body are replaced with one sentence that states the mechanism. No em dash or en dash remains, and no PR numbers appear.

## Edits applied

1. "What happens today", body. The three sentences before the first link ("A TLOG replica follower can skip the IndexWriter ... accepted into the update log.") are replaced with: "The bypass path returns 1 without setting `rc`, so the finally block counts it as an error."
2. Proof bullet 2. "On the merge-base `9b3a84b1c460` with only the branch's `SolrIndexMetricsTest.java` applied, the test fails at its assertion (run 2026-10-09, seed 13265C0FFEE13265). The counter datapoint is present and above zero there." is replaced with: "On the merge-base `9b3a84b1c460` with only the branch's `SolrIndexMetricsTest.java` applied, the test fails (run 2026-10-07)."

## Notes

- The overlay wording in the Proof (the branch's test file applied to the base) is not in the receipt, which says only "base production". The receipt's source log is not tracked at HEAD, so the wording stays as a note for the main side to confirm against the log.
- The Limits sentence "This was checked by reading, not by a run." is accurate and stays.

## Convergence line

NEEDS EDIT is resolved by the two edits above. The draft converges on the checked text. The overlay note is for the main side.
