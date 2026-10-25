# Critical tickets census (2026-10-24)

Source: live Jira, `project = SOLR AND resolution is EMPTY AND priority = Critical`, ordered by last update ascending (stalest first). 86 tickets. This is the priority-Critical set only; the open Blocker set (15 tickets) is a separate, already-promised line.

Shape: 65 Bug, 12 Improvement, 5 Task, 2 New Feature, 2 Wish. Status: 79 Open, 4 Patch Available, 3 Reopened. Age: 64 untouched for over 2 years, 7 between 1 and 2 years, 4 within the year, 11 within 30 days. 11 assigned, 6 carry a fix version, 24 have attachments, 24 have issue links.

Batches of 10 in census order. One claim covers one batch (`assignments/critical-tickets-comments-round-1.md`).

## Batch 01

| Key | Type | Status | Updated | Att | Links | Summary |
|---|---|---|---|---|---|---|
| SOLR-4180 | Wish | Open | 2012-12-13 | 0 | 0 | StatisticsHandler multicire support |
| SOLR-6420 | Bug | Open | 2014-10-14 | 0 | 0 | ADDREPLICA can add a replica that reports green without actually replica |
| SOLR-5821 | Bug | Reopened | 2014-12-05 | 2 | 0 | Search inconsistency on SolrCloud replicas |
| SOLR-7351 | Bug | Open | 2015-04-07 | 0 | 1 | MoreLikeThis component doesn't work with dates |
| SOLR-7517 | Bug | Open | 2015-05-14 | 0 | 0 | When you can't persist a core.properties file we simply log an error and |
| SOLR-5850 | Bug | Open | 2015-05-30 | 4 | 1 | Race condition in ConcurrentUpdateSolrServer |
| SOLR-6683 | Bug | Open | 2015-08-23 | 0 | 0 | Need a configurable parameter to control the doc number between peersync |
| SOLR-8331 | Bug | Open | 2015-11-26 | 0 | 0 | CorruptIndexException after auto commit |
| SOLR-8417 | Bug | Open | 2015-12-15 | 1 | 0 | HttpSolrCall occur a Infinite loop request process if the collection is  |
| SOLR-7926 | Bug | Reopened | 2016-02-05 | 0 | 0 | Hit highlighting with EdgeNGramFilterFactory |

## Batch 02

| Key | Type | Status | Updated | Att | Links | Summary |
|---|---|---|---|---|---|---|
| SOLR-8905 | Bug | Open | 2016-03-25 | 0 | 0 | AutoAddReplicas feature has bug that local coreproperties file was delet |
| SOLR-3684 | Improvement | Open | 2016-05-09 | 1 | 0 | Frequently full gc while do pressure index |
| SOLR-4285 | Bug | Open | 2016-05-09 | 0 | 0 | Solr mangles distributed query parameters |
| SOLR-4165 | Bug | Reopened | 2016-05-09 | 0 | 2 | Queries blocked when stopping a node |
| SOLR-9138 | Bug | Open | 2016-05-20 | 0 | 0 | CDATA should be requisite for text/string field type in xml-format resul |
| SOLR-9170 | Bug | Open | 2016-05-28 | 0 | 0 | Need detailed diagnostics for Zookeeper Connection Issues |
| SOLR-8887 | Bug | Open | 2016-11-21 | 0 | 1 | Solr Security features cannot export the internal, deprecated DefaultHtt |
| SOLR-4690 | Bug | Open | 2016-12-07 | 0 | 0 | Highlighting Doesn't works when boost is used along with query |
| SOLR-8028 | Bug | Open | 2017-02-03 | 0 | 0 | ConcurrentModificationException thrown from JavaBinCodec |
| SOLR-6345 | Bug | Open | 2018-01-11 | 0 | 0 | collapsingQParserPlugin degraded performance when using tagging |

## Batch 03

| Key | Type | Status | Updated | Att | Links | Summary |
|---|---|---|---|---|---|---|
| SOLR-8767 | Bug | Open | 2018-05-03 | 0 | 1 | RealTimeGetComponent and stored/copyField exclusion |
| SOLR-2908 | Improvement | Open | 2019-04-21 | 2 | 1 | To push the terms.limit parameter from the master core to all the shard  |
| SOLR-13460 | Bug | Open | 2019-06-08 | 0 | 0 | org.apache.solr.common.SolrException: Exception writing document id site |
| SOLR-11650 | Bug | Open | 2019-06-08 | 2 | 0 | Credentials used for BasicAuth displayed in clear text on slave nodes |
| SOLR-11668 | Bug | Open | 2019-06-08 | 0 | 0 | Lookup not supported at this time orStackOverflowError after upgradin fr |
| SOLR-12415 | Bug | Open | 2019-06-08 | 0 | 0 | Solr Loadbalancer client LBHttpSolrClient not working as expected, if a  |
| SOLR-11972 | Improvement | Open | 2019-06-08 | 0 | 0 | range of numeric values with unit in Solr |
| SOLR-11330 | Improvement | Open | 2019-06-08 | 0 | 0 | Solr arabic search |
| SOLR-10950 | Wish | Open | 2019-06-08 | 0 | 0 | Support context filtering for FuzzyLookupFactory |
| SOLR-10928 | Improvement | Open | 2019-06-08 | 0 | 0 | Support elevate.q in QueryElevationComponent |

