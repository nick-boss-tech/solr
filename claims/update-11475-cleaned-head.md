# Claim: SOLR-11475 draft and receipt at the cleaned-history head

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: upstream commit `39c856f2d98` ("SOLR-11475 draft and receipt at the cleaned-history head"). It changes:
- `pr-drafts/update-processing/SOLR-11475.md` (citation links move to the new head)
- `receipts/SOLR-11475.md` (adds a history-cleanup note)

Head checked live on 2026-10-09 with `git ls-remote`: `solr-11475-submit` at `229947201fd797e57e2d3db1ff2c6552097c9734`. The draft and the receipt both name this head.

The receipt says the history was rewritten to remove Co-Authored-By trailers, and that the tree is byte-identical to the gated head `0de48e492fd` (tree `3223028c6f4e2ff44bf67509423e5815114f97b4`). The reviewers check that claim with git, not by trust.

Gate log named in the receipt (`g11475-fix-gate.log`) is not on disk under `research/`. The receipt is the only named source for the test counts.

Split: two subagents, read only, four tasks each.
- Subagent 1 (draft against receipt and code): receipt counts and dates; citation links at the new head; the tree and trailer check on the rewritten history; the changelog fragment.
- Subagent 2 (test claims and wording): the five test classes and their inheritance; the test's timeout and comment; the "A choice to check" section; formula, plain language, vocabulary, length.

Output: each subagent writes `reports/update-11475-citations.md` and `reports/update-11475-wording.md`. The lead writes the round roll-up `reports/update-11475-cleaned-head.md`.

Not in scope: edits to any draft or receipt, submit branch, live PR description, PR, or comment; builds, Gradle, and test runs. Reading files, `git show`, `git log`, `git diff`, `git rev-parse`, `git cat-file -e`, and `git grep` are allowed.
