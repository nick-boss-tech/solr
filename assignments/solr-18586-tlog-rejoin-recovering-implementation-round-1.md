# Assignment: SOLR-18586 root cause and fix, TestTlogReplica (implementation, round 1)

Status: OPEN
Tags: implementation, premise-run, records
Intended host: any pool Linux host with the `implementation` tag (per its `hosts/` file).

Owner direction (2026-10-25): the RC3 failures must be fixed, the fixes applied to RC3, and the result run through the smoke tester. This item has no fix and no confirmed cause. Bring the branch back to the owner for gate; the gate itself is not this assignment.

## Facts (from vm5's round, do not re-derive)

- Failing methods: `TestTlogReplica.testKillLeader`, `testRecovery`, and `testOnlyLeaderIndexes` (intermittent: failed on main, passed on RC3 once). Test file: `solr/core/src/test/org/apache/solr/cloud/TestTlogReplica.java`. `doReplaceLeader` is at lines 438 to 487; the failing wait for `testKillLeader` is at line 484, "Expected collection to be 1x2", after the old leader is restarted.
- Reproduces at the RC3 tip `749712b71940c9f96d824c533f32f5c56dcd3924` (seed `3635CC89D23D0929`, 2 failed) and at apache/main `13c693d805f` (seed `9D6BB87C4F145405`, 3 failed). Logs: `/workspace/smoke-rc3/tlog-repro/rc3-tip-run1.log`, `main-run1.log`. Report: `reports/rc3-tlogreplica-rootcause-round-1.md`.
- Captured state at the failure: the rejoining TLOG replica (`core_node2`) stays `"state":"recovering"`; the leader (`core_node4`) is active. Both nodes are live.
- Ruled out (`reports/rc3-tlogreplica-peersync-probe-round-1.md`): setting `firstTime = true` in `RecoveryStrategy.java` line 572 (letting TLOG take peersync first) does not change the outcome at seed `9D6BB87C4F145405`.
- Code read so far, not traced at runtime: `ZkController.java` around lines 1504 to 1512 (TLOG non-leader startup path skips `recoverFromLog`) and lines 1834 to 1855 (when non-leaders call `doRecovery`); `RecoveryStrategy.java` around lines 571 to 600 and 716 to 720.

## Work

1. Base: RC3 tip, host lock, record the JDK. Reproduce `testKillLeader` at seed `3635CC89D23D0929` first.
2. Root-cause with probes (rung 2), not by guessing. The useful question is why the rejoining TLOG replica never leaves `recovering`: which recovery attempt runs, what it waits on, and what the leader publishes. Instrument those points on a scratch tree and report what the captured state shows.
3. Fix only what the probes support, on a branch named `solr-18586-tlog-rejoin-recovering` off the RC3 tip, pushed to the fork. If the cause is not established, say so and stop; do not ship a speculative fix.
4. Matched pair (owner's rule, `assignments/rc3-proof-pair-requirement-round-1.md`): the base leg is the RC3 failure at seed `3635CC89D23D0929`; the fixed leg is the same method and seed on the fix branch. Archive each run's XML to a per-run directory before the next run in the same worktree.
5. Report: `reports/solr-18586-tlog-rejoin-fix-round-1.md`, with the probes, the root cause or "not established", the diff, the matched pair, and the branch name and head SHA.

## Rules

- Never force-push. Fork branch only. No PR, no JIRA or GitHub comment, no dev-list text.
- One host lock at a time. Record each decision with its rung (`material/decision-ladder.md`).

## Deliverables

- `reports/solr-18586-tlog-rejoin-fix-round-1.md`
- The fix branch `solr-18586-tlog-rejoin-recovering` on the fork, with its head SHA in the report (or the report says no fix branch because the cause was not established).
