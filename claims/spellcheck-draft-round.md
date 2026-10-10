# Claim: spellcheck draft round

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: the main side's answers in `material/spellcheck-round-5-answers.md` (commit `27a30874916`), which answer the ten owner decisions of `reports/spellcheck-round-5.md`, give the gate receipts, and state the decision states. Output: drafts under `pr-drafts/spellcheck/` for the branches the answers unblock, hold checks for the rest, and `reports/spellcheck-draft-round.md`.

Drafts (each head checked live with `git ls-remote` on 2026-10-09):
- SOLR-3701: head `aabd678dec7330c55dbc044ce6b2f46dd00a0c29`, matches. Decision 2 adopted. Receipt: GATED, PUSHED 2026-10-06 at that head, premise grounded.
- SOLR-4367: head `0af6087f43fa2b03a9cedb259c1b0dd62039963c`, matches. Decision 5 adopted. Receipt: GATED, PUSHED 2026-10-06 at that head, with a Pairs round at the same tip.

Held, no draft this round (hold checks only):
- SOLR-1877: decision 1 is DISCUSS, and the recommended change re-gates the branch.
- SOLR-4366: decision 3 is DISCUSS. Never gated; packaging and the first gate are launched.
- SOLR-4399: decision 6 is DISCUSS, and the recommended change moves the guard and retitles the changelog.
- SOLR-9060: decision 7 is DISCUSS. The changelog narrowing would move the head.
- SOLR-10252: decision 8 is DISCUSS. The wording corrections wait for the owner's field ruling.
- SOLR-10789: decision 9 is adopted, but the gate is launched and no receipt is recorded.
- SOLR-17612: decision 10 is DISCUSS, and the recommended fix changes the code.

DISCUSS items are recommendations only, not taken, per the answers file.

Gate receipts and logs are main-side records. Drafts cite the answers file's record of them, as in earlier rounds.

Not in scope: edits to any `solr-*-submit` branch or PR description, opening or commenting on pull requests, builds, Gradle, and test runs.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: reports/spellcheck-draft-round.md. The work was performed by the original claimant; this mark is a record correction, not a new claim.
