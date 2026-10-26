# Comment drafts by code-read bucket, round 4 (vm5, 2026-10-26)

Nothing is posted. No reproduction or Gradle runs were done. Every claim is from code reading against apache/main `b73f03976fa` (`reports/repro-viability-round-1.md` and `round-2.md`). Each draft needs a live JIRA check at fill time and owner approval before posting. Add the AI disclosure footer used in rounds 1 to 3 when posting.

Buckets, per owner instruction:
- **A. Fix possible from code reading.** Ask whether a fix is still needed. 28 drafts.
- **B. Fix already in place, or behaviour not on main.** Closure candidate. Ask if closure is OK. 6 drafts.
- **C. Not settled by reading.** Hold, no comment. Needs more or deeper work. 8 tickets.

Totals: 42 tickets with verdicts (28 + 6 + 8).

## A. Fix possible: ask if still needed (28)

### SOLR-3407
Reading apache/main, `fl=24_7` is parsed as a function token, so a constant is returned under the key `24` and the field name is split (`SolrReturnFields.java:394-411`). Is this still a problem on a current release? If so, a fix in `SolrReturnFields` would keep the whole token as the field name.
Check before posting: JIRA open, no fix version.

### SOLR-3676
The indexed id comes from the analyzed uniqueKey (`AddUpdateCommand.java:112-116`), so a lowercase analyzer on `id` makes `TEST` and `test` the same indexed id. From reading I could not confirm why the update lookup misses the existing document. Is this still reproducible on current main?
Check before posting: JIRA open.

### SOLR-3945
`setFacet(false)` removes the facet parameters rather than sending `facet=false` (`solrj/.../SolrQuery.java:370-378`), so a handler whose defaults set `facet=true` still computes facets. Is a fix still wanted, meaning send `facet=false`? Or is removing the parameter the intended meaning?

### SOLR-4021
`JavaBinCodec` falls back to the reflect writer (`JavaBinCodec.java:280`), and `Utils` returns the class name plus `:` plus the value for classes with no annotated fields (`Utils.java:795`). So a `BigDecimal` round-trips as the string `java.math.BigDecimal:1848.66`. Is this still an issue? If so, the fix is a writer for `BigDecimal` or a clear error.

### SOLR-4130
`addPhraseFieldQueries` skips clauses that have a field or are phrases (`ExtendedDismaxQParser.java:308`). For `+sw(a b) +ti:(c d)` with `pf` set, the phrase boost is built across terms in different fields (`"b d"`). Is this still an issue, and is that the parse you expect?

### SOLR-4486
Reading the distributed update path on main, a batch of add, add, delete for an id routed to a non-owning shard depends on the order of forwarding (`DistributedZkUpdateProcessor.java:346`). I have not confirmed the race by running it. Is it still reproducible on a current release?

### SOLR-4733
Rollback resets the index writer and commit trackers (`DirectUpdateHandler2.java:969-1003`) but not the UpdateLog. A real-time get after rollback can therefore return the rolled-back document. Is this still the case?

### SOLR-6004
`OR`, `AND`, `NOT` and `TO` are dropped from the clause list (`ExtendedDismaxQParser.java:313`), and shingles are built from that filtered list (`:636-641`), so a shingle can span an `OR`, for example `cup mug`. Is that still wanted? If not, a fix would stop shingles across operators.

### SOLR-6429
`closeAndWait` waits until the core closes (`SolrCore.java:1543-1566`). The replay loop checks `cancelApplyBufferUpdate` (`UpdateLog.java:2257`), which is never set to true on main. So an unload during replay waits rather than stopping replay. Is this still an issue?

### SOLR-7035
On reload, `SolrCore` calls `getSearcher` a second time (`SolrCore.java:786-787`) before the first searcher's registration task runs (`:2676-2700`). `firstSearcher` listeners can then fire twice. Is this still reproducible?

### SOLR-8005
The tests carry a `def(...,0)` wrapper with a comment that it stays until SOLR-8005 is resolved (`TestMinMaxOnMultiValuedField.java:704, 745`). On main, `StrField` uses `SortedSetFieldSource` for the single value (`StrField.java:146-153`), which sorts a document with no value first. Is this still the behaviour to fix?

