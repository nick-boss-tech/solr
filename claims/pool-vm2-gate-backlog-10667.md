# Claim: SOLR-10667 premise run, then first gate (round 2, job 7)

Claimant host: vm2. Assignment: assignments/pool-vm2-gate-backlog-round-2.md. Job: gates/SOLR-10667.md.
Capability tags: gate, premise-run. Staffing: 1.
Started: 2026-10-11T04:21:27Z (UTC).
Branch: solr-10667-submit at 32b594f280c5c5f0e5241dadbad7677cdb563c3e (live tip, verified by ls-remote at claim time).

Heartbeat: 2026-10-11T04:21:27Z.

Heartbeat: 2026-10-11T04:31:21Z (relaunch after first environment failure).

Result 2026-10-11T04:38:49Z: BLOCKED, environment, no test executed. Both assembleDist runs (base cabedd1d9680 with the pre-fix packaging.gradle and head 32b594f280c5) fail in :solr:webapp:js-client:jsClientDownloadDeps: "Cannot find package '@babel/plugin-syntax-dynamic-import'" from the npm build. The first base attempt also failed at configuration (worktree still being populated); it is not evidence. Premise not shown either way; no receipt written. Main agent decides whether to retry on a clean npm cache or with a build-flag change (not taken here).
BLOCKED: 2026-10-11T04:38:49Z (UTC), outcome BLOCKED (environment).
