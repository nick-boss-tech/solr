# solr-9595-submit

- Branch: origin/solr-9595-submit
- Head: 7ff1350ab7b6 (matches the listed head; checked against the ticket worktree)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d9680, 16 commits behind, 3 commits ahead)
- Scope: 3 commits, 4 files, +88/-2. `solr/core/src/java/org/apache/solr/index/SlowCompositeReaderWrapper.java` (`terms(field)` caches the non-null `MultiTerms` in a `ConcurrentHashMap`; `liveDocs` is built once in the constructor and returned by `getLiveDocs()`), `solr/core/src/test/org/apache/solr/index/TestSlowCompositeReaderWrapper.java` (new `testTermsAndLiveDocsAreCached`), changelog `changelog/unreleased/SOLR-9595-cache-slow-reader-terms.yml` (`type: changed`, author Nick Shanin), and `SOLR-9595-TESTING.md` (author's hypothetical-reproduction note, left in place).
- Verdict: Ready for review
- Reviewer: review-agent A3 (claude-haiku-5-5, Claude Code), 2026-10-08
- Patch: none

Nothing here was compiled, formatted, or run. No Gradle, no spotless, no test run, no fail-before proof. Every claim below rests on reading the diff and the code at the listed head. `SOLR-9595-TESTING.md` is labeled "hypothetical reproduction (nothing was compiled or run)" and is treated as unverified.

## Findings (ranked)

1. **LOW, verified. Benign race on the terms cache.** `terms(field)` uses `cachedTerms.get`, then `MultiTerms.getTerms`, then `put` (`SlowCompositeReaderWrapper.java:162-172`), not `computeIfAbsent`. Two threads that miss at the same time each compute a `Terms` and the second `put` overwrites the first. Both values are valid, so reads stay correct. Only the identity guarantee from `assertSame` is lost under a race. Not patched: the test is single-threaded and the behaviour is acceptable.

2. **LOW, verified. `liveDocs` is now computed eagerly.** `liveDocs = MultiBits.getLiveDocs(in)` runs in the constructor (`SlowCompositeReaderWrapper.java:134`), so every wrapper pays the O(leaves) cost even if `getLiveDocs()` is never called. The old code built it lazily on each call. How often wrappers are built was not traced, so the impact is unknown. Not patched.

3. **Verified (checked, no issue). The new test follows its file's convention.** `testOrdMapsAreCached` (line 104) and `testCoreListenerOnSlowCompositeReaderWrapper` (line 45) also have no `@Test`, and so does the new `testTermsAndLiveDocsAreCached` (line 135). This is the file's style, not a defect. It also suggests that unannotated `test*` methods in `SolrTestCase` classes already run on `main`.

4. **Verified (checked, no issue). Test API and assertions.** `SlowCompositeReaderWrapper.wrap(IndexReader)` is `public static` (line 94). `terms(String)` and `getLiveDocs()` are public (lines 162, 367). The test's `assertSame` on repeated `terms("f")` and `getLiveDocs()` would fail on the old code, where each call builds a new object, so the fail-before is a real assertion failure. The `assertNull` for an unknown field and the liveDocs content checks (doc 0 deleted, doc 1 live) follow from the index the test builds.

5. **Verified (checked, no issue). Changelog.** `nick: nick-boss-tech` is used by 40 fragments on `upstream/main`, so the key is valid for this repo. `type: changed` is a valid value.

6. **Hypothesis, LOW. Shared `Bits` and `Terms`.** Callers that relied on `getLiveDocs()` returning a fresh object each time would now share one. `Bits` is read-only, so nothing should mutate it. No caller was searched.

## Owner calls (not decided here)

None needed.

## Not checked

- Nothing compiled, formatted, or run. No focused test, no fail-before run, no Spotless, no Error Prone.
- How often `SlowCompositeReaderWrapper` is constructed on the search path, for finding 2.
- All callers of `getLiveDocs()` and `terms()` on the wrapper, for finding 6.
- `SOLR-9595-TESTING.md` is treated as unverified. Left in place.
