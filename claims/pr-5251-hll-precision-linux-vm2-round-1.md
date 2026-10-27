# Claim: SOLR-18598 / PR 5251 HLL precision gate, round 1 (vm2, Linux)

Status: CLAIMED

- Claimant host: vm2.
- Tags: gate, premise-run, records.
- Started: 2026-10-27T21:10Z (UTC).
- Job: `gates/pr-5251-hll-precision-round-1.md` (board `01bd01f05dc`).
- Exact fork head F: `e7834ca954505c27778e59a61140bf02490226a6` on `nick-boss-tech/solr:solr-18598-submit` (verified by live `ls-remote`).
- Fail-before base P: `ca539cad196f26c78e14a1dc698b84e0f5c1f22e`, `elangelo/solr:json-facet-hll-precision` (verified by live `ls-remote`).
- Competing claims: `claims/pr-5251-hll-precision-windows-round-1.md` is the implementation claim (Windows, no execution). No other Linux claim exists for this job.
- Constraints: no GitHub, PR, JIRA or upstream write. No force push. Any tidy or source repair follows the job's superseding-gate rule; no edits to this job file. Commit identity Nick Shanin, no trailers.
- Host resources: JDK 21 `/workspace/jdk/jdk-21.0.12.1+1`, Gradle home `/workspace/gradle-home`, build lock `/workspace/tools/.gradle-vm2.lock` on every Gradle run, serialized.
- Deliverables: `receipts/SOLR-18598.md` and the superseding-gate file only if a repair is needed.
- Plan: detached positive worktree at F, tidy and tree cleanliness, focused and neighbor legs, standalone clone for `check -x test` and `checkSiteLinks`, fail-before worktree at P with two test files overlaid.
