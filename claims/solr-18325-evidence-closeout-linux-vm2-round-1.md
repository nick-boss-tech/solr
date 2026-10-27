# Claim: SOLR-18325 evidence closeout, Linux round 1 (vm2)

Status: CLAIMED

- Claimant host: vm2.
- Tags: gate, premise-run, review, records.
- Started: 2026-10-27T17:58Z (UTC).
- Job: `gates/solr-18325-evidence-closeout-round-1.md` (fork commit `8db594649cf`).
- Candidate, unchanged: `nick-boss-tech/solr:solr-18325-submit` at `8bfca8edd30acf11bdf9d6a733312e97f99ec78a`.
- Exact pre-Jackson base: `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`.
- Released baseline: Maven Central solr-solrj and solr-api 10.0.0.
- Competing claims: none for this Linux job. `claims/solr-18325-evidence-closeout-windows-round-1.md` is the Windows handoff and runs no commands.
- Deliverables: `receipts/SOLR-18325-evidence-closeout-round-1.md`, `reports/solr-18325-evidence-closeout-linux-round-1.md`, `reports/solr-18325-http-compatibility-prerequisite-design-round-1.md`.
- Constraints: no GitHub, PR, Jira or upstream write. No production shim, restoration or requirement waiver. No force push. Fork pushes only to `solr-18325-submit` if a source change is separately justified, which this job does not do. Mutants stay in scratch worktrees and are never pushed.
- Host resources: JDK 21 `/workspace/jdk/jdk-21.0.12.1+1`, JDK 17 `/workspace/jdk/jdk17`, Gradle home `/workspace/gradle-home`, build lock `/workspace/tools/.gradle-vm2.lock` on every Gradle run. Maven will be installed under a user-owned tools directory.
- Plan: install and verify Maven, run a real Maven consumer on the candidate artifacts, run three discriminating mutants with the fixed-head positive control, run a binary API comparison of released, base and candidate, then write the prerequisite design.
