# Repro viability round 2: 38 repro-needed tickets (vm5, 2026-10-26)

Pipeline steps 1 and 2 only (repro needed > repro is viable). Code read on apache/main `b73f03976fa` through `git show`, with JIRA REST for status. Nothing was built, run or implemented. Round 1 covered SOLR-3676, 3945, 4130, 4486 (all viable), so they are not repeated here.

Verdict meaning: VIABLE means a test on main would fail for the reported reason. LIKELY-NOT means main does not produce the reported behaviour, or the report is already handled. UNDETERMINED means reading cannot settle it, and the probe is named.

Counts: 24 viable, 11 likely not, 3 undetermined.

## VIABLE (24)

- SOLR-3407 (fl with digit-leading names): `SolrReturnFields.java:394-411`. Test: index `24_7`, request `fl=24_7`, assert no extra `24` entry.
- SOLR-4021 (JavaBinCodec unrecognised class): `JavaBinCodec.java:280`, `Utils.java:795`. Test in `TestJavaBinCodec`: round-trip a `BigDecimal("1848.66")`; main gives `java.math.BigDecimal:1848.66`.
- SOLR-4733 (rollback leaves UpdateLog): `DirectUpdateHandler2.java:969-1003`. Test: add, rollback, real-time get; main still serves the rolled-back doc.
- SOLR-6004 (eDismax drops OR, shingles span it): `ExtendedDismaxQParser.java:313, 636-641`. Test: `coffee AND (cup OR mug^0.5)` with pf2; assert `coffee mug` shingle; main has `cup mug`.
- SOLR-6429 (unload does not stop tlog replay): `SolrCore.java:1543-1566`, `UpdateLog.java:2257`. Test: block replay with `testing_logReplayHook`, unload on a thread, assert prompt return.
- SOLR-7035 (firstSearcher fires twice on reload): `SolrCore.java:786-787, 2640-2651, 2676-2700`. Test: count firstSearcher calls across a reload; main fires twice.
- SOLR-8005 (min/max on multivalued string, missing docs): `StrField.java:146-153`; the test comments at `TestMinMaxOnMultiValuedField.java:704, 745` say "until SOLR-8005 is resolved". Test: drop the `def(...,0)` wrapper and assert the missing doc sorts as 0.
- SOLR-8030 (replay ignores non-default chain): `UpdateLog.java:2096-2102, 2232`. Test in `TestRecovery`: processor after DUP keyed on a request param; replay drops its effect.
- SOLR-8619 (replica added at term 0 wins election): `ShardTerms.java:189-196`, `ShardLeaderElectionContext.java:185-193`. Integration test, RF=1, add replica, restart old node, check old docs. Caution: fixVersion 5.5 is set but the path is still present on main, so check the fix before any comment.
- SOLR-8629 (GET_VERSIONS 500 blocks leader): `PeerSync.java:370-431`, `SyncStrategy.java:228-232`. Test: inject HTTP 500 on one replica's getVersions; candidate should win, main rejoins.
- SOLR-8733 (adds version 0 when forwarded): `DistributedUpdateProcessor.java:239-240, 251-258`, `DistributedZkUpdateProcessor.java:792`. Test: 2 shards, `versions=true`, send to a node that forwards; assert non-zero versions.
- SOLR-8841 (mm with stacked sub-tokens): `ExtendedDismaxQParser.java:1443-1449`. Test: compound filter emitting stacked tokens, `mm=100%`, one sub-word doc; main returns 1 hit, expected 0. Positional tokens would pass, so the repro needs stacked tokens.
- SOLR-8879 (grouping ignores PostFilter in cache-overflow path): `Grouping.java:386-391` vs `:394-398`. Test: small `group.cache.percent` plus non-cached PostFilter that excludes all docs; main returns unfiltered count.
- SOLR-9206 (TermVector NPE with grouping): `TermVectorComponent.java:243-250`, `QueryComponent.java:1820`. Test: `group=true&tv=true&tv.fl=...` with no debug; `docList` is null, so NPE.
- SOLR-9622 (`[explain]` missing in grouped output): `ExplainAugmenterFactory.java:102`, `TextResponseWriter.java:197`. Test: `group=true&fl=id,[explain]`; main omits explain. `group.format=simple` works. Related: SOLR-14693 is resolved and marked as a duplicate.
- SOLR-9765 (schema API leaves field after replica failure): `SchemaManager.java:149-173, 186`, `ManagedIndexSchema.java:293-297`. Test: one replica fails reload; main leaves the field in ZK and on the coordinator. Related: SOLR-9751 is resolved.
- SOLR-10110 (soft-commit callbacks skipped on closed core): `DirectUpdateHandler2.java:893-896`, `SolrCore.java:2291-2292`, `CommitTracker.java:282`. Covers soft-commit callbacks only; hard-commit hooks run first.
- SOLR-10332 (currency stats returns float error): `StatsValuesFactory.java:467-468, 493`, `CurrencyFieldType.java:459-500`. Test: `stats.field={!func}currency(price,EUR)`, 295.95 EUR; main returns 295.95001220703125. Caveat: the Lucene default `objectVal` behaviour is from memory, since Lucene source is not in this repo.
- SOLR-10363 (complexphrase prefix skips multi-term analysis): `ComplexPhraseQParserPlugin.java:172-175`, `SolrQueryParserBase.java:1257-1262`. Test on a Turkish-lowercase field: `"60 YAŞIND*"` vs `"60 yaşınd*"`; main gives 0 for uppercase. Must use a prefix term.
- SOLR-10726 (firstSearcher fires on replica add): `SolrCore.java:1231, 2640-2645`, `IndexFetcher.java:679, 807`. SolrCloud test with a counting listener; main fires at least twice.
- SOLR-10751 (equal timestamp returns ALREADY_IN_SYNC): `IndexFetcher.java:613-618`. Test needs a seam to force equal commit timestamps with different generations.
- SOLR-10952 (cached local scores with stats cache): `SolrIndexSearcher.java:1572-1578, 1739`. Remove `{!cache=false}` from `TestDefaultStatsCache.java:79` and run `TestExactSharedStatsCache`; scores should diverge. Depends on QRC autowarm.
- SOLR-11083 (MoveReplica loses replica between delete and add): `MoveReplicaCmd.java:189-190, 252`. Needs a new test-only hook that aborts between delete and add; the hook does not exist yet.
- SOLR-11577 (503 on stale cached state): `CloudReplicaSource.java:63`, `HttpShardHandler.java:538-539`. MiniSolrCloud test: stop a node and query before cached state updates. The window is timing-dependent, so retries may be needed.

