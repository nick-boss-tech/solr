# Claim: SOLR-11356 SJ-R1-49 header copy, patched gate (vm2, gate backlog coordinator)

- Claimed: 2026-10-14T10:34:37Z by vm2. Job: gates/SOLR-11356-sj-r1-49-header-copy.md.
- Head before the patch: 6120dae28d04a07763f44d4bc5e9d9793af025f6 (solr-11356-submit). The patch is gates/SOLR-11356-sj-r1-49-header-copy.patch.
- Capability tags: gate.
- Runner: gates/g11356-header-copy-gate.sh, worktree /workspace/gates/solr-11356-header-copy (detached at the head). Fork push only as the fast-forward the job names after the patched gate is green.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: CLAIMED, queued behind the SOLR-17055 gate work; fix and test not yet written.
