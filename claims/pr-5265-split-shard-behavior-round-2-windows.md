# Claim: split-shard migration behavior repairs, round 2 (Windows)

Status: CLAIMED

- Owner direction: Nick asked in chat to patch the existing PR branch to fix all three findings from the follow-up review.
- Host: windows; tags implementation, review, records. Started 2026-10-30 UTC.
- Target: PR 5265, SOLR-16398; fork branch `solr-16398-split-shard` at `0495ffa2b00772189d2f92d83b32140f1b5e8b39`.
- Board reviewed: `432b09211d72e19ab024d3a81a70cfe39d86f58f`. Earlier mapping/changelog work is completed by `receipts/pr-5265-split-shard-followup-round-1.md`; this is a distinct slice.
- Scope: restore the split-specific timeout, return requested timing output, and reject conflicting split options. Add focused regression tests using existing infrastructure, preserve v1 behavior and the separately tracked SOLR-17241 boundary.
- Worktree: isolated `wt/SOLR-16398-PR5265-behavior-r2` based on the exact live PR head. Normal fast-forward fork push only; no public comment or body edit.
- Deliverables: pushed fix commit, `reports/pr-5265-split-shard-behavior-round-2.md`, and `gates/pr-5265-split-shard-behavior-round-2.md` pinned to the pushed head. Linux gate host supplies the new receipt after exact-head proof; Windows runs no Gradle.
- Decisions: use a typed split response for timing and preserve existing helper defaults for other consumers via timeout overloads. Reuse MockV2APITest and the existing generated-client cloud test; no additional fixture.
