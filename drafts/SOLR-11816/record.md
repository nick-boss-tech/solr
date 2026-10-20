# Skip record: SOLR-11816

- Ticket: SOLR-11816, "Rolling restarts (rf>1) cause latency spike". JIRA status read 2026-10-20T07:24:29Z (UTC): Open, resolution None, no fix versions, updated 2019-06-08.
- Verdict: keep-open (kept; re-read at the pinned head).
- Draft branch: solr-11816-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T07:24:29Z (UTC).

## Ticket reading

During rolling restarts with replication factor 2 or more, query latency rises about four times. Some shards are reported as leaderless. The reporter supplies a Docker, InfluxDB and Grafana reproduction (external repository, not in this tree) and says the leaderless state is still being reproduced. No comments.

## Code reading (pinned head)

- Not done line by line for this record: the ticket names no class or method, only a latency symptom and a leaderless state. The leader-election and recovery paths that a leaderless shard would go through were checked for the sibling tickets in this batch (ShardLeaderElectionContext.java, LeaderElector.java, Overseer.java; see the SOLR-12166 and SOLR-11932 records).
- evidence.py at the pinned head: no identifiers in backticks in the ticket; no test classes named.

## History

- evidence.py at the pinned head: no commits naming SOLR-11816.

## Reproduction attempts

Not attempted on this host. The reproduction is a Docker Compose stack with InfluxDB and Grafana plus an indexing and query load and rolling restarts. The pool's Linux hosts do not run that stack, and a latency measurement needs a timed run, not a pass or fail.

## Gap analysis

The symptom is measured latency, which needs the reporter's load setup to compare. The leaderless state would need a deterministic check at restart, which the reporter has not yet reproduced. Neither is a code-level defect that this pass can name.

## Verdict justification

keep-open meets the rubric: the attempts made are the ticket reading and the cross-reference to the leader-election paths; the missing piece is a timed reproduction with the reporter's load and a leaderless-shard check. close-cannot-reproduce is not supported, since no run was made. close-fixed and close-obsolete are not supported, since no fix or removed code is named.

## Limits

This record does not measure latency. It does not establish that the leaderless state occurs on the pinned head.
