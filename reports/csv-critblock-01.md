# CSV Blocker and Critical slice 01 (vm4)

Assignment: `assignments/pool-csv-critical-blocker-round-1.md`, slice 01. Claim: `claims/pool-csv-critblock-01.md`.
Basis: JIRA REST `issues.apache.org/jira/rest/api/2/issue/<KEY>` read 2026-10-20 (UTC) for status, links and comments. Upstream reference `c8ab71430f` (apache/main). The local clone is shallow, so upstream history was checked through the GitHub API (`repos/apache/solr/compare`, `search/commits`).

Status: PARTIAL. Rows marked PENDING have not been classified yet. The claim stays open until every row has a class and evidence.

| Ticket | Summary | Class | Evidence | Next step |
| --- | --- | --- | --- | --- |
| SOLR-2908 | Push terms.limit from master to shard cores | NOT-REPRODUCIBLE | Code path read at base: `TermsComponent.java` lines 436 to 446. For the default sort by count, the shard request sets `TERMS_LIMIT` to -1 on purpose, so shards return full counts and the master applies the limit at merge (line 527). The ticket's `-1` is the designed shard request. No multi-shard run. Link SOLR-13337 (Closed) not yet read | Read SOLR-13337; run a two-shard terms request with terms.limit to confirm the master limit |
| SOLR-3044 | Incrementally deprecate NamedList | PENDING | Open. Link: Duplicate SOLR-14730 (Resolved) | Read SOLR-14730 and whether it covers this |
| SOLR-3684 | Frequent full GC under index pressure | PENDING | Open, 19 comments, no links | Read comments for a concrete config or fix |
| SOLR-4165 | Queries blocked when stopping a node | ALREADY-FIXED | Duplicate of SOLR-4421 (Resolved). Fix commit `384d42b5e3` ("SOLR-4421,SOLR-4165: On CoreContainer shutdown, all SolrCores should publish their state as DOWN"), an ancestor of `c8ab71430f` (compare status: ahead). JIRA status is Reopened, so reproduction is not done | Confirm the reopen reason in the comments; if none, keep ALREADY-FIXED with that caveat |
| SOLR-4180 | StatisticsHandler on multicore | PENDING | Open, no comments, no links | Check `StatisticsHandler` wiring in `solr-jetty` config at base |
| SOLR-4285 | Distributed query parameters mangled | PENDING | Open, no links | Check parameter forwarding in the shard request path |
| SOLR-4690 | Highlighting fails when a boost is used | PENDING | Open, no links | Reproduce with a boosted query in a test core |
| SOLR-5735 | ChaosMonkey test timeouts | PENDING | Open. Link: Reference SOLR-6002 (Resolved), commits `b74b88a823`, `678758ae57` in base | Check whether the timeout is still reported and whether the SOLR-6002 changes address it |
| SOLR-5821 | Search inconsistency across SolrCloud replicas | PENDING | Reopened, no links | Read comments; check replica routing and commit semantics |
| SOLR-5850 | Race in ConcurrentUpdateSolrServer | PENDING | Open. Link: Reference SOLR-7571 (Resolved). Overlap: skip queue record exists | Start from the skip queue record, do not re-open the question |
| SOLR-6213 | StackOverflow in leader election | PENDING | Open. Duplicates SOLR-5692, SOLR-6285 (both Resolved). Base `LeaderElector.joinElection` still calls itself (lines 209 to 222) | Check whether the recursion is bounded now, and whether SOLR-6285 changed it |
| SOLR-6345 | collapsingQParserPlugin slow with tagging | PENDING | Open, no links | Reproduce with tagged collapse and facet.query |
| SOLR-6420 | ADDREPLICA reports green without replicating | PENDING | Open, no links | Check replica state transitions in the add-replica path |
| SOLR-6683 | Configurable peer-sync vs snapshot threshold | PENDING | Open, no links. Base `PeerSync.java` has no hit for a gap or 100 threshold on grep | Read `RecoveryStrategy` for the peer-sync decision |
| SOLR-6816 | Review SolrCloud indexing performance | PENDING | Open umbrella, 36 comments, Reference SOLR-7344 (Resolved) | Umbrella review, not a defect; decide the class on the evidence |
| SOLR-7351 | MoreLikeThis does not work with dates | PENDING | Open. Link: Reference SOLR-4860 (Resolved) | Reproduce with a TrieDate field in MLT |
| SOLR-7394 | Replicas do not recover after cluster restart | PENDING | Open, updated 2026-10-02, no links | Read comments and current recovery logic |
| SOLR-7517 | core.properties write failure only logged | PENDING | Base `CorePropertiesLocator.writePropertiesFile` (lines 96 to 108) logs and throws a `SolrException` (BAD_REQUEST), so the failure is no longer silent at base. Fixing commit not identified (GitHub search by key returned nothing) | Find the commit that added the throw (commit path history via the API) |
| SOLR-7550 | PeerSync fails when a replica returns 500 | PENDING | Open, updated 2026-10-14, no links | Check PeerSync error handling |
| SOLR-7889 | Secure ZooKeeper should be easy and the default | PENDING | Open. Links: Container SOLR-7236 (Closed), dependent SOLR-8346 (Closed). Base `ZkController` imports `SecurityAwareZkACLProvider` and `DefaultZkACLProvider` | Read the default ACL selection; likely an owner-level default decision |
| SOLR-7926 | Hit highlighting with EdgeNGram | PENDING | Reopened, no links | Reproduce with the EdgeNGram field type from the ticket |
| SOLR-8028 | ConcurrentModificationException in JavaBinCodec | PENDING | Open, no links. Base `JavaBinCodec.writeArray` is reached from `writeVal` (lines 382, 386) | Read `writeArray` and the iteration it uses |
| SOLR-8096 | Major faceting performance regressions | PENDING | Open. Links: Incorporates SOLR-8466 (Closed), Reference SOLR-9176 (Closed), SOLR-7730 (Resolved), Regression LUCENE-5666 (Closed) | Check whether facet.method=uif and the multi-valued optimization are back at base |
| SOLR-8331 | CorruptIndexException after auto commit | PENDING | Open, no links | Read comments; check for a later fix in the commit path |
| SOLR-8417 | Infinite loop when no core of a collection loads | PENDING | Base `HttpSolrCall.java` lines 950 to 958 cap forwards at `totalReplicas` and throw `INVALID_STATE`, so the loop is bounded at base. Fixing commit not identified | Find the commit that added the cap, and reproduce the all-cores-unloaded case if cheap |
