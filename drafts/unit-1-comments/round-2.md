# Unit 1 comment drafts, round 2 (vm5, 2026-10-26)

Nothing is posted. Round 1 (20 tickets) is in `round-1.md`. This round adds the other 85 ticket-text records, triaged by four parallel read-only passes against apache/main `b73f03976fa`. Evidence paths are relative to the Solr checkout. Each draft still needs a live Jira check at fill time and owner approval.

## Counts (85 this round, 105 with round 1)

| Group | This round | Total with round 1 |
|---|---|---|
| Closure draft | 8 | 13 |
| Maintainer question | 34 | 40 |
| Repro needed | 34 | 41 |
| No comment | 9 | 11 |

## Closure drafts that may be fixed-family, not not-a-bug

- SOLR-6099
- SOLR-6712
- SOLR-7198
- SOLR-8770
- SOLR-11292

Where a draft says the code "now" behaves differently, the ticket was probably fixed later. Those belong in the closure-fixed or obsolete family, with the fixing version or commit cited, and need re-filing before any comment.

## Closure drafts

### SOLR-4859
Evidence: solr/core/src/java/org/apache/solr/update/processor/MinFieldValueUpdateProcessorFactory.java:32 (values must be mutually comparable); solr/core/src/java/org/apache/solr/update/processor/ParseLongFieldUpdateProcessorFactory.java:56 (explicit numeric conversion processor exists); solr/solr-ref-guide/modules/configuration-guide/pages/update-request-processors.adoc:387
Draft: I looked at this against current main. Min and Max compare the values they receive, and numeric conversion is done by the explicit Parse*FieldUpdateProcessorFactory processors, such as ParseLongFieldUpdateProcessorFactory. If you see a real defect on a supported release, reply and I will reopen it. Otherwise I plan to close it as not a bug.

### SOLR-6099
Evidence: solr/core/src/java/org/apache/solr/cloud/api/collections/DeleteShardCmd.java:83-93 (CONSTRUCTION and RECOVERY slices with a range can be deleted; RECOVERY_FAILED is rejected); solr/core/src/java/org/apache/solr/cloud/api/collections/SplitShardCmd.java:325-345 (split cleanup calls DeleteShardCmd for non-active sub-shards)
Draft: I looked at this against current main. DELETESHARD now accepts under-construction and recovering slices with a range (DeleteShardCmd.java:83-93), and SPLITSHARD cleanup calls it for sub-shards that are not active (SplitShardCmd.java:325-345). If you see a real defect on a supported release, reply and I will reopen it. Otherwise I plan to close it as not a bug.

### SOLR-6712
Evidence: solr/core/src/java/org/apache/solr/handler/component/HighlightComponent.java:233-235 (a shard response with no highlight section is skipped)
Draft: I looked at this against current main. The highlight merge now skips shard responses that have no highlight section (HighlightComponent.java:233-235). If you see a real defect on a supported release, reply and I will reopen it. Otherwise I plan to close it as not a bug.

### SOLR-7070
Evidence: solr/solr-ref-guide/modules/indexing-guide/pages/fields.adoc:43 (field names should not start with a digit); solr/core/src/java/org/apache/solr/search/SolrReturnFields.java:225-229 (a plain field name must start with a Java identifier character)
Draft: I looked at this against current main. The reference guide says field names should not start with a digit (fields.adoc:43), and fl reads a plain field name only when it starts with an identifier character (SolrReturnFields.java:225-229). If you see a real defect on a supported release, reply and I will reopen it. Otherwise I plan to close it as not a bug.

### SOLR-7198
Evidence: solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContextBase.java:147-153; solr/core/src/java/org/apache/solr/cloud/LeaderElector.java:359-361
Draft: I looked at this against current main. The leader node is now created in one ZooKeeper multi that also sets the parent node, so it fails if /collections/{collection} has been deleted, and election setup does not create that parent. If you see a real defect on a supported release, reply and I will reopen it. Otherwise I plan to close it as not a bug.

### SOLR-8770
Evidence: solr/solrj/src/java/org/apache/solr/common/util/JavaBinCodec.java:1247-1248 (a null value is written as a NULL tag); JavaBinCodec.java:315-316 (NULL is read back as null); solr/core/src/java/org/apache/solr/update/DocumentBuilder.java:68 (null fields are not added); solr/solrj/src/java/org/apache/solr/client/solrj/util/ClientUtils.java:161-167 (XML writer skips null values).
Draft: I looked at this against current main. The binary writer now sends a null field as a NULL tag, the server reads it back as null, and the field is not added, which matches the XML writer. If you see a real defect on a supported release, reply and I will reopen it. Otherwise I plan to close it as not a bug.

