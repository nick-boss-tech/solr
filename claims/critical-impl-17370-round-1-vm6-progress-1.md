# Progress 1: claim critical-impl-17370-round-1-vm6 (heartbeat and code-read notes)

Heartbeat: 2026-10-27T08:42:29Z

- Pinned main `ee2f1bf2e0109956145d7fd995da63c4091a589f`. Ticket SOLR-17370 read from Jira (open, reporter ravijha, versions 9.5 and 9.6.1, no components).
- The ticket's stack: `HttpSolrCall 500 ... IllegalAccessException: access violation: class org.apache.lucene.document.LongPoint, from public Lookup`, thrown from `Utils.getReflectWriter` (`solr/solrj/src/java/org/apache/solr/common/util/Utils.java:764`) while `JavaBinCodec` writes the response.
- The class `LongPoint` is an anonymous `PointRangeQuery` subclass in lucene-core 10.4.0 (checked with javap). So a Lucene Query object reaches the facet response; the exception text is a symptom of that.
- `git grep` for `IllegalAccessException` in `solr/core/src/java/org/apache/solr/search/facet` at this head: no matches. The triage's pointer to "accumulator selection for string fields" is not yet confirmed as the origin.
- Not yet located: the code that puts a Query into the facet response. Checked and not it: `FacetFieldProcessorByHashDV` (only `calc.formatValue`), `FacetRangeProcessor`/`LongPoint` references in the facet package (none), and the query-related lines in `FacetProcessor.java` (filter and domain handling).
- Premise run (Step 1): not yet executed. The facet tests in this package extend `SolrTestCaseHS`, which the assignment rules out for new tests, so the premise test needs another harness. The worktree `/workspace/worktrees/dx-main-vm6` is already built at this same head and can be reused.
- Next: find the Query origin (search `SolrReturnFields`, `debug`, and the `facet.query` / `facet.range` response writers), then write the premise test on `SolrTestCase` and run it. No branch, no push, no Jira text.
