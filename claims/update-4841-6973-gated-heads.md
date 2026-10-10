# Claim: SOLR-4841 and SOLR-6973 drafts and receipts at their new gated heads

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: upstream commit `ce8edcfedf9` ("SOLR-6973 and SOLR-4841 drafts and receipts at their new gated heads"). It changes:
- `pr-drafts/update-processing/SOLR-4841.md` and `receipts/SOLR-4841.md`
- `pr-drafts/update-processing/SOLR-6973.md` and `receipts/SOLR-6973.md`

Heads, checked live on 2026-10-09 with `git ls-remote` and a read-only fetch of the fork's submit branches:
- `solr-4841-submit` at `bda9f9d640d9ba4bc0ecf5ed891c14f9ddc77082`. The new draft names this head. The receipt header still names `e4c878627108`; the receipt's top-up line names `bda9f9d640d9`.
- `solr-6973-submit` at `51fcd05e695ae1a5983ed69a3d2afc50e7f7bc1e`. The new draft names this head. The receipt header still names `fa5b59ba07b4`; the receipt's re-gate line names `51fcd05e695`.

Gate logs named in the receipts (`g4841-gate.log`, `g4841-changedtype-topup.log`, `g6973-gate.log`, `g6973-copilotfix-gate.log`) are not on disk under `research/`. The receipts are the only named source for Proof numbers in this round.

Split: two subagents, read only, four tasks each.
- Subagent for SOLR-4841: draft against receipt; code citations at `bda9f9d640d9`; changelog fragment and wording; formula and plain-language check.
- Subagent for SOLR-6973: the same four checks at `51fcd05e695`, including the sentence about the review fix and the signature class order.

Output: each subagent writes `reports/update-4841-gated-heads.md` and `reports/update-6973-gated-heads.md`. The lead writes the round roll-up `reports/update-4841-6973-gated-heads.md`.

Not in scope: edits to any draft or receipt, submit branch, live PR description, PR, or comment; builds, Gradle, and test runs. Reading files, `git show`, `git grep`, and `git cat-file` are allowed.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: reports/update-4841-6973-gated-heads.md. The work was performed by the original claimant; this mark is a record correction, not a new claim.
