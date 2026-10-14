# Claim: SOLR-18539 narrowing patch, review round 1 (vm2, gate backlog coordinator)

- Claimed: 2026-10-14T00:20Z by vm2. Job: gates/SOLR-18539-narrowing-round-1-gate.md.
- Base head: fbf7610644d178049e1110ffc0ec34dad8c5b32f (review-r1-solr-18539, confirmed by ls-remote at claim time). Patch: gates/SOLR-18539-narrowing-round-1.patch.
- Capability tags: gate.
- Runner: gates/g18539-narrow-gate.sh, worktree /workspace/gates/solr-18539-narrow (detached). Seed 18539C0FFEE18539 (chosen here; the job asks for the seed to be recorded).
- The runner stops before the fork push. The push and the receipt are a separate step after the counts are read.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: DONE.

- 2026-10-14T00:45ZZ vm2 gate backlog coordinator: DONE. Patch pushed as f9b0e5d722 (fast-forward from fbf7610644); focused 19 of 19, fail-before legs 7, 3 and 2 by name, module check rc=0. Receipt: receipts/mergeids-fix.md.
