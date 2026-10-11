# Claim: SOLR-10390 premise run (round 1, tranche 1, job 12)

Claimant host: vm2. Assignment: assignments/pool-vm2-gate-backlog-round-1.md. Job: gates/SOLR-10390-premise.md.
Capability tags: premise-run (BATS). Staffing: 1.
Started: 2026-10-11T01:25:00Z (UTC).

Heartbeat: 2026-10-11T01:25:00Z.

Result 2026-10-11T01:49Z: premise HOLDS on the test body. Base cabedd1d96 bin/solr fails at refute 'Please install lsof' (test_start_solr.bats:129). Head 4af4a6834e bin/solr passes the body; the bats report is "not ok" only from teardown (solr stop --all, 30s wait, force-killed on this host; reproduced manually on base and head). Receipt receipts/SOLR-10390.md; no gate run.
DONE: 2026-10-11T01:49Z (UTC), outcome PREMISE HELD.

## Full-gate claim (shared backlog)

Claimant host: vm3. Assignment: assignments/pool-shared-gate-backlog-buffer.md. Job: gates/SOLR-10390.md.
Capability tags: gate, bats. Staffing: 1.
Branch: `solr-10390-submit` at `4af4a6834e2b43251381027c24b18761fc5a9513` (live tip verified by ls-remote at claim time).
Merge-base: `cabedd1d968059215188f4e7563fb303241899ed`.
Runner: Linux; BATS 1.10.0 at `/usr/bin/bats`. Worktree: `/workspace/gates/solr-10390-build`. Logs: `/workspace/gates/logs/vm3/10390-*.log`.
Started: 2026-10-11T07:54:04Z (UTC).
Heartbeat: 2026-10-11T07:54:04Z (UTC).
Status: CLAIMED. Packaging, changelog parse, proof, and focused BATS run remain; Java checks are not applicable per the gate file.