### SOLR-10030
Evidence: solr/core/src/java/org/apache/solr/response/transform/ChildDocTransformer.java:62 ; solr/core/src/java/org/apache/solr/search/SolrReturnFields.java:178-209 (transformer comes from fl) ; solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java:231 (output uses return fields transformer)
Draft: I looked at this against current main. Child documents come back from the realtime get handler only when fl includes [child], so getById with no field list returns none by design. If you see a real defect on a supported release, reply and I will reopen it. Otherwise I plan to close it as not a bug.

### SOLR-11292
Evidence: solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java:957 (alias resolved to its target collections), :987-988 (slices gathered for each resolved collection)
Draft: I looked at this against current main. Alias requests now resolve to the target collections and route across every slice of each one, so they are not limited to the first collection's nodes. If you see a real defect on a supported release, reply and I will reopen it. Otherwise I plan to close it as not a bug.

## Maintainer-call drafts

### SOLR-4983
Evidence: solr/core/src/java/org/apache/solr/cloud/api/collections/Assign.java:182-190 (core name is built as collection_shard_replica_N); solr/core/src/java/org/apache/solr/cloud/api/collections/CreateCollectionCmd.java:320; solr/core/src/java/org/apache/solr/search/join/CrossCollectionJoinQParser.java:106 (fromIndex read as a collection name)
Draft: Should the generated core name change for collection CREATE, or is it an internal detail now that cross-collection joins take a collection name?
1. Close as intended: fromIndex takes a collection name (CrossCollectionJoinQParser.java:106), and core names are generated internally (Assign.java:182-190).
2. Keep open for a change: change the core name to use the collection name, or add a core-name option to CREATE.
3. Split or repro first: split the join question from the core-naming question, then decide each one.

### SOLR-4996
Evidence: solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java:898-960 (non-update requests for a collection are sent to replica endpoints picked by the replica list transformer, one request to one replica list; no propagate option found)
Draft: Should SolrJ send read-only collection requests such as LukeRequest to every replica, or keep sending each request to one replica?
1. Close as intended: a collection request goes to one chosen replica (CloudSolrClient.java:898-960), and callers that need per-replica data can address a core directly.
2. Keep open for a change: add an option that sends read-only admin requests to all replicas.
3. Split or repro first: ask for a LukeRequest run on two replicas that differ, then decide.

### SOLR-5095
Evidence: solr/core/src/java/org/apache/solr/core/SolrCore.java:227 (info registry is a flat ConcurrentHashMap keyed by plugin name); solr/core/src/java/org/apache/solr/core/SolrResourceLoader.java:817-819 (plugins are put by bean name); JmxMonitoredMap named in the ticket: not found
Draft: Should the info registry get namespaces so two plugin types with the same name do not overwrite each other?
1. Close as superseded: the JMX map named in the ticket is not in main (not found), and the flat registry is what main uses (SolrCore.java:227).
2. Keep open for a change: add namespacing to the registry keys (SolrCore.java:227).
3. Split or repro first: ask for a repro on current main that shows a lost plugin entry, then decide.

### SOLR-5235
Evidence: solr/core/src/java/org/apache/solr/update/UpdateLog.java:2401-2410 (replay commit is flagged REPLAY and sent straight to uhandler.commit, skipping the processor chain; comment at 2408-2409 says it is meant to be written to the log)
Draft: Should a replayed commit also run through the update processor chain, or keep calling the update handler directly?
1. Close as intended: the direct call is meant to add the replay commit to the log so it is not replayed again after a restart (UpdateLog.java:2408-2409).
2. Keep open for a change: run the replayed commit through the chain and have the log processor skip it when the REPLAY flag is set.
3. Split or repro first: split custom-processor commit hooks from the log-replay question, and ask for a repro with a custom processor.

