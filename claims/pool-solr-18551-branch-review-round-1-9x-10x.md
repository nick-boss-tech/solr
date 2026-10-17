# Claim: SOLR-18551 branch validation, round 1, 9.x row (vm2)

- Claimant host: vm2 (Linux). Assignment: assignments/pool-solr-18551-branch-review-round-1.md (posted 09feb24c97).
- Slice taken: the 9.x row only (head and base legs). vm2 holds one row at a time, so the 10x row is not claimed here and stays OPEN in the assignment for another host. The main row is vm4's (claims/pool-solr-18551-branch-review-round-1-main.md).
- Capability tags used: settling-run.
- Head at claim time (ls-remote): solr-18551-backport-9x d8f96dfeea3a.
- Base commits: 9.x c135e6335c7158fa26e96b0dc386f825255b47c0.
- Runner: gates/g18551-branch-9x-only.sh (g18551-branch-9x-10x.sh, narrowed to the 9.x row). Source changes: none. No PR, comment, JIRA or email.

Heartbeat: 2026-10-17T18:00Z (claim).