## Batch 04

| Key | Type | Status | Updated | Att | Links | Summary |
|---|---|---|---|---|---|---|
| SOLR-12069 | Bug | Open | 2019-08-15 | 0 | 0 | Default operator parameter q.op ignored. |
| SOLR-12315 | Bug | Open | 2019-08-15 | 1 | 0 | the number of docs in each group depends on rows |
| SOLR-8096 | Bug | Open | 2019-09-08 | 2 | 4 | Major faceting performance regressions |
| SOLR-6816 | Task | Open | 2019-10-02 | 1 | 1 | Review SolrCloud Indexing Performance. |
| SOLR-12801 | Task | Open | 2019-10-24 | 0 | 15 | Fix the tests, remove BadApples and AwaitsFix annotations, improve env f |
| SOLR-12743 | Bug | Open | 2020-01-07 | 1 | 0 | Memory leak introduced in Solr 7.3.0 |
| SOLR-13850 | Bug | Open | 2020-01-21 | 0 | 0 | Atomic Updates with PreAnalyzedField |
| SOLR-13770 | Bug | Open | 2020-01-21 | 1 | 0 | Solr Fails to recover Replica(s) after Restart due to Deep Nested Direct |
| SOLR-13789 | Bug | Patch Available | 2020-01-21 | 1 | 0 | Solr core reload with config change upstream put solr instance in bad st |
| SOLR-14638 | Bug | Open | 2020-07-20 | 0 | 0 | Edismax boost function zero score |

## Batch 05

| Key | Type | Status | Updated | Att | Links | Summary |
|---|---|---|---|---|---|---|
| SOLR-11310 | Bug | Open | 2020-07-30 | 0 | 0 | QueryElevation doesn't work with Solr LTR  |
| SOLR-14833 | Bug | Open | 2020-10-09 | 3 | 0 |  Empty highlight entry on match only for some queries  |
| SOLR-14311 | New Feature | Open | 2020-11-04 | 0 | 2 | Shared schema should not have access to core level classes |
| SOLR-9956 | Bug | Open | 2020-11-07 | 1 | 0 | Solr java.lang.ArrayIndexOutOfBoundsException when indexed large amount  |
| SOLR-15326 | Bug | Open | 2021-04-12 | 0 | 0 | Missing one record in solr streaming expressions |
| SOLR-14788 | Task | Open | 2021-06-11 | 0 | 2 | Solr: The Next Big Thing |
| SOLR-14649 | Improvement | Open | 2021-08-19 | 0 | 0 | Package manager should use SHA512, not SHA1 |
| SOLR-15412 | Bug | Open | 2021-09-02 | 0 | 0 | Strict validation on Replica metadata can cause complete outage |
| SOLR-15596 | Bug | Open | 2021-09-28 | 0 | 0 | Ability for packages to be upgraded over major versions |
| SOLR-14593 | Task | Open | 2021-11-02 | 0 | 0 | Package store API to disable file upload over HTTP |

## Batch 06

