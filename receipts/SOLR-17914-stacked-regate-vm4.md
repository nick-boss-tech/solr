# Receipt: SOLR-17914 stacked head re-gate (vm4)

Written by vm4. Assignment: `assignments/christos-reply-17914-pr-prep-round-1.md`. Claim: `claims/christos-reply-17914-pr-prep-round-1.md`.

- Branch: `solr-17914-create-collection-dialog-stacked` on the fork, base `solr-17913-collection-details-card` at `0a04d2239e`.
- Gated head: `5ea0055190c7e0857c42a3d4ef354e200c250b32`. Live tip at the time of the run: the same.
- Clean worktree at the head: `/workspace/worktrees/christos-17914-gate`, removed after this run.
- Command: `./gradlew -p ui :shared:jvmTest :shared:spotlessCheck --no-daemon --rerun-tasks`, with `LD_LIBRARY_PATH=/workspace/tools/mesa-egl/lib` (no GL driver on this host), under `/workspace/tools/.gradle-vm4.lock`.
- JDK 21.0.12.1, Gradle 9.7.0 by the repo wrapper.
- Verdict: PASS. `rc=0`, BUILD SUCCESSFUL in 2m 2s.
- Results from the JUnit XML: 53 tests, 0 failures, 0 errors, 2 skipped. `CreateCollectionFormTest` 4 of 4. `CollectionDetailsCardTest` 2 of 2. The skipped tests are `UserAuthenticationContentTest` and `StartContentTest`, both outside this change.
- Logs: `/workspace/gates/logs/vm4/christos-17914-stacked-regate/` (`full.log`, `junit/`, `rc.txt`, `head.txt`, `df-before.txt`, `df-after.txt`).

The earlier run `/workspace/gates/logs/vm4/stacked-17914-full/` (receipt `receipts/SOLR-17914-stacked-on-17913.md`) compiled from `/workspace/worktrees/solr-17660`, a worktree that no longer exists, so its log does not show which checkout it built. This run compiled from the clean worktree at the head, and it agrees with that receipt.
