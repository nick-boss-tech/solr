# Candidate and close comments from the maintainer re-triage, round 1 (vm5, 2026-10-26)

Nothing is posted. Each draft is from code reading against apache/main `b73f03976fa`, no runs. Every post needs owner approval. Add the AI disclosure footer used in earlier drafts when posting.

Category meaning: a **candidate** names one defensible behaviour and asks the ticket creator whether it is still wanted. Any PR and its debate come later and are not in the comment.

## Candidate (21)

### SOLR-3008
From code reading against apache/main b73f03976fa, explicit quoted phrases are not shingled into `pf` (solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java:306-313). A candidate is to keep that and document it in the edismax `pf` section, rather than change scoring. Is that the behaviour you want?

### SOLR-4023
From code reading against apache/main b73f03976fa, the classic parser keeps Lucene precedence for AND and OR, and the reference guide does not say so (solr-ref-guide/modules/query-guide/pages/standard-query-parser.adoc:280-301). A candidate is a docs note with a parentheses recommendation, with no grammar change. Is that enough for your case?

### SOLR-6719
From code reading against apache/main b73f03976fa, `property.name` on collection CREATE is ignored for the core name (solr/core/src/java/org/apache/solr/cloud/api/collections/CreateCollectionCmd.java:380). A candidate is to document that rather than honour or reject it. Is that acceptable?

### SOLR-6785
From code reading against apache/main b73f03976fa, the CSV writer escapes backslashes by default for polyfields (solr/core/src/java/org/apache/solr/response/CSVResponseWriter.java:303-306). A candidate is to document that default rather than change CSV output. Is that what you need?

### SOLR-7069
From code reading against apache/main b73f03976fa, a collection-path request fails with a 500 when the local replica's core failed to initialise, instead of trying a remote replica (solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java:901-911; solr/core/src/java/org/apache/solr/core/CoreContainer.java:2216-2224). A candidate is to fall through to a healthy remote replica on the collection path and keep the 500 for direct requests. Is this still a problem?

### SOLR-7071
From code reading against apache/main b73f03976fa, the path is the same as SOLR-7069 (HttpSolrCall.java:901-911). A candidate is the same change, handled with SOLR-7069. Is this still a problem, or is it covered by 7069?

### SOLR-7247
From code reading against apache/main b73f03976fa, real-time get and delete-by-id without `_route_` skip the id or use the local shard when `routeField` is used (solr/solrj/src/java/org/apache/solr/common/cloud/CompositeIdRouter.java:162-168; solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java:1064-1066). A candidate is to send those requests to all shards. Is that still needed?

### SOLR-7257
From code reading against apache/main b73f03976fa, real-time get drops copyField targets, stored or not (solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java:626-633). A candidate is to document that on the real-time get page. Is that enough?

### SOLR-7347
From code reading against apache/main b73f03976fa, the version clock is not raised from versions a new leader has seen (solr/core/src/java/org/apache/solr/update/VersionInfo.java:127-135). A candidate is to raise the clock on the follower's apply path, so a new leader never issues a lower version. Is this still wanted?

### SOLR-7932
From code reading against apache/main b73f03976fa, `IndexFetcher` skips on equal wall-clock timestamp and checks a full copy by timestamp (solr/core/src/java/org/apache/solr/handler/IndexFetcher.java:613, 641). A candidate is to compare generations in SolrCloud instead. Is that still the behaviour you want fixed?

### SOLR-8553
From code reading against apache/main b73f03976fa, RELOAD is sent only to ACTIVE replicas (solr/core/src/java/org/apache/solr/cloud/api/collections/CollApiCmds.java:277). A candidate is to include RECOVERING replicas too, so a schema change reaches them. Is this still a problem?

### SOLR-8862
From code reading against apache/main b73f03976fa, `createCollection` in the test cluster can run before nodes answer core requests (solr/core/src/java/org/apache/solr/cloud/ZkController.java:497-508). A candidate is a test-harness wait for a core-level response, with no change to ZooKeeper layout. Is the failure still seen?

### SOLR-8893
From code reading against apache/main b73f03976fa, `tv.df` uses the local reader (solr/core/src/java/org/apache/solr/handler/component/TermVectorComponent.java:384), while the reference guide says it uses global statistics. A candidate is to correct the guide. Is that enough for you?

### SOLR-9174
From code reading against apache/main b73f03976fa, `q.op=AND` also governs the analyzer's multi-token sub-query when `mm` is set (solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java:445, 1013). A candidate is to make that sub-query use SHOULD when `mm` is given. Is this still reported on a current release?

