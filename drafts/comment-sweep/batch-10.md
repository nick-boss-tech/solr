# Comment sweep batch 10 (vm5, 2026-10-26), drafts only, nothing posted

## SOLR-5630
- Decision: CANDIDATE
- Affected versions (JIRA): 4.3
- Updated: 2014-01-14, status: Open
- Draft: From code reading against apache/main b73f03976fa, ExtendedDismaxQParser sets up.setAllowLeadingWildcard(true) as a literal at solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java:148, and the getter at solr/core/src/java/org/apache/solr/parser/SolrQueryParserBase.java:414 is called only from tests, so no schema, solrconfig or request setting reaches the flag. One change that could be made is to read a boolean edismax parameter, default true, in place of that literal at line 148. Is this still wanted, [~pkeegan]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java:148; solr/core/src/java/org/apache/solr/parser/SolrQueryParserBase.java:137, 407-408, 414

## SOLR-5646
- Decision: CANDIDATE
- Affected versions (JIRA): 4.5, 4.5.1, 4.6
- Updated: 2014-01-20, status: Open
- Draft: From code reading against apache/main b73f03976fa, SolrEventListener declares only postCommit, postSoftCommit and newSearcher (solr/core/src/java/org/apache/solr/core/SolrEventListener.java:23-61) with no close or destroy hook. SolrCore keeps the registered listeners in firstSearcherListeners and newSearcherListeners (solr/core/src/java/org/apache/solr/core/SolrCore.java:738-739), and doClose() at line 1739 does not touch them. One change that could be made is a default no-op close() on SolrEventListener, called from doClose() for both lists. Is this still wanted, [~raintung.li]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/core/SolrEventListener.java:23-61; solr/core/src/java/org/apache/solr/core/SolrCore.java:738-739, 1739

## SOLR-5696
- Decision: CLOSE
- Affected versions (JIRA): 4.6.1
- Updated: 2014-02-05, status: Open
- Draft: From code reading against apache/main b73f03976fa, ConcurrentUpdateBaseSolrClient.close() calls scheduler.shutdown() and waits up to 60 seconds for the runners to drain the queue (solr/solrj/src/java/org/apache/solr/client/solrj/impl/ConcurrentUpdateBaseSolrClient.java:661-667) before the HTTP client is closed in the finally block (lines 677-679). This ordering applies to the client-owned executor set at lines 218-221; shutdownNow() at line 690 still cancels queued work. Is closure OK, [~dsmiley]?
- Citations checked: yes, solr/solrj/src/java/org/apache/solr/client/solrj/impl/ConcurrentUpdateBaseSolrClient.java:179-181, 218-221, 256, 301, 653, 661-667, 677-679, 690

## SOLR-5708
- Decision: CLOSE
- Affected versions (JIRA): 4.6, 4.6.1
- Updated: 2014-02-07, status: Open
- Draft: From code reading against apache/main b73f03976fa, SolrConfig.initLibs() no longer loads <lib/> entries from solrconfig.xml: it logs "<lib/> entries no longer supported in solrconfig.xml; ignoring..." (solr/core/src/java/org/apache/solr/core/SolrConfig.java:935-938) and adds only the instance lib directory (lines 922-931). The <lib path=...> option described in the ticket is therefore removed on main rather than broken. Is closure OK, [~searchivarius]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/core/SolrConfig.java:915, 922, 931, 935-938

## SOLR-5726
- Decision: CLOSE
- Affected versions (JIRA): 4.6, 4.6.1
- Updated: 2014-02-14, status: Open
- Draft: From code reading against apache/main b73f03976fa, MoreLikeThisComponent handles the shard-side MLT and merges per-shard results in finishStage (solr/core/src/java/org/apache/solr/handler/component/MoreLikeThisComponent.java:96-136 and 227-289). The per-term query is built from the field type's indexed value in a {!term} local param (lines 165-184), and the unique key is passed in a {!field} local param (lines 140-163), so the pre-analyzed-field and escaping problems in the ticket are not present in this code. The mlt.rfl parameter described in the ticket is not implemented on main. Is closure OK, or is mlt.rfl still wanted, [~mounandi]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/component/MoreLikeThisComponent.java:96, 136, 140, 151, 163-165, 180-184, 227, 279, 282-289 (absence of mlt.rfl checked by grep of solr/core/src/java/org/apache/solr/handler/component/MoreLikeThisComponent.java, solr/core/src/java/org/apache/solr/handler/MoreLikeThisHandler.java and solr/solrj/src/java/org/apache/solr/common/params/MoreLikeThisParams.java)

