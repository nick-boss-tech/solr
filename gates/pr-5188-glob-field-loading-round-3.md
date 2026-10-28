# Gate: PR 5188 current API and regression boundaries, round 3

Status: DONE

Completed 2026-10-28 (cleanup slice 2, phone agent): superseded by `gates/pr-5188-glob-field-loading-round-3-superseding-555e1bf.md` under its repair rule, which executed; see `receipts/PR-5188-glob-field-loading-round-3.md`.
Tags: gate, implementation, review
Intended host: any capable Linux gate host with JDK 21, Python 3, Git and the supported serialized Solr runner.

## Authority, inputs and deliverables

Nick authorized the naming changes and a review of missing implementation regressions. Windows merged the current PR's rename commit, adapted the added calls, added two focused tests and pushed normally. No Gradle on Windows.

- Fork branch: nick-boss-tech/solr:pr-5188-glob-field-loading-followup.
- Candidate H: f00caeb5e83ac375672ae27fb43ee1546795e98b (fork API read-back verified).
- Current PR production B: db867d80365510790bb6a04ce33105cd196cf34c.
- Prior proved follow-up: 409183aa9be8c8500942eadf22d036abaf1ead5c, an ancestor of H. Its receipt does not cover H.
- Naming merge: 2994813ae51847fdc17a1480086c2a9f8cfb059c, parents 409183a and db867d8. Incremental H against B remains the original five outbound files.
- Pre-fix P: clean detached B with H's final BasicFunctionalityTest.java and TestPseudoReturnFields.java overlaid. Do not use e07455a or the old tests-only commit for this phase: this base deliberately includes the new ReturnFields API names.
- Module: :solr:core. Seed: 5188C0DE (-Ptests.seed=5188C0DE, verify printed seed).
- Claim context: claims/pr-5188-api-and-regressions-windows-round-3.md.
- Implementation report/hashes: reports/pr-5188-glob-field-loading-implementation-round-3.md.
- Receipt: receipts/PR-5188-glob-field-loading-round-3.md.
- Report: reports/pr-5188-glob-field-loading-gate-round-3.md.
- Durable raw evidence directory: reports/pr-5188-glob-field-loading-round-3-evidence/ (or a durable accessible archive linked by the report, with hash and manifest).

## Bootstrap and claim

Read a live board snapshot, OPEN-WORK/WORKFLOW/START-HERE/UI-PLAYBOOK, your host capability file, decision map/ladder and owner complaints. Recheck this phase's claim/receipt/target paths. Round-2 vm6 has a terminal result and receipt; this is new work. Run detached tidy preflight before claiming per WORKFLOW. First new add-only gate claim pushed wins. Record board/source/final SHAs, overlay hashes, JDK/host/runner, UTC start and log paths. Use the host build lock and disk/inode guard, one gate at a time. Heartbeat in the host file. Do not edit previous jobs/claims/receipts.

Read back the fork tip before running; if it differs from H, reconcile and post a superseding exact-head gate. Read the live upstream PR 5188 head for overlap; retain the explicitly pinned B unless a new base is recorded before running. Never update apache/solr or David's branch.

## Scope and new coverage

The added code uses loadsField, loadsFieldExplicitly and returnsAllFields. The inner stored-field selector conflict was resolved by keeping the follow-up's single shared outer getStoredFieldsToLoad method, with the current API names. No loading policy change was made in this phase. RTG DV decoration, internal reconstruction, /export and term vectors remain out of scope.

New testGlobRTGEmptyAndAliasFieldLoading checks missing_* selects zero output fields and leaves title/source_s/unrelated_s lazy, then reuses that cached document for an alias whose output collides with a real stored field, then fl=* must recover the original stored values using the same cache object. No cache clear hides the transition.

New testGlobPrefetchBoundaries checks all eight combinations of explicit/glob fields, rows=50/51 and isShard=false/true, with hl=false and without response serialization. Only non-shard requests returning 50 results should populate the document cache; source_s must be eager and unrelated_s lazy. Shard and 51-result requests must leave result docs absent from the cache. Existing QueryComponent's <=50 guard is unchanged; this pins its integration with the new glob path.

All earlier loading, transformer/pseudo-field, uncommitted visibility and unconditional original/unified positive-snippet assertions remain. There is no snippet exception.

## Mandatory matched proof

