# Superseding gate: SOLR-18608 prefetch repair, round 2

Status: OPEN
Tags: gate, implementation, review, records
Intended host: any capable Linux CPU gate host with JDK 21, Python 3, Git and this branch's pinned Gradle wrapper/build dependencies, serialized under the host build lock.

## Authority and pinned inputs

Continuation of Nick's request to patch PR 5298 and hand it to the gate, under round 1's scoped repair rule after its failed receipt. This job supersedes gates/pr-5298-dv-prefetch-round-1.md for the repaired head; it does not change the failed outcome at the old head.

- Fork branch: nick-boss-tech/solr:pr-5188-glob-field-loading-followup
- Repaired candidate R: a99e40291ae17fbee56b821113cc9bc15659b147
- Failed parent H: 4d547451e3914de441fc897ea6a8c0652a48736a
- Original reviewed production B: 7ee68a7000b67bed06ad3130ffff27a086a56aad
- Public draft PR: https://github.com/apache/solr/pull/5298
- Repair claim/report: claims/pr-5298-dv-prefetch-repair-windows-round-2.md / reports/pr-5298-dv-prefetch-repair-round-2.md
- Prior failed receipt/report: receipts/PR-5298-dv-prefetch-round-1.md / reports/pr-5298-dv-prefetch-gate-round-1.md
- Module: :solr:core; fixed seed: 5298C0DE (verify the printed seed).
- Final BasicFunctionalityTest.java blob f39c8488b6c8ed4982ce82108b9f13ccb3bd3604; SHA-256 e2afe28327ab70fbd696bbc60966b00079b8d7ee17df11e8d95da032f2a74997 (LF bytes).
- LargeFieldTest.java remains unchanged: blob ef3af492fdbaf9493ae5a35a4474b9449af38332; SHA-256 abaf6c092868617d4c8ec08afc3aa7ab498bc3da1b5b2315494f8d50e5a014eb.
- Execution receipt: receipts/PR-5298-dv-prefetch-round-2.md (execution host only writer).
- Execution report: reports/pr-5298-dv-prefetch-gate-round-2.md
- Durable evidence: reports/pr-5298-dv-prefetch-round-2-evidence/ or accessible archive with a SHA-256 manifest. VM-local paths alone do not finish the gate.

## Diagnosis and repair scope

Round 1 was GATE FAILED at H, not an environment-only refusal. Its three intended pre-fix assertions passed the sensitivity bar; its three positive methods failed because the helper fed JSON to XPath. R adds explicit wt=xml.

The unchanged LargeFieldTest fails at line 96, its initial lazyField assertion after the explicit fl=str query and the unfiltered doc(0) cache lookup. It fails before the later lazyField-only query at line 99. str is a stored/DV field under its schema version 1.7. H's DV substitution suppressed explicit prefetch, so doc(0) eagerly populated the empty cache. R restricts substitution and empty-set bypass to pattern selections, restoring B's established explicit prefetch behavior. Do not change the LargeFieldTest fixture/schema/assertions to make this pass.

The source trace is in the repair report. The prior receipt already contains B-pass/H-fail isolated LargeFieldTest results at seed 5298C0DE; retain that evidence as the repair's negative leg. This job requires fresh R execution and repeats all original mandatory legs with R's final test blob. Do not blindly rerun old H expecting a different result.

## Preflight and claim

Read live board bootstrap, host capabilities, this job and claims/receipts. Verify R equals both current fork and PR heads; a moved head needs reconciliation and a new exact-head job before running. Detached tidy preflight before an add-only execution claim; first claim wins. One gate per host under its build lock, disk/inode guard and normal absolute temp roots. Record board/source SHAs, runner, JDK, paths and UTC times; heartbeat in the host file. Never edit round-1 job/claims/receipt or this job.

## Mandatory proof at fixed seed 5298C0DE

Use the supported guarded Linux runner. Every test leg uses cleanTest with fresh XML, and archives its command, times, exit status, output and XML before the next clean. Missing APIs, compilation errors, startup failures, skipped tests or unmatched filters are not runtime fail-before proof.

1. Build P as detached B plus only R's BasicFunctionalityTest.java. Verify P production blobs match B, the test matches R's blob/hash above, and the only overlay is that test file.
2. Three independent negative legs on P, one cleanTest/test invocation per method:
   - org.apache.solr.BasicFunctionalityTest.testGlobNonStoredDocValuesPrefetch
   - org.apache.solr.BasicFunctionalityTest.testGlobSubstitutedDocValuesPrefetch
   - org.apache.solr.BasicFunctionalityTest.testGlobEmptyPrefetch
   Command shape: ./gradlew :solr:core:cleanTest :solr:core:test --tests <one method> -Ptests.seed=5298C0DE
   Each must execute and fail at Unexpected stored-document prefetch for fl= followed by its selection, before serialization. Round 1 observed these failures with the earlier test blob; this phase must use final R tests. A pass or a different failure needs investigation.
3. At exact R, run the nine focused methods:
   ./gradlew :solr:core:cleanTest :solr:core:test --tests 'org.apache.solr.BasicFunctionalityTest.testGlob*' --tests org.apache.solr.search.TestPseudoReturnFields.testGlobRTGUncommittedFields -Ptests.seed=5298C0DE
   Require 9 executed, no failures/errors/skips. The repaired tests must reach and pass XML value/count assertions and both original/unified highlighting controls, not merely their pre-serialization cache checks. Highlight controls remain positive coverage pins, not independently mutation-proven assertions.
4. At R, independently run the unchanged neighboring regression:
   ./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.LargeFieldTest -Ptests.seed=5298C0DE
   Require 1/1 pass with the same seed as the documented B-pass/H-fail pair. Verify LargeFieldTest's blob is unchanged and link the round-1 negative evidence.
5. At the same R, full changed/neighbor suites:
   ./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest --tests org.apache.solr.search.TestPseudoReturnFields --tests org.apache.solr.response.TestRetrieveFieldsOptimizer --tests org.apache.solr.search.ReturnFieldsTest --tests org.apache.solr.highlight.TestUnifiedSolrHighlighter --tests org.apache.solr.search.LargeFieldTest -Ptests.seed=5298C0DE
   All pass; record actual XML counts/failures/errors/skips per class. Round 1 executed 117 tests across these classes; do not replace actual XML with that historical count.
6. At R: ./gradlew tidy, tracked tree clean afterward; ./gradlew :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true; ./gradlew check -x test. Require successful exits at this exact head, no silent exclusions or module-only substitution for the root check. H's passing checks do not cover R.

## Repairs, evidence and completion

This is a diagnosed repair run. For any executed failure, investigate and document the cause before a retry; no blind reruns. The one-relaunch rule applies only to an environment interruption with no tests executed, with cause/remedy recorded. Keep the round-1 runtime-restart event and final successful root-check result distinct.

Scoped source/test/tidy repairs may be committed as Nick Shanin with no attribution trailers, plain-pushed to the fork branch and separately read back. Any such new head needs an add-only repair report and superseding job before proof; never silently move R or weaken assertions. Rebuild P with final tests and rerun required legs at that head. Larger changes need a documented scope decision.

Green requires all three intended P failures, all nine R methods, unchanged LargeFieldTest independently and in the six suites, clean tidy, Error Prone and root check at one final pushed head, with durable raw evidence/manifest. Record actual heads, counts and execution conditions. Keep the old receipt failed; a round-2 receipt may be green only after every leg completes. Its final section may draft a concise description from actual results. No public comment, live body edit, JIRA write, readiness flip or merge is authorized by this job.