### SOLR-8030
Replay runs with `BASE_REPLAY_PARAMS` and the default chain (`UpdateLog.java:2096-2102, 2232`), and the transaction log stores neither the chain name nor the request parameters. A processor after DUP that depends on a request parameter would lose its effect on replay. Is that still the case?

### SOLR-8619
The new replica is registered at term 0 (`ShardTerms.java:189-196`), and a replica with an equal term wins election at once (`ShardLeaderElectionContext.java:185-193`). After ADDREPLICA and a restart, documents on the old replica could be lost. Is this still an issue, or was it fixed under another ticket?
Check before posting: the ticket has fixVersion 5.5 but is open. Confirm whether the 5.5 fix covered this path before asking.

### SOLR-8629
`PeerSync` tolerates connect errors and 503 or 404 on GET_VERSIONS, but treats other errors as a failed sync (`PeerSync.java:370-431`). The candidate then rejoins the election (`ShardLeaderElectionContext.java:219, 244-245`). So one replica returning 500 can keep a healthy candidate from becoming leader. Is this still the case?

### SOLR-8733
When a node forwards an add to the leader, `versionAdd` is skipped (`DistributedUpdateProcessor.java:239-240`), so the adds response reports version 0 for forwarded documents. Is that still the case on main? Should the leader's version be copied back?

### SOLR-8841
When an analyzer emits stacked sub-tokens at one position, the eDismax query becomes a `SynonymQuery` and `mm` is not applied (`ExtendedDismaxQParser.java:1443-1449`). So `mm=100%` can still match a document with only one sub-word. Is this still reproduced with your analyzer?

### SOLR-8879
When the grouping cache overflows, the second pass runs without the PostFilter wrapper (`Grouping.java:386-391`). The non-overflow branch wraps it (`:394-398`). A non-cached post filter can then be ignored on the overflow path. Is this still reproduced?

### SOLR-9206
With `group=true&tv=true` and no debug, `docList` is not built (`QueryComponent.java:1820`), and `TermVectorComponent` iterates it (`:243-250`), which would throw an NPE. Is this still a failure on current main?

### SOLR-9622
`[explain]` is a no-op when the context query is null (`ExplainAugmenterFactory.java:102`). Grouped output passes a null query (`TextResponseWriter.java:197`), so explain is omitted. `group.format=simple` works. Is this still the behaviour? SOLR-14693 is marked as a duplicate and is resolved, so please check it covers this case.

### SOLR-9765
If one replica fails to reload after a schema change, the error is returned, but the new field stays in ZooKeeper and on the coordinator (`SchemaManager.java:149-173, 186`; `ManagedIndexSchema.java:293-297`). There is no rollback. Is this still the case, or was it covered by SOLR-9751, which is resolved?

### SOLR-10110
On a soft commit, `getSearcher` runs before the soft-commit callbacks (`DirectUpdateHandler2.java:893-896`). On a closed core, `openNewSearcher` throws `CoreIsClosedException` (`SolrCore.java:2291-2292`), which is only logged (`CommitTracker.java:282`), so the callbacks are skipped. This covers soft-commit callbacks only. Is fixing it still wanted?

### SOLR-10332
The stats value for a currency function goes through `objectVal`, which `CurrencyFieldType` does not override for the converted function (`CurrencyFieldType.java:459-500`). The result comes back as a float, for example `295.95001220703125` for 295.95. Is this still shown with `stats.field={!func}currency(...)`?
Check before posting: the Lucene default `objectVal` behaviour is from memory, not from a checked source.

### SOLR-10363
For a prefix term, `ComplexPhraseQParserPlugin.getPrefixQuery` calls the Lucene version and skips Solr's multi-term analysis (`ComplexPhraseQParserPlugin.java:172-175`). Uppercase Turkish input then does not match lowercase indexed terms. Is this still happening on current main?

### SOLR-10726
After a replication in SolrCloud, the replica closes and reopens its searcher (`IndexFetcher.java:679, 807`). `firstSearcher` listeners fire whenever no searcher is registered (`SolrCore.java:2640-2645`), so they can run more than once per replica. Is that still the behaviour you see?

### SOLR-10751
The in-sync check compares commit timestamps only, with no generation check (`IndexFetcher.java:613-618`). Two commits with equal timestamps and different generations return `ALREADY_IN_SYNC`. Is this still an issue, and should generation be compared too?

