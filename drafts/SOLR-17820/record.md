# Skip record: SOLR-17820

- Ticket: SOLR-17820, "Shard and Collection health is GREEN with down replicas". JIRA status read 2026-10-19 (UTC): Resolved, resolution Fixed, fix versions 10.2. Fix commit 7e3b17502194 (Apache main, 2026-10-08, "SOLR-17820: Fix CLUSTERSTATUS health reporting GREEN with down replicas (#5046)"), with backports to branch_10x (6b2edf4525) and branch_10_1 (a3603260ea).
- Verdict: pr-needed. The verdict changed from keep-open in the earlier audit (see Gap analysis).
- Draft branch: solr-17820-draft-pr (replaces solr-17820-draft-open, whose record reads "not reproducible").
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream fix 7e3b17502194 is not an ancestor of this head.
- Checked by: vm4, 2026-10-19 13:05-13:09 UTC.

## Ticket reading

The ticket says CLUSTERSTATUS reports a shard as GREEN when one replica is down. Its example lists five replicas with one in state "down". The reporter cites the cluster-node-management guide, which says the shard should be YELLOW. There are five comments: the first (2026-10-06) is the reporter finding it while working on SOLR-15395; the rest are jira-bot commit notices for the upstream fix. No reproduction steps are given.

## Code reading

- `solr/core/src/java/org/apache/solr/handler/admin/ClusterStatus.java:70-82`, `Health.calcShardHealth`: with a leader, GREEN only when the active fraction is exactly 1.0, otherwise YELLOW, ORANGE or RED. Read as written, a shard with a leader and four of five active replicas gives YELLOW. This is the part the earlier audit relied on.
- `ClusterStatus.java:300-334`, `postProcessCollectionJSON`: the active count comes from `r.get("state")` equal to `ACTIVE`, and the shard health is computed from it at line 329.
- `ClusterStatus.java:245-265`, `getCollectionStatus`: calls `postProcessCollectionJSON` (lines 252 and 265) on the collection JSON it is given.
- `ClusterStatus.java:278-296`, `crossCheckReplicaStateWithLiveNodes`: replicas whose state is not DOWN but whose node is not in the live node list are set to DOWN in the map.
- `ClusterStatus.java:358`: `collectionStatus = getCollectionStatus(docCollection, name, shards);` runs the health computation.
- `ClusterStatus.java:375`: `crossCheckReplicaStateWithLiveNodes(liveNodes, collectionStatus);` runs after the health values were already written at line 358.

What this shows at the pinned head: the live-node cross-check runs after the health is computed, so a replica whose state.json entry says active but whose node is no longer live is counted as active when health is computed. A shard with a leader and all its replicas listed as active gives GREEN even when some of those nodes are down. The upstream fix moves the cross-check ahead of `getCollectionStatus` (commit 7e3b17502194, `ClusterStatus.java` lines 408-413 in that tree), and adds `ClusterStatusTest` coverage.

Grep of `ClusterStatus.java` at the pinned head for the two functions: `getCollectionStatus(` is defined at line 245 and called at 358; `crossCheckReplicaStateWithLiveNodes(` is defined at 278 and called at 375 only. The evidence tool lists no commit on the pinned head naming SOLR-17820.

## History

- 7e3b17502194, 2026-10-08, "SOLR-17820: Fix CLUSTERSTATUS health reporting GREEN with down replicas (#5046)" (Eric Pugh). Upstream fix: moves `crossCheckReplicaStateWithLiveNodes` before `getCollectionStatus`, and adds a 69-line test to `ClusterStatusTest`. Not an ancestor of the pinned head (checked with `git merge-base --is-ancestor`).
- 6b2edf4525 (branch_10x) and a3603260ea (branch_10_1): cherry-picks of 7e3b175021. Upstream only.
- No commit on the pinned head names SOLR-17820 (`git log --grep SOLR-17820 498a09aea97...` is empty).

## Reproduction attempts

Not attempted. The verdict rests on the code path at the pinned head. A reproduction needs a multi-node cluster or a test harness that puts a replica in state.json as active on a node that is not live. The upstream `ClusterStatusTest` addition is the check to run for the PR; it was not run in this pass, which is review-only.

## Gap analysis

The earlier audit read `calcShardHealth` alone and concluded that one down replica of five gives YELLOW. That holds for the example as written, where the down replica's state is "down". It misses the path that matters: replicas still marked active in state.json on a node that is not live. At the pinned head, health is computed before the live-node check, so those replicas count as active and the shard can read GREEN.

Uncertainty that remains: the ticket's example shows the replica in state "down", so it is not clear which path the reporter hit. This record does not establish that the reporter's cluster was in the dead-node state. It establishes that the pinned head computes health in the wrong order for that state, and that the upstream fix changes that order.

## Verdict justification

pr-needed meets the rubric: the code path is read with file and line (`ClusterStatus.java:358` before `:375`, and `:329` for the health), the cause is named, and a change is needed on the fork's main. The change exists upstream as 7e3b17502194; the PR would bring that commit (or an equivalent) onto the fork's main, with its `ClusterStatusTest` addition.

Not met: the rubric's close-fixed needs the fixing commit to be an ancestor of the pinned head. It is not. So the verdict is not close-fixed, even though upstream JIRA says Fixed.

## Limits

This record does not reproduce the bug and does not run `ClusterStatusTest`. It does not say whether the reporter's cluster hit the dead-node path. It does not change the upstream JIRA status, and it does not decide whether the fork should take the upstream fix or wait for a sync.
