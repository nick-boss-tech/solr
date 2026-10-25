# Claim: pool-phone-agent-2-gate-pr-5108-reverse-cursor-round-1-two-shard

- Claimant host: `phone-agent-2`, matching `hosts/phone-agent-2.md`.
- Status: ACTIVE.
- Job or assignment: `gates/pr-5108-reverse-cursor-round-1-two-shard.md`.
- Slice: Execute only the prescribed SOLR-17786 / PR #5108 gate at candidate head `2c8af559c8456ded97811455622fb06786ae74ba`. No #5107 work, public PR action, Jira action, or extra tests.
- Capability tags used: `gate`, `premise-run`, `settling-run`.
- Started: 2026-10-25T03:54:24Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat under `WORKFLOW.md`. Attempts: 1. Idempotency key: `pr-5108-reverse-cursor-round-1-two-shard@2c8af559c8456ded97811455622fb06786ae74ba`.
- Toolchain: Ubuntu 24.04.4 LTS; OpenJDK 21.0.12.1 at `/usr/lib/jvm/java-21-openjdk-amd64`; Gradle wrapper 9.7.0; `GRADLE_USER_HOME=/home/ubuntu/.gradle`.
- Preflight: detached `gate-preflight.sh` at `2c8af559c8456ded97811455622fb06786ae74ba` returned 0 and recorded `tidy clean`. Log directory: `receipts/SOLR-17786-gate-round-1-two-shard/preflight/`. Before launch, free inodes were 21.92%, no Gradle process was active, and the host lock was available. The runner emitted a nonfatal `/workspace/.env: No such file or directory` warning; the explicitly supplied token allowed the private fetch, and the completed preflight logs record a successful tidy run.
- Host lock: `/workspace/tools/.gradle-phone-agent-2.lock`. The clean detached test worktree at the new head will be created after this claim; no compile, gate test, mutation, or module-check step has run at this pin.
- Links: Ticket or parent: SOLR-17786; public PR #5108 head `32f80171030ca1108cd20292f50232d2729ed930`; candidate branch `solr-17786-reverse-cursor-submit`. Category: none; `pool/work-map.md` is absent on the refreshed board. Siblings: #5107 is explicitly sequenced after this gate and is out of scope. Supersedes: `claims/pool-phone-agent-2-gate-pr-5108-reverse-cursor-round-1-post-tidy.md`.
- Decision ladder: rung 3 (stated intent). The gate's explicit topology contingency directs a scoped harness repair and new pinned input if the selected fixture does not exercise the required multi-shard path; Nick directed that only this gate be completed and the gate's contingency settles this test-fixture-only change. No product logic or new test was added.

Heartbeat: 2026-10-25T03:54:24Z

Result: 2026-10-25T03:54:24Z: clean detached preflight completed at the superseding pin; this claim opens before compile/test/check execution. Gate run pending.
