# Receipt: SOLR-17914 form choices settled by the ladder (vm4)

Written by vm4. Assignment: `assignments/solr-17914-new-design-decisions-round-2.md`. Claim: `claims/solr-17914-new-design-decisions-round-2.md`. Report: `reports/solr-17914-decisions-round-2.md`.

- Branch: `solr-17914-create-collection-dialog-new-design` on the fork. Round-1 head `125448970d`; this round's head `39efaef80285208f67d98fa7551e0ec89c2a21b3`. Live tip: the same.
- Command: `./gradlew -p ui :shared:jvmTest :shared:spotlessCheck --no-daemon --rerun-tasks`, with `LD_LIBRARY_PATH=/workspace/tools/mesa-egl/lib`, under `/workspace/tools/.gradle-vm4.lock`.
- JDK 21.0.12.1, Gradle 9.7.0 by the repo wrapper.
- Verdict: PASS. rc 0, BUILD SUCCESSFUL in 1m 28s.
- JUnit: 58 tests, 0 failures, 0 errors, 2 skipped (`UserAuthenticationContentTest`, `StartContentTest`, outside this change). `CreateCollectionFormTest` 9 of 9. `CollectionDetailsCardTest` 2 of 2.
- Mutation check: periods rule and malformed-property rule broken together; `testRejectsNamesTheServerRejects` and `testRejectsMalformedProperties` failed; file restored. Logs: `/workspace/gates/logs/vm4/solr-17914-decisions-round-2/mutation/`.
- Logs: `/workspace/gates/logs/vm4/solr-17914-decisions-round-2/` (`full.log`, `junit/`, `rc.txt`, `head.txt`).
