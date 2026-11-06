# Claim: SOLR-18602 alias opt-out control test, round 1

Status: CLAIMED (Windows, implementation/review/records, 2026-11-06 04:13 UTC)

- Source: direct owner instruction in Codex, 2026-11-06 UTC.
- Anchor: apache/solr PR #5259, SOLR-18602, public head `ec8e706a41a0213dd924b286b1f5ebfc476f7cd0` (verified against live PR and fetched PR ref).
- Slice: add one test to `DeleteReplicaTest` proving that count-based deletion does not resolve a simple alias when `followAliases=false`; assert the request is rejected with the alias collection name and no replicas are removed.
- Branch: `nick-boss-tech/solr:solr-18602-pr5259-alias-optout-test`, based on the contributor's exact PR head. The contributor branch and public PR remain untouched.
- Outbound file: `solr/core/src/test/org/apache/solr/cloud/DeleteReplicaTest.java` only; no production or changelog change.
- Decision ladder rung 1: the count path resolves `extCollectionName` only when `FOLLOW_ALIASES` is true; otherwise it passes the alias name to `ClusterState.getCollection`, which returns BAD_REQUEST for an unknown collection. SOLR-18602 and PR #5259 are scoped to the opt-in true case. The new test pins that false remains opt-out and cannot delete from the alias target.
- Verification handoff: coverage-only. Linux gate must show the original `DeleteReplicaTest` suite and suite with the new test both pass; an isolated mutation that forces alias resolution despite `followAliases=false` must make the new assertion fail at runtime. Windows runs no Gradle or tests.
- Reviewer: Nick Shanin (requesting owner). No PR opening/comment, JIRA write, or upstream write is included.
