# vm2 queue monitor

## 2026-10-10 17:32 UTC

First snapshot. No prior monitor state. HEAD is 32660e04b7.

Pool commits since the pool opened, newest first:

- 32660e04b7 Nick Shanin. vm2 onboarding checks: host capabilities and smoke test.
- 704012e95c Nick Shanin. Windows review pass on the assignment pool: vm2 claim recorded, no claim taken.
- 3c218214fe Nick Shanin. Claim: VM2 onboarding checks.
- 0adfc6d261 Nick Shanin. Windows review pass on the assignment pool: no claim taken.
- 7b877b0107 Nick Shanin. Workflow: open assignment pool for all agent hosts, with pilot assignments.

Older history is not replayed.

Claims. claims/pool-vm2-onboarding-checks.md is DONE. Claimant host is vm2. Heartbeat is 2026-10-10T17:24:00Z. Deliverable is hosts/vm2.md. That heartbeat is under 2 hours old, so the claim is not stale.

The other 65 claim files have no heartbeat line. They predate the pool heartbeat rule. They are flagged here as lacking heartbeats, not as active work, and they are not taken over.

Gates. SOLR-18530 is RUNNING on vm1. SOLR-18531 is QUEUED on vm1 behind SOLR-18530. SOLR-18532 is QUEUED on vm1 behind SOLR-18530. No gate is FAILED. No receipt exists yet for those three tickets. The receipt set is 317 files. The latest commit added none.

Unclaimed assignments (no claims file with the same slug): open-update-29-prs, pool-admin-ui-premise-runs, pool-solr-16630-testcoordinatorrole-fix, update-processing-groups-cd-review, update-processing-not-gated-review. The last two have slice claims under other names. They still have no claims file matching the assignment slug.

hosts/vm2.md no longer says the onboarding checks are pending. Capabilities are confirmed on 2026-10-10: review, draft, implementation, gate, premise-run, settling-run, sweep, bats. Full gate capability is not yet proven. The smoke test covered changelog parse and tidy only, at the SOLR-17987 receipt head 38abf64231.

## 2026-10-10 17:41 UTC

Changed since the 17:32 snapshot. Origin moved from f11ed56f874 to 3219aac3f85. Two new commits, both by Nick Shanin:

- 3219aac3f8 Claim: SOLR-16630 TestCoordinatorRole fix. Adds claims/pool-solr-16630-testcoordinatorrole-fix.md.
- 66ddf1ba18 Windows review pass on the assignment pool. Updates hosts/windows.md and adds a windows review report.

The SOLR-16630 claim names vm2 as claimant, capability tags implementation and gate, started 2026-10-10T17:40:00Z. Its heartbeat is current, so it is not stale. The assignment's gate job file gates/SOLR-16630.md does not exist yet.

Gates are unchanged. SOLR-18530 is RUNNING on vm1. SOLR-18531 and SOLR-18532 are QUEUED on vm1 behind it. No gate is FAILED. No new receipts.

Stale claims: none. The claims with a timestamped heartbeat are the SOLR-16630 claim and the vm2 onboarding claim, both under 2 hours old. The 65 older claims without a heartbeat line are unchanged from the 17:32 snapshot. They are flagged, not taken over.

Unclaimed assignments: open-update-29-prs, pool-admin-ui-premise-runs, update-processing-groups-cd-review, update-processing-not-gated-review. pool-solr-16630-testcoordinatorrole-fix is now claimed.

hosts/vm2.md is unchanged and still reports the onboarding checks as done.

No action taken by vm2.
