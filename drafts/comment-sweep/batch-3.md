# Comment sweep batch 3 (vm5, 2026-10-26), drafts only, nothing posted

## SOLR-3456
- Decision: CANDIDATE
- Affected versions (JIRA): 3.6, 4.0-ALPHA
- Updated: 2012-05-16, status: Open (no resolution, no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, MoreLikeThisComponent still calls mltHelper.getMoreLikeThis(id, 0, rows, null, flags) at solr/core/src/java/org/apache/solr/handler/component/MoreLikeThisComponent.java:433, so no filter query reaches its similar-document search. MoreLikeThisHandler parses filter queries with QueryUtils.parseFilterQueries at solr/core/src/java/org/apache/solr/handler/MoreLikeThisHandler.java:127 and passes them at line 165. The candidate change is to parse the filters the same way in the component and pass them at MoreLikeThisComponent.java:433 in place of null. Is this still wanted, [~cbieber]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/component/MoreLikeThisComponent.java:433; solr/core/src/java/org/apache/solr/handler/MoreLikeThisHandler.java:127, 165

## SOLR-3461
- Decision: CLOSE
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2012-05-20, status: Open (no resolution, no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, the method is now testStressRecovery in solr/core/src/test/org/apache/solr/search/TestStressRecovery.java:66, and line 67 already skips it on Windows with assumeFalse("FIXME: ...", Constants.WINDOWS), which is the suppression the ticket asked for. On main, testStressRecovery exists only in that file, not in the TestRealTimeGet class named in the ticket. Is closure OK, [~uschindler]?
- Citations checked: yes, solr/core/src/test/org/apache/solr/search/TestStressRecovery.java:66-67

## SOLR-3350
- Decision: CLOSE
- Affected versions (JIRA): 3.5, 4.0-ALPHA
- Updated: 2012-05-31, status: Open (no resolution, no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, TextField.parseFieldQuery no longer uses a hard-coded enablePositionIncrements flag; it builds the phrase with new QueryBuilder(analyzer).createPhraseQuery(field, queryText) at solr/core/src/java/org/apache/solr/schema/TextField.java:228-231, so the analyzer's own token positions drive the result. The true/false switches the ticket describes are absent on main. Is closure OK, [~teofili]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/schema/TextField.java:228-231

## SOLR-3498
- Decision: CANDIDATE
- Affected versions (JIRA): 3.6, 4.0-ALPHA
- Updated: 2012-06-01, status: Open (no resolution, no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, ContentStreamUpdateRequest no longer exists under solr/; its successor ContentWriterUpdateRequest (solr/solrj/src/java/org/apache/solr/client/solrj/request/ContentWriterUpdateRequest.java:36-40) never reads getCommitWithin(), and AbstractUpdateRequest.setCommitWithin (solr/solrj/src/java/org/apache/solr/client/solrj/request/AbstractUpdateRequest.java:145-148) only stores the field, while getParams() (AbstractUpdateRequest.java:118-120) does not carry it. The candidate change is to have ContentWriterUpdateRequest write UpdateParams.COMMIT_WITHIN into its request params when commitWithin is set, which is what the reporter's setParam workaround does by hand. Is this still wanted, [~cm]?
- Citations checked: yes, solr/solrj/src/java/org/apache/solr/client/solrj/request/ContentWriterUpdateRequest.java:36-40; solr/solrj/src/java/org/apache/solr/client/solrj/request/AbstractUpdateRequest.java:27, 118-120, 145-148; solr/solrj/src/java/org/apache/solr/client/solrj/request/XMLRequestWriter.java:92; solr/solrj/src/java/org/apache/solr/client/solrj/request/JavaBinUpdateRequestCodec.java:63-64; solr/solrj/src/java/org/apache/solr/common/params/UpdateParams.java:41

## SOLR-3576
- Decision: HOLD
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2012-06-25, status: Open (no resolution, no fixVersion)
- Draft: none (HOLD: the ticket asks which default, latency or throughput, should ship for HttpShardHandlerFactory, which is a maintainer policy choice the code cannot settle; the current default is SolrHttpConstants.DEFAULT_MAXCONNECTIONSPERHOST = 100000 at solr/solrj/src/java/org/apache/solr/client/solrj/impl/SolrHttpConstants.java:24, applied at solr/core/src/java/org/apache/solr/handler/component/HttpShardHandlerFactory.java:298-303).
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/component/HttpShardHandlerFactory.java:298-303; solr/solrj/src/java/org/apache/solr/client/solrj/impl/SolrHttpConstants.java:24

## SOLR-3552
- Decision: CLOSE
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2012-08-31, status: Open (no resolution, no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, the partialResults response header is set when a shard response carries an exception (solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java:1101-1102 and 1239-1242) and is documented as the flag for incomplete results (solr/solr-ref-guide/modules/query-guide/pages/common-query-parameters.adoc:326-331). A replica that answers from a stale index is not flagged by this path, so the split-brain case is only partly covered. Is closure OK, [~otis]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java:1101-1102, 1239-1242; solr/solr-ref-guide/modules/query-guide/pages/common-query-parameters.adoc:326-331

## SOLR-2999
- Decision: CANDIDATE
- Affected versions (JIRA): 3.1, 3.2, 3.3, 3.4, 3.5, 4.0-ALPHA
- Updated: 2012-09-07, status: Open (no resolution, no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, the buildOnOptimize branch rebuilds whenever the new searcher's reader has exactly one leaf (solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java:814-816), so an empty commit that opens a new searcher on an already optimized index would rebuild, as the ticket reports. The candidate change is to skip that rebuild when the new and current searchers' readers have the same index version, a check the newSearcher listener at SpellCheckComponent.java:811-812 can make before calling buildSpellIndex. Is this still wanted, [~olivers]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java:811-816

## SOLR-3277
- Decision: CLOSE
- Affected versions (JIRA): 3.6, 4.0-ALPHA
- Updated: 2012-09-07, status: Open (no resolution, no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, a glob source with a dynamic-reference destination is registered as a DynamicCopy (solr/core/src/java/org/apache/solr/schema/IndexSchema.java:1002-1007). Copy targets are computed per matching field name by DynamicCopy.getTargetField (IndexSchema.java:1302-1305), and DocumentBuilder applies them through getCopyFieldsList (solr/core/src/java/org/apache/solr/update/DocumentBuilder.java:183; IndexSchema.java:1530-1538), so title_text is copied to title_phonetic. Is closure OK, [~erickerickson]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/schema/IndexSchema.java:970-983, 1002-1007, 1302-1305, 1530-1538; solr/core/src/java/org/apache/solr/update/DocumentBuilder.java:183

## SOLR-3525
- Decision: HOLD
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2012-09-07, status: Open (no resolution, no fixVersion)
- Draft: none (HOLD: the bracketed text comes from the per-field similarity's explain output in Lucene's similarity classes, which a solr/-only read cannot cite; solr/core/src/java/org/apache/solr/search/similarities/SchemaSimilarityFactory.java:134 defines the SchemaSimilarity wrapper and no explain text exists under solr/core, so the fix cannot be settled from solr/ code).
- Citations checked: yes, solr/core/src/java/org/apache/solr/search/similarities/SchemaSimilarityFactory.java:134

## SOLR-3564
- Decision: HOLD
- Affected versions (JIRA): 4.0-ALPHA
- Updated: 2012-09-07, status: Open (no resolution, no fixVersion)
- Draft: none (HOLD: ResponseBuilder.setResult still dereferences its QueryResult argument at solr/core/src/java/org/apache/solr/handler/component/ResponseBuilder.java:488-489; the grouping path builds a QueryResult with no DocListAndSet at QueryComponent.java:1664-1667, so a null reaches setResults. Whether the timeAllowed re-query still hits that path needs a run, which this read-only pass cannot do. The collation re-query failure is caught and logged as a warning at solr/core/src/java/org/apache/solr/spelling/SpellCheckCollator.java:173-176.)
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/component/ResponseBuilder.java:488-491; solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java:1664-1667; solr/core/src/java/org/apache/solr/spelling/SpellCheckCollator.java:173-176; solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java:838-839, 1545-1550

## Holds and nones
- SOLR-3525 HOLD: the explain text that prints the empty brackets lives in Lucene's similarity code, outside the solr/ citation scope, so the fix cannot be settled here.
- SOLR-3576 HOLD: the ticket asks for a default-setting policy (latency vs throughput) that the code cannot decide.
- SOLR-3564 HOLD: the reported NPE site still exists in shape, and a grouping path can pass a null DocListAndSet into it; confirming whether the timeAllowed re-query reaches it needs a run.
- NONE: none in this batch.
- Open PRs: a GitHub search of apache/solr for each of the ten keys found no open PRs (unauthenticated API; the gh CLI is not installed on this host).
