# Claim: SOLR-18325 released binary baseline attribution, round 1 (vm2, Linux)

Status: CLAIMED

- Claimant host: vm2, per `hosts/vm2.md`.
- Tags: gate, premise-run, review, records.
- Started: 2026-10-27T17:05Z (UTC).
- Job: `gates/solr-18325-binary-attribution-round-1.md` (fork commit `9d8a65fe807`).
- Candidate, unchanged: `nick-boss-tech/solr:solr-18325-submit` at `8bfca8edd30acf11bdf9d6a733312e97f99ec78a`.
- Exact pre-Jackson base: `f45b38d5f6c0c9f7f1204f6d518aaad18a4a88b7`.
- Released control: Maven Central `solr-solrj` 10.0.0.
- Competing claims: none on the board for this job. Claims for `solr-18325-binary-attribution-windows-round-1` and `solr-18587` belong to other hosts and cover different work.
- Deliverables: `receipts/SOLR-18325-binary-attribution-round-1.md` and `reports/solr-18325-binary-attribution-linux-round-1.md`. The round 2 receipt, report and assignment stay unchanged.
- Constraints: no GitHub, PR, Jira, or upstream write. No waiver, no shim, no production source change. No force push. Commits as Nick Shanin with no trailers, if any commit is needed.
- Host resources: JDK 21 `/workspace/jdk/jdk-21.0.12.1+1`, JDK 17 `/workspace/jdk/jdk17`, Gradle home `/workspace/gradle-home`, build lock `/workspace/tools/.gradle-vm2.lock` on every Gradle run.
- Plan: copy round 2 legacy consumer bytes (verify SHA-256), publish the base artifact from a detached base tree, resolve a base consumer runtime and the released runtime outside the harness, run the same old class bytes against three runtimes on Solr 10.0.0 standalone, and collect descriptors with javap.
