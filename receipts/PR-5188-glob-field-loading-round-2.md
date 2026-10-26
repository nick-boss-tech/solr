# PR 5188 full snippet and field-loading proof, round 2: receipt (vm6)

- Superseding gate: `gates/pr-5188-glob-field-loading-round-2-superseding-409183a.md`. Original job `gates/pr-5188-glob-field-loading-round-2.md` (head H `cb8d3dcf0172515e11b92a9d1c7d9aa186f1364c`) is unchanged. Claim: `claims/pr-5188-glob-field-loading-round-2-vm6.md`. Repair: `reports/pr-5188-glob-field-loading-repair-round-2.md`.
- Host: vm6, Linux aarch64, Temurin OpenJDK 21.0.12.1+1, Gradle wrapper from the branch, serialized under `/workspace/tools/.gradle-vm6.lock`. Seed `5188C0DE` (log line `Running tests with randomization seed: tests.seed=5188C0DE`).
- Final head F5: `409183aa9be8c8500942eadf22d036abaf1ead5c` (tree `5e64cdfd0b887391c09ba2be4756dacdc836aee5`), read back from the fork after push. Author Nick Shanin `<nick.boss.us@gmail.com>`, no trailers. Production since `51e92b6`: unchanged. Only `BasicFunctionalityTest.java` changed at H (tidy formatting) and at F5.
- Pre-fix tree P: upstream production `e07455a0f953e702da27f677c51ea894b7bafc3a` (the three changed production files match its blobs) plus F5's `BasicFunctionalityTest.java` (blob `1a320e232f3d96510997476223d69949aadcce78`, sha256 `b2be8289da46dd7d3d16273a7512872162a8dcdddec52976a86143a38e63592e`) and `TestPseudoReturnFields.java` (blob `b81b328189af7f2a3c273009890c642ed946d44e`).

## Legs (UTC start-end, exit, fresh XML)

| Leg | Command (test filters) | rc | UTC | Result |
| --- | --- | --- | --- | --- |
| tidy at F5 | `tidy` | 0 | 18:27:50 to 18:30:00 | tree clean after (`tidy-tree: 0 changed`) |
| pre-fix snippets, P | `testGlobHighlightSnippets` | 0 | 18:30:03 to 18:32:18 | 1 test, 0 failures, 0 errors, 0 skipped. All 24 rows positive (both highlighters, implicit and `hl.q`, `weightMatches` true and false, `fl=source_s`, `source_*`, `*`) |
| pre-fix RTG, P | `testGlobRTGStoredFieldLoading` | 1 | 18:32:18 to 18:34:28 | fails: `AssertionError: unrelated_s should remain lazy` (after response-shape checks pass) |
| pre-fix prefetch, P | `testGlobHighlightPrefetch` | 1 | 18:34:28 to 18:36:39 | fails: `AssertionError: original: source_* did not prefetch source_s`. The finite `original` control passes first. |
| post-fix focused, F5 | the four methods | 0 | 18:36:40 to 18:39:23 | `BasicFunctionalityTest` 3 tests, 0/0/0; `TestPseudoReturnFields.testGlobRTGUncommittedFields` 1 test, 0/0/0 |
| post-fix suites, F5 | `BasicFunctionalityTest`, `TestPseudoReturnFields`, `TestRetrieveFieldsOptimizer`, `ReturnFieldsTest`, `TestUnifiedSolrHighlighter` | 0 | 18:39:23 to 18:41:36 | `BasicFunctionalityTest` 26/0/0/0; `TestPseudoReturnFields` 35/0/0/0; `TestRetrieveFieldsOptimizer` 1/0/0/0; `ReturnFieldsTest` 16/0/0/0; `TestUnifiedSolrHighlighter` 33/0/0/0 |
| compile with Error Prone, F5 | `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true` | 0 | 18:41:37 to 18:47:43 | executed |
| check, F5 | `:solr:core:check -x test` | 0 | 18:47:43 to 18:54:25 | module check, not repository-wide |

The XML counts are read from the leg XML directories, each cleaned before its leg. `BasicFunctionalityTest` has 26 test cases (25 before, plus `testGlobHighlightSnippets`).

## Evidence

- Durable copy: `reports/pr-5188-glob-field-loading-round-2-evidence/`, all 52 files from the run directory, including each leg's raw log, `status.txt`, `driver.sh` and fresh XML. Hashes: `reports/pr-5188-glob-field-loading-round-2-evidence/MANIFEST.sha256`.
- Local runner directory: `/workspace/gates/logs/vm6/pr5188-round-2-superseding-409183a/`.

## Limits

- Module `:solr:core` only, with the listed test classes. No full `:solr:core:test` and no whole-repository check.
- Logs contain the `java.security.policy` warnings from the security manager; not investigated.
- The positive snippet row set is a fixture precondition and does not discriminate by itself. The loading assertions carry the discrimination.
- Upstream PR 5188 head was not re-read in this round.

## Proposed incremental change description (draft, not posted)

RTG and lazy prefetch now honor glob field selection. RTG loads only the stored fields its response and transformers need. Lazy prefetch resolves glob matches before it adds highlight fields and the unique key. The pre-fix base fails the two new loading methods for the expected loading reasons, and the head passes them along with a 24-row snippet matrix that covers both highlighters. Uncommitted and realtime-searcher visibility is pinned by `TestPseudoReturnFields.testGlobRTGUncommittedFields`. No disk-I/O or latency claim is made. AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.
