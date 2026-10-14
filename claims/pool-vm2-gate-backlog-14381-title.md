# Claim: SOLR-14381 changelog title correction and gate (vm2, gate backlog coordinator)

- Claimed: 2026-10-14T05:03Z by vm2. Job: gates/SOLR-14381-title-fix.md.
- Head: a28b3f672cb4daa7cc32833082b0f499f41e3a85 (solr-14381-submit, ls-remote matched). Replacement title is the one in the job file.
- Capability tags: gate, implementation (changelog title line only).
- Runner: gates/g14381-gate.sh, worktree /workspace/gates/solr-14381-title (detached). Pushes the title commit as a fast-forward to solr-14381-submit after green.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Result: GREEN. Title commit 7caf8d33d3bf29d38e3f16c38a01282982329e90 pushed to solr-14381-submit as a fast-forward; receipt refreshed.
- Status: DONE.
