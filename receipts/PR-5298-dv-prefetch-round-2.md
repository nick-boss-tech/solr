# Receipt: PR 5298 dv-prefetch gate, round 2

GATE GREEN at R a99e40291ae17fbee56b821113cc9bc15659b147 (tree dac6c35be41a77ff157a72a1df9b146cb7326c9c), the live head of fork branch pr-5188-glob-field-loading-followup and draft PR #5298 at execution time.

Job: gates/pr-5298-dv-prefetch-round-2.md. Executor: phone-agent. Seed: 5298C0DE. The round-1 receipt at H 4d547451e391 remains GATE FAILED and unchanged.

What ran, all at the head above unless noted:

- Preflight tidy (:solr:core:tidy): clean, tree unchanged.
- Pre-fix proof on P (B 7ee68a7000 plus only R's BasicFunctionalityTest.java, blob f39c8488b6, production diff empty): each of the three new methods failed in its own invocation at "Unexpected stored-document prefetch" for its selection (fl=*_s_dvo, fl=*_idv, fl=missing_*).
- Nine focused methods at R: 9 executed, 0 failures, 0 errors, 0 skipped.
- LargeFieldTest at R, unchanged file (blob ef3af492fd): 1 of 1 pass at the same seed as round 1's B-pass/H-fail pair.
- Six suites at R: 117 tests, 0 failures, 0 errors, 0 skipped (BasicFunctionalityTest 31, TestPseudoReturnFields 35, TestUnifiedSolrHighlighter 33, ReturnFieldsTest 16, TestRetrieveFieldsOptimizer 1, LargeFieldTest 1), counted from fresh JUnit XML.
- Error Prone compile: pass. Root check (check -x test): pass.

Execution window: 2026-11-05T22:33:40Z to 22:51:50Z. Report: reports/pr-5298-dv-prefetch-gate-round-2.md. Evidence: reports/pr-5298-dv-prefetch-round-2-evidence/ (archive with SHA-256 manifest).

## Draft PR text (from these results; not posted or applied by this gate)

Proof: On Linux at a99e4029, the three new tests fail on the base commit with only the test file added, each at the assertion that no stored document is prefetched for its glob selection, and pass with the change. LargeFieldTest, which failed at the previous head, passes again at this head and at the base; the repair limits the substitution to pattern selections. The full BasicFunctionalityTest, TestPseudoReturnFields, TestUnifiedSolrHighlighter, ReturnFieldsTest, TestRetrieveFieldsOptimizer and LargeFieldTest suites pass (117 tests), and the module check, Error Prone compile and repository check all pass.
