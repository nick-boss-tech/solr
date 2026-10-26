# Unit-1 comment drafts, round 5: completion pass (vm5, 2026-10-26)

Nothing is posted. Code reading only, against apache/main `b73f03976fa`, with JIRA status fetched read-only. Each draft needs a live JIRA check at fill time and owner approval. Add the AI disclosure footer used in rounds 1 to 3 when posting.

## 1. Fixed-family closures re-checked (5)

The round-2 closure drafts said the code now behaves differently. Re-checking found none of them fixed on main. They are reclassified and the closure wording is withdrawn.

- **SOLR-6099** (DELETESHARD of sub-shards): NOT FIXED. CONSTRUCTION and RECOVERY slices are deletable (`DeleteShardCmd.java:84-88`), but RECOVERY_FAILED, which split sets on failed sub-shards (`SplitShardCmd.java:666`), is still rejected, so a re-split fails at `SplitShardCmd.java:319-352`. Class: ASK-IF-STILL-NEEDED.
  Draft: "From code reading against apache/main b73f03976fa, DELETESHARD now accepts CONSTRUCTION and RECOVERY sub-shards (solr/core/src/java/org/apache/solr/cloud/api/collections/DeleteShardCmd.java:84-88), but a sub-shard in RECOVERY_FAILED, which split sets on failure (SplitShardCmd.java:666), is still rejected there. A re-split after a failed split therefore still appears to fail. Is this still a problem on a current release?"

- **SOLR-6712** (highlighting in distributed grouped results): NOT FIXED. The NPE site is guarded (`QueryComponent.java:1405-1406`), but `resultIds` is filled only on the non-grouped path (`QueryComponent.java:963`), so grouped documents get no ShardDoc positions and their highlights are dropped (`HighlightComponent.java:267-272`). Class: ASK-IF-STILL-NEEDED.
  Draft: "From code reading against apache/main b73f03976fa, resultIds is filled only for non-grouped results (solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java:963), so HighlightComponent (HighlightComponent.java:267-272) drops highlights for grouped documents in distributed search. Do you still see missing highlights for grouped results on a current release?"

- **SOLR-7198** (leader znode creation after collection delete): UNCLEAR. Delete removes `/collections/<name>` recursively (`DeleteCollectionCmd.java:207-210`), and leader-path creation notes it succeeds only while the collection exists (`ShardLeaderElectionContextBase.java:81`). The race that leaves znodes needs a runtime probe. Class: UNCERTAIN, hold.
  Information request: "Which Solr version and sequence (delete collection while a replica is re-registering?) left the leader znode behind, and does it reproduce on a current release?"

- **SOLR-8770** (null values in javabin): UNCLEAR. A null is written as the NULL tag (`JavaBinCodec.java:1247-1249`) and read back as null (`:315-316`), so the literal "NULL" string in the ticket does not appear by reading. No commit or resolution confirms a fix. Class: UNCERTAIN, hold.
  Information request: "Which client and server versions produced the NULL string in the field, and does it reproduce on a current release with the binary codec?"

- **SOLR-11292** (alias routing): UNCLEAR. Alias targets are resolved and routed from the target collection's state (`CloudSolrClient.java:957`, `:1060-1068`), so the reported routing does not reproduce by reading, but no commit ties this to a fix. Class: UNCERTAIN, hold.
  Information request: "Which SolrJ version and alias definition showed requests going only to the first collection's nodes, and does it still happen on a current release?"

## 2. "No comment" tickets, now drafted (11)

### Close candidates (2)

- **SOLR-8067** (ZkCmdExecutor retry on session expiry): ZkCmdExecutor no longer exists on main. The only session-expiry retry is in `ZkSolrResourceLoader.java:79-105`, the follow-up the comments name. The reporter already suggested Invalid. Draft: "From code reading against apache/main b73f03976fa, ZkCmdExecutor no longer exists, and the only session-expiry retry is the resource-loader retry (solr/core/src/java/org/apache/solr/cloud/ZkSolrResourceLoader.java:79-105), which the comments name as the follow-up. Can this be closed as Invalid, or is retry-on-session-expiry in retryOperation still needed?"
  Check before posting: the reporter's Invalid suggestion is in the comments; confirm no newer reply.

- **SOLR-3721** (overlapping recovery): `DefaultSolrCoreState.doRecovery` serializes recovery per core and cancels the prior recovery before blocking on the lock (`solr/core/src/java/org/apache/solr/update/DefaultSolrCoreState.java:52, 315-326`). The subagent reported that a comment says a fix was committed, but I have not verified that commit, so the draft does not cite it. Draft: "From code reading against apache/main b73f03976fa, doRecovery serializes recovery per core and cancels the previous recovery before blocking on the lock (solr/core/src/java/org/apache/solr/update/DefaultSolrCoreState.java:52, 315-326). Can you still reproduce overlapping 'Starting recovery' lines on a current release? If not, can this be closed?"
  Check before posting: verify the commit mentioned in the comments before any closure wording.

