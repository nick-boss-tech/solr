# Claim: PR 5224 test helper reader lifetime, Windows round 1

Status: CLAIMED

- Claimant: windows, Codex desktop chat for PR 5224.
- Started: 2026-10-25T23:33:14Z.
- Tags used: implementation, review, records.
- Assignment: Nick's direct chat instruction on October 25, 2026: build a test helper without the reader-lifetime issue so it can be tested.
- Reviewed board snapshot: f7bfb7b2c18a0d12b6231ed44f147f5d7fef8343. No SOLR-18585 / PR 5224 assignment, gate, claim, or receipt was present.
- Input: apache/solr PR 5224, mkhludnev/solr:intervals-script, f28c6e79c82a9fb74c3e07607b86e74b3d4331d0.
- Scope: only solr/core/src/test/org/apache/solr/search/TestScriptFilterIntervalsSource.java. Make the helper borrow an already-open reader context and keep each reader alive through all assertions. Preserve all four existing cases.
- Output branch: nick-boss-tech/solr:solr-18585-submit, new fork branch based on the reviewed PR head. Do not modify the contributor's branch or the upstream PR.
- Decision ladder: rung 1 settles ownership. The helper currently closes the reader before returning reader-backed postings; the caller must own the reader until iteration finishes. No production semantics change is needed.
- Deliverables on this board: reports/pr-5224-test-helper-windows-round-1.md and gates/pr-5224-test-helper-round-1.md.
- Completion of this implementation claim means the scoped commit is pushed and the report plus exact-head Linux gate are posted. Runtime proof remains pending until a capable Linux gate host writes its receipt.
- No Gradle, tidy, test queue, or runtime proof on Windows. No public GitHub/JIRA communication is authorized by this implementation request.
