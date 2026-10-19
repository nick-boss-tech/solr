# Skip record: SOLR-16792

- Ticket: SOLR-16792, "Solr CoreAdmin API status throws a null-pointer exception when the index is empty/corrupted". JIRA status Open, resolution none, created 2023-05-08, last updated 2025-11-29, checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-16792-draft-open.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

After a collection build, the first error in the logs is `SolrCoreInitializationException: SolrCore '...' is not available due to init failure: Error opening new searcher`, and the CoreAdmin STATUS call throws a null-pointer exception on a core with an empty or corrupted index. The stack in the ticket is cut at `CoreAdminOperation`, which the ticket's own text says is from an earlier code layout. Comment: epugh (2025-11-29) asks whether it still happens and for reproduction steps. No reply from the reporter is recorded.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `solr/core/src/java/org/apache/solr/handler/admin/api/CoreStatus.java:118-160` (the STATUS path that the v2 and v1 CoreAdmin status calls use): a core that is loading is reported as loading; a not-loaded core is filled from its descriptor only; a loaded core is opened with `cores.getCore(cname)`, and only a non-null core reaches the unguarded dereferences.
- The unguarded dereferences on that branch: `core.getCoreDescriptor().getCloudDescriptor().getLastPublished().toString()` and `core.getSolrConfig().getZnodeVersion()` (lines about 146-158).
- `solr/core/src/java/org/apache/solr/cloud/CloudDescriptor.java:41, 66-71, 137`: `lastPublished` defaults to `Replica.State.ACTIVE`; `setLastPublished` is called with a state at publish time (`ZkController.java:1917`, `state` is the publish state) and from `CloudDescriptor` reload (line 137) with the other descriptor's value, which is itself never null. No caller passes null.
- So at the pinned head, no code path found makes `lastPublished` null on a loaded core, and the NPE in the ticket has no path that this reading explains. The audit verdict of 2026-10-05 reads the same branch and reaches the same conclusion.

## History

- The ticket's stack names `CoreAdminOperation`, which the ticket says is pre-refactor. The refactor to `api/CoreStatus.java` was not traced on the fork.
- No commit on the fork names SOLR-16792 (empty `git log --grep` at the pinned head).

## Reproduction attempts

Not attempted. The maintainer's request (epugh, 2025-11-29) is for steps that reproduce the NPE, and the reporter has not replied. A reproduction on the pinned head would create a collection, corrupt or empty one replica's index on disk, load the cores, and call the CoreAdmin STATUS for that core, with the Solr log. That run is the missing piece. It is not run in this closeout.

## Gap analysis

The NPE is not explained by any path found at the pinned head. Two readings remain: the reporter's build is an earlier layout where the dereference was reachable, or a path exists that this reading did not follow (for example a core whose loaded state changes during the STATUS call). Only the reproduction separates these.

## Verdict justification

keep-open, not close-cannot-reproduce: a close verdict needs a reproduction attempt with a result on the pinned head, and none was run. A code-path argument alone is not enough for close-cannot-reproduce (the rubric says so). The keep-open minimum is met: the attempts (the code reads and the `lastPublished` writers) are named, and the missing piece (the reproduction the maintainer asked for) is named. If the reporter does not reply and the reproduction also fails on main, the next verdict is close-cannot-reproduce.

## Limits

This record does not run a reproduction. It does not check the pre-refactor `CoreAdminOperation` code. It does not check the corrupted-index behavior of the search open path beyond the STATUS call.
