# Comment sweep batch 7 (vm5, 2026-10-26), drafts only, nothing posted

## SOLR-4424
- Decision: CANDIDATE
- Affected versions (JIRA): 4.0
- Updated: 2013-02-12, status: Open (no resolution, no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, DOMUtil.addToNamedList reads the name with getAttr and adds the element under a null name without any check (solr/solrj/src/java/org/apache/solr/common/util/DOMUtil.java:140 and solr/solrj/src/java/org/apache/solr/common/util/DOMUtil.java:153). A null name is valid for NamedList, so the change that could be made is to reject attributes other than name on children of lst, which would catch a misspelt "Name" as proposed in the ticket. Is this still wanted, [~jkrupan]?
- Citations checked: yes. solr/solrj/src/java/org/apache/solr/common/util/DOMUtil.java:76-78, 133-155

## SOLR-4501
- Decision: CLOSE
- Affected versions (JIRA): 4.1
- Updated: 2013-02-25, status: Open (no comments, no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, the mlt count is no longer passed as the flags argument: solr/core/src/java/org/apache/solr/handler/component/MoreLikeThisComponent.java:128 and solr/core/src/java/org/apache/solr/handler/component/MoreLikeThisComponent.java:134 pass the flags variable set at solr/core/src/java/org/apache/solr/handler/component/MoreLikeThisComponent.java:83-86, and the count is read separately at solr/core/src/java/org/apache/solr/handler/component/MoreLikeThisComponent.java:236. Is closure OK?
- Citations checked: yes. solr/core/src/java/org/apache/solr/handler/component/MoreLikeThisComponent.java:83-86, 128, 134, 236, 404-405

## SOLR-2660
- Decision: HOLD
- Affected versions (JIRA): 3.3, 4.0-ALPHA
- Updated: 2013-03-06, status: Open (no fixVersion)
- Draft: none. The reporter's proposal (phraseAsBoolean and factory methods for phrase queries on omitPositions fields) is not settled in the ticket: rcmuir (2012-03-05) said he was open to other opinions, and janhoy (2013-03-05) asked to commit a first part. The code cannot settle whether the feature is wanted, and the exception path for phrases on omitted positions lives in Lucene, outside solr/.
- Citations checked: yes. solr/core/src/java/org/apache/solr/parser/SolrQueryParserBase.java:545-560 (no getPhraseQuery, getMultiPhraseQuery or phraseAsBoolean under solr/core/src/java/org/apache/solr/parser/, checked by git grep)

## SOLR-4614
- Decision: CLOSE
- Affected versions (JIRA): 4.1
- Updated: 2013-03-19, status: Open (no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, ClusterState no longer has a getSlices method and only exposes getCollection (solr/solrj/src/java/org/apache/solr/common/cloud/ClusterState.java:112). CloudSolrClient throws a SolrException with "Collection not found" before it reaches ClientUtils.addSlices when the collection is missing (solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java:988-994). Is closure OK, [~mumrah]?
- Citations checked: yes. solr/solrj/src/java/org/apache/solr/common/cloud/ClusterState.java:112, solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java:988-994, solr/solrj/src/java/org/apache/solr/client/solrj/util/ClientUtils.java:269-279

## SOLR-4295
- Decision: CANDIDATE
- Affected versions (JIRA): 4.0, 4.2
- Updated: 2013-03-19, status: Open (no comments, no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, only setFacetPrefix has a field-specific overload, which builds the "f.<field>.facet.prefix" name (solr/solrj/src/java/org/apache/solr/client/solrj/request/SolrQuery.java:395-398). setFacetLimit, setFacetMinCount, setFacetMissing and setFacetSort (solr/solrj/src/java/org/apache/solr/client/solrj/request/SolrQuery.java:439, 458, 478, 498) have no field variant. A change that could be made is adding field overloads for those four setters using the same pattern with FacetParams.FACET_LIMIT, FACET_MINCOUNT, FACET_MISSING and FACET_SORT (solr/solrj/src/java/org/apache/solr/common/params/FacetParams.java:76, 82, 94, 130). Is this still wanted, [~cbartolo]?
- Citations checked: yes. solr/solrj/src/java/org/apache/solr/client/solrj/request/SolrQuery.java:390-398, 439-442, 458-461, 478-481, 498-501; solr/solrj/src/java/org/apache/solr/common/params/FacetParams.java:76, 82, 94, 130

## SOLR-4610
- Decision: CLOSE
- Affected versions (JIRA): 3.4, 3.6.2, 4.1, 4.2
- Updated: 2013-03-20, status: Open (no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, QueryParsing.toString(Query, IndexSchema) formats TermQuery values through the field type (solr/core/src/java/org/apache/solr/search/QueryParsing.java:234-237), and its javadoc names it as the schema-aware alternative to Query.toString (solr/core/src/java/org/apache/solr/search/QueryParsing.java:338-346). That javadoc also says the helper does not format custom Query classes, which is the documented limit of this route. Is closure OK, [~lundgren]?
- Citations checked: yes. solr/core/src/java/org/apache/solr/search/QueryParsing.java:229-237, 338-350

## SOLR-4627
- Decision: HOLD
- Affected versions (JIRA): 4.1 (fixVersion 4.1, released; not a 7.x line)
- Updated: 2013-03-21, status: Open
- Draft: none. The warmup loop still reads only top-level values (solr/core/src/java/org/apache/solr/core/QuerySenderListener.java:71-87), and grouped results sit under the "grouped" key (solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java:1843). Grouped documents come from rb.retrievedDocuments (solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java:733-737), which is filled on the distributed path by solr/core/src/java/org/apache/solr/search/grouping/distributed/responseprocessor/StoredFieldsShardResponseProcessor.java:56. The code does not show whether single-node grouping fetches those documents during warmup, so it cannot settle the report.
- Citations checked: yes. solr/core/src/java/org/apache/solr/core/QuerySenderListener.java:71-87; solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java:733-737, 1843; solr/core/src/java/org/apache/solr/search/grouping/endresulttransformer/GroupedEndResultTransformer.java:100-101, 118, 123, 126-131; solr/core/src/java/org/apache/solr/search/grouping/distributed/responseprocessor/StoredFieldsShardResponseProcessor.java:56

## SOLR-4431
- Decision: CLOSE
- Affected versions (JIRA): 4.1
- Updated: 2013-04-10, status: Open (no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, the admin UI still routes the cloud view at "#/~cloud" (solr/webapp/web/js/angular/app.js:93), and the Jetty example still redirects only the root path (solr/server/etc/jetty.xml:121-129), so no /admin, /cores or /cloud shortcut exists. The ticket has -1 votes against shortcut URLs from hossman (2013-04-09) and markrmiller@gmail.com (2013-04-10), because a core or collection named "cloud" would collide, and no committer approval for the patch. Is closure as Won't Fix OK, [~mbennett]?
- Citations checked: yes. solr/webapp/web/js/angular/app.js:90-96; solr/server/etc/jetty.xml:121-129

## SOLR-4686
- Decision: CLOSE
- Affected versions (JIRA): 4.1
- Updated: 2013-04-11, status: Open (no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, the offset mapping is still the Lucene HTMLStripCharFilter behaviour, and the only Solr-side use is solr/core/src/java/org/apache/solr/update/processor/HTMLStripFieldUpdateProcessorFactory.java:64, so this tree has no separate change to make. In the ticket, the committer sarowe (2013-04-11) declined to change end-tag offsets because mixed markup such as "xxx <b>yyy</b>" would become imbalanced, and the reporter proposed closing as won't change. Is closure as Won't Fix OK, [~floerke@doctronic.de]?
- Citations checked: yes. solr/core/src/java/org/apache/solr/update/processor/HTMLStripFieldUpdateProcessorFactory.java:25, 64 (git grep, no other Solr-side HTMLStripCharFilter use in solr/core/src/java or solr/solrj/src/java)

## SOLR-4782
- Decision: CANDIDATE
- Affected versions (JIRA): 4.2.1
- Updated: 2013-05-02, status: Open (no comments, no fixVersion)
- Draft: From code reading against apache/main b73f03976fa, collection delete carries only the name and followAliases in its remote message (solr/core/src/java/org/apache/solr/handler/admin/api/DeleteCollection.java:68-75), and DeleteCollectionCmd hard-codes DELETE_INSTANCE_DIR and DELETE_DATA_DIR to true (solr/core/src/java/org/apache/solr/cloud/api/collections/DeleteCollectionCmd.java:116-117). A change that could be made is to read those two flags from the message with default true, as DeleteReplicaCmd already does (solr/core/src/java/org/apache/solr/cloud/api/collections/DeleteReplicaCmd.java:306-309), and to add them to the delete request and remote message. Is this still wanted, [~jcr]?
- Citations checked: yes. solr/core/src/java/org/apache/solr/handler/admin/api/DeleteCollection.java:53-75; solr/core/src/java/org/apache/solr/handler/admin/CollectionsHandler.java:523-529; solr/core/src/java/org/apache/solr/cloud/api/collections/DeleteCollectionCmd.java:114-117; solr/core/src/java/org/apache/solr/cloud/api/collections/DeleteReplicaCmd.java:304-309

## Holds and nones
- SOLR-2660 (HOLD): the ticket's proposal (phraseAsBoolean, factory methods) is undecided in the ticket, and the phrase exception path is in Lucene, so the code cannot settle it.
- SOLR-4627 (HOLD): grouped documents reach the response through rb.retrievedDocuments, which is only filled on the distributed path; the code does not show whether single-node warmup fetches grouped documents, so it cannot settle the report.
- NONE: no tickets.
