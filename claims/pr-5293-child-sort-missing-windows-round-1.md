# PR 5293 childfield missing-value follow-up, Windows round 1

- Status: CLAIMED, source implementation; Linux proof pending.
- Started: 2026-11-05 UTC.
- Host: windows, Codex desktop; tags: implementation, review, draft, records.
- Authority: Nick's direct request to review PR 5293 briefly and produce patches on a branch for a handoff comment.
- Contributor parent: `978e2b1e352c7c39a68f7b952e6ab12eabf80890`; fork branch planned: `pr-5293-solr-18622-child-sort-missing`.
- Board reviewed: `f7a545452944a322ff6d4f94925b463899eeec7e`; no matching assignment, claim, or gate for this PR or ticket. Numeric substring matches in unrelated logs are not assignments.
- Slice: correct the parent missing-value argument in ChildFieldValueSourceParser; extend the existing TestNestedDocsSort fixture with executable missing-value cases. Preserve contributor ancestry and commit/push to Nick's fork only.
- Correction: the earlier review's M1 default-child regression claim is withdrawn. Actual Lucene 10.4 source already skips missing children, and Lucene 10.6's five-argument constructor delegates with null childMissingValue. The verified gap is inconsistent configured handling between a missing child and a parent with no selected child value.
- Decision ladder: rung 1, actual Lucene 10.4/10.6 constructor and comparator implementations, plus Solr's schema missing-value settings. Preserve the PR's child behavior and apply the same setting to parents. Runtime regression proof is assigned to Linux.
- Deliverables: `reports/pr-5293-child-sort-missing-windows-round-1.md`, `gates/pr-5293-child-sort-missing-round-1.md`, pushed source branch, local updated review and comment draft. No public comment until required proof is complete or Nick approves a draft with its limits.
