# Gate: PR 5188 full snippet and field-loading proof, round 2

Status: DONE

Completed 2026-10-28 (cleanup slice 2, phone agent): superseded by `gates/pr-5188-glob-field-loading-round-2-superseding-409183a.md` under its repair rule, which executed; see `receipts/PR-5188-glob-field-loading-round-2.md`.
Tags: gate, implementation, review
Intended host: any capable Linux gate host with JDK 21, Python 3, Git and the supported serialized Solr runner.

## Authority and inputs

Nick requested the more thorough solution and another gate after the missing unified snippet assertion was explained. The previous narrowing is not accepted for this phase. Windows restored the assertion and pushed a test-only repair; no Windows Gradle execution is authorized.

- Fork branch: nick-boss-tech/solr:pr-5188-glob-field-loading-followup.
- Candidate head H: cb8d3dcf0172515e11b92a9d1c7d9aa186f1364c (API read-back confirmed).
- Previous receipt head: 51e92b63fddb97db83a3962f758bde3c79f1c4dd. Production is unchanged from that head; only BasicFunctionalityTest.java changed in H.
- Original PR production B: e07455a0f953e702da27f677c51ea894b7bafc3a.
- Pre-fix proof P: detached B with H's BasicFunctionalityTest.java and TestPseudoReturnFields.java overlaid. The original tests-only commit 4ccd146 is obsolete for this phase's final tests.
- Seed: 5188C0DE, passed as -Ptests.seed=5188C0DE and verified in the test runner output.
- Module: :solr:core.
- Windows claim: claims/pr-5188-unified-snippet-windows-round-2.md.
- Implementation report: reports/pr-5188-glob-field-loading-implementation-round-2.md, with final test SHA-256 hashes.
- New receipt deliverable: receipts/PR-5188-glob-field-loading-round-2.md.
- New report deliverable: reports/pr-5188-glob-field-loading-gate-round-2.md.
- Durable evidence: reports/pr-5188-glob-field-loading-round-2-evidence/ or another durable, user-accessible archive linked from the report.

The previous gate has terminal result claims/pr-5188-glob-field-loading-round-1-vm6-result.md and a receipt. This is a new phase. Do not edit the previous job, claim, reports or receipt, and do not silently reuse their head as this phase's proof.

## Claim and preflight

Read one live board snapshot, WORKFLOW.md, START-HERE.md, UI-PLAYBOOK.md, OPEN-WORK.md, host capability record, decision map/ladder, owner complaints and this implementation report. Recheck claims and deliverables for this phase. Perform detached tidy preflight before claiming as WORKFLOW requires. Claim in a new add-only file; first push wins. Record H/B/P test hashes, snapshot, host/JDK/runner, start UTC, log paths and seed. Acquire the host build lock and disk/inode guard. Keep all Gradle work serialized and heartbeat in the host file.

Read the fork tip before running. If it differs from H, reconcile ownership and post a new exact-head job before testing that head. Read the live PR 5188 head for overlap and record it; the pinned B stays the pre-fix source. Never update apache/solr or the original contributor's branch.

## Restored requirement

BasicFunctionalityTest.testGlobHighlightPrefetch uses q=title:keyword and fq=id:7783. The ID filter selects the fixture, and the main query supplies the single-field text query to implicit highlighting. It now requires exactly <em>keyword</em> from both original and unified, as well as eager cached id/title/source_s, untouched lazy unrelated_s, and exact finite/glob response fields. There is no conditional skip of the unified snippet assertion.

