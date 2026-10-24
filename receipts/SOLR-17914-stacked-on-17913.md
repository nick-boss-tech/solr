# Receipt: SOLR-17914 stacked on SOLR-17913

- Written by vm4, the gating host. Claim: `claims/pool-ui-SOLR-17914-stacked-gate-vm4.md`.
- Branch: `solr-17914-create-collection-dialog-stacked` on the fork. Base: `solr-17913-collection-details-card` at `0a04d2239e`. Commits: `98b0e82629` (create collection form, strings deduplicated against 17913) and `5ea0055190` (draft PR description).
- Gated head: `5ea0055190c7e0857c42a3d4ef354e200c250b32`. Live tip: the same.
- Verdict: PASS at the gated head.
- Command: `./gradlew -p ui :shared:jvmTest :shared:spotlessCheck --no-daemon --rerun-tasks`, with `LD_LIBRARY_PATH=/workspace/tools/mesa-egl/lib` (no GL driver on this host), under `/workspace/tools/.gradle-vm4.lock`.
- JDK 21.0.12.1. Gradle 9.7.0 by the repo wrapper.
- Results: 17 suites, 53 tests, 0 failures, 0 errors, 2 skipped. The skipped tests are `UserAuthenticationContentTest` and `StartContentTest`, both outside this change. `spotlessCheck` passes. Build 1m 20s.
- Logs on vm4: `/workspace/gates/logs/vm4/stacked-17914-full/` (`full.log`, `junit/`).
- Relation to other receipts: the unstacked head `a502747ec4` has its own vm6 receipt (`receipts/SOLR-17914.md`), not changed here. The stacked head differs from it only by the removal of two duplicate string names, which are already defined by 17913.
- Blast radius: not flagged.

## PR description draft

Draft on the branch at `pr-draft/SOLR-17914.md` (in `5ea0055190`). Not opened as a PR. This PR must merge after SOLR-17913.
