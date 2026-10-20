# Pool assignment: Jira backlog, open Blocker and Critical issues, round 1

Status: DONE (all four slices DONE: claims/pool-csv-critblock-01.md, -02, -03, -04; reports/csv-critblock-01.md to -04.md; closed by vm2 2026-10-20T10:38:25Z)

- Posted by: the phone agent, 2026-10-19, at the owner’s direction: the backlog is prioritized, and the open Blocker and Critical issues go first.
- Source: `material/jira-issues-csv/solr-jira-metadata-2026-10-18-1792393324869.csv` on this branch. Of its 4,078 open rows, exactly 100 are Blocker (16) or Critical (84). They are listed below oldest first in four slices of 25.
- This round is carved out of the backlog gating in `assignments/pool-csv-backlog-round-1.md`: these slices are claimable without waiting for the JSON queue to drain. The rest of that assignment keeps its gating.
- Overlap: SOLR-5850, SOLR-7517, SOLR-12315 and SOLR-15237 already have records in `material/skip-audit/queue-current.json` (the first two also appear in the remediation file). Start from those records instead of re-opening the questions they already settled.
- Claim: one slice at a time, `claims/pool-csv-critblock-<NN>.md`, first post wins. Any Linux host may claim; there are no host exclusions in this round.

## Per-ticket work (evidence first; a classification without this evidence does not count)

1. Re-read the ticket on Jira in this pass: status, description, comments, linked PRs and commits. The CSV is a snapshot from 2026-10-18, not evidence.
2. Check the issue against current upstream main: read the code path the ticket names, with file and line, and where the ticket describes reproducible behavior, attempt the reproduction and record the result.
3. Classify the ticket: STILL-PRESENT, ALREADY-FIXED (name the fixing commit or PR), OBSOLETE (say why: API removed, feature replaced, report against an unshipped version), or NOT-REPRODUCIBLE (record exactly what was tried).
4. For STILL-PRESENT: implement the fix on a branch named for the ticket, run the gate at its head, and write the receipt in `receipts/`. A PR draft per the formula may be written to `drafts/`; opening stays with the owner.
5. For the other three classes: the product is the evidence record itself, in the slice report. Nothing is posted to Jira; a closeout comment draft may be included in the report for the owner.

Deliverable per slice: `reports/csv-critblock-<NN>.md`, one row per ticket with its classification and the evidence pointers (Jira read date, file and line, reproduction result, or fixing commit), plus any branches and receipts for STILL-PRESENT items. The claim goes DONE in the same push as the report.

Limits: no Jira or GitHub posts or comments, no PR openings, no force push. Do not lower a ticket’s classification to clear the slice faster; an honest NOT-REPRODUCIBLE with the attempts recorded is a finished item.

## Slices (oldest first)

### Slice 01 (25 tickets)

