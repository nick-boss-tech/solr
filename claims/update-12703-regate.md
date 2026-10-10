# Claim: SOLR-12703 draft and receipt at the re-gated head

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: `receipts/SOLR-12703.md` and `pr-drafts/update-processing/SOLR-12703.md` as updated in commit `4e3af9620c5`. The branch changed one changelog line in `bdd29ba1990` ("SOLR-12703: correct the changelog symptom wording"): the symptom changes from a RunUpdateProcessor failure to the operand being stored as a field value. The receipt records the re-gate at `bdd29ba19900`, with 27 tests, 1 skip, and 0 failures, and a pre-fix proof on the earlier head `ed95d555e62`.

Head checked live with `git ls-remote` on 2026-10-09: `solr-12703-submit` at `bdd29ba19900`. Matches the receipt.

Split: two subagents, read only. The first checks the draft against the receipt and the changelog line, and the internal wording. The second checks the draft's code citations at the new head. The lead applies any change.

Output: `reports/update-12703-regate.md`.

Not in scope: edits to any file, PR, comment, title, or branch; builds, Gradle, and test runs.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: reports/update-12703-regate.md. The work was performed by the original claimant; this mark is a record correction, not a new claim.
