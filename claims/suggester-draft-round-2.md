# Claim: suggester draft round 2

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: the drafts that `material/suggester-draft-round-answers.md` (commit `bfc53695856`) unblocks. It answers items A to D from the draft round, releases the holds on SOLR-9968, SOLR-14171, SOLR-17393, and SOLR-9637 with their ledger receipts, and confirms the Lucene checks for SOLR-10937 and SOLR-11844.

Drafts, each at the live tip checked with `git ls-remote` on 2026-10-09:
- SOLR-9227: `pr-drafts/suggester/SOLR-9227.md` edited. The bracketed HOLD line in Limits is replaced with the wording from answer A. Head `3c23fc5cfa6`, matches.
- SOLR-9968: new draft. Head `c31d2ff1a3f`, matches. Top-up receipt at that head.
- SOLR-14171: new draft. Head `faa262eedc5`, matches. Gate receipt at that head.
- SOLR-17393: new draft. Head `626241e647e`, matches. Gate receipt at that head. Merge-only scope confirmed (answer C).
- SOLR-9637: new draft, stacked on SOLR-17393. Head `c50fa4ffd93`, matches. Receipt dated 2026-10-05.
- SOLR-10937: new draft. Head `7d0cd11bafd`, matches. Five-lookup list confirmed (answer, docs verification).
- SOLR-11844: new draft. Head `cbd08c20e9a`, matches. Scaling rule confirmed (answer, docs verification).

Held, no draft this round:
- SOLR-17215: live tip moved from `66be01719765` to `04d35df186dd`. The material has no gate receipt for the new tip yet ("the draft waits for that receipt"). Hold.

Gate receipts and logs are main-side records. Drafts cite the receipts ledger entries and logs as the material names them.

Not in scope: edits to any `solr-*-submit` branch or PR description, opening or commenting on pull requests, builds, Gradle, and test runs, SOLR-18505, and the audits (their pins are not drafts; a stale pin is reported, not edited).