## SOLR-5717
- Decision: CLOSE
- Affected versions (JIRA): 4.6
- Updated: 2014-02-14, status: Open
- Draft: From code reading against apache/main b73f03976fa, group.field on a numeric field is routed to the function grouping path (solr/core/src/java/org/apache/solr/search/Grouping.java:148-151). Single-valued trie fields map to the LEGACY_INTEGER and LEGACY_LONG uninversion types (solr/core/src/java/org/apache/solr/schema/TrieField.java:211-217), whose parsers read only shift-0 terms through LegacyNumericUtils.filterPrefixCodedInts and filterPrefixCodedLongs (solr/core/src/java/org/apache/solr/uninverting/FieldCache.java:173-181 and 238-241), so the shifted higher-precision terms are not read as group values. Is closure OK, [~ddhutia]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/search/Grouping.java:148-151; solr/core/src/java/org/apache/solr/schema/TrieField.java:211-217; solr/core/src/java/org/apache/solr/uninverting/FieldCache.java:173-181, 238-241; solr/core/src/java/org/apache/solr/uninverting/UninvertingReader.java:348; solr/core/src/java/org/apache/solr/legacy/LegacyNumericUtils.java:386-396

## SOLR-5759
- Decision: HOLD
- Affected versions (JIRA): 4.4
- Updated: 2014-02-21, status: Open
- Draft: none. HOLD reason: the code cannot settle it by reading. Fragment selection is delegated to the Lucene Highlighter through the fragmenter set at solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java:224, so whether a fragment drops part of a phrase at a larger hl.fragsize needs a reproduction (a probe or a JUnit test), which the ticket does not have and which hossman already requested on 2014-02-21.
- Citations checked: yes, solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java:140, 224, 243 (only context; no fragment-boundary logic lives in the tree)

## SOLR-5747
- Decision: HOLD
- Affected versions (JIRA): 4.5, 4.5.1, 4.6, 4.6.1
- Updated: 2014-02-24, status: Open
- Draft: none. HOLD reason: the request is for a new automatic config-change reload across collections, which is scope widening. Main has only manual reload (solr/core/src/java/org/apache/solr/core/CoreContainer.java:1957-1971, with no config-node watcher found by grep in solr/core/src/java/org/apache/solr/cloud/ZkController.java). The ticket's only recorded position is opt-in and default off, but the choice of default is an open maintainer question, so it goes to the owner (decision ladder rung 5).
- Citations checked: yes, solr/core/src/java/org/apache/solr/core/CoreContainer.java:1957-1971; solr/core/src/java/org/apache/solr/cloud/ZkController.java (absence by grep)

## SOLR-5805
- Decision: HOLD
- Affected versions (JIRA): 4.7
- Updated: 2014-03-04, status: Open
- Draft: none. HOLD reason: main has only a pull-based health endpoint (solr/core/src/java/org/apache/solr/handler/admin/HealthCheckHandler.java:63, wired at solr/core/src/java/org/apache/solr/core/CoreContainer.java:879-882) and no background thread that changes cluster state. A self-removing background check is new design work (pluggable or default checker, and what "unhealthy" removes), and the ticket leaves that open, so it is an owner question (rung 5).
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/admin/HealthCheckHandler.java:63; solr/core/src/java/org/apache/solr/core/CoreContainer.java:879-882

## SOLR-5910
- Decision: CANDIDATE
- Affected versions (JIRA): 4.7
- Updated: 2014-03-25, status: Open
- Draft: From code reading against apache/main b73f03976fa, the only free-space check found is in shard split on the parent leader (solr/core/src/java/org/apache/solr/cloud/api/collections/SplitShardCmd.java:855-920), and IndexFetcher does not compare the leader's file sizes with free space before it starts copying (solr/core/src/java/org/apache/solr/handler/IndexFetcher.java:621-634; SIZE is read per file at line 1356). One change that could be made is to sum SIZE over filesToDownload after the empty-list check at line 624 and throw a SolrException when the index directory's file store reports less usable space. Is this still wanted, [~peacewon]?
- Citations checked: yes, solr/core/src/java/org/apache/solr/cloud/api/collections/SplitShardCmd.java:855-856, 908-921; solr/core/src/java/org/apache/solr/handler/IndexFetcher.java:621-635, 1354-1357 (absence of a free-space check in IndexFetcher by grep of solr/core/src/java)

## Holds and nones
- SOLR-5759 HOLD: no reproduction; the fragment logic is in Lucene's Highlighter, so reading the code cannot settle it.
- SOLR-5747 HOLD: scope widening (new automatic config reload, default on or off unresolved); owner question.
- SOLR-5805 HOLD: scope widening (new background self-removal health subsystem); owner question.
- NONE: none in this batch.
