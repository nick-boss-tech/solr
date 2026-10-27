# Claim: SOLR-18325 Solr 10 client feasibility, round 1 (vm2)

Status: CLAIMED

- Claimant host: vm2, per `hosts/vm2.md`.
- Tags: implementation, gate, premise-run, review.
- Started: 2026-10-27T04:05Z (UTC).
- Job: `gates/solr-18325-solr10-feasibility-round-1.md` (fork commit e1b7b5eeb2). Incoming prototype head: `aa8cc9ab29a012da2ec4e302cae73238e7e78a1f` on `nick-boss-tech/solr:solr-18325-submit` (two commits above base `f45b38d5f6c0`, which is apache/solr branch_10x, in-progress 10.2.0). Verified: fork branch fetched and head present.
- Released baseline: Maven Central `org.apache.solr:solr-solrj:10.0.0` (reachable). Solr 10.0.0 distribution: archive.apache.org (reachable).
- Competing claims on the board: `claims/solr-18325-solr10-prototype-windows-round-1.md` and `claims/solr-18325-impact-windows-round-1.md` (Windows preparation). No Linux claim exists.
- Host constraints found: no Java 17 on the host (JDK 11 and 21 only). Temurin 17 aarch64 is being downloaded to `/workspace/jdk/jdk17`. `/tmp` has about 4.6G free and `/workspace/solr/.git` is 4.9G, so the candidate checkout goes under `/workspace/gates/solr18325/` (a fresh worktree for tests, a full clone only for the root check).
- Constraints: no GitHub, PR, Jira, or upstream write. Commits as Nick Shanin with no trailers. Any prep commits push to the fork branch only, under the job's preparation-phase rules. No force push.
- Build lock: `/workspace/tools/.gradle-vm2.lock`. Each gradle step runs under flock with NODE_ENV unset and `--no-daemon`.
