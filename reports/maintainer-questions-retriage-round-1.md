# Maintainer-question re-triage, round 1 (vm5, 2026-10-26)

The 40 maintainer-question drafts were re-triaged with the decision ladder (`material/decision-ladder.md`). Each ticket stops at the first rung that settles it. Source reads are against apache/main `b73f03976fa`, read-only. Nothing was posted or committed to any PR.

Limit: the local clone is shallow (70 commits, grafted at `e34067ae647`). `git log -S` cannot reach the commits that introduced most behaviours, so citations are to the tree state at `b73f03976fa`, not to introducing commits. Where an agent cited a SHA outside the clone (SOLR-3959 `61851c09665`, SOLR-4370 `2274676c0d0`, SOLR-4208 `76f5a8bada6`), that SHA is not verified here.

## Summary

| Decision | Count | Tickets |
|---|---|---|
| PR choice (recorded in PR body) | 21 | 3008, 4023, 6719, 6785, 7069, 7071, 7247, 7257, 7347, 7932, 8553, 8862, 8893, 9174, 9230, 9361, 9369, 9514, 9550, 9841, 11495 |
| Close (closing comment, owner approval needed) | 9 | 2855, 4100, 4983, 4996, 5095, 6837, 8543, 9223, 11927 |
| Question kept open (rung 5) | 8 | 5235, 5962, 6364, 6714, 7511, 8634, 9226, 10112 |
| Question pending a probe (rung 2) | 2 | 3744, 4674 |

Of the 21 PR choices, 9 are docs-only (3008, 4023, 6719, 6785, 7257, 8893, 9361, 9514, 11495), 1 is a comment-only change (9369), and 7071 folds into 7069. That leaves about 10 that change code.

## PR choices

- **SOLR-3008** (rung 4): keep the pf design, where explicit quoted phrases are not shingled into pf, and document it. A code change would alter scores for all edismax users with quoted phrases. `ExtendedDismaxQParser.java:306-313`.
- **SOLR-4023** (rung 4): keep classic Lucene precedence and document it with a parentheses recommendation. A grammar change would break every classic query. `standard-query-parser.adoc:280-301`.
- **SOLR-6719** (rung 4): keep current behaviour and document that `property.name` is ignored for collection CREATE. Honouring it would collide core names; rejecting it would break callers. `CreateCollectionCmd.java:380, 392`; `collection-management.adoc:246-261`.
- **SOLR-6785** (rung 4): keep the SOLR-3959 backslash escape default for polyfields and document it. Reverting would change CSV output for existing users. `CSVResponseWriter.java:303-306`; `TestCSVResponseWriter.java:149-157`.
- **SOLR-7069** and **SOLR-7071** (rung 4, one PR): on the collection path, skip a local replica whose core failed to init and fall through to the remote replica. Keep the 500 for direct requests. `HttpSolrCall.java:901-911`; `CoreContainer.java:2216-2224`; `ZkController.java:2185, 2222-2226`.
- **SOLR-7247** (rung 4): keep `routeField`. Route RTG and delete-by-id without `_route_` to all shards, because a silent miss on read or delete is a wrong result. `CompositeIdRouter.java:138-168`; `RealTimeGetComponent.java:1064-1066`; `DistributedZkUpdateProcessor.java:720-726`.
- **SOLR-7257** (rung 3): RTG keeps dropping copyField targets, for consistency with the update log. Document it on the RTG page. `RealTimeGetComponent.java:626-633, 916-917`.
- **SOLR-7347** (rung 4): keep the time-based clock and add the Lamport receive rule, so a new leader never issues a version below one it has seen. `VersionInfo.java:127-135`. The ticket says "We should still fix this".
- **SOLR-7932** (rung 4): in SolrCloud, replace the wall-clock equality skip and the full-copy timestamp check with generation checks. `IndexFetcher.java:613, 641`.
- **SOLR-8553** (rung 4): send RELOAD to live replicas in both ACTIVE and RECOVERING states. `CollApiCmds.java:277`; `CollectionHandlingUtils.java:408-427`.
- **SOLR-8862** (rung 2, after probe): leave `/live_nodes` unchanged and make the MiniSolrCloudCluster harness wait for a core-level response before `createCollection`. The probe is not yet run.
- **SOLR-8893** (rung 4): keep `tv.df` on the local reader and correct the ref guide, which says ExactStatsCache covers query terms only. `TermVectorComponent.java:384`.
- **SOLR-9174** (rung 2, after probe): keep `q.op` for top-level clauses. With an explicit `mm`, make the analyzer's multi-token sub-query use SHOULD. `ExtendedDismaxQParser.java:445, 1013`. The probe is not yet run.
- **SOLR-9230** (rung 4): write BigDecimal and BigInteger in JavaBinCodec as their `toString()` text, and leave the reflect fallback unchanged. `JavaBinCodec.java:280, 1256`; `Utils.java:795`.
- **SOLR-9361** (rung 4): keep state names and DELETEREPLICA `onlyIfDown`. Fix the docs only. `Replica.java:52-61`; `DeleteReplicaCmd.java:274-279`; `replica-management.adoc:585`.
- **SOLR-9369** (rung 4): keep the `commitTimeMSec` equality check and replace the TODO with the reason it is safe. It compares a value copied with the leader's segments file. `IndexFetcher.java:611-617`.
- **SOLR-9514** (rung 3): deprecate BALANCESHARDUNIQUE in the ref guide and javadoc only. No removal. Committer noble.paul already decided this in 2016. `CollectionParams.java:113`; `cluster-node-management.adoc:546`.
- **SOLR-9550** (rung 4): make InnerJoinStream throw when a side's tuples are out of order. This keeps the documented sorted-input precondition, and silent wrong results now throw. `InnerJoinStream.java:63-73`.
- **SOLR-9841** (rung 4): add an opt-in boolean, defaulting to true, that skips the soft commit after expiration delete. `DocExpirationUpdateProcessorFactory.java:402-406`.
- **SOLR-11495** (rung 3, probe for final text): docs-only section on which query parsers load by default and how to shadow one from solrconfig. The shadowing steps need a probe before the text is final. `QParserPlugin.java:47-99`.

