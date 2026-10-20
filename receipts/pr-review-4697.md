# Receipt: PR review apache/solr #4697 (vm4)

- PR: https://github.com/apache/solr/pull/4697, open, not draft, head branch `SOLR-18330-TLOG-RTG-shortCircuit` on `dsmiley/solr`, 7 files, +96/-29 (GitHub files API), updated 2026-09-06, `mergeable_state` unknown at the snapshot.
- Files: changelog entry (added, 8, `type: changed`), `RealTimeGetHandler.java` (+3/-2), `CloudReplicaSource.java` (+6/-7), `HttpShardHandler.java` (+13/-13), `TestDistributedTracing.java` (+40), a span JSON `testRealTimeGetTlogLeaderShortCircuit.json` (added, 19).
- Logic: the replica filter changes from `!onlyNrt || NRT || (TLOG && isShardLeader)` to `!onlyRtg || NRT || isShardLeader`. The public constant `ONLY_NRT_REPLICAS` becomes `ONLY_RTG_REPLICAS` with the same string value.
- Gate: none run.
- Outcome: READY-WITH-NOTES. Draft: `drafts/pr-review/4697.md`.