### SOLR-5962
Evidence: solr/solrj/src/java/org/apache/solr/common/cloud/Replica.java:52-75 (states are ACTIVE, DOWN, RECOVERING, RECOVERY_FAILED; no replay state); solr/core/src/java/org/apache/solr/cloud/ZkController.java:1505-1524 (startup tlog replay is a blocking wait)
Draft: Should a node show a new replica state, such as REPLAYING, while it replays its tlog, or is the current state set enough?
1. Close as is: startup replay is a blocking step (ZkController.java:1520-1524), and the state set has no replay state (Replica.java:52-75).
2. Keep open for a change: add a replay state to Replica.State so the cluster state shows replay progress.
3. Split or repro first: ask for the state a replica reports during a long replay on current main, then decide.

### SOLR-6364
Evidence: solr/core/src/java/org/apache/solr/response/JSONWriter.java:191-193 (JSON values are written raw, with no quoting); solr/solrj/src/java/org/apache/solr/common/util/JsonTextWriter.java:228-230 (writeLong writes the value as is); no special case for _version_ found in the response writers
Draft: Should the JSON response write _version_ as a string so JavaScript clients keep full precision, or stay a bare number?
1. Close as intended: the bare-number JSON output is the current format (JSONWriter.java:191-193), and JavaScript clients can read the value with a big-number library.
2. Keep open for a change: write _version_ as a JSON string in the response.
3. Split or repro first: ask whether the change should be opt-in per request, then decide.

### SOLR-6714
Evidence: solr/core/src/java/org/apache/solr/cloud/api/collections/CollectionHandlingUtils.java:503-511 (per-replica failures are added to a "failure" list); solr/core/src/java/org/apache/solr/handler/admin/api/ReloadCollectionAPI.java:75-84 (the RELOAD v1 path does not set an error status itself)
Draft: Should RELOAD return an error HTTP status when some replicas fail, or keep the failures in the body of a 200 response?
1. Close as intended: failures are listed in the response body (CollectionHandlingUtils.java:503-511), and the wider status work is tracked in SOLR-6595, as the last comment says.
2. Keep open for a change: return a non-200 status when any replica fails to reload.
3. Split or repro first: split the RELOAD status fix from the wider status work in SOLR-6595, then decide.

### SOLR-6719
Evidence: solr/core/src/java/org/apache/solr/cloud/api/collections/CreateCollectionCmd.java:380 (Solr sets the core name), 392 (property.* params are copied into the core CREATE params); solr/core/src/java/org/apache/solr/cloud/api/collections/CollectionHandlingUtils.java:308-312
Draft: Should property.name be ignored for collection CREATE, or should it be rejected or honored?
1. Close as intended: Solr sets the core name itself (CreateCollectionCmd.java:380), so property.name does not name the core.
2. Keep open for a change: reject property.name with a clear error, or document that it is ignored.
3. Split or repro first: ask for a run that shows which core name a CREATE with property.name actually gets, then decide.

### SOLR-6785
Evidence: solr/core/src/java/org/apache/solr/response/CSVResponseWriter.java:303-306 (a polyfield with no escape gets a default backslash escape)
Draft: Should polyfield values keep the default backslash escape in CSV output, as the SOLR-3959 fix added, or should that fix be reverted?
1. Close as intended: the default escape for polyfields is set on purpose (CSVResponseWriter.java:303-306).
2. Keep open for a change: revert the SOLR-3959 default so polyfields follow the normal encapsulation rule.
3. Split or repro first: ask for a CSV example from a user with polyfield values, then decide.

### SOLR-6837
Evidence: solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java:162 (waits for replicas up to leaderVoteWait), 270-273 (logs potential data loss when a replica becomes leader after the timeout)
Draft: Is the lost write in this report a known limit of the leader-election design, or a defect to fix?
1. Close as intended: the code waits for replicas (ShardLeaderElectionContext.java:162) and logs potential data loss when that wait times out (270-273), so the loss is a known limit.
2. Keep open for a change: change leader election so a partitioned leader cannot accept writes that replicas never see.
3. Split or repro first: ask for a repro on current main with the leader partitioned, and split the consistency question from the leader-election question.

### SOLR-7069
Evidence: solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java:901-921 (only ACTIVE local replicas are tried; checkProps calls getCore, which throws for a failed core); solr/core/src/java/org/apache/solr/core/CoreContainer.java:2216-2224 (throws SolrCoreInitializationException for a recorded init failure); solr/core/src/java/org/apache/solr/cloud/ZkController.java:2183-2187, 2222-2230 (on startup DOWN is published only when publishState is set or the replica is on another node)
Draft: Should a query that reaches a local replica with a failed core return the init error (500), or fail over to another replica?
1. Close as intended: the 500 with the init error is deliberate (CoreContainer.java:2216-2224), so the request reports the failure instead of moving on.
2. Keep open for a change: catch the init-failure error in the local replica loop (HttpSolrCall.java:901-911) and try the next replica or a remote node.
3. Split or repro first: ask for a run on current main where the local replica is still ACTIVE in ZooKeeper and the query goes to the collection path, then decide.

