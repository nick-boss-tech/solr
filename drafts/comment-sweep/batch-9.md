# Comment sweep batch 9 (vm5, 2026-10-26), drafts only, nothing posted

## SOLR-5181
- Decision: CLOSE
- Affected versions (JIRA): 4.5
- Updated: 2013-08-21, status: Open
- Draft: From code reading against apache/main b73f03976fa, a failed core CREATE now appends the root cause to the error message as " Caused by: ..." (solr/core/src/java/org/apache/solr/core/CoreContainer.java:1574-1590). For a missing solrconfig.xml that root cause is the "Can't find resource 'solrconfig.xml'" error raised in solr/core/src/java/org/apache/solr/core/SolrResourceLoader.java:384-385. So the terse 4.5 message described in the ticket is no longer produced on main. Is closure OK, [~jkrupan]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/core/CoreContainer.java:1574-1590 and 1718; solr/core/src/java/org/apache/solr/core/SolrResourceLoader.java:384-385

## SOLR-5278
- Decision: CANDIDATE
- Affected versions (JIRA): 4.0, 4.4
- Updated: 2013-09-26, status: Open
- Draft: From code reading against apache/main b73f03976fa, SolrCore.initCodec takes the configured CodecFactory and returns its codec (solr/core/src/java/org/apache/solr/core/SolrCore.java:1568-1604) without checking that the codec name resolves through SPI. One change that could be made is to call Codec.forName(codec.getName()) on that codec in initCodec and throw a SolrException naming the codec when the lookup fails, which is the fail-fast check the ticket asks for. Is this still wanted, [~scottsch]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/core/SolrCore.java:1095 and 1568-1604

## SOLR-5293
- Decision: CANDIDATE
- Affected versions (JIRA): 4.4
- Updated: 2013-10-01, status: Open
- Draft: From code reading against apache/main b73f03976fa, the only header hook on a shard request is ShardRequest.headers (solr/core/src/java/org/apache/solr/handler/component/ShardRequest.java:66-67), which code sets internally, for example the lock-id header in solr/core/src/java/org/apache/solr/cloud/api/collections/CollectionHandlingUtils.java:746, and HttpShardHandler.prepareLBRequest copies it onto the sub-request (solr/core/src/java/org/apache/solr/handler/component/HttpShardHandler.java:203-210). Nothing copies headers from the incoming request, because SolrRequestInfo keeps only the principal from the HTTP request (solr/core/src/java/org/apache/solr/request/SolrRequestInfo.java:59, 162-165). One change that could be made is to keep a configured list of inbound header names alongside the request info set at solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java:516 and copy those into sreq.headers in prepareLBRequest. Is this still wanted, [~tvaillancourt]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/component/ShardRequest.java:66-67; solr/core/src/java/org/apache/solr/cloud/api/collections/CollectionHandlingUtils.java:746; solr/core/src/java/org/apache/solr/handler/component/HttpShardHandler.java:203-210; solr/core/src/java/org/apache/solr/request/SolrRequestInfo.java:59, 162-165; solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java:516

## SOLR-5435
- Decision: HOLD
- Affected versions (JIRA): 4.3.1
- Updated: 2013-11-11, status: Open
- Draft: none. Reason: the code cannot settle it by reading. solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java:737-893 (splitIntoClauses) does split the quoted "jenkins " phrase and the field-qualified clause as separate clauses, but the reported output "used_name:text:^7.0 | (text:text: text:text)" is produced later in the query build, and confirming it needs a probe run, which this drafting task does not allow.
- Citations checked: yes, solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java:737-893 and 897-905 (getFieldName)

## SOLR-5521
- Decision: CLOSE
- Affected versions (JIRA): 4.2.1
- Updated: 2013-12-06, status: Open
- Draft: From code reading against apache/main b73f03976fa, the per-shard MLT query puts the unique key and value into a field local-param, "{!field f=<key>}<value>", built in solr/core/src/java/org/apache/solr/handler/component/MoreLikeThisComponent.java:140-151, so a leading hyphen in the id is not read as query syntax. The unquoted q=itemId:-1578997856 in the report is the user's own query parameter, and a bare negative term after a colon is a query-parser syntax error by design. Is closure OK, [~wtmitchell3]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/component/MoreLikeThisComponent.java:118-122 and 140-151