| Key | Type | Status | Updated | Att | Links | Summary |
|---|---|---|---|---|---|---|
| SOLR-15759 | Bug | Open | 2021-11-17 | 0 | 0 | clean cloud collection terms |
| SOLR-14346 | Bug | Open | 2021-12-11 | 0 | 0 | Solr fails to check zombie server in LbHttpSolrClient |
| SOLR-15096 | Bug | Open | 2022-02-10 | 1 | 0 | [REGRESSION] Collection Delete Performance significantly degraded in Jav |
| SOLR-16100 | Bug | Open | 2022-05-12 | 0 | 0 | Solr tool does not work with custom zkACLProvider |
| SOLR-15998 | Improvement | Open | 2022-05-12 | 0 | 0 | replicaType=TLOG oom killer event occurs |
| SOLR-16509 | Bug | Patch Available | 2023-01-25 | 0 | 1 | All query classes should be immutable |
| SOLR-16725 | Bug | Open | 2023-06-06 | 0 | 0 | CLUSTERSTATUS API output has inconsistent data types for a few values ag |
| SOLR-17122 | Bug | Open | 2024-01-18 | 0 | 0 | Solr Unique, HLL, numBuckets returns incorrect counts |
| SOLR-16139 | Bug | Open | 2024-02-16 | 0 | 2 | [Regression] JSON stat facet functions not working on analysed String (S |
| SOLR-15237 | Bug | Open | 2024-02-27 | 0 | 2 | Distributed search with index sharding is not working with basic authent |

## Batch 07

| Key | Type | Status | Updated | Att | Links | Summary |
|---|---|---|---|---|---|---|
| SOLR-17252 | Bug | Open | 2024-05-02 | 1 | 0 | Invoking nano from the release wizard destroys terminal |
| SOLR-17297 | Bug | Open | 2024-05-29 | 0 | 0 | Classloading issue with plugin and modules |
| SOLR-17281 | Bug | Open | 2024-05-29 | 0 | 0 | Solr elected a down replica as a leader |
| SOLR-17291 | Bug | Open | 2024-05-29 | 0 | 0 | Error opening new searcher Caused by indexDeletionPolicy must not be nul |
| SOLR-16914 | Bug | Open | 2024-12-20 | 5 | 0 | Japanese Synonyms Search not workinh |
| SOLR-3044 | Improvement | Open | 2024-12-20 | 0 | 1 | Incrementally deprecate NamedList & replace with typesafe API |
| SOLR-17143 | Bug | Open | 2025-03-11 | 0 | 1 | Streaming with multiple shards can trigger unexpected IdleTimeout |
| SOLR-17762 | Bug | Open | 2025-05-14 | 5 | 0 | Apache Solr 9.8.1 configuration issues on 2 VM setup |
| SOLR-14996 | Bug | Open | 2025-07-23 | 0 | 0 | Facet incorrect counts when FQ exclusion applied with collapsing |
| SOLR-17370 | Bug | Open | 2025-08-30 | 0 | 0 | Search with facets throws IllegalAccessException if some string fields c |

## Batch 08

| Key | Type | Status | Updated | Att | Links | Summary |
|---|---|---|---|---|---|---|
| SOLR-17788 | Bug | Open | 2025-10-15 | 0 | 0 | Cores Hang During Full Index Bootstrap in Leader/Replica Mode  |
| SOLR-7889 | Improvement | Open | 2025-11-29 | 0 | 2 | Secure ZooKeeper should be easy and the default |
| SOLR-17375 | Improvement | Open | 2026-03-31 | 0 | 0 | Close IndexReader asynchronously on commit for performance |
| SOLR-13360 | Bug | Open | 2026-06-24 | 4 | 1 | StringIndexOutOfBoundsException: String index out of range: -3 |
| SOLR-17433 | Bug | Open | 2026-09-04 | 0 | 1 | SolrStream by default creates a Http2SolrClient that can only stream for |
| SOLR-17767 | New Feature | Open | 2026-09-30 | 0 | 1 | SIP-24: Java Security Manager replacement |
| SOLR-7394 | Bug | Open | 2026-10-02 | 7 | 0 | Shard replicas don't recover after cluster wide restart |
| SOLR-16655 | Bug | Open | 2026-10-09 | 3 | 0 | Indexing nested documents and URPs |
| SOLR-7550 | Bug | Open | 2026-10-14 | 0 | 0 | PeerSync fails if a replica returns 500 error |
| SOLR-18010 | Bug | Open | 2026-10-22 | 1 | 0 | Adding a new role corrupts security.json file |

## Batch 09

| Key | Type | Status | Updated | Att | Links | Summary |
|---|---|---|---|---|---|---|
| SOLR-14200 | Bug | Patch Available | 2026-10-23 | 1 | 0 | Shard Tolerance in Streaming API |
| SOLR-16155 | Bug | Patch Available | 2026-10-23 | 0 | 0 | DocumentBuilder should not include field values in error messages |
| SOLR-5735 | Task | Open | 2026-10-23 | 0 | 1 | ChaosMonkey test timeouts. |
| SOLR-6213 | Bug | Open | 2026-10-23 | 1 | 2 | StackOverflowException in Solr cloud's leader election |
| SOLR-18402 | Improvement | Open | 2026-10-24 | 0 | 1 | Consolidate wasRequestUnsent / wasCommError (retry) logic |
| SOLR-18341 | Improvement | Open | 2026-10-24 | 0 | 3 | SolrJ: let each SolrRequest declare whether it may be retried |
