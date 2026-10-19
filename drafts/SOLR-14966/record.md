# Skip record: SOLR-14966

- Ticket: SOLR-14966, "Solr should use cache collection state to validate _stateVer_ sent by SolrJ". JIRA status Open, resolution none, fix versions none, created 2020-10-27, updated 2024-11-19. Linked SOLR-14985 (Closed). Checked 2026-10-19 UTC.
- Verdict: keep-open (changed from close-fixed; this redo supersedes the recorded verdict).
- Draft branch: solr-14966-draft-open (the earlier close-fixed branch solr-14966-draft-close is not updated).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14966-vm2.md).

## Ticket reading

When a request carries the stateVer parameter, Solr checks the collection version the client sent against its own state. The reporter says the check is meant to use the cached state (about 2 seconds) but calls ZooKeeper each time, because clusterState.getCollectionOrNull(coll) does not use the cache. The suggested change is getCollectionOrNull(coll, true), which reads the cache and goes to ZooKeeper only after the TTL. Two comments: a 2022 note that the code links are broken; a 2024 maintainer comment (dsmiley) calling it "more of a minor optimization opportunity than a bug".

## Code reading

- solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java:826-843 (origin/main 498a09aea97): checkStateVersionsAreValid calls ZkStateReader.compareStateVersions for each stateVer pair. This is the path the ticket describes.
- solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/ZkStateReader.java:537-538: compareStateVersions begins with `DocCollection collection = clusterState.getCollectionOrNull(coll);`, the no-flag overload.
- solr/solrj/src/java/org/apache/solr/common/cloud/ClusterState.java:128-130: getCollectionOrNull(name) calls getCollectionOrNull(name, false). Lines 142-144: that calls ref.get(allowCached) with allowCached false. So the stateVer comparison does not use the cached collection state. That matches the ticket.
- HttpSolrCall.java:347: the other lookup in this file (resolveDocCollection or the core lookup) already uses getCollectionOrNull(collectionName, true). The stateVer path was not changed in the same way.

Commands at the pinned head:
- git grep -n 'compareStateVersions' origin/main: HttpSolrCall.java:839 (the call), ZkStateReader.java:537 (the definition, in solrj-zookeeper).
- git grep -n 'getCollectionOrNull(collectionName, true)' origin/main -- solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java: line 347 (other path).
- git log -S 'getCollectionOrNull(collectionName, true)' origin/main -- solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java: fda4c40f53c (2025-09-05), 09e8d0cb0d9 (2024-04-10), b212f2e9b7b (2018-02-25). None of these is a change to the stateVer comparison.

## History

- Commits on origin/main naming SOLR-14966: none.
- SOLR-14985 (linked, Closed): not traced in this pass.

## Reproduction attempts

Not attempted. The claim to test is a count of ZooKeeper reads per request that carries stateVer, with and without the cache flag. That needs a cluster run under request load, which was not set up in this pass.

## Gap analysis

The code confirms the ticket's mechanism: the stateVer comparison reads the collection without the cache, so each such request can reach ZooKeeper. Two things are open. First, the count of ZooKeeper reads and the latency effect under load is not measured. Second, the maintainer's view is that this is an optimization with a staleness trade-off: using the cache allows up to two seconds of stale state in the version check, and whether that is acceptable is a design decision.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-fixed does not hold: no commit changed the stateVer comparison, and the path the ticket names still calls the uncached overload at ZkStateReader.java:538. pr-needed would need a reproduction, which this pass did not run; the rubric says a pr-needed draft says we reproduced it. The missing pieces are (1) a measured count of ZooKeeper reads per stateVer request, with and without the cached lookup, and (2) an owner decision on accepting up to two seconds of staleness in the version check.

## Limits

This record does not measure ZooKeeper load. It does not evaluate the staleness trade-off in production. It does not read SOLR-14985.
