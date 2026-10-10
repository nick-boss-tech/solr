# Claim: suggester round 4 drafts (first draft round)

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: the draft round that `material/suggester-round-4-decisions.md` (branch tip `81339a26ef6`) opens. That file says the draft round can start with SOLR-9227 now, and that SOLR-9968, SOLR-14171, SOLR-17393, and SOLR-17215 join as their gates record green. The round 4 assignment (`assignments/suggester-round-4.md`) said drafts follow in a later round. This claim is that round. Drafts go under `pr-drafts/suggester/SOLR-<ticket>.md`, following `pr-formula.md`.

Ready to draft now:
- SOLR-9227, live tip `3c23fc5cfa6`, the head named by its gate receipt.

Held, no draft this round. Live tips checked 2026-10-09 with `git ls-remote`:
- SOLR-9968: live `c31d2ff1a3f`. Wording narrowing is on top of the gated tip; top-up gate pending.
- SOLR-14171: live `faa262eedc5`. Packaged; first full gate running.
- SOLR-17393: live `626241e647e`. Packaged; baseline gate running. The HIGH finding stays open until the per-shard item is settled.
- SOLR-17215: live `66be01719765`. Catch narrowing and ref-guide note not yet landed; implementation lane running.
- SOLR-10937: live `7d0cd11bafd`. Docs verification lane pushing.
- SOLR-11844: live `cbd08c20e9a`. Docs lane, conditional on the Lucene weight check.
- SOLR-9637: live `c50fa4ffd93`. Its own PR after SOLR-17393 lands, rebased onto that landed tip.

Gate receipts and gate logs are main-side records. The drafts cite the material's record of them, as in earlier rounds.

Not in scope: edits to any `solr-*-submit` branch or PR description, opening or commenting on pull requests, builds, Gradle, and test runs, SOLR-18505, and the SOLR-13696 and SOLR-13943 drafts (their own claim, `claims/update-processing-13696-r8.md`).

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: pr-drafts/suggester/. The work was performed by the original claimant; this mark is a record correction, not a new claim.
