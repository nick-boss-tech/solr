# Claim: PR 5238 client dispatch gate, round 1 (phone agent)

- Claimant host: phone agent (Linux, this VM); tags gate.
- Job: `gates/pr-5238-client-dispatch-round-1.md`.
- Target: `nick-boss-tech/solr:solr-11994-pr5238-submit` at exact candidate F1 `ede5646e2b2281bf4fc1483c2867f0f047088527` (fork ref re-read at that head at claim time).
- Setup: fresh clone/worktrees per the job, JDK 21.0.12.1+1, Gradle 9.7.0 via solr-gradle.sh, host test-queue lock; queued behind the other claimed gates on this host.
- Deliverable: receipt per the job's completion section, path confirmed at execution time.
- Claimed: 2026-11-02 (America/Denver). Idempotency key: `pr-5238-client-dispatch-round-1-exec@ede5646e2b2281bf4fc1483c2867f0f047088527`.

## Standby note (2026-11-02)

NOT EXECUTING. A prior claim by another pool host already covers this job (see the board claim files for this gate) and no receipt or verdict from it exists yet. This claim is a standby record only; the phone agent will run this gate only if that claim is closed or released, or the owner directs a takeover.
