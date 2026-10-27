# Claim: rc3-smoke-loop-round-1

- Claimant host: vm5, matching `hosts/vm5.md`.
- Status: CLAIMED.
- Job or assignment: owner request in chat, 2026-10-24: run the Solr 10.1.0 RC3 smoke tester (the command in the vote thread) in a loop until near the vote close, count the runs, and report any failing test with its log.
- Slice: the whole loop. Sequential runs, one at a time, each under `flock /workspace/tools/.gradle-vm5.lock`, one fresh temp directory per run under `/workspace/smoke-rc3/tmp/`.
- Owner decisions: (1) the loop may hold the vm5 Gradle lock for about 80 minutes per run; (2) temp directories on `/workspace`, since `/` has 4.6 GB free; (3) stop starting runs at Mon 2026-10-26 20:00 MDT (2026-10-27 02:00 UTC). A run in progress at that time finishes.
- Capability tags used: sweep. No code change, no post, no email, no vote.
- Started: 2026-10-24T08:02Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: rc3-smoke-loop-round-1@vm5.
- Links: release 10.1.0 RC3, tip 749712b719, artifacts under https://dist.apache.org/repos/dist/dev/solr/solr-10.1.0-RC3-rev-749712b71940c9f96d824c533f32f5c56dcd3924. Siblings: rc3-vote-note-round-1 (DONE). Supersedes: none.
- Decision ladder: rung 4 for the run shape. Fresh temp directory per run, to match the vote-thread process. Passing runs delete their temp directory; failing runs keep theirs for inspection. Repeats are the point of this claim; the WORKFLOW rule against repeating failed runs does not apply to this loop.
- Results: `/workspace/smoke-rc3/runs/results.tsv` (one line per run) and the logs under `/workspace/smoke-rc3/runs/`. The final report goes to `reports/rc3-smoke-loop-round-1.md`.

Heartbeat: 2026-10-24T08:02Z


Released as stale by vm5 (overnight tick, 2026-10-27). Rung 1: last heartbeat 2026-10-24T08:02Z, hourly heartbeat required; named assignment none n/a. The claim is closed for intake. The assignment status is unchanged.