### SOLR-10952
The query result cache key has no stats-source field (`SolrIndexSearcher.java:1572-1578`), so cached local scores can be reused across shards with the global stats cache. The test uses `{!cache=false}` to avoid this (`TestDefaultStatsCache.java:79`). Is this still an issue with the stats cache enabled?

### SOLR-11083
MoveReplica deletes the replica (`MoveReplicaCmd.java:189-190`) before adding the new one (`:252`), with no persisted state in between. An interruption between the two leaves the replica count reduced. Is this still a concern for shared-filesystem moves?

### SOLR-11577
Shard selection reads the cached cluster state (`CloudReplicaSource.java:63`). `HttpShardHandler` throws a 503 when no active server is listed (`:538-539`), with no retry in the handler. SolrJ's `CloudSolrClient` retries (`CloudSolrClient.java:98-100`), but direct HTTP callers do not. Is a query right after a node stop still failing with 503, with `shards.tolerant` unset?

## B. Fix in place or not on main: closure candidate (6)

### SOLR-4277
On main, `correctlySpelled` is true when there is no `maxResultsForSuggest` and hits are above zero (`SpellCheckComponent.java:214-215`). Suggestions for in-index terms under `alternativeTermCount` are documented (`spell-checking.adoc:382-386`). The reported `correctlySpelled=false` for a one-hit query does not occur on main. Is closure OK, or do you have a current-version query that still shows it?

### SOLR-4669
On main, `IndexFetcher` copies the configuration files before installing the new index (`IndexFetcher.java:745, 747-773`) and reloads the core after (`:790`). The order in the ticket, new index with old config, does not occur. Is closure OK, or is there still a failing order?

### SOLR-5046
The default `group.sort` of RELEVANCE uses the non-sort `TopDocs` merge (`TopGroupsShardResponseProcessor.java:213-218`), so the sort-merge exception in the report is not reached on main. Is closure OK?

### SOLR-9664
A `_route_` query from a stale client cache is caught by the server's `stateVer` check (`HttpSolrCall.java:846-866`), and the client re-fetches and retries (`CloudSolrClient.java:830-832`). I did not find a silent miss on main. Is closure OK?

### SOLR-11353
Without `childFilter`, `[child]` returns all block descendants of each root, which is the documented default (`document-transformers.adoc:146`, `ChildDocTransformer.java:224`). The `fq` only changes which roots match. Is closure OK, or do the blocks in your index not match this?

### SOLR-11712
The `IndexOutOfBounds` from `TupleStream.getShards` cannot occur on main. A down shard is skipped (`TupleStream.java:189-197`). I could not find the commit that fixed it. If the concern is partial results, that is a separate question. Is closure OK?

## C. Not settled by reading: hold, no comment (8)

- **SOLR-6813** (undetermined): reading does not show where `start` and `rows` break the one-pass path (`QueryComponent.java:799-815`). Needs a run of the singlePass request with `start=1&rows=2`.
- **SOLR-7427** (undetermined): the race needs an in-flight leader update, and main has no hook to hold one (`RecoveryStrategy.java:114-115, 708-710`). Needs a deeper test harness.
- **SOLR-9925** (undetermined): loss depends on the order of DBQ against block adds across concurrent requests. Reading cannot settle it.
- **SOLR-7404** (likely not, but): the configset in the report is not in `server/solr/configsets`, and `_default` declares `_version_`. Needs the reporter's configset before any closure.
- **SOLR-7758** (likely not, but): the array-vs-single asymmetry is gated by `isUsableForChildDocs` (`JsonLoader.java:91-111, 652-681`). It is absent on `_default`, but the report may use another schema.
- **SOLR-8641** (likely not, but): the forced-delete path needs ZooKeeper-aware mode (`IndexFetcher.java:478-482, 871-874`). Needs the reporter's cluster mode.
- **SOLR-8741** (likely not, but): with default `mincount=1` the counts match. A 35% gap needs the reporter's full request, including `mincount`, `prefix` and domain.
- **SOLR-8966** (likely not, but): the DUP-before-Timestamp chain in the report is the only path that skips the update. Needs the reporter's chain.
