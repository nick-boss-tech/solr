# Lucene block join missing values: Windows implementation

- Started: 2026-11-05 UTC; host: windows, Codex desktop.
- Authority: Nick requested preparation of a Lucene patch for the defects found while reviewing Solr PR 5293; preparing the fork and branch is part of that request. Public Lucene issues, comments and PR creation are not authorized here.
- Tags: implementation, review, records.
- Base: apache/lucene main `aa366ae209ae8296dd70d2835c6d47f8fc201972`.
- Slice: count missing values only among selected child documents, and preserve the SortedDocValues contract when a missing string wins selection. Add direct iterator and actual sort regression tests using existing join test infrastructure.
- Evidence: `receipts/pr-5293-child-sort-missing-round-1.md`, including the 10.4 addendum at board snapshot `e966ddf964a32732b28c16149a3174fa2a3d0f26`. Filtered child ordering passes on the 10.4 Solr stack and fails on 10.6; string missing-first changes from wrong order to assertion; numeric requested placement improves on 10.6.
- Scope: local Lucene checkout, branch `fix-block-join-missing-values`; fork `nick-boss-tech/lucene` to be created for Nick's review. The fork does not currently exist (API 404). No matching open Lucene PR found for ToParentDocValues or block-join missing values.
- Decision ladder: rung 1, source, iterator contracts and reproduced failures establish the bounded repair; preserve the contributor's separate parent missing-value choice. No claim of runtime proof for new Lucene code on Windows.
- Deliverables: pushed Lucene branch, `reports/lucene-block-join-missing-values-windows-round-1.md`, `gates/lucene-block-join-missing-values-round-1.md`, local technical handoff for Nick. Linux/JDK 25 must supply Lucene main proof. No Gradle or tests on Windows.
- Lucene AI_POLICY.md was read: coding assistance is permitted, human ownership is required, and public PR body/comments must be human-written. Nick must review/explain the patch and author the public contribution; no AI-written maintainer comment or autonomous PR will be published.