### SOLR-7071
Evidence: solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java:901-921 (only ACTIVE local replicas are tried; checkProps calls getCore, which throws for a failed core); solr/core/src/java/org/apache/solr/core/CoreContainer.java:2216-2224 (throws SolrCoreInitializationException for a recorded init failure); solr/core/src/java/org/apache/solr/cloud/ZkController.java:2183-2187, 2222-2230 (startup DOWN publish rule; same as SOLR-7069)
Draft: Is this the same question as SOLR-7069: should a failed local core return the init error (500), or fail over to another replica?
1. Close as intended: the 500 with the init error is deliberate (CoreContainer.java:2216-2224), so the request reports the failure instead of moving on.
2. Keep open for a change: catch the init-failure error in the local replica loop (HttpSolrCall.java:901-911) and try the next replica or a remote node.
3. Split or repro first: merge this into SOLR-7069 as the same root, and ask for one run on current main that covers both reports.

### SOLR-7247
Evidence: solr/solrj/src/java/org/apache/solr/common/cloud/CompositeIdRouter.java:138; solr/core/src/java/org/apache/solr/update/SolrIndexSplitter.java:731, 820
Draft: With a routeField configured, sliceHash ignores the route field when no document is passed, and SolrIndexSplitter calls it that way. Should a missing route value throw an error, or should the request route to all shards?
1. Close as intended.
2. Keep open for a change.
3. Split or repro first.

### SOLR-7257
Evidence: solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java:626-633, 916-917
Draft: Real-time get removes copyField targets even when they are stored. Is that intended, or should stored copyField targets be returned?
1. Close as intended.
2. Keep open for a change.
3. Split or repro first.

### SOLR-7347
Evidence: solr/core/src/java/org/apache/solr/update/VersionInfo.java:127-129
Draft: Versions are built from System.currentTimeMillis(), so clock skew can order updates wrongly across leader changes. Is this an accepted design trade-off, or should a fix be scheduled?
1. Close as intended.
2. Keep open for a change.
3. Split or repro first.

### SOLR-7511
Evidence: solr/core/src/java/org/apache/solr/core/CoreContainer.java:1776-1777
Draft: Corrupt-index recovery is opt-in through CoreInitFailedAction, which defaults to none, so a core that fails with an EOF while opening a searcher does not self-heal by default. Should the default change, or should recovery stay opt-in?
1. Close as intended.
2. Keep open for a change.
3. Split or repro first.

### SOLR-7932
Evidence: solr/core/src/java/org/apache/solr/handler/IndexFetcher.java:611-613
Draft: IndexFetcher still compares commit wall-clock timestamps with the leader version, and a TODO at that line asks the same question. Should SolrCloud always replicate and rely on peer sync, or keep the timestamp check?
1. Close as intended.
2. Keep open for a change.
3. Split or repro first.

### SOLR-8543
Evidence: solr/server/solr/configsets/_default/conf/solrconfig.xml:846, 891-892
Draft: The default add-unknown-fields chain parses numeric-looking JSON strings into numbers, so a JSON string "50" becomes a long field. Is that intended, or should JSON string types be kept as strings?
1. Close as intended.
2. Keep open for a change.
3. Split or repro first.

### SOLR-8553
Evidence: solr/core/src/java/org/apache/solr/cloud/api/collections/CollApiCmds.java:277
Draft: The schema and config reload runs only on ACTIVE replicas, so a recovering replica keeps its old config. Should recovering replicas also be reloaded?
1. Close as intended.
2. Keep open for a change.
3. Split or repro first.

### SOLR-8634
Evidence: solr/core/src/java/org/apache/solr/search/ValueSourceParser.java:270, 341
Draft: The sum and div functions use float implementations, so 64-bit values lose precision. Is float the intended result type, or should these functions use double?
1. Close as intended.
2. Keep open for a change.
3. Split or repro first.