- SOLR-2908 (Critical, created 2011-11-19, PR available: No): To push the terms.limit parameter from the master core to all the shard cores.
- SOLR-3044 (Critical, created 2012-01-18, PR available: No): Incrementally deprecate NamedList & replace with typesafe API
- SOLR-3684 (Critical, created 2012-07-27, PR available: No): Frequently full gc while do pressure index
- SOLR-4165 (Critical, created 2012-12-11, PR available: No): Queries blocked when stopping a node
- SOLR-4180 (Critical, created 2012-12-13, PR available: No): StatisticsHandler multicire support
- SOLR-4285 (Critical, created 2013-01-08, PR available: No): Solr mangles distributed query parameters
- SOLR-4690 (Critical, created 2013-04-08, PR available: No): Highlighting Doesn't works when boost is used along with query
- SOLR-5735 (Critical, created 2014-02-16, PR available: No): ChaosMonkey test timeouts.
- SOLR-5821 (Critical, created 2014-03-06, PR available: No): Search inconsistency on SolrCloud replicas
- SOLR-5850 (Critical, created 2014-03-11, PR available: No): Race condition in ConcurrentUpdateSolrServer
- SOLR-6213 (Critical, created 2014-06-30, PR available: No): StackOverflowException in Solr cloud's leader election
- SOLR-6345 (Critical, created 2014-08-08, PR available: No): collapsingQParserPlugin degraded performance when using tagging
- SOLR-6420 (Critical, created 2014-08-24, PR available: No): ADDREPLICA can add a replica that reports green without actually replicating
- SOLR-6683 (Critical, created 2014-10-31, PR available: No): Need a configurable parameter to control the doc number between peersync and the snapshot pull recovery
- SOLR-6816 (Critical, created 2014-12-03, PR available: No): Review SolrCloud Indexing Performance.
- SOLR-7351 (Critical, created 2015-04-06, PR available: No): MoreLikeThis component doesn't work with dates
- SOLR-7394 (Critical, created 2015-04-15, PR available: Yes): Shard replicas don't recover after cluster wide restart
- SOLR-7517 (Critical, created 2015-05-09, PR available: No): When you can't persist a core.properties file we simply log an error and continue.
- SOLR-7550 (Critical, created 2015-05-15, PR available: Yes): PeerSync fails if a replica returns 500 error
- SOLR-7889 (Critical, created 2015-08-07, PR available: No): Secure ZooKeeper should be easy and the default
- SOLR-7926 (Critical, created 2015-08-14, PR available: No): Hit highlighting with EdgeNGramFilterFactory
- SOLR-8028 (Critical, created 2015-09-10, PR available: No): ConcurrentModificationException thrown from JavaBinCodec
- SOLR-8096 (Critical, created 2015-09-25, PR available: No): Major faceting performance regressions
- SOLR-8331 (Critical, created 2015-11-23, PR available: No): CorruptIndexException after auto commit
- SOLR-8417 (Critical, created 2015-12-15, PR available: No): HttpSolrCall occur a Infinite loop request process if the collection is totally not loaded 

### Slice 02 (25 tickets)

- SOLR-8767 (Critical, created 2016-03-01, PR available: No): RealTimeGetComponent and stored/copyField exclusion
- SOLR-8887 (Critical, created 2016-03-23, PR available: No): Solr Security features cannot export the internal, deprecated DefaultHttpClient class as part of their user facing API.
- SOLR-8905 (Critical, created 2016-03-25, PR available: No): AutoAddReplicas feature has bug that local coreproperties file was deleted by mistake
- SOLR-9138 (Critical, created 2016-05-20, PR available: No): CDATA should be requisite for text/string field type in xml-format result set
- SOLR-9170 (Critical, created 2016-05-28, PR available: No): Need detailed diagnostics for Zookeeper Connection Issues
- SOLR-9956 (Critical, created 2017-01-11, PR available: No): Solr java.lang.ArrayIndexOutOfBoundsException when indexed large amount of documents
- SOLR-10928 (Critical, created 2017-06-21, PR available: No): Support elevate.q in QueryElevationComponent
- SOLR-10950 (Critical, created 2017-06-24, PR available: No): Support context filtering for FuzzyLookupFactory
- SOLR-11310 (Critical, created 2017-09-01, PR available: No): QueryElevation doesn't work with Solr LTR 
- SOLR-11330 (Critical, created 2017-09-06, PR available: No): Solr arabic search
- SOLR-11650 (Critical, created 2017-11-16, PR available: No): Credentials used for BasicAuth displayed in clear text on slave nodes
- SOLR-11668 (Critical, created 2017-11-23, PR available: No): Lookup not supported at this time orStackOverflowError after upgradin from 6.3.0
- SOLR-11972 (Critical, created 2018-02-12, PR available: No): range of numeric values with unit in Solr
- SOLR-12069 (Critical, created 2018-03-08, PR available: No): Default operator parameter q.op ignored.
- SOLR-12315 (Critical, created 2018-05-05, PR available: No): the number of docs in each group depends on rows
- SOLR-12415 (Critical, created 2018-05-29, PR available: No): Solr Loadbalancer client LBHttpSolrClient not working as expected, if a Solr node goes down, it is unable to detect when it become live again due to 404 error
- SOLR-12743 (Critical, created 2018-09-05, PR available: No): Memory leak introduced in Solr 7.3.0
- SOLR-12801 (Critical, created 2018-09-24, PR available: No): Fix the tests, remove BadApples and AwaitsFix annotations, improve env for test development.
- SOLR-12833 (Blocker, created 2018-10-04, PR available: No): Use timed-out lock in DistributedUpdateProcessor
- SOLR-13360 (Critical, created 2019-04-01, PR available: Yes): StringIndexOutOfBoundsException: String index out of range: -3
- SOLR-13460 (Critical, created 2019-05-10, PR available: No): org.apache.solr.common.SolrException: Exception writing document id sitecore://master/{a55da3d7-04f1-47bd-8303-70c61fbeefab}?lang=en&ver=1&ndx=sitecore_master_index to the index; possible analysis error.
- SOLR-13770 (Critical, created 2019-09-17, PR available: No): Solr Fails to recover Replica(s) after Restart due to Deep Nested Directories Created During Recovery inside TLOG
- SOLR-13789 (Critical, created 2019-09-24, PR available: No): Solr core reload with config change upstream put solr instance in bad state
- SOLR-13850 (Critical, created 2019-10-16, PR available: No): Atomic Updates with PreAnalyzedField
- SOLR-14200 (Critical, created 2020-01-21, PR available: No): Shard Tolerance in Streaming API

