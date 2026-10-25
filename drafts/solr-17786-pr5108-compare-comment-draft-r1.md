# DRAFT r1, NOT POSTED: comment on apache/solr PR 5108 (supersedes drafts/solr-17786-pr5108-compare-comment-draft.md, whose link compared by commit sha in apache/solr and did not name the PR's branch)

Owner note (not part of the comment): Nick has not reviewed this text. Posting to a PR is an owner action. Compare link checked against the GitHub API on 2026-10-25 against the PR's branch cursor-mark-reverse on HoustonPutman/solr (PR head 32f80171030c, unchanged): 4 commits ahead, 0 behind, 17 files.

---

🤖 *AI text below* 🤖 *(not yet reviewed by Nick Shanin)*

**Compare with the branch we are testing: four commits on this head.**

[Compare the PR branch cursor-mark-reverse with our branch](https://github.com/HoustonPutman/solr/compare/cursor-mark-reverse...nick-boss-tech:solr-17786-reverse-cursor-submit)

The branch starts at this PR's head `32f80171030c` and adds four commits:

1. `7ba3b78c45c` implements reverse ordering with the existing Lucene comparators. It changes `SortSpec`, `CursorMark`, `QueryComponent`, `ResponseBuilder`, `SearchHandler`, `QueryResult` and `SolrIndexSearcher`, and adds the guide section and changelog entry.
2. `a3d97ca294f` reformats five files with the project's tidy task. It changes no logic.
3. `2c8af559c84` runs `DistribCursorPagingTest` with two shards (`sliceCount = 2; fixShardCount(2);`). The test previously ran one shard with two cores. This is three lines in the test.
4. `fde07028138` adds `@SuppressForbidden(reason = "singletonList(null) is intentional")` on `SortSpec.reverse()`, following the same use in `RuleBasedAuthorizationPluginBase`. It changes no behavior.

**Results on the branch head `fde070281383`.**

- `SortSpecReverseTest`, `CursorMarkTest`, `CursorPagingTest`, `DistribCursorPagingTest` (seed 5108C0DE): 16 tests, 0 failures, 0 errors, 0 skipped.
- `QueryResponseTest` (SolrJ, seed 5108C0DE): 7 tests, 0 failures, 0 errors, 0 skipped.
- Reverse tests (seed 17786C0DE): 9 tests, 0 failures, 0 errors, 0 skipped.
- Removing the response-position reversal makes `DistribCursorPagingTest.testReverseCursorPaging` fail with `expected:<[3, 4, 5]> but was:<[5, 4, 3]>`.
- Removing the local result normalization makes `CursorPagingTest.testReverseCursorPaging` fail the same way.
- Restoring the old zero-row markers makes `CursorMarkTest.testZeroRowCursorMarks` fail on a null marker.
- `:solr:core:check` and `:solr:solrj:check` pass. The reference guide build passes in a clean checkout.

The diff is the easiest way to see the change. Commit 3 is the one a reviewer should look at first, because it changes which shard layout the distributed test runs on.
