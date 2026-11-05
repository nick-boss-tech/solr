# Gate: SOLR-18608 DocValues prefetch regression, round 1

Status: OPEN
Tags: gate, implementation, review, records
Intended host: any capable Linux CPU gate host with JDK 21, Python 3, Git and this branch's pinned Gradle wrapper/build dependencies, serialized under the host build lock.

## Authority and exact inputs

Nick requested that the reviewed PR 5298 branch be patched and handed to the gate. Windows authored source/tests and ran no Gradle, compiler or test job.

- Fork branch: nick-boss-tech/solr:pr-5188-glob-field-loading-followup
- Exact candidate H: 4d547451e3914de441fc897ea6a8c0652a48736a
- Reviewed PR parent B: 7ee68a7000b67bed06ad3130ffff27a086a56aad
- PR: https://github.com/apache/solr/pull/5298 (open draft; its head moved with the authorized plain fork push)
- Implementation claim: claims/pr-5298-dv-prefetch-windows-round-1.md
- Implementation report: reports/pr-5298-dv-prefetch-implementation-round-1.md
- Module: :solr:core
- Fixed seed: 5298C0DE; confirm the printed seed for every test leg.
- H test overlay: BasicFunctionalityTest.java blob 379421ae51cef1a9e4b58f0c9889c5ef5e7def6c; SHA-256 f44182b29339e09d2f3fa41db835fec781495bce052a8b912ecef1c7e57dc6ca (LF bytes).
- Execution receipt: receipts/PR-5298-dv-prefetch-round-1.md (execution host is its only writer).
- Execution report: reports/pr-5298-dv-prefetch-gate-round-1.md
- Durable raw evidence: reports/pr-5298-dv-prefetch-round-1-evidence/ or a durable accessible archive linked with SHA-256 manifest. VM-local paths alone do not complete the job.

Prior PR 5188 receipts cover historical heads, not H. Do not substitute those results or upstream labeler success for any leg here.

## Scope

The prefetch selector applies the same all-or-nothing stored-to-DocValues substitution rule as RetrieveFieldsOptimizer. RTG retains its candidate stored-field selector. Prefetch skips a final empty field set only after highlight fields and the unique key are added. No warm-cache forcing or physical disk-I/O/latency guarantee is introduced.

Three new BasicFunctionalityTest methods cover nonstored DV globs, stored/DV substitution globs and unmatched globs. Each checks a cold-cache query both before and after serialization and includes original/unified highlighting controls. Ordinary stored-glob prefetch, RTG aliases/cache transitions and shard/50-row boundaries remain covered by the existing tests. The lazy helper comment clarifies that wrapper state is not proof that the shared backing document contains no unrelated values.

## Bootstrap, claim and preflight

Read the live board bootstrap, host capabilities, gate job and related claims/receipts. Run detached tidy preflight at H before claiming, per WORKFLOW.md. First add-only gate claim wins. Record source/board SHAs, clean worktree, JDK, runner, host lock, disk/inode guard, UTC times and evidence paths. One gate per host. Heartbeat in the host file.

Read back the fork and live PR heads before executing; both must equal H. If they differ, reconcile and post a superseding job naming the new input before running. Do not edit this immutable job or prior claims/receipts.

## Mandatory proof legs

Use the supported guarded Linux runner and absolute temp paths. Archive separate raw logs, command, UTC start/end, exit status and freshly produced JUnit XML for every leg. Use cleanTest before each independent test leg and preserve its results before the next clean. No cached XML, skipped method, unmatched filter, compilation/setup failure or startup error counts as runtime negative proof.

1. Construct pre-fix P as clean detached B, overlaying only H's BasicFunctionalityTest.java. Verify all production blobs still match B and the test blob/SHA-256 matches H. The tests call no newly added production API, so no synthetic API scaffold is necessary.
2. Run each of these three methods independently on P, with separate cleanTest/test invocations and seed 5298C0DE:
   - org.apache.solr.BasicFunctionalityTest.testGlobNonStoredDocValuesPrefetch
   - org.apache.solr.BasicFunctionalityTest.testGlobSubstitutedDocValuesPrefetch
   - org.apache.solr.BasicFunctionalityTest.testGlobEmptyPrefetch
   Command shape: ./gradlew :solr:core:cleanTest :solr:core:test --tests <one method> -Ptests.seed=5298C0DE
   Each must execute and fail at the intended pre-serialization assertion: Unexpected stored-document prefetch for fl= followed by its selection. These signatures are source predictions, not Windows execution results. Investigate a different failure or a pass; neither satisfies the expected proof. The highlighting controls are positive coverage pins, not independently mutation-proven assertions.
3. At exact H, run those same three methods plus the existing five BasicFunctionalityTest glob methods and TestPseudoReturnFields.testGlobRTGUncommittedFields. Invocation:
   ./gradlew :solr:core:cleanTest :solr:core:test --tests 'org.apache.solr.BasicFunctionalityTest.testGlob*' --tests org.apache.solr.search.TestPseudoReturnFields.testGlobRTGUncommittedFields -Ptests.seed=5298C0DE
   Require all nine methods to execute and pass with no skips, including original/unified exact snippets, DV values, empty output and cache-state assertions.
4. At the same H, run full changed/neighbor suites:
   ./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest --tests org.apache.solr.search.TestPseudoReturnFields --tests org.apache.solr.response.TestRetrieveFieldsOptimizer --tests org.apache.solr.search.ReturnFieldsTest --tests org.apache.solr.highlight.TestUnifiedSolrHighlighter --tests org.apache.solr.search.LargeFieldTest -Ptests.seed=5298C0DE
   Require pass and record actual XML counts, failures/errors/skips per class. Do not infer counts from old receipts.
5. Same H: ./gradlew tidy; require a clean tracked tree afterward. Run ./gradlew :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true and ./gradlew check -x test. Record all exits and distinguish the repository root check from a module-only check. No silent exclusions or substitution of :solr:core:check for the root check.

## Repairs and completion

If tidy changes files, commit the scoped repair as Nick Shanin with no attribution trailers, plain-push only to the fork branch, read back the full SHA, and post an add-only repair report plus superseding exact-head gate before executing proof. Source/test repairs follow the same rule; do not silently move H. Rebuild P with the final test blob and repeat required legs for the superseding head.

For an executed test failure, diagnose before any repair/retry; never blindly rerun. An environment interruption with no tests executed may be relaunched once under the board rule, with the cause and remedy recorded. Wider production changes, weakened assertions or omitted proof legs need a documented scope decision, not an unexplained rerun.

Green requires all three intended pre-fix failures, every positive method/suite, clean tidy, Error Prone and root check passing at one final pushed head with fresh durable evidence and a manifest. Receipt must include exact input/final heads, execution conditions, actual counts and known limits; its final section may draft a concise description based on actual results. Keep Implemented, Proof Pending until then. Posting this job does not assert that a Linux host has claimed or run it. No public PR/JIRA comment, live body edit, readiness flip or merge is authorized by this gate.
