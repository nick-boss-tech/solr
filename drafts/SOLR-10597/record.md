# Skip record: SOLR-10597

- Ticket: SOLR-10597, "Overseer election endless loop on NodeExists /overseer_elect/leader". JIRA status read 2026-10-20T12:10:38Z (UTC): Open, resolution None, updated 2019-06-08.
- Verdict: keep-open (kept; re-read at the pinned head).
- Draft branch: solr-10597-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T12:10:38Z (UTC).

## Ticket reading

A five-node cluster, with DNS names changing under Docker, reached a state where every node believed it was the overseer leader and failed to create `/overseer_elect/leader` with NodeExists, logging without end. A full cluster restart cleared it. No comments.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/cloud/LeaderElector.java:130-133`: on `NodeExistsException` when the node thinks it is the leader, the code logs an error and calls `retryElection(context, false)` (returns from the method). This is the retry the ticket describes.
- `solr/core/src/java/org/apache/solr/cloud/Overseer.java:457-470`: the overseer reads `/overseer_elect/leader`, and returns on NoNode or an error, so it does not create the node itself.
- Whether `retryElection` can loop without a bound under a stale-leader ZK state was not traced to the end in this pass. The loop in the ticket is a log flood, so the bound matters.

## History

- evidence.py at the pinned head: no commits naming SOLR-10597.

## Reproduction attempts

Not run. The precondition (a stale leader node with each node seeing itself as leader) needs a ZooKeeper setup with stale sessions, which a focused test does not produce by default. The missing pieces are a reproduction of the stale state and a trace of `retryElection` to its exit.

## Gap analysis

The retry path exists at the pinned head. Whether it can recur without bound is not settled by reading alone, and the ticket's cluster state is not reproduced.

## Verdict justification

keep-open meets the rubric: attempts made are the code-path reading (LeaderElector.java:130-133, Overseer.java:457-470); the missing piece is a reproduction of the stale-leader state and a trace of the retry bound. close-fixed, close-obsolete and close-cannot-reproduce are not supported.

## Limits

This record does not establish the loop. It does not check ZooKeeper-side session handling.
