# Claim: update-29 rulings, drafts brought up to the executed heads

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: `material/update-29-rulings.md` (commit `020ff7addf4`), which records the owner's rulings on the three real contradictions in `reports/update-29-consistency-pass.md` (R1, R2, R3), and their execution on the submit branches. The refreshed receipts for SOLR-6045 and SOLR-12705 are at the same commit. Output: draft edits under `pr-drafts/update-processing/` and a round report.

Heads, checked live with `git ls-remote` on 2026-10-09:
- SOLR-6045: live `bcae04d77bdb`, the head the refreshed receipt names. Matches. The ruling widens the merger check to plain-first, in both orders (R2).
- SOLR-12705: live `aa56b7b1be4`, the head the refreshed receipt names. Matches. The counting pin is re-scoped, test only (R1).
- SOLR-7504: live `22b77196e662`, unchanged. Its combined run with 12705 at `aa56b7b1be4` passed the same day, per the 12705 receipt.
- SOLR-14718: live `29c09959791a`, unchanged. Its Limits are rewritten to the post-5939 mechanism (R3).
- SOLR-5939: live `f8d4bdbea518`, unchanged. Per-request attribution stands (R3).

Drafts to bring up to the rulings: 6045 (What, Proof and Limits, because the check is widened), 12705 (Proof, landing order, and the counted-field outcome), 7504 (the combined run, the shared helper, and the landing order), 14718 (Limits, rewritten to the post-5939 mechanism). Any other draft that describes the same behavior is reported, not edited.

Landing order from the ruling: 7504 before 12705. SOLR-16655 still lands before 12705.

Split: two subagents. The first takes SOLR-6045 and SOLR-12705. The second takes SOLR-7504, SOLR-14718, and a cross-draft check for other drafts that describe the changed behavior.

Not in scope: edits to any `solr-*-submit` branch or PR description, opening or commenting on pull requests, builds, Gradle, and test runs. The owner's rulings are executed by the main side, not here.
