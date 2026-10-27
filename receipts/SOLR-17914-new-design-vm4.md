# Receipt: SOLR-17914 new-design form (vm4)

Written by vm4. Assignment: `assignments/solr-17914-new-design-form-round-1.md`. Claim: `claims/solr-17914-new-design-form-round-1.md`. Report: `reports/solr-17914-new-design-round-1.md`.

- Branch: `solr-17914-create-collection-dialog-new-design` on the fork, new branch from `5ea0055190`.
- Gated head: `125448970db256fdecb802733abbde7e20d2b1a2`. Live tip: the same.
- Command: `./gradlew -p ui :shared:jvmTest :shared:spotlessCheck --no-daemon --rerun-tasks`, with `LD_LIBRARY_PATH=/workspace/tools/mesa-egl/lib`, under `/workspace/tools/.gradle-vm4.lock`.
- JDK 21.0.12.1, Gradle 9.7.0 by the repo wrapper.
- Verdict: PASS. rc 0, BUILD SUCCESSFUL in 1m 25s.
- Results from JUnit XML: 57 tests, 0 failures, 0 errors, 2 skipped (`UserAuthenticationContentTest`, `StartContentTest`, outside this change). `CreateCollectionFormTest` 8 of 8. `CollectionDetailsCardTest` 2 of 2.
- Mutation check: PULL-only rule and name rule broken one at a time in a run of `CreateCollectionFormTest`; the matching tests failed, and the file was restored. Logs: `/workspace/gates/logs/vm4/solr-17914-new-design-mutation/`.
- Logs: `/workspace/gates/logs/vm4/solr-17914-new-design/` (`full.log`, `junit/`, `rc.txt`, `head.txt`).
