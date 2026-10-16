# Claim: SOLR-8430 capped limiter replacement, retitle and re-gate (shared buffer, vm2 coordinator)

Claimant host: vm2. Queue: assignments/pool-shared-gate-backlog-buffer.md (vm2 claims end to end). Job: gates/SOLR-8430.md.
Capability tags: implementation, gate. Staffing: 1.
Started: 2026-10-12T03:48Z (UTC, claim pushed before any code change).
Branch: solr-8430-submit at 49af21be58927a62b50bd7ba1df1688110fba63e (live tip, verified by ls-remote at claim time).
Decision: reports/replication-backup-round-1-g3.md, finding 1 (cap) and finding 2 (retitle), adopted per the owner decisions list, item 1, with one limiter per rate value (the branch's grain).

Heartbeat: 2026-10-12T03:48Z. Code read next (ReplicationAPIBase.java lines 99-110), then the implementation commit.

## 2026-10-12T03:50Z heartbeat (vm2 coordinator)

Implementation pushed to the fork as a fast-forward: solr-8430-submit at 094f0716a8a3673fa9e2e0e9e8e4ad40cb2db122 (from 49af21be589). Gate running (runner /workspace/gates/g8430-gate.sh, log /workspace/gates/logs/vm2/8430-gate.out).

## 2026-10-12T04:19Z FAILED at 094f0716a8 (vm2 coordinator)

Gate ended 04:12:44Z: FAILED, not green. Failing test: ReplicationRateLimiterTest.testZeroMeansUnthrottled (test fixture order dependence on the static limiter map, seed 8430C0FFEE008430). Classification and evidence in gates/SOLR-8430.md, "Gate run at 094f0716a8". Claim not DONE. Not relaunched.

Next step recorded: a narrow test-fixture fix on solr-8430-submit. At 04:18Z a Gradle build started in /workspace/gates/solr-8430 from a session other than this coordinator run, with an uncommitted fix there: a production hook `clearSharedRateLimitersForTest()` in ReplicationAPIBase.java plus a @Before in the test. This coordinator did not start that build and has not touched that worktree. Note for its owner: the hook does not exist at the pre-cap production file 49af21be58, so the cap leg would fail at compile, not by content. Clearing the field by reflection in the test keeps the cap leg valid. vm2 stays busy until that build ends.

## 2026-10-12T04:21:48Z heartbeat (vm2 coordinator)

The 04:18Z build recorded above was the tidy run started by the vm2 session that committed 0d64c35c5a. The note about a foreign build is withdrawn. Patch pushed at 0d64c35c5a (test-only seam plus @Before; see gates/SOLR-8430.md, "Patch at 0d64c35c5a"). Re-gate running; the cap leg uses the seam appended by the gate runner. Claim still open until the gate ends.

## 2026-10-12T04:47Z DONE (vm2 coordinator)

Gate GREEN at 0d64c35c5a (gate ended 04:41:31Z; record in gates/SOLR-8430.md, "Gate run at 0d64c35c5a"). Deliverables: receipts/SOLR-8430.md refreshed at 0d64c35c5a, gates/SOLR-8430.md marked DONE. Branch unchanged on the fork at 0d64c35c5a.

Status: CLOSED (vm2 audit, 2026-10-16T00:55Z). Job gates/SOLR-8430.md is DONE on pr-prepare; no work remains under this claim. Earlier status lines are kept as history.
