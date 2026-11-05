# PR 5298 dv-prefetch gate, round 2: execution report

Job: gates/pr-5298-dv-prefetch-round-2.md
Executor: phone-agent (Linux, this VM), claim claims/pr-5298-dv-prefetch-round-2-phone-agent.md
Candidate R: a99e40291ae17fbee56b821113cc9bc15659b147 (tree dac6c35be41a77ff157a72a1df9b146cb7326c9c), verified equal to the fork branch head and the live PR #5298 head before preflight.
Failed parent H: 4d547451e3914de441fc897ea6a8c0652a48736a. Original reviewed production B: 7ee68a7000b67bed06ad3130ffff27a086a56aad.
Fixed seed: 5298C0DE. Builds via solr-gradle.sh (Gradle 9.7.0, vendored JDK 21), serialized under the host build lock.

## Preflight (2026-11-05T22:33:40Z to 22:35:07Z)

Detached :solr:core:tidy at R: rc 0, tracked tree clean afterward. P clone verified at B with only R's BasicFunctionalityTest.java overlaid (blob f39c8488b6c8ed4982ce82108b9f13ccb3bd3604, SHA-256 e2afe28327ab70fbd696bbc60966b00079b8d7ee17df11e8d95da032f2a74997); production diff vs B empty. LargeFieldTest.java at R verified unchanged (blob ef3af492fdbaf9493ae5a35a4474b9449af38332, SHA-256 abaf6c092868617d4c8ec08afc3aa7ab498bc3da1b5b2315494f8d50e5a014eb). Disk 71% used at start.

## Negative legs on P (B plus final R tests), seed 5298C0DE

Each leg was its own cleanTest/test invocation; each executed 1 test with 1 failure at the intended pre-serialization assertion:

- testGlobNonStoredDocValuesPrefetch: "Unexpected stored-document prefetch for fl=*_s_dvo" (22:36:49Z)
- testGlobSubstitutedDocValuesPrefetch: "Unexpected stored-document prefetch for fl=*_idv" (22:38:10Z)
- testGlobEmptyPrefetch: "Unexpected stored-document prefetch for fl=missing_*" (22:38:56Z)

## Positive legs at R, seed 5298C0DE

- Nine focused methods (22:39:41Z): 9 executed, 0 failures, 0 errors, 0 skipped (BasicFunctionalityTest glob methods 8, TestPseudoReturnFields.testGlobRTGUncommittedFields 1). The repaired tests reach and pass their XML value and count assertions, including both highlighting controls.
- LargeFieldTest independently (22:41:41Z): 1 of 1 pass, with the test file unchanged. Together with round 1's isolated B-pass and H-fail at the same seed, this completes the regression proof pair for the repair: the pattern-only restriction restores the explicit-selection prefetch behavior the test pins.
- Six suites (22:42:27Z): 117 tests, 0 failures, 0 errors, 0 skipped. Per class: BasicFunctionalityTest 31, TestPseudoReturnFields 35, TestUnifiedSolrHighlighter 33, ReturnFieldsTest 16, TestRetrieveFieldsOptimizer 1, LargeFieldTest 1. Counts are from this run's fresh JUnit XML.
- Error Prone compile (:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true): rc 0, BUILD SUCCESSFUL in 3m 57s.
- Root check (check -x test) at R in the normal clone: rc 0, BUILD SUCCESSFUL in 4m 24s.

Legs ran 2026-11-05T22:36:47Z to 22:51:50Z. No retries were needed and no repair was made during execution; R is unchanged.

## Verdict

GATE GREEN at R a99e40291ae17fbee56b821113cc9bc15659b147. Every mandatory leg in the round-2 job completed at the one final pushed head. Raw evidence with a SHA-256 manifest is archived at reports/pr-5298-dv-prefetch-round-2-evidence/.
