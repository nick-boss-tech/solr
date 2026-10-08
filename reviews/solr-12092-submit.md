# solr-12092-submit

- Branch: origin/solr-12092-submit
- Head: ca9573dabd38
- Base: upstream/main at 9d7cc2884e8a (merge-base 14c7aac0d151, 45 commits behind)
- Scope: 6 commits. `ExtendedDismaxQParser.java` (+9/-2: `isStopFilter` now also matches `ManagedStopFilterFactory`, used in both loops of `noStopwordFilterAnalyzer`), `TestManagedStopFilterFactory.java` (+93: three new tests), test fixture `schema-rest.xml` (+36: three new field types, no new fields), ref guide `edismax-query-parser.adoc` (+6), changelog `SOLR-12092-edismax-managed-stopwords.yml` (+9). The handoff doc from `3a54f4f3fdf` is removed at head (`c171ae986d7`).
- Verdict: Nearly
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff and the code at the listed head.

## Delta check against earlier disposition

- Handoff: re-dispositioned in round 33 at this head. Head unchanged: `origin/solr-12092-submit` is at `ca9573dabd38`, as listed.
- Commit history: the code change is `cde23f0a9d7`. The latest commit, `ca9573dabd3`, is described in its message as covering the index-analyzer stop filter tests and the ref-guide wording. Which commit added which file was not traced.

## Verified code facts

- `ManagedStopFilterFactory extends BaseManagedTokenFilterFactory extends TokenFilterFactory` (`solr/core/.../rest/schema/analysis/`), so it is a valid element of the `TokenFilterFactory[]` checked by `isStopFilter`.
- Both loops in `noStopwordFilterAnalyzer` (index side: "return the query analyzer unchanged if the indexer has a stop filter"; query side: "remove the first stop filter") now use the same predicate. The result is consistent: a managed filter on either side is treated the same as `StopFilterFactory`.
- All other stop-filter logic in `ExtendedDismaxQParser` goes through `noStopwordFilterAnalyzer` (line 1117). No other `instanceof StopFilterFactory` check was left unchanged.
- The `stopwords.txt` fixture in `collection1/conf` contains `a`, `an`, and `the`. The tests' expectations for `the` depend on this, and it holds.
- The import of `ManagedStopFilterFactory` from `org.apache.solr.rest.schema.analysis` places a `search` class on a `rest` package dependency. Same module (`solr/core`), so no build problem.

## Findings (ranked)

1. **LOW, verified. The index-side change is not in the changelog.** `isStopFilter` is also applied to the index analyzer. If the index analyzer has a `ManagedStopFilterFactory`, the query analyzer is now left alone even when `stopwords=false` (test 3 shows this). Before the change, the index's managed filter was not seen, so the query analyzer's stop filter was removed. The changelog only says "keeps the words removed by a ManagedStopFilterFactory in the query analyzer". The index-side behaviour should be named, or the owner should accept that the title covers it.

2. **Positive, verified by reading (not run). Discriminating tests.** On base, `testEdismaxStopwordsFalseKeepsManagedStopwords` fails at its `stopwords=false` assertion: base does not recognize the managed filter, so the query analyzer is left unchanged, "the" is removed, and the query matches the document (1 result, where the test expects 0). `testEdismaxStopwordsFalseWithManagedStopFilterInIndexAnalyzer` fails on base at the `stopwords=false` assertion: base strips the query `StopFilterFactory` (the index's managed filter is not seen), so "the" is required and nothing matches (0 results, where the test expects 1).

3. **Honestly labelled, verified. `testEdismaxStopwordsFalseWithManagedStopFilterInBothAnalyzers` passes on base.** Its Javadoc says so ("it pins the shape"). On base the query analyzer is also unchanged here, so the result is the same either way. It is a regression pin, not a fix test. That is the right label.

4. **LOW, verified. Shared fixture.** `schema-rest.xml` is loaded by nine test classes (`TestBulkSchemaAPI`, `TestRestManager`, `TestSolrConfigHandler`, `TestConfigSetImmutable`, `SolrRestletTestBase`, `TestManagedStopFilterFactory`, `TestManagedSynonymGraphFilterFactory`, `TestUseDocValuesAsStored2`, `RootFieldTest`). The change adds three field types and no fields. `TestBulkSchemaAPI` looks up field types by name, not by count. Whether any test asserts the full field-type listing or count was not checked, so this is a hypothesis.

5. **LOW, hypothesis. Shared managed resource.** All three new tests PUT `["a","an","the"]` to the shared `/schema/analysis/stopwords/english` resource and do not restore it. Other tests in `TestManagedStopFilterFactory` put their own lists first, so the sharing looks safe. Not checked against every test in the class.

6. **LOW, verified (style).** Test 3's Javadoc describes the unpatched-parser failure in detail. Good. The first test's comment "the index analyzer has no stop filter" matches the fixture.

## Owner call (not decided here)

Does the index-side change (finding 1) belong in this ticket, or should it be split? The code is one predicate used in two places, so splitting would be artificial. The owner should decide whether the changelog title needs to name the index-side effect. Not decided here.

## Not checked

- Not compiled, formatted (spotless), or run. Pass and fail on base and head is a hypothesis (finding 2).
- Full field-type listing assertions in the nine `schema-rest.xml` users (finding 4).
- Shared managed-stopword state across all tests in `TestManagedStopFilterFactory` (finding 5).
- Error Prone was not run.
- Nothing pushed or posted to GitHub or JIRA.
