# Comment sweep batch 4 (vm5, 2026-10-26), drafts only, nothing posted

## SOLR-3882
- Decision: CLOSE
- Affected versions (JIRA): 4.0-BETA (released)
- Updated: 2012-09-24, status: Open (no fixVersion, no comments)
- Draft: From code reading against apache/main b73f03976fa, the DOWNNODE update marks a node's replicas DOWN and keeps them in the collection state rather than removing them (solr/core/src/java/org/apache/solr/cloud/overseer/NodeMutator.java:47-58, 78-85). Removing those entries is done with DELETENODE, which deletes all replicas on a node (solr/core/src/java/org/apache/solr/handler/admin/api/DeleteNode.java:39-44; solr/solr-ref-guide/modules/deployment-guide/pages/cluster-node-management.adoc:848-849). [~jimtronic] Is closure OK?
- Citations checked: yes, solr/core/src/java/org/apache/solr/cloud/overseer/NodeMutator.java:47-58, 78-85; solr/core/src/java/org/apache/solr/handler/admin/api/DeleteNode.java:39-44; solr/solr-ref-guide/modules/deployment-guide/pages/cluster-node-management.adoc:848-849

## SOLR-3887
- Decision: CANDIDATE
- Affected versions (JIRA): 4.0-BETA (released)
- Updated: 2012-09-25, status: Open (two comments: hossman removed the 4.0 fixVersion as a feature after 4.0 freeze; reporter vreon replied)
- Draft: From code reading against apache/main b73f03976fa, a top-level JSON array is read only as a list of adds (solr/core/src/java/org/apache/solr/handler/loader/JsonLoader.java:181-183, 540-552), and the top-level add, delete and commit keys are each dispatched one at a time (JsonLoader.java:167-196), so there is no single ordered array of mixed operations. A change that could be made is an "operations" key in JsonLoader.processUpdate that reads an array of single-command objects and dispatches each in order. [~vreon] Is this still wanted?
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/loader/JsonLoader.java:167-196, 181-183, 540-552

## SOLR-3907
- Decision: CANDIDATE
- Affected versions (JIRA): 4.0 (released)
- Updated: 2012-09-30, status: Open (no comments)
- Draft: From code reading against apache/main b73f03976fa, geodist returns the full haversine distance (solr/core/src/java/org/apache/solr/search/function/distance/HaversineFunction.java:93, registered at solr/core/src/java/org/apache/solr/search/ValueSourceParser.java:422), and the existing squared function sqedist is Euclidean only (solr/core/src/java/org/apache/solr/search/ValueSourceParser.java:704). A change that could be made is a geosqedist parser beside geodist that returns the haversine a-term before the final square-root step, which orders points the same way. [~billnbell] Is this still wanted?
- Citations checked: yes, solr/core/src/java/org/apache/solr/search/function/distance/HaversineFunction.java:93; solr/core/src/java/org/apache/solr/search/ValueSourceParser.java:422, 704

## SOLR-3919
- Decision: HOLD
- Affected versions (JIRA): 4.0-BETA (released)
- Updated: 2012-10-07, status: Open (no comments)
- Draft: none. HOLD: the merge is done by Lucene's Highlighter.getBestTextFragments; Solr only passes the mergeContiguous flag (solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java:342-344, 698, 769), and the Lucene merge code is not in this tree, so the code cannot settle whether the single-hit merge is a Solr bug.
- Citations checked: yes, solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java:342-344, 698, 769

## SOLR-3944
- Decision: CLOSE
- Affected versions (JIRA): 4.0 (released)
- Updated: 2012-10-14, status: Open (no comments)
- Draft: From code reading against apache/main b73f03976fa, the shipped start script starts Solr with -XX:+CrashOnOutOfMemoryError (solr/bin/solr:1334-1336), so an OutOfMemoryError ends the JVM instead of leaving the index writer running in the state reported here. Startup logging for that flag is at solr/core/src/java/org/apache/solr/servlet/CoreContainerProvider.java:218-236, so a restart after an OOM is the designed recovery. [~phancox] Is closure OK for installs that do not use bin/solr?
- Citations checked: yes, solr/bin/solr:1331-1336; solr/core/src/java/org/apache/solr/servlet/CoreContainerProvider.java:218-236