## SOLR-5559
- Decision: HOLD
- Affected versions (JIRA): 4.6
- Updated: 2013-12-19, status: Open
- Draft: none. Reason: the failure mode (one node keeping core folders and a stale entry, then an Overseer queue loop exception) needs a test run to confirm or refute. solr/core/src/java/org/apache/solr/cloud/api/collections/DeleteCollectionCmd.java:121-150 tolerates failed replicas (failedReplicas) and waits only for the collection to leave the cluster state (waitForState, 60 seconds), so the ticket's requested "nothing left on the cluster before OK" check is a design question for the owner.
- Citations checked: yes, solr/core/src/java/org/apache/solr/cloud/api/collections/DeleteCollectionCmd.java:64, 121-150

## SOLR-5546
- Decision: CLOSE
- Affected versions (JIRA): 4.5.1
- Updated: 2013-12-20, status: Open
- Draft: From code reading against apache/main b73f03976fa, the JSON Facet API documents sum, avg, min and max aggregations (solr/solr-ref-guide/modules/query-guide/pages/json-facet-api.adoc:569-572), and a terms facet can nest them per bucket, as in the "sales_rank": "sum(sales_rank)" example (json-facet-api.adoc:818-828), implemented by solr/core/src/java/org/apache/solr/search/facet/SumAgg.java:28-31. That covers the per-bucket aggregate the ticket asks for, although the facet.*.sum.field syntax proposed there is not offered. Is closure OK, [~raintung.li]?
- Citations checked: yes, solr/solr-ref-guide/modules/query-guide/pages/json-facet-api.adoc:569-572 and 818-828; solr/core/src/java/org/apache/solr/search/facet/SumAgg.java:28-31

## SOLR-5566
- Decision: CLOSE
- Affected versions (JIRA): 4.4, 4.6
- Updated: 2013-12-20, status: Open
- Draft: From code reading against apache/main b73f03976fa, HighlightComponent.finishStage collects shard highlights by document position and builds its response through getAllHighlights, which uses SolrPluginUtils.removeNulls (solr/core/src/java/org/apache/solr/handler/component/HighlightComponent.java:215-241 and 275-280). removeNulls skips null entries (solr/core/src/java/org/apache/solr/util/SolrPluginUtils.java:680-690), so the SimpleOrderedMap built from an array with null entries in the reported stack trace is not built on this path. Is closure OK, [~radomirml]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/component/HighlightComponent.java:215-241 and 275-280; solr/core/src/java/org/apache/solr/util/SolrPluginUtils.java:680-690

## SOLR-5591
- Decision: CANDIDATE
- Affected versions (JIRA): 4.5.1
- Updated: 2013-12-30, status: Open
- Draft: From code reading against apache/main b73f03976fa, SolrJ builds multipart bodies through MultipartContentWriter (solr/solrj/src/java/org/apache/solr/client/solrj/request/RequestWriter.java:47-61), and only the Jetty client writes them (solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java:757-762). The JDK client, which HttpSolrClient uses when the Jetty module is not on the classpath (solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java:389-399, 433), throws for multipart at solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpJdkSolrClient.java:302-303, so SolrCloud requests fail on multipart in that setup. One change that could be made is to write the multipart body in HttpJdkSolrClient instead of throwing at those lines. Is this still wanted, given the comments that servlet limits no longer apply, [~kwright@metacarta.com]?
- Citations checked: yes, solr/solrj/src/java/org/apache/solr/client/solrj/request/RequestWriter.java:47-61; solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java:757-762; solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java:389-399, 433; solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpJdkSolrClient.java:302-303

## SOLR-5619
- Decision: HOLD
- Affected versions (JIRA): 4.6
- Updated: 2014-01-08, status: Open
- Draft: none. Reason: the feature is absent on main (solr/core/src/java/org/apache/solr/schema/BinaryField.java:105-111 emits only a StoredField, and solr/core/src/java/org/apache/solr/schema/BinaryField.java:74-76 throws on sort), but the ticket's open design questions (use cases, query semantics, tokenizing of binary values, raised by erickerickson on 2014-01-08) have no ruling, so an owner decision is needed before a comment can be made.
- Citations checked: yes, solr/core/src/java/org/apache/solr/schema/BinaryField.java:74-76 and 105-111

---

Holds and nones:
- SOLR-5435 HOLD: the reported "text:text:" output is produced in the query build, which code reading cannot confirm without a probe run.
- SOLR-5559 HOLD: whether a replica can leave core folders and wedge the Overseer queue needs a test run, and the wait-for-clusterstate-only cleanup is an owner design question.
- SOLR-5619 HOLD: the feature is absent on main, but its use-case and query-semantics questions are open and need an owner ruling.
- No NONE decisions in this batch.
- JIRA remote links were empty for all ten tickets, so no open PR was found; the HOLD reasons above are the code-based ones.
