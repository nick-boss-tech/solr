# Claim: solr-18600-dual-release-behavior-closure-round-2 (vm2)

- Claimant host: vm2.
- Status: ACTIVE.
- Job or assignment: `assignments/solr-18600-dual-release-behavior-closure-round-2.md` with its addendum `assignments/solr-18600-dual-release-behavior-closure-round-2-additive-addendum.md` (board `2408fb0d93e1`).
- Slice: ALL remaining round-2 Linux legs (A connect, B custom-client and ownership, C URL dispatch and protected closure, D successful requests), plus the consumer matrix re-run on the final head. Candidate: `nick-boss-tech/solr:solr-18600-additive` at `6f61b6dbaf96` (live ref verified 2026-10-30 at claim time). Base `a336bf871b2c`; original pin `c40665af8d6b`.
- Not in this claim: any push to a production branch, PR, Jira, GitHub comment, mailing list, or upstream action. Submission remains HOLD. The round-4 scope review is separate.
- Capability tags: gate, premise-run, implementation, records.
- Started: 2026-10-30T01:54:03Z (UTC). Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1.
- Idempotency key: solr-18600-dual-release-behavior-closure-round-2@6f61b6dbaf963627fb4e9d4f479c9e5f6d9979d3.
- Toolchain: JDK 21.0.12.1+1 at `/workspace/jdk/jdk-21.0.12.1+1`; Gradle 9.7.0 wrapper; JDK 17 at `/workspace/jdk/jdk-17.0.21+9` only if present, otherwise recorded as unrun.
- Lock: `/workspace/tools/.gradle-vm2.lock` (flock -n). Preflight 01:54 UTC: `/` 4.6G free, 9% inodes used. `/workspace` (NFS) reports 0 inode counters, recorded as an unverified guard input.
- Board snapshot read before claiming: `coordination-ui` at `ff3a810938df`. No competing round-2 claim found.
- Log and artifact directory: `/workspace/gates/logs/vm2/solr18600-closure-r2/`. Portable copies will be published to the board.

Heartbeat: 2026-10-30T01:54:03Z