## SOLR-3962
- Decision: CANDIDATE
- Affected versions (JIRA): 3.5 (released), 3.6 (released), 4.0 (released)
- Updated: 2012-10-19, status: Open (no comments)
- Draft: From code reading against apache/main b73f03976fa, addPhraseFieldQueries keeps a bare *:* clause among the phrase inputs (solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java:297-324), and addShingledPhraseQueries parses that text through the analyzer (ExtendedDismaxQParser.java:619-629, 678). A change that could be made is to skip the bare *:* clause in addPhraseFieldQueries so no pf phrase is built for the match-all query. [~tkurosaka] Is this change OK?
- Citations checked: yes, solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java:177, 297-324, 619-629, 678, 878

## SOLR-3991
- Decision: HOLD
- Affected versions (JIRA): 4.0 (released)
- Updated: 2012-10-25, status: Open (no comments)
- Draft: none. HOLD: this is a thread interaction (warmup and spellcheck collation waiting on a searcher) that needs a run to settle; the 4.0 path named in the ticket (replication statistics waiting on a searcher) has no match in solr/core/src/java/org/apache/solr/handler/ReplicationHandler.java on main, and the remaining collation path in solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java:116-126 cannot be settled by reading alone.
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/ReplicationHandler.java (no getSearcher or getStatistics match); solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java:116-126

## SOLR-4014
- Decision: CLOSE
- Affected versions (JIRA): 4.0-ALPHA, 4.0-BETA, 4.0 (all released)
- Updated: 2012-10-30, status: Open (one comment by reporter raintung.li, a note that the plugin source is simple)
- Draft: From code reading against apache/main b73f03976fa, SearchHandler builds its components from the components, first-components and last-components init args (solr/core/src/java/org/apache/solr/handler/component/SearchHandler.java:91-93, 165-169), so a custom SearchComponent can be added to a /select handler without rewriting the handler. The ref guide documents this at solr/solr-ref-guide/modules/configuration-guide/pages/requesthandlers-searchcomponents.adoc:278-292, and the proposed plugin element is not read because SearchHandler.init only looks for shardHandlerFactory children (SearchHandler.java:120-128). [~raintung.li] Is closure OK?
- Citations checked: yes, solr/core/src/java/org/apache/solr/handler/component/SearchHandler.java:91-93, 120-128, 165-169; solr/solr-ref-guide/modules/configuration-guide/pages/requesthandlers-searchcomponents.adoc:278-292

## SOLR-4017
- Decision: HOLD
- Affected versions (JIRA): 4.0 (released)
- Updated: 2012-11-05, status: Open (no comments)
- Draft: none. HOLD: the request is an analyzer-based signature option, which widens the scope of SignatureUpdateProcessorFactory; the code hashes raw field values (solr/core/src/java/org/apache/solr/update/processor/SignatureUpdateProcessorFactory.java:70-71), and the ticket itself says the raw-text approach may be kept for efficiency. This needs an owner decision on scope.
- Citations checked: yes, solr/core/src/java/org/apache/solr/update/processor/SignatureUpdateProcessorFactory.java:70-71

## SOLR-3785
- Decision: HOLD
- Affected versions (JIRA): 4.0 (released)
- Updated: 2012-11-09, status: Open (many comments; last by steff1193 on 2012-11-09 proposing ZkStateReader-side state calculation)
- Draft: none. HOLD: a graceful close publishes DOWNNODE, which marks the node's replicas DOWN (solr/core/src/java/org/apache/solr/cloud/ZkController.java:847, 3092; solr/core/src/java/org/apache/solr/cloud/overseer/NodeMutator.java:78-85), but a peer killed without a graceful close gets no DOWNNODE, since the live-nodes listener at ZkController.java:1210-1236 only generates nodeLost events. Closing that gap would change behaviour and needs a run to confirm, so it is an owner call.
- Citations checked: yes, solr/core/src/java/org/apache/solr/cloud/ZkController.java:847, 1210-1236, 3092; solr/core/src/java/org/apache/solr/cloud/overseer/NodeMutator.java:78-85

---

## Holds and nones
- SOLR-3919 HOLD: the single-hit snippet merge happens in Lucene's Highlighter, which is outside this tree; Solr only passes the flag.
- SOLR-3991 HOLD: a deadlock needs a run; the ticket's 4.0 path is not present in ReplicationHandler.java on main.
- SOLR-3785 HOLD: graceful shutdown is covered, but a killed peer gets no DOWNNODE, and fixing that is a behaviour change for the owner.
- SOLR-4017 HOLD: analyzer-based signatures are a new option (scope widening), for the owner.
- NONE: none in this batch.
