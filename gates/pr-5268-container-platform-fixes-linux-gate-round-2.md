# Gate job: PR 5268 container platform fixes, superseding gate at H1, round 2

Status: POSTED by vm5, 2026-10-31 00:59 UTC. Immutable once posted. Supersedes the H0 gate handoff gates/pr-5268-container-platform-fixes-round-1.md for proof purposes; H0 is not proof for H1.

- Ticket: none; original PR https://github.com/apache/solr/pull/5268.
- Fork branch: nick-boss-tech/solr, pr-5268-container-platform-fixes.
- Pinned final source head H1: a18d913f2908f0ddf244b43384bd3caecbe9bc6e (metadata repair commit on top of H0 83a5c0d6e9f8758e26dddc217d7a3eec2339600c).
- Phase A record: reports/pr-5268-container-platform-fixes-linux-phase-a-round-1.md.
- Claiming host: vm5 (Linux 6.1 aarch64). Claim: claims/pr-5268-container-platform-fixes-linux-round-1.md.
- Worktree: /workspace/worktrees/pool-vm5-5268 (positive legs); detached negative worktree for Phase C to be named in its own log.
- Logs: /workspace/gates/logs/vm5/pr-5268-container-platform-fixes-round-1/.
- Proof legs: as in gates/pr-5268-container-platform-fixes-round-1.md, Phase B 1 to 7 and Phase C, seed 5268C0DE, fresh XML per leg, no -x test except the root check.
- Known blocker on this host: no Docker or Podman. Phase B legs 4 (DeleteByQueryToIdTest) and 5 (S3OutputStreamTest) cannot pass on vm5. GREEN for H1 needs a Docker-capable host to run those two legs; without them this job is not GREEN.
