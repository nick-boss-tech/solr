# Claim: check the highlighting answers against the two changed drafts

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: `material/highlighting-round-1-answers.md` (commit `24f69b3d9e4`), which answers the highlighting round report. The main side changed two drafts, `pr-drafts/highlighting/SOLR-2681.md` and `pr-drafts/highlighting/SOLR-4540.md`. This claim checks those two drafts against the answers and the receipts. Output: `reports/highlighting-answers-check.md`.

What the answers say, to be checked:
- SOLR-2681: the internal note is removed; "on request" is replaced by a planned-submission sentence; the count of 36 of 36 is not to be posted until the recount is recorded. The changelog title is narrowed at packaging, not yet.
- SOLR-4540: the base-code line is filled from a premise run. The changelog title is to be corrected at packaging, not yet.

Heads, checked live with `git ls-remote` on 2026-10-09:
- SOLR-2681: `4cb25b1691b9ca66552a93687f79cd62993efcf9`. Matches the receipt.
- SOLR-4540: `62c06439fb0653b299ec54f110e4f94e420cea9b`. Matches the receipt.

Split: two subagents, one per draft. Each is read only: it reports whether the draft matches the answers and the receipt, and any text that is internal or unsupported, with exact replacement text. The lead applies any change.

Not in scope: opening PRs, posting comments, editing branches or PR descriptions, builds, Gradle, and test runs. The premise run and the recount are main-side work.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: reports/highlighting-answers-check.md. The work was performed by the original claimant; this mark is a record correction, not a new claim.
