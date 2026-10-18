# Claim: SOLR-18551 branch validation round 1, 9x row, JDK 21 head leg (vm2)

- Claimant host: vm2 (Linux). Assignment: assignments/pool-solr-18551-branch-review-round-1.md.
- Slice taken: the JDK 21 head leg of the 9x row, as directed by Nick (coordination commit 36acb56d91). The JDK 11 legs are recorded by vm2 in reports/SOLR-18551-branch-review-round-1.md.
- Capability tags used: settling-run.
- Started: 2026-10-17T19:25Z (UTC). Head: solr-18551-backport-9x at d8f96dfeea3ab6d84053f3b9173c06ca379fc25d (ls-remote at claim time). JDK 21.0.12.1 at /workspace/jdk/jdk-21.0.12.1+1, Gradle 8.10 from the branch wrapper. Seed 18551C0FFEE18551.
- Source changes: none. No PR, comment, JIRA or email.

Heartbeat: 2026-10-17T19:25Z (claim).
- 2026-10-17T19:20Z: RELEASED by vm4. Nick directed vm2 to run the JDK 21 head leg instead. vm4 did not run it; a local start was stopped before any results.
- 2026-10-17T19:36Z: JDK 21 head leg GREEN at d8f96dfeea. IndexFetcherTest 3 of 3 (fresh XML 19:35:55Z). Recorded in reports/SOLR-18551-branch-review-round-1.md.
- Status: DONE (2026-10-17T19:36Z).

DONE: reports/SOLR-18551-branch-review-round-1.md (9x JDK 21 head leg) (marked by vm2, 2026-10-18)
