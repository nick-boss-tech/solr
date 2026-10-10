# Claim: SOLR-7504 null-count fix, drafts brought up to the new head

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: `receipts/SOLR-7504.md` (commit `0e567a0d7db`), refreshed at the null-fix head. The receipt names `2fe06bfd917f193b3591dfa4c088ef0994279ed6` as the gated head and the live tip, with focused counts 30 of 30 and 42 of 42, a pre-fix proof in which the new test `testCountValuesNullValue` fails on the pre-fix branch production at `22b77196e662`, and a note that the combined run with SOLR-12705 was before the fix.

Head checked live with `git ls-remote` on 2026-10-09: `solr-7504-submit` at `2fe06bfd917f`. Matches the receipt.

Drafts to update in `pr-drafts/update-processing/`:
- `SOLR-7504.md`: cite the new head throughout; the Proof uses the receipt's counts and pre-fix result; "a null counts as 0" is now true and stays; the combined-run sentence names the head it ran at (`22b77196e662`, before the null fix) and says the fix is not part of that run.
- `SOLR-12705.md`: the landing-order and combined-run sentences name the SOLR-7504 head that was combined, and say that the combined run predates the SOLR-7504 null fix.
- Any other draft that cites `22b77196e662` for SOLR-7504 or describes the 7504 combined run: report, do not edit.

Split: two subagents, one per draft. A third check, of other drafts that cite the 7504 head, is in the second subagent's report.

Not in scope: edits to any PR, comment, PR title, submit branch, or live PR description; builds, Gradle, and test runs.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: reports/update-7504-nullfix.md. The work was performed by the original claimant; this mark is a record correction, not a new claim.