The new independent testGlobHighlightSnippets sends 24 requests covering original/unified, implicit query/explicit hl.q, weightMatches true/false, and fl=source_s/source_*/*. Every row must return the exact positive title snippet. It uses no fallback summary or alternate field. Its success on B is a fixture precondition, not a bug discriminator. The two existing loading methods carry discrimination.

This setup is a proposed repair, not an executed explanation of the old multi-field Boolean query's missing snippet. If a positive row still produces no snippet, diagnose the query/schema/highlighter path and repair the fixture. Do not remove or conditionally skip the assertion, silently disable weightMatches, accept null/empty snippets, or declare the old exception accepted.

## Mandatory Linux runs

Keep exact commands, UTC start/end, exit code, actual printed seed and fresh per-leg XML. Use :solr:core:cleanTest before each leg and archive its results before the next clean. Clear stale output; skipped or unmatched filters do not count as execution. The commands below are task/filter inputs to the supported guarded runner, with its normal environment and absolute temp paths.

1. Verify P's production is exactly B and its two overlaid test blobs match final H. Record full hashes, not just short refs. Verify the candidate production delta since 51e92b6 is empty.
2. Positive fixture on P:
   ./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testGlobHighlightSnippets -Ptests.seed=5188C0DE
   Must pass with zero skips. All 24 cases must satisfy the positive snippet assertion. Capture or instrument the row labels if diagnosing a failure; do not count a setup/compile error as an expected base failure.
3. Pre-fix RTG on P:
   ./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testGlobRTGStoredFieldLoading -Ptests.seed=5188C0DE
   Must execute and fail for unrelated_s should remain lazy (or the same loaded-field invariant with its actual diagnostic), after response-shape checks pass.
4. Pre-fix prefetch on P:
   ./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testGlobHighlightPrefetch -Ptests.seed=5188C0DE
   Must execute and fail for original: source_* did not prefetch source_s. The finite original control must pass first. A snippet failure is a fixture failure requiring repair, not sensitivity proof. Leg 2 independently proves that both highlighters can produce snippets on B.
5. Post-fix focused on exact final H:
   ./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest.testGlobRTGStoredFieldLoading --tests org.apache.solr.BasicFunctionalityTest.testGlobHighlightPrefetch --tests org.apache.solr.BasicFunctionalityTest.testGlobHighlightSnippets --tests org.apache.solr.search.TestPseudoReturnFields.testGlobRTGUncommittedFields -Ptests.seed=5188C0DE
   All four methods, including the full snippet matrix and both loading methods, must pass with zero skips. Keep the original explicit/glob, cold/warm RTG, transformer/synthetic-name, fl=*, pseudo-only and uncommitted visibility controls.
6. Full modified/neighbor suites on that same H:
   ./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.BasicFunctionalityTest --tests org.apache.solr.search.TestPseudoReturnFields --tests org.apache.solr.response.TestRetrieveFieldsOptimizer --tests org.apache.solr.search.ReturnFieldsTest --tests org.apache.solr.highlight.TestUnifiedSolrHighlighter -Ptests.seed=5188C0DE
   Record actual class counts/errors/failures/skips from fresh XML. BasicFunctionalityTest should have the added method relative to the old 25-test receipt. No hardcoded summary substitutes for XML.
7. At the same final head, run tidy (tree clean afterward), :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true, and :solr:core:check -x test. Record each exit. No silent exclusions, and do not call this a whole-repository check.

## Repairs and evidence

A failure is investigated as a repair; never blindly repeat a test that actually ran and failed. Follow the documented one-relaunch rule only for attempts that never executed tests. Narrow test/format repairs within the existing test scope are authorized. Commit as Nick Shanin with no attribution trailers; push normally to the same fork branch; read back the full SHA. Add a repair report and a superseding job naming the new head. Do not move this immutable job's head. If tests or formatting change, rebuild P using the final test overlay and repeat the matched legs at the final head. Any proposed production repair needs an evidence-based scope decision recorded before changing it.

Before completing the handoff, publish durable evidence: runner/commands or command manifest, status/exit file, each raw leg log, final clean-tree/head proof, test-overlay hashes, and fresh per-leg JUnit XML. Include the positive fixture and both negative loading assertion messages. A VM-local absolute path alone is insufficient. A durable archive is acceptable if the report links to it and includes its hash and contents manifest.

Green requires the positive snippet matrix on P and H, both expected loading failures on P, every post-fix test/check green at the same final pushed head, and accessible evidence. No unified-snippet exception is permitted. A failure/incomplete run produces an explicit report and remains pending. The receipt's final section may draft the incremental change description using actual results, without claiming disk-I/O measurements, latency gains, full core tests or whole-repository checks. No public PR/comment/JIRA write is authorized by this job.
