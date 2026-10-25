# SOLR-17895: persistent lock-node lifecycle

**The reported persistent-directory leak is still present in Apache Solr `main` at `07fdebdda54a24085086281da1cde701e122564d` (2026-10-25).** In the distributed collection API path, each command uses an ephemeral lock node beneath a directory chain that Solr creates as persistent. Normal lock release deletes the ephemeral entry, but it does not remove that directory chain. The current runner even has a TODO noting that it may leave lock directories behind when a command targets a collection that does not exist. [1] [2] [3]

## What persists

`ZkController` creates `DistributedCollectionConfigSetCommandRunner` when the Overseer is disabled. The runner anchors collection locks at `/distributedapi/collectionlocks`. [1] [2]

The lock factory builds a separate path for each lock level:

- Collection: `/distributedapi/collectionlocks/<collection>/Locks`
- Shard: `/distributedapi/collectionlocks/<collection>/_<shard>/Locks`
- Replica: `/distributedapi/collectionlocks/<collection>/_<shard>/_<replica>` [3]

For a new, non-mirrored lock, `ZkDistributedLockFactory` first calls `makeLockPath`. That method calls `SolrZkClient.makePath(..., CreateMode.PERSISTENT)`. The current `SolrZkClient` uses Curator's `creatingParentsIfNeeded()` and passes the requested mode to `withMode(createMode)`, so newly created path components are not converted to containers or ephemeral nodes. [4] [5]

The lock entry itself is different: `ZkDistributedLock` creates a child with an `R_` or `W_` prefix using `CreateMode.EPHEMERAL_SEQUENTIAL`. The runner calls `lock.release()` in a `finally` block, and release deletes that lock entry when it owns it. A mirrored nested lock deliberately reuses its caller's existing lock entry and does not delete it; the caller remains responsible for that ephemeral entry. Neither case removes the persistent parent directories. [2] [6]

That means ordinary command completion removes the transient sequential lock child while leaving the persistent hierarchy. Collection deletion also does not remove these paths through the current runner: they live under `/distributedapi/collectionlocks`, separate from the collection znode, and the release path calls `lock.release()` rather than deleting its parents. The runner's TODO explicitly recognizes this leftover-directory behavior. [2]

## Curator changes and current status

Solr's use of Curator as the ZooKeeper client does not by itself fix this leak. The collection lock still uses Solr's `ZkDistributedLock` implementation, and the path factory explicitly requests `PERSISTENT`. The earlier Curator integration changed ZooKeeper client management; it did not change this lock factory's requested node mode. The current Solr build declares Curator framework/client modules, while the Jira discussion notes that `curator-recipes` was not a Solr dependency and that a recipe migration would also need to account for mirrored locks. [4] [5] [7] [8]

The fix proposed in PR #5161 is separate from that migration: it changes new lock directories to ZooKeeper `CONTAINER` nodes, retries creation if an idle container was swept, and removes eligible legacy directories on collection DELETE. The PR is still open and unmerged as of this report, so those changes are not in the cited `main` revision. [9]

## Evidence and Jira draft decision

Amrit reported reproducing the leftover directories after creating and deleting collections with the Overseer disabled. I did not independently run a cluster or tests; this task was limited to source evidence. The current source path is consistent with the reported reproduction. [10]

No Jira comment draft was added. The Jira thread already covers the relevant point: dsmiley clarified that Curator is not itself the cause and suggested container parents; Amrit's October 23 reply explains that the container-node/legacy-cleanup fix is separate from adopting Curator recipes, and identifies lock mirroring as a migration concern. Repeating those findings would not add a new point to the live thread. [11] [12]

## References

[1]: https://github.com/apache/solr/blob/07fdebdda54a24085086281da1cde701e122564d/solr/core/src/java/org/apache/solr/cloud/ZkController.java#L390-L403 "Distributed runner activation when the Overseer is disabled"
[2]: https://github.com/apache/solr/blob/07fdebdda54a24085086281da1cde701e122564d/solr/core/src/java/org/apache/solr/cloud/api/collections/DistributedCollectionConfigSetCommandRunner.java#L73-L83 "Collection lock root and runner lifecycle"
[3]: https://github.com/apache/solr/blob/07fdebdda54a24085086281da1cde701e122564d/solr/core/src/java/org/apache/solr/cloud/ZkDistributedCollectionLockFactory.java#L64-L129 "Collection, shard, and replica lock paths"
[4]: https://github.com/apache/solr/blob/07fdebdda54a24085086281da1cde701e122564d/solr/core/src/java/org/apache/solr/cloud/ZkDistributedLockFactory.java#L37-L71 "Persistent parent-path creation"
[5]: https://github.com/apache/solr/blob/07fdebdda54a24085086281da1cde701e122564d/solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/SolrZkClient.java#L578-L646 "Curator-backed path creation honoring the requested mode"
[6]: https://github.com/apache/solr/blob/07fdebdda54a24085086281da1cde701e122564d/solr/core/src/java/org/apache/solr/cloud/ZkDistributedLock.java#L106-L213 "Ephemeral sequential lock creation and release"
[7]: https://github.com/apache/solr/blob/07fdebdda54a24085086281da1cde701e122564d/solr/core/build.gradle#L115-L121 "Curator framework and client dependencies"
[8]: https://github.com/apache/solr/commit/e5d15cc84f8ad790e1b6c901d74bd86b7c1de17c "SOLR-16116 Curator integration for ZooKeeper client management"
[9]: https://github.com/apache/solr/pull/5161 "SOLR-17895 proposed container-node and legacy cleanup changes; PR remains open"
[10]: https://issues.apache.org/jira/browse/SOLR-17895?focusedCommentId=18127308&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-18127308 "Amrit's reported single-node reproduction"
[11]: https://issues.apache.org/jira/browse/SOLR-17895?focusedCommentId=18031438&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-18031438 "dsmiley on Curator and container nodes"
[12]: https://issues.apache.org/jira/browse/SOLR-17895?focusedCommentId=18130690&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-18130690 "Amrit on recipes, legacy cleanup, and lock mirroring"
