# Review: SOLR-17840 preserve partial results (phone agent, round 1)

Verdict: OPEN-READY

Reviewer: phone agent (vm1), at the owner's direction, 2026-10-22. Builder and gater of record: phone-agent-2. This is an independent read of the diff plus independent verification runs; the reviewer's runs are listed separately from the gate claims they check.

## Branch

- Branch: `solr-17840-submit` at `6829ee47a3d699bfcb07c981189acc18172e725e` (3 commits), base and merge-base Apache main `5b2ac4dea21a7c86e31e2ba0cea58c262dc558c8`.
- Diff: 7 files, 475 insertions, 8 deletions. Production: `QueryComponent.java` (9 lines), `DocsStreamer.java` (5), `QueryLimits.java` (25). Tests: new `TestQueryComponentPartialResults` (161) and `TestPartialResultsFetch` (274); `TestLTRQParserPlugin` loses only its `@Ignore` and the now-unused import. Changelog entry present and correctly shaped (author by name only, ticket link).

## Findings

1. Premise and scope. SOLR-17840 is a Blocker naming two Lucene 10 problems: DisjunctionDISIApproximation becoming final, and timeouts not working properly. The first is already handled on main: `MultiFeaturesScorer` in the LTR module implements its own final `nextDoc`/`advance` mimicking that class. The timeout problem is what remains, and this branch is scoped to exactly it. The PR title ("Preserve LTR results and honor partialResults") does not overclaim the ticket.

2. The defect is a publication-order bug, and the fix matches it. On the base, `QueryComponent` calls `maybeExitWithPartialResults` before creating and adding the result context; when a limit has tripped and partial results are allowed, the method marks the response partial and returns true, and the component returns without publishing anything, so the hits the LTR reranker preserved (it reverts the rerank and keeps the original ranking) never reach the response: `numFound` 0 against the expected 4. The change stores the flag, publishes the `BasicResultContext`, and only then returns early. Nothing else in the method's flow changes.

3. The disallowed path is untouched. When `partialResults` is false, `maybeExitWithPartialResults` throws `QueryLimitsExceededException` before any publication, exactly as on the base, and the existing handler converts that into the empty response marked `omitted`. The new component test asserts that shape explicitly (response is an empty `SolrDocumentList`, `partialResults` header reads `omitted`), covering the failure an earlier version of this work hit.

4. The fetch window is narrow and contained. `QueryLimits.withPartialResultsFetch` suppresses `shouldExit()` only while one already-selected document is fetched by `DocsStreamer.next()`, only when partial results are allowed and the limit has already tripped, through a ThreadLocal saved and restored in a finally block, with nesting handled. Transformers run after the fetch, outside the window, so limit checks resume before them. A limit that trips mid-fetch is not retroactively suppressed; the regression scenario is a limit tripped during search or rerank, before streaming starts, and that is the case covered.

5. Relation to the handed-back candidate. The earlier candidate (`solr-17840-preserve-partial-results-phone-agent-2` at 3999ff0be34) was handed back with a demonstrated doc-values fetch and response-streaming timeout to address. This branch is that work completed: the streaming fetch no longer aborts on the already-tripped limit, and the full LTR class passes at the head (see verification), where the candidate failed its focused class.

6. Tests are behavioral. `TestQueryComponentPartialResults` drives the real local SearchHandler component loop with a mocked searcher and a limit that trips after the search returns; it covers disallowed, explicitly allowed, and default partial-results settings. `TestPartialResultsFetch` covers suppression scope, restoration after an exception, nesting, the `partialResults=false` non-suppression case, that untripped limits are still consulted, ThreadLocal isolation across threads, and an end-to-end `DocsStreamer` fetch. Expectations are stated as response shapes and document identity, never derived from the code under test. Both new classes extend `SolrTestCase`.

## Verification (reviewer's own runs, this host)

Seed `1BB02D3762B56611` throughout; counts from fresh JUnit XML; logs at `~/workspace/tools/review-17840-head-core.log`, `review-17840-head-ltr.log`, `review-17840-base-ltr.log`.

- Head `6829ee47a3`: `TestQueryComponentPartialResults` 3 run, 0 skipped, 0 failures. `TestPartialResultsFetch` 7 run, 0 skipped, 0 failures. `TestLTRQParserPlugin` 15 run, 0 skipped, 0 failures.
- Base `5b2ac4dea21` with only the `@Ignore("SOLR-17840")` and its import removed: `TestLTRQParserPlugin` 15 run, exactly 1 failure, `ltr_expensiveFeatureRescoring_shouldTimeOutAndReturnPartialResults`, message `mismatch: '4'!='0' @ response/numFound`. This reproduces the draft's claimed base failure exactly.
- Not rerun by the reviewer: `TestQueryLimits` (2 tests) and `ComponentStageLimitsTest` (4) from the gate's 31-test total, and tidy, Error Prone, and the module checks, which the gate record covers at this head.

## Draft accuracy

The draft the owner uploaded (agent 2's, "for Nick to approve; not opened") checks against the diff and the runs above: file and line citations match, the base failure text matches, the head counts match for every class the reviewer reran. Its Limits are honest (full suites not run, mocked searcher in the component test, grouped and distributed handling out of scope). One precision: the sentence "only an already-selected document fetch may finish after a limit trips" holds for a limit already tripped when the fetch starts, which is the regression case; it does not promise protection for a limit tripping mid-fetch.

## Packaging notes

- The branch carries 3 commits. Squash to one before opening, tree verified identical afterward, per the owner's standing pattern; the gate evidence and this review then carry to the squashed head.
- No PR draft existed in the branch receipt; the uploaded draft is appended to `receipts/SOLR-17840.md` as its final section with this review.

## Limits of this review

Full core and LTR suites were not run by the reviewer. Distributed and grouped partial-results behavior was read about only through the code paths above and is not exercised by the new tests; the draft already says so.
