# Skip record: SOLR-9329

- Ticket: SOLR-9329, "[SOLR][5.5.1] ReplicateAfter optimize is not working". JIRA status read 2026-10-20T15:09:16Z (UTC): Open, resolution None, updated 2019-06-08.
- Verdict: keep-open (kept; re-read at the pinned head).
- Draft branch: solr-9329-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T15:09:16Z (UTC).

## Ticket reading

With `replicateAfter=optimize`, the master reports the last committed index, not the last optimized one, after a restart; the optimized version reappears only after the next optimize. The reporter is on 5.5.1 and asks about it. Comments: a possible fix in 5.5.2 (SOLR-9036), and a request for steps to reproduce.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/handler/ReplicationHandler.java:1354`: `replicateOnOptimize` is set from the configured `replicateAfter` list.
- `ReplicationHandler.java:1374-1376`: when `replicateOnOptimize` or `backupOnOptimize` is set, an optimize callback is registered (`registerOptimizeCallback`). That is the trigger the ticket depends on.
- The version that the `indexversion` command reports after a restart (last commit versus last optimize) was not traced in this pass; the commented-out check at line 1254 is the only other use of the flag.

## History

- evidence.py at the pinned head: no commits naming SOLR-9329. The 5.5.2 fix in SOLR-9036 is not located in this tree.

## Reproduction attempts

Not run. A run would configure a master with `replicateAfter=optimize`, optimize, restart the master, and call `command=indexversion` before and after the next optimize.

## Gap analysis

The optimize trigger exists at the pinned head. Whether the reported version after restart is the last optimized one or the last commit is the open question, and the run answers it.

## Verdict justification

keep-open meets the rubric: attempts made are the code-path reading of the trigger and the version reporting; the missing piece is the restart run. close-fixed is not supported: no fix commit is named. close-obsolete is not supported: the trigger is present.

## Limits

This record does not run the restart case. It does not check SOLR-9036.
