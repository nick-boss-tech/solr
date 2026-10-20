# Receipt: PR review apache/solr #4109 (vm4)

- PR: https://github.com/apache/solr/pull/4109, open, not draft, head branch `SOLR-18101-SolrjCommitApi` on `dsmiley/solr`, 2 files, +121/-14 (GitHub files API), updated 2026-09-04, `mergeable_state` unknown at the snapshot.
- Files: `solr/solrj/src/java/org/apache/solr/client/solrj/request/AbstractUpdateRequest.java` (+98/-14), `UpdateRequest.java` (+23).
- Enum check: the patch removes `public enum ACTION` near the top of `AbstractUpdateRequest` and adds `public enum ACTION` further down, so the enum is moved. Base callers of `ACTION` (`RecoveryStrategy.java:321`, `CollectionHandlingUtils.java:264`, `SolrCmdDistributor.java:300`, `SolrBenchState.java`, `AnalysisAfterCoreReloadTest.java`, `ApiToolTest.java`) would still compile.
- Tests: no test files in the PR.
- Formatting: two consecutive blank lines in the added code after `commitAsync()`.
- Gate: none run.
- Outcome: NEEDS-CHANGES. Draft: `drafts/pr-review/4109.md`.