### SOLR-9230
From code reading against apache/main b73f03976fa, `BigDecimal` falls back to the reflect writer, which outputs the class name and value (solr/solrj/src/java/org/apache/solr/common/util/JavaBinCodec.java:280; solr/solrj/src/java/org/apache/solr/common/util/Utils.java:795). A candidate is to write `BigDecimal` and `BigInteger` as their text, as before 6.0. Is that still wanted?

### SOLR-9361
From code reading against apache/main b73f03976fa, `onlyIfDown` requires state DOWN, and a crashed node's replicas can stay ACTIVE (solr/core/src/java/org/apache/solr/cloud/api/collections/DeleteReplicaCmd.java:274-279). A candidate is to document that in the replica management guide rather than change states. Is that enough?

### SOLR-9369
From code reading against apache/main b73f03976fa, the `commitTimeMSec` equality check compares a value copied with the leader's segments file, not two clocks (solr/core/src/java/org/apache/solr/handler/IndexFetcher.java:611-617). A candidate is to replace the TODO with that reason and leave the behaviour as is. Is that still an open concern?

### SOLR-9514
From code reading against apache/main b73f03976fa, `BALANCESHARDUNIQUE` is live and not deprecated (solr/core/src/java/org/apache/solr/common/params/CollectionParams.java:113). A candidate is to deprecate it in the guide and javadoc, with no removal. Is deprecation the right step?

### SOLR-9550
From code reading against apache/main b73f03976fa, `InnerJoinStream` does not check that its input is sorted (solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/InnerJoinStream.java:63-73). A candidate is to throw when a side's tuples are out of order. Is an error the behaviour you want?

### SOLR-9841
From code reading against apache/main b73f03976fa, expiration delete always sets soft commit (solr/core/src/java/org/apache/solr/update/processor/DocExpirationUpdateProcessorFactory.java:402-406). A candidate is an opt-in setting, default unchanged, that skips the soft commit. Is that still wanted?

### SOLR-11495
From code reading against apache/main b73f03976fa, every standard query parser is registered by default (solr/core/src/java/org/apache/solr/search/QParserPlugin.java:47-99), and the production guide does not say so. A candidate is a docs section on the default parsers and how to shadow one from solrconfig. Is that enough?

## Close (9)

### SOLR-2855
From code reading against apache/main b73f03976fa, range facets have no bucket cap (solr/core/src/java/org/apache/solr/handler/component/RangeFacetRequest.java:114-146). The original reply said this is by design, and the reporter agreed. Can this close as intended, with the limit documented?

### SOLR-4100
From code reading against apache/main b73f03976fa, commitWithin is soft by default (solr/core/src/java/org/apache/solr/core/SolrConfig.java:833-834), and the hard-commit setting is documented (solr-ref-guide/modules/configuration-guide/pages/commits-transaction-logs.adoc:190-204). Can this close, pointing to that setting?

### SOLR-4983
From code reading against apache/main b73f03976fa, cross-collection join takes a collection name for `fromIndex` (solr/core/src/java/org/apache/solr/search/join/CrossCollectionJoinQParser.java), and core names are generated internally (solr/core/src/java/org/apache/solr/cloud/api/collections/Assign.java:182-190). Can this close? The single-shard join limit could be noted in join-query-parser.adoc.

### SOLR-4996
From code reading against apache/main b73f03976fa, a collection request goes to one replica chosen by the replica list (solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java:898-960). Per-replica reads are a core-level client job. Can this close as intended?

### SOLR-5095
From code reading against apache/main b73f03976fa, the JMX map named in the ticket is not in main, so its lost-MBean bug has no path there. The flat-name collision in SolrCore still exists (solr/core/src/java/org/apache/solr/core/SolrCore.java:227, 3458), but nothing reproduces a lost MBean. Can this close, with that noted?

### SOLR-6837
From code reading against apache/main b73f03976fa, the leader waits for replicas and warns about data loss (solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java:162, 273). `min_rf` is now report-only (solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-8.adoc:329). Can this close as a known limit of leader-only durability?

### SOLR-8543
From code reading against apache/main b73f03976fa, the default chain guesses a field's type from its value, so a JSON string "50" becomes a long (solr-ref-guide/modules/indexing-guide/pages/schemaless-mode.adoc:25). Can this close, with a sentence added to say guessing is by value and not JSON type?

### SOLR-9223
From code reading against apache/main b73f03976fa, SOLR-7065 (Closed, Fixed 7.3/8.0) covers the case where a lower-term replica becomes leader after the wait (solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java:185-188, 340-360). The remaining case is the FORCELEADER path. Can this close as fixed by SOLR-7065?

### SOLR-11927
From code reading against apache/main b73f03976fa, XML has no empty child-list syntax, and JSON already expresses an empty child list with `_childDocuments_: []` (solr/core/src/java/org/apache/solr/handler/loader/JsonLoader.java:73). Can this close as won't fix?
