# Claim: pool-skip-repro-14159-round-1

- Claimant host: vm4, matching a file in `hosts/` (`hosts/vm4.md`).
- Status: ACTIVE; mark DONE with the deliverable paths when the work lands.
- Job or assignment: `assignments/pool-skip-repro-14159-round-1.md`.
- Slice: the whole assignment (three runs of TestCloudConsistency at the pinned head, log search, SOLR-13486 read).
- Capability tags used: sweep, review.
- Started: 2026-10-18T04:41:00Z (UTC).
- Build protocol: the Gradle lock for this host is `/workspace/tools/.vm4-gradle.lock`, per the operator. `pool/bin/sweep-run.sh` takes `/workspace/tools/.gradle-vm4.lock` instead, so runs are wrapped in `flock` on the vm4 lock directly. Env from `/workspace/tools/vm4-env.sh` (`~/.pool-env` is absent on this host). Logs under `/workspace/gates/logs/vm4/`.

Heartbeat: 2026-10-18T04:53:00Z.

Result: 2026-10-18T04:52:35Z: `:solr:core:test --rerun --tests org.apache.solr.cloud.TestCloudConsistency` at head 498a09aea9 passed, 2 of 2 tests, 0 failures, seed E71A306C9B3EC191. Log `/workspace/gates/logs/vm4/14159/run1.log`, XML under `/workspace/gates/logs/vm4/14159/run1.xml/`. Conclusive as one pass. No signature matches. Runs 2 and 3 not started: the Decisions cadence row allows one run and a rerun only on failure. Main agent to confirm whether more passes are wanted.

DONE: 2026-10-18T04:53:00Z. Deliverables: `reports/2026-10-18-skip-repro/D2-14159.md`.
