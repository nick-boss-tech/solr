# Claim: SOLR-10667 premise run, then first gate (round 2, job 7)

Status: CLOSED 2026-10-17 (batch close approved by Nick; no heartbeat within 2 hours; listed in reports/claims-stale-batch-close-2026-10-17.md)

Claimant host: vm2. Assignment: assignments/pool-vm2-gate-backlog-round-2.md. Job: gates/SOLR-10667.md.
Capability tags: gate, premise-run. Staffing: 1.
Started: 2026-10-11T04:21:27Z (UTC).
Branch: solr-10667-submit at 32b594f280c5c5f0e5241dadbad7677cdb563c3e (live tip, verified by ls-remote at claim time).

Heartbeat: 2026-10-11T04:21:27Z.

Heartbeat: 2026-10-11T04:31:21Z (relaunch after first environment failure).

Result 2026-10-11T04:38:49Z: BLOCKED, environment, no test executed. Both assembleDist runs (base cabedd1d9680 with the pre-fix packaging.gradle and head 32b594f280c5) fail in :solr:webapp:js-client:jsClientDownloadDeps: "Cannot find package '@babel/plugin-syntax-dynamic-import'" from the npm build. The first base attempt also failed at configuration (worktree still being populated); it is not evidence. Premise not shown either way; no receipt written. Main agent decides whether to retry on a clean npm cache or with a build-flag change (not taken here).
BLOCKED: 2026-10-11T04:38:49Z (UTC), outcome BLOCKED (environment).

## 2026-10-13T19:18Z DONE (marked on verification by vm2 gate backlog coordinator)

Deliverable receipts/SOLR-10667.md names GATE GREEN at 9ce9ef1db5e9d33312ac628d2e3f0b236a474c36, and the job file carries vm1's status correction (2026-10-13T16:28Z). This is a record correction for the missed claim mark, not a claim transfer.

STALE: no live work by vm2 since 2026-10-18; marked by the vm2 coordinator so the pool tick does not resume it. Work not finished, see the job or assignment file.
