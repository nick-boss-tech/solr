# Skip record: SOLR-17509

- Ticket: SOLR-17509, "Collection Backup: Invalid status request". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2024-10-21, updated 2024-10-25, no fix version, no comments.
- Verdict: keep-open. Unchanged from the earlier record.
- Draft branch: solr-17509-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 13:22-14:10 UTC.

## Ticket reading

On Solr 9.7.0, a backup of a 3 TB collection ran after a full index. The backup status lookup returned `notfound` for request id 1-...20241015505368482655751 and failed after six tries. The S3 location has the index files but no `backup.properties`. Small collections did not hit this. The ticket has no comments.

## Code reading

- `solr/core/src/java/org/apache/solr/cloud/api/collections/CollectionHandlingUtils.java:591-613`: on `notfound` the status request is retried up to five times with a one-second sleep, then throws `BAD_REQUEST` "Invalid status request for requestId ... Retried N times". The error in the ticket is this path.
- `solr/core/src/java/org/apache/solr/cloud/Overseer.java:905-911`, `getCompletedMap`: the completed-status store for collection API tasks is a `SizeLimitedDistributedMap` with `NUM_RESPONSES_TO_STORE` (`Overseer.java:146`, value 10000). It is limited by count, not by time. Once more than 10,000 entries are stored, the oldest completed status is removed (the removal callback at `Overseer.java:910` drops the async id).
- Consequence at the pinned head: a backup's completed status can be evicted by later completions, and a status poll after that returns `notfound`. The six tries in the ticket fit that path, but only if more than 10,000 collection tasks completed between the backup and the poll. Whether that happened on the reporter's cluster is not in the ticket.

## History

- SOLR-17160 (`d3b4c2e1ae`, 2024-07-15, "Core admin async ID status, 10k limit and time expire (#2304)") is an ancestor of the pinned head, but it changes `CoreAdminHandler` and `GetNodeCommandStatus` (core admin async ids), not the collection API map. The queue note that ties this ticket to SOLR-17160 is therefore not supported by the code: the collection path does not use that change.
- SOLR-17462: no commit on the pinned head names it (`git log --grep`).
- No commit on the pinned head names SOLR-17509 (evidence tool: no hits).

## Reproduction attempts

Not attempted. A reproduction needs a backup run on a collection with a large number of completed collection tasks, or a test that fills the completed map past its limit and then polls the first id. This pass is review-only and runs no builds.

## Gap analysis

The status error is produced correctly by the code for the case where the id is no longer in the completed map. The ticket does not show that the backup had finished, and the missing `backup.properties` could mean the backup did not finish (in which case `notfound` is a different problem from eviction). Two explanations remain open: eviction after more than 10,000 later collection tasks, and a backup that never completed. The ticket does not separate them.

Missing piece for a verdict change: the number of collection API tasks completed on the reporter's cluster between the backup and the status poll, and whether the backup reached its final step (the `backup.properties` write). With those, the record can say whether this is eviction, a failed backup, or a bug.

## Verdict justification

keep-open meets the rubric: the code path is read with file and line, the audit's duplicate link (SOLR-17160) is shown not to apply to the collection path, and the missing piece is named. A close needs either a fix on main or a demonstrated non-issue, and neither is shown. A pr-needed needs a line that causes the failure; the eviction limit is by design, so the failure is a consequence of the limit, not a defect the code shows.

## Limits

This record does not reproduce the failure and does not say which explanation holds for the reporter. It does not change the queue note's claim about SOLR-17160 in the queue file; it only states that the code does not support it. It does not post anything to JIRA or GitHub.
