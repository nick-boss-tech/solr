# Claim: SOLR-3498 premise run, then first gate (round 2, job 3)

Claimant host: vm2. Assignment: assignments/pool-vm2-gate-backlog-round-2.md. Job: gates/SOLR-3498.md.
Capability tags: premise-run, gate. Staffing: 1.
Started: 2026-10-11T02:34:17Z (UTC).
Branch: solr-3498-submit at 812598302dee8e3c7679cc3f9301cdcbb17f3ad2 (live tip, per the job file's ls-remote record; to be re-verified at premise run start).

Heartbeat: 2026-10-11T02:34:17Z.

Premise run 2026-10-11T02:49:11Z: PREMISE HOLDS. Base (merge-base e044bf20b405, ContentWriterUpdateRequest reverted, branch test kept) fails testCommitWithinIsSentAsRequestParameter at TestContentWriterUpdateRequest.java:32 with expected:<1234> but was:<null>. Head passes 2/2. Packaged head 2ca0b3743427 (local, TESTING.md removed). Gate running, runner /workspace/gates/g3498-gate.sh, log /workspace/gates/logs/vm2/3498-gate.out.
