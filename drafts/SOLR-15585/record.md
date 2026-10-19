# Skip record: SOLR-15585

- Ticket: SOLR-15585, "Graceful shutdown can cause data loss with PULL replicas". JIRA status Open, resolution none, fix versions none, created 2021-08-16, updated 2022-06-02. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-15585-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-15585-vm2.md).

## Ticket reading

With TLOG leader replicas and PULL replicas, a clean shutdown of a node holding leaders can finish before the PULL replicas have fetched the latest committed segments. The ticket suggests forcing a replication from the leader's node before shutdown. Four comments (2021-08-18 to 2022-06-02): a maintainer asks whether all TLOG replicas must be offline for the loss to happen; the reporter says yes (one TLOG replica claims leadership otherwise, and PULL replicas then fetch from it); a maintainer asks for the timeline and notes that a long PULL poll interval would make the loss plausible; a 2022 comment asks whether the loss counts as data loss while the TLOG replicas are down.

## Code reading

- solr/core/src/java/org/apache/solr/cloud/ReplicateFromLeader.java:83-89 (origin/main 498a09aea97): the follower replication configuration takes its poll interval from determinePollInterval(uinfo).
- ReplicateFromLeader.java:140-145: determinePollInterval starts from a default of "00:00:03" (three seconds) and then uses the update handler's commit settings (autoCommit max time, soft commit time, openSearcher, and commitPollInterval). So the PULL replica's poll interval, and therefore the window in which committed segments can be missing on it, follows the commit configuration.
- The shutdown path (which does not wait for PULL replicas to catch up) was not read in this pass.

Commands at the pinned head:
- git show origin/main:solr/core/src/java/org/apache/solr/cloud/ReplicateFromLeader.java | sed -n 83,89p and 140,145.
- git grep -n 'determinePollInterval\|pollInterval' origin/main -- solr/core/src/java/org/apache/solr/cloud/ReplicateFromLeader.java.

## History

- Commits on origin/main naming SOLR-15585: none.
- The history of ReplicateFromLeader's poll interval logic was not traced in this pass.

## Reproduction attempts

Not attempted. The loss needs a multi-node cluster with TLOG and PULL replicas, a clean shutdown of the leader node, and a check of the PULL replica's segments afterwards. That is a cluster test with timing, which was not set up in this pass.

## Gap analysis

The poll interval sets an upper bound on how far a PULL replica can lag a leader, and the shutdown path decides whether the leader waits. The shutdown path, and whether it waits for PULL replicas, are not read here. Whether the data loss is real on this code, or is the expected behaviour of a PULL replica that has not polled yet, is the question the comments raise, and it is not settled by the code read.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. The missing pieces are (1) the shutdown path, read at the pinned head, to see whether it waits for PULL replicas, and (2) a cluster reproduction with a TLOG leader shut down cleanly before a PULL replica polls, counting the documents that are missing. A close verdict needs the expected behaviour documented or a fix; a pr-needed verdict needs the shutdown step that loses the data.

## Limits

This record does not reproduce the loss. It does not read the shutdown path. It does not settle whether the loss is expected for a PULL replica with a long poll interval.
