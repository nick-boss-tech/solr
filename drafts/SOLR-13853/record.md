# Skip record: SOLR-13853

- Ticket: SOLR-13853, SolrCloud >> Collections API >> Shards state does not correctly reflect underlying replicas state. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-10-17, updated 2020-01-21, no fix versions, no links, no comments.
- Verdict: keep-open.
- Draft branch: solr-13853-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read run at this head (detached worktree).
- Source: remediation redo of batch 05, item 16, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (round audit-1, 2026-10-04, deferred) cited only the ticket title; the code path is read here.

## Ticket reading

During a rolling upgrade, a shard shows state "active" in CLUSTERSTATUS while both of its replicas are recovering or down. The reporter expects the shard state to reflect its replicas.

## Code reading at the pinned head

- solr/core/src/java/org/apache/solr/cloud/overseer/SliceMutator.java:211-222: the Overseer's `updateShardState` writes the shard state from the message (`props.put(ZkStateReader.STATE_PROP, message.getStr(key))`, :218), with a timestamp (:219-223). The shard state is an explicit stored field.
- SliceMutator.java:212: the ACTIVE case clears the parent markers (`PARENT`, `shard_parent_node`, `shard_parent_zk_session`). That is the split-lifecycle use of the field.
- solr/core/src/java/org/apache/solr/cloud/overseer/ReplicaMutator.java:613: `propMap.put(sliceName, Slice.State.ACTIVE.toString())`, one of the places that sets a slice to active. Other places are split and restore (SplitShardCmd.java:729, :996, :1000; RestoreCmd.java:517).
- `git grep -n "updateShardState\|STATE_PROP" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/core/src/java/org/apache/solr/cloud/overseer/SliceMutator.java` shows that the replica-state updates in the same class do not recompute the slice state from the replicas: the slice state is a stored value changed by the operations listed above.

So the shard state is an independent field set by collection operations, not a function of replica health. That matches the reporter's observation (shard active, replicas down). Whether the shard state should follow the replicas is a design choice, not a defect shown by reading.

## History

- No commit names SOLR-13853. The history of ReplicaMutator.java:613 and SliceMutator.java:211-222 was not traced in this pass.

## Reproduction attempts

Not run. A reproduction needs a SolrCloud collection with a shard whose replicas are taken down, then CLUSTERSTATUS. This pass did not start a cluster.

## Gap analysis

The mechanism is in the code: shard state is stored and set by operations, and replica state changes do not update it. Whether a shard should be "active" with no active replica is a maintainer design decision about the meaning of shard state, which the ticket raises.

## Verdict justification

keep-open: the attempts made are the code reads and greps above at the pinned head, the Jira read, and the mechanism check. The missing piece is a maintainer decision on what shard state means relative to replica state, and a cluster run that shows the state after a replica outage. close-fixed and close-obsolete are not supported: nothing on the pin changes the behavior. pr-needed is not supported without the decision.

Missing for a closing or PR verdict: the decision on shard-state semantics, and the run output.

## Limits

This record does not reproduce the state. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
