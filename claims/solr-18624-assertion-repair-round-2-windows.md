# Claim: SOLR-18624 assertion repair after follow-up review, round 2 (Windows)

- Status: LIVE; implementation only, Linux proof pending.
- Claimant host: windows. Tags: implementation, review, records.
- Started: 2026-11-05 05:04:13 UTC.
- Authority: Nick's direct chat instruction, "Try to patch this", following the review of `c610e6d05db5700835a123de54da61137478e48f`. This is a new implementation slice following the completed publication-only job in `assignments/solr-18624-flaky-multiword-synonyms-round-1.md`; it does not change that assignment's original boundaries.
- Board snapshot checked: `643a8482733c749fa1b9b327a01fd2f0195bc2a9`. No competing SOLR-18624 implementation or gate claim was found. The publication claim's deliverable already exists.
- Source branch: `nick-boss-tech/solr:solr-18624-flaky-multiword-synonyms`.
- Starting fork head: `c610e6d05db5700835a123de54da61137478e48f`; upstream base `07ccc73cd24e26e59da169ca35b3fd5f64c35986`.
- Planned Windows worktree: `C:\Users\shaninna\dev\Solr-issues\wt\SOLR-18624-assertion-repair`.
- Scope: only `solr/core/src/test/org/apache/solr/search/TestMultiWordSynonyms.java`. Require baseline score differences proving `bf`; restore reversed-phrase scoring coverage for `bar foo`; assert the specific slop clauses for both requests, without requiring an arbitrary winner for `tropical cyclone foo`.
- Decision ladder: rung 1, code and existing query expectations distinguish the one-bigram and two-bigram requests. Runtime discrimination remains a Linux gate requirement, not a claimed Windows result.
- Deliverables: scoped ordinary commit and verified non-forced fork push; `reports/solr-18624-assertion-repair-round-2.md`; `gates/solr-18624-assertion-repair-round-2.md`. Those records close this implementation claim; the executing Linux host owns `receipts/SOLR-18624-assertion-repair-round-2.md`.
- No Gradle, compilation or tests on Windows. No PR, JIRA write or public comment is part of this slice.
