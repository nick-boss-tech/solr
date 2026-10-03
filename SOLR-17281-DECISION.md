# SOLR-17281: decision needed (handoff, remove before any PR)

Branch head reviewed: `e5a5dc3ff93`. Nothing here was compiled or run.

## Status

Not ready. The one-line change in `ShardLeaderElectionContext#replicasWithHigherTermParticipated`
(`getLiveNodes().contains(node)` -> `replica.isActive(liveNodes)`) shrinks the set of replicas that can veto a
lower-term replica's election. A restarted replica is published `DOWN` until it wins its own election, so a live,
`DOWN`, higher-term replica (the rebooted old leader in the ticket) no longer blocks an empty replica; the empty
one waits out `leaderVoteWait` and then becomes leader. This is from code reading, not a reproduction.

## Decision

1. Revert the one-line change (leaves the branch empty), or replace it with a fix aimed at the real cause.
2. The root cause is not established. Ask the reporter for the election logs ("Can't become leader",
   "no other potential leader was found", "Potential data loss") and the shard's `terms.json`. Candidates: the
   `leaderVoteWait` timeout path, the `zkShardTerms.registered(...)` guard, or term/recovering state after a
   replica move.

## If a test is added

Assert that a live, `DOWN`, higher-term replica still vetoes, and that a replica on a node outside `liveNodes`
does not. Do not use the workspace's `testDownReplicaWithHigherTermDoesNotBlockElection`; it asserts the opposite.

Full review: `research/branch-reviews/round-6/SOLR-17281-review.md` in the workspace.
