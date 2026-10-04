# SOLR-16437 - hypothetical-reproduction handoff

**Read this first: the fix and test on this branch were written without being compiled or run.** The audit pipeline has no Gradle access. Treat both as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-16437 - "ADDREPLICAPROP does not sanity-check inputs" (Jason Gerlowski, 2022). Reopened from a skip by the skip audit (audit-1).
- Branch: `solr-16437-submit` off `apache/solr` main `e2cdb2d7e8ae`

## The bug, as understood
`AddReplicaPropCmd.call` only checked the required parameters and then offered the state update to the overseer (or the distributed updater). `ReplicaMutator.addReplicaProperty` does throw BAD_REQUEST for an unknown replica, but that runs asynchronously in the state-update path, so the caller already got a 200 and the error ends up only in the log.

## What the branch changes
- `AddReplicaPropCmd.call` now looks the collection, shard and replica up in the current cluster state and throws `SolrException(BAD_REQUEST, "Could not find collection/shard/replica ..., no action taken.")` before submitting the update.
- Test: `CollectionsAPISolrJTest.testAddReplicaPropRejectsUnknownReplica` expects a 400 `RemoteSolrException` for a bad replica, and a `RemoteSolrException` for a bad shard and a bad collection.

## Guesses to verify first
1. Exceptions thrown from `call()` surface to the client as the same error code (the command runner may map them to 500 or wrap them); only the bad-replica case asserts 400.
2. `ZkStateReader.getClusterState()` is current enough (a replica created a moment ago might not be visible yet). The existing happy-path test (`testAddAndDeleteReplicaProp`) waits for the collection first, so it should be fine.
3. Whether to validate the `property`/`property.value` content (the ticket title says "inputs" generally, and "other APIs"); only target existence is checked here.
4. Case sensitivity: `ReplicaMutator` compares replica names with `equalsIgnoreCase` in places; `Slice.getReplica` is exact. Check that the callers do not rely on a case-insensitive name.

## Verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.cloud.CollectionsAPISolrJTest"
```
Fail-before: revert `CollApiCmds.java` only; the new test should fail because no exception is thrown.

## Not done
No JIRA comment, no PR.
