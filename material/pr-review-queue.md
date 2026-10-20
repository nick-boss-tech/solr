# Open PR review queue (apache/solr, not authored by nick-boss-tech)

Snapshot 2026-10-20 from the GitHub API for `apache/solr` `state=open`. 174 PRs. Ready-for-review (non-draft) PRs are listed first, then drafts, each group by least recently updated. `mergeable` is GitHub's `mergeable_state` at snapshot time; `unknown` means GitHub had not computed it yet and needs a re-query before a claim.

| # | Title | Author | Draft | Created | Updated | mergeable | Files | +/- | Claim |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| 1875 | SOLR-16957: Test user managed cluster with a twist! | epugh | no | 2023-08-31 | 2025-10-16 | unknown | 1 | +197/-0 | |
| 2783 | SOLR-17492: Introduce recommendations of WAYS of running Solr from small to massive | epugh | no | 2024-10-19 | 2026-01-28 | unknown | 9 | +344/-16 | |
| 2410 | SOLR-17240: Fix Http2SolrClient maxConnectionsPerDestination usage | HoustonPutman | no | 2024-04-18 | 2026-03-10 | unknown | 3 | +19/-10 | |
| 4042 | SOLR-18068:  POC of keeping existing assertQ logic with solrTestRule | epugh | no | 2026-01-11 | 2026-05-18 | unknown | 3 | +427/-3 | |
| 3684 | SOLR-17712 : Deprecate CommonAdminParams.waitForFinalState and make it true default | abumarjikar | no | 2025-09-23 | 2026-07-14 | dirty | 19 | +64/-30 | |
| 4179 | SOLR-16398: Migrate SplitCore and SplitShard APIs from @EndPoint/@Command to JAX-RS | epugh | no | 2026-03-03 | 2026-08-28 | unknown | 23 | +1433/-895 | |
| 4181 | Migrate RealTimeGetAPI from @EndPoint to JAX-RS | epugh | no | 2026-03-03 | 2026-08-28 | unknown | 9 | +409/-32 | |
| 4538 | SOLR-17559: DocCollection more immutable | dsmiley | no | 2026-06-19 | 2026-08-29 | unknown | 2 | +65/-54 | |
| 4563 | SOLR-18295: Fix flaky ReplicationFactorTest by retrying single-doc sends | janhoy | no | 2026-06-28 | 2026-08-31 | unknown | 1 | +17/-6 | |
| 4475 | skip empty clause for mm count in BoolQParserPlugin | renatoh | no | 2026-05-29 | 2026-08-31 | unknown | 7 | +176/-87 | |
| 4604 | Update com.google.errorprone to 2.50.0 | janhoy | no | 2026-07-02 | 2026-09-02 | unknown | 150 | +495/-203 | |
| 4626 | SOLR-17433: Set default http request timeout to infinite | VishnuPriyaChandraSekar | no | 2026-07-08 | 2026-09-04 | unknown | 10 | +158/-37 | |
| 4109 | SOLR-18101: SolrJ commit options, fluent API | dsmiley | no | 2026-02-06 | 2026-09-04 | unknown | 2 | +121/-14 | |
| 4697 | SOLR-18330: RTG requests to TLOG can short-circuit | dsmiley | no | 2026-08-03 | 2026-09-06 | unknown | 7 | +96/-29 | |
| 4616 | SOLR-17841: Add multithreaded search benchmarks for baseline comparison | arup-chauhan | no | 2026-07-06 | 2026-09-08 | unknown | 1 | +20/-3 | |
| 4874 | SOLR-18417: Remove PingRequestHandler and related code | gerlowskija | no | 2026-09-04 | 2026-09-08 | unknown | 39 | +27/-1197 | |
| 4227 | SOLR-18069: Add Maven build smoke test to verify published POMs | dsmiley | no | 2026-03-19 | 2026-09-10 | unknown | 8 | +486/-4 | |
| 4458 | SOLR-18251: Add Solr schema support for DocValues skip lists on PointField types | iamsanjay | no | 2026-05-23 | 2026-09-15 | unknown | 11 | +140/-6 | |
| 4912 | SOLR-18427: Add v2 API equivalent for List Replicas | iprithv | no | 2026-09-14 | 2026-09-15 | unknown | 9 | +492/-0 | |
| 4911 |  SOLR-18426: Add v2 API equivalent for List Shards | iprithv | no | 2026-09-14 | 2026-09-15 | unknown | 10 | +541/-1 | |
| 4910 | SOLR-18445 Let /update handler support JSONL / NDJSON | janhoy | no | 2026-09-13 | 2026-09-17 | unknown | 18 | +1159/-68 | |
| 4509 | SOLR-18251: Introduced docValuesSkipList='true' field property | VadimKirilchuk | no | 2026-06-07 | 2026-09-18 | unknown | 18 | +1007/-32 | |
| 4765 | Tolerate JVM warning lines when detecting Java version | mpdude | no | 2026-08-19 | 2026-09-19 | unknown | 2 | +10/-1 | |
| 4324 | SOLR-18207: Add derived stored retrieval for DenseVectorField to avoid duplicate vector storage | iamsanjay | no | 2026-04-23 | 2026-09-20 | unknown | 6 | +327/-6 | |
| 4921 | SOLR-18466: Support _route_ in the streaming expression search function | tsornin14 | no | 2026-09-20 | 2026-09-22 | unknown | 4 | +255/-3 | |
| 4625 | SOLR-18301 overseer election does not converge | kotman12 | no | 2026-07-08 | 2026-09-23 | unknown | 7 | +421/-82 | |
| 4899 | SOLR-18439: v2 APIs use 200 on success and 500 with a error.msg key | epugh | no | 2026-09-10 | 2026-09-24 | unknown | 4 | +58/-33 | |
| 4946 | SOLR-18483: Remove assumptions around wt, enabling v2 apis to communicate using headers | epugh | no | 2026-09-25 | 2026-09-25 | unknown | 9 | +108/-112 | |
| 4653 | SOLR-18315: Optimize UTF conversions by using standard Java APIs | psalagnac | no | 2026-07-23 | 2026-09-26 | unknown | 8 | +105/-313 | |
| 4892 | SOLR-17864:  Migrate tracing property names | epugh | no | 2026-09-09 | 2026-09-26 | unknown | 18 | +46/-18 | |
| 4177 | SOLR-18457: Migrate V2 /update endpoints to using JAX-RS | epugh | no | 2026-03-02 | 2026-09-27 | unknown | 37 | +2886/-819 | |
| 4672 | SOLR-18320: Use Configsets V2 API instead of direct ZooKeeper access in bin/solr create | epugh | no | 2026-07-26 | 2026-09-27 | unknown | 5 | +168/-105 | |
| 4471 | SOLR-17767: Add a security Java Agent (SIP-24) | janhoy | no | 2026-05-27 | 2026-09-30 | unknown | 64 | +5152/-5 | |
| 4291 | Render OpenAPI spec as Ref Guide page | epugh | no | 2026-04-19 | 2026-09-30 | unknown | 9 | +122/-0 | |
| 1151 | SOLR-16155 DocumentBuilder should not include field values in error msgs | janhoy | no | 2022-10-30 | 2026-09-30 | unknown | 10 | +93/-35 | |
| 4853 | Revamp Solr Extraction Module to work with Tika 4 - abandoned | solrbot | no | 2026-09-01 | 2026-10-07 | unknown | 26 | +850/-303 | |
| 4893 | SOLR-17864: Provide backwards support for "<str name="hideStackTrace">${solr.hideStackTrace:false}</str>" | epugh | no | 2026-09-09 | 2026-10-09 | unknown | 10 | +152/-35 | |
| 4935 | SOLR-18476: Support file suffix based MMapDirectory preload | liangkaiwen | no | 2026-09-22 | 2026-10-09 | unknown | 6 | +233/-7 | |
| 5049 | Per-version docs styling and unified javadocs search | janhoy | no | 2026-10-06 | 2026-10-09 | unknown | 59 | +2044/-517 | |
| 5063 | SOLR-18521: Unify picocli connection options into one ConnectionOptions mixin | janhoy | no | 2026-10-08 | 2026-10-09 | unknown | 81 | +899/-925 | |
| 4913 | SOLR-18449 standard test properties naming | epugh | no | 2026-09-15 | 2026-10-09 | unknown | 380 | +960/-996 | |
| 4919 | SOLR-18424: add oversampling + raw dense vector reranking | liangkaiwen | no | 2026-09-18 | 2026-10-09 | unknown | 9 | +827/-12 | |
| 5099 | GH workflow validate-changelog: Make the /no-changelog command actually clear the failed changelog check | janhoy | no | 2026-10-10 | 2026-10-10 | unknown | 3 | +147/-90 | |
| 4240 | SOLR-18161: improve node system response | igiguere | no | 2026-03-25 | 2026-10-12 | dirty | 14 | +310/-163 | |
| 4710 | SOLR-18336: bin/solr.cmd start should wait only for the started Solr instance | janhoy | no | 2026-08-04 | 2026-10-12 | unknown | 5 | +166/-47 | |
| 5108 | SOLR-17786: Support cursorMark in the reverse direction | HoustonPutman | no | 2026-10-12 | 2026-10-12 | unknown | 9 | +262/-60 | |
| 5110 | SOLR-12613: node picker for cross-node screens | iprithv | no | 2026-10-12 | 2026-10-12 | unknown | 38 | +1113/-202 | |
| 5020 | SOLR-18497: Do not create a failed core again when a concurrent reload loaded it | serhiy-bzhezytskyy | no | 2026-10-03 | 2026-10-13 | unknown | 3 | +84/-2 | |
| 5019 | SOLR-18496: Read the renamed unique key when merging single-pass shard responses | serhiy-bzhezytskyy | no | 2026-10-03 | 2026-10-13 | unknown | 5 | +89/-5 | |
| 4783 | SOLR-18367: default waitForFinalState per command, remove the deprecated opt-out | serhiy-bzhezytskyy | no | 2026-08-21 | 2026-10-13 | unknown | 39 | +505/-117 | |
| 4758 | SOLR-18379: remove DocsStreamer.KNOWN_TYPES for the ExternalizeStoredValuesAsObjects marker | serhiy-bzhezytskyy | no | 2026-08-19 | 2026-10-13 | unknown | 15 | +86/-89 | |
| 5120 | SOLR-18500: Single-quote the URL in every curl example | serhiy-bzhezytskyy | no | 2026-10-13 | 2026-10-13 | unknown | 51 | +275/-275 | |
| 4804 | SOLR-18363: remove EarlyTerminatingSortingCollector, use native TopFieldCollector | serhiy-bzhezytskyy | no | 2026-08-24 | 2026-10-13 | unknown | 7 | +145/-159 | |
| 5126 | SOLR-3479: New [hl] document transformer for inline highlighting | dsmiley | no | 2026-10-13 | 2026-10-13 | unstable | 7 | +515/-15 | |
| 5113 | SOLR-9957: /export supports document transformers in fl | dsmiley | no | 2026-10-13 | 2026-10-13 | unknown | 5 | +208/-41 | |
| 4652 | SOLR-18314: Correct pf boost handling for single-term and clause-less queries | ispringer | no | 2026-07-23 | 2026-10-14 | unknown | 9 | +345/-2 | |
| 3750 | SOLR-17949: Add Azure Blob Storage backup repository module | prateeksinghalgit | no | 2025-10-09 | 2026-10-14 | unknown | 50 | +5184/-1 | |
| 5121 | SOLR-18526: Use "user-managed" for the mode and fix the start commands | serhiy-bzhezytskyy | no | 2026-10-13 | 2026-10-14 | unknown | 19 | +40/-40 | |
| 4960 | ensure original token is not set to type:SYNONYM in SGF | renatoh | no | 2026-09-29 | 2026-10-15 | unknown | 2 | +40/-6 | |
| 5109 | SOLR-18533: Developer tooling for monitoring | janhoy | no | 2026-10-12 | 2026-10-15 | unknown | 35 | +2154/-0 | |
| 5164 | Fixes to the non-cloud documentation for snapshots | psalagnac | no | 2026-10-15 | 2026-10-15 | unknown | 1 | +60/-15 | |
| 5165 | build: support running in a git worktree (RAT, git-status) | dsmiley | no | 2026-10-15 | 2026-10-16 | unknown | 2 | +8/-10 | |
| 5167 | SOLR-18548: Overseer collection lock leaks when marking an async task as running fails | SpookyJumpyBeans | no | 2026-10-16 | 2026-10-16 | unstable | 3 | +112/-42 | |
| 5127 | SOLR-17971: Fix REBALANCELEADERS wait when the Overseer is disabled | sarkaramrit1993 | no | 2026-10-14 | 2026-10-16 | unknown | 7 | +92/-26 | |
| 5114 | SOLR-18538: CREATESNAPSHOT reports success when a shard has no active replica | SpookyJumpyBeans | no | 2026-10-13 | 2026-10-16 | clean | 3 | +109/-21 | |
| 5163 | Cap concurrent open PRs from non-committers at 7 | janhoy | no | 2026-10-15 | 2026-10-16 | unknown | 3 | +10/-1 | |
| 4675 | Executors/Threads: Enforce reasonable rules | dsmiley | no | 2026-07-27 | 2026-10-17 | unknown | 13 | +209/-93 | |
| 5065 | SOLR-18010: Fix security.json corruption from parallel edits | sarkaramrit1993 | no | 2026-10-09 | 2026-10-17 | unknown | 3 | +75/-0 | |
| 5161 | SOLR-17895: Remove unused lock nodes when the Overseer is disabled | sarkaramrit1993 | no | 2026-10-15 | 2026-10-17 | unstable | 7 | +384/-33 | |
| 5124 | SOLR-18525: Fix core create hang when its replica is removed during registration | sarkaramrit1993 | no | 2026-10-13 | 2026-10-17 | unknown | 3 | +301/-10 | |
| 5066 | SOLR-17953: Ignore hidden files when locating an incremental backup | sarkaramrit1993 | no | 2026-10-09 | 2026-10-17 | unknown | 8 | +212/-14 | |
| 5177 | Rework installing-solr.adoc's Preparing for Installation recommendations | epugh | no | 2026-10-17 | 2026-10-17 | unknown | 1 | +5/-6 | |
| 4690 | SOLR-17328: Add CycloneDX SBOMs to Solr binary distributions | ppkarwasz | no | 2026-08-01 | 2026-10-17 | unknown | 16 | +2075/-84 | |
| 5058 | Demonstrate HTTP QUERY for the read-only SQL interface | epugh | no | 2026-10-07 | 2026-10-17 | unknown | 11 | +470/-51 | |
| 4663 | SOLR-18234: Reorganize org.apache.solr.cli into domain-based sub-packages | jaykay12 | no | 2026-07-25 | 2026-10-18 | unknown | 102 | +607/-106 | |
| 5175 | SOLR-16396: Align v2 full-config section keys with pluralised forms | jaykay12 | no | 2026-10-16 | 2026-10-18 | unstable | 5 | +34/-8 | |
| 5172 | SOLR-18550: Fix for Wrong numFound and numFoundExact for Combined Query results | punAhuja | no | 2026-10-16 | 2026-10-19 | unstable | 5 | +134/-16 | |
| 5128 | S3 backup repository: stop writing and depending on directory marker objects | elangelo | no | 2026-10-14 | 2026-10-19 | clean | 13 | +318/-95 | |
| 5119 | SOLR-18542: Added support for weighted RRF in combined queries | punAhuja | no | 2026-10-13 | 2026-10-19 | unstable | 6 | +277/-17 | |
| 5173 | SOLR-18551: Prevent mmap leak in IndexFetcher | gerlowskija | no | 2026-10-16 | 2026-10-19 | clean | 2 | +13/-1 | |
| 5182 | Document cuvs module breakage in Solr 10.1 | rahulgoswami | no | 2026-10-19 | 2026-10-19 | clean | 2 | +18/-0 | |
| 5179 | SOLR-16398: Add V2 JAX-RS API for replication details | jaykay12 | no | 2026-10-17 | 2026-10-19 | unknown | 9 | +448/-71 | |
| 4178 | SOLR-18212: Migrate PackageAPI endpoints from @EndPoint/@Command to JAX-RS | epugh | no | 2026-03-02 | 2026-10-19 | unstable | 26 | +1285/-992 | |
| 4985 | Update AWS SDK to v2.56.0 (branch_9x) | solrbot | no | 2026-10-01 | 2026-10-19 | unstable | 66 | +66/-66 | |
| 5185 | SOLR-18544: Source CPU utilization metric from JFR instead JMX | mlbiscoc | no | 2026-10-19 | 2026-10-20 | unstable | 1 | +7/-0 | |
| 5178 | Fix v2 API 404 when a /config request is proxied to another node | epugh | no | 2026-10-17 | 2026-10-20 | clean | 2 | +38/-2 | |
| 5168 | SOLR-16396: Migrate GET /config/requestHandler to JAX-RS | jaykay12 | no | 2026-10-16 | 2026-10-20 | unstable | 13 | +357/-114 | |
| 5186 | SOLR-18557: Restore source collection when REINDEXCOLLECTION cleanup fails | SpookyJumpyBeans | no | 2026-10-19 | 2026-10-20 | unstable | 3 | +104/-50 | |
| 16 | SOLR-15104: add SOLR_GC_LOG_PRESTART_ARCHIVE option to bin/solr | cpoerschke | yes | 2021-03-15 | 2021-03-15 | unknown | 2 | +8/-1 | |
| 111 | SOLR-14164: Remove Solr's FunctionRangeQuery & ValueSourceRangeFilter | dsmiley | yes | 2021-05-01 | 2021-06-10 | unknown | 10 | +48/-312 | |
| 200 | LUCENE-8682: remove deprecated WordDelimiterFilter[Factory] classes | cpoerschke | yes | 2021-07-03 | 2021-07-10 | unknown | 6 | +14/-46 | |
| 347 | SOLR-15695: CommitTrackerRollingRestartsTest to demonstrate ... | cpoerschke | yes | 2021-10-15 | 2021-10-18 | unknown | 4 | +201/-2 | |
| 384 | SOLR-15758: in UpdateLog allow numDeletes[ByQuery]ToKeep customisation | cpoerschke | yes | 2021-11-02 | 2021-11-05 | unknown | 3 | +9/-5 | |
| 171 | SOLR-15437: ReRanking / LTR does not work in SolrCloud with custom sort by score | tomglk | yes | 2021-06-10 | 2021-11-09 | unknown | 29 | +540/-76 | |
| 453 | SOLR-15802: SQLHandler endpoint to return the Solr query for a SQL stmt | thelabdude | yes | 2021-12-09 | 2021-12-10 | unknown | 5 | +183/-37 | |
| 567 | SOLR-15958: FilterFeatureScorer now also delegates for Scorable's methods | cpoerschke | yes | 2022-01-26 | 2022-01-26 | unknown | 2 | +27/-4 | |
| 905 | SOLR-16250 Fix LIKE for string fields and multi terms | kiranchitturi | yes | 2022-06-14 | 2022-06-17 | unknown | 4 | +72/-27 | |
| 623 | WIP {!join cacheEventually=true ...}...  | mkhludnev | yes | 2022-02-10 | 2022-07-11 | unknown | 12 | +838/-13 | |
| 945 | SOLR-16078: Create SolrJ sub-modules, including solrj-core | HoustonPutman | yes | 2022-07-15 | 2022-07-15 | unknown | 1110 | +175/-157 | |
| 980 | always(?) use SolrCore.getLatestSchema in RealTimeGetComponent | cpoerschke | yes | 2022-08-22 | 2022-09-12 | unknown | 1 | +5/-4 | |
| 522 | SOLR-15415 Use Embedded ZK in Tests | madrob | yes | 2022-01-12 | 2022-10-18 | unknown | 47 | +286/-576 | |
| 778 | Forbid 'String.matches(String)' API. | cpoerschke | yes | 2022-03-30 | 2022-10-27 | unknown | 9 | +27/-9 | |
| 1258 | Refactoring to move live nodes handling out of ZkStateReader and into own class | justinrsweeney | yes | 2022-12-30 | 2023-01-03 | unknown | 9 | +180/-132 | |
| 1146 | SOLR-16509: All query classes should be immutable | risdenk | yes | 2022-10-28 | 2023-01-23 | unknown | 28 | +204/-233 | |
| 1488 | SOLR-12963: add support for node-level uninvertible support configuration | magibney | yes | 2023-03-23 | 2023-03-29 | unknown | 8 | +100/-4 | |
| 1344 | Tracking Field usage with DirectoryReader | risdenk | yes | 2023-02-09 | 2023-05-01 | unknown | 3 | +206/-3 | |
| 1463 | SOLR-16594: improve eDismax strategy for generating a term-centric query | rseitz | yes | 2023-03-15 | 2023-05-10 | unknown | 13 | +2005/-62 | |
| 1788 | SOLR-16894: Add statscache allowed as resource loader aware | softwaredoug | yes | 2023-07-15 | 2023-07-15 | unknown | 1 | +3/-1 | |
| 1073 | [experimental] start BERT module | cpoerschke | yes | 2022-10-14 | 2023-10-11 | unknown | 20 | +546/-4 | |
| 2004 | Curator remove retry on conn loss | risdenk | yes | 2023-10-12 | 2023-10-12 | unknown | 176 | +1583/-3214 | |
| 2109 | [experimental] (Abstract+Demo)VectorToTextStream | cpoerschke | yes | 2023-12-01 | 2023-12-04 | unknown | 4 | +307/-0 | |
| 2120 | [experimental] TextToVectorProcessor(Factory) using ONNX via OpenNLP | cpoerschke | yes | 2023-12-06 | 2023-12-07 | unknown | 12 | +189/-4 | |
| 2153 | enable solr.log for solrj streaming | mkhludnev | yes | 2023-12-14 | 2023-12-14 | unknown | 2 | +92/-0 | |
| 537 | SOLR-15932 logging rollover configurable | janhoy | yes | 2022-01-18 | 2024-01-12 | unknown | 1 | +4/-4 | |
| 478 | SOLR-15873: factor out a ReRankRescorer class, simplify LTR[Interleaving]Rescorer code | cpoerschke | yes | 2021-12-24 | 2024-03-19 | unknown | 5 | +301/-9 | |
| 2459 | SOLR-17289: OrderedNodePlacementPlugin: optimize don't loop collections | dsmiley | yes | 2024-05-14 | 2024-05-22 | unknown | 2 | +32/-3 | |
| 2469 | SOLR-17301: Support all types of requests in rate limiting | noblepaul | yes | 2024-05-20 | 2024-06-03 | unknown | 5 | +81/-37 | |
| 1638 | SOLR-8393: Component for resource usage planning | igiguere | yes | 2023-05-10 | 2024-06-07 | unknown | 19 | +1783/-9 | |
| 2576 | SOLR-15689: PeerSync to consistently handle 'delete' highest versions | cpoerschke | yes | 2024-07-19 | 2024-07-19 | unknown | 2 | +89/-1 | |
| 2603 | PeerSync.Updater.updateRecordComparator -- clarify lstX.get(0) vs. lstX.get(1) | cpoerschke | yes | 2024-07-29 | 2024-07-29 | unknown | 1 | +1/-0 | |
| 2626 | PeerSync: INFO-log DBQs with NOW | cpoerschke | yes | 2024-08-07 | 2024-08-07 | unknown | 1 | +7/-0 | |
| 2704 | Add filter query tracing | risdenk | yes | 2024-09-10 | 2024-09-30 | unknown | 3 | +143/-0 | |
| 2738 | SOLR-?????: add hl.encoder=stripHTML for the unified highlighter only | cpoerschke | yes | 2024-10-02 | 2024-10-02 | unknown | 3 | +79/-0 | |
| 470 | SOLR-15869: expand LTRRescorer.rescore test coverage | cpoerschke | yes | 2021-12-22 | 2024-10-12 | unknown | 1 | +22/-1 | |
| 2865 | facet module ext | mkhludnev | yes | 2024-11-15 | 2024-11-15 | unknown | 22 | +291/-174 | |
| 2880 | SOLR-17571: Introduce dependabot | malliaridis | yes | 2024-11-20 | 2024-12-01 | unknown | 10 | +441/-317 | |
| 3172 | SOLR-17662 SPI: TransformerFactory/DocTransformer | dsmiley | yes | 2025-02-10 | 2025-02-12 | unknown | 14 | +115/-24 | |
| 2930 | SOLR-16391: Convert "modify-coll" API to JAX-RS | gerlowskija | yes | 2025-01-01 | 2025-02-25 | unknown | 16 | +316/-241 | |
| 3023 | SOLR-17728: Add smoke test for V2 API | colvinco | yes | 2025-01-10 | 2025-07-01 | unknown | 4 | +457/-1 | |
| 3511 | New Collections UI  | iamsanjay | yes | 2025-08-26 | 2025-08-27 | unknown | 27 | +1794/-5 | |
| 2382 | SOLR-4587: integrate lucene-monitor into solr | kotman12 | yes | 2024-04-01 | 2025-09-09 | unknown | 42 | +5032/-1 | |
| 2597 | SOLR-17319: CompoundQueryComponent scribbles | cpoerschke | yes | 2024-07-26 | 2025-09-10 | unknown | 3 | +149/-0 | |
| 3746 | docCollection ease blocking. | sijuv | yes | 2025-10-09 | 2025-10-13 | unknown | 1 | +54/-61 | |
| 395 | SOLR-15767: Prometheus alert rules for monitoring SolrCloud clusters on Kubernetes | thelabdude | yes | 2021-11-03 | 2025-10-26 | unknown | 1 | +243/-0 | |
| 3191 | Optimize Gradle tasks and configurations | malliaridis | yes | 2025-02-16 | 2025-10-31 | unknown | 55 | +263/-253 | |
| 3806 | NO JIRA: reduce false-positive ERROR logging in IndexFetcher | cpoerschke | yes | 2025-10-23 | 2025-11-01 | unknown | 1 | +2/-0 | |
| 3828 | SOLR-17657: Use Gradle Verification Metadata | ppkarwasz | yes | 2025-11-02 | 2025-11-02 | unknown | 460 | +13925/-494 | |
| 3648 | SOLR-17319: CompoundQueryComponent scribbles continued with CompoundQueryComponentTest | cpoerschke | yes | 2025-09-10 | 2025-11-03 | unknown | 8 | +612/-22 | |
| 3983 | SOLR-14361 Solr should own the boot process (SIP-6) | janhoy | yes | 2025-12-27 | 2025-12-28 | unknown | 21 | +4442/-355 | |
| 3965 | SOLR-18025 Attempt to fix graceful shutdown of LeaderTragicEventTest | janhoy | yes | 2025-12-18 | 2026-01-17 | unknown | 2 | +50/-8 | |
| 4079 | SOLR-18087 HTTP/2 Struggles With Streaming Large Responses | kotman12 | yes | 2026-01-27 | 2026-02-13 | unknown | 16 | +1236/-22 | |
| 4184 | distrib join through coord node | mkhludnev | yes | 2026-03-03 | 2026-03-07 | unknown | 1 | +53/-5 | |
| 4199 | Potential solution to Maven POM generation issue | janhoy | yes | 2026-03-09 | 2026-03-09 | unknown | 1 | +28/-0 | |
| 4207 | Add 'optimized' category to changelog entries | psalagnac | yes | 2026-03-11 | 2026-03-11 | unknown | 4 | +14/-10 | |
| 1548 | SOLR-16739 Introduce the Kubernetes Module and KubernetesConfigSetService | HoustonPutman | yes | 2023-04-06 | 2026-03-12 | unknown | 31 | +2156/-1 | |
| 4230 | [exploratory] modules/language-models: semantic highlighting component | cpoerschke | yes | 2026-03-20 | 2026-03-20 | unknown | 9 | +432/-1 | |
| 313 | SOLR-13681: make Lucene's index sorting directly configurable in Solr | cpoerschke | yes | 2021-09-28 | 2026-03-27 | unknown | 6 | +123/-8 | |
| 4186 | Join from coordinator node fails  | endika-ravn | yes | 2026-03-04 | 2026-03-29 | unknown | 2 | +69/-28 | |
| 3972 | SOLR-12074: Add new NumericField that combines terms and points | HoustonPutman | yes | 2025-12-20 | 2026-04-10 | unknown | 109 | +9618/-671 | |
| 4245 | SOLR-18190: Index upgrade Collection level API | rahulgoswami | yes | 2026-03-29 | 2026-04-19 | unknown | 8 | +585/-8 | |
| 4336 | SOLR-18213 Replace simplemagic with built-in magic-byte file type detection | janhoy | yes | 2026-04-25 | 2026-05-10 | unknown | 40 | +695/-195 | |
| 4334 | SOLR-18211 Migrate jwt-auth module from bc-jose4j to nimbus-jose-jwt | janhoy | yes | 2026-04-25 | 2026-05-12 | unknown | 11 | +902/-680 | |
| 2391 | SOLR-18094 Support running embedded-zk in "ensemble" mode with new node role | gerlowskija | yes | 2024-04-05 | 2026-05-14 | unknown | 14 | +1152/-374 | |
| 1951 | Port defaulting should be the same for all tools | HoustonPutman | yes | 2023-09-22 | 2026-05-29 | unknown | 19 | +325/-207 | |
| 4493 | Prototype reusing jetties | kotman12 | yes | 2026-06-01 | 2026-06-04 | unknown | 8 | +473/-26 | |
| 4311 | Docs and AI prompt for assistance with bulk upgrading dependencies | janhoy | yes | 2026-04-21 | 2026-06-29 | unknown | 2 | +378/-6 | |
| 4714 | Fix flaky ChaosMonkeySafeLeaderWithPullReplicasTest | janhoy | yes | 2026-08-06 | 2026-08-16 | unknown | 1 | +43/-4 | |
| 4792 | explore integrating Lucene's DiversifiedTopDocsCollector | cpoerschke | yes | 2026-08-21 | 2026-08-21 | unknown | 4 | +430/-2 | |
| 2452 | User Behavior Insights implementation for Apache Solr | epugh | yes | 2024-05-09 | 2026-08-28 | unknown | 25 | +2340/-22 | |
| 4827 | Realtime retrieval of docs in RESTful shape | epugh | yes | 2026-08-28 | 2026-08-28 | unknown | 17 | +836/-32 | |
| 4145 | SOLR-18111: Remove deprecated field types (Legacy and Trie and BBox) | epugh | yes | 2026-02-18 | 2026-08-31 | unknown | 164 | +2048/-12341 | |
| 4863 | Solr Cell: index Tika 4.x `tk:chunks` into a `dense_vector` field | epugh | yes | 2026-09-02 | 2026-09-02 | unknown | 26 | +1187/-106 | |
| 4329 | SOLR-18191: draft | renatoh | yes | 2026-04-24 | 2026-09-04 | unknown | 5 | +154/-7 | |
| 4280 | Exclude test_rolling_upgrade from integrationTests; add testSmokeBats task | dsmiley | yes | 2026-04-13 | 2026-09-09 | unknown | 2 | +53/-3 | |
| 4933 | Scope ExecutorUtil.isSolrServerThread() to Solr-originated work and propagate by inheritance | dsmiley | yes | 2026-09-22 | 2026-09-25 | unknown | 6 | +105/-4 | |
| 4943 | SOLR-18479: Add v2 copy-field endpoints to the Schema API | janhoy | yes | 2026-09-24 | 2026-09-27 | unknown | 9 | +692/-34 | |
| 4742 | SOLR-18348: Move building suggester to async process outside of commit lifecycle. | epugh | yes | 2026-08-15 | 2026-09-30 | unknown | 23 | +1978/-18 | |
| 4829 | SOLR-18402: Consolidate wasRequestUnsent / wasCommError (retry) logic | chan-dx | yes | 2026-08-30 | 2026-09-30 | unknown | 10 | +371/-121 | |
| 5107 | SOLR-8442: Make security easier WIP | epugh | yes | 2026-10-12 | 2026-10-12 | unknown | 21 | +1717/-154 | |
| 4210 | SOLR-18147 Make a new Grafana dashboard for Solr 10.x | janhoy | yes | 2026-03-12 | 2026-10-12 | clean | 40 | +5265/-9 | |
| 5112 | SOLR-18537: Proxy requests to a node via X-Solr-targetNode | dsmiley | yes | 2026-10-12 | 2026-10-12 | unknown | 3 | +157/-0 | |
| 5166 | Ref guide: upgrade Antora to 3.2.1 | dsmiley | yes | 2026-10-16 | 2026-10-16 | unknown | 1 | +1/-1 | |
| 4916 | Add resource-oriented v2 APIs for Basic Auth users and RBAC roles/permissions | epugh | yes | 2026-09-16 | 2026-10-17 | unknown | 24 | +2139/-143 | |
| 5174 | SOLR-16396: Migrate remaining GET /config/{component} sections to JAX-RS | jaykay12 | yes | 2026-10-16 | 2026-10-19 | unstable | 6 | +65/-51 | |
