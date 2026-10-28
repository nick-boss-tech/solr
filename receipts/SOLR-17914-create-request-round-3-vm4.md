# Receipt: SOLR-17914 create request and error mapping (vm4)

Written by vm4. Assignment: `assignments/solr-17914-create-request-round-3.md`. Claim: `claims/solr-17914-create-request-round-3.md`. Draft: `drafts/pr-solr-17914-create-collection-dialog.md`.

- Branch: `solr-17914-create-collection-dialog-new-design` on the fork. Previous head `39efaef802`; gated head `6170ba10ac5061203339fa0a96e714c7e64616c2`. Pushed as a fast-forward.
- Command: `./gradlew -p ui :shared:jvmTest :shared:spotlessCheck --no-daemon --rerun-tasks`, with `LD_LIBRARY_PATH=/workspace/tools/mesa-egl/lib`, under `/workspace/tools/.gradle-vm4.lock`.
- Verdict: PASS. rc 0, BUILD SUCCESSFUL in 1m 55s.
- JUnit: 66 tests, 0 failures, 0 errors, 2 skipped (`UserAuthenticationContentTest`, `StartContentTest`, outside this change). `HttpCollectionsRepositoryTest` 5 of 5; `CreateCollectionFormTest` 9 of 9; `CreateCollectionErrorsTest` 3 of 3; `CollectionDetailsCardTest` 2 of 2.
- Mutation: duplicate-name match widened to any 400 with a message. `testAliasConflictIsNotMappedToNameExists` and `testKeepsTheServerMessageForOtherFailures` failed; file restored. Logs: `/workspace/gates/logs/vm4/solr-17914-create-request-round-3/mutation/`.
- Logs: `/workspace/gates/logs/vm4/solr-17914-create-request-round-3/` (`full.log`, `junit/`, `rc.txt`, `head.txt`). An earlier gate attempt in the same directory was interrupted before BUILD and is kept in `attempt-2/`.
