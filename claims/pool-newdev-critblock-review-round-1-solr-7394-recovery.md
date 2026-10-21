# Claim: pool-newdev-critblock-review-round-1, unit 24 (SOLR-7394)

- Claimant host: vm3, matching a file in `hosts/`.
- Assignment: `assignments/pool-newdev-critblock-review-round-1.md`, unit 24, Critical.
- Branch: `solr-7394-recovery`, head `a92a0d89b20908` (ls-remote at claim time, matches the round table). Base for this review: its own merge base `56ec140e3636d5f4150fa87fbf7103529536ac99` (the first parent of `2b90ab0738`). The round's `14c7aac0d1` base adds unrelated main changes to the diff (rung 1: the branch's own changes are 9 files, 386 insertions, 3 deletions). Receipt: receipts/SOLR-7394.md (a live PR exists on apache/solr; this is a consistency pass; the receipt's proof is not discriminating, both target classes fail to compile at base).
- Status: DONE. Three review passes, each with a regate.
- Capability tags: review, gate.
- Started: 2026-10-21T08:41:09Z (UTC).

Heartbeat: 2026-10-21T08:41:09Z

Result: 2026-10-21T14:38:39Z: passes 1 to 3 GREEN at a92a0d89b20908 (vm3). Mutation probe fails ZkShardTermsRecoveryFailureTest as intended (fb3f8ff3ea).

DONE: 2026-10-21T14:38:39Z. Deliverables: reports/newdev-critblock-review-round-1/solr-7394-recovery.md, receipts/SOLR-7394.md (passes 1 to 3), gates/SOLR-7394-review-pass-1.md, -pass-2.md, -pass-3.md. Final head a92a0d89b20908.
