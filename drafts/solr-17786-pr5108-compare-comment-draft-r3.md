# DRAFT r3, NOT POSTED: comment on apache/solr PR 5108

Owner note (not part of the comment, remove before posting): Nick reviewed the text. Supersedes drafts/solr-17786-pr5108-compare-comment-draft-r2.md. Compile and test results are from local runs on 2026-10-25 (JDK 21, Linux aarch64).

---

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Thanks for the work on this PR. The follow-up below builds directly on your commit `32f80171030c`. It is meant to sit on top of this PR, not replace it.

**The PR head does not compile: 14 errors in three files.**

Running `./gradlew :solr:core:compileJava :solr:core:compileTestJava :solr:solrj:compileTestJava` at [`32f80171030c`](https://github.com/HoustonPutman/solr/blob/32f80171030ca1108cd20292f50232d2729ed930) fails with 14 errors:

- [`SortSpec.java` lines 54 to 68](https://github.com/HoustonPutman/solr/blob/32f80171030ca1108cd20292f50232d2729ed930/solr/core/src/java/org/apache/solr/search/SortSpec.java#L54-L68): the `reverse()` method is unfinished. It has missing semicolons at lines 64 and 67, a missing return at line 68, and an unresolved reference at line 54.
- [`SearchHandler.java` line 468](https://github.com/HoustonPutman/solr/blob/32f80171030ca1108cd20292f50232d2729ed930/solr/core/src/java/org/apache/solr/handler/component/SearchHandler.java#L468): cannot find symbol.
- [`SolrIndexSearcher.java` line 1825](https://github.com/HoustonPutman/solr/blob/32f80171030ca1108cd20292f50232d2729ed930/solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java#L1825): cannot find symbol.

**A follow-up on top of this head compiles and passes its tests.**

The compare shows four commits and 17 files: [compare `cursor-mark-reverse` with the follow-up branch](https://github.com/HoustonPutman/solr/compare/cursor-mark-reverse...nick-boss-tech:solr-17786-reverse-cursor-submit). To apply the follow-up on top of this PR's branch:

```
git fetch https://github.com/HoustonPutman/solr.git cursor-mark-reverse
git checkout -b solr-17786-follow-up 32f80171030ca1108cd20292f50232d2729ed930
git fetch https://github.com/nick-boss-tech/solr.git solr-17786-reverse-cursor-submit
git cherry-pick 32f80171030ca1108cd20292f50232d2729ed930..fde070281383f113a331a9ec5ccde173b66fac3c
git diff --stat 32f80171030ca1108cd20292f50232d2729ed930 fde070281383f113a331a9ec5ccde173b66fac3c
```

The last command should list 17 files and 614 insertions and 127 deletions.

**The tests pass on the follow-up head, and removing each change makes a test fail.**

On `fde070281383f113a331a9ec5ccde173b66fac3c`, run on 2026-10-25:

- `SortSpecReverseTest`, `CursorMarkTest`, `CursorPagingTest` and `DistribCursorPagingTest` with seed 5108C0DE: 16 tests, 0 failures.
- `QueryResponseTest` (SolrJ) with seed 5108C0DE: 7 tests, 0 failures.
- The reverse tests with seed 17786C0DE: 9 tests, 0 failures.
- Removing the response-position reversal in [`QueryComponent.java` lines 1295 to 1299](https://github.com/nick-boss-tech/solr/blob/fde070281383f113a331a9ec5ccde173b66fac3c/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1295-L1299) makes `DistribCursorPagingTest.testReverseCursorPaging` fail with `expected:<[3, 4, 5]> but was:<[5, 4, 3]>`.
- Removing the local result normalization in [`QueryComponent.java` lines 1877 to 1894](https://github.com/nick-boss-tech/solr/blob/fde070281383f113a331a9ec5ccde173b66fac3c/solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java#L1877-L1894) makes `CursorPagingTest.testReverseCursorPaging` fail the same way.
- Restoring the old zero-row markers in [`QueryResult.java` lines 99 to 117](https://github.com/nick-boss-tech/solr/blob/fde070281383f113a331a9ec5ccde173b66fac3c/solr/core/src/java/org/apache/solr/search/QueryResult.java#L99-L117) makes `CursorMarkTest.testZeroRowCursorMarks` fail on a null marker.
- `:solr:core:check` and `:solr:solrj:check` pass. The reference guide builds in a clean checkout.

**Backward sorts keep competitive skipping off for now.**

Skipping is disabled for inverse comparators in [`SortSpec.java` lines 89 to 91](https://github.com/nick-boss-tech/solr/blob/fde070281383f113a331a9ec5ccde173b66fac3c/solr/core/src/java/org/apache/solr/search/SortSpec.java#L89-L91). Results are correct with skipping off. The cost of that has not been measured.

The choice, as a ladder:

1. Is a correct first version acceptable without measured cost for backward sorts? If yes, keep skipping off. This is the recommendation.
2. If benchmarks show that reverse pages cost too much, enable skipping for inverse comparators as a separate change.

Does the first version need to ship with skipping off, as recommended?

**Limits: no benchmarks, and only Linux aarch64 with JDK 21 was tested.** Backward cursors need every participating node to be upgraded. Forward cursor tokens keep their existing encoding.

<details>
<summary>Commits on top of this PR head (4)</summary>

1. [`7ba3b78c45c`](https://github.com/nick-boss-tech/solr/commit/7ba3b78c45c) implements reverse ordering with the existing Lucene comparators, plus the guide section and changelog entry.
2. [`a3d97ca294f`](https://github.com/nick-boss-tech/solr/commit/a3d97ca294f) reformats five files with the project's tidy task. It changes no logic.
3. [`2c8af559c84`](https://github.com/nick-boss-tech/solr/commit/2c8af559c84) runs `DistribCursorPagingTest` with two shards. The test previously ran one shard with two cores.
4. [`fde07028138`](https://github.com/nick-boss-tech/solr/commit/fde07028138) adds `@SuppressForbidden` on `SortSpec.reverse()`, following the same use in `RuleBasedAuthorizationPluginBase`. It changes no behavior.

</details>
