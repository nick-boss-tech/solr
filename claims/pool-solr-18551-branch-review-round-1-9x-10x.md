# Claim: SOLR-18551 branch validation, round 1, 9.x row (vm2)

Status: DONE 2026-10-17 (verified by vm4 2026-10-18 (rule 6); deliverable reports/SOLR-18551-branch-review-round-1.md, 9x section; the JDK 21 base leg was run by vm4 on c135e6335c, logs/vm4/18551-9101-base2.log)

- Claimant host: vm2 (Linux). Assignment: assignments/pool-solr-18551-branch-review-round-1.md (posted 09feb24c97).
- Slice taken: the 9.x row only (head and base legs). vm2 holds one row at a time, so the 10x row is not claimed here and stays OPEN in the assignment for another host. The main row is vm4's (claims/pool-solr-18551-branch-review-round-1-main.md).
- Capability tags used: settling-run.
- Head at claim time (ls-remote): solr-18551-backport-9x d8f96dfeea3a.
- Base commits: 9.x c135e6335c7158fa26e96b0dc386f825255b47c0.
- Runner: gates/g18551-branch-9x-only.sh (g18551-branch-9x-10x.sh, narrowed to the 9.x row). Source changes: none. No PR, comment, JIRA or email.

Heartbeat: 2026-10-17T18:00Z (claim).
- 2026-10-17T18:20Z: 9.x head leg stalled at :buildSrc:jar (Gradle client and daemon idle for 30+ minutes, no test ran). Stopped by the coordinator; log logs/vm2/18551-9x-head.log and the result file record it. Not relaunched. The 9.x row is open again pending a decision on retry. Queue is free.

- 2026-10-17T19:20Z: RELEASED by vm4 (coordinator). Stale since the 18:20Z stall. Retry result for the 9x row is in reports/SOLR-18551-branch-review-round-1.md, as reported by vm2. The JDK 21 head leg is claimed by vm4 in claims/pool-solr-18551-branch-review-round-1-9x-jdk21.md.
