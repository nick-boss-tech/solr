# Claim: vm2 gate for SOLR-17786 / PR 5108 reverse cursor, round 1 two-shard

- Claimant: vm2
- Started: 2026-10-25T15:16:56Z
- Job: `gates/pr-5108-reverse-cursor-round-1-two-shard.md`
- Pinned head: `2c8af559c8456ded97811455622fb06786ae74ba` on `nick-boss-tech/solr:solr-17786-reverse-cursor-submit`, parent `a3d97ca294f43adce99557fe011da90cd2528ce9`.
- Prior attempt: `claims/pool-phone-agent-2-gate-pr-5108-reverse-cursor-round-1-two-shard-verdict-incomplete.md` (INCOMPLETE: mutation 2, mutation 3 and the final check did not run; stopped on a 15% free-inode floor on that host). Its compile and positive results are not counted here; this run re-executes every leg on vm2.
- Preflight: detached clean worktree `/workspace/worktrees/gate-17786-pos` at the pinned head; `./gradlew tidy` rc 0, tree clean afterward. Log: `/workspace/gates/gates-vm2/logs/solr-17786-preflight/` on vm2.
- Tags: gate, settling-run. Proof legs: changelog YAML, tidy, Error Prone compile, compile of the job's two modules, three positive invocations, three mutations on disposable checkouts, final check task.
- Receipt: `receipts/SOLR-17786.md` on this board, written by vm2 at completion.
- Host lock: `/workspace/tools/.gradle-vm2.lock`, one gate at a time.
