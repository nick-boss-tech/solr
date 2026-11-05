# SOLR-18621 / PR 5291 deeper review and fixes, round 1

- Status: CLAIMED for Windows source review and implementation; Linux proof pending.
- Claimant: windows, Codex desktop. Started: 2026-11-05 06:09 UTC.
- Capability tags: implementation, review, records.
- Authority: Nick's direct chat instruction, "Try to do a deeper review then implement a patch for the issues." This supersedes the read-only implementation limit for item 1 of `assignments/pr-5291-review-adjacency-scan-since-2026-08-01-round-1.md` for this follow-up. No adjacency census work is claimed.
- Board snapshot reviewed: `dac7bb450b563de85307ae99154b201bb9869a73`, refreshed before posting to `92d6bee8fa759999c8c52a843c4da6eeee88276e` (one unrelated report added). Prior review: `reports/pr-5291-review-round-1.md`. No overlapping implementation or gate record found.
- Contributor parent and current live head: `600e0ec4afd82004a94852c419892897be14e6f0`; PR base `82abb86bb5b7326fe48c117e4de69981a43e4251`.
- Task branch: `solr-18621-params-factory-fixes`; worktree `wt/PR-5291-params-factory-fixes`. Preserve contributor ancestry and plain-push only to `nick-boss-tech/solr`.
- Deliverables: `reports/pr-5291-params-factory-fixes-round-1.md`, `gates/pr-5291-params-factory-fixes-round-1.md`, and local `research/branch-reviews/2026-11-05-pr-batch/PR-5291-deeper-review.md`.

## Decisions and planned scope

Rung 1 settles immutable isolation and existing null-literal compatibility: protect arrays from all accessor paths without changing shared MultiMapSolrParams semantics; keep existing `ModifiableSolrParams.of(null)` source valid. Rename the proposed varargs method on both classes to `ofPairs(String...)`, since an inherited `SolrParams.of(String...)` would still make the subclass call ambiguous. Keep zero and single-pair `of` overloads. Rung 3 settles null policy: retain the ticket's explicit null-to-empty-string behavior; the author comment is an open preference, not a finalized alternative contract.

Blast radius before implementation: SolrParams.java (private immutable implementation, renamed factory), ModifiableSolrParams.java (renamed pair factory), the six existing helper files (delegate name only), SolrParamTest.java and ModifiableSolrParamsTest.java (immutability regressions, null source compatibility, factory contract), and changelog/unreleased/SOLR-18621.yml (factory name and Nick Shanin author). No new public class, test fixture or dependency. Tests reuse the existing focused suites. Wider SolrQuery duplicate-value semantics are independent and remain outside this patch.

Windows runs no Gradle, compiler, formatting tool or tests. Publish the exact pushed head and a Linux gate covering focused suites, runtime fail-before immutability, null source compilation, helper consumers, tidy and root check. Keep Implemented, Proof Pending until a matching receipt exists. No upstream public write is part of this claim.
