# Claim: SOLR-17241 wt response-format implementation, Windows round 1

- Source of work: Nick's 2026-10-29 instruction to implement and push the reviewed fix, then obtain a Linux gate receipt BEFORE any public comment. Prior review: receipts/solr-wt-non-hosting-review-round-1.md.
- Board snapshot: 41739430f8c21177e806f698b280318b09635820. Existing review complete; no competing implementation claim or solr-17241-submit branch found.
- Claimant: windows, Codex desktop; tags implementation, review, records.
- Started: 2026-10-29 UTC; exact timestamp is the claim commit.
- Base: upstream/main 9859d8bb2e8d63061116b8281639a4beb1be480c.
- Scope: use SOLR_QUERY_REQUEST params first and SOLR_PARAMS only as the missing-request fallback in MediaTypeOverridingFilter; preserve negotiation/error/exemption rules. Add focused filter tests, deterministic hosting/non-hosting HTTP regression and a Nick Shanin changelog fragment. No routing/body-parser changes or SOLR-16398 edits.
- Branch/worktree: new solr-17241-submit on the fork; local wt/SOLR-17241. Sparse checkout of outbound sources only; Linux gate uses a full checkout.
- Deliverables: pushed source/test/changelog commit; gates/SOLR-17241-wt-format-round-1.md pinned to its full SHA; reports/SOLR-17241-wt-format-implementation-windows-round-1.md; add-only done record.
- Verification: no Gradle or local queue on Windows. Review/format by hand; required fail-before/passing-after and build proof belongs to the Linux gate.
- Public communication: HOLD. A gate receipt is a prerequisite; no JIRA/GitHub comment or PR is authorized by this claim. Do not use the clean-gate PR-comment exception for a JIRA post.
- Decision ladder: reviewed behavior and upstream intent at rungs 1-3; rung 4 selected the filter-local fallback; Nick has now authorized implementation and the gate handoff.
