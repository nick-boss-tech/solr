# Claim: listener UBI contract proof, Windows round 1

Status: CLAIMED (Windows implementation; Linux proof pending)

- Started: 2026-10-29 07:24:06 UTC.
- Source: Nick's direct request to build a patch or tests on the author's branch after reviewing the UBI adapter proposal.
- Reviewed board snapshot: `e16c19923cd2b3d9084ecd0ecf9d10c836f2e2bf`.
- Parent: PR 5238 head `aa03c9544968f1d9de9b6b36e2823bcbe76268b9`.
- Capability tags: implementation, review, records. Host: Windows. No Gradle or local test queue.
- Slice: one test-only UBI-style SearchComponent/listener adapter and integration contract tests on the real hook. This does not import PR 2452's unfinished UBI feature or assert that the current hook is defective.
- Fork branch: `solr-11994-pr5238-ubi-submit`; isolated worktree `wt/SOLR-11994-pr5238-ubi`.
- Intended outbound path: `solr/core/src/test/org/apache/solr/handler/component/SearchRequestListenerUbiTest.java` only.
- Deliverables: pushed scoped commit, `reports/pr-5238-ubi-contract-windows-round-1.md`, and `gates/pr-5238-ubi-contract-round-1.md` with exact-head Linux proof requirements.
- Acceptance: response/event query-ID equality and returned IDs for local/distributed searches; coordinator-only recording; handler/parameter opt-in; isolated sink failures; safe copied payload after embedded request closure; defined rejection of failed/partial searches.
- Existing client-dispatch gate/receipt is historical and proves a different branch. No ownership of that gate is claimed.
- Decision: implement a test consumer against the existing hook first. Request a hook change only if that consumer demonstrates an actual API limitation. No public PR/JIRA communication is authorized by this claim.
