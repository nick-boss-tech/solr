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

Test added (round-3 patch pass, **not compiled or run**):

- `TestPerReplicaStates#testPersistRetriesOnStaleState` (real ZK via the
  existing cluster setup, using the package-private `PerReplicaStatesOps(Function)`
  constructor): (1) an operation computed from a stale view re-adds an existing
  node, then succeeds with the next version after the retry refreshes it;
  (2) an operation that is stale on every attempt now throws
  `KeeperException.NodeExistsException` instead of returning silently.

Queued for the verification run: `org.apache.solr.common.cloud.TestPerReplicaStates`
(module `:solr:solrj-zookeeper`), with Spotless.

## Patch limits and follow-ups

- **Not compiled or tested.**
- Rethrows the last caught stale-state exception (a `NodeExistsException` or
  `NoNodeException`) rather than inventing a new exception type — this is the
  "relevant exception" the ticket asks for. An error is logged first with the
  znode and attempt count, and the refresh after the final failed attempt is
  skipped.
- Callers that previously (incorrectly) relied on silent success will now see
  the exception; that is the intended behavior change. The call sites
  (`ZkStateWriter`, `DistributedClusterStateUpdater`,
  `ShardLeaderElectionContextBase`, `ZkController`) were not traced for how each
  handles it; describe that in the PR.
- Changelog fragment added: `changelog/unreleased/SOLR-17292.yml`.
- Remove this file before opening the upstream PR.
