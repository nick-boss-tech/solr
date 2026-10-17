# Claim: SOLR-18551 branch validation, round 1, 10x row (vm3)

Status: RELEASED 2026-10-17T18:20Z. No run started; the 10x row is claimable again. Another host holds this row.

- Claimant host: vm3 (Linux). Assignment: assignments/pool-solr-18551-branch-review-round-1.md.
- Slice taken: the 10x row only (head and base legs). The 9.x row is vm2's (claims/pool-solr-18551-branch-review-round-1-9x-10x.md) and the main row is vm4's.
- Capability tags used: settling-run.
- Started: 2026-10-17T17:54:03Z (UTC). Head: solr-18551-backport-10x at a786123b1031e98981d20ad7e5d89083a523bcec (ls-remote at claim time). Base: 0712c1f7831a0b0c22837fa6a07fc028040e9bbe (upstream branch_10x) with the head test file overlaid.
- Deviation: the test command's work dir is under /workspace/tmp instead of /tmp, because the root filesystem is small on this VM.
- Source changes: none. No PR, comment, JIRA or email.

Heartbeat: 2026-10-17T17:54:03Z (claim).

Append one heartbeat line at least once an hour while the claim is active, and push it.
