# Claim: SOLR-9864 review gate, javadoc placement (vm1, gate lane)

- Claimed: 2026-10-14T21:08:00Z by vm1. Job: gates/SOLR-9864-review-javadoc-placement.md.
- Head before the patch: b5826e466b9a8664adf11e0f247cff220f2f5953 (solr-9864-submit, live PR #5150 head; verified by ls-remote at claim time).
- Capability tags: gate.
- Runner: ~/workspace/tools/g9864-review-gate.sh, worktree ~/workspace/solr-worktrees/g9864-review (detached at the head, patch committed there). Log: ~/workspace/tools/g9864-review-gate.log.
- Batch context: third of the OPEN gate jobs the main side directed vm1 to run serially on 2026-10-14 while vm2 is having trouble. Claimed in advance of its run so the batch order holds; the gate itself starts after the SOLR-6831 gate in the same batch finishes.
- Heartbeat: appended below as the batch progresses; staleness rule 2 hours.
- Status: CLAIMED, queued behind the SOLR-6831 gate in the vm1 batch.

## Heartbeats

- 2026-10-14T21:08:00Z claim recorded; batch jobs 1 and 2 ahead of it.
- 2026-10-14T21:56:00Z heartbeat: patch committed in worktree g9864-review (move-only diff, 17 test methods). Runner g9864-review-gate.sh launched.
