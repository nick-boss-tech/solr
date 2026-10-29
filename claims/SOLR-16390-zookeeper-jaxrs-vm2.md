# Claim: SOLR-16390 ZooKeeper v2 APIs gate (vm2, Linux)

Status: CLAIMED

- Claimant host: vm2. Tags: gate.
- Started: 2026-10-29T07:00Z (UTC).
- Job: `gates/SOLR-16390-zookeeper-jaxrs.md`.
- Head: `66d3775134e8169952bd1c1d97a28befac7c29f6` on `nick-boss-tech/solr:solr-16390-zookeeper-jaxrs` (live `ls-remote` matched).
- Base: `74197140a666d4f0396b68484c2bf9df7e79221e` (origin/main).
- Host: JDK 21 `/workspace/jdk/jdk-21.0.12.1+1`, Gradle home `/workspace/gradle-home`, `--no-daemon`, build lock `/workspace/tools/.gradle-vm2.lock`.
- Clone: fresh blobless clone under `/workspace/worktrees/solr16390-zk`, not the shared store.
- Steps: changelog parse, tidy, Error Prone compile (`-Pvalidation.errorprone=true`), `:solr:core:check -x test`. No focused tests written on this job.
