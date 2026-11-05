# Claim: SOLR-18608 DocValues prefetch follow-up

Status: CLAIMED (Windows implementation; Linux proof pending)
Started: 2026-11-05 20:40 UTC
Host: windows (Codex desktop)
Tags: implementation, review, records
Authority: Nick requested "Patch this branch and then hand it off to the gate" in the PR 5298 review chat.
Board snapshot: 3a860c33f4f5ac750c7c80cac7cffaded38a2297

Input: nick-boss-tech/solr:pr-5188-glob-field-loading-followup at 7ee68a7000b67bed06ad3130ffff27a086a56aad, the current open draft PR 5298 head. Existing historical PR 5188 claims/gates cover older heads and a different phase; no current claim for this DocValues slice was found.

Slice: preserve the response optimizer's DocValues-only path when prefetching partial globs, cover nonstored DV, stored/DV substitution, empty selection, ordinary stored fields and highlight inputs, and document the existing lazy-wrapper proof limit. Reuse the existing test class and schema. No Windows Gradle or tests.

Decision ladder: rung 1. The existing RetrieveFieldsOptimizer substitutes eligible stored fields with DocValues and skips stored documents; the new prefetch currently runs before that substitution. Keep RTG's candidate stored-field selector semantics intact and have prefetch consume the final search retrieval plan.

Deliverables:
- A scoped ordinary commit pushed fast-forward to the same fork branch, verified through a separate remote ref read. The public draft PR head will move.
- reports/pr-5298-dv-prefetch-implementation-round-1.md
- gates/pr-5298-dv-prefetch-round-1.md naming the exact pushed head, focused tests, fail-before proof, tidy, Error Prone and root check, with receipts/PR-5298-dv-prefetch-round-1.md owned by the Linux execution host.

This claim completes when the implementation report and gate job exist. Code remains Implemented, Proof Pending until all required Linux proof legs pass at the final head.
