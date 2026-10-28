# Gate: PR 5188 glob RTG field loading and lazy prefetch, round 1

Status: DONE

Completed 2026-10-28 (cleanup slice 2, phone agent): superseded by the round-1 chain ending at `gates/pr-5188-glob-field-loading-round-1-superseding-51e92b6.md`, which executed; see `receipts/PR-5188-glob-field-loading-round-1.md`. PR 5188 is merged.
Tags: gate, implementation, review
Intended host: any capable Linux gate host with JDK 21, Python 3, Git and the supported Solr runner, one build at a time under its host lock.

## Inputs and deliverables

- Authority: Nick requested an implementation pass and a handoff for pre-fix/post-fix runs on 2026-10-26. Windows prepared and pushed the candidate; no Windows Gradle or test run is authorized.
- Upstream context: https://github.com/apache/solr/pull/5188, original PR head e07455a0f953e702da27f677c51ea894b7bafc3a.
- Fork branch: nick-boss-tech/solr:pr-5188-glob-field-loading-followup.
- Candidate post-fix head: b71a1687bf98cf7b4b3edf635bfb89f25e4af8e2.
- Tests-only pre-fix head: 4ccd146b904b4574dbd412163f5c88b5f2e0de6b (the direct parent of candidate head). Production is byte-identical to e07455a and both changed test files are identical to candidate head.
- Module: :solr:core.
- Seed for every leg: 5188C0DE (use -Ptests.seed=5188C0DE and record the actual printed test seed).
- Claim context: claims/pr-5188-glob-field-loading-windows-round-1.md.
- Implementation evidence: reports/pr-5188-glob-field-loading-implementation-round-1.md, including exact outbound files and file hashes.
- Gate receipt: receipts/PR-5188-glob-field-loading-round-1.md.
- Gate report: reports/pr-5188-glob-field-loading-gate-round-1.md.
- Durable raw logs and fresh JUnit XML: reports/pr-5188-glob-field-loading-round-1-evidence/ (or a durable linked evidence location recorded in the report).

## Claim and preflight

Read the live board snapshot, OPEN-WORK.md, WORKFLOW.md, START-HERE.md, UI-PLAYBOOK.md, your host record, pool/decision-map.md, material/decision-ladder.md, and pool/owner-complaints-2026-10-19.md. Read the implementation report and check claims/receipts/deliverables for this exact gate phase. First pushed add-only gate claim wins. Do not edit this gate or the Windows claim. Completion is derived from the gate receipt/report.

Use detached isolated trees; take the host build lock, disk/inode guard and supported runner. Perform the detached tidy preflight before claiming as WORKFLOW requires. Record board snapshot, both full source SHAs, JDK, runner, host, UTC start time, log paths and seed in the claim. Heartbeat in your own host record, at least hourly. No simultaneous gate/build on the host.

Read back the fork tip before testing. If it differs from the pinned candidate, inspect ownership and changes and post a superseding gate rather than silently replacing this input. Read the live upstream PR head for context; if it moved, record the difference and inspect overlap, while retaining this explicitly pinned baseline. No push to the original contributor's branch or apache/solr is authorized.

## Purpose and limits

These are proposed field-loading regressions, not yet runtime-confirmed defects. The RTG response path previously passed null for globs and materialized every cached field. The candidate shares the existing query stored-field selector with RTG and prefetch and filters RTG conversion before materializing. It leaves internal input-document reconstruction, RTG docValues decoration, /export, and term-vector behavior unchanged.

The prefetch helper now resolves glob matches before unioning highlight fields and the unique key. QueryComponent's existing non-shard / <=50 results guard and the lazy-loading requirement remain intact. Pseudo-only and all-fields prefetch keep their old no-op behavior. Original highlighting uses the document cache; Unified independently loads text. This gate proves cached eager/lazy state and output, not disk-read counts, latency or that Unified's own text reads disappear.

## Mandatory matched runs

Archive each leg separately with raw command, UTC start/end, exit code and fresh XML. Use :solr:core:cleanTest before each leg so stale XML cannot count. Tests must actually compile and execute. Never treat skipped tests, a missing test filter match, compilation errors, fixture setup failures or unrelated failures as the expected regression.

