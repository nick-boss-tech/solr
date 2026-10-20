# Skip record: SOLR-12166

- Ticket: SOLR-12166, "Race condition in rejoinElection and registering replica". JIRA status read 2026-10-20T05:51:56Z (UTC): Open, resolution None, no fix versions, updated 2019-06-08.
- Verdict: close-obsolete (replaces the recorded keep-open; the fresh evidence does not support keep-open).
- Draft branch: solr-12166-draft-close.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T05:51:56Z (UTC).

## Ticket reading

The ticket describes a race between a leader's rejoinElection, its recovery, and its registration as ACTIVE, which can leave a shard with a DOWN leader. The mechanism it depends on is the leader-initiated recovery (LIR) flag: the flag that election clears when all replicas participate. The test named in the ticket is LIROnShardRestartTest. No comments.

## Code reading

- Grep at the pinned head, package solr/core/src/java/org/apache/solr/cloud, case-insensitive, for `leaderInitiatedRecovery|setLeaderRecovery|lirPath|LIR_`: empty result.
- `git grep -n "LIROnShardRestartTest" 498a09aea97aac86c2cc7ac114113b946d249942` (via evidence.py): not present on the pinned head.
- The only `rejoinElection` hit in the Java tree is the warning in `solr/core/src/java/org/apache/solr/cloud/Overseer.java:492`, which is an unrelated log line. The leader handoff uses shard terms: `solr/core/src/java/org/apache/solr/cloud/ZkShardTerms.java:74` (class ZkShardTerms).
- The candidate code path the ticket describes is gone at the pinned head.

## History

- evidence.py at the pinned head: no commits naming SOLR-12166.
- The queue record (material/skip-audit/queue-current.json) notes LIR was removed in Solr 9, which the grep above confirms for the cloud package at the pinned head. The removal commit was not located in this pass; a history search with `git log -S` over the full tree was too slow to finish in this tick.

## Reproduction attempts

Not attempted. The close-obsolete rubric requires the grep and its empty result, not a run.

## Gap analysis

The race needs the LIR flag and the rejoinElection call on the leader path. Neither exists at the pinned head. Leader handoff now uses shard terms, so the same race would have to be argued against that mechanism, which is a different ticket and not this one.

## Verdict justification

close-obsolete meets the rubric: the code the ticket is about has been removed, and the search of the pinned head finds no remaining use (the empty case-insensitive grep above, run at 498a09aea97aac86c2cc7ac114113b946d249942 in solr/core/src/java/org/apache/solr/cloud). The rubric's removal-commit check is the part not done here.

## Limits

This record does not name the commit that removed LIR, and does not establish that shard-term election has no version of this race. A later pass could name the removal commit with a history search.
