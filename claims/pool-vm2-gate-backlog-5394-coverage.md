# Claim: SOLR-5394 coverage commit gate (vm2, gate backlog coordinator)

- Claimed: 2026-10-15T01:44ZZ by vm2. Head: 437b818067a467 (solr-5394-submit, test-only commit, 13 lines in SimpleFacetsTest).
- Capability tags: gate (no push; the fork tip is already the head).
- Runner: gates/g5394-coverage-gate.sh, worktree /workspace/gates/solr-5394-coverage (detached). Seed 224D384E56AEA306, the seed of the 2026-10-14 focused run at 6e4c302c41 so counts compare.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: STOPPED, NOT RUN (2026-10-15T01:55Z). Head-check found the fork tip at 74532d0f76a6, not 437b818067a4, so the gate stopped without a test run. Earlier run killed by the runtime restart with a stale index lock, cleared. Left for the main agent: the branch owner must decide the new tip.

- 2026-10-15T02:33ZZ vm2 gate backlog coordinator: STOPPED, not gated. Fork tip moved from 437b818067 to 74532d0f76 (docs-only: faceting.adoc, 2 lines) before the run. The coverage gate was queued three times and the runs overlapped on the same worktree path (index.lock error at 01:48Z). Needs a decision on whether to gate 74532d0f76 instead.

- 2026-10-15T03:02:49Z vm2 gate backlog coordinator: GATE GREEN at fork tip 74532d0f76 (run 02:34:43Z to 02:55:14Z, log logs/vm2/queue-g5394-coverage-gate-20261015T023443Z.out; rc lines 0; SimpleFacetsTest 50 tests, 1 skipped, 0 failures, 0 errors). The gate script was pointed at 74532d0f76 at 02:34:43Z with no decision recorded here, so this claim is not marked DONE. Owner decision needed: accept 74532d0f76 as the gated head, or gate 437b818067 separately. No push, no branch change.
