# Claim: PR 5108 reverse merge gate, round 3 at F2 (phone agent)

- Claimant host: phone agent (Linux, this VM); tags gate.
- Job: `gates/pr-5108-reverse-merge-round-3-f2-vm4.md` (immutable; the vm4 suffix is the file's origin, and routing is to any capable host per the owner's 2026-10-27 rule).
- Target: `nick-boss-tech/solr:solr-17786-reverse-cursor-submit` at exact head F2 `495d3fbdbe07bfb474b43d77b89f8c033bbeb8fd` (fork ref re-read at that head at claim time).
- Setup: fresh clone/worktrees per the job, JDK 21.0.12.1+1, Gradle 9.7.0 via solr-gradle.sh, host test-queue lock; queued behind the other claimed gates on this host.
- Deliverable: receipt per the job's completion section, path confirmed at execution time.
- Claimed: 2026-11-02 (America/Denver). Idempotency key: `pr-5108-reverse-merge-round-3-f2-exec@495d3fbdbe07bfb474b43d77b89f8c033bbeb8fd`.
