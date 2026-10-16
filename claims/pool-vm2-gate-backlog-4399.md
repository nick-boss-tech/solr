# Claim: SOLR-4399 implementation plus first gate (shared buffer, vm2 coordinator)

Claimant host: vm2. Queue: assignments/pool-shared-gate-backlog-buffer.md (clarification 2026-10-11, vm2 claims end to end). Job: gates/SOLR-4399.md.
Capability tags: implementation, gate. Staffing: 1.
Started: 2026-10-11T19:32Z (UTC).
Branch: solr-4399-submit at de6cc6b27edded2836898f6fb91b39cd11344f6a (live tip, verified by ls-remote at claim time).

Heartbeat: 2026-10-11T19:32Z.

Note: the job file says to push implementation commits before the gate steps. This claim holds the fork push until the gate is green, per the queue monitor rule that no branch is pushed to the fork before green.

Local implementation commit 82c0c11373d (on solr-4399-gate, worktree /workspace/gates/solr-4399, not pushed to the fork; pushes wait for green per the monitor rule). Changes: init guard moved to build(); FileBasedSpellCheckerTest case renamed to testMissingSourceLocationIsRejectedAtBuild; changelog retitled. Gate runner /workspace/gates/g4399-gate.sh (proof worktree /workspace/gates/solr-4399-proof at de6cc6b27edd), log /workspace/gates/logs/vm2/4399-gate.out. Started 2026-10-11T19:37Z (UTC).

Heartbeat: 2026-10-11T19:37Z.

Heartbeat: 2026-10-11T19:42Z (UTC), queue monitor: gate still running (compile step, log growing), no action taken.
- Heartbeat: 2026-10-11T19:51Z (queue monitor): gate still running, log growing; proof leg failed as predicted (FileBasedSpellCheckerTest, sourceLocation message), now at focused-head. No action taken.

DONE 2026-10-11T20:03Z (UTC), queue monitor. Outcome: GATE GREEN at local head 82c0c11373d009f773980ba7f2d512e87e817eec (gate end 19:56:39Z). Proof leg failed by content as predicted; focused tests 4 of 4 pass at head; changelog, tidy, compile and module check green. Receipt: receipts/SOLR-4399.md. Fork push of solr-4399-submit not done: held for a main-side go, since the job asks for it before the gate and the monitor rule allows fork pushes only on green.

Push: 2026-10-11T20:35Z (UTC), queue monitor. Fast-forward de6cc6b27e..82c0c11373 pushed to solr-4399-submit under the main-side go recorded above. Remote now at 82c0c11373d.

Status: CLOSED (vm2 audit, 2026-10-16T00:55Z). Job gates/SOLR-4399.md is DONE on pr-prepare; no work remains under this claim. Earlier status lines are kept as history.