### Ask if still needed (6)

- **SOLR-8311** (lifecycle of SolrCoreAware and ResourceLoaderAware objects): `SolrResourceLoader.newInstance` registers those interfaces only while the loader is not live (`solr/core/src/java/org/apache/solr/core/SolrResourceLoader.java:658-697`), and `live` is set at `:764`. Draft: "From code reading against apache/main b73f03976fa, SolrResourceLoader.newInstance registers SolrCoreAware and ResourceLoaderAware instances only while the loader is not live (solr/core/src/java/org/apache/solr/core/SolrResourceLoader.java:658-697), so objects created later are not informed. Is this umbrella still needed for the remaining cases, or should it close in favour of the follow-up issues it links?"

- **SOLR-8709** (TopicStream redelivery): `TopicStream.java:67` has no version checksum or retention window. Draft: "From code reading against apache/main b73f03976fa, TopicStream (solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/TopicStream.java:67) has no version checksum or retention window, so the proposed sliding-window redelivery is not present. Do you still need checksum-based redelivery, or can this close with the limitation documented?"

- **SOLR-9477** (child documents skipped by the UUID processor): only `AddSchemaFieldsUpdateProcessorFactory.java:556` and `IgnoreLargeDocumentProcessorFactory.java:148-149` iterate child documents. `UUIDUpdateProcessorFactory` does not. Draft: "From code reading against apache/main b73f03976fa, UUIDUpdateProcessorFactory does not iterate child documents, while AddSchemaFieldsUpdateProcessorFactory (solr/core/src/java/org/apache/solr/update/processor/AddSchemaFieldsUpdateProcessorFactory.java:556) and IgnoreLargeDocumentProcessorFactory do. Do you still see missing ids on child documents with the current chain, and which processor should handle them?"

- **SOLR-9648** (merge policy upgrade on startup): only the opt-in `UpgradeIndexMergePolicyFactory.java:25-34` upgrades segments. Draft: "From code reading against apache/main b73f03976fa, segment upgrading exists only as the opt-in UpgradeIndexMergePolicyFactory (solr/core/src/java/org/apache/solr/index/UpgradeIndexMergePolicyFactory.java:25-34), and nothing upgrades segments on startup. Is automatic startup upgrade still wanted, or is the opt-in factory enough to close this?"

- **SOLR-9978** (collapse memory with top-level sort): `OrdFieldValueCollector` allocates per-ordinal maps sized by unique values (`solr/core/src/java/org/apache/solr/search/CollapsingQParserPlugin.java:1187-1199`), and no any-head collector exists on main. Draft: "From code reading against apache/main b73f03976fa, collapse with a top-level sort and no min or max still uses OrdFieldValueCollector, which allocates per-ordinal maps (solr/core/src/java/org/apache/solr/search/CollapsingQParserPlugin.java:1187-1199), and there is no any-head collector. Is the any-head optimization still wanted on current main?"

- **SOLR-1557** (shards parameter parsing): `CloudReplicaSource.java:54` reads one value, and `:101` splits without trimming (`StrUtils.java:111-158`). Draft: "From code reading against apache/main b73f03976fa, the shards parameter is read as one value (solr/core/src/java/org/apache/solr/handler/component/CloudReplicaSource.java:54) and split without trimming whitespace (CloudReplicaSource.java:101, solr/solrj/src/java/org/apache/solr/common/util/StrUtils.java:111-158). Do you still need whitespace around shard entries or repeated shards parameters?"

### Uncertain: information request, hold (3)

- **SOLR-7521** (string path for numeric collapse): `CollapsingQParserPlugin.java:1187` and `:1742` keep separate string and numeric collectors. Request: "Which operation on an indexed-only numeric field (collapse, sort or faceting) should use the string path, and what fails on current main?"
- **SOLR-8368** (stale leader from uncapped tlog): the order of election and tlog replay (`ZkController.java:1451`, `:1517`; `ShardLeaderElectionContext.java:218`) cannot be settled by reading. Request: "Which Solr version and node state (uncapped tlog at restart, and the election log lines with the version list) produced the stale leader, and does it reproduce on a current release?"
- **SOLR-11583** (container-free streaming read): byte-backed strings are in place (`ByteArrayUtf8CharSequence.java:33`, `JavaBinCodec.java:1149-1181`), but readMap, readIterator and readArray still build containers (`JavaBinCodec.java:851-861, 938, 977`). Request: "Do you still need a container-free streaming read, and if so, what response shape and allocation figures show the problem on current main?"

## 3. Duplicate candidates (7): no drafts

A JIRA check found no recorded duplicate target for any of the seven: SOLR-3765, 5772, 7721, 9089, 11034, 11787, 13470. None meets the pack rule, so none is drafted. SOLR-5772 has one near-miss comment that discusses the topic without declaring a duplicate. SOLR-11787 has a 2024 question about SOLR-6213 that is unanswered.