## Close

- **SOLR-2855** (rung 3): range facets have no bucket cap by design. The reporter agreed in 2011. `RangeFacetRequest.java:114-146`.
- **SOLR-4100** (rung 1): commitWithin is soft by default by design, and a hard-commit config knob exists. `SolrConfig.java:833-834`; `commits-transaction-logs.adoc:190-204`.
- **SOLR-4983** (rung 3): cross-collection join takes a collection name, and core names are internal. `CrossCollectionJoinQParser.java:~106`; `Assign.java:182-190`. Mention the single-shard join limit in `join-query-parser.adoc:143-144`.
- **SOLR-4996** (rung 4): a collection request goes to one replica by design. Per-replica reads belong on a core-level client. `CloudSolrClient.java:898-960`.
- **SOLR-5095** (rung 1): the JMX map the ticket names is not in main, so its lost-MBean bug has no path. The closing comment must say the flat-key collision remains and was not reproduced. `SolrCore.java:227, 3458`.
- **SOLR-6837** (rung 3): a known limit of leader-only durability. The code waits for replicas and warns about data loss. Note that min_rf is now report-only. `ShardLeaderElectionContext.java:162, 273`; `major-changes-in-solr-8.adoc:329`.
- **SOLR-8543** (rung 3): value-class guessing is documented. A JSON string "50" becomes a long by value. Add a doc sentence. `schemaless-mode.adoc:25`.
- **SOLR-9223** (rung 1): fixed by SOLR-7065 (Closed, Fixed 7.3/8.0). The remaining case is the documented FORCELEADER path. `ShardLeaderElectionContext.java:185-188, 340-360`; `shard-management.adoc:551`.
- **SOLR-11927** (rung 4): won't fix. XML has no empty child-list syntax, and JSON already expresses an empty list with `_childDocuments_: []`. `XMLLoader.java:418-422`; `JsonLoader.java:73`.

## Question kept open (rung 5)

- **SOLR-5235**: should replayed commits go through the custom update processor chain? That changes a public plugin contract and re-runs custom side effects on restart. `UpdateLog.java:2401-2410`.
- **SOLR-5962**: should a replica publish a new REPLAYING state during tlog replay? That changes the public `Replica.State` enum and `state.json`. `Replica.java:52-75`.
- **SOLR-6364**: should Solr add an opt-in response option that writes 64-bit values as JSON strings, or close this as a client-side precision limit? `JSONWriter.java:191-193`.
- **SOLR-6714**: should a RELOAD with failed replicas return a non-200 status now, or wait for the status design in SOLR-6595? `ReloadCollectionAPI.java:75-84`.
- **SOLR-7511**: should CoreInitFailedAction default change from `none` to `fromleader`? That changes startup behaviour for every SolrCloud node with a corrupt index. `CoreContainer.java:1775-1777`.
- **SOLR-8634**: should sum/add and div return double, changing the output type of public functions, or keep float and document the precision limit? `ValueSourceParser.java:270, 341`.
- **SOLR-9226**: should Solr add an opt-in config setting that runs FORCELEADER when a shard has no leader? That is a new automatic, data-affecting config surface. `CollectionsHandler.java:703-706`.
- **SOLR-10112**: should a blocking barrier hold leader updates around every DBQ, trading write throughput for ordering, or should this close because reordered DBQs are already handled? `DirectUpdateHandler2.java:472-555`.

## Question pending probe (rung 2)

- **SOLR-3744**: is the failure in Lucene 10.4.0 BooleanQuery handling of a nested MUST_NOT-only group, or in Solr's `getBooleanQuery` path? Probe: `debugQuery` on `test AND (-fox)` and `(-fox)` on main, comparing parsed queries with an added `*:*` MUST. The Lucene source was not read.
- **SOLR-4674**: does the commit-point fallback make repeaters work without replicateAfter at startup, or does the core-reload path leave a stale commit point? Probe: repeater with replicateAfter commit only, core reload, slave poll, compare indexversion with the leader.

## Next

- The mailing list reply's "Maintainer question" category now covers 8 tickets (rung 5), plus 2 more once their probes run.
- 21 tickets become PR-body choices. A PR is not opened and nothing is posted without owner approval.
- 9 closes need an owner-approved closing comment.
