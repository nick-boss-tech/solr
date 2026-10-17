# Claim: verify the plain-language pass over the 28 drafts

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: the owner's rulings in `material/review-opened-28-answers.md` (commit `555412ae8d4` and `9f567474f62`), and the plain-language pass the main side made to the Proof sections of the drafts (commit `78ce00d90af`). The owner's rule: public PR text states results plainly and never uses internal vocabulary (gate, receipt, ledger, fresh JUnit XML, rc=0, pre-fix proof as a label). The rule preserves every count, head, and date.

Output: a read-only check of the 25 changed drafts against their previous versions at `bc61a3ce3d3`, reporting any count, head, or date that was dropped or changed, any claim the receipts do not support, and any internal vocabulary left. Report: `reports/review-plain-language-28.md`.

Split: two subagents, about twelve drafts each. Each reports, and the lead applies any fix.

Not in scope: edits to any draft in this round, any PR body or title, submit branches, builds, Gradle, and test runs. The owner's rulings say the main side applies the drafts to the live PR bodies.
