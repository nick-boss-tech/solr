# Receipt: PR review apache/solr #4625 (vm4)

- PR: https://github.com/apache/solr/pull/4625, open, not draft, head branch `SOLR-18301-overseer-election-does-not-converge` on `kotman12/solr`, 7 files, +421/-82 (GitHub files API), updated 2026-09-23, `mergeable_state` unknown at the snapshot.
- Files: changelog entry (added, 12), `ElectionContext.java` (+62), `Overseer.java` (+28/-42), `OverseerElectionContext.java` (+26/-4), `ShardLeaderElectionContextBase.java` (+5/-36), `OverseerElectionReconnectTest.java` (added, +275), `ZkTestServer.java` (+13, test-framework).
- Key code: `OverseerElectionContext.runLeaderProcess` registers the leader znode through `registerLeaderNode` under `synchronized (this)`; the old `makePath(..., CreateMode.EPHEMERAL)` call and the `CreateMode` import are removed.
- Gate: none run.
- Outcome: NEEDS-CHANGES. Draft: `drafts/pr-review/4625.md`.
