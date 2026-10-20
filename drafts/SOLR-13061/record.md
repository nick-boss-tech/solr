# Skip record: SOLR-13061

- Ticket: SOLR-13061, Solr replica remaining down status when hitting the maxQueueSize as 20000 after Solr servers restarted.
- Verdict: keep-open.
- Draft branch: solr-13061-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 16 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-12-12, updated 2019-06-08, no fix versions, no links. The report: a 6-node cluster with 1000 collections restarts; the Overseer state-update queue reaches the maximum of 20000 and throws `IllegalStateException: queue is full`, and no exception handler is defined where it is thrown, so replicas remain down. Two comments: 2019-02-25 (A Fat Horse) says the queue limit was made configurable in SOLR-11443; 2019-04-15 (janhoy) asks whether the ticket can close.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/cloud/ZkDistributedQueue.java:278-292`: when the queue is at capacity, `offer` throws `new IllegalStateException("queue is full")` (line 286).
- `solr/core/src/java/org/apache/solr/cloud/Overseer.java:143-144`: the limit is `Integer.getInteger("solr.OverseerStateUpdateMaxQueueSize", 20000)`, so SOLR-11443's change made the limit configurable through a system property, which is the reporter's point.
- The callers of the state-update queue's `offer` at the pin: `Overseer.java:1048` and `:1073`, `ShardLeaderElectionContext.java:157-158`, and `ZkController.java:1936`. In the lines read, none of these has a catch for `IllegalStateException` (the `ZkController` call sits in a try with a `finally` that only clears the logging context; the enclosing method was not read in full).
- SOLR-11443 is an ancestor of the pin (`9543e85460`, "Remove the usage of workqueue for Overseer", and `9fac59ef55`); it changed the Overseer queue handling and the limit, not the caller handling of the queue-full exception.

## History

- `9543e85460` and `9fac59ef55` (SOLR-11443): ancestors of the pin; configurable limit, no change to the queue-full handling in the callers read.
- No commit at the pin names SOLR-13061 (the evidence tool lists none).

## Reproduction attempts

Not run. This pass ran no builds or tests. The missing piece is a run of a restart with a queue at capacity, showing whether the replicas stay down after the exception and which caller leaves them there.

## Gap analysis

- The reporter's workaround (a larger limit) is in the code; the question of the missing handler is not answered by it.
- The enclosing methods of the three callers were not read in full, so a caller-level catch may exist outside the lines read.
- The ticket asks for a handler and a fix shape; neither is recorded.

## Verdict justification

keep-open: the queue-full exception is present, the limit is configurable, and the callers read do not catch it, but whether replicas stay down after the exception, and which caller is responsible, has not been run or fully traced. close-fixed does not fit (the configurable limit is not the handling the ticket asks for, and no commit names the ticket). close-obsolete does not fit (the code is present).

Missing for this verdict: a run of a restart with a full state-update queue at the pin, and the full read of the enclosing methods of the three callers.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
