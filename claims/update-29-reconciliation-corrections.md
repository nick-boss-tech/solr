# Claim: corrections from the update-29 reconciliation

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: `material/update-29-reconciliation-answers.md` (commit `e47afe13b0c`), which accepts the corrections listed in the reconciliation report for the next drafting round, and the refreshed receipts for SOLR-4366 and SOLR-17612 at the same commit. Output: corrections to the Proof and Limits of the drafts named below, and to three audit notes, in the worktree, for the lead to review and commit.

Named heads, checked live with `git ls-remote` on 2026-10-09 where a draft cites a live tip:
- SOLR-4366: live `558864454f7f`, the refreshed receipt's gated head. Matches.
- SOLR-17612: live `dd2708fbb7e5`, the refreshed receipt's gated head. Matches.

Draft corrections, each taken from its receipt at `cfb8f96c4c3` or from the answers file:
- 3657, 4841, 5505, 5754, 5887, 5939, 11483, 13265, 14262, 14718, 16910: the Proof sections, from the receipts. Drop any head, run, or count the receipt does not name, unless the answers file names it.
- 5754 and 5939: the combined gate ran on the local merge commit with tree `1ddbf36202d`. `f2e33340f0ea` is a trial-merge tree of the same two heads, not the gated tree.
- 4841 and 5754: say that the live tip has moved from the receipt head, and what changed.
- 7022: say the live tip was never gated, and cite the CI run on `7940e98b0ee0` only as CI on that commit.
- 12245: add the Limits the owner's record requires (the MDC ask is not addressed and is offered as a follow-up; the null-guard coverage is named).
- 6065: state the status-code disposition the answers file records (500, locked 2026-10-06, gated at `3d2cec9e1ab3`), in place of "That choice is for maintainers to decide". Keep 400 only as the stated alternative.
- 16356: drop or label the commits the receipt does not name (`dcb16c775d62`, `56ec140e3636`).
- 12705: the singular "new test" in the receipt's wording, where the draft says "tests", if the draft's wording is a mismatch.

Audit notes:
- 12705: correct the "Settling run and gate" statement that Concat, FieldValueSubset, and Ignore are unchanged. With the default `true` they take the new path.
- 5939: correct the tolerant-count note (the count is per distinct remote exception, per the draft).
- 7504: correct the combined-tree note to the current 12705 draft, which names `mutateAtomicOperands()`.

Not in scope: changing any owner decision, R1 to R3 (recommendations only, not taken), SOLR-6045 or SOLR-7504 drafts, the spellcheck holds, edits to any solr-*-submit branch or PR description, opening or commenting on pull requests, builds, Gradle, and test runs.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: reports/update-29-reconciliation-corrections.md. The work was performed by the original claimant; this mark is a record correction, not a new claim.