## LIKELY NOT (11)

- SOLR-4277 (correctlySpelled false for in-index term): `SpellCheckComponent.java:187-188, 214-215`; the documented `alternativeTermCount` behaviour. On main a one-hit query is correctlySpelled=true.
- SOLR-4669 (index loaded before new config): `IndexFetcher.java:745, 747-773, 790`. Main copies configs before installing the index and reloads after, so the reported order does not occur.
- SOLR-5046 (sort-merge IllegalArgumentException): `TopGroupsShardResponseProcessor.java:213-218`. The default RELEVANCE sort uses the non-sort merge, so the 4.3 trace path is not reached.
- SOLR-7404 (configset without `_version_`): the reported configset is not on main; `_default` declares `_version_` (`managed-schema.xml:116`). Confirm with the reporter's configset before closing.
- SOLR-7758 (array vs single child docs differ between /update and /update/json/docs): `JsonLoader.java:91-111, 652-681`, `IndexSchema.java:2194-2200`. On `_default`, both paths build child docs, so the asymmetry is absent.
- SOLR-8641 (forced full fetch on standalone master): `IndexFetcher.java:478-482, 579-584, 871-874`, `ReplicationHandler.java:770-774`. The forced-delete path needs ZooKeeper-aware mode; standalone with replication disabled passes.
- SOLR-8741 (numBuckets differs from returned count): `FacetFieldProcessor.java:416-418, 544-558`, `FacetField.java:44`. With default mincount=1 the counts match. A 35% gap cannot be explained by HLL error; ask for the reporter's full request.
- SOLR-8966 (DUP skips default and atomic add/remove): `UpdateRequestProcessorChain.java:151-156`, `AtomicUpdateDocumentMerger.java:194-196`. Schema `default="NOW"` is not applied at update time, and the Timestamp processor sets the field on every atomic op.
- SOLR-9664 (routing from stale cache misses shard): `CloudSolrClient.java:675, 830-832`, `HttpSolrCall.java:846-866`. The server's stateVer check catches the stale version and the client retries.
- SOLR-11353 (`[child]` returns all descendants): documented default, `ChildDocTransformer.java:224`, `document-transformers.adoc:146`. Not a bug unless the blocks are non-contiguous.
- SOLR-11712 (IndexOutOfBounds in getShards): `TupleStream.java:189-197`. A down shard is skipped silently; no `get(0)` on main.

## UNDETERMINED (3)

- SOLR-6813 (distrib.singlePass with start/rows): one-pass flag at `QueryComponent.java:799-815`, expand at `ExpandComponent.java:233`. Probe: add `start=1&rows=2` to the singlePass block at `DistributedExpandComponentTest.java:387-400` and compare with the non-singlePass result.
- SOLR-7427 (replica recovery race with in-flight leader update): `RecoveryStrategy.java:114-115, 708-710`. Probe: in MiniSolrCloud, hold one leader update between forward and commit, then check the replica.
- SOLR-9925 (DBQ vs block add ordering across replicas): `DistributedUpdateProcessor.java:939-949`. Probe: 2 replicas, concurrent DBQ(`_root_`) and parent+child block adds, compare child counts per replica over many iterations.

## Notes

- Every verdict is from reading only. Nothing has been run. Each VIABLE still needs a failing run at a fixed seed before it counts as a repro.
- The status checks use JIRA as of today. Three VIABLE tickets carry a fixVersion or a related resolved ticket (SOLR-8619, 9622, 9765), so check the fix before drafting.
- No comments are drafted or posted. Posting needs owner approval.