1. Verify the pre-fix commit's three changed production paths match original PR e07455a and both changed test paths match candidate head. Record the comparisons/hashes. No cherry-pick or synthetic API shim is needed for the initial candidate.
2. Pre-fix RTG: on exact tests-only head, run:
   ./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testGlobRTGStoredFieldLoading -Ptests.seed=5188C0DE
   Expected source-based prediction: the first cold glob case fails the assertion that unrelated_s should remain lazy. It previously fetched everything, so the field is eager (or a loaded lazy field if the observed path differs). Response-shape assertions should pass first. Capture the actual assertion and explain any difference. This expectation is not a prior observed failure.
3. Pre-fix prefetch: same exact tests-only head and seed, run:
   ./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testGlobHighlightPrefetch -Ptests.seed=5188C0DE
   Expected source-based prediction: finite original-highlighter control passes, equivalent glob fails with original: source_* did not prefetch source_s. On the glob path, the highlighter populated the cache without eagerly fetching the response field. Capture actual failure content; do not accept an unrelated snippet or XML error as prefetch proof.
4. Post-fix focused tests: on exact candidate head, run:
   ./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testGlobRTGStoredFieldLoading --tests org.apache.solr.BasicFunctionalityTest.testGlobHighlightPrefetch --tests org.apache.solr.search.TestPseudoReturnFields.testGlobRTGUncommittedFields -Ptests.seed=5188C0DE
   All three methods must pass with zero skips. RTG covers cold/warm cache, glob versus explicit fields, transformer input outside the glob, synthetic-name collision, fl=* and pseudo-only controls. The uncommitted method covers tlog and fq-forced realtime searcher paths with /select invisibility before/after. Highlighting covers original and unified, eager cached response/highlight/key fields, unrelated lazy state and exact output/snippets.
5. Post-fix full modified and neighboring suites, same exact candidate and seed:
   ./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest --tests org.apache.solr.search.TestPseudoReturnFields --tests org.apache.solr.response.TestRetrieveFieldsOptimizer --tests org.apache.solr.search.ReturnFieldsTest -Ptests.seed=5188C0DE
   Record XML counts by class and failures/errors/skips. This exercises the moved selector's existing stored/DV cases and return-field/alias/transformer semantics.
6. Format and checks at that same final head: run tidy, :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true, and :solr:core:check -x test under the supported serialized runner. Report each task and exit code. A module check is not a repository-wide check. Do not add silent exclusions.

The commands above describe Linux runner task/filter inputs; retain the runner's environment, absolute temp paths, lock and supported tooling. Do not run them unguarded or on Windows. A run that never executes tests follows the documented one-relaunch rule. A real test failure is investigated as a repair, not blindly retried.

## Repairs and acceptance

If the predictions fail to discriminate, publish the actual result and investigate the source/test path. A green pre-fix run does not establish a bug; close a falsified hypothesis or repair the probe using evidence. Do not change expected assertions to match incidental output and call it proof. The uncommitted-output method is a compatibility pin and may pass before and after.

Narrow source/test/format repairs within the five outbound files are authorized on the same fork branch. Commit as Nick Shanin with no attribution trailers and push normally, never force. Add an immutable repair report and superseding gate naming the new head; do not move this job's head or write a green receipt for an earlier head. If tidy changes tests, use final test files over original e07455a production for the new pre-fix leg and record that exact test overlay's hashes. Re-run the matched proof and checks at the final head. Broader behavior changes need a documented scope decision first.

Green requires both pre-fix methods to fail for the relevant loading assertions and all mandatory post-fix tests/checks to pass at one final pushed head. Write the phase receipt only then. Include source/test/final SHAs, identity, diffstat, tidy result, runner/JDK/host, exact commands, seed, actual assertion messages and fresh XML counts. The receipt's final section is a proposed incremental PR-description summary, clearly derived from actual results. A failed/incomplete run gets a failure/repair report with no green receipt. No public PR/comment/JIRA write is authorized by this gate.
