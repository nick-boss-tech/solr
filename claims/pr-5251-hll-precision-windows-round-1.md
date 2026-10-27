# Claim: SOLR-18598 HLL precision merge guard, Windows, round 1

- Claimant: Windows Codex desktop; tags `implementation`, `review`, `records`.
- Started: 2026-10-27 20:28:57 UTC.
- Authorization: Nick's direct request in this chat, "Implement a patch for this then hand it off to the gate", following the PR 5251 review.
- Board snapshot reviewed: `c775d553f7212e43848c1c01c04558318d44fe1e`. No SOLR-18598 receipt, matching assignment, or competing claim found.
- Public PR: apache/solr 5251, unchanged head `ca539cad196f26c78e14a1dc698b84e0f5c1f22e`, contributor branch `elangelo/solr:json-facet-hll-precision`.
- Scope: reject incompatible incoming sketch register counts/widths in the JSON facet HLL merger, add merger regressions including both response orders and compatible explicit-threshold differences, and document deployment of a changed register count.
- Fork destination: `nick-boss-tech/solr:solr-18598-submit`, a plain descendant of the reviewed PR head. No writes to the contributor's branch or upstream PR.
- Decision ladder: rung 1. `HLL.homogeneousUnion` assumes compatible register layouts and does not implement downsampling; inspect incoming serialized metadata and fail clearly before union. Preserve different explicit thresholds with matching layouts. Do not expand the shared HLL API or change StatsComponent.
- Windows restriction: no Gradle, compilation, formatting execution, or tests. Hand-format the patch; all execution, tidy, root checks, guide validation and fail-before evidence belong to Linux.

## Deliverables

1. Ticket-scoped source/test/documentation patch pushed to the fork branch, with remote SHA verified.
2. `gates/pr-5251-hll-precision-round-1.md`, pinning the resulting fork SHA, exact tests, formatting/check/guide requirements and fail-before leg at the original PR production code.
3. `reports/pr-5251-hll-precision-round-1.md`, recording the fork SHA, outbound files, review result and proof-pending handoff.

This claim is immutable. Completion is derived from its deliverables. The Linux host claims the gate separately and records its own runner, lock, worktrees, logs and fresh XML. No public comment is authorized by this implementation claim.