### SOLR-8862
Evidence: solr/core/src/java/org/apache/solr/cloud/ZkController.java:1080 (/live_nodes is ensured at startup); ZkController.java:1176 (a node's ephemeral entry is created under /live_nodes when it registers).
Draft: /live_nodes gets a node entry as soon as a server registers, before its core is ready, so smart clients can route to it too early. The maintainers need to decide whether to add a separate "ready" ZK node or treat the current behaviour as intended.
1. Close as intended: keep /live_nodes as it is and leave clients to handle a not-yet-ready server.
2. Keep open for a change: add a separate ZK node that marks a server as ready for requests.
3. Split or repro first: split the MiniSolrCloudCluster wait into its own ticket and reproduce it with a client.

### SOLR-8893
Evidence: solr/core/src/java/org/apache/solr/handler/component/TermVectorComponent.java:384 (tv.df uses the local reader.docFreq); solr/core/src/java/org/apache/solr/search/stats/ExactStatsCache.java:52-56 (exact stats are only for query terms).
Draft: With ExactStatsCache, tv.df reads the local index, while the class only computes exact statistics for query terms. The maintainers need to decide whether TermVectorComponent should use shard-wide statistics or whether this scope is intended.
1. Close as intended: keep TermVectorComponent on local document frequency, matching the documented ExactStatsCache scope.
2. Keep open for a change: make TermVectorComponent use the cached global docFreq.
3. Split or repro first: reproduce the difference on a two-shard setup and decide from the numbers.

### SOLR-9174
Evidence: solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java:436-447 (mm is turned off when explicit operators are found, otherwise the mm value is applied); ExtendedDismaxQParser.java:1010-1013 (q.op is applied before mm).
Draft: The 5.5 change to mm with q.op=AND looks like a deliberate change, and the last comment says it came from SOLR-8812. The maintainers need to decide whether this default is the intended behaviour.
1. Close as intended: keep the SOLR-8812 mm default as it is.
2. Keep open for a change: restore the pre-5.5 behaviour for q.op with an explicit mm.
3. Split or repro first: run the bi-gram example on 5.4 and on main and compare the results.

### SOLR-9223
Evidence: solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java:183-188 (a replica that cannot become leader waits and then rejoins the election); solr/core/src/java/org/apache/solr/cloud/ZkShardTerms.java:250-253 (FORCELEADER is the documented way to override this).
Draft: The current code refuses to elect a replica that lacks the latest updates, and FORCELEADER is the manual escape. The maintainers need to decide whether to keep consistency over automatic availability or add an automatic path.
1. Close as intended: keep the election guard and FORCELEADER as the recovery path.
2. Keep open for a change: add an automatic way to elect a stale leader after a timeout, with a config switch.
3. Split or repro first: split the automatic-recovery idea into its own ticket and reproduce the leaderless state.

### SOLR-9226
Evidence: solr/core/src/java/org/apache/solr/handler/admin/CollectionsHandler.java:703-704 (FORCELEADER is an admin API action); a grep of solr/core/src/java/org/apache/solr/cloud/*.java found no automatic caller.
Draft: FORCELEADER is only triggered by an admin request, and the last comment warns that automatic use can lose data. The maintainers need to decide whether to add an opt-in automatic trigger.
1. Close as intended: keep FORCELEADER as a manual admin action.
2. Keep open for a change: add an opt-in config setting that triggers FORCELEADER when a shard has no leader.
3. Split or repro first: split the automatic trigger into its own ticket and repro a leaderless shard.

### SOLR-9230
Evidence: solr/solrj/src/java/org/apache/solr/common/util/Utils.java:759 (javadoc: unknown types are written as a string using the class name and toString); Utils.java:795 (return o.getClass().getName() + ':' + o); solr/solrj/src/java/org/apache/solr/common/util/JavaBinCodec.java:276-278 (unknown types use the reflect writer fallback).
Draft: The binary writer writes unknown types such as BigDecimal as a class-name string, and this is documented in Utils. The maintainers need to decide whether to keep this, fail on unknown types, or handle BigDecimal and BigInteger as numbers.
1. Close as intended: keep the documented fallback as it is.
2. Keep open for a change: fail on unknown types, or write BigDecimal and BigInteger as numbers.
3. Split or repro first: split the BigDecimal case from the unknown-type case and repro the BigDecimal indexing failure.

### SOLR-9361
Evidence: solr/solrj/src/java/org/apache/solr/common/cloud/Replica.java:44-50 (ACTIVE may stay set after a node crash; the javadoc says to check live nodes).
Draft: The replica state documentation already says that a crashed node can keep an ACTIVE state in ZooKeeper. The maintainers need to decide whether to rename or change the states, or just improve the docs and UI.
1. Close as intended: keep the current states and the javadoc note.
2. Keep open for a change: change the state names, UI labels, or DELETEREPLICA behaviour for down replicas.
3. Split or repro first: split the UI wording from the DELETEREPLICA question and repro the delete case.

### SOLR-9369
Evidence: solr/core/src/java/org/apache/solr/handler/IndexFetcher.java:611-614 (TODO on comparing timestamps; the commit timestamp is compared to the leader version and the fetch returns ALREADY_IN_SYNC).
Draft: IndexFetcher still compares the commit timestamp with the leader's version in SolrCloud mode, and the code has a TODO about it. The maintainers need to decide whether to keep this check or replace it with the SOLR-9446 approach.
1. Close as intended: keep the commit timestamp check as a fast path.
2. Keep open for a change: skip the commit timestamp check in SolrCloud mode.
3. Split or repro first: repro a case where the timestamp check gives a wrong ALREADY_IN_SYNC result.

### SOLR-9514
Evidence: solr/core/src/java/org/apache/solr/handler/admin/CollectionsHandler.java:869-874 (BALANCESHARDUNIQUE still runs); solr/solrj/src/java/org/apache/solr/common/params/CollectionParams.java:113 (not marked deprecated).
Draft: BALANCESHARDUNIQUE is still a live command and is not marked deprecated. The maintainers need to decide whether to deprecate it now, and whether ADDREPLICAPROP and REMOVEREPLICAPROP cover all its uses.
1. Close as intended: keep BALANCESHARDUNIQUE as a supported command.
2. Keep open for a change: deprecate BALANCESHARDUNIQUE and fill any gaps in ADDREPLICAPROP and REMOVEREPLICAPROP.
3. Split or repro first: list what BALANCESHARDUNIQUE does that ADDREPLICAPROP does not, then decide.

### SOLR-9550
Evidence: solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/InnerJoinStream.java:29-30 (both streams must be sorted by the join fields); InnerJoinStream.java:63-73 (no order check on the input tuples).
Draft: InnerJoinStream documents that both inputs must be sorted, but it does not check the order, so a wrong sort can return wrong results without an error. The maintainers need to decide whether to add an order check or keep the documented precondition.
1. Close as intended: keep the documented precondition and no check.
2. Keep open for a change: add a check that throws when an input is out of order.
3. Split or repro first: reproduce the wrong result from the ticket's example and decide from that.

### SOLR-9841
Evidence: solr/core/src/java/org/apache/solr/update/processor/DocExpirationUpdateProcessorFactory.java:336-337 (javadoc says a soft commit follows the delete), :402-407 (soft commit with openSearcher=true is always sent)
Draft: The expiration run always sends a soft commit that opens a new searcher after it deletes expired docs. Should we:
1. Close as intended, since the commit is documented behaviour.
2. Keep open and add an option to turn that commit off.
3. Split the commit change out, or reproduce the overlapping-searcher warning first.

### SOLR-10112
Evidence: solr/core/src/java/org/apache/solr/update/processor/DistributedUpdateProcessor.java:337 (waitForDependentUpdates call), :560 (method)
Draft: The leader already orders dependent updates by version, but the ticket proposes a stronger barrier around each DBQ. Should we:
1. Close as won't fix for the current design.
2. Keep open for a change that blocks updates around each DBQ until replicas apply it.
3. Split the request, or reproduce the reordering first.

### SOLR-11495
Evidence: solr/core/src/java/org/apache/solr/search/QParserPlugin.java:50-99 (default map enables all parsers)
Draft: The default map enables nearly every query parser. Should we:
1. Close as intended, keep them enabled, and document how to disable parsers.
2. Keep open to shrink the default parser list.
3. Split per parser, or review each parser's security history first.

### SOLR-11927
Evidence: solr/core/src/java/org/apache/solr/handler/loader/XMLLoader.java:418-422 (any nested doc element is read as a child document)
Draft: XML has no way to send an empty child list, so an empty nested doc is indexed as a child. Should we:
1. Close as won't fix, as the last comment on the ticket suggests.
2. Keep open for an XML syntax that clears child documents.
3. Split the request, or reproduce the failure first.

## Repro needed (no comment until repro)

### SOLR-4733
Evidence: solr/core/src/java/org/apache/solr/update/DirectUpdateHandler2.java:969-976 (SolrCloud rejects rollback), 983-988 (standalone rollback resets the index writer and commit trackers only; no UpdateLog reset seen)
Draft: none (no comment)

### SOLR-5046
Evidence: solr/core/src/java/org/apache/solr/search/grouping/distributed/responseprocessor/TopGroupsShardResponseProcessor.java:137-139 (null shard group results are skipped), 177-178 (TopGroups.merge call); the exact source of the IllegalArgumentException is not settled by reading
Draft: none (no comment)

### SOLR-6004
Evidence: solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java:309-315 (OR, AND, NOT and TO are skipped, but the words on both sides stay in one list), 344 (shingles are built from that list)
Draft: none (no comment)

### SOLR-6429
Evidence: solr/core/src/java/org/apache/solr/core/CoreContainer.java:2050-2062 (unload entry points); reading did not settle whether unload stops a running tlog replay
Draft: none (no comment)

### SOLR-6813
Evidence: solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java:799-814 (distrib.singlePass sets onePassDistributedQuery), solr/core/src/java/org/apache/solr/handler/component/ExpandComponent.java:464-468 (expand follows onePassDistributedQuery); start and rows handling not settled by reading
Draft: none (no comment)

### SOLR-7035
Evidence: solr/core/src/java/org/apache/solr/core/SolrCore.java:775-795 (reload calls getSearcher(true,...) after the new core opened its searcher), 1134 and 1218-1231 (core open path opens the first searcher), 2636-2650 (firstSearcher listeners fire when no searcher is registered yet)
Draft: none (no comment)

### SOLR-7404
Evidence: solr/core/src/java/org/apache/solr/update/processor/DistributedUpdateProcessor.java:183
Draft: none (no comment)

### SOLR-7427
Evidence: solr/core/src/java/org/apache/solr/cloud/RecoveryStrategy.java:114-115, 710
Draft: none (no comment)

### SOLR-7758
Evidence: solr/core/src/java/org/apache/solr/handler/loader/JsonLoader.java:91-110
Draft: none (no comment)

### SOLR-8005
Evidence: solr/core/src/java/org/apache/solr/schema/StrField.java:153
Draft: none (no comment)

### SOLR-8030
Evidence: solr/core/src/java/org/apache/solr/update/UpdateLog.java:2096-2101, 2130
Draft: none (no comment)

### SOLR-8619
Evidence: solr/core/src/java/org/apache/solr/cloud/CloudDescriptor.java:41; solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java:404-405
Draft: none (no comment)

### SOLR-8629
Evidence: solr/core/src/java/org/apache/solr/cloud/SyncStrategy.java:154-156
Draft: none (no comment)

### SOLR-8641
Evidence: solr/core/src/java/org/apache/solr/handler/IndexFetcher.java:480-483, 640-644
Draft: none (no comment)

### SOLR-8733
Evidence: solr/core/src/java/org/apache/solr/update/processor/DistributedUpdateProcessor.java:251-258 (the versions response is built from the local cmd.getVersion()); the forwarded-to-leader path is not traced further.
Draft: none (no comment)

### SOLR-8741
Evidence: solr/core/src/java/org/apache/solr/search/facet/FacetFieldProcessor.java:416-424 (numBuckets is incremented before mincount filtering, and for buckets with partial counts); FacetFieldProcessor.java:448-450.
Draft: none (no comment)

### SOLR-8841
Evidence: solr/core/src/java/org/apache/solr/util/SolrPluginUtils.java:465 (setMinShouldMatch counts clauses of the built query); solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java:436-447.
Draft: none (no comment)

### SOLR-8879
Evidence: solr/core/src/java/org/apache/solr/search/Grouping.java:385-391 (when the cache limit is exceeded, the second pass runs without the post filter); compare Grouping.java:352-354 and 394-398 (the post filter is added in the other branches).
Draft: none (no comment)

### SOLR-8966
Evidence: solr/core/src/java/org/apache/solr/update/DocumentBuilder.java:317-318 (default values are applied only when the field is missing from the document); solr/core/src/java/org/apache/solr/update/processor/AtomicUpdateDocumentMerger.java:582-587 (the field default is only read for inc).
Draft: none (no comment)

### SOLR-9206
Evidence: solr/core/src/java/org/apache/solr/handler/component/TermVectorComponent.java:243-249 (listAndSet.docList is dereferenced with no null check); solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java:1624 (rb.setResults is only called on the non-grouped path shown).
Draft: none (no comment)

### SOLR-9622
Evidence: solr/core/src/java/org/apache/solr/response/JavaBinResponseWriter.java:103 and TabularResponseWriter.java:113 (DocList results are written with a null query); solr/core/src/java/org/apache/solr/handler/component/ExplainAugmenterFactory.java:102 (explain needs context.getQuery()).
Draft: none (no comment)

### SOLR-9664
Evidence: solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java:98-100 and 619-630 (the client only refreshes state after a stale-state error from the server, not when routing to a shard it does not know).
Draft: none (no comment)

### SOLR-9765
Evidence: not found (no schema rollback code in solr/core/src/java/org/apache/solr/schema, SchemaHandler.java, or ZkController.java).
Draft: none (no comment)

### SOLR-9925
Evidence: not found
Draft: none (no comment)

### SOLR-10110
Evidence: not found
Draft: none (no comment)

### SOLR-10332
Evidence: solr/core/src/java/org/apache/solr/handler/component/StatsValuesFactory.java:415-451 (double-based stats values)
Draft: none (no comment)

### SOLR-10363
Evidence: solr/core/src/java/org/apache/solr/search/ComplexPhraseQParserPlugin.java:149 (Lucene parser built with the schema query analyzer)
Draft: none (no comment)

### SOLR-10726
Evidence: solr/core/src/java/org/apache/solr/handler/IndexFetcher.java:598, :807 (searcher opened after fetch), :1064-1081 (openNewSearcherAndUpdateCommitPoint)
Draft: none (no comment)

### SOLR-10751
Evidence: solr/core/src/java/org/apache/solr/handler/IndexFetcher.java:579-584 (version 0 handling)
Draft: none (no comment)

### SOLR-10952
Evidence: solr/core/src/java/org/apache/solr/search/SolrIndexSearcher.java:1567-1580 (query result cache key built without the stats source)
Draft: none (no comment)

### SOLR-11083
Evidence: solr/core/src/java/org/apache/solr/cloud/api/collections/MoveReplicaCmd.java:168-189 (old replica deleted first), :379 (new replica added later in the same command)
Draft: none (no comment)

### SOLR-11353
Evidence: solr/core/src/java/org/apache/solr/search/join/BlockJoinParentQParserPlugin.java:29
Draft: none (no comment)

### SOLR-11577
Evidence: solr/core/src/java/org/apache/solr/handler/component/HttpShardHandler.java:255 (throws when no server hosts the shard; no retry in this file) ; solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java:619-621 (stale-state retry exists only in the client)
Draft: none (no comment)

### SOLR-11712
Evidence: solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/CloudSolrStream.java:407-440 (constructStreams no longer indexes get(0); empty results raise IOException)
Draft: none (no comment)

## No comment

### SOLR-7521
Evidence: solr/core/src/java/org/apache/solr/schema/FieldType.java:540
Draft: none (no comment)

### SOLR-8067
Evidence: not found
Draft: none (no comment)

### SOLR-8311
Evidence: solr/core/src/java/org/apache/solr/core/SolrCore.java:365-374
Draft: none (no comment)

### SOLR-8368
Evidence: not found
Draft: none (no comment)

### SOLR-8709
Evidence: solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/TopicStream.java:76
Draft: none (no comment)

### SOLR-9477
Evidence: solr/core/src/java/org/apache/solr/update/processor/AddSchemaFieldsUpdateProcessorFactory.java:556 (child documents are handled here); UUIDUpdateProcessorFactory has no child-document handling (grep found none).
Draft: none (no comment)

### SOLR-9648
Evidence: not found for SolrMergePolicy; solr/core/src/java/org/apache/solr/index/MergePolicyFactory.java:24 (the existing merge policy entry point).
Draft: none (no comment)

### SOLR-9978
Evidence: solr/core/src/java/org/apache/solr/search/CollapsingQParserPlugin.java:388 (initialSize hint), :1064 (hppc map sized by initialSize)
Draft: none (no comment)

### SOLR-11583
Evidence: not found
Draft: none (no comment)
