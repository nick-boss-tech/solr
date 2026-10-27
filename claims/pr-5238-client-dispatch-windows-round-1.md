# Claim: PR 5238 client search dispatch regression and fix, Windows round 1

Status: CLAIMED

- Claimant: windows, Codex desktop; tags: implementation, review, records.
- Started: 2026-10-27 02:13:21 UTC (October 26, America/New_York).
- Assignment: Nick's direct request in this chat to code a regression and fix for the PR 5238 finding and send it to a gate agent.
- Reviewed board: `18c9c228a1c5662c463c5f42a5f6bb5b84196ce7`; no PR 5238 implementation or gate claim found.
- Parent: PR 5238 head `cc9ea9b1b6b153317217ee458ff6287b5e24cc65`.
- Scope: preserve listener events for v2-only and embedded qt-fallback client searches; retain ping, warming/pathless internal, nested-subquery, and shard exclusions.
- Design: record the original selected handler in the request context at core/API dispatch and preserve it during delegation; listener eligibility uses that metadata rather than re-resolving the v1 registry. Decision ladder rung 1: source establishes separate API and v1 registries and the embedded handler fallback.
- New fork branch: `solr-11994-pr5238-submit`, isolated worktree `wt/SOLR-11994-pr5238`. Preserve the older `solr-11994-submit` branch and all original contributor commits.
- Deliverables: pushed scoped fix/regression commit; `reports/pr-5238-client-dispatch-windows-round-1.md`; `gates/pr-5238-client-dispatch-round-1.md` with exact-head positive and negative Linux proof.
- Windows executes no Gradle, tests, formatting tasks, or retired test queue. A capable Linux gate agent must claim the separate gate under its host lock.
- Public action: no PR opening, JIRA write, or change to the contributor's branch. Any later PR comment follows Nick's standing clean-gate policy after proof and review.

Completion is derived from the named deliverables; this claim is immutable.
