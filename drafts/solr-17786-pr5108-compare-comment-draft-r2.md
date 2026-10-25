# DRAFT r2, NOT POSTED: comment on apache/solr PR 5108

Owner note (not part of the comment): Nick has not reviewed this text. Posting to the PR is an owner action. Supersedes drafts/solr-17786-pr5108-compare-comment-draft-r1.md (same link, text restructured). Compile result for this head is from a local run on 2026-10-25 (logs/pr5108-head-compile on vm2).

---

🤖 *AI text below* 🤖 *(not yet reviewed by Nick Shanin)*

**This head does not compile, and we have a follow-up branch that does.**

**What happens today.** At head `32f80171030c`, `:solr:core:compileJava` and `:solr:core:compileTestJava` fail with 14 errors on JDK 21, Linux aarch64. The errors are in three files. `SortSpec.java` has an unfinished `reverse()` method, with missing semicolons at lines 64 and 67, a missing return at line 68, and an unresolved reference at line 54. `SearchHandler.java:468` and `SolrIndexSearcher.java:1825` each report "cannot find symbol".

**What this comment offers.** A follow-up branch on top of this head that compiles and passes the tests below: [compare the PR branch `cursor-mark-reverse` with our branch](https://github.com/HoustonPutman/solr/compare/cursor-mark-reverse...nick-boss-tech:solr-17786-reverse-cursor-submit). The compare shows 4 commits and 17 files. The commits are listed in the details block at the end.

**Proof.** On branch head `fde070281383`:
- Core tests `SortSpecReverseTest`, `CursorMarkTest`, `CursorPagingTest` and `DistribCursorPagingTest` with seed 5108C0DE: 16 tests, no failures.
- SolrJ `QueryResponseTest` with seed 5108C0DE: 7 tests, no failures.
- Reverse tests with seed 17786C0DE: 9 tests, no failures.
- Three checks fail when a piece of the change is removed, which shows the tests exercise it. Removing the response-position reversal fails `DistribCursorPagingTest.testReverseCursorPaging` with `expected:<[3, 4, 5]> but was:<[5, 4, 3]>`. Removing the local result normalization fails `CursorPagingTest.testReverseCursorPaging` the same way. Restoring the old zero-row markers fails `CursorMarkTest.testZeroRowCursorMarks` on a null marker.
- `:solr:core:check` and `:solr:solrj:check` pass. The reference guide builds in a clean checkout.

**The choice.** Competitive skipping is off for backward sorts. This keeps the result correct, because skipping is the riskiest part to verify for inverse comparators. We have not measured what it costs. Our recommendation is to keep skipping off for now. If benchmarks later show that reverse pages cost too much, enabling skipping for inverse comparators is the next step, and it can be a separate change. Do you agree that correctness should land first?

<details>
<summary>Commits on top of this head (4)</summary>

1. `7ba3b78c45c` implements reverse ordering with the existing Lucene comparators. It changes `SortSpec`, `CursorMark`, `QueryComponent`, `ResponseBuilder`, `SearchHandler`, `QueryResult` and `SolrIndexSearcher`, and adds the guide section and changelog entry.
2. `a3d97ca294f` reformats five files with the project's tidy task. It changes no logic.
3. `2c8af559c84` runs `DistribCursorPagingTest` with two shards (`sliceCount = 2; fixShardCount(2);`). The test previously ran one shard with two cores.
4. `fde07028138` adds `@SuppressForbidden(reason = "singletonList(null) is intentional")` on `SortSpec.reverse()`, following the same use in `RuleBasedAuthorizationPluginBase`. It changes no behavior.

</details>

**Limits.** No benchmarks were run. Only Linux aarch64 with JDK 21 was tested, with no Windows run. Backward cursors need every participating node to be upgraded. Forward cursor tokens keep their existing encoding.
