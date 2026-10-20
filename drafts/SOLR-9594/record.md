# Skip record: SOLR-9594

- Ticket: SOLR-9594, "Query requests to one shard collections can switch to two-phase distributed search if they hit a node in recovery". JIRA status read 2026-10-20T13:01:37Z (UTC): Open, resolution None, fix versions 6.7 and 7.0 set, updated 2019-06-08.
- Verdict: keep-open (kept; re-read at the pinned head). The recorded note "fixed in 6.7/7.0" rests on the fix-version field only; the code path still shows the behavior.
- Draft branch: solr-9594-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T13:01:37Z (UTC).

## Ticket reading

On a one-shard collection, a query that lands on a replica that is not active skips the local short-circuit and becomes the aggregator of a two-phase distributed search, which is wrong for components that are not distributed-safe. The reporter proposes forwarding the request to another node, or calling with distrib=false. One 2017 comment moved it to 6.5.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/handler/component/HttpShardHandler.java:575-600` (`canShortCircuit`): the short-circuit requires `cloudDescriptor.getLastPublished() == Replica.State.ACTIVE` (line 586) for the local replica. When the local replica is not active, the method returns false and the request is distributed with this node as aggregator. The condition matches the ticket.
- `SearchHandler.java:427-542`: `shortCircuitedResults` is reached only when the shard handler takes the short-circuit path.

## History

- evidence.py at the pinned head: no commits naming SOLR-9594. The fix version in JIRA is not matched by a commit in this tree.

## Reproduction attempts

Not run. A reproduction needs a one-shard collection with two replicas, one put into recovery, and a query sent to it; that is a SolrCloud test, not a focused unit test, and was not built in this pass.

## Gap analysis

The code path produces the reported behavior at the pinned head. The choice between forwarding and distrib=false is a design decision (the ticket lists both), so a fix is not a single-line change.

## Verdict justification

keep-open meets the rubric: attempts made are the code-path reading (HttpShardHandler.java:575-600); the missing pieces are a SolrCloud reproduction and a design choice between the two options. close-fixed is not supported: the fix-version field is not a fix commit, and the condition is present. close-obsolete is not supported: the path is present.

## Limits

This record does not run the SolrCloud case. It does not choose the fix design.