Keep each leg's raw command/UTC start-end/exit and fresh XML separately. Run :solr:core:cleanTest before each test leg; archive its results before the next clean. Verify the actual seed. A compile/setup failure, skipped method or unmatched filter is not sensitivity proof. Commands below are task/filter inputs to the supported guarded Linux runner, with its normal environment and absolute temp paths.

1. Verify P's production matches B and its two overlaid test files match final H; record full blob/SHA-256 values. Verify both B and prior 409183a are ancestors of H. The implementation report contains candidate hashes. P is original current-PR production, not the merged/fixed production.
2. Positive snippet fixture on P:
   ./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testGlobHighlightSnippets -Ptests.seed=5188C0DE
   Must pass all 24 original/unified, implicit/explicit, weightMatches and fl cases, with zero skips.
3. Four independent pre-fix sensitivity legs on P. Run each method with a separate cleanTest/test invocation and the same seed:
   - org.apache.solr.BasicFunctionalityTest.testGlobRTGStoredFieldLoading: expected unrelated_s should remain lazy, after response checks pass.
   - org.apache.solr.BasicFunctionalityTest.testGlobHighlightPrefetch: expected original: source_* did not prefetch source_s, after the finite original control.
   - org.apache.solr.BasicFunctionalityTest.testGlobRTGEmptyAndAliasFieldLoading: predicted title should remain lazy on the empty-glob request, after the empty response assertion passes.
   - org.apache.solr.BasicFunctionalityTest.testGlobPrefetchBoundaries: predicted Missing prefetched document for fl=source_* rows=50 isShard=false, after all explicit-field control combinations pass.
   Invocation shape: ./gradlew :solr:core:cleanTest :solr:core:test --tests <one method above> -Ptests.seed=5188C0DE.
   First two failures are previously observed on the older base; last two are source-based predictions. Capture actual assertion content and investigate discrepancies. Do not accept an unrelated XML, alias, shard-setup or snippet failure as proof.
4. Exact H, six focused methods:
   ./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testGlobRTGStoredFieldLoading --tests org.apache.solr.BasicFunctionalityTest.testGlobHighlightPrefetch --tests org.apache.solr.BasicFunctionalityTest.testGlobHighlightSnippets --tests org.apache.solr.BasicFunctionalityTest.testGlobRTGEmptyAndAliasFieldLoading --tests org.apache.solr.BasicFunctionalityTest.testGlobPrefetchBoundaries --tests org.apache.solr.search.TestPseudoReturnFields.testGlobRTGUncommittedFields -Ptests.seed=5188C0DE
   All six must pass with zero skips, including both highlighters' exact positive snippets, empty/alias/cache transitions and every prefetch boundary.
5. Full modified/neighbor suites at the same H:
   ./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest --tests org.apache.solr.search.TestPseudoReturnFields --tests org.apache.solr.response.TestRetrieveFieldsOptimizer --tests org.apache.solr.search.ReturnFieldsTest --tests org.apache.solr.highlight.TestUnifiedSolrHighlighter --tests org.apache.solr.search.LargeFieldTest -Ptests.seed=5188C0DE
   Record actual XML counts/errors/failures/skips per class. BasicFunctionalityTest has two added methods beyond the round-2 26-test suite; do not substitute a hardcoded count for actual XML.
6. Same final H: tidy (clean tree afterwards), :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true, and :solr:core:check -x test. Record all exits. Do not silently add exclusions or claim whole-repository/full-core-test coverage.

## Repairs, evidence and completion

If the new assertions do not discriminate or a case fails, investigate the observed failure as a repair. Never blindly rerun a test that executed and failed. Follow the documented one-relaunch rule only when no tests executed. Source/test/format repairs bounded by this five-file follow-up are authorized with a recorded finding; wider changes need a scope decision first. Commit as Nick Shanin, no attribution trailers, plain fork push only; read back the new SHA. Add an immutable repair report and superseding gate. Do not move this job's head. Rebuild P with final test files and repeat the mandatory proof/checks at the new final head.

Keep raw evidence durable as in round 2: driver/commands, status, each leg log, fresh per-leg XML, final clean-tree/head and P/test-overlay identity records, and SHA-256 manifest. VM-local paths alone do not finish the handoff. Do not count XML copied after tidy/compile/check as new test executions.

Green requires positive snippets on P and H, all four relevant pre-fix loading failures, all focused/suite/check legs passing at one final pushed head, and linked durable evidence. No empty-snippet waiver. A failed/incomplete leg produces a failure/repair report and no green receipt. The receipt's final section may draft the incremental description from actual results. No public PR/comment/JIRA action is authorized by this gate.
