# Comment sweep batch 5 (vm5, 2026-10-26), drafts only, nothing posted

## SOLR-4040
- Decision: HOLD
- Affected versions (JIRA): 4.0 (released)
- Updated: 2012-11-14, status: Open (no resolution, fixVersion 4.0)
- Draft: none. Reason: whether a delete-by-query plus commit in one request leaves hits behind is a cross-shard ordering question; the ticket's own comments link SOLR-3933 (distributed commits not ordered within a request). Reading solr/core/src/java/org/apache/solr/update/processor/DistributedUpdateProcessor.java lines 790-799 does not settle it; it needs a multi-shard probe.
- Citations checked: yes, solr/core/src/java/org/apache/solr/update/processor/DistributedUpdateProcessor.java 790-799

## SOLR-3723
- Decision: CLOSE
- Affected versions (JIRA): 3.4, 3.5, 3.6, 3.6.1, 4.0-ALPHA
- Updated: 2012-11-28, status: Open (no resolution, no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, text_en is defined without autoGeneratePhraseQueries (solr/server/solr/configsets/_default/conf/managed-schema.xml line 346), and TextField defaults the flag to false for schema versions above 1.3 (solr/core/src/java/org/apache/solr/schema/TextField.java lines 74-78). Phrase generation is available through the explicit text_en_splitting type (solr/server/solr/configsets/_default/conf/managed-schema.xml line 394), which matches the ruling in this ticket that applications wanting it should use that type. Is closure OK? [~jkrupan]
- Citations checked: yes, solr/server/solr/configsets/_default/conf/managed-schema.xml 346, 394; solr/core/src/java/org/apache/solr/schema/TextField.java 74-78

## SOLR-4122
- Decision: HOLD
- Affected versions (JIRA): 4.0
- Updated: 2012-11-28, status: Open (no resolution, no fixVersion)
- Draft: none. Reason: the EnglishMinimalStemmer logic that produces "dishe" and "boxe" is not in this tree (the repository has no lucene/ directory), and the filter appears only in configuration here (solr/server/solr/configsets/_default/conf/managed-schema.xml line 363). The code cannot be read, and any comment would have to cite a path outside solr/.
- Citations checked: yes, solr/server/solr/configsets/_default/conf/managed-schema.xml 363

## SOLR-3742
- Decision: CLOSE
- Affected versions (JIRA): 4.0-ALPHA, 4.0-BETA, 4.0, 4.0.1 (4.0.1 not flagged released)
- Updated: 2012-12-05, status: Open (no resolution, no fixVersion; linked Reference to SOLR-1581)
- Draft: From code reading against apache/main b73f03976fa, group.facet takes its grouping from group.field only, so with group.func and no group field getGroupedCounts now returns a BAD_REQUEST ("Specify the group.field as parameter or local parameter") instead of an ArrayIndexOutOfBoundsException (solr/core/src/java/org/apache/solr/request/SimpleFacets.java lines 788-795). The documented rule is that grouped facets are computed based on the first specified group (solr/solr-ref-guide/modules/query-guide/pages/result-grouping.adoc lines 209-210). Is closure OK? [~dev4]
- Citations checked: yes, solr/core/src/java/org/apache/solr/request/SimpleFacets.java 788-795; solr/solr-ref-guide/modules/query-guide/pages/result-grouping.adoc 209-210

## SOLR-4141
- Decision: CLOSE
- Affected versions (JIRA): 4.0
- Updated: 2012-12-12, status: Open (no resolution, no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, mm is applied to the top-level BooleanQuery of the parsed user query (solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java lines 436-448) and to single-field boolean queries (lines 1446-1450), but not to a parenthesised group of clauses nested in a larger query, which matches the explanation given in this ticket. Is closure OK? [~maylein]
- Citations checked: yes, solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java 436-448, 1446-1450

## SOLR-4235
- Decision: HOLD
- Affected versions (JIRA): 4.0
- Updated: 2012-12-26, status: Open (no resolution, no fixVersion)
- Draft: none. Reason: this is a feature request (sort and paging on the parent by a field of the joined side). solr/core/src/java/org/apache/solr/search/JoinQParserPlugin.java has no sort or paging handling, so the request is absent from main, and whether to add it is a scope decision for a maintainer.
- Citations checked: yes, solr/core/src/java/org/apache/solr/search/JoinQParserPlugin.java (grep for sort: no matches)

## SOLR-4290
- Decision: CANDIDATE
- Affected versions (JIRA): 4.0
- Updated: 2013-01-10, status: Open (no resolution, no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, a debugQuery request whose main query has no match never reaches getMoreLikeThis, so rawMLTQuery stays null (solr/core/src/java/org/apache/solr/handler/MoreLikeThisHandler.java lines 181-184 and 252-254, 363-365), and that null is passed into DebugComponent.doStandardQueryDebug, where QueryParsing.toString dereferences it (solr/core/src/java/org/apache/solr/handler/component/DebugComponent.java lines 442-443; solr/core/src/java/org/apache/solr/search/QueryParsing.java lines 333-334). A candidate change is to skip the parsedquery and parsedquery_toString entries in DebugComponent.doStandardQueryDebug when query is null, so the debug call no longer throws. Is this still wanted? [~yydzero]
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/MoreLikeThisHandler.java 181-184, 252-254, 363-365; solr/core/src/java/org/apache/solr/handler/component/DebugComponent.java 442-443; solr/core/src/java/org/apache/solr/search/QueryParsing.java 333-334

## SOLR-4258
- Decision: CANDIDATE
- Affected versions (JIRA): 3.6, 4.0
- Updated: 2013-01-22, status: Open (no resolution, no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, an undefined qf field now fails validation with a SyntaxError (solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java lines 223-229 and 276-282), and rebuildUserQuery rewrites operators only when lowercaseOperators is true (lines 530-535). A malformed query still falls back to parseEscapedQuery (lines 169-172), where bare AND, OR and NOT are quoted as literal phrases (lines 466-471), so "(bar AND grill" searches the literal term and (title:and). A candidate change is to drop bare AND/OR/NOT clauses in escapeUserQuery instead of quoting them. Is this still wanted? [~bleakley]
- Note: the malformed-query half is a recorded choice (decision ladder rung 4): the escape fallback is what makes the operator literal, and dropping it is the change proposed here, not a settled rule.
- Citations checked: yes, solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java 169-172, 223-229, 276-282, 466-471, 530-535

## SOLR-4344
- Decision: CLOSE
- Affected versions (JIRA): 4.1 (released)
- Updated: 2013-01-24, status: Open (no resolution, no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, uploaded multipart file parts are no longer wrapped by the FileItem-based stream named in the ticket; they are wrapped as PartContentStream (solr/core/src/java/org/apache/solr/servlet/SolrRequestParsers.java lines 530-537 and 543-546) and exposed through the public SolrQueryRequest.getContentStreams() (solr/core/src/java/org/apache/solr/request/SolrQueryRequest.java lines 87-88), so code in other packages can read the file stream without the package-private class. Is closure OK? [~sezelee]
- Citations checked: yes, solr/core/src/java/org/apache/solr/servlet/SolrRequestParsers.java 530-537, 543-546; solr/core/src/java/org/apache/solr/request/SolrQueryRequest.java 87-88 (FileItemContentStream: no matches in solr/core/src/java)

## SOLR-4350
- Decision: HOLD
- Affected versions (JIRA): 4.1
- Updated: 2013-01-24, status: Open (no resolution, no fixVersion)
- Draft: none. Reason: the slave asks the leader for the file list of the generation the leader reports (solr/core/src/java/org/apache/solr/handler/IndexFetcher.java lines 544-548, 621), and an empty list with "No files to download for index generation" is logged at lines 391-395. Which leader state produces that after a manual data-directory deletion and restart is a runtime question, so a two-node probe is needed before commenting.
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/IndexFetcher.java 391-395, 544-548, 621

## SOLR-4040 through SOLR-4350: holds and nones
- HOLD SOLR-4040: cross-shard ordering of delete-by-query and commit in one request; needs a multi-shard probe.
- HOLD SOLR-4122: stemmer logic lives outside this tree (no lucene/ directory); cannot be read or cited under solr/.
- HOLD SOLR-4235: feature request (sort and paging on the joined side) that is absent from main; scope decision for a maintainer.
- HOLD SOLR-4350: replication empty-file-list path reached, but the leader state that triggers it needs a two-node probe.
- NONE: none in this batch.
