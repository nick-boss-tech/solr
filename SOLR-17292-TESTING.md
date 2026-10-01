# SOLR-17292 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

`PerReplicaStatesOps.persist(String znode, SolrZkClient zkClient)` retries the
multi-op write on stale state (`NodeExistsException`/`NoNodeException`), but
when retries were exhausted the loop simply ended and the method returned
normally — silently dropping the per-replica-state write. Every caller
(`ZkStateWriter`, `DistributedClusterStateUpdater`, `ZkController`,
`ShardLeaderElectionContextBase`) proceeds assuming the state was persisted,
so cluster state could silently diverge from reality.

Fix: track the last caught stale-state `KeeperException` and throw it after
the loop completes without success, per the ticket ("The correct behavior
should be to throw the relevant exception"). The method signature already
declares `throws KeeperException, InterruptedException`, so no caller changes
are needed. The private `persist(operations, ...)` overload already threw on
failure — only the public retry wrapper swallowed it.

File changed:
- `solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/PerReplicaStatesOps.java`
  (public `persist(String, SolrZkClient)`)

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:solrj-zookeeper:compileJava -Pvalidation.errorprone=true
```

Suggested tests (not written):

1. `persist(znode, zkClient)` where every attempt fails with stale state
   (mocked `SolrZkClient` always throwing `NodeExistsException`/`NoNodeException`
   on multi, `fetch` returning refreshed state) → expect `KeeperException`
   thrown after retries, not a silent return.
2. Regression: a persist that succeeds on a retry still returns normally.
3. Existing coverage: `solr/solrj-zookeeper/src/test/org/apache/solr/common/cloud/TestPerReplicaStates.java`
   (uses a real ZK cluster via SolrTestCaseJ4).

## Patch limits and follow-ups

- **Not compiled or tested.**
- Rethrows the last caught stale-state exception (a `NodeExistsException` or
  `NoNodeException`) rather than inventing a new exception type — this is the
  "relevant exception" the ticket asks for.
- Callers that previously (incorrectly) relied on silent success will now see
  the exception; that is the intended behavior change.
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
