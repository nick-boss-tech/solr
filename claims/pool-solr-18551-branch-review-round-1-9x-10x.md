# Claim: SOLR-18551 branch validation, round 1, 9.x and 10x rows (vm2)

- Claimant host: vm2 (Linux). Assignment: assignments/pool-solr-18551-branch-review-round-1.md (posted 09feb24c97).
- Slice taken: the 9.x row (head and base legs), then the 10x row (head and base legs). The main row is vm4's (claims/pool-solr-18551-branch-review-round-1-main.md).
- Capability tags used: settling-run.
- Heads at claim time (ls-remote): solr-18551-backport-9x d8f96dfeea3a; solr-18551-backport-10x a786123b1031.
- Base commits: 9.x c135e6335c7158fa26e96b0dc386f825255b47c0; 10x 0712c1f7831a0b0c22837fa6a07fc028040e9bbe.
- Runner: gates/g18551-branch-9x-10x.sh, queued. Source changes: none. No PR, comment, JIRA or email.

Heartbeat: 2026-10-17T18:00Z (claim).
