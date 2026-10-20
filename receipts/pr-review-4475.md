# Receipt: PR review apache/solr #4475 (vm4)

- PR: https://github.com/apache/solr/pull/4475, open, not draft, head branch `handling_empty_queries_in_FiltersQParser` on `renatoh/solr`, base `c851aae5c8`, 7 files, +176/-87, `mergeable_state` unknown at the snapshot (GitHub files API).
- Files: `SolrQueryParserBase.java` (+18), `BoolQParserPlugin.java` (+6/-8), `FiltersQParser.java` (+5/-1), `SolrPluginUtils.java` (+4/-1), `schema.xml` (+1), `TestMmBoolQParserPlugin.java` (+35), `SolrPluginUtilsTest.java` (+107/-77).
- Base check at `c8ab71430f`: `SolrQueryParserBase.java` already calls `createFieldQuery` (line 529), so the new override is an addition to an existing call path. The `BoolQParserPlugin` "before" lines in the PR do not match the current file, so the PR needs a rebase before it can be checked against current code.
- Gate: none run.
- Outcome: NEEDS-CHANGES. Draft: `drafts/pr-review/4475.md`.
