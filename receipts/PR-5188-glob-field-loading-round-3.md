# PR 5188 current API and regression boundaries, round 3: receipt (vm6)

- Superseding gate: `gates/pr-5188-glob-field-loading-round-3-superseding-555e1bf.md`. Original job `gates/pr-5188-glob-field-loading-round-3.md` (head H `f00caeb5e83ac375672ae27fb43ee1546795e98b`) unchanged. Claim: `claims/pr-5188-round-3-gate-vm6.md`. Repair: `reports/pr-5188-glob-field-loading-repair-round-3.md`.
- Host: vm6, Linux aarch64, Temurin OpenJDK 21.0.12.1+1, Gradle wrapper from the branch, serialized under `/workspace/tools/.gradle-vm6.lock`. Seed: `Running tests with randomization seed: tests.seed=5188C0DE`.
- Final head F6: `555e1bf2398c08b1018ed44c056274c727bd6454` (tree `031fc2c3d7029bdd025102280cd33160e14bc093`), read back from the fork after push. Author Nick Shanin `<nick.boss.us@gmail.com>`, no trailers. Production identical to H.
- Ancestry verified: current PR production `db867d80365510790bb6a04ce33105cd196cf34c` and prior `409183aa9be8c8500942eadf22d036abaf1ead5c` are ancestors of H.
- Pre-fix tree P: `db867d80365510790bb6a04ce33105cd196cf34c` production (verified against the three changed production blobs), plus F6's `BasicFunctionalityTest.java` (blob `1303fbab6b64abd4289cd0ca4a14f59831a43830`, sha256 `bbbd61eead2610a458a1b4cb1c1b5bed51d89e6f21813398d90bdea4085d8552`) and `TestPseudoReturnFields.java` (blob `b81b328189af7f2a3c273009890c642ed946d44e`).

## Legs (UTC start-end, exit)

| Leg | Command (filter) | rc | UTC | Result |
| --- | --- | --- | --- | --- |
| tidy at F6 | `tidy` | 0 | 19:39:07 to 19:41:47 | tree clean after (`tidy-tree: 0 changed`) |
| pre snippets, P | `testGlobHighlightSnippets` | 0 | 19:41:49 to 19:49:05 | positive: all 24 rows, both highlighters |
| pre RTG, P | `testGlobRTGStoredFieldLoading` | 1 | 19:49:05 to 19:51:28 | `AssertionError: unrelated_s should remain lazy` |
| pre prefetch, P | `testGlobHighlightPrefetch` | 1 | 19:51:28 to 19:53:48 | `AssertionError: original: source_* did not prefetch source_s` (after the finite original control) |
| pre alias, P | `testGlobRTGEmptyAndAliasFieldLoading` | 1 | 19:53:48 to 19:55:57 | `AssertionError: title should remain lazy` at `BasicFunctionalityTest.java:1051`, after the empty-glob response assertion passes |
| pre boundaries, P | `testGlobPrefetchBoundaries` | 1 | 19:55:58 to 19:58:08 | `AssertionError: Missing prefetched document for fl=source_* rows=50 isShard=false` (earlier explicit controls pass) |
| post focused, F6 | six methods | 0 | 19:58:08 to 20:04:58 | `BasicFunctionalityTest` 5 tests, 0/0/0; `TestPseudoReturnFields.testGlobRTGUncommittedFields` 1 test, 0/0/0 |
| post suites, F6 | six classes | 0 | 20:04:58 to 20:07:56 | `BasicFunctionalityTest` 28/0/0/0; `TestPseudoReturnFields` 35/0/0/0; `TestRetrieveFieldsOptimizer` 1/0/0/0; `ReturnFieldsTest` 16/0/0/0; `TestUnifiedSolrHighlighter` 33/0/0/0; `LargeFieldTest` 1/0/0/0 |
| compile with Error Prone, F6 | `compileJava compileTestJava -Pvalidation.errorprone=true` | 0 | 20:07:56 to 20:15:35 | executed |
| check, F6 | `:solr:core:check -x test` | 0 | 20:15:36 to 20:24:21 | module check, not repository-wide |

The XML counts are read from each leg's XML directory, cleaned before its leg. `BasicFunctionalityTest` has 28 test cases: the round-2 suite's 26 plus `testGlobRTGEmptyAndAliasFieldLoading` and `testGlobPrefetchBoundaries`.

## Evidence

- Durable copy: `reports/pr-5188-glob-field-loading-round-3-evidence/`, all 66 files from the run directory, including the driver, status, each leg log and fresh XML. Hashes: `reports/pr-5188-glob-field-loading-round-3-evidence/MANIFEST.sha256`.

## Limits

- Module `:solr:core` and the listed test classes. No full `:solr:core:test` and no whole-repository check.
- The positive snippet matrix is a fixture precondition; the four loading assertions carry the discrimination.
- Logs contain the `java.security.policy` warnings from the security manager; not investigated.
- Upstream PR 5188 head was not re-read in this round beyond the pinned B.

## Proposed incremental change description (draft, not posted)

RTG and lazy prefetch honor glob field selection, using the current ReturnFields API names. RTG loads only the stored fields its response and transformers need. Lazy prefetch resolves glob matches before adding highlight fields and the unique key, and it keeps the existing non-shard, 50-result guard. Regression methods pin unmatched globs, alias collisions with cached documents, and the 50/51-row and shard boundaries. The pre-fix base fails the four loading methods for the expected reasons; the head passes them with the 24-row positive snippet matrix. No disk-I/O or latency claim is made. AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.
