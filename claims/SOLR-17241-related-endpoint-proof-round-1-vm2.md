# Claim: SOLR-17241 related backup and ADDREPLICA response proof, round 1 (vm2)

- Claimant host: vm2, matching `hosts/vm2.md` and `hosts/pool-linux.md`.
- Status: CLAIMED, execution starting. Job: `gates/SOLR-17241-related-endpoint-proof-round-1.md`.
- Start (UTC): 2026-10-31T00:25Z.
- Production head: `86d86e3d3e54230b5f3e7b6aa758a189b3b2ed6b`, verified equal to `origin/solr-17241-submit` (nick-boss-tech/solr) before the claim.
- Parent control: `9859d8bb2e8d63061116b8281639a4beb1be480c` (verified present locally).
- Idempotency key: `SOLR-17241-related-endpoint-proof-round-1@86d86e3d3e54230b5f3e7b6aa758a189b3b2ed6b`.
- JDK: `/workspace/jdk/jdk-21.0.12.1+1`. GRADLE_USER_HOME `/workspace/gradle-home`. Every Gradle run under `flock -n /workspace/tools/.gradle-vm2.lock` with `env -u NODE_ENV`.
- Candidate worktree: `/workspace/worktrees/pool-vm2-17241-cand` (detached at the pinned head, plus a test-only overlay). Control worktree: `/workspace/worktrees/pool-vm2-17241-ctrl` (parent blobs for `MediaTypeOverridingFilter.java` and `MessageBodyWriters.java` only).
- Logs and artifacts: `/workspace/gates/logs/vm2/solr17241-related-endpoint-proof-round-1/`.
- Heartbeat: once an hour while the claim is active.
- Attempts: 1. Capability tags used: gate, premise-run. No PR, public comment, JIRA action, or production edit. Nothing pushed to `solr-17241-submit`.
