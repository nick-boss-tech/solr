# Receipt: PR 5298 DocValues prefetch gate round 1, phone-agent

Status: **GATE FAILED** at candidate H `4d547451e3914de441fc897ea6a8c0652a48736a`.
Job: `gates/pr-5298-dv-prefetch-round-1.md`. Report: `reports/pr-5298-dv-prefetch-gate-round-1.md`. Claim: `claims/pr-5298-dv-prefetch-round-1-phone-agent.md`.
Execution: phone-agent, Linux, JDK 21 (Temurin 21.0.12.1+1), Gradle 9.7.0, seed `5298C0DE` throughout, 2026-11-05T21:30Z to 2026-11-05T22:04Z. Live PR head and fork branch read back at H before execution.

What passed: tidy at H clean; the pre-fix proof complete (P = parent `7ee68a7000` plus only H's test file, blob and SHA-256 matching the job; all three new methods fail at the intended "Unexpected stored-document prefetch for fl=" assertion, one invocation each); at H the five existing glob methods, `TestPseudoReturnFields` (35/35 in suites, plus its glob method in the nine-method leg), `TestUnifiedSolrHighlighter` 33/33, `TestRetrieveFieldsOptimizer` 1/1, and `ReturnFieldsTest` 16/16 pass; Error Prone compile rc 0; root check: rc 0, BUILD SUCCESSFUL in 4m 20s (rerun after the first attempt was killed by a runtime restart).

What failed at H:
1. The three new methods (`testGlobNonStoredDocValuesPrefetch`, `testGlobSubstitutedDocValuesPrefetch`, `testGlobEmptyPrefetch`) fail at `BasicFunctionalityTest.java:1197` with a SAX "Content is not allowed in prolog" error. Root cause is in the tests: `assertDocValuesOnlyPrefetch` never sets `wt`, so the response writer returns JSON, which the captured response confirms (numFound 1, one empty doc for `fl=missing_*`; the production result is correct). The tests had never been executed before this gate. Minimal repair: add `"wt", "xml"` to the helper's params.
2. `LargeFieldTest.test` regresses: it passes alone at the parent with the gate seed (1/1) and fails alone at H with the identical assertion (`assertLazyNotLoaded`, line 124 from line 96). The cause is in the follow-up commit's production change to the prefetch selector (DocValues substitution plus the empty-filter early return in `SolrPluginUtils`).

Suites totals at H: 117 tests, 4 failures, 0 errors, 0 skips. Nine-method leg: 9 executed, 3 failures (the new methods). No repair was applied in this gate; a repair round at a new head with the required legs repeated is the path to green. Evidence: `reports/pr-5298-dv-prefetch-round-1-evidence/` (archive and SHA-256 manifest) and `~/workspace/tools/gate-5298/`.
