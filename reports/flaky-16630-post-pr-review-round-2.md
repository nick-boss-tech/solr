# Post-PR review round 2 for SOLR-16630: confirmation

Assignment: `assignments/pool-flaky-16630-post-pr-review-round-2.md`. Claim: `claims/pool-flaky-16630-post-pr-review-round-2.md`. Lead: the windows review agent. Part reports: `reports/flaky-16630-post-pr-review-round-2-s1.md` (the three round 1 findings) and `reports/flaky-16630-post-pr-review-round-2-s2.md` (the whole-body read).

The live draft PR is at head `9c77a4db09d7a890f62dc9a9ffb2bf5613245da7`, the head round 1 covered. The worktree draft equals commit `d15de4f3024`. Read-only throughout: nothing was edited, committed, pushed or posted, and no build, test or gate ran.

## Verdict

**STILL OPEN.** Three findings, all wording. Each has the exact replacement text in the part report.

## Round 1 findings

| Finding | Status | Evidence |
|---|---|---|
| F1: symptom citations link the base commit with base lines | FIXED | All nine base links resolve to the cited base lines. Fix links are on the head SHA. |
| F2: Proof headline overstated one base run | FIXED | One base run and six focused runs, each 1 test, 0 failures, 0 errors. Dates and SHA match the receipt. |
| F3: Limits lacked the follow-up offer | FIXED | "On request, a follow-up ticket and pull request can cover it." is present. |

## New findings from the whole-body read

1. **"can go stale" overstates the risk.** The Limits sentence presents a roughly 10 percent hypothesis as a fact. Use "could go stale".
2. **"In a failing CI run" has no source in the receipt.** The receipt records apache/solr run 38009158573, dated 2026-10-10. Either confirm the run page, or use "In a failing run,".
3. **The Limits link has no base-commit statement.** The link points at the base commit at line 309, but the text does not say so. Add the base-commit statement the formula requires.

The optional notes N1 to N5 from round 1 are not applied in the live body. None of them blocks the verdict.

## Not done

- No edits, commits, pushes, PR writes, Jira writes, builds, tests or gates.
- The CI run page and the Jira ticket were not read.
- The claim is marked DONE in the same push as this report, as WORKFLOW.md requires.
