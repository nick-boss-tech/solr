# Claim: pool-vm3-gate-backlog-16290

- Claimant host: vm3, matching a file in `hosts/` (`hosts/vm3.md`).
- Status: DONE (see the DONE line below).
- Job or assignment: `gates/SOLR-16290-fix.md`.
- Slice: the whole job (fix, pin removal, full gate, records refresh).
- Capability tags used: implementation, gate.
- Started: 2026-10-17T04:32:43Z (UTC).

Heartbeat: 2026-10-17T04:32:43Z. Base confirmed: `solr-16290-submit` at `ff760120c81792022458a5a290b6e58d57b9cd17` (git ls-remote). JDK 21.0.12.1 (Temurin) installed at `/workspace/jdk/jdk-21.0.12.1+1`; Gradle caches under `/workspace`. No build started yet.

Append one heartbeat line at least once an hour while the claim is active, and push it.

Result: 2026-10-17T05:53:33Z: GATE GREEN at fix head d15e7700fd2d86ad0daa13db7120c5d87fae1d4a. Fail-before on pre-fix production: 1 of 9 fails (the pin, empty stars buckets). Focused 9/9, facet package 108 tests 0 failures, Error Prone and module check pass. Receipt: receipts/SOLR-16290.md.

DONE: 2026-10-17T05:55:00Z. Deliverables: receipts/SOLR-16290.md, gates/SOLR-16290-fix.md (DONE), solr-16290-submit d15e7700fd2d86ad0daa13db7120c5d87fae1d4a, changelog/unreleased/SOLR-16290-fix-nested-child-facet-exclusion.yml.

Heartbeat: 2026-10-17T05:29:51Z. Worktree `/workspace/worktrees/solr-16290` at `ff760120c81792022458a5a290b6e58d57b9cd17` (base confirmed). JDK 21.0.12.1 installed at `/workspace/jdk/jdk-21.0.12.1+1`; Gradle caches under `/workspace/gradle-home`. Root before first build: 4.6G free of 7.8G, 481403 inodes free. Gradle wrapper warm-up running under `flock /workspace/tools/.vm3-gradle.lock`. Root filesystem note: this VM was reprovisioned, the 04:32 heartbeat predates it.

Heartbeat: 2026-10-17T05:30:17Z: gate in progress on fix commit 5c80b4b587 (solr-16290-submit, local, not pushed). Error Prone compile flagged a reference comparison; fixing and re-running tidy, Error Prone, module check and facet package. Facet package (org.apache.solr.search.facet.*) and module check passed on 5c80b4b587 before the fix. Logs under /workspace/gates/logs/vm3/16290-*.log.
