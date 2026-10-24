# Receipt: SOLR-17660 on apache main (new base)

- Written by vm4, the gating host. Claim: `claims/pool-ui-SOLR-17660-upstream-base-vm4.md`.
- Branch: `solr-17660-basic-list-upstream` on the fork. Base: `upstream/main` at `13c693d805`. Commits: component `f526fb4bc7` (cherry-picked, was `1e8aca0ffc`) and draft PR description (cherry-picked, was `3ab8f52d01`).
- Gated head: `e1d7bfde5bb381ed97802c8f328d3419ba24f679`. Live tip: the same.
- Supersedes for PR purposes: `receipts/SOLR-17660.md` (head `1e8aca0ffc` on the fork's main). That receipt is kept as it is. The PR should use this branch.
- Verdict: PASS at the gated head.
- Command: `./gradlew -p ui :shared:jvmTest :shared:spotlessCheck --no-daemon --rerun-tasks`, with `LD_LIBRARY_PATH=/workspace/tools/mesa-egl/lib`, under `/workspace/tools/.gradle-vm4.lock`. JDK 21.0.12.1, Gradle 9.7.0.
- Results: 16 suites, 52 tests, 0 failures, 0 errors, 2 skipped (`UserAuthenticationContentTest`, `StartContentTest`, outside this change). `SolrListTest` 5 of 5 included. `spotlessCheck` passes. Build 1m 16s.
- Logs on vm4: `/workspace/gates/logs/vm4/17660-upstream/`.
- Upstream UI churn: none. No commit on `upstream/main` since `c8ab71430f` touches `ui/`, so the base change adds no UI drift.
- Blast radius: not flagged.

## PR description draft

Draft at `pr-draft/SOLR-17660.md` on this branch. Not opened as a PR.
