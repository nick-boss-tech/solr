# Report: PR 5298 DocValues prefetch gate round 1, phone-agent

Job: `gates/pr-5298-dv-prefetch-round-1.md`. Claim: `claims/pr-5298-dv-prefetch-round-1-phone-agent.md`.
Candidate H `4d547451e3914de441fc897ea6a8c0652a48736a` (tree `f472273219cd544598f9f590ca1fd1e21f0e449a`); reviewed parent B `7ee68a7000b67bed06ad3130ffff27a086a56aad`. Live PR #5298 head and fork branch read back at H before execution.
Executor: phone-agent (Linux, this VM). JDK 21 (Temurin 21.0.12.1+1), Gradle 9.7.0 via the host runner, BATS not applicable. Seed `5298C0DE` for every test leg. Window: 2026-11-05T21:30Z to 2026-11-05T22:04Z. All Gradle legs serialized under the host test-queue lock.
Verdict: **GATE FAILED at H.** Two findings, one in the new tests and one production regression, both evidenced below. No source or test repair was applied in this gate.

## Preflight

Normal clones `~/workspace/solr-clones/5298-h` (HEAD H) and `~/workspace/solr-clones/5298-p` (HEAD B), `.git` a directory in both, candidate and parent fetched explicitly from the fork. Tidy at H: rc 0, tracked tree clean afterward (no libs.versions.toml prune). Disk 68 percent used.

## Pre-fix legs (P = B + H's test file): all three fail as specified

Overlay verified before running: the only file differing from B is `BasicFunctionalityTest.java`, blob `379421ae51cef1a9e4b58f0c9889c5ef5e7def6c`, SHA-256 `f44182b29339e09d2f3fa41db835fec781495bce052a8b912ecef1c7e57dc6ca`, both matching the job; production diff vs B empty. Each method ran in its own cleanTest/test invocation at seed `5298C0DE`, 1 test executed, 1 failure:

- `testGlobNonStoredDocValuesPrefetch`: `AssertionError: Unexpected stored-document prefetch for fl=*_s_dvo expected null, but was:<Document<...LazyDocument...>>`
- `testGlobSubstitutedDocValuesPrefetch`: same assertion for `fl=*_idv`.
- `testGlobEmptyPrefetch`: same assertion for `fl=missing_*`.

All three fail at the intended pre-serialization assertion, exactly as the job predicts. The pre-fix proof pair leg is complete.

## Head legs: the three new methods fail, on a test defect

Nine-method leg at H (the three new methods, the five existing `testGlob*` methods, and `TestPseudoReturnFields.testGlobRTGUncommittedFields`): 9 executed, 3 failures, 0 errors, 0 skips. The five existing glob methods and the RTG method pass. The three new methods fail identically in the nine-method leg and again in the suites leg:

`org.xml.sax.SAXParseException: Content is not allowed in prolog`, thrown from `BaseTestHarness.validateXPath` at `BasicFunctionalityTest.java:1197`, the first XPath check in `assertDocValuesOnlyPrefetch`, after the prefetch assertions pass.

Diagnosis, proven by a scratch run in the disposable H clone (test file restored afterward, tree verified clean): the helper builds its request without `wt`, so `req.getResponseWriter()` returns the JSON writer and the response string fed to `validateXPath` is JSON. Captured verbatim for `testGlobEmptyPrefetch` (method=null pass): a normal JSON response with `numFound: 1` and one empty doc `{}`, which is the correct production result for `fl=missing_*`. The sibling helper in `testGlobHighlightPrefetch` sets `wt=xml` explicitly and passes at H. So the production change works at these assertions; the new tests were never executed before this gate (Windows implementation ran no Gradle or tests), and the pre-fix failures masked the defect because they stop before the writer call. Minimal repair: add `"wt", "xml"` to the params in `assertDocValuesOnlyPrefetch`.

## Suites leg and the LargeFieldTest regression

Full neighbor suites at H, seed `5298C0DE`, fresh XML counts: BasicFunctionalityTest 31 tests / 3 failures (the three above); TestUnifiedSolrHighlighter 33/0; TestRetrieveFieldsOptimizer 1/0; ReturnFieldsTest 16/0; TestPseudoReturnFields 35/0; LargeFieldTest 1/1 failure. Totals: 117 tests, 4 failures, 0 errors, 0 skips.

`LargeFieldTest.test` fails at `assertLazyNotLoaded` (LargeFieldTest.java:124, from test line 96): after a query with `fl` naming only the lazy field, the cached document's large field is no longer an unloaded `LargeLazyField`. Discrimination runs: the same class alone at B with the same seed passes (1/1, rc 0); alone at H it fails with the identical assertion and sub-seed. The regression is therefore in the follow-up commit's production change (B to H: `SolrDocumentFetcher.getStoredFieldsToPrefetch` applying the all-or-nothing DocValues substitution to the prefetch selector, and the `SolrPluginUtils` early return when the post-substitution filter is empty), not in the tests and not pre-existing at the parent. Pinpointing which of the two hunks breaks the cached-document field shape needs implementation work; this gate records the regression and does not improvise a production fix.

## Remaining legs

Error Prone compile (`:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`): rc 0.
Root `check -x test` in the normal H clone: the first run was killed by a runtime restart mid-leg (no outcome recorded, not counted either way); rerun: rc 0, BUILD SUCCESSFUL in 4m 20s (rerun 22:00Z to 22:04Z after the first attempt was killed by a runtime restart mid-leg, no outcome).

## Completion state

Implemented, Proof Pending is superseded by: **Gate FAILED at H `4d547451e391`**, with the pre-fix proof complete and two findings to repair: (1) add `wt=xml` in the new test helper; (2) fix the LargeFieldTest regression in the prefetch change. A repair round at a new head, with the required legs repeated, is the path to green. No PR body edit, comment, or other public action was taken by this gate.

Evidence: `~/workspace/tools/gate-5298/` (logs, per-leg JUnit XML, debug captures) and the board archive `reports/pr-5298-dv-prefetch-round-1-evidence/` with its SHA-256 manifest.