### Slice 03 (25 tickets)

- SOLR-14311 (Critical, created 2020-03-07, PR available: No): Shared schema should not have access to core level classes
- SOLR-14346 (Critical, created 2020-03-18, PR available: No): Solr fails to check zombie server in LbHttpSolrClient
- SOLR-14593 (Critical, created 2020-06-25, PR available: No): Package store API to disable file upload over HTTP
- SOLR-14638 (Critical, created 2020-07-08, PR available: No): Edismax boost function zero score
- SOLR-14649 (Critical, created 2020-07-14, PR available: No): Package manager should use SHA512, not SHA1
- SOLR-14788 (Critical, created 2020-08-28, PR available: No): Solr: The Next Big Thing
- SOLR-14833 (Critical, created 2020-09-05, PR available: No):  Empty highlight entry on match only for some queries 
- SOLR-14996 (Critical, created 2020-11-10, PR available: No): Facet incorrect counts when FQ exclusion applied with collapsing
- SOLR-15096 (Critical, created 2021-01-21, PR available: No): [REGRESSION] Collection Delete Performance significantly degraded in Java 11 v 8
- SOLR-15237 (Critical, created 2021-03-09, PR available: No): Distributed search with index sharding is not working with basic authentication plugin enabled
- SOLR-15326 (Critical, created 2021-04-08, PR available: No): Missing one record in solr streaming expressions
- SOLR-15412 (Critical, created 2021-05-18, PR available: No): Strict validation on Replica metadata can cause complete outage
- SOLR-15596 (Critical, created 2021-08-19, PR available: No): Ability for packages to be upgraded over major versions
- SOLR-15759 (Critical, created 2021-11-02, PR available: No): clean cloud collection terms
- SOLR-15998 (Critical, created 2022-02-09, PR available: No): replicaType=TLOG oom killer event occurs
- SOLR-16100 (Critical, created 2022-03-15, PR available: No): Solr tool does not work with custom zkACLProvider
- SOLR-16139 (Critical, created 2022-04-01, PR available: No): [Regression] JSON stat facet functions not working on analysed String (SortableTextField)
- SOLR-16155 (Critical, created 2022-04-18, PR available: Yes): DocumentBuilder should not include field values in error messages
- SOLR-16450 (Blocker, created 2022-10-07, PR available: No): Proper handling on watcher registration failure in `ZkStateReader#waitForState
- SOLR-16509 (Critical, created 2022-10-28, PR available: No): All query classes should be immutable
- SOLR-16655 (Critical, created 2023-02-11, PR available: Yes): Indexing nested documents and URPs
- SOLR-16725 (Critical, created 2023-03-29, PR available: No): CLUSTERSTATUS API output has inconsistent data types for a few values against the newly restored collection
- SOLR-16914 (Critical, created 2023-07-31, PR available: No): Japanese Synonyms Search not workinh
- SOLR-17122 (Critical, created 2024-01-18, PR available: No): Solr Unique, HLL, numBuckets returns incorrect counts
- SOLR-17143 (Critical, created 2024-01-30, PR available: No): Streaming with multiple shards can trigger unexpected IdleTimeout

### Slice 04 (25 tickets)

- SOLR-17252 (Critical, created 2024-04-23, PR available: No): Invoking nano from the release wizard destroys terminal
- SOLR-17281 (Critical, created 2024-05-07, PR available: No): Solr elected a down replica as a leader
- SOLR-17291 (Critical, created 2024-05-14, PR available: No): Error opening new searcher Caused by indexDeletionPolicy must not be null
- SOLR-17297 (Critical, created 2024-05-17, PR available: No): Classloading issue with plugin and modules
- SOLR-17370 (Critical, created 2024-07-13, PR available: Yes): Search with facets throws IllegalAccessException if some string fields contain numeric values
- SOLR-17375 (Critical, created 2024-07-22, PR available: No): Close IndexReader asynchronously on commit for performance
- SOLR-17433 (Critical, created 2024-08-31, PR available: Yes): SolrStream by default creates a Http2SolrClient that can only stream for 60 seconds
- SOLR-17463 (Blocker, created 2024-09-26, PR available: No): Solr hanging at SolrCore.getSearcher
- SOLR-17546 (Blocker, created 2024-11-06, PR available: No): Update .htaccess for Ref Guide for removed pages
- SOLR-17762 (Critical, created 2025-05-14, PR available: No): Apache Solr 9.8.1 configuration issues on 2 VM setup
- SOLR-17767 (Critical, created 2025-05-22, PR available: Yes): SIP-24: Java Security Manager replacement
- SOLR-17788 (Critical, created 2025-06-16, PR available: Yes): Cores Hang During Full Index Bootstrap in Leader/Replica Mode 
- SOLR-17840 (Blocker, created 2025-08-07, PR available: No): LTR is broken after Lucene 10 upgrade
- SOLR-17842 (Blocker, created 2025-08-07, PR available: No): Publish benchmarks for important features
- SOLR-17844 (Blocker, created 2025-08-07, PR available: No): Investigate TestFieldCacheSortRandom failures
- SOLR-17988 (Blocker, created 2025-11-11, PR available: No): Ensure Solr 10 Release Conveys Changes Users Need To Be Aware Of For Properties!
- SOLR-18010 (Critical, created 2025-12-02, PR available: Yes): Adding a new role corrupts security.json file
- SOLR-18240 (Blocker, created 2026-05-14, PR available: No): Complete Major changes and Upgrade Notes in RefGuide for 9.11.0
- SOLR-18297 (Blocker, created 2026-06-30, PR available: Yes): Upgrade Jetty to v12.1.14
- SOLR-18398 (Blocker, created 2026-08-23, PR available: No): Migrate SolrQueryResponse and responseHeader from NamedList to LinkedHashMap
- SOLR-18446 (Blocker, created 2026-09-14, PR available: No): Remove "collection" param routing support in HttpSolrCall
- SOLR-18458 (Blocker, created 2026-09-19, PR available: No): Drop debugQuery=true 
- SOLR-18486 (Blocker, created 2026-09-28, PR available: No): Complete Major changes and Upgrade Notes in RefGuide for 10.1.0
- SOLR-18501 (Blocker, created 2026-10-04, PR available: No): Solr MCP Project IP Clearance
- SOLR-18552 (Blocker, created 2026-10-16, PR available: No): Solr 9: set MMapDirectory.sharedArenaMaxPermits=64
