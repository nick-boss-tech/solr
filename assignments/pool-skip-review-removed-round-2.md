# Pool assignment: Verify removed-feature tickets, round 2

Status: OPEN

- Slug: `pool-skip-review-removed-round-2`.
- Capability tags: review.
- Intended host: any pool Linux host (see hosts/pool-linux.md). Claim under `claims/` before starting.
- Posted by: main agent coordinator, 2026-10-18, at the direction of Nick.

### Items

1. **Check each ticket.** For each row, apply the Method in the Decisions table and record the verdict. Output: one row per ticket in the deliverable.
2. **Record.** Write results into the deliverable as you go, so a stop keeps partial results. Name any ticket you did not reach.

### Tickets

| Ticket | Summary | Skip note (from queue) | Verdict |
| --- | --- | --- | --- |
| SOLR-9826 | Shutting down leader when it's sending updates makes another active node go into recovery | LIR removed, executor no longer interrupted | |
| SOLR-9872 | raf.setLength(0) in transactionLog is unreachable | obsolete: TransactionLog constructor rewritten | |
| SOLR-9913 | LIR should continue on SocketTimeoutException | LIR removed from Solr 9+, obsolete | |
| SOLR-10041 | Leader Initiated Recovery happening when the leader also fails to index the content | LIR removed from Solr 9+, obsolete | |
| SOLR-10201 | Admin UI: Add Collection "creates collection", "Connection to Solr lost", when replicationFactor1 | Admin UI angular, obsolete | |
| SOLR-10326 | AutoAddReplicas should use previous state to populate shared data dirs if SolrCore is not available. | autoAddReplicas rewritten; obsolete | |
| SOLR-10497 | angular error - ngRepeat:dupes. Prevents proper page loading. | Admin UI angular, obsolete UI | |
| SOLR-10511 | Unable to view replication status | Admin UI angular, obsolete UI | |
| SOLR-10709 | get "unexpected docvalues type NUMERIC for field" for query q=field:* if field is defined as docV... | obsolete: Trie gone; PointField/FieldType use existence queries for field:* | |
| SOLR-10889 | Stale zookeper information is used during failover check | obsolete: OverseerAutoReplicaFailoverThread removed (autoAddReplicas rewrite) | |
| SOLR-10904 | Unnecessary waiting during failover in case of failed core creation | obsolete: failover thread replaced by autoAddReplicas rewrite | |
| SOLR-10959 | o.a.s.c.MapSerializable missing in solr-core maven artifact | packaging question for 6.6, obsolete | |
| SOLR-11009 | FacetModule throws NullPointerException when all shard requests fail with shards.tolerant=true | obsolete: FacetModule now null-checks mcontext and merger when all shards fail | |
| SOLR-11246 | MoveReplica API does not preserve replica type information | obsolete: MoveReplicaCmd on main already passes REPLICA_TYPE (lines 247, 370) | |
| SOLR-11378 | Solr can leak transaction logs if a commit happens during unload | no repro/isolating test; autoscaling test removed; similar to SOLR-15695 (core unload vs commit tlog leak);... | |
| SOLR-11435 | Replicas failed to be deleted can overwrite replicas of recreated collections | obsolete: legacyCloud/baseUrl-comparison model was removed from SolrCloud; ticket scenario (deleted replica... | |
| SOLR-11537 | Add Replica after upgrading from Solr 6.2.1 to Solr 7.1.0 throws InvalidClassException | obsolete: Overseer no longer uses Java serialization for SolrResponse (SimpleOrderedMap serialVersionUID mi... | |
| SOLR-11659 | JMX Metrics ObjectName needs comma escaping for /solr/col1,col2/select scenario | obsolete: JMX metrics reporter (JmxObjectNameFactory) no longer exists on main. | |
| SOLR-11776 | HttpSolrClient should handle SocketException:broken pipe | obsolete: leader-initiated recovery (LIR) was removed from SolrCloud; broken-pipe handling premise no longe... | |
| SOLR-11787 | StackOverflowError in leader election | no repro; log4j-frame StackOverflowError in 7.1 leader election, probable duplicate of SOLR-6213; LIR/elect... | |
| SOLR-11955 | Clean up usage of graph token filters in shipped schemas | obsolete: no shipped non-test schema/configset on main chains SynonymGraphFilter into WordDelimiterGraphFil... | |
| SOLR-12166 | Race condition in rejoinElection and registering replica | revisited in session 3: LIR/election race, LIR removed in Solr 9 | |
| SOLR-12446 | Misleading client response when IllegalArgumentException is thrown in SolrIndexSearcher | partly obsolete / design call: LBSolrClient on main already chains the real shard exception as the cause of... | |
| SOLR-12585 | Solr fails even ZK quorum has majority | obsolete: ZK 3.4 client DNS behavior; ZK 3.9 HostProvider skips unresolvable hosts | |
| SOLR-12645 | MemClassLoader#loadResource() is broken | obsolete: MemClassLoader class no longer exists on main (no file match); plugin loading rewritten | |
| SOLR-12646 | Splitting a shard that has been touched by 6x fails with 7x. | obsolete: 6.x-7.x upgrade path, autoscaling Variable$Type NoClassDefFound; autoscaling removed in 9.x, 6.x ... | |
| SOLR-13304 | PreanalyzedField#createField swallows Exception | target code removed: PreanalyzedField deleted via SOLR-17839; reporter/assignee had a 2019 patch, 7+ yrs stale | |
| SOLR-13481 | Re-try of solr request will not happen with different live servers, if one request throws Exception | Stale: target class LBHttpSolrClient no longer exists on main (removed via SOLR-17962/SOLR-18188); retry lo... | |
| SOLR-13501 | Use Http2SolrClient in DUP and remove ConcurrentUpdateHttp2SolrClient. | already-removed class; 2019 maintainer refactoring proposal, benchmark-gated, design-heavy | |
| SOLR-13846 | PreemptiveBasicAuthClientBuilderFactory use of static code blocks makes it unreliable in tests | Stale: subject class PreemptiveBasicAuthClientBuilderFactory was removed from the codebase (deleted by SOLR... | |
| SOLR-14023 | tests.src.home points at ${user.dir}" for tests (?!) | Design-heavy test-infra redesign proposal from active maintainer Dawid Weiss (2019, stale); the user.dir de... | |
| SOLR-14117 | remove java serialization from AnalyticsShardResponseParser.java | stale: AnalyticsShardResponseParser removed with analytics component (SOLR-16661); no ObjectInputStream lef... | |
| SOLR-15297 | Creating a collection with TLOG replicas fails with placement rules | feature for deprecated rules framework; obsolete - placement-plugin framework (current main) already places... | |
| SOLR-15310 | HttpClientUtil interceptors are shared by multiple CoreContainer in same Test JVM | Obsolete/stale: HttpClientUtil static interceptor registry no longer exists in current main (removed by Jet... | |
| SOLR-15350 | fieldCache stats contain entries for other things (a regression) | stale: /admin/mbeans removed from main by SOLR-17853 (Oct 2025); buggy SolrInfoMBeanHandler code no longer ... | |
| SOLR-15690 | Updates in datasource.xml are not reflecting in Solr Console | Stale DIH ticket: DataImportHandler removed in Solr 9 (no code surface on current main); 0 comments, no rep... | |
| SOLR-15704 | Delta import with multiple entities: working partially | stale: DataImportHandler removed from Solr 9; no fix possible in this repo | |
| SOLR-15879 | Memory leak in QueryResultsCache | stale: FastLRUCache maxRamMB-honoring bug on Solr 7.5; class removed from main in 9.x, no repro confirmatio... | |
| SOLR-17763 | Solr container was not available: CoreContainer is either not initialized or shutting down | stale symptom report on 9.6.1; no comments, no repro; reported latch-wait code path removed by SOLR-17118 i... | |
| SOLR-17993 | SolrCoreMetric.CLOUD_CORE_PATTERN does not support split shards | obsolete on main: SolrCoreMetric/CLOUD_CORE_PATTERN gone after OTel metrics refactor; see research note | |

### Decisions

| Decision | Answer or default |
| --- | --- |
| Head | decided: the fork origin/main sha 498a09aea9 at posting time. Record the sha you read. Do not move it. |
| Method | decided: read the ticket description (anonymous JIRA REST at issues.apache.org/jira/rest/api/2/issue/KEY, read only), then check the named code path on the pinned head with `git grep` or `git show`. Cite file path and line. |
| Verdict set | decided: exactly one of: gone (cite the removal or the grep showing no hits), present (cite the code path still there), fixed (cite the commit that changed it), unclear (say what is missing). No other verdicts. |
| Unclear | default: mark unclear and state the missing piece; do not guess a verdict. |
| Gradle | decided: none. No builds or test runs in a review item. |
| Closing or commenting | decided: none. No JIRA, GitHub or email posts. |
| Scope | decided: the rows in this table. |

### Deliverable

`reports/2026-10-18-skip-repro/B2-removed-round-2.md`: one row per ticket with verdict and the cited path or grep.

### Limits

- No PR, comment, JIRA or email. No force push.
- Commit as Nick Shanin with no trailers. No em dashes.

